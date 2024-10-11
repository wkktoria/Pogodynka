package io.github.wkktoria.pogodynka.service;

import io.github.wkktoria.pogodynka.config.LocaleConfig;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceServiceTest {
    @Test
    void getByKeyReturnsValueAssociatedWithKeyForEnglishLocale() {
        // given
        var localeConfig = LocaleConfig.getLocaleConfig();
        localeConfig.setLocale(Locale.ENGLISH);
        var resourceService = new ResourceService(localeConfig);
        final String key = "temperature";

        // when
        var result = resourceService.getByKey(key);

        // then
        assertEquals("temperature", result);
    }

    @Test
    void getByKeyReturnsValueAssociatedWithKeyForPolishLocale() {
        // given
        var localeConfig = LocaleConfig.getLocaleConfig();
        localeConfig.setLocale(Locale.of("pl", "PL"));
        var resourceService = new ResourceService(localeConfig);
        final String key = "temperature";

        // when
        var result = resourceService.getByKey(key);

        // then
        assertEquals("temperatura", result);
    }

    @Test
    void getByKeyReturnsValueAssociatedWithKeyForRussianLocale() {
        // given
        var localeConfig = LocaleConfig.getLocaleConfig();
        localeConfig.setLocale(Locale.of("ru", "RU"));
        var resourceService = new ResourceService(localeConfig);
        final String key = "temperature";

        // when
        var result = resourceService.getByKey(key);

        // then
        assertEquals("температура", result);
    }
}