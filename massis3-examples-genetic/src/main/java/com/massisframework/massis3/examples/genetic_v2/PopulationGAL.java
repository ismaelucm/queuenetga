package com.massisframework.massis3.examples.genetic_v2;

import java.util.Arrays;

public abstract class PopulationGAL {

    protected IndividualGAL[] _populationArr;
    protected float _totalFitness;

    public PopulationGAL() {
    }

    public PopulationGAL(int populationSize) {
        this();
        _populationArr = createNewArray(populationSize);
    }


    protected void createIndividuals()
    {
        for (int i = 0; i < _populationArr.length; ++i) {
            _populationArr[i] = createNewRandomIndividual();
        }
    }

    protected void setPopulationArr(IndividualGAL[] newP) {
        _populationArr = newP;
    }


    public IndividualGAL get(int i) {
        return _populationArr[i];
    }

    public <T extends IndividualGAL> T getCast(int i) {
        return (T) _populationArr[i];
    }

    public void set(int i, IndividualGAL ind) {
        _populationArr[i] = ind;
    }


    public int size() {
        return _populationArr.length;
    }

    public void sort() {
        sort(_populationArr);
    }

    public void sort(IndividualGAL[] populationArr) {
        Arrays.sort(populationArr, (IndividualGAL a, IndividualGAL b) -> {
            float value = a.getFitness() - b.getFitness();
            if (value == 0)
                return 0;
            else if (value < 0)
                return 1;
            else
                return -1;
        });
    }

    public void calcTotalFitness() {
        _totalFitness = 0;
        for (int i = 0; i < _populationArr.length; ++i) {
            _totalFitness += _populationArr[i].getFitness();
        }
    }

    public float getFitness() {
        return _totalFitness;
    }

    public IndividualGAL getFirst() {
        return _populationArr[0];
    }

    public abstract PopulationGAL clone();

    public abstract IndividualGAL[] createNewArray(int size);

    public abstract IndividualGAL createNewRandomIndividual();

    public abstract float getPopulationDiversity();


}
