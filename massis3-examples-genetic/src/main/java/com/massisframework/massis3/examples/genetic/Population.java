package com.massisframework.massis3.examples.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Population<T extends Gene> {
    private List<Individual<T>> _individuals;
    private float _fitness;
    private int _size;
    private int _chromosomeLenght;

    public Population(int size, int chromosomeLenght) {
        _individuals = new ArrayList<>();
        _size = size;
        _chromosomeLenght = chromosomeLenght;
    }

    public int getChromosomeLenght() {
        return _chromosomeLenght;
    }

    public void initPopulation(IProblem<T> problem) {
        for (int i = 0; i < _size; ++i) {
            Individual<T> inidividual = new Individual<T>(_chromosomeLenght);
            inidividual.randomInit(problem);
            addIndividual(inidividual);
        }
    }

    public void addIndividual(Individual<T> individual) {
        _individuals.add(individual);
    }

    public List<Individual<T>> getIndividuals() {
        return _individuals;
    }

    public Individual<T> getIndividual(int i) {
        return _individuals.get(i);
    }

    public void setIndividual(int i, Individual ind) {
        _individuals.set(i, ind);
    }

    public void setFitness(float f) {
        _fitness = f;
    }


    public void sort() {
        //(String a, String b) ->
        Sort(_individuals);
    }

    public static <T extends Gene> void Sort(List<Individual<T>> _individuals) {
        Collections.sort(_individuals, (Individual<T> a, Individual<T> b) -> {
            float value = a.getFitness() - b.getFitness();
            if (value == 0)
                return 0;
            else if (value < 0)
                return 1;
            else
                return -1;
        });

    }

    public int size() {
        return _individuals.size();
    }


    public float getFitness() {
        return _fitness;
    }
}
