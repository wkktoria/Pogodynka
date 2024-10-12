package io.github.wkktoria.pogodynka.controller;

import io.github.wkktoria.pogodynka.exception.InvalidLocationException;
import io.github.wkktoria.pogodynka.service.PreferencesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreferencesController {
    private final static Logger LOGGER = LoggerFactory.getLogger(PreferencesController.class);

    private final PreferencesService preferencesService;

    public PreferencesController(final PreferencesService preferencesService) {
        this.preferencesService = preferencesService;
    }

    public String getLocation() {
        return preferencesService.getLocation();
    }

    public boolean setLocation(final String location) {
        try {
            preferencesService.setLocation(location);
            return true;
        } catch (InvalidLocationException e) {
            LOGGER.error("Cannot set location to: {}", location);
            return false;
        }
    }

    public void setLanguage(final PreferencesService.LANGUAGE language) {
        preferencesService.setLanguage(language);
    }

    public void setUpLanguage() {
        preferencesService.changeDefaultLanguage();
        LOGGER.info("Language set to: {}", preferencesService.getLanguage());
    }
}
