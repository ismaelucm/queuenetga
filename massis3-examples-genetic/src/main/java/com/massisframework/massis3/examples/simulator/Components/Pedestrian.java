package com.massisframework.massis3.examples.simulator.Components;


import com.massisframework.massis3.examples.simulator.Core.Component;
import com.massisframework.massis3.examples.simulator.Genetic.PedestrianGene;
import com.massisframework.massis3.examples.simulator.World.SimulationNode;

import java.util.ArrayList;
import java.util.List;

import static com.massisframework.massis3.examples.simulator.Core.SerializableType.*;
import static com.massisframework.massis3.examples.simulator.Core.TypeOfType.*;

public class Pedestrian extends Component {

    public static final String WAIT = "#WAIT";

    @Attribute(name = "path", type = STRING, typeOfType = LIST)
    public List<String> _path;

    protected int _time;
    protected int _currentNode;


    @Override
    public void init() {

        _time = 0;
        _currentNode = 0;
        getEntity().setTag(Globals.TAGS.PEDESTRIAN);
    }

    @Override
    protected void onStart() {
        _time = 0;
        _currentNode = 0;
    }

    @Override
    protected void onUpdate(int time) {

        _time += time;
    }

    public boolean canMove(int timeToCrossNode) {
        return _time >= timeToCrossNode;
    }

    public void reset(PedestrianGene p) {
        if (_path == null)
            _path = new ArrayList<>();
        _path.clear();
        for (int i = 0; i < p.size(); ++i) {
            int gen = p.get(i);
            String node = PedestrianGene.decodificate(gen);
            _path.add(node);
        }
    }

    public SimulationNode getNextNode() {
        if ((_currentNode + 1) < _path.size()) {
            String next = _path.get(_currentNode + 1);
            if (next.compareTo(WAIT) != 0)
                return this.getEntity().getWorld().getNode(next);
            else
                return this.getTransfor().getPosition();
        } else
            return null;

    }

    public boolean isPathFinised() {
        return _currentNode >= (_path.size() - 1);
    }

    public void move() {
        SimulationNode nextNode = getNextNode();
        if (nextNode != null) {
            getTransfor().setPosition(nextNode);
            _currentNode++;
        }
    }

    public List<String> getPath() {
        return _path;
    }

    public void setPath(List<String> path) {
        _path = path;
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
