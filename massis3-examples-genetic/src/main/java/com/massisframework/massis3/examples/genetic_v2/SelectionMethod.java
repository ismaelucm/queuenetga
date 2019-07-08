package com.massisframework.massis3.examples.genetic_v2;

public interface SelectionMethod {
    PopulationGAL select(PopulationGAL populationGal, GeneticALProblem problem, int elitism, float diversity);
}
