package app;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class HabitTrackerForm {
    private JPanel mainPanel;
    private JTextArea textArea1;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton analyticsButton;

    private String currentUser;
    private JFrame frame;

    private MongoDatabase db;
    private MongoCollection<Document> habitCollection;

    public HabitTrackerForm(String username) {
        this.currentUser = username;
        db = Database.getDatabase();
        habitCollection = db.getCollection("habits");

        frame = new JFrame("Habit Tracker - " + username);
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        updateDisplay();

        addButton.addActionListener(e -> {
            String habit = JOptionPane.showInputDialog(frame, "Enter new habit:");
            if (habit != null && !habit.isEmpty()) {
                Document doc = new Document("user", currentUser)
                        .append("habit", habit);
                habitCollection.insertOne(doc);
                updateDisplay();
            }
        });
        editButton.addActionListener(e -> {
            List<Document> habits = habitCollection.find(new Document("user", currentUser)).into(new ArrayList<>());
            if (habits.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No habits to edit!");
                return;
            }
            String[] habitNames = habits.stream().map(d -> d.getString("habit")).toArray(String[]::new);
            String selected = (String) JOptionPane.showInputDialog(frame, "Select habit to edit:",
                    "Edit Habit", JOptionPane.QUESTION_MESSAGE, null, habitNames, habitNames[0]);

            if (selected != null) {
                String newName = JOptionPane.showInputDialog(frame, "New name:", selected);
                if (newName != null && !newName.isEmpty()) {
                    habitCollection.updateOne(new Document("user", currentUser).append("habit", selected),
                            new Document("$set", new Document("habit", newName)));
                    updateDisplay();
                }
            }
        });
        deleteButton.addActionListener(e -> {
            List<Document> habits = habitCollection.find(new Document("user", currentUser)).into(new ArrayList<>());
            if (habits.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No habits to delete!");
                return;
            }

            String[] habitNames = habits.stream().map(d -> d.getString("habit")).toArray(String[]::new);
            String selected = (String) JOptionPane.showInputDialog(frame, "Select habit to delete:",
                    "Delete Habit", JOptionPane.QUESTION_MESSAGE, null, habitNames, habitNames[0]);

            if (selected != null) {
                habitCollection.deleteOne(new Document("user", currentUser).append("habit", selected));
                updateDisplay();
            }
        });
        analyticsButton.addActionListener(e -> {
            List<Document> habits = habitCollection.find(new Document("user", currentUser)).into(new ArrayList<>());
            JOptionPane.showMessageDialog(frame, "You have " + habits.size() + " habits.");
        });
    }

    private void updateDisplay() {
        textArea1.setText("");
        List<Document> habits = habitCollection.find(new Document("user", currentUser)).into(new ArrayList<>());
        for (int i = 0; i < habits.size(); i++) {
            textArea1.append((i + 1) + ". " + habits.get(i).getString("habit") + "\n");
        }
    }
}
