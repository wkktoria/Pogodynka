package io.github.wkktoria.pogodynka.service;

import io.github.wkktoria.pogodynka.controller.WeatherController;
import io.github.wkktoria.pogodynka.exception.InvalidLocationException;
import io.github.wkktoria.pogodynka.exception.MissingApiKeyException;
import io.github.wkktoria.pogodynka.exception.ReportGenerationProblemException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportServiceTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReportServiceTest.class);
    private final static String VALID_LOCATION = "Warsaw";
    private final static String NON_EXISTENT_LOCATION = "NonExistentLocation";

    @AfterEach
    void clenUp() {
        for (File file : Objects.requireNonNull(new File(System.getProperty("user.dir")).listFiles())) {
            if (file.getName().startsWith(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                    && file.getName().endsWith(".pdf") && file.getName().contains(VALID_LOCATION)) {
                if (file.delete()) {
                    LOGGER.info("Deleted file {}", file.getAbsolutePath());
                }
            }
        }
    }

    @Test
    void generateCreatesPdfWithWeatherReport() throws MissingApiKeyException, InvalidLocationException, ReportGenerationProblemException {
        // given
        var weatherController = new WeatherController(new WeatherService());
        var reportService = new ReportService(weatherController);
        var expectedFile = new File(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "-" + VALID_LOCATION + "-report" + ".pdf");

        // when
        reportService.generate(VALID_LOCATION);

        // then
        assertTrue(expectedFile.exists());
    }

    @Test
    void generateThrowsInvalidLocationExceptionForNonExistentLocation() throws MissingApiKeyException {
        // given
        var weatherController = new WeatherController(new WeatherService());
        var reportService = new ReportService(weatherController);

        // when + then
        assertThrows(InvalidLocationException.class, () -> reportService.generate(NON_EXISTENT_LOCATION));
    }
}