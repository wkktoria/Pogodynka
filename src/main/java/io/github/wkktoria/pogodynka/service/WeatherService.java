package io.github.wkktoria.pogodynka.service;

import com.google.gson.Gson;
import io.github.wkktoria.pogodynka.api.WeatherApiResponse;
import io.github.wkktoria.pogodynka.model.Weather;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WeatherService {
    private final static Logger LOGGER = LoggerFactory.getLogger(WeatherService.class);
    private final String apiKey = System.getenv("WEATHER_API_KEY");
    private final OkHttpClient client = new OkHttpClient();

    public WeatherService() {
        if (apiKey == null || apiKey.isEmpty()) {
            LOGGER.error("Weather API key is missing");
            throw new RuntimeException("Weather API key is missing");
        }
    }

    public Weather getWeather(final String location) {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=%s", location, apiKey);

        try {
            String responseBody = getResponseBody(url);
            return convertResponseBody(responseBody);
        } catch (IOException e) {
            LOGGER.error("Unable to get weather from API", e);
        }

        return null;
    }

    protected String getResponseBody(final String url) throws IOException {
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : null;
        }
    }

    private ImageIcon getWeatherImage(final String code) {
        String imageUrl = String.format("https://openweathermap.org/img/wn/%s.png", code);

        try {
            Image image = ImageIO.read(new URI(imageUrl).toURL());
            return new ImageIcon(image);
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Unable to get image from API", e);
        }

        return null;
    }

    private Weather convertResponseBody(final String responseBody) throws IOException {
        Gson gson = new Gson();

        if (responseBody != null && responseBody.contains("404")) {
            return null;
        }

        WeatherApiResponse apiResponse = gson.fromJson(responseBody, WeatherApiResponse.class);

        return new Weather(apiResponse.getName(), apiResponse.getMain().getFeelsLike(), apiResponse.getMain().getHumidity(), getWeatherImage(apiResponse.getWeather().getFirst().getIcon()));
    }
}
