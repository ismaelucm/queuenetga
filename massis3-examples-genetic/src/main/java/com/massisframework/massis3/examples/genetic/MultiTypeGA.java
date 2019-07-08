package com.massisframework.massis3.examples.genetic;

import com.massisframework.massis3.Dice;
import com.massisframework.massis3.examples.genetic.cross.CrossPair;

public class MultiTypeGA {
    private int _elitism;
    private float _mutationRate;
    private float _crossoverRate;
    //PArameters to configure the population.
    private int _populationSize;

    //parameters to configure the genetic execution.
    private int _maxGenerations;

    private MultiGenotypePopulation _population;
    private IProblemMultyType _problem;

    private float _bestFitness;
    private MultiGenotypeIndividual _theBestOfThebest;
    private String _fileOutput;
    private int _generation = 0;

    public MultiTypeGA(IProblemMultyType problem, GeneticAlSetup setup, String output, int... chromosomeLenghts) {
        _populationSize = setup.getPopulationSize();
        _mutationRate = setup.getMutationRate();
        _crossoverRate = setup.getCrossoverRate();
        _elitism = setup.getElitism();
        _maxGenerations = setup.getNumMaxGeneratoin();
        _problem = problem;
        _fileOutput = output;
        _population = new MultiGenotypePopulation(_populationSize, chromosomeLenghts);
    }

    public MultiTypeGA(IProblemMultyType problem, int maxGenerations, int populationSize, int chromosomeLenght, float mutationRate, float crossoverRate, int elitism, String output, int... chromosomeLenghts) {
        _populationSize = populationSize;

        _mutationRate = mutationRate;
        _crossoverRate = crossoverRate;
        _elitism = elitism;
        _problem = problem;
        _maxGenerations = maxGenerations;
        _population = new MultiGenotypePopulation(_populationSize, chromosomeLenghts);
        _fileOutput = output;
    }

    public void init() throws Exception {
        _population.initPopulation(_problem);
        evaluatePopulation();
        _generation = 0;
    }

    public void evaluatePopulation() throws Exception {
        float populationFitness = 0;
        for (int i = 0; i < _populationSize; ++i) {
            MultiGenotypeIndividual individual = _population.getIndividual(i);
            float fitness = calcFitness(individual);
            if (fitness != Float.MIN_VALUE) {
                individual.setFitness(fitness);
                populationFitness += fitness;
            } else
                throw new Exception("Evaluation Error");
        }
        _population.setFitness(populationFitness);
        _population.sort();
    }

    public float calcFitness(MultiGenotypeIndividual in) {
        return _problem.calcFitness(in);
    }

    public void run(boolean debug) throws Exception {
        while (!nextGeneration(_generation++)) {
            MultiGenotypeIndividual individual = getTheBest();
            if (individual.getFitness() > _bestFitness) {
                _bestFitness = individual.getFitness();
                _theBestOfThebest = individual.clone();
            }
            if (debug)
                System.out.println("Generation " + _generation + " Best fitness " + individual.getFitness() + " The best all the time fitness " + _bestFitness);
        }
    }

    public boolean nextGeneration(int generation) throws Exception {
        MultiGenotypePopulation population = selectToReprodution();
        reproduction(population);
        evaluatePopulation();
        return isFinish(generation);
    }

    public MultiGenotypeIndividual getTheBest() {
        return _population.getIndividual(0);
    }

    public MultiGenotypePopulation selectToReprodution() {
        return _problem.getAbstractSelectionMethod().getSelection(_population, _elitism);
    }

    public void reproduction(MultiGenotypePopulation population) {
        int numCrossovers = crossover(population);
        int numMutations = mutation(population);
        System.out.println("Crossovers " + numCrossovers + " num mutations " + numMutations);
        //customOperator(population);
        _population = population;
    }

    public int crossover(MultiGenotypePopulation population) {
        //System.out.println("--crossover--");
        //population.getIndividual(0).show();
        int crossovers = 0;
        for (int i = _elitism; i < population.size(); ++i) {
            float random = (float) Math.random();
            if (random < _crossoverRate) {
                MultiGenotypeIndividual in1 = population.getIndividual(i);
                int rand2 = Dice.GetRange(i, population.size() - 1);
                try {
                    MultiGenotypeIndividual in2 = population.getIndividual(rand2);
                    CrossPair<Gene> childs = _problem.getCrossMethod().cross(in1, in2, _problem);
                    // population.setIndividual(i,childs.getChildA());
                    //population.setIndividual(rand2,childs.getChildB());
                    crossovers++;

                } catch (Exception e) {
                    System.out.println("aaa");
                }

            }
        }
        //population.getIndividual(0).show();
        return crossovers;
    }

    public int mutation(MultiGenotypePopulation population) {
        //sort();
        //System.out.println("--mutation--");
        //population.getIndividual(0).show();
        int numMutations = 0;
        for (int i = _elitism; i < population.size(); ++i) {
            float random = (float) Math.random();
            if (random < _mutationRate) {
                MultiGenotypeIndividual individual = population.getIndividual(i);
                individual = _problem.mutate(individual);
                population.setIndividual(i, individual);
                numMutations++;
            }
        }
        //population.getIndividual(0).show();
        return numMutations;
    }

    public boolean isFinish(int generations) {
        return _problem.isFinish(_population, generations) || generations >= _maxGenerations;
    }
}
