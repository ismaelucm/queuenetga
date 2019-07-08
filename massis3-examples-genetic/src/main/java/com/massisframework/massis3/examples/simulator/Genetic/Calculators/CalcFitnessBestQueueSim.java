package com.massisframework.massis3.examples.simulator.Genetic.Calculators;

import com.massisframework.massis3.Pair;
import com.massisframework.massis3.examples.genetic_v2.IndividualGAL;
import com.massisframework.massis3.examples.simulator.Components.HelpperClasses.SimulationResult;
import com.massisframework.massis3.examples.simulator.Core.Simulation;
import com.massisframework.massis3.examples.simulator.Core.Simulator;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.IndividualQueueSimConfig;
import com.massisframework.massis3.examples.simulator.World.World;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class CalcFitnessBestQueueSim extends Calculators
{

    private String[] _orderNames;
    private List<Pair<String, String>> _transitionOrder;


    public CalcFitnessBestQueueSim(Simulation _simulation, String[] _orderNames,
                                   List<Pair<String, String>> _transitionOrder, SimulationResult resultToCompareFitness) {
        super(_simulation,resultToCompareFitness);
        this._orderNames = _orderNames;
        this._transitionOrder = _transitionOrder;

    }

    public List<Pair<String, String>> getPairs()
    {
        return _transitionOrder;
    }

    public String[] getOrderNames()
    {
        return _orderNames;
    }

    @Override
    public float calcFitness(IndividualGAL ind) {

        boolean error = false;
        try {
            this._simulation.reset();
            IndividualQueueSimConfig individualQueueSimConfig = (IndividualQueueSimConfig) ind;
            int[] nodeSizeList = individualQueueSimConfig.getNodeSizeList();
            int[] timeList = individualQueueSimConfig.getTimeList();
            int[] transitionList = individualQueueSimConfig.getTransitionList();
            Simulator simulator = _simulation.getSimulator();
            World world = simulator.getWorld();
            world.configure(nodeSizeList, timeList, transitionList, _orderNames, _transitionOrder);
            //this._simulation.resetPopulationSim(genotype, "ArquetypeSimulationConfigExample");
        } catch (Exception e) {
            e.printStackTrace();
            error = true;
        }

        if(!error)
            return calcSimilarity();
        else
            return 1.4E-45F;
    }



}
