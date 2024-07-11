package io.github.wkktoria.pogodynka.service;

import io.github.wkktoria.pogodynka.config.LocaleConfig;

public class ResourceService {
    private final LocaleConfig localeConfig;

    public ResourceService(final LocaleConfig localeConfig) {
        this.localeConfig = localeConfig;
    }

    public String getByKey(final String key) {
        return localeConfig.getResourceBundle().getString(key);
    }
}
