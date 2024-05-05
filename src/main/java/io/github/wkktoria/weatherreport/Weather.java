package io.github.wkktoria.weatherreport;

import java.math.BigDecimal;

class Weather {
    private String location;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private String imageUrlString;

    Weather(final String location, final BigDecimal temperature, final BigDecimal humidity, final String imageUrlString) {
        this.location = location;
        this.temperature = temperature;
        this.humidity = humidity;
        this.imageUrlString = imageUrlString;
    }

    String getLocation() {
        return location;
    }

    void setLocation(final String location) {
        this.location = location;
    }

    BigDecimal getTemperature() {
        return temperature;
    }

    void setTemperature(final BigDecimal temperature) {
        this.temperature = temperature;
    }

    BigDecimal getHumidity() {
        return humidity;
    }

    void setHumidity(final BigDecimal humidity) {
        this.humidity = humidity;
    }

    String getImageUrlString() {
        return imageUrlString;
    }

    void setImageUrlString(final String imageUrlString) {
        this.imageUrlString = imageUrlString;
    }
}
