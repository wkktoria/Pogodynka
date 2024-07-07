package io.github.wkktoria.pogodynka.service;

import com.google.gson.Gson;
import io.github.wkktoria.pogodynka.api.WeatherApiResponse;
import io.github.wkktoria.pogodynka.exception.ApiProblemException;
import io.github.wkktoria.pogodynka.exception.InvalidLocationException;
import io.github.wkktoria.pogodynka.exception.MissingApiKeyException;
import io.github.wkktoria.pogodynka.model.Weather;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WeatherService {
    private final static Logger LOGGER = LoggerFactory.getLogger(WeatherService.class);
    private final static String INVALID_LOCATION_MESSAGE = "city not found";
    private final static String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=%s";
    private final String apiKey = System.getenv("WEATHER_API_KEY");
    private final OkHttpClient client = new OkHttpClient();

    public WeatherService() throws MissingApiKeyException {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new MissingApiKeyException("Weather API key is missing");
        }
    }

    public Weather getWeather(final String location) throws ApiProblemException {
        final String url = String.format(WEATHER_API_URL, location, apiKey);

        try {
            final String responseBody = getResponseBody(url);
            return convertResponseBody(responseBody);
        } catch (IOException e) {
            throw new ApiProblemException("Unable to get weather data from API");
        } catch (InvalidLocationException e) {
            LOGGER.error("Unable to get weather data for location: {}", location);
            return null;
        }
    }

    public boolean isValidLocation(final String location) {
        final String url = String.format(WEATHER_API_URL, location, apiKey);

        try {
            final String responseBody = getResponseBody(url);
            return responseBody != null && !responseBody.contains(INVALID_LOCATION_MESSAGE);
        } catch (IOException e) {
            LOGGER.error("Unable to get weather data from API");
            return false;
        }
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

    private Weather convertResponseBody(final String responseBody) throws InvalidLocationException {
        Gson gson = new Gson();

        if (responseBody != null && responseBody.contains(INVALID_LOCATION_MESSAGE)) {
            throw new InvalidLocationException("Unable to find weather data for given location");
        }

        WeatherApiResponse apiResponse = gson.fromJson(responseBody, WeatherApiResponse.class);

        return new Weather(apiResponse.getName(), apiResponse.getMain().getFeelsLike(),
                apiResponse.getMain().getHumidity(), getWeatherImageSource(apiResponse.getWeather().getFirst().getIcon()),
                apiResponse.getWind().getSpeed(), apiResponse.getMain().getPressure());
    }
}
