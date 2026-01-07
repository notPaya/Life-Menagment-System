package app;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class FinanceTracker extends JFrame {

    private JPanel mainPanel;
    private JTextField amount;
    private JTextField balance;
    private JComboBox transaction;
    private JComboBox category;
    private JButton saveButton;
    private JTable tableFinance;

    private String currentUser;
    private MongoCollection<Document> financeCollection;
    private DefaultTableModel tableModel;

    public FinanceTracker(String username) {

        this.currentUser = username;

        MongoDatabase db = Database.getDatabase();
        financeCollection = db.getCollection("finance");

        setTitle("Finance Tracker - " + username);
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setupTable();
        loadData();
        setupSaveButton();
        transaction.addItem("Choose");
        transaction.addItem("Revenues");
        transaction.addItem("Expenses");
        category.addItem("Choose");
        category.addItem("Food");
        category.addItem("Bills");
        category.addItem("Other");

        pack();
        setVisible(true);

    }

    private void setupTable() {

        tableModel = new DefaultTableModel(
                new Object[]{"Type", "Category", "Amount"}, 0
        );
        tableFinance.setModel(tableModel);
    }

    private void setupSaveButton() {

        saveButton.addActionListener(e -> {

            try {
                double value = Double.parseDouble(amount.getText());
                String type = transaction.getSelectedItem().toString();
                String cat = category.getSelectedItem().toString();

                Document doc = new Document("user", currentUser)
                        .append("type", type)
                        .append("category", cat)
                        .append("amount", value);

                financeCollection.insertOne(doc);

                loadData();
                amount.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid amount!");
            }
        });
    }

    private void loadData() {

        tableModel.setRowCount(0);

        MongoCursor<Document> cursor =
                financeCollection.find(new Document("user", currentUser)).iterator();

        double total = 0;

        while (cursor.hasNext()) {
            Document d = cursor.next();

            String type = d.getString("type");
            String cat = d.getString("category");
            double value = d.getDouble("amount");

            if (type.equals("Income")) total += value;
            else total -= value;

            Vector<Object> row = new Vector<>();
            row.add(type);
            row.add(cat);
            row.add(value);

            tableModel.addRow(row);
        }

        balance.setText(String.valueOf(total));
    }
}
