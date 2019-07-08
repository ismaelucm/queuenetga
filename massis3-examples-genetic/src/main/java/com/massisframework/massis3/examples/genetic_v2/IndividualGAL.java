package com.massisframework.massis3.examples.genetic_v2;

public abstract class IndividualGAL {
    private float _fitness;

    public float getFitness() {
        return _fitness;
    }

    public synchronized void setFitness(float f) {
        _fitness = f;
    }

    public int getNumLayers() {
        return 1;
    }

    public abstract IndividualGAL clone();

    public abstract void copyCross(int layer, int a, int b, IndividualGAL parent2, IndividualGAL child1, IndividualGAL child2, boolean cross);

    public abstract int getChromosomeSize(int layer);
}
