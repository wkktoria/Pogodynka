package io.github.wkktoria.pogodynka;

import com.formdev.flatlaf.FlatDarkLaf;
import io.github.wkktoria.pogodynka.config.LocaleConfig;
import io.github.wkktoria.pogodynka.controller.*;
import io.github.wkktoria.pogodynka.exception.ApiProblemException;
import io.github.wkktoria.pogodynka.exception.MissingApiKeyException;
import io.github.wkktoria.pogodynka.service.*;
import io.github.wkktoria.pogodynka.view.MainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Pogodynka {
    private static final Logger LOGGER = LoggerFactory.getLogger(Pogodynka.class);
    private static final WeatherController weatherController;
    private static final ReportController reportController;
    private static final PreferencesController preferencesController;
    private static final ResourceController resourceController;

    static {
        try {
            resourceController = new ResourceController(new ResourceService(LocaleConfig.getLocaleConfig()));
            weatherController = new WeatherController(new WeatherService());
            reportController = new ReportController(new ReportService(weatherController, resourceController));
            preferencesController = new PreferencesController(new PreferencesService(weatherController));

            if (weatherController.getWeather(preferencesController.getLocation()) == null) {
                throw new ApiProblemException("Couldn't get weather information");
            }
        } catch (MissingApiKeyException | ApiProblemException e) {
            LOGGER.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        preferencesController.setUpLanguage();
        
        MainFrame frame = new MainFrame("Pogodynka", resourceController,
                preferencesController, weatherController, reportController);
        frame.showFrame();
    }
}

