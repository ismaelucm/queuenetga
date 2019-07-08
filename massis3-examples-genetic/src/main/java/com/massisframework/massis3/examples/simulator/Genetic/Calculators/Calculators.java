package com.massisframework.massis3.examples.simulator.Genetic.Calculators;

import com.massisframework.massis3.examples.genetic_v2.IndividualGAL;
import com.massisframework.massis3.examples.simulator.Components.HelpperClasses.SimulationResult;
import com.massisframework.massis3.examples.simulator.Core.Simulation;

import java.util.Map;
import java.util.Set;

public abstract class Calculators {

    protected Simulation _simulation;
    protected SimulationResult _resultToCompareFitness;

    public Calculators(Simulation sim, SimulationResult result)
    {
        _simulation = sim;
        this._resultToCompareFitness = result;
    }

    public abstract float calcFitness(IndividualGAL ind);

    public Simulation getSimulation()
    {
        return _simulation;
    }

    public float calcSimilarity()
    {
        boolean error = false;
        try {
            this._simulation.run();
        } catch (Exception e) {
            error = true;
            System.out.println("Error in the simulation ");
            e.printStackTrace();
        }

        if (!error) {
            SimulationResult simulationResult = (SimulationResult) this._simulation.getResultType();
            return this.calcFitness(simulationResult);
            //TODO: calculo el numero de personas detectadas por el simulador frente al detectado por el simulador de colas
            //el numero debe ser el mismo o lo mas similar posible. Eso da un fitness alto.
        } else {
            return 1.4E-45F;
        }
    }

    protected float calcFitness(SimulationResult simulationResult) {
        return calcFitnessStatic(simulationResult,_resultToCompareFitness);
    }

    public static float calcFitnessStatic(SimulationResult simulationResult,SimulationResult simulationResult2)
    {

        float acumulativeErrorPorcentage = 0f;

        int numSensorWithAnyPedestrian = 0;
        if(simulationResult2 == null)
            System.out.println("simulationResult2 es nulo");
        Set<Map.Entry<String, Integer>> entrySet = simulationResult2.getAllResults().entrySet();
        for (Map.Entry<String, Integer> entry : entrySet) {
            int spected = entry.getValue();

            if(spected > 0) {
                int simulated = simulationResult.getSensorResult(entry.getKey());
                int difference = Math.abs(spected - simulated);

                float errorRelativeness = ((float) difference) / ((float) spected);

                numSensorWithAnyPedestrian++;
                acumulativeErrorPorcentage += errorRelativeness;
            }
        }

        float meanError = acumulativeErrorPorcentage / numSensorWithAnyPedestrian;
        return 1f-meanError;
    }

    public SimulationResult getSimulationResult() {
        return _resultToCompareFitness;
    }
}
