package app;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Life Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(420, 280);
            frame.setLocationRelativeTo(null);
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(new Color(245, 245, 245));
            JLabel title = new JLabel("Life Management System");
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            title.setFont(new Font("Arial", Font.BOLD, 26));
            title.setForeground(new Color(40, 40, 40));
            panel.add(Box.createVerticalStrut(25));
            panel.add(title);
            panel.add(Box.createVerticalStrut(35));
            JButton loginButton = new JButton("Login");
            loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            loginButton.setPreferredSize(new Dimension(180, 45));
            loginButton.setMaximumSize(new Dimension(180, 45));
            loginButton.setBackground(new Color(52, 152, 219));
            loginButton.setForeground(Color.WHITE);
            loginButton.setFont(new Font("Arial", Font.BOLD, 16));
            loginButton.setBorder(new LineBorder(Color.DARK_GRAY, 2, true));
            loginButton.setFocusPainted(false);
            JButton registerButton = new JButton("Register");
            registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            registerButton.setPreferredSize(new Dimension(180, 45));
            registerButton.setMaximumSize(new Dimension(180, 45));
            registerButton.setBackground(new Color(46, 204, 113));
            registerButton.setForeground(Color.WHITE);
            registerButton.setFont(new Font("Arial", Font.BOLD, 16));
            registerButton.setBorder(new LineBorder(Color.DARK_GRAY, 2, true));
            registerButton.setFocusPainted(false);
            panel.add(loginButton);
            panel.add(Box.createVerticalStrut(15));
            panel.add(registerButton);
            frame.add(panel);
            loginButton.addActionListener(e -> {
                frame.dispose();
                new LoginForm();
            });
            registerButton.addActionListener(e -> {
                frame.dispose();
                new RegisterForm();
            });
            frame.setVisible(true);
        });
    }
}
