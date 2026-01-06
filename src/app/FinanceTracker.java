package app;

import javax.swing.*;

public class FinanceTracker extends JFrame {

    private JPanel mainPanel;
    private JTextField amount;
    private JTextField balance;
    private JComboBox category;
    private JComboBox transaction;
    private JButton saveButton;
    private JTable tableFinance;

    public FinanceTracker(String username) {

        setTitle("Finance Tracker - " + username);

        setContentPane(mainPanel);

        // Ako koristiš GUI Designer — bolje je pack()
        pack();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 🔥 OVO JE NEDOSTAJALO
        setVisible(true);
    }
}
