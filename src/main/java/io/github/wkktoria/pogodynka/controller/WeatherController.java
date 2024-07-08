package io.github.wkktoria.pogodynka.controller;

import io.github.wkktoria.pogodynka.exception.ApiProblemException;
import io.github.wkktoria.pogodynka.exception.InvalidApiKeyException;
import io.github.wkktoria.pogodynka.exception.InvalidLocationException;
import io.github.wkktoria.pogodynka.model.Weather;
import io.github.wkktoria.pogodynka.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherController {
    private final static Logger LOGGER = LoggerFactory.getLogger(WeatherController.class);
    private final WeatherService weatherService;

    public WeatherController(final WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public Weather getWeather(final String location) {
        try {
            return weatherService.getWeather(location);
        } catch (ApiProblemException | InvalidLocationException | InvalidApiKeyException e) {
            LOGGER.error(e.getLocalizedMessage());
            return null;
        }
    }

    public boolean isValidLocation(final String location) {
        try {
            return weatherService.isValidLocation(location);
        } catch (ApiProblemException e) {
            LOGGER.error(e.getLocalizedMessage());
            return false;
        }
    }
}
