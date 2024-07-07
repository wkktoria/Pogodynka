package io.github.wkktoria.pogodynka.exception;

public class ReportGenerationProblemException extends Exception {
    public ReportGenerationProblemException() {
        super("Report generation problem");
    }

    public ReportGenerationProblemException(String message) {
        super(message);
    }
}
