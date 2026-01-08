package app;

import javax.swing.*;
import java.awt.*;

public class MainMenuForm {
    private JFrame frame;
    private JPanel mainPanel;
    private JButton financeButton;
    private JButton habitTrackerButton;
    private JButton sleepTrackerButton;
    private JButton foodTrackerButton;
    private JButton moodTrackerButton;
    private JButton waterTrackerButton;

    public MainMenuForm(String username) {
        frame = new JFrame("Main Menu");
        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JLabel welcomeLabel = new JLabel("Hello, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(welcomeLabel, gbc);

        financeButton = new JButton("FinanceApp");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(financeButton, gbc);

        habitTrackerButton = new JButton("Habit Tracker");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        mainPanel.add(habitTrackerButton, gbc);

        sleepTrackerButton = new JButton("Sleep Tracker");
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(sleepTrackerButton, gbc);

        foodTrackerButton = new JButton("Food Tracker");
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(foodTrackerButton, gbc);

        moodTrackerButton = new JButton("Mood Tracker");
        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(moodTrackerButton, gbc);

        waterTrackerButton = new JButton("Water Tracker");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(waterTrackerButton, gbc);

        financeButton.addActionListener(e -> new FinanceTracker(username));

        habitTrackerButton.addActionListener(e -> new HabitTrackerForm(username));

        sleepTrackerButton.addActionListener(e -> new SleepTrackerForm(username));

        foodTrackerButton.addActionListener(e -> new FoodTrackerForm(username));

        moodTrackerButton.addActionListener(e ->
                JOptionPane.showMessageDialog(frame, "Mood Tracker clicked"));

        waterTrackerButton.addActionListener(e -> new WaterTrackerForm(username));

        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

