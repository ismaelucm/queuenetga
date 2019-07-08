package com.massisframework.massis3.examples.genetic_v2.SelectionMethods;

import com.massisframework.massis3.Dice;

import com.massisframework.massis3.examples.genetic_v2.*;
import com.massisframework.massis3.examples.simulator.Components.HelpperClasses.GeneticMaths;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tournaments implements SelectionMethod {
    private int _mintournamentSize;
    private int _maxtournamentSize;

    private boolean _exploration;
    public Tournaments(int min, int max) {
        _mintournamentSize = min;
        _maxtournamentSize = max;
        _exploration = false;
    }

    @Override
    public PopulationGAL select(PopulationGAL populationGal, GeneticALProblem problem, int elitism, float diversity) {
        PopulationGAL newPopilation = problem.createPopulation(populationGal.size(), false);
        //calcRelative(populationGal);

        for (int i = 0; i < elitism; ++i) {
            IndividualGAL individualGAL = populationGal.get(i);
            IndividualGAL newIndividdual = individualGAL.clone();
            newPopilation.set(i, newIndividdual);
        }

        int tournamentSize = calcTorunamentSize(diversity);
        for (int i = elitism; i < populationGal.size(); ++i) {
            IndividualGAL[] tournament = problem.createIndividualArray(tournamentSize);
            for (int j = 0; j < tournamentSize; ++j) {
                int position = Dice.GetRange(0, populationGal.size() - 1);
                IndividualGAL individual = populationGal.get(position);
                IndividualGAL newIndividdual = individual.clone();
                tournament[j] = newIndividdual;
            }
            populationGal.sort(tournament);// TODO: use GetTheBest is more efficient

            newPopilation.set(i, tournament[0]);
        }

        return newPopilation;
    }

    protected int calcTorunamentSize(float diversity)
    {
        if(diversity < 0f)
            return _maxtournamentSize;

        if(diversity < 0.42 && !_exploration) {
            _exploration = true;
            return _mintournamentSize;
        }

        if(_exploration && diversity > 0.70f)
        {
            _exploration = false;
        }
        else if(_exploration)
            return _mintournamentSize;

        return GeneticMaths.diversityFuntion(_mintournamentSize,_maxtournamentSize,diversity,GeneticMaths.DIRECTED);
    }
}
