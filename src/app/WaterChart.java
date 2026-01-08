package app;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class WaterChart extends JFrame {
    private ArrayList<Double> data;
    public WaterChart(ArrayList<Double> data) {
        this.data = data;

        setTitle("Water Intake Chart");
        setSize(500,400);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (data == null || data.isEmpty())
            return;

        int x = 50;
        int baseY = 350;

        g.drawString("Water Intake (L)", 200, 80);

        for (int i = 0; i < data.size(); i++) {

            int height = (int) (data.get(i) * 40);

            g.setColor(Color.CYAN);
            g.fillRect(x, baseY - height, 40, height);

            g.setColor(Color.BLACK);
            g.drawRect(x, baseY - height, 40, height);

            g.drawString("Day " + (i+1), x, baseY + 15);
            g.drawString(data.get(i) + "L", x, baseY - height - 5);

            x += 60;
        }
    }
}
