package com.massisframework.massis3.examples.genetic.cross;

import com.massisframework.massis3.examples.genetic.*;

public abstract class AbstractCrossMulti {

    public enum AbstractCrossMethods {MULTIPOINT_CROSS, SIMPLE_CROSS, UNIFORM_CROSS}

    public static AbstractCrossMulti getCrossMethod(AbstractCross.AbstractCrossMethods id) {
        switch (id) {
            case MULTIPOINT_CROSS:
                return new MultipointCrossMulti();
            case SIMPLE_CROSS:
                return new SimpleCrossMulti();
            case UNIFORM_CROSS:
                return new UniformCrossMulti();
        }
        return null;
    }

    public abstract CrossPair<Gene> cross(MultiGenotypeIndividual A, MultiGenotypeIndividual B, IProblemMultyType problem);
}
