package io.github.wkktoria.pogodynka.controller;

import io.github.wkktoria.pogodynka.exception.MissingApiKeyException;
import io.github.wkktoria.pogodynka.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.junit.jupiter.api.Assertions.*;

class WeatherControllerTest {
    private final static String VALID_LOCATION = "Warsaw";
    private final static String NON_EXISTENT_LOCATION = "NonExistentLocation";

    @Test
    void getWeatherReturnsWeather() throws MissingApiKeyException {
        // given
        var weatherService = new WeatherService();
        var weatherController = new WeatherController(weatherService);

        // when
        var result = weatherController.getWeather(VALID_LOCATION);

        // then
        assertNotNull(result);
        assertEquals(VALID_LOCATION, result.location());
        assertNotNull(result.temperature());
        assertNotNull(result.humidity());
        assertNotNull(result.imageSource());
        assertNotNull(result.windSpeed());
        assertNotNull(result.pressure());
    }

    @Test
    void getWeatherReturnsNullForNonExistentLocation() throws MissingApiKeyException {
        // given
        var weatherService = new WeatherService();
        var weatherController = new WeatherController(weatherService);

        // when
        var result = weatherController.getWeather(NON_EXISTENT_LOCATION);

        // then
        assertNull(result);
    }

    @Test
    @ExtendWith(OutputCaptureExtension.class)
    void getWeatherLogsErrorWhenNonExistentLocation(final CapturedOutput output) throws MissingApiKeyException {
        // given
        var weatherService = new WeatherService();
        var weatherController = new WeatherController(weatherService);

        // when
        weatherController.getWeather(NON_EXISTENT_LOCATION);

        // then
        assertFalse(output.isEmpty());
        assertTrue(output.getErr().contains("Unable to get weather data for location"));
    }

    @Test
    void isValidLocationReturnsTrueForExistingLocation() throws MissingApiKeyException {
        // given
        var weatherService = new WeatherService();
        var weatherController = new WeatherController(weatherService);

        // when
        var result = weatherController.isValidLocation(VALID_LOCATION);

        // then
        assertTrue(result);
    }

    @Test
    void isValidLocationReturnsFalseForNonExistentLocation() throws MissingApiKeyException {
        // given
        var weatherService = new WeatherService();
        var weatherController = new WeatherController(weatherService);

        // when
        var result = weatherController.isValidLocation(NON_EXISTENT_LOCATION);

        // then
        assertFalse(result);
    }
}