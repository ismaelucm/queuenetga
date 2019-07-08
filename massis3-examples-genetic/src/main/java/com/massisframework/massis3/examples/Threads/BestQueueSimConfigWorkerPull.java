package com.massisframework.massis3.examples.Threads;

import com.massisframework.massis3.examples.simulator.Genetic.Calculators.CalcFitnessBestQueueSim;
import com.massisframework.massis3.examples.simulator.Genetic.Calculators.Calculators;

import java.util.List;

public class BestQueueSimConfigWorkerPull extends WorkerPull {

    List<List<CalcFitnessBestQueueSim>> _listOfCalculators;

    public BestQueueSimConfigWorkerPull(List<List<CalcFitnessBestQueueSim>> listOfCalculators) {
        super(listOfCalculators.size());
        _listOfCalculators = listOfCalculators;
        createWorkers();
    }

    protected void createWorkers()
    {
        for(int i = 0; i < getNumThread(); ++i)
        {
            BestQueueSimConfigWorker w = new BestQueueSimConfigWorker(i,_listOfCalculators.get(i));
            _workerList.add(w);
        }
    }

    public int getNumNodes()
    {
        return _listOfCalculators.get(0).get(0).getSimulation().getSimulator().getWorld().getAllNodes().size();
    }

    public int getNumTransitions()
    {
        return _listOfCalculators.get(0).get(0).getSimulation().getSimulator().getWorld().getAllTansitionOnlyOneDirection().size();
    }

    @Override
    public int getNumCalculators()
    {
        return _listOfCalculators.get(0).size();
    }

    @Override
    public Calculators getCalculator(int id)
    {
        return _listOfCalculators.get(0).get(id);
    }
}
