package com.massisframework.massis3.examples.genetic.SelectionCommands;

import com.massisframework.massis3.Dice;
import com.massisframework.massis3.examples.genetic.Individual;
import com.massisframework.massis3.examples.genetic.MultiGenotypeIndividual;
import com.massisframework.massis3.examples.genetic.MultiGenotypePopulation;
import com.massisframework.massis3.examples.genetic.Population;

import java.util.ArrayList;
import java.util.List;

public class TournamentMulti extends AbstractSelectionMulti {

    private int _tournamentSize;

    public TournamentMulti() {
        super();
    }

    public void configure(Population population) {

    }

    public void setAdicionalConfiguration(int tournamentSize) {
        _tournamentSize = tournamentSize;
    }


    @Override
    public MultiGenotypePopulation getSelection(MultiGenotypePopulation p, int elitism) {
        /*MultiGenotypePopulation newPopulation = new MultiGenotypePopulation(p.size(),p.getChromosomeLenght());

        List<MultiGenotypeIndividual> tournament = new ArrayList<>();
        for(int i = 0; i < elitism; ++i)
        {
            MultiGenotypeIndividual individual = p.getIndividual(i);
            MultiGenotypeIndividual newIndividdual = individual.clone();
            newPopulation.addIndividual(newIndividdual);
        }
        for(int i = elitism; i < p.size(); ++i)
        {
            for(int j = 0; j < _tournamentSize; ++j)
            {
                int position = Dice.GetRange(0,p.size()-1);
                MultiGenotypeIndividual individual = p.getIndividual(position);
                tournament.add(j,individual);
            }
            MultiGenotypePopulation.Sort(tournament);
            newPopulation.addIndividual(tournament.get(0).clone());
            tournament.clear();
        }
        return newPopulation;*/
        return null;
    }

    public int getTournamentSize() {
        return _tournamentSize;
    }

    public void setToornamentSize(int s) {
        _tournamentSize = s;
    }


}

