package io.github.wkktoria.pogodynka.service;

import io.github.wkktoria.pogodynka.config.LocaleConfig;
import io.github.wkktoria.pogodynka.controller.WeatherController;
import io.github.wkktoria.pogodynka.exception.InvalidLocationException;

import java.util.Locale;
import java.util.Objects;
import java.util.prefs.Preferences;

public class PreferencesService {
    private final static String LOCATION_KEY = "location";
    private final static String DEFAULT_LOCATION = "Warsaw";
    private final static String LANGUAGE_KEY = "language";
    private final static LANGUAGE DEFAULT_LANGUAGE = LANGUAGE.ENGLISH;
    private final Preferences preferences;
    private final WeatherController weatherController;

    public PreferencesService(final WeatherController weatherController) {
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

    public String getLanguage() {
        return preferences.get(LANGUAGE_KEY, DEFAULT_LANGUAGE.toString());
    }

    public void setLanguage(final LANGUAGE language) {
        preferences.put(LANGUAGE_KEY, language.toString());
        changeDefaultLanguage();
    }

    public void changeDefaultLanguage() {
        final String language = getLanguage();

        if (Objects.equals(language, LANGUAGE.ENGLISH.toString())) {
            LocaleConfig.getLocaleConfig().setLocale(Locale.ENGLISH);
        } else if (Objects.equals(language, LANGUAGE.POLISH.toString())) {
            LocaleConfig.getLocaleConfig().setLocale(Locale.of("pl", "PL"));
        } else if (Objects.equals(language, LANGUAGE.RUSSIAN.toString())) {
            LocaleConfig.getLocaleConfig().setLocale(Locale.of("ru", "RU"));
        }
    }

    public enum LANGUAGE {
        ENGLISH,
        POLISH,
        RUSSIAN,
    }
}
