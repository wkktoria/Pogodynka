package io.github.wkktoria.pogodynka.controller;

import io.github.wkktoria.pogodynka.exception.MissingApiKeyException;
import io.github.wkktoria.pogodynka.service.ReportService;
import io.github.wkktoria.pogodynka.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportControllerTest {
    private final static String VALID_LOCATION = "Warsaw";
    private final static String NON_EXISTENT_LOCATION = "NonExistentLocation";

    @Test
    void generateReturnsTrueForSuccessfullyGeneratedReport() throws MissingApiKeyException {
        // given
        var weatherController = new WeatherController(new WeatherService());
        var reportService = new ReportService(weatherController);
        var reportController = new ReportController(reportService);

        // when
        var result = reportController.generate(VALID_LOCATION);

        // then
        assertTrue(result);
    }

    @Test
    void generateReturnsFalseForNonExistentLocation() throws MissingApiKeyException {
        // given
        var weatherController = new WeatherController(new WeatherService());
        var reportService = new ReportService(weatherController);
        var reportController = new ReportController(reportService);

        // when
        var result = reportController.generate(NON_EXISTENT_LOCATION);

        // then
        assertFalse(result);
    }

    @Test
    @ExtendWith(OutputCaptureExtension.class)
    void generateLogsErrorWhenNonExistentLocation(final CapturedOutput output) throws MissingApiKeyException {
        // given
        var weatherController = new WeatherController(new WeatherService());
        var reportService = new ReportService(weatherController);
        var reportController = new ReportController(reportService);

        // when
        var result = reportController.generate(NON_EXISTENT_LOCATION);

        // then
        assertFalse(output.isEmpty());
        assertTrue(output.getErr().contains("Cannot generate report"));
    }
}