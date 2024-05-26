package io.github.wkktoria.weatherreport;

import java.awt.*;
import java.math.BigDecimal;

class Weather {
    private String location;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private Image image;

    Weather(String location, BigDecimal temperature, BigDecimal humidity, Image image) {
        this.location = location;
        this.temperature = temperature;
        this.humidity = humidity;
        this.image = image;
    }

    String getLocation() {
        return location;
    }

    BigDecimal getTemperature() {
        return temperature;
    }

    BigDecimal getHumidity() {
        return humidity;
    }

    Image getImage() {
        return image;
    }
}
