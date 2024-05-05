package io.github.wkktoria.weatherreport;

import javax.swing.*;
import java.awt.*;

class WeatherReportApplication {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Weather Report");
        frame.setSize(new Dimension(600, 300));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}
