package com.massisframework.massis3.examples.genetic.SelectionCommands;

import com.massisframework.massis3.examples.genetic.Gene;
import com.massisframework.massis3.examples.genetic.Individual;
import com.massisframework.massis3.examples.genetic.Population;
import com.massisframework.massis3.examples.genetic.cross.AbstractCross;
import com.massisframework.massis3.examples.genetic.cross.MultipointCross;
import com.massisframework.massis3.examples.genetic.cross.SimpleCross;

import java.util.List;

public abstract class AbstractSelection<T extends Gene> {

    public enum AbstractSelectionMethods {HIERARCHICAL, ROULETTE, TOURNAMENT}

    public static <T extends Gene> AbstractSelection<T> getSelectionMethod(AbstractSelection.AbstractSelectionMethods id) {
        switch (id) {
            case HIERARCHICAL:
                return new Hierarchical<T>();
            case ROULETTE:
                return new RouletteSelection<T>();
            case TOURNAMENT:
                return new Tournament<T>();
        }
        return null;
    }

    public abstract Population<T> getSelection(Population<T> p, int elitims);

}
