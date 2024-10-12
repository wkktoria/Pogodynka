package io.github.wkktoria.pogodynka.view;

import io.github.wkktoria.pogodynka.controller.PreferencesController;
import io.github.wkktoria.pogodynka.controller.ResourceController;
import io.github.wkktoria.pogodynka.controller.WeatherController;

import javax.swing.*;
import java.awt.*;

class MainPanel extends JPanel {
    private final LocationPanel locationPanel;
    private final InfoPanel infoPanel;
    private final InputPanel inputPanel;

    MainPanel(final PreferencesController preferencesController,
              final WeatherController weatherController,
              final ResourceController resourceController) {
        locationPanel = new LocationPanel(preferencesController, weatherController);
        infoPanel = new InfoPanel(resourceController);
        inputPanel = new InputPanel(weatherController, resourceController);

        setLayout(new BorderLayout());

        add(locationPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }

    LocationPanel getLocationPanel() {
        return locationPanel;
    }

    InfoPanel getInfoPanel() {
        return infoPanel;
    }

    InputPanel getInputPanel() {
        return inputPanel;
    }
}
