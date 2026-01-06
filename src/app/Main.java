package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // 1️⃣ prvo uključimo Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2️⃣ onda kreiramo GUI
        JFrame frame = new JFrame("Life Management System");
        frame.setContentPane(new MainForm().getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
