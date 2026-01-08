package app;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class FoodTrackerForm extends JFrame {

    private JPanel mainPanel;
    private JTextField foodField;
    private JTextField proteinField;
    private JButton addButton;
    private JButton deleteButton;
    private JList<String> foodList;
    private DefaultListModel<String> listModel;
    private List<ObjectId> foodIds = new ArrayList<>();
    private MongoCollection<Document> foodCollection;
    private String currentUser;
    public FoodTrackerForm(String username) {

        this.currentUser = username;

        MongoDatabase db = Database.getDatabase();
        foodCollection = db.getCollection("food");

        setTitle("Food Tracker - " + username);
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);

        listModel = new DefaultListModel<>();
        foodList.setModel(listModel);

        loadFood();
        setupButtons();
        setupListClick();

        setVisible(true);
    }
    private void loadFood() {

        listModel.clear();
        foodIds.clear();

        MongoCursor<Document> cursor =
                foodCollection.find(new Document("user", currentUser)).iterator();

        while (cursor.hasNext()) {
            Document doc = cursor.next();

            ObjectId id = doc.getObjectId("_id");
            String food = doc.getString("food");
            double protein = doc.getDouble("protein");

            foodIds.add(id);
            listModel.addElement(food + " - " + protein + " g protein");
        }
    }
    private void setupButtons() {
        addButton.addActionListener(e -> {

            String food = foodField.getText().trim();
            double protein;

            try {
                protein = Double.parseDouble(proteinField.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid protein value!");
                return;
            }

            if (food.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Food name required!");
                return;
            }

            Document doc = new Document("user", currentUser)
                    .append("food", food)
                    .append("protein", protein);

            foodCollection.insertOne(doc);

            foodField.setText("");
            proteinField.setText("");

            loadFood();
        });
        deleteButton.addActionListener(e -> {

            int index = foodList.getSelectedIndex();

            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Select item to delete!");
                return;
            }

            ObjectId id = foodIds.get(index);

            foodCollection.deleteOne(new Document("_id", id));

            loadFood();
        });
    }
    private void setupListClick() {

        foodList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {

                int index = foodList.getSelectedIndex();

                if (index != -1) {
                    String text = foodList.getSelectedValue();
                    String[] parts = text.split(" - ");

                    foodField.setText(parts[0]);

                    if (parts.length > 1) {
                        String proteinText = parts[1].replace(" g protein", "");
                        proteinField.setText(proteinText);
                    }
                }
            }
        });
    }
}
