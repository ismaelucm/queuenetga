package com.massisframework.massis3.examples.genetic.cross;

import com.massisframework.massis3.examples.genetic.Individual;
import com.massisframework.massis3.examples.genetic.MultiGenotypeIndividual;

public class CrossPairMulti {
    private MultiGenotypeIndividual childA;
    private MultiGenotypeIndividual childB;

    public CrossPairMulti(MultiGenotypeIndividual childA, MultiGenotypeIndividual childB) {
        this.childA = childA;
        this.childB = childB;
    }


    public MultiGenotypeIndividual getChildA() {
        return childA;
    }

    public void setChildA(MultiGenotypeIndividual childA) {
        this.childA = childA;
    }

    public MultiGenotypeIndividual getChildB() {
        return childB;
    }

    public void setChildB(MultiGenotypeIndividual childB) {
        this.childB = childB;
    }
}
