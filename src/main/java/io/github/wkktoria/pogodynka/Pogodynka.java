package io.github.wkktoria.pogodynka;

import com.formdev.flatlaf.FlatDarkLaf;
import io.github.wkktoria.pogodynka.config.LocaleConfig;
import io.github.wkktoria.pogodynka.controller.LocationPreferencesController;
import io.github.wkktoria.pogodynka.controller.ReportController;
import io.github.wkktoria.pogodynka.controller.ResourceController;
import io.github.wkktoria.pogodynka.controller.WeatherController;
import io.github.wkktoria.pogodynka.exception.ApiProblemException;
import io.github.wkktoria.pogodynka.exception.MissingApiKeyException;
import io.github.wkktoria.pogodynka.service.LocationPreferencesService;
import io.github.wkktoria.pogodynka.service.ReportService;
import io.github.wkktoria.pogodynka.service.ResourceService;
import io.github.wkktoria.pogodynka.service.WeatherService;
import io.github.wkktoria.pogodynka.view.MainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Pogodynka {
    private static final Logger LOGGER = LoggerFactory.getLogger(Pogodynka.class);
    private static final WeatherController weatherController;
    private static final ReportController reportController;
    private static final LocationPreferencesController locationPreferencesController;
    private static final ResourceController resourceController;

    static {
        try {
            resourceController = new ResourceController(new ResourceService(LocaleConfig.getLocaleConfig()));
            weatherController = new WeatherController(new WeatherService());
            reportController = new ReportController(new ReportService(weatherController, resourceController));
            locationPreferencesController = new LocationPreferencesController(new LocationPreferencesService(weatherController));

            if (weatherController.getWeather(locationPreferencesController.getLocation()) == null) {
                throw new ApiProblemException("Couldn't get weather information");
            }
        } catch (MissingApiKeyException | ApiProblemException e) {
            LOGGER.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        MainFrame frame = new MainFrame("Pogodynka", resourceController,
                locationPreferencesController, weatherController, reportController);
        frame.showFrame();
    }
}

