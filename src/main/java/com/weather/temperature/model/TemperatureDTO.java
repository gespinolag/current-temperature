package com.weather.temperature.model;

public class TemperatureDTO {
    private String temperature;

    public TemperatureDTO() {
        // do nothing
    }
    public TemperatureDTO(String temperature) {
        this.temperature = temperature;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
