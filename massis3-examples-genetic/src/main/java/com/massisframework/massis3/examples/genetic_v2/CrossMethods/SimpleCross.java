package com.massisframework.massis3.examples.genetic_v2.CrossMethods;

import com.massisframework.massis3.examples.genetic_v2.CrossMethod;
import com.massisframework.massis3.examples.genetic_v2.IndividualGAL;

public class SimpleCross implements CrossMethod {
    @Override
    public void cross(IndividualGAL parent1, IndividualGAL parent2, IndividualGAL child1, IndividualGAL child2) {
        for (int i = 0; i < parent1.getNumLayers(); ++i) {
            int size = parent1.getChromosomeSize(i);
            int crossingPoint = size / 2;
            parent1.copyCross(i, 0, crossingPoint, parent2, child1, child2, true);
            parent1.copyCross(i, crossingPoint, parent1.getChromosomeSize(i), parent2, child1, child2, true);
        }
    }
}
