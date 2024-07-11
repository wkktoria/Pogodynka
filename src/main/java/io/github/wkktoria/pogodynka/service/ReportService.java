package io.github.wkktoria.pogodynka.service;

import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;
import io.github.wkktoria.pogodynka.config.LocaleConfig;
import io.github.wkktoria.pogodynka.controller.WeatherController;
import io.github.wkktoria.pogodynka.exception.InvalidLocationException;
import io.github.wkktoria.pogodynka.exception.ReportGenerationProblemException;
import io.github.wkktoria.pogodynka.model.Weather;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReportService {
    private final WeatherController weatherController;
    private final LocaleConfig localeConfig;

    public ReportService(final WeatherController weatherController, final LocaleConfig localeConfig) {
        this.weatherController = weatherController;
        this.localeConfig = localeConfig;
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
            document.add(new Paragraph(localeConfig.getResourceBundle().getString("weatherReport"), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            com.lowagie.text.Image weatherIcon = com.lowagie.text.Image.getInstance(weather.getImageSource());
            document.add(weatherIcon);
            document.add(new Paragraph(localeConfig.getResourceBundle().getString("location") + ": " + weather.getLocation()));
            document.add(new Paragraph(localeConfig.getResourceBundle().getString("temperature") + ": " + weather.getTemperature() + "°C"));
            document.add(new Paragraph(localeConfig.getResourceBundle().getString("humidity") + ": " + weather.getHumidity() + "%"));
            document.add(new Paragraph(localeConfig.getResourceBundle().getString("windSpeed") + ": " + weather.getWindSpeed() + " m/s"));
            document.add(new Paragraph(localeConfig.getResourceBundle().getString("pressure") + ": " + weather.getPressure() + " hPa"));
        } catch (IOException e) {
            throw new ReportGenerationProblemException();
        }
    }
}
