package com.massisframework.massis3.examples.genetic_v2.CrossMethods;

import com.massisframework.massis3.Dice;
import com.massisframework.massis3.examples.genetic_v2.CrossMethod;
import com.massisframework.massis3.examples.genetic_v2.IndividualGAL;

public class UniformCross implements CrossMethod {
    @Override
    public void cross(IndividualGAL parent1, IndividualGAL parent2, IndividualGAL child1, IndividualGAL child2) {

        for (int i = 0; i < parent1.getNumLayers(); ++i) {
            //si es par cruzo si es impar no cruzo
            int size = parent1.getChromosomeSize(i);
            for (int j = 0; j < size; ++j) {
                int rand = Dice.GetRange(0, 9); // tiro un dado de 0-10
                parent1.copyCross(i, j, j + 1, parent2, child1, child2, rand % 2 == 0);
            }
        }
    }
}
