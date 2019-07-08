package com.massisframework.massis3.examples.simulator.Genetic.Populations;

import com.massisframework.massis3.examples.genetic_v2.IndividualGAL;
import com.massisframework.massis3.examples.genetic_v2.PopulationGAL;
import com.massisframework.massis3.examples.simulator.Components.HelpperClasses.GeneticMaths;
import com.massisframework.massis3.examples.simulator.Components.Pedestrian;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.IndividualQueueSimConfig;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.PedestrianGA;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.PedestrianIndividual;
import com.massisframework.massis3.examples.simulator.Genetic.PedestrianGene;
import com.massisframework.massis3.examples.simulator.World.World;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PopulationPedestrianConfig extends PopulationGAL {

    private int _pathSize;
    private int _populationSize;
    private int _numIndividuals;
    private float _mutationRate;
    private World _world;


    public PopulationPedestrianConfig(PopulationPedestrianConfig toCopy) {
        super();
        this.setPopulationArr(createNewArray(toCopy.size()));
        for (int i = 0; i < size(); ++i) {
            this.set(i, toCopy.get(i).clone());
        }
        baseInit(toCopy._world,toCopy._pathSize,toCopy._numIndividuals,toCopy._populationSize,toCopy._mutationRate);
    }

    public PopulationPedestrianConfig(World world, int pathSize, int numIndividuals, int populationSize, float mutationRate, boolean initialized) {
        super(populationSize);
        baseInit(world,pathSize,numIndividuals, populationSize,mutationRate);
        if(initialized)
            createIndividuals();
    }

    @Override
    public PopulationGAL clone() {
        PopulationPedestrianConfig p = new PopulationPedestrianConfig(this);
        return p;
    }

    @Override
    public IndividualGAL[] createNewArray(int size) {
        return new PedestrianIndividual[size];
    }


    @Override
    public IndividualGAL createNewRandomIndividual()
    {
        return new PedestrianIndividual(_numIndividuals,_pathSize,_mutationRate, _world,true);
    }

    @Override
    public float getPopulationDiversity() {

        List<Map<Integer, Integer>> genList = new ArrayList<>();

        for(int i = 0; i < _populationArr.length; ++i)
        {
            PedestrianIndividual pedestrianInd = (PedestrianIndividual) _populationArr[i];
            List<PedestrianGA> pedestrianList = pedestrianInd.getPedestrianGAList();
            for(int j = 0; j < pedestrianList.size(); ++j)
            {
                PedestrianGA pedestrian = pedestrianList.get(j);
                int[] path = pedestrian.getPathEncode();
                for(int k = 0; k < path.length; k++)
                {
                    int val = path[k];
                    int position = k+j*path.length;
                    if(genList.size() < (position+1))
                    {
                        Map<Integer,Integer> map = new LinkedHashMap<>();
                        genList.add(map);
                    }
                    Map<Integer,Integer> map = genList.get(position);
                    if(map.containsKey(val))
                    {
                        int num = map.get(val);
                        map.put(val,num+1);
                    }
                    else
                    {
                        map.put(val,1);
                    }
                }
            }
        }


        double entropy = 0f;
        for(int gen = 0; gen < genList.size(); ++gen)
        {
            Map<Integer, Integer> genMap = genList.get(gen);
            for(Integer count:genMap.values())
            {
                double prob = ((double)count)/((double)_populationArr.length);
                double entTemp = prob* GeneticMaths.log(PedestrianGA.numberOfDomineElements(),prob);
                entropy += entTemp;
            }
        }
        double finalEntropy =  -1d*entropy/genList.size();
        return (float) finalEntropy;
    }

    protected void baseInit(World world,int pathSize, int numIndividuals, int populationSize, float mutationRate) {
        _pathSize = pathSize;
        _populationSize = populationSize;
        _numIndividuals = numIndividuals;
        _world = world;
        _mutationRate = mutationRate;
    }
}
