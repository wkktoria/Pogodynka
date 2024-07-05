package io.github.wkktoria.pogodynka.model;

public class Weather {
    private final String location;
    private final Double temperature;
    private final Integer humidity;
    private final String imageSource;

    public Weather(final String location, final Double temperature, final Integer humidity, final String imageSource) {
        this.location = location;
        this.temperature = temperature;
        this.humidity = humidity;
        this.imageSource = imageSource;
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

    public String getImageSource() {
        return imageSource;
    }
}
