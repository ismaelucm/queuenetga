package com.massisframework.massis3.examples.simulator.Genetic.Populations;

import com.massisframework.massis3.examples.genetic_v2.IndividualGAL;
import com.massisframework.massis3.examples.genetic_v2.PopulationGAL;
import com.massisframework.massis3.examples.simulator.Components.HelpperClasses.GeneticMaths;
import com.massisframework.massis3.examples.simulator.Core.Simulation;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.IndividualQueueSimConfig;
import com.massisframework.massis3.examples.simulator.World.SimulationNode;
import com.massisframework.massis3.examples.simulator.World.SimulationVertice;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PopulationQueueSimConfig extends PopulationGAL {
    private int _capacitySize;
    private int _timeSize;
    private int _transitiontimeSize;

    public PopulationQueueSimConfig(int size, int capacitySize, int timeSize, int transitiontimeSize, boolean initialized) {
        super(size);
        baseInit(capacitySize,timeSize,transitiontimeSize);
        if(initialized)
                createIndividuals();
    }

    public PopulationQueueSimConfig(int size, int capacitySize, int timeSize, int transitiontimeSize, boolean initialized, Simulation simulation)
    {
        super(size);
        baseInit(capacitySize,timeSize,transitiontimeSize);
        if(initialized)
            createIndividualUsingManualConfig(simulation);
    }

    protected void baseInit(int capacitySize, int timeSize, int transitiontimeSize)
    {
        _capacitySize = capacitySize;
        _timeSize = timeSize;
        _transitiontimeSize = transitiontimeSize;
    }

    public PopulationQueueSimConfig(PopulationQueueSimConfig toCopy) {
        super();
        this.setPopulationArr(createNewArray(toCopy.size()));
        for (int i = 0; i < size(); ++i) {
            this.set(i, toCopy.get(i).clone());
        }
    }

    public PopulationGAL clone() {
        PopulationQueueSimConfig p = new PopulationQueueSimConfig(this);
        return p;
    }

    public IndividualGAL[] createNewArray(int size) {
        return new IndividualQueueSimConfig[size];
    }

    public IndividualGAL createNewRandomIndividual() {
        return new IndividualQueueSimConfig(_capacitySize, _timeSize, _transitiontimeSize,true);
    }

    public IndividualGAL createNewRandomIndividualWithManualSetup(IndividualQueueSimConfig ind) {

        return new IndividualQueueSimConfig(ind,0.5f,0.5f,0.5f,
                10,20,1,2,1,2);
    }

    protected void createIndividualUsingManualConfig(Simulation simulation)
    {
        IndividualQueueSimConfig baseIndividual = new IndividualQueueSimConfig(_capacitySize,_timeSize,_transitiontimeSize, false);
        List<SimulationNode> allSimNodes =  simulation.getSimulator().getWorld().getAllSimulationNode();
        List<SimulationVertice> allSimVerticles =  simulation.getSimulator().getWorld().getAllTansitionOnlyOneDirection();

        int[] simSize = new int[_capacitySize];
        int[] timeSize = new int[_timeSize];
        int[] transitionSize = new int[_transitiontimeSize];

        for(int i = 0; i < _capacitySize; ++i)
        {
            simSize[i] = allSimNodes.get(i).getCapacity();

        }

        for(int i = 0; i < _transitiontimeSize; ++i)
        {
            transitionSize[i] = allSimVerticles.get(i).get_transportRate();
            timeSize[i] =allSimVerticles.get(i).get_timeToCrossEdge();
        }

        baseIndividual.setCapacityArray(simSize);
        baseIndividual.setTimeArray(timeSize);
        baseIndividual.setTransitionSize(transitionSize);
        for (int i = 0; i < _populationArr.length; ++i) {
            _populationArr[i] = createNewRandomIndividualWithManualSetup(baseIndividual);
        }
    }

    public float getPopulationDiversity()
    {
        IndividualQueueSimConfig firstInd = (IndividualQueueSimConfig) _populationArr[0];
        List<List<Map<Integer, Integer>>> layerList = new ArrayList<>();
        int numGenes = 0;
        for(int layer = 0; layer < firstInd.getNumLayers(); layer++)
        {
            numGenes+=firstInd.getCountLayer(layer);
        }

        for(int layer = 0; layer < firstInd.getNumLayers(); layer++) {
            List<Map<Integer, Integer>> frequencyLayer = new ArrayList<>();//lista de cada gene
            for (int i = 0; i < _populationArr.length; ++i) {
                IndividualQueueSimConfig individual = (IndividualQueueSimConfig) _populationArr[i];
                int[] genotypeArray = individual.getLayer(layer);

                for (int j = 0; j < genotypeArray.length; j++) {
                    int gen = genotypeArray[j];
                    if (i == 0) {
                        Map<Integer, Integer> genMapper = new LinkedHashMap<>(); //mapa que guarda para cada valor, el número de apariciones
                        frequencyLayer.add(genMapper);
                    }

                    Map<Integer, Integer> genMap = frequencyLayer.get(j);
                    if (!genMap.containsKey(gen))
                        genMap.put(gen, 1);
                    else {
                        int count = genMap.get(gen);
                        count++;
                        genMap.put(gen, count);
                    }
                }
            }
            layerList.add(frequencyLayer);
        }

        double entropy = 0f;
        for(int layer = 0; layer < layerList.size(); ++layer)
        {
            List<Map<Integer, Integer>> frequencyLayer = layerList.get(layer);
            for(int gen = 0; gen < frequencyLayer.size(); ++gen)
            {
                Map<Integer, Integer> genMap = frequencyLayer.get(gen);
                for(Integer count:genMap.values())
                {
                    double prob = ((double)count)/((double)_populationArr.length);
                    double entTemp = prob* GeneticMaths.log(firstInd.getDomineLayer(layer),prob);
                    entropy += entTemp;
                }
            }
        }


        double finalEntropy =  -1d*entropy/numGenes;
        return (float)finalEntropy;
    }


}
