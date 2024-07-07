package io.github.wkktoria.pogodynka.service;

import io.github.wkktoria.pogodynka.controller.WeatherController;
import io.github.wkktoria.pogodynka.exception.InvalidLocationException;

import java.util.prefs.Preferences;

public class LocationPreferencesService {
    private final static String LOCATION_KEY = "location";
    private final static String DEFAULT_LOCATION = "Warsaw";
    private final Preferences preferences;
    private final WeatherController weatherController;

    public LocationPreferencesService(final WeatherController weatherController) {
        this.preferences = Preferences.userRoot().node(this.getClass().getName());
        this.weatherController = weatherController;
    }

    public String getLocation() {
        return preferences.get(LOCATION_KEY, DEFAULT_LOCATION);
    }

    public void setLocation(final String location) throws InvalidLocationException {
        if (weatherController.isValidLocation(location)) {
            preferences.put(LOCATION_KEY, location);
        } else {
            throw new InvalidLocationException();
        }
    }
}
