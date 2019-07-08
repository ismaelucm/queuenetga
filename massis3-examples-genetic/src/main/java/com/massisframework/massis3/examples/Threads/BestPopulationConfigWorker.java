package com.massisframework.massis3.examples.Threads;

import com.massisframework.massis3.examples.simulator.Genetic.Calculators.CalcFitnessBestPopulationConfig;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.PedestrianIndividual;

import java.util.concurrent.Semaphore;

public class BestPopulationConfigWorker extends Worker{

    private CalcFitnessBestPopulationConfig _calculator;

    private PedestrianIndividual _individial;
    private float _fitness;

    Semaphore semaphoreInd = new Semaphore(1);

    public BestPopulationConfigWorker(int id, CalcFitnessBestPopulationConfig calc) {
        super(id);
        _calculator = calc;
    }

    public synchronized void setIndividual(final PedestrianIndividual individual)
    {
        //_individial = individual;
        try {
            semaphoreInd.acquire();
            _individial = individual;
            semaphoreInd.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected synchronized void runCommand() {

        try {
            semaphoreInd.acquire();
            float fitnees = _calculator.calcFitness(_individial);
            semaphoreInd.release();
            setFitness(fitnees);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized float getFitness()
    {
        float ret = -1;
        try {
            semaphoreInd.acquire();
            ret = _fitness;
            semaphoreInd.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
        //return _fitness;
    }

    protected synchronized void setFitness(float f)
    {
        try {
            semaphoreInd.acquire();
            _fitness = f;
            semaphoreInd.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //_fitness = f;
    }
}
