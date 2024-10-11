package io.github.wkktoria.pogodynka.service;

import io.github.wkktoria.pogodynka.exception.ApiProblemException;
import io.github.wkktoria.pogodynka.exception.InvalidApiKeyException;
import io.github.wkktoria.pogodynka.exception.InvalidLocationException;
import io.github.wkktoria.pogodynka.exception.MissingApiKeyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherServiceTest {
    private final static String VALID_LOCATION = "Warsaw";
    private final static String NON_EXISTENT_LOCATION = "NonExistentLocation";

    @Test
    void getWeatherReturnsWeather() throws MissingApiKeyException, ApiProblemException, InvalidLocationException, InvalidApiKeyException {
        // given
        var weatherService = new WeatherService();

        // when
        var result = weatherService.getWeather(VALID_LOCATION);

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
    void getWeatherThrowsInvalidLocationExceptionForNonExistentLocation() throws MissingApiKeyException {
        // given
        var weatherService = new WeatherService();

        // when + then
        assertThrows(InvalidLocationException.class, () -> weatherService.getWeather(NON_EXISTENT_LOCATION));
    }

    @Test
    void isValidLocationReturnsTrueForExistingLocation() throws MissingApiKeyException, ApiProblemException {
        // given
        var weatherService = new WeatherService();

        // when
        var result = weatherService.isValidLocation(VALID_LOCATION);

        // then
        assertTrue(result);
    }

    @Test
    void isValidLocationReturnsFalseForNonExistentLocation() throws MissingApiKeyException, ApiProblemException {
        // given
        var weatherService = new WeatherService();

        // when
        var result = weatherService.isValidLocation(NON_EXISTENT_LOCATION);

        // then
        assertFalse(result);
    }
}