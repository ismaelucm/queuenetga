package com.massisframework.massis3.examples.genetic_v2.SelectionMethods;

import com.massisframework.massis3.examples.genetic_v2.GeneticALProblem;
import com.massisframework.massis3.examples.genetic_v2.PopulationGAL;

public class Hierarchical extends Roulette {


    float[] _prob;
    float _A;
    float _B;

    public Hierarchical(float a, float b) {
        _A = a;
        _B = b;
    }

    @Override
    public PopulationGAL select(PopulationGAL populationGal, GeneticALProblem problem, int elitism,float diversity) {

        configureProb(populationGal);
        return selectRoulette(populationGal, problem, elitism);
    }

    protected void configureProb(PopulationGAL population) {
        if (_prob == null)
            _prob = new float[population.size()];

        for (int i = 0; i < population.size(); ++i) {
            _prob[i] = probFuntion((population.size() - i));
        }

        if (_relativeitness == null)
            _relativeitness = new float[population.size()];

        for (int i = 0; i < _prob.length; ++i) {
            if (i > 0)
                _relativeitness[i] = _relativeitness[i - 1] + _prob[i];
            else
                _relativeitness[i] = _prob[i];
        }
    }

    protected float probFuntion(int rankValue) {
        return _A * rankValue + _B;
    }
}
