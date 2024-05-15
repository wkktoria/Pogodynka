package io.github.wkktoria.weatherreport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class WeatherReportApplication {
    private static final Logger logger = LoggerFactory.getLogger(WeatherReportApplication.class);

    public static void main(String[] args) {
        JFrame frame = new JFrame("Weather Report");
        frame.setSize(new Dimension(400, 250));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        String apiKey = System.getenv("WEATHER_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            logger.error("API key was not provided");
            System.exit(1);
        }

        WeatherJson weatherJson = new WeatherJson(apiKey);
        Weather weather = weatherJson.createWeather("Warsaw");

        JPanel infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(400, 150));
        infoPanel.setLayout(new GridLayout(2, 1));
        infoPanel.setBackground(Color.LIGHT_GRAY);

        JPanel inputPanel = new JPanel();
        inputPanel.setPreferredSize(new Dimension(400, 50));
        inputPanel.setLayout(new BorderLayout());

        Image weatherImage = weather.getImage();
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(weatherImage));

        JLabel locationLabel = new JLabel(weather.getLocation());
        JLabel temperatureLabel = new JLabel("Temperature: " + weather.getTemperature() + "°C");
        JLabel humidityLabel = new JLabel("Humidity: " + weather.getHumidity() + "%");

        JTextField locationField = new JTextField();
        locationField.setPreferredSize(new Dimension(300, 50));
        locationField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {

            }

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchWeather(weatherJson, locationField, imageLabel, locationLabel, temperatureLabel, humidityLabel);
                }
            }

            @Override
            public void keyReleased(final KeyEvent e) {

            }
        });
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 50));
        searchButton.addActionListener(e -> searchWeather(weatherJson, locationField, imageLabel, locationLabel, temperatureLabel, humidityLabel));

        infoPanel.add(imageLabel);
        infoPanel.add(locationLabel);
        infoPanel.add(temperatureLabel);
        infoPanel.add(humidityLabel);

        inputPanel.add(locationField, BorderLayout.WEST);
        inputPanel.add(searchButton, BorderLayout.EAST);

        panel.add(infoPanel);
        panel.add(inputPanel);

        frame.add(panel);

        frame.setVisible(true);
    }

    private static void searchWeather(final WeatherJson weatherJson, final JTextField locationField, final JLabel imageLabel, final JLabel locationLabel, final JLabel temperatureLabel, final JLabel humidityLabel) {
        String location = locationField.getText();

        if (location.isEmpty()) {
            return;
        }

        Weather weather;
        try {
            weather = weatherJson.createWeather(location);
        } catch (WeatherJson.WeatherJsonException e) {
            JOptionPane.showMessageDialog(null, String.format("Could not get weather for '%s'.", locationField.getText()), "Invalid location", JOptionPane.ERROR_MESSAGE);
            return;
        }

        locationLabel.setText(weather.getLocation());
        temperatureLabel.setText("Temperature: " + weather.getTemperature() + "°C");
        humidityLabel.setText("Humidity: " + weather.getHumidity() + "%");
        imageLabel.setIcon(new ImageIcon(weather.getImage()));

        locationField.setText("");
    }
}

