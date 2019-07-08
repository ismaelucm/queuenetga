package com.massisframework.massis3.examples.simulator.Genetic.Problems;

import com.massisframework.massis3.Dice;
import com.massisframework.massis3.examples.Threads.*;
import com.massisframework.massis3.examples.genetic_v2.*;
import com.massisframework.massis3.examples.simulator.Components.HelpperClasses.SimulationResult;
import com.massisframework.massis3.examples.simulator.Core.Simulation;
import com.massisframework.massis3.examples.simulator.Genetic.Calculators.CalcFitnessBestPopulationConfig;
import com.massisframework.massis3.examples.simulator.Genetic.Calculators.CalcFitnessBestQueueSim;
import com.massisframework.massis3.examples.simulator.Genetic.Calculators.Calculators;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.IndividualQueueSimConfig;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.PedestrianIndividual;
import com.massisframework.massis3.examples.simulator.Genetic.Populations.PopulationPedestrianConfig;
import com.massisframework.massis3.examples.simulator.Genetic.Populations.PopulationQueueSimConfig;
import com.massisframework.massis3.examples.simulator.World.World;

public class FindBestWorldConfiguration extends GeneticALProblem{

    protected int _pedestrianPathSize;
    protected int _numIndividualsPerInd;
    protected float _mutationRate;
    protected World _world;


    public FindBestWorldConfiguration(Simulation sim, int numIndividualsPerInd, int pedestrianPathSize,float mutationRate, SimulationResult simulationResult, CrossMethod cross, SelectionMethod select) {
        super(cross, select);
        _pedestrianPathSize = pedestrianPathSize;
        _numIndividualsPerInd = numIndividualsPerInd;
        _mutationRate = mutationRate;
        //String arquetype, Simulation sim,  SimulationResult resultToCompareFitness
        addNewCalculator(new CalcFitnessBestPopulationConfig(sim,simulationResult));
        _world = sim.getSimulator().getWorld();
    }

    public FindBestWorldConfiguration(Calculators calc, int numIndividualsPerInd, int pedestrianPathSize,float mutationRate, CrossMethod cross, SelectionMethod select) {
        super(cross, select);
        _pedestrianPathSize = pedestrianPathSize;
        _numIndividualsPerInd = numIndividualsPerInd;
        _mutationRate = mutationRate;
        addNewCalculator(calc);
        _world = calc.getSimulation().getSimulator().getWorld();
    }

    public FindBestWorldConfiguration(World world, int numIndividualsPerInd,int pedestrianPathSize, float mutationRate, CrossMethod cross, SelectionMethod select, WorkerPull workerPull) {
        super(cross, select, workerPull);
        _pedestrianPathSize = pedestrianPathSize;
        _numIndividualsPerInd = numIndividualsPerInd;
        _mutationRate = mutationRate;
        _world = world;
    }

    @Override
    public void pararellEvaluate(PopulationGAL population) {
        BestPopulationconfigWorkerPull workerPull = this.<BestPopulationconfigWorkerPull>getWorkerPull();
        for (int i = 0; i < population.size(); i += workerPull.getNumThread()) {

            for(int j = i; j < (i+workerPull.getNumThread()) && j < population.size(); j++)
            {
                //System.out.println("Worker "+(j-i)+" working in the individual " + j);
                IndividualGAL ind = population.get(j);
                BestPopulationConfigWorker w = (BestPopulationConfigWorker) workerPull.getWorker(j-i);
                w.setIndividual((PedestrianIndividual) ind);
                w.activated();
            }

            while(!workerPull.isAllWorkedFinished())
            {
                // System.out.println("Waiting Workers...");
            }

            for(int j = i; j < (i+workerPull.getNumThread()) && j < population.size(); j++)
            {

                BestPopulationConfigWorker w = (BestPopulationConfigWorker) workerPull.getWorker(j-i);
                float fitness = w.getFitness();
                //System.out.println("Individual "+j+" have fitness " + fitness + " calculated by worker " + (j-i) );
                IndividualGAL ind = population.get(j);
                ind.setFitness(fitness);
            }
        }
    }

    @Override
    public PopulationGAL createPopulation(int populationSize, boolean initialized) {
        PopulationPedestrianConfig population = new PopulationPedestrianConfig(_world,_pedestrianPathSize,_numIndividualsPerInd,populationSize,_mutationRate,initialized);
        return population;
    }

    @Override
    protected void mutate(IndividualGAL ind, float numGenMutations) {
        PedestrianIndividual indLocal = (PedestrianIndividual) ind;

        for(int i = 0; i < indLocal.getChromosomeSize(0); i++)
        {
            float probToModifyThisGen = Dice.GetRange(0f, 1f);
            if(probToModifyThisGen < numGenMutations)
            {
                indLocal.mutate(i);
            }
        }
    }

    @Override
    public IndividualGAL[] createIndividualArray(int numElements) {
        PedestrianIndividual[] arr = new PedestrianIndividual[numElements];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = new PedestrianIndividual(_numIndividualsPerInd,_pedestrianPathSize,_mutationRate,_world,false);
        }
        return arr;
    }


}