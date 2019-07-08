package com.massisframework.massis3.examples.genetic.cross;

import com.massisframework.massis3.Dice;
import com.massisframework.massis3.examples.genetic.Gene;
import com.massisframework.massis3.examples.genetic.IProblem;
import com.massisframework.massis3.examples.genetic.Individual;


public class MultipointCross<T extends Gene> extends AbstractCross<T> {


    public MultipointCross() {

    }


    @Override
    public CrossPair<T> cross(Individual<T> A, Individual<T> B, IProblem<T> problem) {
        CrossPair<T> childs = new CrossPair(new Individual(A.size()), new Individual(B.size()));

        int crossingPoint = Dice.GetRange(1, (A.size() / 2) - 1);
        int init = 0;

        int step = 0;
        while ((init + crossingPoint) < A.size()) {
            crossover(step, init, init + crossingPoint, childs, A, B);
            init += crossingPoint;
            step++;
        }

        if (init < A.size())
            crossover(step, init, A.size(), childs, A, B);

        return childs;
    }

    protected void crossover(int step, int min, int max, CrossPair<T> childs, Individual<T> A, Individual<T> B) {
        for (int i = min; i < max; ++i) {
            if (step % 2 == 0) {
                childs.getChildA().add(A.getGen(i));
                childs.getChildB().add(B.getGen(i));
            } else {
                childs.getChildA().add(B.getGen(i));
                childs.getChildB().add(A.getGen(i));
            }
        }
    }
}
