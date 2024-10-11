package io.github.wkktoria.pogodynka.service;

import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;
import io.github.wkktoria.pogodynka.controller.ResourceController;
import io.github.wkktoria.pogodynka.controller.WeatherController;
import io.github.wkktoria.pogodynka.exception.InvalidLocationException;
import io.github.wkktoria.pogodynka.exception.ReportGenerationProblemException;
import io.github.wkktoria.pogodynka.model.Weather;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReportService {
    private final WeatherController weatherController;
    private final ResourceController resourceController;

    public ReportService(final WeatherController weatherController, final ResourceController resourceController) {
        this.weatherController = weatherController;
        this.resourceController = resourceController;
    }

    public void generate(final String filename, final String location) throws ReportGenerationProblemException, InvalidLocationException {
        if (!weatherController.isValidLocation(location)) {
            throw new InvalidLocationException();
        }

        Weather weather = weatherController.getWeather(location);

        try (Document document = new Document()) {
            final File targetFile = new File(String.format("%s.pdf", filename));
            final PdfWriter instance = PdfWriter.getInstance(document, new FileOutputStream(targetFile));

            document.open();
            instance.getInfo().put(PdfName.CREATOR, new PdfString(Document.getVersion()));
            document.add(new Paragraph(resourceController.getByKey("weatherReport", ResourceController.Case.UPPER_CASE), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            com.lowagie.text.Image weatherIcon = com.lowagie.text.Image.getInstance(weather.imageSource());
            document.add(weatherIcon);
            document.add(new Paragraph(resourceController.getByKey("location", ResourceController.Case.CAPITALIZED_CASE) + ": " + weather.location()));
            document.add(new Paragraph(resourceController.getByKey("temperature", ResourceController.Case.CAPITALIZED_CASE) + ": " + weather.temperature() + "°C"));
            document.add(new Paragraph(resourceController.getByKey("humidity", ResourceController.Case.CAPITALIZED_CASE) + ": " + weather.humidity() + "%"));
            document.add(new Paragraph(resourceController.getByKey("windSpeed", ResourceController.Case.CAPITALIZED_CASE) + ": " + weather.windSpeed() + " m/s"));
            document.add(new Paragraph(resourceController.getByKey("pressure", ResourceController.Case.CAPITALIZED_CASE) + ": " + weather.pressure() + " hPa"));
        } catch (IOException e) {
            throw new ReportGenerationProblemException();
        }
    }
}
