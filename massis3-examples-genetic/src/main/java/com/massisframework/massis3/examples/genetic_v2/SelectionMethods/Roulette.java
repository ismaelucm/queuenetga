package com.massisframework.massis3.examples.genetic_v2.SelectionMethods;

import com.massisframework.massis3.examples.genetic_v2.*;


public class Roulette implements SelectionMethod {

    float[] _relativeitness;

    @Override
    public PopulationGAL select(PopulationGAL populationGal, GeneticALProblem problem, int elitism, float diversity) {

        calcRelative(populationGal);
        return selectRoulette(populationGal, problem, elitism);
    }

    protected PopulationGAL selectRoulette(PopulationGAL populationGal, GeneticALProblem problem, int elitism) {
        PopulationGAL newPopilation = problem.createPopulation(populationGal.size(),false);


        for (int i = 0; i < elitism; ++i) {
            IndividualGAL individualGAL = populationGal.get(i);
            IndividualGAL newIndividdual = individualGAL.clone();
            newPopilation.set(i, newIndividdual);
        }

        for (int i = elitism; i < populationGal.size(); ++i) {
            IndividualGAL individualGAL = populationGal.get(i);
            float rand = (float) Math.random();
            IndividualGAL individual = findIndividual(rand, populationGal);
            IndividualGAL newInd = individual.clone();
            newPopilation.set(i, newInd);
        }

        return newPopilation;
    }

    public IndividualGAL findIndividual(float rand, PopulationGAL population) {
        IndividualGAL indiviual = null;
        for (int i = 0; indiviual == null && i < _relativeitness.length; ++i) {
            if (rand <= _relativeitness[i]) {
                indiviual = population.get(i);
            }
        }

        return indiviual;
    }

    protected void calcRelative(PopulationGAL populationGal) {
        if (_relativeitness == null)
            _relativeitness = new float[populationGal.size()];

        for (int i = 0; i < populationGal.size(); ++i) {
            IndividualGAL individual = populationGal.get(i);
            float relativeFitness = individual.getFitness() / populationGal.getFitness();
            if (i > 0)
                _relativeitness[i] = _relativeitness[i - 1] + relativeFitness;
            else
                _relativeitness[i] = relativeFitness;
        }
    }
}
