package io.github.wkktoria.weatherreport;

import javax.swing.*;
import java.awt.*;
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
        JLabel locationLabel = new JLabel("Location: " + weather.getLocation().get());
        JLabel temperatureLabel = new JLabel("Temperature: " + weather.getTemperature().get() + "°C");
        JLabel humidityLabel = new JLabel("Humidity: " + weather.getHumidity().get() + "%");

        weatherImage.ifPresent(image -> imageLabel.setIcon(new ImageIcon(image)));

        frame.add(imageLabel);
        frame.add(locationLabel);
        frame.add(temperatureLabel);
        frame.add(humidityLabel);

        frame.setVisible(true);
    }
}
