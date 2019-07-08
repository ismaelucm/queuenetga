package com.massisframework.massis3.examples.genetic.SelectionCommands;

import com.massisframework.massis3.examples.genetic.MultiGenotypePopulation;
import com.massisframework.massis3.examples.genetic.Population;

public class HierarchicalMulti extends RouletteSelectionMulti {

    float[] _prob;
    float _A;
    float _B;

    protected void configure(MultiGenotypePopulation population) {
        super.configure(population);
        if (_prob == null)
            _prob = new float[population.size()];
    }

    public void setAdicionalConfiguration(float a, float b) {
        _A = a;
        _B = b;
    }


    public HierarchicalMulti() {
        super();
    }


    @Override
    public MultiGenotypePopulation getSelection(MultiGenotypePopulation population, int elitims) {

        configure(population);

        for (int i = 0; i < population.size(); ++i) {
            _prob[i] = probFuntion((population.size() - i));
        }

        for (int i = 0; i < _prob.length; ++i) {
            if (i > 0)
                _relativeitness[i] = _relativeitness[i - 1] + _prob[i];
            else
                _relativeitness[i] = _prob[i];
        }
        return generate(population, elitims);
    }

    protected float probFuntion(int rankValue) {
        return _A * rankValue + _B;
    }

    public void setAValue(float a) {
        _A = a;
    }

    public void setBValue(float b) {
        _B = b;
    }

    public float getAValue() {
        return _A;
    }

    public float getBValue() {
        return _B;
    }
}
