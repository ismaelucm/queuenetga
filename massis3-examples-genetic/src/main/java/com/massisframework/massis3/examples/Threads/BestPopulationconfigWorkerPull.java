package com.massisframework.massis3.examples.Threads;

import com.massisframework.massis3.examples.simulator.Genetic.Calculators.CalcFitnessBestPopulationConfig;
import com.massisframework.massis3.examples.simulator.Genetic.Calculators.CalcFitnessBestQueueSim;
import com.massisframework.massis3.examples.simulator.Genetic.Calculators.Calculators;

import java.util.List;

public class BestPopulationconfigWorkerPull extends WorkerPull {

    List<CalcFitnessBestPopulationConfig> _listOfCalculators;


    public BestPopulationconfigWorkerPull(List<CalcFitnessBestPopulationConfig> listOfCalculators) {
        super(listOfCalculators.size());
        _listOfCalculators = listOfCalculators;
        createWorkers();
    }

    protected void createWorkers()
    {
        for(int i = 0; i < getNumThread(); ++i)
        {
            BestPopulationConfigWorker w = new BestPopulationConfigWorker(i,_listOfCalculators.get(i));
            _workerList.add(w);
        }
    }

}
