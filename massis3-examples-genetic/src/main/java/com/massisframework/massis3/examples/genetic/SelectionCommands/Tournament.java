package com.massisframework.massis3.examples.genetic.SelectionCommands;

import com.massisframework.massis3.Dice;
import com.massisframework.massis3.examples.genetic.Gene;
import com.massisframework.massis3.examples.genetic.Individual;
import com.massisframework.massis3.examples.genetic.Population;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Tournament<T extends Gene> extends AbstractSelection<T> {

    private int _tournamentSize;

    public Tournament() {
        super();
    }

    public void configure(Population population) {

    }

    public void setAdicionalConfiguration(int tournamentSize) {
        _tournamentSize = tournamentSize;
    }


    @Override
    public Population<T> getSelection(Population<T> p, int elitism) {
        Population<T> newPopulation = new Population<>(p.size(), p.getChromosomeLenght());

        List<Individual<T>> tournament = new ArrayList<>();
        for (int i = 0; i < elitism; ++i) {
            Individual<T> individual = p.getIndividual(i);
            Individual<T> newIndividdual = individual.clone();
            newPopulation.addIndividual(newIndividdual);
        }
        for (int i = elitism; i < p.size(); ++i) {
            for (int j = 0; j < _tournamentSize; ++j) {
                int position = Dice.GetRange(0, p.size() - 1);
                Individual<T> individual = p.getIndividual(position);
                tournament.add(j, individual);
            }
            Population.Sort(tournament);
            newPopulation.addIndividual(tournament.get(0).clone());
            tournament.clear();
        }
        return newPopulation;
    }

    public int getTournamentSize() {
        return _tournamentSize;
    }

    public void setToornamentSize(int s) {
        _tournamentSize = s;
    }


}
