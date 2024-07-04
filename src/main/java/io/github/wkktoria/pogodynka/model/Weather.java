package io.github.wkktoria.pogodynka.model;

import javax.swing.*;

public class Weather {
    private final String location;
    private final Double temperature;
    private final Integer humidity;
    private final ImageIcon image;

    public Weather(final String location, final Double temperature, final Integer humidity, final ImageIcon image) {
        this.location = location;
        this.temperature = temperature;
        this.humidity = humidity;
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public ImageIcon getImage() {
        return image;
    }
}
