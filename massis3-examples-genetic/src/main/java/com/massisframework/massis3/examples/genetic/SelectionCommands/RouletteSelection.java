package com.massisframework.massis3.examples.genetic.SelectionCommands;

import com.massisframework.massis3.examples.genetic.Gene;
import com.massisframework.massis3.examples.genetic.Individual;
import com.massisframework.massis3.examples.genetic.Population;


import java.util.ArrayList;
import java.util.List;

public class RouletteSelection<T extends Gene> extends AbstractSelection<T> {


    float[] _relativeitness;

    public RouletteSelection() {
        super();
    }

    protected void configure(Population population) {
        if (_relativeitness == null)
            _relativeitness = new float[population.size()];
    }

    @Override
    public Population<T> getSelection(Population<T> population, int elitism) {

        //_relativeitness = new float[population.size()];
        if (_relativeitness == null)
            _relativeitness = new float[population.size()];

        for (int i = 0; i < population.size(); ++i) {
            Individual individual = population.getIndividual(i);
            float relativeFitness = individual.getFitness() / population.getFitness();
            if (i > 0)
                _relativeitness[i] = _relativeitness[i - 1] + relativeFitness;
            else
                _relativeitness[i] = relativeFitness;
        }


        return generate(population, elitism);
    }

    public Individual findIndividual(float rand, Population population) {
        Individual indiviual = null;
        for (int i = 0; indiviual == null && i < _relativeitness.length; ++i) {
            if (rand <= _relativeitness[i]) {
                indiviual = population.getIndividual(i);
            }
        }

        return indiviual;
    }


    protected Population<T> generate(Population population, int elitism) {
        Population<T> newPopulation = new Population<>(population.size(), population.getChromosomeLenght());
        //List<Individual<T>> newPopulation = new ArrayList<>();
        for (int i = 0; i < elitism; ++i) {
            Individual<T> individual = population.getIndividual(i);
            Individual<T> newIndividdual = individual.clone();
            newPopulation.addIndividual(newIndividdual);
        }

        while (newPopulation.size() < population.size()) {
            float rand = (float) Math.random();
            Individual individual = findIndividual(rand, population);
            Individual newIndividual = individual.clone();
            newPopulation.addIndividual(newIndividual);

        }

        return newPopulation;
    }
}
