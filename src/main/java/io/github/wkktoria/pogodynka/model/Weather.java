package io.github.wkktoria.pogodynka.model;

public record Weather(String location, Double temperature, Integer humidity, String imageSource, Double windSpeed,
                      Integer pressure) {
}
