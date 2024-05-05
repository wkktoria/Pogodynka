package io.github.wkktoria.weatherreport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Optional;

class WeatherReportApplication {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Weather Report");
        frame.setSize(new Dimension(400, 250));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        String apiKey = System.getenv("WEATHER_API_KEY");
        Weather weather;

        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("Please provide a API key");
            System.exit(1);
        }

        weather = new Weather("Warsaw", apiKey.trim());
        if (weather.getJson().isEmpty()) {
            System.out.println("Could not create weather JSON");
            System.exit(1);
        }

        if (weather.getLocation().isEmpty() || weather.getTemperature().isEmpty() || weather.getHumidity().isEmpty()) {
            System.out.println("Could not get weather data");
            System.exit(1);
        }

        JPanel infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(400, 150));
        infoPanel.setLayout(new GridLayout(2, 1));
        infoPanel.setBackground(Color.LIGHT_GRAY);

        JPanel inputPanel = new JPanel();
        inputPanel.setPreferredSize(new Dimension(400, 50));
        inputPanel.setLayout(new BorderLayout());

        Optional<Image> weatherImage = weather.createImage();
        JLabel imageLabel = new JLabel();
        weatherImage.ifPresent(image -> imageLabel.setIcon(new ImageIcon(image)));

        JLabel locationLabel = new JLabel(weather.getLocation().get());
        JLabel temperatureLabel = new JLabel("Temperature: " + weather.getTemperature().get() + "°C");
        JLabel humidityLabel = new JLabel("Humidity: " + weather.getHumidity().get() + "%");

        JTextField locationField = new JTextField();
        locationField.setPreferredSize(new Dimension(300, 50));
        locationField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {

            }

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchWeather(weather, imageLabel, locationLabel, temperatureLabel, humidityLabel, locationField);
                }
            }

            @Override
            public void keyReleased(final KeyEvent e) {

            }
        });
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 50));
        searchButton.addActionListener(e -> searchWeather(weather, imageLabel, locationLabel, temperatureLabel, humidityLabel, locationField));

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

    private static void searchWeather(final Weather weather, final JLabel imageLabel, final JLabel locationLabel, final JLabel temperatureLabel, final JLabel humidityLabel, final JTextField locationField) {
        if (locationField.getText().isEmpty()) {
            return;
        }

        weather.setJson(locationField.getText().trim());

        if (weather.getJson().isEmpty()) {
            JOptionPane.showMessageDialog(null, String.format("Could not create weather JSON for desired location (%s).", locationField.getText()), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (weather.getLocation().isEmpty() || weather.getTemperature().isEmpty() || weather.getHumidity().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Could not get weather data.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (weather.createImage().isPresent()) {
            imageLabel.setIcon(new ImageIcon(weather.createImage().get()));
        }

        locationLabel.setText(weather.getLocation().get());
        temperatureLabel.setText("Temperature: " + weather.getTemperature().get() + "°C");
        humidityLabel.setText("Humidity: " + weather.getHumidity().get() + "%");

        locationField.setText("");
    }
}
