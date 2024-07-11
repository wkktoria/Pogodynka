package io.github.wkktoria.pogodynka.service;

import io.github.wkktoria.pogodynka.config.LocaleConfig;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceServiceTest {
    @Test
    void getByKeyReturnsValueAssociatedWithKeyForEnglishLocale() {
        // given
        var localeConfig = new LocaleConfig();
        localeConfig.setLocale(Locale.ENGLISH);
        var resourceService = new ResourceService(localeConfig);
        final String key = "temperature";

        // when
        var result = resourceService.getByKey(key);

        // then
        assertEquals("Temperature", result);
    }

    @Test
    void getByKeyReturnsValueAssociatedWithKeyForPolishLocale() {
        // given
        var localeConfig = new LocaleConfig();
        localeConfig.setLocale(Locale.of("pl", "PL"));
        var resourceService = new ResourceService(localeConfig);
        final String key = "temperature";

        // when
        var result = resourceService.getByKey(key);

        // then
        assertEquals("Temperatura", result);
    }
}