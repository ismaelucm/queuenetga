package com.massisframework.massis3.examples.simulator.Components;


import com.massisframework.massis3.examples.simulator.Components.HelpperClasses.SimulationResult;
import com.massisframework.massis3.examples.simulator.Core.Component;

import java.util.ArrayList;
import java.util.List;

public class SimulationFinishControll extends Component {


    private List<Pedestrian> _pedestrians;
    private SimulationResult _simulationResult;

    @Override
    public void init() {

        _simulationResult = new SimulationResult();
    }


    @Override
    protected void onStart() {

        _pedestrians = this.getEntity().getSimulator().getComponentsInTheSimulator(Pedestrian.class);
        //System.out.println("SimulationFinishControll");
    }

    @Override
    protected void onUpdate(int time) {

        if (!this.getEntity().getSimulator().isSimulatoinFinished()) {
            boolean simFinised = true;
            for (int i = 0; simFinised && i < _pedestrians.size(); ++i) {
                simFinised = _pedestrians.get(i).isPathFinised();
            }
            if (simFinised)
                this.getEntity().getSimulator().setSimulationFinished(simFinised);
        }
    }

    @Override
    protected void onEnd() {

        List<Sensor> sensors = this.getEntity().getSimulator().getComponentsInTheSimulator(Sensor.class);
        for (int i = 0; i < sensors.size(); ++i) {
            if (sensors.get(i).isEnable()) {
                _simulationResult.setSensorResult(sensors.get(i).getSensorName(), sensors.get(i).getPedestriansDetected());
            }
        }

        this.getEntity().getSimulator().setResults((Object) _simulationResult);
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }

}
