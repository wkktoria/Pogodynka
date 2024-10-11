package io.github.wkktoria.pogodynka.view;

import io.github.wkktoria.pogodynka.controller.ResourceController;
import io.github.wkktoria.pogodynka.model.Weather;

import javax.swing.*;
import java.awt.*;

class InfoPanel extends JPanel {
    private final ResourceController resourceController;

    private final JLabel temperatureLabel = new JLabel();
    private final JLabel humidityLabel = new JLabel();
    private final JLabel windSpeedLabel = new JLabel();
    private final JLabel pressureLabel = new JLabel();

    InfoPanel(final ResourceController resourceController) {
        this.resourceController = resourceController;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        temperatureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        humidityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        windSpeedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pressureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(temperatureLabel);
        add(humidityLabel);
        add(windSpeedLabel);
        add(pressureLabel);
    }

    void updateLabels() {
        temperatureLabel.setText(getTemperatureText());
        humidityLabel.setText(getHumidityText());
        windSpeedLabel.setText(getWindSpeedText());
        pressureLabel.setText(getPressureText());
    }

    private String getTemperatureText() {
        return resourceController
                .getByKey("temperature", ResourceController.Case.CAPITALIZED_CASE)
                + ": " + Weather.getWeather().getTemperature() + "°C";
    }

    private String getHumidityText() {
        return resourceController
                .getByKey("humidity", ResourceController.Case.CAPITALIZED_CASE)
                + ": " + Weather.getWeather().getHumidity() + "%";
    }

    private String getWindSpeedText() {
        return resourceController
                .getByKey("windSpeed", ResourceController.Case.CAPITALIZED_CASE)
                + ": " + Weather.getWeather().getWindSpeed() + " m/s";
    }

    private String getPressureText() {
        return resourceController
                .getByKey("pressure", ResourceController.Case.CAPITALIZED_CASE)
                + ": " + Weather.getWeather().getPressure() + " hPa";
    }
}
