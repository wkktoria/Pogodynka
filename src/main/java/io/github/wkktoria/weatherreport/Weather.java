package io.github.wkktoria.weatherreport;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.Optional;
import java.util.Scanner;

class Weather {
    private final String apiUrlString;
    private final String apiKey;
    private Optional<JSONObject> json;

    Weather(String location, String apiKey) {
        this.apiKey = apiKey;
        apiUrlString = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=%s", location, apiKey);
        json = createJsonObject(location);
    }

    Optional<Image> createImage() {
        try {
            String iconCode;
            Optional<JSONArray> weatherJsonArray = getWeatherJsonArray();
            if (json.isPresent() && weatherJsonArray.isPresent()) {
                iconCode = weatherJsonArray.get().getJSONObject(0).getString("icon");
                String imageUrlString = "https://openweathermap.org/img/wn/%s.png";
                return Optional.of(ImageIO.read(new URI(String.format(imageUrlString, iconCode)).toURL()));
            }
        } catch (IOException | URISyntaxException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    Optional<JSONObject> getJson() {
        return json;
    }

    void setJson(final String location) {
        json = createJsonObject(location);
    }

    Optional<String> getLocation() {
        return json.map(j -> j.getString("name"));
    }

    Optional<BigDecimal> getTemperature() {
        Optional<JSONObject> mainJsonObject = getMainJsonObject();
        return mainJsonObject.map(jsonObject -> jsonObject.getBigDecimal("temp"));
    }

    Optional<BigDecimal> getHumidity() {
        Optional<JSONObject> mainJsonObject = getMainJsonObject();
        return mainJsonObject.map(jsonObject -> jsonObject.getBigDecimal("humidity"));
    }

    private Optional<JSONObject> getMainJsonObject() {
        return json.map(jsonObject -> jsonObject.getJSONObject("main"));
    }

    private Optional<JSONArray> getWeatherJsonArray() {
        return json.map(jsonObject -> jsonObject.getJSONArray("weather"));
    }

    private Optional<JSONObject> createJsonObject(final String location) {
        try {
            URI uri = new URI(String.format(String.format(apiUrlString, location, apiKey)));
            URLConnection connection = uri.toURL().openConnection();
            InputStream inputStream = connection.getInputStream();
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            String response = scanner.hasNext() ? scanner.next() : "";
            return Optional.of(new JSONObject(response));
        } catch (URISyntaxException | IOException e) {
            return Optional.empty();
        }
    }
}
