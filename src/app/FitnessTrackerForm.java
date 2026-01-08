package app;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class FitnessTrackerForm extends JFrame {

    private JPanel mainPanel;
    private JTextField exerciseField;
    private JTextField durationField;
    private JTextField caloriesField;
    private JButton addButton;
    private JButton deleteButton;
    private JList<String> fitnessList;

    private DefaultListModel<String> listModel;
    private List<ObjectId> fitnessIds = new ArrayList<>();

    private MongoCollection<Document> fitnessCollection;
    private String currentUser;

    public FitnessTrackerForm(String username) {

        this.currentUser = username;
        MongoDatabase db = Database.getDatabase();
        fitnessCollection = db.getCollection("fitness");
        setTitle("Fitness Tracker - " + username);
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 550);
        setLocationRelativeTo(null);
        listModel = new DefaultListModel<>();
        fitnessList.setModel(listModel);
        loadFitness();
        setupButtons();
        setupListClick();

        setVisible(true);
    }

    private void loadFitness() {
        listModel.clear();
        fitnessIds.clear();
        MongoCursor<Document> cursor =
                fitnessCollection.find(new Document("user", currentUser)).iterator();

        while (cursor.hasNext()) {
            Document doc = cursor.next();

            ObjectId id = doc.getObjectId("_id");
            String exercise = doc.getString("exercise");
            int duration = doc.getInteger("duration");
            int calories = doc.getInteger("calories");

            fitnessIds.add(id);
            listModel.addElement(
                    exercise + " | " + duration + " min | " + calories + " kcal"
            );
        }
    }

    private void setupButtons() {

        addButton.addActionListener(e -> {

            String exercise = exerciseField.getText().trim();
            int duration;
            int calories;
            try {
                duration = Integer.parseInt(durationField.getText());
                calories = Integer.parseInt(caloriesField.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numbers!");
                return;
            }
            if (exercise.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Exercise name required!");
                return;
            }
            Document doc = new Document("user", currentUser)
                    .append("exercise", exercise)
                    .append("duration", duration)
                    .append("calories", calories);
            fitnessCollection.insertOne(doc);
            clearFields();
            loadFitness();
        });
        deleteButton.addActionListener(e -> {

            int index = fitnessList.getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Select training to delete!");
                return;
            }
            ObjectId id = fitnessIds.get(index);
            fitnessCollection.deleteOne(new Document("_id", id));

            clearFields();
            loadFitness();
        });
    }
    private void setupListClick() {

        fitnessList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = fitnessList.getSelectedIndex();
                if (index == -1) return;
                String selected = fitnessList.getSelectedValue();
                String[] parts = selected.split("\\|");
                exerciseField.setText(parts[0].trim());
                durationField.setText(parts[1].replace("min", "").trim());
                caloriesField.setText(parts[2].replace("kcal", "").trim());
            }
        });
    }
    private void clearFields() {
        exerciseField.setText("");
        durationField.setText("");
        caloriesField.setText("");
    }
}
