package com.massisframework.massis3.examples.genetic.cross;


import com.massisframework.massis3.examples.genetic.Gene;
import com.massisframework.massis3.examples.genetic.IProblem;
import com.massisframework.massis3.examples.genetic.Individual;

public abstract class AbstractCross<T extends Gene> {

    public enum AbstractCrossMethods {MULTIPOINT_CROSS, SIMPLE_CROSS, UNIFORM_CROSS}

    public static <T extends Gene> AbstractCross<T> getCrossMethod(AbstractCrossMethods id) {
        switch (id) {
            case MULTIPOINT_CROSS:
                return new MultipointCross<T>();
            case SIMPLE_CROSS:
                return new SimpleCross<T>();
            case UNIFORM_CROSS:
                return new UniformCross<>();
        }
        return null;
    }

    public abstract CrossPair<T> cross(Individual<T> A, Individual<T> B, IProblem<T> problem);
}
