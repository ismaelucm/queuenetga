package com.massisframework.massis3.examples.genetic_v2;

public class GeneticAlgorithmSetup {

    private int elitism;
    private float minMutationRate;
    private float maxMutationRate;
    private float minNumGenesMutatedPerMutation;
    private float maxNumGenesMutatedPerMutation;
    private float crossoverRate;
    private int populationSize;
    private int maxGenerations;


    public GeneticAlgorithmSetup(int elitism, float minMutationRate, float maxMutationRate, float minNumGenesMutatedPerMutation, float maxNumGenesMutatedPerMutation, float crossoverRate, int populationSize, int maxGenerations) {
        this.elitism = elitism;
        this.minMutationRate = minMutationRate;
        this.maxMutationRate = maxMutationRate;
        this.minNumGenesMutatedPerMutation = minNumGenesMutatedPerMutation;
        this.maxNumGenesMutatedPerMutation = maxNumGenesMutatedPerMutation;
        this.crossoverRate = crossoverRate;
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
    }

    public int getElitism() {
        return elitism;
    }

    public void setElitism(int elitism) {
        this.elitism = elitism;
    }

    public float getMinMutationRate() {
        return minMutationRate;
    }

    public float getMaxMutationRate() {
        return maxMutationRate;
    }

    public void setMinMutationRate(float mutationRate) {
        this.minMutationRate = mutationRate;
    }

    public void setMaxMutationRate(float mutationRate) {
        this.maxMutationRate = mutationRate;
    }

    public float getCrossoverRate() {
        return crossoverRate;
    }


    public float getMinNumGenesMutatedPerMutation() {
        return minNumGenesMutatedPerMutation;
    }

    public float getMaxNumGenesMutatedPerMutation() {
        return maxNumGenesMutatedPerMutation;
    }

    public void setMinNumGenesMutatedPerMutation(float numGenesMutatedPerMutation) {
        this.minNumGenesMutatedPerMutation = numGenesMutatedPerMutation;
    }

    public void setMaxNumGenesMutatedPerMutation(float numGenesMutatedPerMutation) {
        this.maxNumGenesMutatedPerMutation = numGenesMutatedPerMutation;
    }

    public void setCrossoverRate(float crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getMaxGenerations() {
        return maxGenerations;
    }

    public void setMaxGenerations(int maxGenerations) {
        this.maxGenerations = maxGenerations;
    }
}
