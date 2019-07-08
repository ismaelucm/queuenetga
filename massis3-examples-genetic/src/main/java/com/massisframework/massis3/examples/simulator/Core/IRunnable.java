package com.massisframework.massis3.examples.simulator.Core;

public interface IRunnable {
    void start() throws Exception;

    void update(int time) throws Exception;

    void end();
}
