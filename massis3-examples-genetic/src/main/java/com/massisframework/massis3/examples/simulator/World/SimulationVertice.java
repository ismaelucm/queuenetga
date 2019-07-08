package com.massisframework.massis3.examples.simulator.World;

public class SimulationVertice {

    public static int MIN_TRANSPORT_RATE = 1;
    public static int MAX_TRANSPORT_RATE = 30;
    public static int MIN_TIME_TO_CROSS = 1;
    public static int MAX_TIME_TO_CROSS = 50;

    private int _transportRate;
    private int _timeToCrossEdge;

    public boolean isTransporRateBlocked() {
        return _transporRateBlocked;
    }

    public void setTransporRateBlocked(boolean _transporRateBlocked) {
        this._transporRateBlocked = _transporRateBlocked;
    }


    public boolean isTimeToCrossEdgeBlocked() {
        return _timeToCrossEdgeBlocked;
    }

    public void setTimeToCrossEdgeBlocked(boolean timeToCrossNodeBlocked) {
        this._timeToCrossEdgeBlocked = timeToCrossNodeBlocked;
    }


    private boolean _transporRateBlocked;
    private boolean _timeToCrossEdgeBlocked;

    public SimulationVertice() {
        _transportRate = 0;
    }


    public SimulationVertice(int rate) {
        _transportRate = rate;
    }

    public int get_transportRate() {
        return _transportRate;
    }

    public void set_transportRate(int _transportRate) {
        this._transportRate = _transportRate;
    }

    public int get_timeToCrossEdge() {
        return _timeToCrossEdge;
    }

    public void set_timeToCrossEdge(int timeToCrossNode) {
        this._timeToCrossEdge = timeToCrossNode;
    }
}
