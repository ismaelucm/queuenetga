package com.massisframework.massis3.examples.simulator.Configuration;

import com.massisframework.massis3.XMLHelpper;
import org.w3c.dom.Node;

import java.util.Map;

public class SimulationConfig {
    private static final String STEM_TIME = "stepTime";
    private static final String NUM_STEPS = "numSteps";
    private static final String SIMULATION_TIME = "simulationTime";

    private int stepTime;
    private int numSteps;
    private int simulationTime;

    public SimulationConfig() {
        stepTime = Integer.MAX_VALUE;
        numSteps = Integer.MAX_VALUE;
        simulationTime = Integer.MAX_VALUE;
    }

    /* <SimulationConfig stepTime="1" numSteps="100" simulationTime="10"/>*/

    public SimulationConfig(Node configNode) {
        Map<String, String> sceneAttrMap = XMLHelpper.getAttributes(configNode);
        stepTime = Integer.parseInt(sceneAttrMap.get(STEM_TIME));
        numSteps = Integer.parseInt(sceneAttrMap.get(NUM_STEPS));
        simulationTime = Integer.parseInt(sceneAttrMap.get(SIMULATION_TIME));
    }


    public void setStepTime(int stepTime) {
        this.stepTime = stepTime;
    }

    public int getStepTime() {
        return this.stepTime;
    }

    public int getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
    }

    public int getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTime(int simulationTime) {
        this.simulationTime = simulationTime;
    }
}
