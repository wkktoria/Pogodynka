package io.github.wkktoria.pogodynka.controller;

import io.github.wkktoria.pogodynka.config.LocaleConfig;
import io.github.wkktoria.pogodynka.exception.MissingApiKeyException;
import io.github.wkktoria.pogodynka.service.ReportService;
import io.github.wkktoria.pogodynka.service.ResourceService;
import io.github.wkktoria.pogodynka.service.WeatherService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportControllerTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReportControllerTest.class);
    private final static String VALID_LOCATION = "Warsaw";
    private final static String NON_EXISTENT_LOCATION = "NonExistentLocation";
    private final static String REPORT_FILENAME = "test";

    @AfterEach
    void clenUp() {
        File file = new File(REPORT_FILENAME + ".pdf");
        if (file.delete()) {
            LOGGER.info("Deleted file {}", file.getAbsolutePath());

        }
    }

    @Test
    void generateReturnsTrueForSuccessfullyGeneratedReport() throws MissingApiKeyException {
        // given
        var weatherController = new WeatherController(new WeatherService());
        var reportService = new ReportService(weatherController, new ResourceController(new ResourceService(new LocaleConfig())));
        var reportController = new ReportController(reportService);

        // when
        var result = reportController.generate(REPORT_FILENAME, VALID_LOCATION);

        // then
        assertTrue(result);
    }

    @Test
    void generateReturnsFalseForNonExistentLocation() throws MissingApiKeyException {
        // given
        var weatherController = new WeatherController(new WeatherService());
        var reportService = new ReportService(weatherController, new ResourceController(new ResourceService(new LocaleConfig())));
        var reportController = new ReportController(reportService);

        // when
        var result = reportController.generate(REPORT_FILENAME, NON_EXISTENT_LOCATION);

        // then
        assertFalse(result);
    }

    @Test
    @ExtendWith(OutputCaptureExtension.class)
    void generateLogsErrorWhenNonExistentLocation(final CapturedOutput output) throws MissingApiKeyException {
        // given
        var weatherController = new WeatherController(new WeatherService());
        var reportService = new ReportService(weatherController, new ResourceController(new ResourceService(new LocaleConfig())));
        var reportController = new ReportController(reportService);

        // when
        reportController.generate(REPORT_FILENAME, NON_EXISTENT_LOCATION);

        // then
        assertFalse(output.isEmpty());
        assertTrue(output.getErr().contains("Cannot generate report"));
    }
}