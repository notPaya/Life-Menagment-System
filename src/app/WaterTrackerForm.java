package app;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class WaterTrackerForm {
    private JPanel mainPanel;
    private JTextArea waterTextArea;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton analyticsButton;

    private String currentUser;
    private JFrame frame;

    public WaterTrackerForm(String username) {
        this.currentUser = username;

        frame = new JFrame("Water Tracker");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // ➕ Add dugme
        addButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Enter water intake (litres):");
            try {
                double liters = Double.parseDouble(input);
                MongoCollection<Document> collection = Database.getDatabase().getCollection("water");
                Document doc = new Document("username", currentUser)
                        .append("water", liters);
                collection.insertOne(doc);
                updateDisplay();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number!");
            }
        });

        // ✏️ Edit dugme
        editButton.addActionListener(e -> {
            MongoCollection<Document> collection = Database.getDatabase().getCollection("water");
            List<Document> userWater = collection.find(Filters.eq("username", currentUser)).into(new ArrayList<>());

            if (userWater.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No water entries to edit!");
                return;
            }

            String[] entries = userWater.stream()
                    .map(doc -> {
                        Object waterObj = doc.get("water");
                        double water = (waterObj instanceof Number) ? ((Number) waterObj).doubleValue() : 0;
                        return water + " L";
                    })
                    .toArray(String[]::new);

            String selected = (String) JOptionPane.showInputDialog(frame,
                    "Select entry to edit:", "Edit Water Intake",
                    JOptionPane.PLAIN_MESSAGE, null, entries, entries[0]);

            if (selected != null) {
                String newValue = JOptionPane.showInputDialog(frame, "Enter new value (litres):");
                try {
                    double newLiters = Double.parseDouble(newValue);
                    int index = java.util.Arrays.asList(entries).indexOf(selected);
                    Document docToEdit = userWater.get(index);  // <-- POPRAVLJENO
                    collection.updateOne(Filters.eq("_id", docToEdit.getObjectId("_id")),
                            new Document("$set", new Document("water", newLiters)));
                    updateDisplay();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number!");
                }
            }
        });

        // ❌ Delete dugme
        deleteButton.addActionListener(e -> {
            MongoCollection<Document> collection = Database.getDatabase().getCollection("water");
            List<Document> userWater = collection.find(Filters.eq("username", currentUser)).into(new ArrayList<>());

            if (userWater.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No water entries to delete!");
                return;
            }

            String[] entries = userWater.stream()
                    .map(doc -> {
                        Object waterObj = doc.get("water");
                        double water = (waterObj instanceof Number) ? ((Number) waterObj).doubleValue() : 0;
                        return water + " L";
                    })
                    .toArray(String[]::new);

            String selected = (String) JOptionPane.showInputDialog(frame,
                    "Select entry to delete:", "Delete Water Intake",
                    JOptionPane.PLAIN_MESSAGE, null, entries, entries[0]);

            if (selected != null) {
                int index = java.util.Arrays.asList(entries).indexOf(selected);
                Document docToDelete = userWater.get(index);  // <-- POPRAVLJENO
                collection.deleteOne(Filters.eq("_id", docToDelete.getObjectId("_id")));
                updateDisplay();
            }
        });

        // 📊 Analytics dugme
        analyticsButton.addActionListener(e -> {
            MongoCollection<Document> collection = Database.getDatabase().getCollection("water");
            List<Document> userWater = collection.find(Filters.eq("username", currentUser)).into(new ArrayList<>());

            if (userWater.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No water entries yet!");
                return;
            }

            double sum = 0;
            for (Document doc : userWater) {
                Object waterObj = doc.get("water");
                double water = (waterObj instanceof Number) ? ((Number) waterObj).doubleValue() : 0;
                sum += water;
            }
            double avg = sum / userWater.size();

            JOptionPane.showMessageDialog(frame,
                    "Average water intake: " + String.format("%.2f", avg) + " L");
        });

        updateDisplay();
    }

    private void updateDisplay() {
        MongoCollection<Document> collection = Database.getDatabase().getCollection("water");
        List<Document> userWater = collection.find(Filters.eq("username", currentUser)).into(new ArrayList<>());

        waterTextArea.setText("");
        for (int i = 0; i < userWater.size(); i++) {
            Object waterObj = userWater.get(i).get("water");
            double water = (waterObj instanceof Number) ? ((Number) waterObj).doubleValue() : 0;
            waterTextArea.append("Day " + (i + 1) + ": " + water + " L\n");
        }
    }
}
