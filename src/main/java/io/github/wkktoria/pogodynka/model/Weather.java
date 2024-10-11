package io.github.wkktoria.pogodynka.model;

public class Weather {
    private  String location;
    private  String imageSource;
    private  Double temperature;
    private  Integer humidity;
    private  Double windSpeed;
    private  Integer pressure;
    private static final Weather INSTANCE = new Weather();

    private Weather() {}

    public static Weather getWeather() {
        return INSTANCE;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    //    private Weather(final String location, final Double temperature, final Integer humidity, final String imageSource,
//                   final Double windSpeed, final Integer pressure) {
//        this.location = location;
//        this.temperature = temperature;
//        this.humidity = humidity;
//        this.imageSource = imageSource;
//        this.windSpeed = windSpeed;
//        this.pressure = pressure;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public Double getTemperature() {
//        return temperature;
//    }
//
//    public Integer getHumidity() {
//        return humidity;
//    }
//
//    public String getImageSource() {
//        return imageSource;
//    }
//
//    public Double getWindSpeed() {
//        return windSpeed;
//    }
//
//    public Integer getPressure() {
//        return pressure;
//    }
}