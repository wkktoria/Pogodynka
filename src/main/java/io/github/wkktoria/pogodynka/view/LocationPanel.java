package io.github.wkktoria.pogodynka.view;

import io.github.wkktoria.pogodynka.controller.PreferencesController;
import io.github.wkktoria.pogodynka.controller.WeatherController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

class LocationPanel extends JPanel {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationPanel.class);

    private final PreferencesController preferencesController;
    private final WeatherController weatherController;

    private final JLabel locationImageLabel = new JLabel();
    private final JLabel locationLabel = new JLabel();

    LocationPanel(final PreferencesController preferencesController,
                  final WeatherController weatherController) {
        this.preferencesController = preferencesController;
        this.weatherController = weatherController;

        locationLabel.setText(getDefaultLocation());
        setLocationImageLabel(getDefaultLocation());

        add(locationImageLabel);
        add(locationLabel);
    }

    private String getDefaultLocation() {
        return preferencesController.getLocation();
    }

    void setLocationImageLabel(final String location) {
        try {
            Image image = ImageIO.read(new URI(weatherController.getWeather(location).getImageSource()).toURL());
            locationImageLabel.setIcon(new ImageIcon(image));
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Couldn't load image", e);
        }
    }

    JLabel getLocationLabel() {
        return locationLabel;
    }
}
