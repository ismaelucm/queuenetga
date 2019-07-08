package com.massisframework.massis3.examples.simulator.Genetic.Calculators;

import com.massisframework.massis3.examples.genetic_v2.IndividualGAL;
import com.massisframework.massis3.examples.simulator.Components.HelpperClasses.SimulationResult;
import com.massisframework.massis3.examples.simulator.Core.Simulation;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.IndividualQueueSimConfig;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.PedestrianGA;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.PedestrianIndividual;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CalcFitnessBestPopulationConfig extends Calculators{

    public CalcFitnessBestPopulationConfig( Simulation sim,  SimulationResult resultToCompareFitness) {
        super(sim,resultToCompareFitness);
    }

    @Override
    public float calcFitness(IndividualGAL ind) {

        this._simulation.reset();
        boolean error = false;
        PedestrianIndividual individual = (PedestrianIndividual) ind;
        List<PedestrianGA> pedestrianGAList = individual.getPedestrianGAList();

        try {
            _simulation.resetPopulation(pedestrianGAList);
        } catch (Exception e) {
            e.printStackTrace();
            error = true;
        }

        if(!error)
            return calcSimilarity();
        else
            return 1.4E-45F;
    }

    @Override
    protected float calcFitness(SimulationResult simulationResult) {
        float acumulativeErrorPorcentage = 0;

        int numSensorWithAnyPedestrian = 0;
        Map<String, Integer> allResults = _resultToCompareFitness.getAllResults();

        List<String> allNodes = _simulation.getSimulator().getWorld().getAllNodes();
        for(int i = 0; i < allNodes.size(); ++i)
        {
            String nodeName = allNodes.get(i);
            int spected =  allResults.containsKey(nodeName) ? allResults.get(nodeName).intValue() : 0;
            int simulated = simulationResult.getSensorResult(nodeName);
            if(!(simulated == 0 && spected == 0)) {
                int difference = Math.abs(spected - simulated);

                float errorRelativeness = spected == 0 ? 1f : ((float) difference) / ((float) spected);
                numSensorWithAnyPedestrian++;
                acumulativeErrorPorcentage += errorRelativeness;
            }
        }

        float meanError = acumulativeErrorPorcentage / numSensorWithAnyPedestrian;

        return 1f-meanError;
    }
}
