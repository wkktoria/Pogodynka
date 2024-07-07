package io.github.wkktoria.pogodynka.controller;

import io.github.wkktoria.pogodynka.exception.InvalidLocationException;
import io.github.wkktoria.pogodynka.exception.ReportGenerationProblemException;
import io.github.wkktoria.pogodynka.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReportController.class);
    private final ReportService reportService;

    public ReportController(final ReportService reportService) {
        this.reportService = reportService;
    }

    public boolean generate(final String location) {
        try {
            reportService.generate(location);
            return true;
        } catch (ReportGenerationProblemException | InvalidLocationException e) {
            LOGGER.error("Cannot generate report");
            return false;
        }
    }
}
