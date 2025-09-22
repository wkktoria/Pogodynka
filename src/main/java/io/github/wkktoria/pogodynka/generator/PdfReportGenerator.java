package io.github.wkktoria.pogodynka.generator;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;
import io.github.wkktoria.pogodynka.config.LocaleConfig;
import io.github.wkktoria.pogodynka.controller.ResourceController;
import io.github.wkktoria.pogodynka.controller.WeatherController;
import io.github.wkktoria.pogodynka.exception.InvalidLocationException;
import io.github.wkktoria.pogodynka.exception.ReportGenerationProblemException;
import io.github.wkktoria.pogodynka.model.Weather;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PdfReportGenerator implements ReportGenerator {
    private final WeatherController weatherController;
    private final ResourceController resourceController;

    public PdfReportGenerator(final WeatherController weatherController,
                              final ResourceController resourceController) {
        this.weatherController = weatherController;
        this.resourceController = resourceController;
    }

    @Override
    public void generate() throws InvalidLocationException, ReportGenerationProblemException {
        generate("report", "Warsaw");
    }

    @Override
    public void generate(String filePath, String location) throws InvalidLocationException, ReportGenerationProblemException {
        if (!weatherController.isValidLocation(location)) {
            throw new InvalidLocationException();
        }

        final Weather weather = weatherController.getWeather(location);

        try (Document document = new Document()) {
            PdfWriter instance = PdfWriter.getInstance(document, new FileOutputStream(filePath));

            document.open();
            instance.getInfo().put(PdfName.CREATOR, new PdfString(Document.getVersion()));

            com.lowagie.text.Image weatherIcon = Image.getInstance(weather.getImageSource());
            weatherIcon.setAlignment(Element.ALIGN_CENTER);
            document.add(weatherIcon);

            Paragraph title = new Paragraph(
                    resourceController.getByKey("weatherReport", ResourceController.Case.UPPER_CASE)
            );
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph generateDate = new Paragraph(
                    "(" + LocalDateTime.now()
                            .format(DateTimeFormatter
                                    .ofPattern("HH:mm:ss, dd MMM yyyy",
                                            LocaleConfig.getLocaleConfig().getLocale())) + ")"
            );
            generateDate.setAlignment(Element.ALIGN_CENTER);
            document.add(generateDate);

            final Paragraph locationInfo = new Paragraph(
                    resourceController.getByKey("location", ResourceController.Case.CAPITALIZED_CASE)
                            + ": " + weather.getLocation()
            );
            document.add(locationInfo);

            final Paragraph temperatureInfo = new Paragraph(
                    resourceController.getByKey("temperature", ResourceController.Case.CAPITALIZED_CASE)
                            + ": " + weather.getTemperature() + "°C"
            );
            document.add(temperatureInfo);

            final Paragraph humidityInfo = new Paragraph(
                    resourceController.getByKey("humidity", ResourceController.Case.CAPITALIZED_CASE)
                            + ": " + weather.getHumidity() + "%"
            );
            document.add(humidityInfo);

            final Paragraph windSpeedInfo = new Paragraph(
                    resourceController.getByKey("windSpeed", ResourceController.Case.CAPITALIZED_CASE)
                            + ": " + weather.getWindSpeed() + " m/s"
            );
            document.add(windSpeedInfo);

            final Paragraph pressureInfo = new Paragraph(
                    resourceController.getByKey("pressure", ResourceController.Case.CAPITALIZED_CASE)
                            + ": " + weather.getPressure() + " hPa"
            );
            document.add(pressureInfo);
        } catch (IOException e) {
            throw new ReportGenerationProblemException();
        }
    }
}
