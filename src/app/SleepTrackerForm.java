package app;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class SleepTrackerForm {
    private JPanel mainPanel;
    private JTextArea sleepTextArea;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton analyticsButton;

    private String currentUser;
    private JFrame frame;

    public SleepTrackerForm(String username) {
        this.currentUser = username;

        frame = new JFrame("Sleep Tracker");
        mainPanel = new JPanel(new BorderLayout());
        sleepTextArea = new JTextArea(15, 30);
        sleepTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(sleepTextArea);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        analyticsButton = new JButton("Analytics");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(analyticsButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add dugme
        addButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "How many hours did you sleep?");
            try {
                double hours = Double.parseDouble(input);
                MongoCollection<Document> collection = Database.getDatabase().getCollection("sleep");
                Document doc = new Document("username", currentUser)
                        .append("hours", hours);
                collection.insertOne(doc);
                updateDisplay();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number!");
            }
        });

        // Edit dugme
        editButton.addActionListener(e -> {
            MongoCollection<Document> collection = Database.getDatabase().getCollection("sleep");
            List<Document> userSleep = collection.find(Filters.eq("username", currentUser)).into(new ArrayList<>());

            if (userSleep.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No entries to edit!");
                return;
            }

            String[] entries = userSleep.stream()
                    .map(doc -> doc.getDouble("hours") + " hours")
                    .toArray(String[]::new);

            String selected = (String) JOptionPane.showInputDialog(frame,
                    "Select entry to edit:", "Edit Sleep",
                    JOptionPane.PLAIN_MESSAGE, null, entries, entries[0]);

            if (selected != null) {
                String newValueStr = JOptionPane.showInputDialog(frame, "Enter new hours:");
                try {
                    double newHours = Double.parseDouble(newValueStr);
                    Document docToEdit = userSleep.get(java.util.Arrays.asList(entries).indexOf(selected));
                    collection.updateOne(Filters.eq("_id", docToEdit.getObjectId("_id")),
                            new Document("$set", new Document("hours", newHours)));
                    updateDisplay();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid number!");
                }
            }
        });

        // Delete dugme
        deleteButton.addActionListener(e -> {
            MongoCollection<Document> collection = Database.getDatabase().getCollection("sleep");
            List<Document> userSleep = collection.find(Filters.eq("username", currentUser)).into(new ArrayList<>());

            if (userSleep.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No entries to delete!");
                return;
            }

            String[] entries = userSleep.stream()
                    .map(doc -> doc.getDouble("hours") + " hours")
                    .toArray(String[]::new);

            String selected = (String) JOptionPane.showInputDialog(frame,
                    "Select entry to delete:", "Delete Sleep",
                    JOptionPane.PLAIN_MESSAGE, null, entries, entries[0]);

            if (selected != null) {
                Document docToDelete = userSleep.get(java.util.Arrays.asList(entries).indexOf(selected));
                collection.deleteOne(Filters.eq("_id", docToDelete.getObjectId("_id")));
                updateDisplay();
            }
        });

        // Analytics dugme
        analyticsButton.addActionListener(e -> {
            MongoCollection<Document> collection = Database.getDatabase().getCollection("sleep");
            List<Document> userSleep = collection.find(Filters.eq("username", currentUser)).into(new ArrayList<>());

            if (userSleep.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No data yet!");
                return;
            }

            double sum = 0;
            for (Document doc : userSleep) {
                sum += doc.getDouble("hours");
            }
            double avg = sum / userSleep.size();

            JOptionPane.showMessageDialog(frame,
                    "Average sleep: " + String.format("%.2f", avg) + " hours");
        });

        updateDisplay();
    }

    private void updateDisplay() {
        MongoCollection<Document> collection = Database.getDatabase().getCollection("sleep");
        List<Document> userSleep = collection.find(Filters.eq("username", currentUser)).into(new ArrayList<>());

        sleepTextArea.setText("");
        for (int i = 0; i < userSleep.size(); i++) {
            sleepTextArea.append("Day " + (i + 1) + ": " + userSleep.get(i).getDouble("hours") + " hours\n");
        }
    }
}
