package io.github.wkktoria.pogodynka.view;

import io.github.wkktoria.pogodynka.controller.*;
import io.github.wkktoria.pogodynka.generator.ReportGenerator;
import io.github.wkktoria.pogodynka.model.Weather;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static ResourceController resourceController = null;
    private static Toolbar toolbar = null;
    private static MainPanel mainPanel = null;

    public MainFrame(final String title,
                     final ResourceController resourceController,
                     final PreferencesController preferencesController,
                     final WeatherController weatherController,
                     final ReportGenerator reportGenerator) {
        MainFrame.resourceController = resourceController;
        toolbar = new Toolbar(resourceController, reportGenerator, preferencesController);
        mainPanel = new MainPanel(preferencesController, weatherController, resourceController);

        setTitle(title);
        setSize(new Dimension(560, 280));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(toolbar, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    static void updateLanguage() {
        toolbar.getConfigureDefaultLocationButton()
                .setText(resourceController
                        .getByKey("configureDefaultLocation", ResourceController.Case.CAPITALIZED_CASE));
        toolbar.getGenerateReportButton()
                .setText(resourceController
                        .getByKey("generateReport", ResourceController.Case.CAPITALIZED_CASE));

        mainPanel.getInfoPanel().updateLabels();

        mainPanel.getInputPanel().getSearchButton()
                .setText(resourceController.getByKey("search", ResourceController.Case.CAPITALIZED_CASE));
    }

    static void updateWeatherInfo() {
        mainPanel.getLocationPanel().setLocationImageLabel(Weather.getWeather().getLocation());
        mainPanel.getLocationPanel().getLocationLabel().setText(Weather.getWeather().getLocation());

        mainPanel.getInfoPanel().updateLabels();
    }

    static MainPanel getMainPanel() {
        return mainPanel;
    }

    public void showFrame() {
        updateLanguage();
        setVisible(true);
    }
}
