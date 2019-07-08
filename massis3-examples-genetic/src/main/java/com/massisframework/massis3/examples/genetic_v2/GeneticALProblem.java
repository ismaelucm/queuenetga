package com.massisframework.massis3.examples.genetic_v2;

import com.massisframework.massis3.Dice;
import com.massisframework.massis3.examples.Threads.WorkerPull;
import com.massisframework.massis3.examples.simulator.Genetic.Calculators.Calculators;

import java.util.ArrayList;
import java.util.List;


public abstract class GeneticALProblem {

    private CrossMethod _cross;
    private SelectionMethod _selectionMethod;
    private WorkerPull _workerPull;
    private List<Calculators> _calculatorFitness;

    public GeneticALProblem(CrossMethod cross, SelectionMethod select) {
        _cross = cross;
        _selectionMethod = select;
        _workerPull = null;
        _calculatorFitness = new ArrayList<>();
    }

    public GeneticALProblem(CrossMethod cross, SelectionMethod select, WorkerPull workerPull) {
        _cross = cross;
        _selectionMethod = select;
        _workerPull = workerPull;
        _workerPull.start();
        _calculatorFitness = new ArrayList<>();
    }

    public Calculators getCalculator(int id)
    {
        return _workerPull == null ? _calculatorFitness.get(id) : _workerPull.getCalculator(id);
    }

    public int getNumCalculators()
    {
        return _workerPull == null ? _calculatorFitness.size() : _workerPull.getNumCalculators();
    }

    protected <T extends WorkerPull> T getWorkerPull()
    {
        return (T) _workerPull;
    }

    public boolean isFinish(PopulationGAL p, int generations) {
        return false;
    }

    public void mutate(PopulationGAL population, float mutationRate, float numGenesMutation, int elitism) {

        for (int i = elitism; i < population.size(); ++i) {
            IndividualGAL ind = population.getCast(i);
            float prob = Dice.GetRange(0, 1f);
            if (prob <= mutationRate) {
                IndividualGAL in1 = population.get(i);
                mutate(in1,numGenesMutation);
            }
        }
    }

    public void crossover(PopulationGAL population, float crossoverRate, int elitism) {
        for (int i = elitism; i < population.size(); ++i) {
            float prob = Dice.GetRange(0, 1f);
            if (prob <= crossoverRate) {
                IndividualGAL in1 = population.get(i);
                int rand2 = Dice.GetRange(i, population.size() - 1);
                IndividualGAL in2 = population.get(rand2);
                IndividualGAL[] childs = cross(in1, in2);
                population.set(i, childs[0]);
                population.set(rand2, childs[1]);
            }
        }
    }


    public abstract void pararellEvaluate(PopulationGAL population);


    public void evaluateSingleCore(PopulationGAL population)
    {
        for (int i = 0; i < population.size(); ++i) {
            IndividualGAL ind = population.get(i);
            float fitness = calcFitness(ind);
            //TODO: LA función fitness no es estocástica. Hay que mirar porque.
            /*System.out.println(fitness);
            System.out.println(calcFitness(ind));
            System.out.println(calcFitness(ind));
            System.out.println("-----");*/
            //float fitness2 = calcFitness(ind);
            /*if(Math.abs(fitness-fitness2) > 0.01f)
                throw new RuntimeException("la función de fitness es estocástica");*/
            ind.setFitness(fitness);
        }
    }

    public void evaluate(PopulationGAL population) {

        if(_workerPull==null)
            evaluateSingleCore(population);
        else
            pararellEvaluate(population);

        population.sort();
    }

    public PopulationGAL selection(PopulationGAL population, int elitism, float diversity) {
        return _selectionMethod.select(population, this, elitism,diversity);
    }


    public IndividualGAL getTheBest(PopulationGAL population) {
        return population.getFirst();
    }

    public void setTheBest(IndividualGAL ind,PopulationGAL population)
    {
        population.set(0,ind);
    }

    protected IndividualGAL[] cross(IndividualGAL ind1, IndividualGAL ind2) {
        IndividualGAL[] result = createIndividualArray(2);
        _cross.cross(ind1, ind2, result[0], result[1]);
        return result;
    }

    public void finished()
    {

    }

    public void join()
    {
        try {
            _workerPull.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addNewCalculator(Calculators calc)
    {
        _calculatorFitness.add(calc);
    }

    protected float calcFitness(IndividualGAL ind) {

        float fitness = 0f;
        for(int i = 0; i < _calculatorFitness.size(); ++i)
        {
            fitness += _calculatorFitness.get(i).calcFitness(ind);
        }

        return fitness /  _calculatorFitness.size();
    }

    public abstract PopulationGAL createPopulation(int populationSize, boolean initialized);

    protected abstract void mutate(IndividualGAL ind, float numGenMutations);

    public abstract IndividualGAL[] createIndividualArray(int numElements);



}
