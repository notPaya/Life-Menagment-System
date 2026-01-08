package app;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Life Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 250);
            frame.setLocationRelativeTo(null);
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JLabel title = new JLabel("Life Management System");
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            title.setFont(new Font("Arial", Font.BOLD, 20));
            panel.add(Box.createVerticalStrut(20));
            panel.add(title);
            panel.add(Box.createVerticalStrut(25));
            JButton loginButton = new JButton("Login");
            loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            loginButton.setPreferredSize(new Dimension(120, 35));
            loginButton.setMaximumSize(new Dimension(120, 35));
            JButton registerButton = new JButton("Register");
            registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            registerButton.setPreferredSize(new Dimension(120, 35));
            registerButton.setMaximumSize(new Dimension(120, 35));

            panel.add(loginButton);
            panel.add(Box.createVerticalStrut(10));
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
