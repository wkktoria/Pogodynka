package io.github.wkktoria.pogodynka.generator;

import io.github.wkktoria.pogodynka.config.LocaleConfig;
import io.github.wkktoria.pogodynka.controller.ResourceController;
import io.github.wkktoria.pogodynka.controller.WeatherController;
import io.github.wkktoria.pogodynka.exception.InvalidLocationException;
import io.github.wkktoria.pogodynka.exception.MissingApiKeyException;
import io.github.wkktoria.pogodynka.exception.ReportGenerationProblemException;
import io.github.wkktoria.pogodynka.service.ResourceService;
import io.github.wkktoria.pogodynka.service.WeatherService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class PdfReportGeneratorTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(PdfReportGeneratorTest.class);
    private final static String VALID_LOCATION = "Warsaw";
    private final static String NON_EXISTENT_LOCATION = "NonExistentLocation";
    private final static String REPORT_FILENAME = "report";

    @AfterEach
    void cleanUp() {
        File file = new File(REPORT_FILENAME + ".pdf");
        if (file.delete()) {
            LOGGER.info("Deleted file {}", file.getAbsolutePath());

        }
    }

    @Test
    void generateCreatesPdf() throws InvalidLocationException, ReportGenerationProblemException, MissingApiKeyException {
        // given
        var weatherController = new WeatherController(new WeatherService());
        var reportGenerator = new PdfReportGenerator(weatherController,
                new ResourceController(
                        new ResourceService(LocaleConfig.getLocaleConfig())));

        // when
        reportGenerator.generate(REPORT_FILENAME, VALID_LOCATION);

        // then
        assertTrue(new File(REPORT_FILENAME + ".pdf").exists());
    }

    @Test
    void generateCreatesPdfWithDefaultFilenameAndLocation() throws MissingApiKeyException, InvalidLocationException, ReportGenerationProblemException {
        // given
        var weatherController = new WeatherController(new WeatherService());
        var reportGenerator = new PdfReportGenerator(weatherController,
                new ResourceController(
                        new ResourceService(LocaleConfig.getLocaleConfig())));

        // when
        reportGenerator.generate();

        // then
        assertTrue(new File(REPORT_FILENAME + ".pdf").exists());
    }

    @Test
    void generateThrowsInvalidLocationExceptionForNonExistentLocation() throws MissingApiKeyException {
        // given
        var weatherController = new WeatherController(new WeatherService());
        var reportGenerator = new PdfReportGenerator(weatherController,
                new ResourceController(
                        new ResourceService(LocaleConfig.getLocaleConfig())));

        // when + then
        assertThrows(InvalidLocationException.class,
                () -> reportGenerator.generate(REPORT_FILENAME, NON_EXISTENT_LOCATION));
    }
}