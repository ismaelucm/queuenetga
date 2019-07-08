package com.massisframework.massis3.examples.simulator.Genetic.Individuals;

import com.massisframework.massis3.examples.genetic_v2.IndividualGAL;
import com.massisframework.massis3.examples.genetic_v2.PopulationGAL;
import com.massisframework.massis3.examples.simulator.World.World;

import java.util.ArrayList;
import java.util.List;

public class PedestrianIndividual extends IndividualGAL {

    protected PedestrianGA[] _individualArray;
    protected int _pedestrianPathSize;
    protected int _numIndividuals;
    protected World _world;
    protected float _genMutationRate;


    public PedestrianIndividual(int pedestrianPathSize, int numIndividuals, float mutationRate, World world)
    {
        super();
        _world = world;
        _numIndividuals = numIndividuals;
        _pedestrianPathSize = pedestrianPathSize;
        _genMutationRate = mutationRate;
    }

    public PedestrianIndividual(PedestrianIndividual ind)
    {
        super();
        _individualArray = new PedestrianGA[ind._numIndividuals];
        _pedestrianPathSize = ind._pedestrianPathSize;
        _world = ind._world;
        _genMutationRate = ind._genMutationRate;
        _numIndividuals = ind._numIndividuals;

        for (int i = 0; i < _numIndividuals; ++i) {
            _individualArray[i] = ind._individualArray[i].clone();
        }
    }

    public PedestrianIndividual(int numIndividuals,int pedestrianPathSize, float mutationRate, World world,boolean initialized) {
        this(pedestrianPathSize,numIndividuals,mutationRate,world);
        _individualArray = new PedestrianGA[numIndividuals];

        if(initialized)
            initialize();
    }

    @Override
    public IndividualGAL clone() {
        PedestrianIndividual pi = new  PedestrianIndividual(_pedestrianPathSize,_numIndividuals, _genMutationRate,_world);
        pi._individualArray = new PedestrianGA[_numIndividuals];
        for(int i = 0; i < this._individualArray.length; ++i)
        {
            pi._individualArray[i] = _individualArray[i].clone();
        }

        return pi;
    }

    @Override
    public void copyCross(int layer, int a, int b, IndividualGAL parent2, IndividualGAL child1, IndividualGAL child2, boolean cross) {
        PedestrianIndividual child2Local = (PedestrianIndividual) child2;
        PedestrianIndividual child1Local = (PedestrianIndividual) child1;
        PedestrianIndividual parent2Local = (PedestrianIndividual) parent2;
        for (int i = a; i < b; ++i) {
            if (cross) {
                PedestrianGA[] childs = _individualArray[i].cross(parent2Local._individualArray[i],_world);//p1 => c2
                child2Local._individualArray[i] = childs[1];
                child1Local._individualArray[i] = childs[0];
            } else {
                child2Local._individualArray[i] = parent2Local._individualArray[i];//p2 => c1
                child1Local._individualArray[i] = _individualArray[i];//p1 => c2
            }
        }
    }

    @Override
    public int getChromosomeSize(int layer) {
        return _individualArray.length;
    }

    protected void initialize()
    {
        for (int i = 0; i < _individualArray.length; ++i) {
            _individualArray[i] = new PedestrianGA(_genMutationRate,_pedestrianPathSize);//.createValidRandomPath(_pedestrianPathSize,_world);
            _individualArray[i].randomInit(_world);
        }


    }

    public void mutate(int chromosome) {
        _individualArray[chromosome].mutate(_world);
    }


    public List<PedestrianGA>  getPedestrianGAList()
    {
        List<PedestrianGA> pedestrianGAList = new ArrayList<>();


        for(int i =0 ; i < _individualArray.length; ++i)
        {
            pedestrianGAList.add(_individualArray[i]);
        }

        return pedestrianGAList;
    }
}
