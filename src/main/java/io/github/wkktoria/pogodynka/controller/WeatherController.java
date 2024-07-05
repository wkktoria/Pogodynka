package io.github.wkktoria.pogodynka.controller;

import io.github.wkktoria.pogodynka.model.Weather;
import io.github.wkktoria.pogodynka.service.WeatherService;

public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(final WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public Weather getWeather(final String location) {
        return weatherService.getWeather(location);
    }

    public String getWeatherImageUrl(final String location) {
        return weatherService.getWeatherImageUrl(location);
    }
}
