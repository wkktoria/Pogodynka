package io.github.wkktoria.pogodynka.service;

import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;
import io.github.wkktoria.pogodynka.controller.WeatherController;
import io.github.wkktoria.pogodynka.model.Weather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
    private final WeatherController weatherController;

    public ReportService(final WeatherController weatherController) {
        this.weatherController = weatherController;
    }

    public void generate(final String location) {
        Weather weather = weatherController.getWeather(location);

        try (Document document = new Document()) {
            final File targetFile = new File(String.format("%s-%s-report.pdf", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), weather.getLocation()));
            final PdfWriter instance = PdfWriter.getInstance(document, new FileOutputStream(targetFile));

            document.open();
            instance.getInfo().put(PdfName.CREATOR, new PdfString(Document.getVersion()));
            document.add(new Paragraph("Weather Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            com.lowagie.text.Image weatherIcon = com.lowagie.text.Image.getInstance(weather.getImageSource());
            document.add(weatherIcon);
            document.add(new Paragraph("Location: " + weather.getLocation()));
            document.add(new Paragraph("Temperature: " + weather.getTemperature() + "°C"));
            document.add(new Paragraph("Humidity: " + weather.getHumidity() + "%"));
            document.add(new Paragraph("Wind speed: " + weather.getWindSpeed() + " m/s"));
            document.add(new Paragraph("Pressure: " + weather.getPressure() + " hPa"));
        } catch (IOException e) {
            LOGGER.error("Unable to generate report", e);
        }
    }
}
