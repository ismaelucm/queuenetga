package com.massisframework.massis3.examples.genetic_v2.CrossMethods;

import com.massisframework.massis3.Dice;
import com.massisframework.massis3.examples.genetic_v2.CrossMethod;
import com.massisframework.massis3.examples.genetic_v2.IndividualGAL;

public class MultipointCross implements CrossMethod {
    @Override
    public void cross(IndividualGAL parent1, IndividualGAL parent2, IndividualGAL child1, IndividualGAL child2) {
        for (int i = 0; i < parent1.getNumLayers(); ++i) {
            int size = parent1.getChromosomeSize(i);
            int crossingPoints = Dice.GetRange(1, (size / 2));
            int increment = size / crossingPoints;

            int init = 0;
            int step = 0;
            while ((init + increment) <= size) {

                if (size % 2 != 0) {
                    if ((init + increment) < (size - 1))
                        parent1.copyCross(i, init, init + increment, parent2, child1, child2, step % 2 == 0);
                    else
                        parent1.copyCross(i, init, size, parent2, child1, child2, step % 2 == 0);

                } else
                    parent1.copyCross(i, init, init + increment, parent2, child1, child2, step % 2 == 0);

                init += increment;
                step++;
            }

        }
    }
}
