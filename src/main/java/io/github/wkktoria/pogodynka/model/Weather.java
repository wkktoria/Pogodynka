package io.github.wkktoria.pogodynka.model;

public class Weather {
    private final String location;
    private final Double temperature;
    private final Integer humidity;
    private final String imageSource;
    private final Double windSpeed;
    private final Integer pressure;

    public Weather(final String location, final Double temperature, final Integer humidity, final String imageSource,
                   final Double windSpeed, final Integer pressure) {
        this.location = location;
        this.temperature = temperature;
        this.humidity = humidity;
        this.imageSource = imageSource;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
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

    public Double getWindSpeed() {
        return windSpeed;
    }

    public Integer getPressure() {
        return pressure;
    }
}
