package io.github.wkktoria.pogodynka.controller;

import io.github.wkktoria.pogodynka.config.LocaleConfig;
import io.github.wkktoria.pogodynka.service.ResourceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ResourceControllerTest {
    @Test
    void getByKeyReturnsValueAssociatedWithKey() {
        // given
        var localeConfig = new LocaleConfig();
        localeConfig.setLocale(Locale.ENGLISH);
        var resourceController = new ResourceController(new ResourceService(localeConfig));
        final String key = "temperature";

        // when
        var result = resourceController.getByKey(key);

        // then
        assertEquals("temperature", result);
    }

    @Test
    void getByKeyReturnsValueAssociatedWithKeyInLowercase() {
        // given
        var localeConfig = new LocaleConfig();
        localeConfig.setLocale(Locale.ENGLISH);
        var resourceController = new ResourceController(new ResourceService(localeConfig));
        final String key = "temperature";

        // when
        var result = resourceController.getByKey(key, ResourceController.Case.LOWER_CASE);

        // then
        assertEquals("temperature", result);
    }

    @Test
    void getByKeyReturnsValueAssociatedWithKeyInUppercase() {
        // given
        var localeConfig = new LocaleConfig();
        localeConfig.setLocale(Locale.ENGLISH);
        var resourceController = new ResourceController(new ResourceService(localeConfig));
        final String key = "temperature";

        // when
        var result = resourceController.getByKey(key, ResourceController.Case.UPPER_CASE);

        // then
        assertEquals("TEMPERATURE", result);
    }

    @Test
    void getByKeyReturnsNullForNonExistentKey() {
        // given
        var resourceController = new ResourceController(new ResourceService(new LocaleConfig()));
        final String key = "nonexistent";

        // when
        var result = resourceController.getByKey(key);

        // then
        assertNull(result);
    }

    @Test
    @ExtendWith(OutputCaptureExtension.class)
    void getByKeyLogsErrorWhenNonExistentKey(final CapturedOutput output) {
        // given
        var resourceController = new ResourceController(new ResourceService(new LocaleConfig()));
        final String key = "nonexistent";

        // when
        resourceController.getByKey(key);

        // then
        assertFalse(output.isEmpty());
        assertTrue(output.getErr().contains("Cannot get resource"));
    }
}