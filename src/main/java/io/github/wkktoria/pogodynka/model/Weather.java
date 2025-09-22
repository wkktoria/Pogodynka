package io.github.wkktoria.pogodynka.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Weather {
    private static final Weather INSTANCE = new Weather();
    private String location;
    private String imageSource;
    private Double temperature;
    private Integer humidity;
    private Double windSpeed;
    private Integer pressure;

    private Weather() {
    }

    public static Weather getWeather() {
        return INSTANCE;
    }
}