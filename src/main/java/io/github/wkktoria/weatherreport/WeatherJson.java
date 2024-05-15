package io.github.wkktoria.weatherreport;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.Scanner;

class WeatherJson {
    private final static Logger logger = LoggerFactory.getLogger(WeatherJson.class);

    private final String apiKey;

    WeatherJson(String apiKey) {
        this.apiKey = apiKey;
    }

    private Image getImage(final String location) {
        JSONArray jsonArray = createJsonObject(location).getJSONArray("weather");
        String iconCode = jsonArray.getJSONObject(0).getString("icon");
        String imageUrl = "https://openweathermap.org/img/wn/%s.png";
        try {
            return ImageIO.read(new URI(String.format(imageUrl, iconCode)).toURL());
        } catch (URISyntaxException | IOException e) {
            logger.error("Failed to get image with code '{}' from URL: {}", iconCode, imageUrl);
            throw new WeatherJsonException("Could not get image with code " + iconCode);
        }
    }

    Weather createWeather(final String location) {
        return new Weather(getLocation(location), getTemperature(location), getHumidity(location), getImage(location));
    }

    private String getLocation(final String location) {
        JSONObject jsonObject = createJsonObject(location);
        return jsonObject.getString("name");
    }

    private BigDecimal getTemperature(final String location) {
        JSONObject jsonObject = createJsonObject(location).getJSONObject("main");
        return jsonObject.getBigDecimal("temp");
    }

    private BigDecimal getHumidity(final String location) {
        JSONObject jsonObject = createJsonObject(location).getJSONObject("main");
        return jsonObject.getBigDecimal("humidity");
    }

    private JSONObject createJsonObject(final String location) {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=%s", location, apiKey);
        try {
            URI uri = new URI(url);
            URLConnection connection = uri.toURL().openConnection();
            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            String response = scanner.hasNext() ? scanner.next() : "";
            return new JSONObject(response);
        } catch (URISyntaxException | IOException e) {
            logger.error("Failed to create JSON object from URL: {}", url);
            throw new WeatherJsonException("Could not create JSON object for location: " + location);
        }
    }

    protected static class WeatherJsonException extends RuntimeException {
        public WeatherJsonException(String message) {
            super(message);
        }
    }
}
