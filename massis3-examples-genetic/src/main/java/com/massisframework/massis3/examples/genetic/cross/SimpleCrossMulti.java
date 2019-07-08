package com.massisframework.massis3.examples.genetic.cross;

import com.massisframework.massis3.examples.genetic.*;

public class SimpleCrossMulti extends AbstractCrossMulti {
    @Override
    public CrossPair<Gene> cross(MultiGenotypeIndividual A, MultiGenotypeIndividual B, IProblemMultyType problem) {

        /*CrossPair<Gene> childs = new CrossPair(new Individual(A.size()),new Individual(B.size()));


        int crossingPoint = A.size()/2;

        for(int i = 0; i < crossingPoint; ++i)
        {
            childs.getChildA().add(A.getGen(i));
            childs.getChildB().add(B.getGen(i));
        }

        for(int i = crossingPoint; i < A.size(); ++i)
        {
            childs.getChildA().add(B.getGen(i));
            childs.getChildB().add(A.getGen(i));
        }

        return childs;*/
        return null;
    }
}
