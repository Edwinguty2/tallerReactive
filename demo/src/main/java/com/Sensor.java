package com;

public class Sensor {
    private final String id;
    private final double temperature;

    public Sensor(String id, double temperature) {
        this.id = id;
        this.temperature = temperature;
    }

    public String getId() {
        return id;
    }

    public double getTemperature() {
        return temperature;
    }
    
}
