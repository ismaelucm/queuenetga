package com.massisframework.massis3.examples.simulator.Genetic.Individuals;

import com.massisframework.massis3.Dice;
import com.massisframework.massis3.examples.genetic_v2.IndividualGAL;
import com.massisframework.massis3.examples.simulator.World.SimulationNode;
import com.massisframework.massis3.examples.simulator.World.SimulationVertice;

public class IndividualQueueSimConfig extends IndividualGAL {

    private int[] _capacityGenotype;
    private int[] _timeGenotype;
    private int[] _transitionTimeGenotype;


    public IndividualQueueSimConfig(IndividualQueueSimConfig ind) {
        super();
        _capacityGenotype = new int[ind.getCapacityGenotypeSize()];
        _timeGenotype = new int[ind.getTimeGenotypeSize()];
        _transitionTimeGenotype = new int[ind.getTransitionTimeGenotypeSize()];

        for (int i = 0; i < _capacityGenotype.length; ++i) {
            _capacityGenotype[i] = ind._capacityGenotype[i];
        }

        for (int i = 0; i < _timeGenotype.length; ++i) {
            _timeGenotype[i] = ind._timeGenotype[i];
        }

        for (int i = 0; i < _transitionTimeGenotype.length; ++i) {
            _transitionTimeGenotype[i] = ind._transitionTimeGenotype[i];
        }
        setFitness(ind.getFitness());
    }

    public IndividualQueueSimConfig(IndividualQueueSimConfig ind,
                                    float capacitySizeProb, float timeCrossProb, float transitTimeProb,
                                    int minSizeOffset, int maxSizeOffset,
                                    int minTimeOffset, int maxTimeOffset,
                                    int minTransitOffset, int maxTransitOffset) {
        super();
        _capacityGenotype = new int[ind.getCapacityGenotypeSize()];
        _timeGenotype = new int[ind.getTimeGenotypeSize()];
        _transitionTimeGenotype = new int[ind.getTransitionTimeGenotypeSize()];

        for (int i = 0; i < _capacityGenotype.length; ++i) {
            _capacityGenotype[i] = ind._capacityGenotype[i];
        }

        for (int i = 0; i < _timeGenotype.length; ++i) {
            _timeGenotype[i] = ind._timeGenotype[i];
        }

        for (int i = 0; i < _transitionTimeGenotype.length; ++i) {
            _transitionTimeGenotype[i] = ind._transitionTimeGenotype[i];
        }
        setFitness(ind.getFitness());
    }

    @Override
    public int getNumLayers() {
        return 3;
    }

    public IndividualQueueSimConfig(int capacitySize, int timeSize, int transitiontimeSize, boolean initialized) {
        super();
        _capacityGenotype = new int[capacitySize];
        _timeGenotype = new int[timeSize];
        _transitionTimeGenotype = new int[transitiontimeSize];
        if(initialized)
            initialize();
    }

    public void setCapacityArray(int[] capacityArray)
    {
        _capacityGenotype=capacityArray;
    }

    public void setTimeArray(int[] timeArray)
    {
        _timeGenotype=timeArray;
    }

    public void setTransitionSize(int[] transitArray)
    {
        _transitionTimeGenotype=transitArray;
    }


    protected void initialize()
    {
        for (int i = 0; i < _capacityGenotype.length; ++i) {
            _capacityGenotype[i] = Dice.GetRange(SimulationNode.MIN_CAPACITY,SimulationNode.MAX_CAPACITY);
        }

        for (int i = 0; i < _timeGenotype.length; ++i) {
            _timeGenotype[i] = Dice.GetRange(SimulationVertice.MIN_TIME_TO_CROSS,SimulationVertice.MAX_TIME_TO_CROSS);
        }

        for (int i = 0; i < _transitionTimeGenotype.length; ++i) {
            _transitionTimeGenotype[i] = Dice.GetRange(SimulationVertice.MIN_TRANSPORT_RATE,SimulationVertice.MAX_TRANSPORT_RATE);
        }
    }


    @Override
    public IndividualGAL clone() {
        IndividualQueueSimConfig ind = new IndividualQueueSimConfig(this);
        return ind;
    }


    public int getCapacityGenotypeSize() {
        return _capacityGenotype.length;
    }

    public int getTimeGenotypeSize() {
        return _timeGenotype.length;
    }

    public int getTransitionTimeGenotypeSize() {
        return _transitionTimeGenotype.length;
    }

    public void copyCross(int layer, int a, int b, IndividualGAL parent2, IndividualGAL child1, IndividualGAL child2, boolean cross) {
        IndividualQueueSimConfig child2Local = (IndividualQueueSimConfig) child2;
        IndividualQueueSimConfig child1Local = (IndividualQueueSimConfig) child1;
        IndividualQueueSimConfig parent2Local = (IndividualQueueSimConfig) parent2;
        for (int i = a; i < b; ++i) {
            if (cross) {
                if (layer == 0) {
                    child2Local._capacityGenotype[i] = _capacityGenotype[i];//p1 => c2
                    child1Local._capacityGenotype[i] = parent2Local._capacityGenotype[i];//p2 => c1
                } else if (layer == 1) {
                    child2Local._timeGenotype[i] = _timeGenotype[i];//p1 => c2
                    child1Local._timeGenotype[i] = parent2Local._timeGenotype[i];//p2 => c1
                } else if (layer == 2) {
                    child2Local._transitionTimeGenotype[i] = _transitionTimeGenotype[i];//p1 => c2
                    child1Local._transitionTimeGenotype[i] = parent2Local._transitionTimeGenotype[i];//p2 => c1
                }
            } else {
                if (layer == 0) {
                    child1Local._capacityGenotype[i] = _capacityGenotype[i];//p1 => c1
                    child2Local._capacityGenotype[i] = parent2Local._capacityGenotype[i];//p2 => c2
                } else if (layer == 1) {
                    child1Local._timeGenotype[i] = _timeGenotype[i];//p1 => c1
                    child2Local._timeGenotype[i] = parent2Local._timeGenotype[i];//p2 => c2
                } else if (layer == 2) {
                    child1Local._transitionTimeGenotype[i] = _transitionTimeGenotype[i];//p1 => c1
                    child2Local._transitionTimeGenotype[i] = parent2Local._transitionTimeGenotype[i];//p2 => c2
                }
            }
        }
    }

    public int getChromosomeSize(int layer) {
        if (layer == 0)
            return _capacityGenotype.length;
        else if (layer == 1)
            return _timeGenotype.length;
        else if (layer == 2)
            return _transitionTimeGenotype.length;
        else
            return -1;
    }

    public void mutate(int layer, int chromosome) {
        if (layer == 0)
            _capacityGenotype[chromosome] = capcacityRandValue();
        else if (layer == 1)
            _timeGenotype[chromosome] = timeRandValue();
        else if (layer == 2)
            _transitionTimeGenotype[chromosome] = transitionTimeRandValue();
    }

    public int capcacityRandValue() {
        return Dice.GetRange(SimulationNode.MIN_CAPACITY, SimulationNode.MAX_CAPACITY);
    }

    public int timeRandValue() {
        return Dice.GetRange(SimulationVertice.MIN_TIME_TO_CROSS, SimulationVertice.MAX_TIME_TO_CROSS);
    }

    public int transitionTimeRandValue() {
        return Dice.GetRange(SimulationVertice.MIN_TRANSPORT_RATE, SimulationVertice.MAX_TRANSPORT_RATE);
    }

    public int[] getNodeSizeList() {
        return _capacityGenotype;
    }

    public int getDomineLayer(int layer)
    {
        if(layer == 0)
            return SimulationNode.MAX_CAPACITY-SimulationNode.MIN_CAPACITY;
        else if(layer == 1)
            return SimulationVertice.MAX_TIME_TO_CROSS-SimulationVertice.MIN_TIME_TO_CROSS;
        else if(layer == 2)
            return SimulationVertice.MAX_TRANSPORT_RATE-SimulationVertice.MIN_TRANSPORT_RATE;
        else
            return -1;
    }

    public int getCountLayer(int layer)
    {
        if(layer == 0)
            return getNodeSizeList().length;
        else if(layer == 1)
            return getTimeList().length;
        else if(layer == 2)
            return getTransitionList().length;
        else
            return -1;
    }

    public int[] getLayer(int layer)
    {
        if(layer == 0)
            return getNodeSizeList();
        else if(layer == 1)
            return getTimeList();
        else if(layer == 2)
            return getTransitionList();
        else
            return null;
    }

    public int[] getTimeList() {
        return _timeGenotype;
    }

    public int[] getTransitionList() {
        return _transitionTimeGenotype;
    }

    public String serialize() {
        String capacityGenotypeStr = "";
        String timeGenotype = "";
        String transitionTimeGenotype = "";
        for (int i = 0; i < _capacityGenotype.length; ++i) {
            int value = _capacityGenotype[i];
            capacityGenotypeStr += value;
            if (i < (_capacityGenotype.length - 1))
                capacityGenotypeStr += ",";
        }

        for (int i = 0; i < _timeGenotype.length; ++i) {
            float value = _timeGenotype[i];
            timeGenotype += value;
            if (i < (_timeGenotype.length - 1))
                timeGenotype += ",";
            ;
        }

        for (int i = 0; i < _transitionTimeGenotype.length; ++i) {
            float value = _transitionTimeGenotype[i];
            transitionTimeGenotype += value;
            if (i < (_transitionTimeGenotype.length - 1))
                transitionTimeGenotype += ",";
            ;
        }

        return capacityGenotypeStr + "\n" + timeGenotype + "\n" + transitionTimeGenotype;
    }
}
