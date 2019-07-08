package com.massisframework.massis3.examples.simulator.Core;

import com.massisframework.massis3.Pair;
import com.massisframework.massis3.examples.genetic.Individual;
import com.massisframework.massis3.examples.simulator.Configuration.ArquetypeSetDescription;
import com.massisframework.massis3.examples.simulator.Configuration.SimulationConfig;
import com.massisframework.massis3.examples.simulator.Configuration.SimulationDescription;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.PedestrianGA;
import com.massisframework.massis3.examples.simulator.Genetic.PedestrianGene;

import java.util.List;

public class Simulation {
    private int _stepTime;
    private int _numSteps;
    private int _simulationTime;
    private Simulator _simulator;
    private SimulationConfig _simConfig;


    public Simulation() {
        _stepTime = 1;
        _numSteps = Integer.MAX_VALUE;
        _simulationTime = Integer.MAX_VALUE;
        _simulator = new Simulator();
    }

    public void addArquetype(ArquetypeSetDescription ar) {
        _simulator.addArquetype(ar);
    }


    public void configure(SimulationDescription sim) {
        _simConfig = sim.getSimulationConfig();
        _numSteps = _simConfig.getNumSteps();
        _simulationTime = _simConfig.getSimulationTime();
        _stepTime = _simConfig.getStepTime();
        _simulator.configure(sim.getSimulatorConfig());
    }


    protected void setSimulator(Simulator simulator)
    {
        _simulator = simulator;
    }

    public void reset() {
        _simulator.reset();
    }

    public void resetPopulationSim(List<PedestrianGene> individual, String arquetypeSetName) throws Exception {
        _simulator.resetPopulationSim(individual, arquetypeSetName);
    }

    public void resetPopulation(List<PedestrianGA> individual) throws Exception {
        _simulator.resetPopulation(individual);
    }

    public Simulator getSimulator() {
        return _simulator;
    }

    public void run() throws Exception {
        int steps = 0;
        int time = 0;
        _simulator.start();
        //System.out.println("---------------- Simulation Start ---------------------------");
        while (steps < _numSteps && time < _simulationTime && !_simulator.isSimulatoinFinished()) {
            _simulator.update(_stepTime);
            time += _stepTime;
            steps++;
            //System.out.println("Step "+steps+ " Time simulated "+time);
        }
        _simulator.end();
        //System.out.println("---------------- Simulation End "+time+"---------------------------");
    }

    public List<String> getAdjacents(String nome) {
        return _simulator.getAdjacents(nome);
    }

    public List<String> getNodes() {
        return _simulator.getWorld().getAllNodes();
    }

    public Object getResult() {
        return _simulator.getResult();
    }

    public <T> T getResultType() {
        return _simulator.<T>getResultType();
    }


    public Simulation withStepTime(int t) {
        setStepTime(t);
        return this;
    }

    public int getStepTime() {
        return _stepTime;
    }

    public void setStepTime(int stepTime) {
        this._stepTime = stepTime;
    }

    public int getNumSteps() {
        return _numSteps;
    }

    public Simulation withNumSteps(int n) {
        setNumSteps(n);
        return this;
    }

    public void setNumSteps(int _numSteps) {
        this._numSteps = _numSteps;
    }

    public int getSimulationTime() {
        return _simulationTime;
    }

    public void setSimulationTime(int _simulationTime) {
        this._simulationTime = _simulationTime;
    }

    public Simulation withSimulationTime(int t) {
        setSimulationTime(t);
        return this;
    }
}
