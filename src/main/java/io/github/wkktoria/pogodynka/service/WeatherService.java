package io.github.wkktoria.pogodynka.service;

import com.google.gson.Gson;
import io.github.wkktoria.pogodynka.api.WeatherApiResponse;
import io.github.wkktoria.pogodynka.exception.ApiProblemException;
import io.github.wkktoria.pogodynka.exception.InvalidApiKeyException;
import io.github.wkktoria.pogodynka.exception.InvalidLocationException;
import io.github.wkktoria.pogodynka.exception.MissingApiKeyException;
import io.github.wkktoria.pogodynka.model.Weather;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class WeatherService {
    private final static String INVALID_LOCATION_MESSAGE = "city not found";
    private final static String INVALID_API_KEY_MESSAGE = "Invalid API key";
    private final static String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=%s";
    private final static String API_KEY = System.getenv("WEATHER_API_KEY");
    private final OkHttpClient client = new OkHttpClient();

    public WeatherService() throws MissingApiKeyException {
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new MissingApiKeyException("Weather API key is missing");
        }
    }

    public Weather getWeather(final String location) throws ApiProblemException, InvalidLocationException, InvalidApiKeyException {
        final String url = String.format(WEATHER_API_URL, location, API_KEY);

        try {
            final String responseBody = getResponseBody(url);
            return convertResponseBody(responseBody);
        } catch (IOException e) {
            throw new ApiProblemException("Unable to get weather data from API");
        } catch (InvalidLocationException e) {
            throw new InvalidLocationException("Unable to get weather data for location: " + location);
        } catch (InvalidApiKeyException e) {
            throw new InvalidApiKeyException("Unable to get data from API with provided API key");
        }
    }

    public boolean isValidLocation(final String location) throws ApiProblemException {
        final String url = String.format(WEATHER_API_URL, location, API_KEY);

        try {
            final String responseBody = getResponseBody(url);
            return responseBody != null && !responseBody.contains(INVALID_LOCATION_MESSAGE);
        } catch (IOException e) {
            throw new ApiProblemException("Unable to get weather data from API");
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

    private Weather convertResponseBody(final String responseBody) throws InvalidLocationException, InvalidApiKeyException {
        Gson gson = new Gson();

        if (responseBody != null && responseBody.contains(INVALID_LOCATION_MESSAGE)) {
            throw new InvalidLocationException("Unable to find weather data for given location");
        }

        if (responseBody != null && responseBody.contains(INVALID_API_KEY_MESSAGE)) {
            throw new InvalidApiKeyException("Provided API key is invalid");
        }

        WeatherApiResponse apiResponse = gson.fromJson(responseBody, WeatherApiResponse.class);

        Weather weather = Weather.getWeather();

        weather.setLocation(apiResponse.getName());
        weather.setImageSource(getWeatherImageSource(apiResponse.getWeather().getFirst().getIcon()));
        weather.setTemperature(apiResponse.getMain().getFeelsLike());
        weather.setHumidity(apiResponse.getMain().getHumidity());
        weather.setWindSpeed(apiResponse.getWind().getSpeed());
        weather.setPressure(apiResponse.getMain().getPressure());

        return weather;
    }
}
