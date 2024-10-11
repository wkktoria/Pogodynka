package io.github.wkktoria.pogodynka.config;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleConfig {
    private Locale locale;
    private ResourceBundle resourceBundle;
    private static final LocaleConfig INSTANCE = new LocaleConfig();

    private LocaleConfig() {
        this.locale = Locale.getDefault();
        resourceBundle = ResourceBundle.getBundle("pogodynka.resource", locale);
    }

    public static LocaleConfig getLocaleConfig() {
        return INSTANCE;
    }

    public void setLocale(final Locale locale) {
        this.locale = locale;
        resourceBundle = ResourceBundle.getBundle("pogodynka.resource", locale);
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
}
