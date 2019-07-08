package com.massisframework.massis3.examples.simulator.Genetic.Problems;

import com.massisframework.massis3.Dice;
import com.massisframework.massis3.Pair;
import com.massisframework.massis3.examples.Threads.BestQueueSimConfigWorker;
import com.massisframework.massis3.examples.Threads.BestQueueSimConfigWorkerPull;
import com.massisframework.massis3.examples.genetic_v2.*;
import com.massisframework.massis3.examples.simulator.Components.HelpperClasses.SimulationResult;
import com.massisframework.massis3.examples.simulator.Core.Simulation;
import com.massisframework.massis3.examples.simulator.Core.Simulator;
import com.massisframework.massis3.examples.simulator.Genetic.Calculators.CalcFitnessBestQueueSim;
import com.massisframework.massis3.examples.simulator.Genetic.Calculators.Calculators;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.IndividualQueueSimConfig;
import com.massisframework.massis3.examples.simulator.Genetic.Populations.PopulationQueueSimConfig;
import com.massisframework.massis3.examples.simulator.World.World;

import java.util.List;

public class FindBestQueueSimConfig extends GeneticALProblem {

    private int _capacitySize;
    private int _timeSize;
    private int _transitiontimeSize;

    //private Simulation _simulation;
    //private String[] _orderNames;
    //private SimulationResult _resultToCompareFitness;
    //private List<Pair<String, String>> _transitionOrder;

    public FindBestQueueSimConfig(World world, CrossMethod cross, SelectionMethod select,
                                  List<Calculators> calcs) {
        super(cross, select);
        int numNodes = world.getAllNodes().size();
        int numSimulationVerticles = world.getAllTansitionOnlyOneDirection().size();
        _capacitySize = numNodes;
        _timeSize = numSimulationVerticles;
        _transitiontimeSize = numSimulationVerticles;
        //_simulation = simulation;
        //_orderNames = orderNames;
        //_transitionOrder = transitionOrder;
        //_resultToCompareFitness = resultTocompareFitness;
        //addNewCalculator(new CalcFitnessBestQueueSim(simulation,orderNames,transitionOrder,resultTocompareFitness));
        for(int i = 0; i < calcs.size(); ++i)
        {
            addNewCalculator(calcs.get(i));
        }
    }

    public FindBestQueueSimConfig(CrossMethod cross, SelectionMethod select,
                                  BestQueueSimConfigWorkerPull pull)
    {
        super(cross, select, pull);
        _capacitySize = pull.getNumNodes();
        _timeSize = pull.getNumTransitions();
        _transitiontimeSize = pull.getNumTransitions();
    }

    @Override
    public IndividualGAL[] createIndividualArray(int numElements) {
        IndividualQueueSimConfig[] arr = new IndividualQueueSimConfig[numElements];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = new IndividualQueueSimConfig(_capacitySize, _timeSize, _transitiontimeSize,false);
        }
        return arr;
    }



    @Override
    public void pararellEvaluate(PopulationGAL population) {
        BestQueueSimConfigWorkerPull workerPull = this.<BestQueueSimConfigWorkerPull>getWorkerPull();
        for (int i = 0; i < population.size(); i += workerPull.getNumThread()) {

            for(int j = i; j < (i+workerPull.getNumThread()) && j < population.size(); j++)
            {
                //System.out.println("Worker "+(j-i)+" working in the individual " + j);
                IndividualGAL ind = population.get(j);
                BestQueueSimConfigWorker w = (BestQueueSimConfigWorker) workerPull.getWorker(j-i);
                w.setIndividual((IndividualQueueSimConfig) ind);
                w.activated();
            }

            while(!workerPull.isAllWorkedFinished())
            {
               // System.out.println("Waiting Workers...");
            }

            for(int j = i; j < (i+workerPull.getNumThread()) && j < population.size(); j++)
            {

                BestQueueSimConfigWorker w = (BestQueueSimConfigWorker) workerPull.getWorker(j-i);
                float fitness = w.getFitness();
                //System.out.println("Individual "+j+" have fitness " + fitness + " calculated by worker " + (j-i) );
                IndividualGAL ind = population.get(j);
                ind.setFitness(fitness);
            }
        }
        //System.out.println("Evaluated finished...");
    }


    @Override
    protected void mutate(IndividualGAL ind, float numGenMutations) {
        IndividualQueueSimConfig indLocal = (IndividualQueueSimConfig) ind;
        for (int i = 0; i < indLocal.getNumLayers(); ++i) {
            int size = indLocal.getChromosomeSize(i);

            for(int j = 0; j < size; j++)
            {
                float probToModifyThisGen = Dice.GetRange(0f, 1f);
                if(probToModifyThisGen < numGenMutations)
                {
                    indLocal.mutate(i, j);
                }

            }
        }
    }

    @Override
    public PopulationGAL createPopulation(int populationSize, boolean initialized) {
        PopulationQueueSimConfig population = new PopulationQueueSimConfig(populationSize, _capacitySize, _timeSize, _transitiontimeSize,initialized);
        return population;
    }

    public PopulationGAL createPopulation(int populationSize, boolean initialized,Simulation simulation) {
        PopulationQueueSimConfig population = new PopulationQueueSimConfig(populationSize, _capacitySize, _timeSize, _transitiontimeSize,initialized,simulation);
        return population;
    }

    @Override
    public void finished()
    {
        BestQueueSimConfigWorkerPull workerPull = this.<BestQueueSimConfigWorkerPull>getWorkerPull();
        if(workerPull != null) {
            for (int i = 0; i < workerPull.getNumThread(); i++) {
                BestQueueSimConfigWorker w = (BestQueueSimConfigWorker) workerPull.getWorker(i);
                w.stop();
            }
        }
    }

}
