package io.github.wkktoria.pogodynka.controller;

import io.github.wkktoria.pogodynka.service.ReportService;

public class ReportController {
    private final ReportService reportService;

    public ReportController(final ReportService reportService) {
        this.reportService = reportService;
    }

    public void generate(final String location) {
        reportService.generate(location);
    }
}
