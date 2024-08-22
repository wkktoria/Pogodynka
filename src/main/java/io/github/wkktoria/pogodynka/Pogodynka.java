package io.github.wkktoria.pogodynka;

import com.formdev.flatlaf.FlatDarkLaf;
import io.github.wkktoria.pogodynka.config.LocaleConfig;
import io.github.wkktoria.pogodynka.controller.LocationPreferencesController;
import io.github.wkktoria.pogodynka.controller.ReportController;
import io.github.wkktoria.pogodynka.controller.ResourceController;
import io.github.wkktoria.pogodynka.controller.WeatherController;
import io.github.wkktoria.pogodynka.exception.ApiProblemException;
import io.github.wkktoria.pogodynka.exception.MissingApiKeyException;
import io.github.wkktoria.pogodynka.model.Weather;
import io.github.wkktoria.pogodynka.service.LocationPreferencesService;
import io.github.wkktoria.pogodynka.service.ReportService;
import io.github.wkktoria.pogodynka.service.ResourceService;
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
import java.util.Locale;

class Pogodynka {
    private static final Logger LOGGER = LoggerFactory.getLogger(Pogodynka.class);
    private static final WeatherController weatherController;
    private static final ReportController reportController;
    private static final LocationPreferencesController locationPreferencesController;
    private static final LocaleConfig localeConfig;
    private static final ResourceController resourceController;

    private static Weather weather;

    private static JButton configureDefaultLocationButton;
    private static JButton searchButton;
    private static JButton generateReportButton;

    private static JLabel temperatureLabel;
    private static JLabel humidityLabel;
    private static JLabel windSpeedLabel;
    private static JLabel pressureLabel;

    static {
        try {
            localeConfig = new LocaleConfig();
            resourceController = new ResourceController(new ResourceService(localeConfig));
            weatherController = new WeatherController(new WeatherService());
            reportController = new ReportController(new ReportService(weatherController, resourceController));
            locationPreferencesController = new LocationPreferencesController(new LocationPreferencesService(weatherController));
            weather = weatherController.getWeather(locationPreferencesController.getLocation());

            if (weather == null) {
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
        frame.setSize(new Dimension(480, 280));
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        configureDefaultLocationButton = new JButton("Configure default location");
        configureDefaultLocationButton.addActionListener(e -> configureDefaultLocation());

        String[] languages = resourceController.getByKey("availableLanguages").split(",");
        JList<String> languageList = new JList<>(languages);
        languageList.setVisibleRowCount(1);
        languageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane languageListScrollPane = new JScrollPane(languageList);

        languageList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                JList<?> source = (JList<?>) e.getSource();
                int languageIndex = source.getSelectedIndex();

                switch (languageIndex) {
                    case 0:
                        localeConfig.setLocale(Locale.ENGLISH);
                        break;
                    case 1:
                        localeConfig.setLocale(Locale.of("pl", "PL"));
                        break;
                    case 2:
                        localeConfig.setLocale(Locale.of("ru", "RU"));
                        break;
                    default:
                        return;
                }

                update();
            }
        });

        toolBar.add(configureDefaultLocationButton);
        toolBar.add(languageListScrollPane);

        JPanel locationPanel = new JPanel();

        JLabel imageLabel = new JLabel();
        setImageLabel(imageLabel, locationPreferencesController.getLocation());
        JLabel locationLabel = new JLabel(locationPreferencesController.getLocation());

        locationPanel.add(imageLabel);
        locationPanel.add(locationLabel);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        temperatureLabel = new JLabel("Temperature");
        temperatureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        humidityLabel = new JLabel("Humidity");
        humidityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        windSpeedLabel = new JLabel("Wind speed");
        windSpeedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pressureLabel = new JLabel("Pressure");
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
                    searchWeather(locationField, locationLabel
                    );
                }
            }

            @Override
            public void keyReleased(final KeyEvent e) {

            }
        });
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchWeather(locationField, locationLabel
        ));

        inputPanel.add(locationField);
        inputPanel.add(searchButton);

        generateReportButton = new JButton("Generate report");
        generateReportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateReportButton.addActionListener(e -> generateReport(locationLabel));

        panel.add(toolBar);
        panel.add(infoPanel);
        panel.add(inputPanel);
        panel.add(generateReportButton);

        frame.add(panel);

        update();

        frame.setVisible(true);
    }

    private static void update() {
        configureDefaultLocationButton.setText(resourceController.getByKey("configureDefaultLocation"));
        searchButton.setText(resourceController.getByKey("search"));
        generateReportButton.setText(resourceController.getByKey("generateReport"));

        temperatureLabel.setText(resourceController.getByKey("temperature") + ": " + weather.getTemperature() + "°C");
        humidityLabel.setText(resourceController.getByKey("humidity") + ": " + weather.getHumidity() + "%");
        windSpeedLabel.setText(resourceController.getByKey("windSpeed") + ": " + weather.getWindSpeed() + " m/s");
        pressureLabel.setText(resourceController.getByKey("pressure") + ": " + weather.getPressure() + " hPa");
    }

    private static void setImageLabel(JLabel imageLabel, final String location) {
        try {
            Image image = ImageIO.read(new URI(weatherController.getWeather(location).getImageSource()).toURL());
            imageLabel.setIcon(new ImageIcon(image));
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Couldn't load image", e);
        }
    }

    private static void searchWeather(JTextField locationField, JLabel locationLabel) {
        String location = locationField.getText();

        if (location.isEmpty()) {
            return;
        }

        weather = weatherController.getWeather(location);

        if (weather == null) {
            JOptionPane.showMessageDialog(null,
                    resourceController.getByKey("couldNotFindWeatherInformationFor") + " " + location + ".",
                    resourceController.getByKey("invalidLocation"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        locationLabel.setText(weather.getLocation());
        locationField.setText("");

        update();
    }

    private static void generateReport(final JLabel locationLabel) {
        String filename = JOptionPane.showInputDialog(null,
                resourceController.getByKey("reportFilename") + ": ",
                resourceController.getByKey("chooseReportFilename"),
                JOptionPane.QUESTION_MESSAGE);

        if (filename == null || filename.isEmpty()) {
            return;
        }

        String location = locationLabel.getText();

        if (location == null || location.isEmpty()) {
            return;
        }

        if (reportController.generate(filename, location)) {
            JOptionPane.showMessageDialog(null,
                    resourceController.getByKey("reportWasSuccessfullyGenerated") + ".",
                    resourceController.getByKey("reportGenerated"),
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    resourceController.getByKey("couldNotGenerateReport") + ".",
                    resourceController.getByKey("reportGenerationFailed"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void configureDefaultLocation() {
        String location = JOptionPane.showInputDialog(null,
                resourceController.getByKey("defaultLocation") + ": ",
                resourceController.getByKey("configureDefaultLocation"),
                JOptionPane.QUESTION_MESSAGE);

        if (location == null || location.isEmpty()) {
            return;
        }

        WordUtils.capitalizeFully(location);

        if (locationPreferencesController.setLocation(location)) {
            JOptionPane.showMessageDialog(null,
                    resourceController.getByKey("defaultLocationSetSuccessfullyTo") + " " + location + ".",
                    resourceController.getByKey("defaultLocationSetUp"),
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    resourceController.getByKey("couldNotSetDefaultLocationTo") + " " + location + ".",
                    resourceController.getByKey("invalidLocation"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

