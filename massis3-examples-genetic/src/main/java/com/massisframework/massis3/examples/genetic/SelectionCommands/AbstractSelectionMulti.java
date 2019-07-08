package com.massisframework.massis3.examples.genetic.SelectionCommands;

import com.massisframework.massis3.examples.genetic.Gene;
import com.massisframework.massis3.examples.genetic.MultiGenotypePopulation;
import com.massisframework.massis3.examples.genetic.Population;

public abstract class AbstractSelectionMulti {

    public enum AbstractSelectionMethods {HIERARCHICAL, ROULETTE, TOURNAMENT}

    public static AbstractSelectionMulti getSelectionMethod(AbstractSelection.AbstractSelectionMethods id) {
        switch (id) {
            case HIERARCHICAL:
                return new HierarchicalMulti();
            case ROULETTE:
                return new RouletteSelectionMulti();
            case TOURNAMENT:
                return new TournamentMulti();
        }
        return null;
    }

    public abstract MultiGenotypePopulation getSelection(MultiGenotypePopulation p, int elitims);
}
