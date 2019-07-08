package com.massisframework.massis3.examples.genetic;

import java.util.Collections;
import java.util.List;

public class MultiGenotypePopulation {

    private int _populationSize;
    private List<MultiGenotypeIndividual> _individuals;
    private float _fitness;

    public MultiGenotypePopulation(int populationSize, int... chromosomeLenghts) {
        _populationSize = populationSize;
    }

    public void initPopulation(IProblemMultyType problem, int... chromosomeLenght) {
        for (int i = 0; i < _populationSize; ++i) {
            MultiGenotypeIndividual inidividual = new MultiGenotypeIndividual(chromosomeLenght);
            inidividual.randomInit(problem);
            addIndividual(inidividual);
        }
    }

    public void addIndividual(MultiGenotypeIndividual individual) {
        _individuals.add(individual);
    }

    public MultiGenotypeIndividual getIndividual(int i) {
        return _individuals.get(i);
    }

    public void setFitness(float f) {
        _fitness = f;
    }

    public void sort() {
        //(String a, String b) ->
        Sort(_individuals);
    }

    public static <T extends Gene> void Sort(List<MultiGenotypeIndividual> _individuals) {
        Collections.sort(_individuals, (MultiGenotypeIndividual a, MultiGenotypeIndividual b) -> {
            float value = a.getFitness() - b.getFitness();
            if (value == 0)
                return 0;
            else if (value < 0)
                return 1;
            else
                return -1;
        });

    }

    public int getChromosomeLenght(int i) {
        try {
            return _individuals.get(0).size(i);
        } catch (Exception e) {
            return -1;
        }
    }

    public void setIndividual(int i, MultiGenotypeIndividual ind) {
        _individuals.set(i, ind);
    }

    public int size() {
        return _individuals.size();
    }

    public float getFitness() {
        return _fitness;
    }

}
