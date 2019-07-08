package com.massisframework.massis3.examples.simulator.Components;

import com.massisframework.massis3.examples.simulator.Core.Component;
import com.massisframework.massis3.examples.simulator.Entity.Entity;

import java.util.List;

import static com.massisframework.massis3.examples.simulator.Core.SerializableType.FLOAT;
import static com.massisframework.massis3.examples.simulator.Core.SerializableType.INT;
import static com.massisframework.massis3.examples.simulator.Core.SerializableType.STRING;
import static com.massisframework.massis3.examples.simulator.Core.TypeOfType.SIMPLE;

public class Sensor extends Component {


    @Attribute(name = "timeBetweenMeasures", type = INT, typeOfType = SIMPLE)
    public int _timeBetweenMeasures;

    @Attribute(name = "sensorName", type = STRING, typeOfType = SIMPLE)
    public String _sensorName;

    private int _time;

    private int _pedestriansDetected;

    @Override
    public void init() {

        _time = 0;
        _pedestriansDetected = 0;
    }

    public int getPedestriansDetected() {
        return _pedestriansDetected;
    }

    public String getSensorName() {
        return _sensorName;
    }

    @Override
    protected void onStart() {
        _time = 0;
        _pedestriansDetected = 0;
    }

    @Override
    protected void onUpdate(int time) {

        _time += time;
        if (_time >= _timeBetweenMeasures) {

            _time = _timeBetweenMeasures - _time;
            List<Entity> pedestrianInMyNode = getTransfor().getPosition().getEntitysByComponent(Pedestrian.class);
            _pedestriansDetected += pedestrianInMyNode.size();
            //if(_sensorName.compareTo("PasilloAscensores")==0)
            //  System.out.print("Sensor de pasilloAscesor "+pedestrianInMyNode.size() + "\n");
        }
    }

    @Override
    protected void onEnd() {


    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }


}
