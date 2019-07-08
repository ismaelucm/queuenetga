package com.massisframework.massis3.examples.Threads;

import java.util.concurrent.Semaphore;

public abstract class Worker implements Runnable {

    private volatile boolean _active;
    private volatile boolean _stopped;
    private volatile boolean _finished;
    private int _id;


    Semaphore semaphoreFinish = new Semaphore(1);
    Semaphore semaphoreActive = new Semaphore(1);
    Semaphore semaphoreStopped = new Semaphore(1);

    public Worker(int id){

        notFinished();
        desactivate();
        notStop();
        _id = id;
    }

    public synchronized void activated()
    {
        try {
            semaphoreActive.acquire();
            _active = true;
            semaphoreActive.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected synchronized void desactivate()
    {
        try {
            semaphoreActive.acquire();
            _active = false;
            semaphoreActive.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected synchronized boolean isActive()
    {
        boolean ret = false;
        try {
            semaphoreActive.acquire();
            ret =_active;
            semaphoreActive.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public void run() {

        while(!isStopped()) {

            if(isActive()) {
                notFinished();
                runCommand();
                finished();
                desactivate();
            }
        }
    }

    public int getId() {return _id;}

    public synchronized void stop()
    {
        try {
            semaphoreStopped.acquire();
            _stopped = true;
            semaphoreStopped.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        };
    }

    public synchronized void notStop()
    {
        try {
            semaphoreStopped.acquire();
            _stopped = false;
            semaphoreStopped.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        };
    }

    public synchronized boolean isStopped()
    {
        boolean ret = false;
        try {
            semaphoreStopped.acquire();
            ret = _stopped;
            semaphoreStopped.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        };
        return ret;
    }

    public synchronized boolean isFinisehd()
    {
        boolean ret = false;
        try {
            semaphoreFinish.acquire();
            ret = _finished;
            semaphoreFinish.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        };
        return ret;
    }

    public synchronized void finished()
    {
        try {
            semaphoreFinish.acquire();
            _finished = true;
            semaphoreFinish.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        };
    }


    public synchronized void notFinished()
    {
        try {
            semaphoreFinish.acquire();
            _finished = false;
            semaphoreFinish.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        };
    }

    protected abstract void runCommand();

}
