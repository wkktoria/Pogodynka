package io.github.wkktoria.weatherreport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Optional;

class WeatherReportApplication {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Weather Report");
        frame.setSize(new Dimension(600, 300));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new FlowLayout());

        String apiKey = System.getenv("WEATHER_API_KEY");
        Weather weather;

        if (apiKey.trim().isEmpty()) {
            System.out.println("Please provide a API key");
            System.exit(1);
        }

        weather = new Weather("Warsaw", apiKey);
        if (weather.getJson().isEmpty()) {
            System.out.println("Could not create weather JSON");
            System.exit(1);
        }

        if (weather.getLocation().isEmpty() || weather.getTemperature().isEmpty() || weather.getHumidity().isEmpty()) {
            System.out.println("Could not get weather data");
            System.exit(1);
        }

        Optional<Image> weatherImage = weather.createImage();
        JLabel imageLabel = new JLabel();
        weatherImage.ifPresent(image -> imageLabel.setIcon(new ImageIcon(image)));

        JLabel locationLabel = new JLabel("Location: " + weather.getLocation().get());
        JLabel temperatureLabel = new JLabel("Temperature: " + weather.getTemperature().get() + "°C");
        JLabel humidityLabel = new JLabel("Humidity: " + weather.getHumidity().get() + "%");

        JTextField locationField = new JTextField();
        locationField.setPreferredSize(new Dimension(200, 20));
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
        searchButton.addActionListener(e -> {
            searchWeather(weather, imageLabel, locationLabel, temperatureLabel, humidityLabel, locationField);
        });

        frame.add(imageLabel);
        frame.add(locationLabel);
        frame.add(temperatureLabel);
        frame.add(humidityLabel);
        frame.add(locationField);
        frame.add(searchButton);

        frame.setVisible(true);
    }

    private static void searchWeather(final Weather weather, final JLabel imageLabel, final JLabel locationLabel, final JLabel temperatureLabel, final JLabel humidityLabel, final JTextField locationField) {
        if (locationField.getText().isEmpty()) {
            return;
        }

        weather.setJson(locationField.getText());

        if (weather.getJson().isEmpty()) {
            System.out.println("Could not create weather JSON");
            return;
        }

        if (weather.getLocation().isEmpty() || weather.getTemperature().isEmpty() || weather.getHumidity().isEmpty()) {
            System.out.println("Could not get weather data");
            return;
        }

        if (weather.createImage().isPresent()) {
            imageLabel.setIcon(new ImageIcon(weather.createImage().get()));
        }

        locationLabel.setText("Location: " + weather.getLocation().get());
        temperatureLabel.setText("Temperature: " + weather.getTemperature().get() + "°C");
        humidityLabel.setText("Humidity: " + weather.getHumidity().get() + "%");

        locationField.setText("");
    }
}
