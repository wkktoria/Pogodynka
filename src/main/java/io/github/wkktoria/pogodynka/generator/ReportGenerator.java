package io.github.wkktoria.pogodynka.generator;

import io.github.wkktoria.pogodynka.exception.InvalidLocationException;
import io.github.wkktoria.pogodynka.exception.ReportGenerationProblemException;

public interface ReportGenerator {
    void generate() throws InvalidLocationException, ReportGenerationProblemException;

    void generate(final String filename, final String location) throws InvalidLocationException, ReportGenerationProblemException;
}
