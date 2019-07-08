package com.massisframework.massis3.examples.genetic.SelectionCommands;

import com.massisframework.massis3.examples.genetic.Individual;
import com.massisframework.massis3.examples.genetic.MultiGenotypeIndividual;
import com.massisframework.massis3.examples.genetic.MultiGenotypePopulation;
import com.massisframework.massis3.examples.genetic.Population;

public class RouletteSelectionMulti extends AbstractSelectionMulti {


    float[] _relativeitness;

    public RouletteSelectionMulti() {
        super();
    }

    protected void configure(MultiGenotypePopulation population) {
        if (_relativeitness == null)
            _relativeitness = new float[population.size()];
    }

    @Override
    public MultiGenotypePopulation getSelection(MultiGenotypePopulation population, int elitism) {

        //_relativeitness = new float[population.size()];
        if (_relativeitness == null)
            _relativeitness = new float[population.size()];

        for (int i = 0; i < population.size(); ++i) {
            MultiGenotypeIndividual individual = population.getIndividual(i);
            float relativeFitness = individual.getFitness() / population.getFitness();
            if (i > 0)
                _relativeitness[i] = _relativeitness[i - 1] + relativeFitness;
            else
                _relativeitness[i] = relativeFitness;
        }


        return generate(population, elitism);
    }

    public MultiGenotypeIndividual findIndividual(float rand, MultiGenotypePopulation population) {
        MultiGenotypeIndividual indiviual = null;
        for (int i = 0; indiviual == null && i < _relativeitness.length; ++i) {
            if (rand <= _relativeitness[i]) {
                indiviual = population.getIndividual(i);
            }
        }

        return indiviual;
    }


    protected MultiGenotypePopulation generate(MultiGenotypePopulation population, int elitism) {
        /*for(int i = 0; i < A.numSubgenotypes(); ++i)
        {

        }*/
        MultiGenotypePopulation newPopulation = new MultiGenotypePopulation(population.size(), population.getChromosomeLenght(0));
        //List<Individual<T>> newPopulation = new ArrayList<>();
        for (int i = 0; i < elitism; ++i) {
            MultiGenotypeIndividual individual = population.getIndividual(i);
            MultiGenotypeIndividual newIndividdual = individual.clone();
            newPopulation.addIndividual(newIndividdual);
        }

        while (newPopulation.size() < population.size()) {
            float rand = (float) Math.random();
            MultiGenotypeIndividual individual = findIndividual(rand, population);
            MultiGenotypeIndividual newIndividual = individual.clone();
            newPopulation.addIndividual(newIndividual);

        }

        return newPopulation;
    }
}
