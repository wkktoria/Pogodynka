package io.github.wkktoria.pogodynka.service;

import com.google.gson.Gson;
import io.github.wkktoria.pogodynka.api.WeatherApiResponse;
import io.github.wkktoria.pogodynka.model.Weather;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
        final String weatherApiUrl = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=%s";
        final String url = String.format(weatherApiUrl, location, apiKey);

        try {
            final String responseBody = getResponseBody(url);
            return convertResponseBody(responseBody);
        } catch (IOException e) {
            LOGGER.error("Unable to get weather from API", e);
        }

        return null;
    }

    private String getResponseBody(final String url) throws IOException {
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : null;
        }
    }

    private String getWeatherImageSource(final String code) {
        final String weatherImageUrl = "https://openweathermap.org/img/wn/%s.png";
        return String.format(weatherImageUrl, code);
    }

    private Weather convertResponseBody(final String responseBody) throws IOException {
        Gson gson = new Gson();

        final String locationNotFoundMessage = "city not found";
        if (responseBody != null && responseBody.contains(locationNotFoundMessage)) {
            return null;
        }

        WeatherApiResponse apiResponse = gson.fromJson(responseBody, WeatherApiResponse.class);

        return new Weather(apiResponse.getName(), apiResponse.getMain().getFeelsLike(),
                apiResponse.getMain().getHumidity(), getWeatherImageSource(apiResponse.getWeather().getFirst().getIcon()),
                apiResponse.getWind().getSpeed(), apiResponse.getMain().getPressure());
    }
}
