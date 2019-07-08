package com.massisframework.massis3.examples.genetic;

import com.massisframework.massis3.examples.genetic.SelectionCommands.AbstractSelection;
import com.massisframework.massis3.examples.genetic.cross.AbstractCross;

import java.util.List;

public interface IProblem<T extends Gene> {

    float calcFitness(Individual<T> individual);

    boolean isFinish(Population<T> p, int generations);

    void customOperator(Population<T> p, int elitism, float crossoverRate, float mutationRate);

    Individual<T> mutate(Individual<T> individual);

    AbstractCross<T> getCrossMethod();

    AbstractSelection<T> getAbstractSelectionMethod();

    T createRandomGene();

    List<T> crossGen(T a, T b);

    boolean isCrossGenDefined();
}
