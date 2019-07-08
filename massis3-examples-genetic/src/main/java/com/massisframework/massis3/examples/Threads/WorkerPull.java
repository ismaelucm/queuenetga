package com.massisframework.massis3.examples.Threads;

import com.massisframework.massis3.examples.simulator.Genetic.Calculators.Calculators;

import java.util.ArrayList;
import java.util.List;

public abstract class WorkerPull
{

    private int _numThreads;
    List<Worker> _workerList;
    List<Thread> _threadList;

    public WorkerPull(int numWorkers)
    {
        _numThreads = numWorkers;
        _workerList = new ArrayList<>();
        _threadList = new ArrayList<>();
    }

    public void start()
    {
        for(int i = 0; i < _workerList.size(); ++i)
        {
            Thread thread = new Thread(_workerList.get(i));
            thread.start();
            _threadList.add(thread);
        }
    }

    public void join() throws InterruptedException {
        for(int i = 0; i < _threadList.size(); ++i)
        {
            Thread thread = _threadList.get(i);
            thread.join();
        }
    }


    public int getNumThread()
    {
        return _numThreads;
    }

    public Worker getWorker(int workerID)
    {
        return _workerList.get(workerID);
    }

    public synchronized boolean isAllWorkedFinished()
    {
        int numThreadsFinised = 0;
        for(int i = 0; i < _numThreads; ++i)
        {
            if(_workerList.get(i).isFinisehd())
                numThreadsFinised++;
        }

        return numThreadsFinised == _numThreads;
    }

    public int getNumCalculators() { return 0;}

    public Calculators getCalculator(int id)
    {
        return null;
    }
}
