package com.massisframework.massis3.examples.genetic.cross;

import com.massisframework.massis3.Dice;
import com.massisframework.massis3.examples.genetic.Gene;
import com.massisframework.massis3.examples.genetic.IProblem;
import com.massisframework.massis3.examples.genetic.Individual;

import java.util.List;


public class UniformCross<T extends Gene> extends AbstractCross<T> {
    @Override
    public CrossPair<T> cross(Individual<T> A, Individual<T> B, IProblem<T> problem) {
        CrossPair<T> childs = new CrossPair(new Individual(A.size()), new Individual(B.size()));
        for (int i = 0; i < A.size(); ++i) {
            int rand = Dice.GetRange(0, 9);
            if (rand % 2 == 0) {

                if (problem.isCrossGenDefined()) {
                    List<T> genes = problem.crossGen(A.getGen(i), B.getGen(i));
                    childs.getChildA().add(genes.get(0));
                    childs.getChildB().add(genes.get(1));
                } else {
                    childs.getChildA().add(A.getGen(i));
                    childs.getChildB().add(B.getGen(i));
                }

            } else {

                if (problem.isCrossGenDefined()) {
                    List<T> genes = problem.crossGen(A.getGen(i), B.getGen(i));
                    childs.getChildA().add(genes.get(1));
                    childs.getChildB().add(genes.get(0));
                } else {
                    childs.getChildA().add(B.getGen(i));
                    childs.getChildB().add(A.getGen(i));
                }
            }
        }

        return childs;
    }
}
