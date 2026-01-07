package app;

import com.mongodb.client.*;
import org.bson.Document;

import javax.swing.*;
import java.util.ArrayList;

public class StudyTrackerForm {

    private JPanel mainPanel;
    private JList<String> taskList;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    private String currentUser;
    private MongoCollection<Document> studyCollection;
    private DefaultListModel<String> listModel;
    private ArrayList<Document> tasks;

    public StudyTrackerForm(String username) {

        this.currentUser = username;

        MongoDatabase db = Database.getDatabase();
        studyCollection = db.getCollection("study");

        JFrame frame = new JFrame("Study Tracker");
        frame.setContentPane(mainPanel);
        frame.setSize(450, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        listModel = new DefaultListModel<>();
        taskList.setModel(listModel);

        loadTasks();

        addButton.addActionListener(e -> addTask());
        editButton.addActionListener(e -> editTask());
        deleteButton.addActionListener(e -> deleteTask());

        frame.setVisible(true);
    }

    private void loadTasks() {
        listModel.clear();
        tasks = new ArrayList<>();

        MongoCursor<Document> cursor =
                studyCollection.find(new Document("user", currentUser)).iterator();

        while (cursor.hasNext()) {
            Document doc = cursor.next();
            tasks.add(doc);
            listModel.addElement(doc.getString("title"));
        }
    }

    private void addTask() {

        String title = JOptionPane.showInputDialog("Enter study task:");

        if (title == null || title.isEmpty()) return;

        Document doc = new Document("user", currentUser)
                .append("title", title);

        studyCollection.insertOne(doc);
        loadTasks();
    }

    private void editTask() {

        int index = taskList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(null, "Select task to edit!");
            return;
        }

        String newTitle = JOptionPane.showInputDialog("Edit task:", tasks.get(index).getString("title"));

        if (newTitle == null || newTitle.isEmpty()) return;

        Document filter = new Document("_id", tasks.get(index).getObjectId("_id"));

        studyCollection.updateOne(filter, new Document("$set",
                new Document("title", newTitle)));

        loadTasks();
    }

    private void deleteTask() {

        int index = taskList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(null, "Select task to delete!");
            return;
        }

        Document filter = new Document("_id", tasks.get(index).getObjectId("_id"));

        studyCollection.deleteOne(filter);

        loadTasks();
    }
}
