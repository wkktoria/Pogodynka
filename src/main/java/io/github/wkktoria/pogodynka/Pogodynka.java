package io.github.wkktoria.pogodynka;

import com.formdev.flatlaf.FlatLightLaf;
import io.github.wkktoria.pogodynka.controller.ReportController;
import io.github.wkktoria.pogodynka.controller.WeatherController;
import io.github.wkktoria.pogodynka.model.Weather;
import io.github.wkktoria.pogodynka.service.ReportService;
import io.github.wkktoria.pogodynka.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

class Pogodynka {
    private static final Logger LOGGER = LoggerFactory.getLogger(Pogodynka.class);
    private static final String DEFAULT_LOCATION = "Warsaw";
    private static final WeatherController weatherController = new WeatherController(new WeatherService());
    private static final ReportController reportController = new ReportController(new ReportService(weatherController));

    public static void main(String[] args) {
        FlatLightLaf.setup();

        JFrame frame = new JFrame("Pogodynka");
        frame.setSize(new Dimension(480, 480));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(new Color(27, 71, 120));

        JPanel infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(400, 150));
        infoPanel.setLayout(new GridLayout(2, 2));
        infoPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
        infoPanel.setBackground(new Color(27, 71, 120));

        JPanel inputPanel = new JPanel();
        inputPanel.setPreferredSize(new Dimension(400, 50));
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
        inputPanel.setBackground(new Color(27, 71, 120));

        JLabel imageLabel = new JLabel();
        setImageLabel(imageLabel, DEFAULT_LOCATION);

        JLabel locationLabel = createInfoLabel(weatherController.getWeather(DEFAULT_LOCATION).getLocation());
        JLabel temperatureLabel = createInfoLabel("Temperature: " + weatherController.getWeather(DEFAULT_LOCATION).getTemperature() + "°C");
        JLabel humidityLabel = createInfoLabel("Humidity: " + weatherController.getWeather(DEFAULT_LOCATION).getHumidity() + "%");

        JTextField locationField = new JTextField();
        locationField.setPreferredSize(new Dimension(300, 50));
        locationField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {

            }

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchWeather(locationField, imageLabel, locationLabel, temperatureLabel, humidityLabel);
                }
            }

            @Override
            public void keyReleased(final KeyEvent e) {

            }
        });
        JButton searchButton = new JButton("Search");
        searchButton.setOpaque(true);
        searchButton.setContentAreaFilled(true);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setBackground(new Color(28, 141, 162));
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(e -> searchWeather(locationField, imageLabel, locationLabel, temperatureLabel, humidityLabel));

        JButton generateReportButton = new JButton("Generate Report");
        generateReportButton.addActionListener(e -> generateReport(locationLabel));

        infoPanel.add(imageLabel);
        infoPanel.add(locationLabel);
        infoPanel.add(temperatureLabel);
        infoPanel.add(humidityLabel);

        inputPanel.add(locationField, BorderLayout.CENTER);
        inputPanel.add(searchButton, BorderLayout.EAST);

        panel.add(infoPanel);
        panel.add(inputPanel);
        panel.add(generateReportButton);

        frame.add(panel);

        frame.setVisible(true);
    }

    private static void setImageLabel(JLabel imageLabel, final String location) {
        try {
            Image image = ImageIO.read(new URI(weatherController.getWeather(location).getImageSource()).toURL());
            imageLabel.setIcon(new ImageIcon(image));
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Couldn't load image", e);
        }
    }

    private static JLabel createInfoLabel(final String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);

        return label;
    }

    private static void searchWeather(JTextField locationField, JLabel imageLabel, JLabel locationLabel, JLabel temperatureLabel, JLabel humidityLabel) {
        String location = locationField.getText();

        if (location.isEmpty()) {
            return;
        }

        Weather weather = weatherController.getWeather(location);

        if (weather == null) {
            JOptionPane.showMessageDialog(null, "Couldn't find weather for '" + location + "'.", "Invalid location", JOptionPane.ERROR_MESSAGE);
            return;
        }

        locationLabel.setText(weather.getLocation());
        temperatureLabel.setText("Temperature: " + weather.getTemperature() + "°C");
        humidityLabel.setText("Humidity: " + weather.getHumidity() + "%");
        setImageLabel(imageLabel, weather.getLocation());
        locationField.setText("");
    }

    private static void generateReport(final JLabel locationLabel) {
        String location = locationLabel.getText();

        if (location.isEmpty()) {
            return;
        }

        try {
            reportController.generate(location);
            JOptionPane.showMessageDialog(null, "Report was successfully generated.", "Report generated", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            LOGGER.error("Couldn't generate report", e);
        }
    }
}

