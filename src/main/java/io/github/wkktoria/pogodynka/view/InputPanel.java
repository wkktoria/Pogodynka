package io.github.wkktoria.pogodynka.view;

import io.github.wkktoria.pogodynka.controller.ResourceController;
import io.github.wkktoria.pogodynka.controller.WeatherController;
import io.github.wkktoria.pogodynka.model.Weather;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class InputPanel extends JPanel {
    private final WeatherController weatherController;
    private final ResourceController resourceController;

    private final JTextField locationTextField = new JTextField();
    private final JButton searchButton = new JButton();

    InputPanel(final WeatherController weatherController,
               final ResourceController resourceController) {
        this.weatherController = weatherController;
        this.resourceController = resourceController;

        locationTextField.setColumns(20);
        locationTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchWeather();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        searchButton.addActionListener(e -> searchWeather());

        add(locationTextField);
        add(searchButton);
    }

    JTextField getLocationTextField() {
        return locationTextField;
    }

    JButton getSearchButton() {
        return searchButton;
    }

    void searchWeather() {
        final String location = locationTextField.getText();

        if (location.isEmpty()) {
            return;
        }

        Weather weather = weatherController.getWeather(location);

        if (weather == null) {
            JOptionPane.showMessageDialog(null,
                    resourceController.getByKey("couldNotFindWeatherInformationFor", ResourceController.Case.SENTENCE_CASE) + " " + location + ".",
                    resourceController.getByKey("invalidLocation", ResourceController.Case.CAPITALIZED_CASE),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        MainFrame.updateWeatherInfo();
        locationTextField.setText("");
    }
}
