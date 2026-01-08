package app;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;

public class FoodTrackerForm extends JFrame {

    private JPanel mainPanel;
    private JTextField foodField;
    private JTextField proteinField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JList<String> foodList;

    private DefaultListModel<String> listModel;
    private MongoCollection<Document> foodCollection;
    private String currentUser;

    public FoodTrackerForm(String username) {

        this.currentUser = username;

        MongoDatabase db = Database.getDatabase();
        foodCollection = db.getCollection("food");

        setTitle("Food Tracker - " + username);
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        listModel = new DefaultListModel<>();
        foodList.setModel(listModel);

        loadFood();
        setupButtons();

        pack();
        setVisible(true);
    }

    private void setupButtons() {

        addButton.addActionListener(e -> {

            String food = foodField.getText().trim();
            String proteinText = proteinField.getText().trim();

            if (food.isEmpty() || proteinText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
                return;
            }

            try {
                double protein = Double.parseDouble(proteinText);

                Document doc = new Document("user", currentUser)
                        .append("food", food)
                        .append("protein", protein);

                foodCollection.insertOne(doc);

                foodField.setText("");
                proteinField.setText("");

                loadFood();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Protein must be a number!");
            }
        });

        editButton.addActionListener(e -> {

            int index = foodList.getSelectedIndex();
            if (index == -1) return;

            String selected = listModel.getElementAt(index);

            String food = foodField.getText().trim();
            String proteinText = proteinField.getText().trim();

            if (food.isEmpty() || proteinText.isEmpty()) return;

            double protein = Double.parseDouble(proteinText);

            foodCollection.deleteOne(
                    new Document("user", currentUser)
                            .append("display", selected)
            );

            Document updated = new Document("user", currentUser)
                    .append("food", food)
                    .append("protein", protein);

            foodCollection.insertOne(updated);

            loadFood();
        });
        deleteButton.addActionListener(e -> {

            int index = foodList.getSelectedIndex();
            if (index == -1) return;

            String selected = listModel.getElementAt(index);

            foodCollection.deleteOne(
                    new Document("user", currentUser)
                            .append("display", selected)
            );

            loadFood();
        });
    }

    private void loadFood() {

        listModel.clear();

        MongoCursor<Document> cursor =
                foodCollection.find(new Document("user", currentUser)).iterator();

        while (cursor.hasNext()) {
            Document d = cursor.next();
            String display = d.getString("food") +
                    " - " + d.getDouble("protein") + " g protein";
            d.append("display", display);
            listModel.addElement(display);
        }
    }
}
