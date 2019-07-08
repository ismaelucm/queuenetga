package com.massisframework.massis3.examples.genetic_v2;


import com.massisframework.massis3.examples.simulator.Components.HelpperClasses.GeneticMaths;

public class GeneticAlgorithm implements Interruptible {


    private GeneticAlgorithmSetup _setup;
    private GeneticALProblem _problem;

    private PopulationGAL _population;
    private int _generation;
    private IndividualGAL _theBestofThebest;
    private float _bestFitness;
    private volatile boolean _stop;


    public GeneticAlgorithm(GeneticALProblem problem, GeneticAlgorithmSetup setup) {
        _setup = setup;
        _problem = problem;
        _stop = false;
        _population = _problem.createPopulation(setup.getPopulationSize(),true);
    }

    public GeneticAlgorithm(GeneticALProblem problem, GeneticAlgorithmSetup setup,PopulationGAL population) {
        _setup = setup;
        _problem = problem;
        _stop = false;
        _population = population;
    }

    public void init() {
        _problem.evaluate(_population);
        _generation = 0;
    }

    public void run(boolean debug) throws Exception {
        _bestFitness = -1000f;
        float populationDiversity = -1f;
        while (!nextGeneration(_generation++,populationDiversity) && !_stop) {
            IndividualGAL theBestOfThisPopulation = _problem.getTheBest(_population);
            float fitnessTheBestThisGen = theBestOfThisPopulation.getFitness();
            if (theBestOfThisPopulation.getFitness() > _bestFitness) {
                _bestFitness = theBestOfThisPopulation.getFitness();
                _theBestofThebest = theBestOfThisPopulation.clone();
            }
            else
            {
                if(Math.abs(fitnessTheBestThisGen - _bestFitness) > 0.0001)
                {
                    _problem.setTheBest(_theBestofThebest.clone(),_population);
                }
            }

            populationDiversity = _population.getPopulationDiversity();
            if (debug)
                System.out.println("Generation " + _generation + " Best fitness " + theBestOfThisPopulation.getFitness() + " The best all the time fitness " + _bestFitness + " Diversity "+populationDiversity);
        }

        _problem.finished();
    }

    public boolean nextGeneration(int generation, float populationDiversity) {
        PopulationGAL parents = reproduction(populationDiversity);
        parents.sort();
        _problem.crossover(parents, _setup.getCrossoverRate(), _setup.getElitism());

        float mutationRate = _setup.getMinMutationRate();
        float numGenesMutatedPerMutation = _setup.getMinNumGenesMutatedPerMutation();
        if(populationDiversity >= 0f)
        {
            mutationRate = GeneticMaths.diversityFuntion(_setup.getMinMutationRate(),_setup.getMaxMutationRate(),populationDiversity,GeneticMaths.ONE_MINUS);
            numGenesMutatedPerMutation = GeneticMaths.diversityFuntion(_setup.getMinNumGenesMutatedPerMutation(),_setup.getMaxNumGenesMutatedPerMutation(),populationDiversity,GeneticMaths.DIRECTED);
        }

        _problem.mutate(parents, mutationRate, numGenesMutatedPerMutation,_setup.getElitism());
        _population = replace(parents);
        _problem.evaluate(_population);
        _population.calcTotalFitness();
        return generation >= _setup.getMaxGenerations() || _problem.isFinish(_population, generation);
    }

    public PopulationGAL reproduction(float diversity) {
        PopulationGAL newPopulation = _problem.selection(_population, _setup.getElitism(),diversity);
        return newPopulation;
    }

    public PopulationGAL replace(PopulationGAL populationNew) {
        return populationNew;
    }

    public void stop() {
        _stop = true;
    }

    @Override
    public void interrupt() {
        stop();
    }




    public <T extends IndividualGAL> T getSolution() {
        return (T) _theBestofThebest;
    }
}
