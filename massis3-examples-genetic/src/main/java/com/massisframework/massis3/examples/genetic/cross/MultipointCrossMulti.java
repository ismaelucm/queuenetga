package com.massisframework.massis3.examples.genetic.cross;

import com.massisframework.massis3.Dice;
import com.massisframework.massis3.examples.genetic.*;

public class MultipointCrossMulti extends AbstractCrossMulti {


    public MultipointCrossMulti() {

    }


    @Override
    public CrossPair<Gene> cross(MultiGenotypeIndividual A, MultiGenotypeIndividual B, IProblemMultyType problem) {

        CrossPairMulti childs = null;
        try {
            int[] parameterA = new int[A.numSubgenotypes()];
            int[] parameterB = new int[B.numSubgenotypes()];
            for (int i = 0; i < A.numSubgenotypes(); ++i) {
                parameterA[i] = A.size(i);
                parameterB[i] = B.size(i);
            }
            childs = new CrossPairMulti(new MultiGenotypeIndividual(parameterA), new MultiGenotypeIndividual(parameterB));
        } catch (Exception e) {

        }
        try {
            for (int i = 0; i < A.numSubgenotypes(); ++i) {
                int crossingPoint = Dice.GetRange(1, (A.size(i) / 2) - 1);
                int init = 0;

                int step = 0;
                while ((init + crossingPoint) < A.size(i)) {
                    crossover(step, init, init + crossingPoint, childs, A, B);
                    init += crossingPoint;
                    step++;
                }

                if (init < A.size(i))
                    crossover(step, init, A.size(i), childs, A, B);

            }
        } catch (Exception e2) {

        }
        //TODO Return child.
        return null;
    }

    protected void crossover(int step, int min, int max, CrossPairMulti childs, MultiGenotypeIndividual A, MultiGenotypeIndividual B) {
        try {
            for (int index = 0; index < A.numSubgenotypes(); ++index) {
                for (int i = min; i < max; ++i) {
                    if (step % 2 == 0) {
                        childs.getChildA().addGen(index, A.getSubGenotype(index).get(i));
                        childs.getChildB().addGen(index, B.getSubGenotype(index).get(i));
                    } else {
                        childs.getChildA().addGen(index, B.getSubGenotype(index).get(i));
                        childs.getChildB().addGen(index, A.getSubGenotype(index).get(i));
                    }
                }
            }
        } catch (Exception e2) {

        }
    }
}
