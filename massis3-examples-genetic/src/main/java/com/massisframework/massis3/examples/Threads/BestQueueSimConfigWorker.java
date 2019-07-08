package com.massisframework.massis3.examples.Threads;

import com.massisframework.massis3.examples.simulator.Genetic.Calculators.CalcFitnessBestQueueSim;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.IndividualQueueSimConfig;

import java.util.List;
import java.util.concurrent.Semaphore;

public class BestQueueSimConfigWorker extends Worker
{
    private List<CalcFitnessBestQueueSim> _calculator;
    private IndividualQueueSimConfig _individial;
    private float _fitness;

    Semaphore semaphoreInd = new Semaphore(1);

    public BestQueueSimConfigWorker(int id, List<CalcFitnessBestQueueSim> calc) {
        super(id);
        _calculator = calc;
    }

    public synchronized void setIndividual(final IndividualQueueSimConfig individual)
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

        int iteration = 0;
        try {
            semaphoreInd.acquire();
            float accumulatedFitness = 0f;
            for(int i=0; i < _calculator.size(); ++i) {
                float fitnees = _calculator.get(i).calcFitness(_individial);
                accumulatedFitness += fitnees;
            }
            accumulatedFitness = accumulatedFitness /  _calculator.size();
            setFitness(accumulatedFitness);
            semaphoreInd.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Problem in the iteration "+iteration);
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
        _fitness = f;
    }
}
