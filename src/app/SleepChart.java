package app;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SleepChart extends JPanel {

    private ArrayList<Double> data;

    public SleepChart(ArrayList<Double> data) {
        this.data = data;
        setPreferredSize(new Dimension(500, 400));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (data.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        int barWidth = width / data.size();

        double max = 0;
        for (double d : data) if (d > max) max = d;

        for (int i = 0; i < data.size(); i++) {

            double value = data.get(i);
            int barHeight = (int) ((value / max) * (height - 50));

            int x = i * barWidth + 10;
            int y = height - barHeight - 30;

            g2.setColor(Color.BLUE);
            g2.fillRect(x, y, barWidth - 20, barHeight);

            g2.setColor(Color.BLACK);
            g2.drawString("Day " + (i + 1), x, height - 10);

            g2.drawString(value + "h", x, y - 5);
        }
    }
    public static void showChart(ArrayList<Double> data) {
        JFrame frame = new JFrame("Sleep Chart");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new SleepChart(data));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
