package com.massisframework.massis3.examples.simulator.Components.HelpperClasses;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimulationResult {

    Map<String, Integer> _sensorPedestrianDetected;

    public SimulationResult() {
        _sensorPedestrianDetected = new LinkedHashMap<>();
    }

    public void setSensorResult(String sensor, int value) {
        _sensorPedestrianDetected.put(sensor, value);
    }

    public int getSensorResult(String sensor) {
        if(!_sensorPedestrianDetected.containsKey(sensor))
            throw new RuntimeException("sensor "+sensor + " not found SimulationResult");
        return _sensorPedestrianDetected.get(sensor).intValue();
    }

    public Map<String, Integer> getAllResults() {
        if(_sensorPedestrianDetected == null)
            System.out.println("Error los sensores de presencia. No se ha detectado ningún pedestrian");
        return _sensorPedestrianDetected;
    }
}