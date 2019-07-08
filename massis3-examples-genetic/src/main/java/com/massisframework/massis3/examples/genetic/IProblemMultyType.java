package com.massisframework.massis3.examples.genetic;

import com.massisframework.massis3.examples.genetic.SelectionCommands.AbstractSelectionMulti;
import com.massisframework.massis3.examples.genetic.cross.AbstractCrossMulti;

import java.util.List;

public interface IProblemMultyType {

    float calcFitness(MultiGenotypeIndividual individual);

    boolean isFinish(MultiGenotypePopulation p, int generations);

    void customOperator(MultiGenotypePopulation p, int elitism, float crossoverRate, float mutationRate);

    MultiGenotypeIndividual mutate(MultiGenotypeIndividual individual);

    AbstractCrossMulti getCrossMethod();

    AbstractSelectionMulti getAbstractSelectionMethod();

    Gene createRandomGene(int index);

    List<Gene> crossGen(Gene a, Gene b);

    boolean isCrossGenDefined();
}
