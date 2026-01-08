package app;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainMenuForm {

    private JFrame frame;
    private JPanel mainPanel;
    private JButton financeButton;
    private JButton habitTrackerButton;
    private JButton sleepTrackerButton;
    private JButton foodTrackerButton;
    private JButton fitnessTrackerButton;
    private JButton waterTrackerButton;

    public MainMenuForm(String username) {

        frame = new JFrame("Life Management System");
        frame.setSize(500, 420);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel titleLabel = new JLabel("Life Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(40, 40, 40));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 1;
        mainPanel.add(welcomeLabel, gbc);

        financeButton = createButton("Finance Tracker", new Color(52, 152, 219));
        habitTrackerButton = createButton("Habit Tracker", new Color(155, 89, 182));
        sleepTrackerButton = createButton("Sleep Tracker", new Color(52, 73, 94));
        foodTrackerButton = createButton("Food Tracker", new Color(46, 204, 113));
        fitnessTrackerButton = createButton("Fitness Tracker", new Color(230, 126, 34));
        waterTrackerButton = createButton("Water Tracker", new Color(26, 188, 156));

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(financeButton, gbc);
        gbc.gridx = 1;
        mainPanel.add(habitTrackerButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(sleepTrackerButton, gbc);
        gbc.gridx = 1;
        mainPanel.add(foodTrackerButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(fitnessTrackerButton, gbc);
        gbc.gridx = 1;
        mainPanel.add(waterTrackerButton, gbc);

        financeButton.addActionListener(e -> new FinanceTracker(username));
        habitTrackerButton.addActionListener(e -> new HabitTrackerForm(username));
        sleepTrackerButton.addActionListener(e -> new SleepTrackerForm(username));
        foodTrackerButton.addActionListener(e -> new FoodTrackerForm(username));
        fitnessTrackerButton.addActionListener(e -> new FitnessTrackerForm(username));
        waterTrackerButton.addActionListener(e -> new WaterTrackerForm(username));

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(Color.DARK_GRAY, 2, true));
        button.setPreferredSize(new Dimension(180, 45));
        return button;
    }
}
