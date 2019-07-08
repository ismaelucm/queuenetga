package com.massisframework.massis3.examples.genetic.cross;

import com.massisframework.massis3.examples.genetic.Gene;
import com.massisframework.massis3.examples.genetic.Individual;

public class CrossPair<T extends Gene> {
    private Individual<T> childA;
    private Individual<T> childB;

    public CrossPair(Individual<T> childA, Individual<T> childB) {
        this.childA = childA;
        this.childB = childB;
    }


    public Individual<T> getChildA() {
        return childA;
    }

    public void setChildA(Individual<T> childA) {
        this.childA = childA;
    }

    public Individual<T> getChildB() {
        return childB;
    }

    public void setChildB(Individual<T> childB) {
        this.childB = childB;
    }
}