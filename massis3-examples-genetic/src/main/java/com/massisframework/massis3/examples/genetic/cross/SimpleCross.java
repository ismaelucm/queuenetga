package com.massisframework.massis3.examples.genetic.cross;

import com.massisframework.massis3.Pair;
import com.massisframework.massis3.examples.genetic.Gene;
import com.massisframework.massis3.examples.genetic.IProblem;
import com.massisframework.massis3.examples.genetic.Individual;


public class SimpleCross<T extends Gene> extends AbstractCross<T> {
    @Override
    public CrossPair<T> cross(Individual<T> A, Individual<T> B, IProblem<T> problem) {

        CrossPair<T> childs = new CrossPair(new Individual(A.size()), new Individual(B.size()));


        int crossingPoint = A.size() / 2;

        for (int i = 0; i < crossingPoint; ++i) {
            childs.getChildA().add(A.getGen(i));
            childs.getChildB().add(B.getGen(i));
        }

        for (int i = crossingPoint; i < A.size(); ++i) {
            childs.getChildA().add(B.getGen(i));
            childs.getChildB().add(A.getGen(i));
        }

        return childs;
    }
}
