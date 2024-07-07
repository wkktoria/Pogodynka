package io.github.wkktoria.pogodynka.controller;

import io.github.wkktoria.pogodynka.exception.InvalidLocationException;
import io.github.wkktoria.pogodynka.service.LocationPreferencesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocationPreferencesController {
    private final static Logger LOGGER = LoggerFactory.getLogger(WeatherController.class);
    private final LocationPreferencesService locationPreferencesService;

    public LocationPreferencesController(final LocationPreferencesService locationPreferencesService) {
        this.locationPreferencesService = locationPreferencesService;
    }

    public String getLocation() {
        return locationPreferencesService.getLocation();
    }

    public boolean setLocation(final String location) {
        try {
            locationPreferencesService.setLocation(location);
            return true;
        } catch (InvalidLocationException e) {
            LOGGER.error("Cannot set location to: {}", location);
            return false;
        }
    }
}
