package io.github.wkktoria.pogodynka;

import com.formdev.flatlaf.FlatDarkLaf;
import io.github.wkktoria.pogodynka.controller.LocationPreferencesController;
import io.github.wkktoria.pogodynka.controller.ReportController;
import io.github.wkktoria.pogodynka.controller.WeatherController;
import io.github.wkktoria.pogodynka.exception.ApiProblemException;
import io.github.wkktoria.pogodynka.exception.MissingApiKeyException;
import io.github.wkktoria.pogodynka.model.Weather;
import io.github.wkktoria.pogodynka.service.LocationPreferencesService;
import io.github.wkktoria.pogodynka.service.ReportService;
import io.github.wkktoria.pogodynka.service.WeatherService;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

class Pogodynka {
    private static final Logger LOGGER = LoggerFactory.getLogger(Pogodynka.class);
    private static final WeatherController weatherController;
    private static final ReportController reportController;
    private static final LocationPreferencesController locationPreferencesController;
    private static final Weather defaultWeather;

    static {
        try {
            weatherController = new WeatherController(new WeatherService());
            reportController = new ReportController(new ReportService(weatherController));
            locationPreferencesController = new LocationPreferencesController(new LocationPreferencesService(weatherController));
            defaultWeather = weatherController.getWeather(locationPreferencesController.getLocation());

            if (defaultWeather == null) {
                throw new ApiProblemException("Couldn't get weather information");
            }
        } catch (MissingApiKeyException | ApiProblemException e) {
            LOGGER.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        FlatDarkLaf.setup();

        JFrame frame = new JFrame("Pogodynka");
        frame.setSize(new Dimension(360, 280));
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        JButton configDefaultLocationButton = new JButton("Configure default location");
        configDefaultLocationButton.addActionListener(e -> configureDefaultLocation());

        toolBar.add(configDefaultLocationButton);

        JPanel locationPanel = new JPanel();

        JLabel imageLabel = new JLabel();
        setImageLabel(imageLabel, locationPreferencesController.getLocation());
        JLabel locationLabel = new JLabel(locationPreferencesController.getLocation());

        locationPanel.add(imageLabel);
        locationPanel.add(locationLabel);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel temperatureLabel = new JLabel("Temperature: " + defaultWeather.getTemperature() + "°C");
        temperatureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel humidityLabel = new JLabel("Humidity: " + defaultWeather.getHumidity() + "%");
        humidityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel windSpeedLabel = new JLabel("Wind speed: " + defaultWeather.getWindSpeed() + " m/s");
        windSpeedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel pressureLabel = new JLabel("Pressure: " + defaultWeather.getPressure() + " hPa");
        pressureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(locationPanel);
        infoPanel.add(temperatureLabel);
        infoPanel.add(temperatureLabel);
        infoPanel.add(humidityLabel);
        infoPanel.add(windSpeedLabel);
        infoPanel.add(pressureLabel);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField locationField = new JTextField();
        locationField.setColumns(15);
        locationField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {

            }

            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchWeather(locationField, imageLabel, locationLabel, temperatureLabel, humidityLabel,
                            windSpeedLabel, pressureLabel);
                }
            }

            @Override
            public void keyReleased(final KeyEvent e) {

            }
        });
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchWeather(locationField, imageLabel, locationLabel, temperatureLabel,
                humidityLabel, windSpeedLabel, pressureLabel));

        inputPanel.add(locationField);
        inputPanel.add(searchButton);

        JButton generateReportButton = new JButton("Generate Report");
        generateReportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateReportButton.addActionListener(e -> generateReport(locationLabel));

        panel.add(toolBar);
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

    private static void searchWeather(JTextField locationField, JLabel imageLabel, JLabel locationLabel,
                                      JLabel temperatureLabel, JLabel humidityLabel, JLabel windSpeedLabel, JLabel pressureLabel) {
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
        windSpeedLabel.setText("Wind speed: " + weather.getWindSpeed() + " m/s");
        pressureLabel.setText("Pressure: " + weather.getPressure() + " hPa");
        locationField.setText("");
    }

    private static void generateReport(final JLabel locationLabel) {
        String location = locationLabel.getText();

        if (location.isEmpty()) {
            return;
        }

        if (reportController.generate(location)) {
            JOptionPane.showMessageDialog(null, "Report was successfully generated.", "Report generated", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Couldn't generate report.", "Generation failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void configureDefaultLocation() {
        String location = JOptionPane.showInputDialog(null, "Default location:", "Configure default location", JOptionPane.QUESTION_MESSAGE);

        if (location != null) {
            WordUtils.capitalizeFully(location);
        }

        if (location != null && locationPreferencesController.setLocation(location)) {
            JOptionPane.showMessageDialog(null, "Default location set successfully to " + location + ".", "Default location configured", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Couldn't set default location to " + location + ".", "Invalid location", JOptionPane.ERROR_MESSAGE);
        }
    }
}

