package com.massisframework.massis3.examples.genetic;

import com.massisframework.massis3.Dice;
import com.massisframework.massis3.examples.genetic.cross.CrossPair;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeneticAl<T extends Gene> {
    //Parameters to cofigure the genetic algorithm
    private int _elitism;
    private float _mutationRate;
    private float _crossoverRate;
    //PArameters to configure the population.
    private int _populationSize;
    private int _chromosomeLenght;
    //parameters to configure the genetic execution.
    private int _maxGenerations;

    private Population<T> _population;
    private IProblem<T> _problem;

    private float _bestFitness;
    private Individual<T> _theBestofThebest;
    private String _fileOutput;
    private int _generation = 0;


    public GeneticAl(IProblem problem, GeneticAlSetup setup, String output) {
        _populationSize = setup.getPopulationSize();
        _chromosomeLenght = setup.getChromosomeLenght();
        _mutationRate = setup.getMutationRate();
        _crossoverRate = setup.getCrossoverRate();
        _elitism = setup.getElitism();
        _maxGenerations = setup.getNumMaxGeneratoin();
        _problem = problem;
        _fileOutput = output;
        _population = new Population(_populationSize, _chromosomeLenght);
    }

    public GeneticAl(IProblem problem, int maxGenerations, int populationSize, int chromosomeLenght, float mutationRate, float crossoverRate, int elitism, String output) {
        _populationSize = populationSize;
        _chromosomeLenght = chromosomeLenght;
        _mutationRate = mutationRate;
        _crossoverRate = crossoverRate;
        _elitism = elitism;
        _problem = problem;
        _maxGenerations = maxGenerations;
        _population = new Population(_populationSize, _chromosomeLenght);
        _fileOutput = output;
    }

    public void init() throws Exception {
        _population.initPopulation(_problem);
        evaluatePopulation();
        _generation = 0;
    }

    public boolean nextGeneration(int generation) throws Exception {
        Population<T> population = selectToReprodution();
        reproduction(population);
        evaluatePopulation();
        return isFinish(generation);
    }

    public void setMaxGenerations(int max) {
        _maxGenerations = max;
    }

    public int getMaxGenerations() {
        return _maxGenerations;
    }

    public void run(boolean debug) throws Exception {
        while (!nextGeneration(_generation++)) {
            Individual<T> individual = getTheBest();
            if (individual.getFitness() > _bestFitness) {
                _bestFitness = individual.getFitness();
                _theBestofThebest = individual.clone();
            }
            if (debug)
                System.out.println("Generation " + _generation + " Best fitness " + individual.getFitness() + " The best all the time fitness " + _bestFitness);
        }


    }

    public void writeSolution() throws Exception {
        System.out.println("------ SOLUTION --------");
        System.out.println("Best individual " + _bestFitness);
        String s = _theBestofThebest.serializePlain();
        System.out.print(s);
        if (_fileOutput != null && _fileOutput.compareTo("") != 0) {
            System.out.println("writing the best individual found in " + _fileOutput);
            Path path = Paths.get(_fileOutput);

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(s);
                writer.write("");
                writer.write("" + _bestFitness);
            }
        }
    }

    public Individual<T> getSolution() {
        return _theBestofThebest;
    }

    public Individual<T> getTheBest() {
        return _population.getIndividual(0);
    }

    public Population<T> selectToReprodution() {
        return _problem.getAbstractSelectionMethod().getSelection(_population, _elitism);
    }

    public void reproduction(Population<T> population) {
        int numCrossovers = crossover(population);
        int numMutations = mutation(population);
        System.out.println("Crossovers " + numCrossovers + " num mutations " + numMutations);
        //customOperator(population);
        _population = population;
    }

    public Population getPopulation() {
        return _population;
    }


    public float calcFitness(Individual in) {
        return _problem.calcFitness(in);
    }

    public void evaluatePopulation() throws Exception {
        float populationFitness = 0;
        for (int i = 0; i < _populationSize; ++i) {
            Individual individual = _population.getIndividual(i);
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


    public boolean isFinish(int generations) {
        return _problem.isFinish(_population, generations) || generations >= _maxGenerations;
    }

    public void sort() {
        _population.sort();
    }

    public void customOperator(Population population) {
        _problem.customOperator(population, _elitism, _crossoverRate, _mutationRate);
    }

    public int mutation(Population population) {
        //sort();
        //System.out.println("--mutation--");
        //population.getIndividual(0).show();
        int numMutations = 0;
        for (int i = _elitism; i < population.size(); ++i) {
            float random = (float) Math.random();
            if (random < _mutationRate) {
                Individual individual = population.getIndividual(i);
                individual = _problem.mutate(individual);
                population.setIndividual(i, individual);
                numMutations++;
            }
        }
        //population.getIndividual(0).show();
        return numMutations;
    }

    public int crossover(Population<T> population) {
        //System.out.println("--crossover--");
        //population.getIndividual(0).show();
        int crossovers = 0;
        for (int i = _elitism; i < population.size(); ++i) {
            float random = (float) Math.random();
            if (random < _crossoverRate) {
                Individual<T> in1 = population.getIndividual(i);
                int rand2 = Dice.GetRange(i, population.size() - 1);
                try {
                    Individual<T> in2 = population.getIndividual(rand2);
                    CrossPair<T> childs = _problem.getCrossMethod().cross(in1, in2, _problem);
                    population.setIndividual(i, childs.getChildA());
                    population.setIndividual(rand2, childs.getChildB());
                    crossovers++;

                } catch (Exception e) {
                    System.out.println("aaa");
                }

            }
        }
        //population.getIndividual(0).show();
        return crossovers;
    }


}
