package com.massisframework.massis3.examples.simulator.Core;

import com.massisframework.massis3.examples.simulator.Components.NodeTrafficControll;
import com.massisframework.massis3.examples.simulator.Components.Pedestrian;
import com.massisframework.massis3.examples.simulator.World.SimulationNode;

import java.util.ArrayList;
import java.util.List;

public class SimulationQueue {
    private List<Pedestrian> _list;

    public SimulationQueue() {
        _list = new ArrayList<>();
    }

    public void enqueue(Pedestrian p) {
        _list.add(p);
    }

    public List<Pedestrian> getPedestrianWithIncorrectPath(List<NodeTrafficControll.VerticeTraffic> verticeTraffics) {
        List<Pedestrian> set = new ArrayList<>();
        for (int i = 0; i < _list.size(); ++i) {
            Pedestrian pedestrian = _list.get(i);
            if (!pedestrian.isPathFinised()) {

                SimulationNode nextNode = pedestrian.getNextNode();
                SimulationNode currentPosition = pedestrian.getTransfor().getPosition();
                String nextNodeName = nextNode.getName();
                if (nextNode != currentPosition) {
                    boolean found = false;
                    for (int j = 0; !found && j < verticeTraffics.size(); ++j) {
                        SimulationNode position = verticeTraffics.get(j).getTrafficControll().getTransfor().getPosition();
                        String positionName = position.getName();

                        if (positionName.compareToIgnoreCase(nextNodeName) == 0) {
                            found = true;
                        }
                    }
                    if (!found)
                        set.add(pedestrian);
                }
            }
        }
        return set;
    }

    public List<Pedestrian> getPedestrianGoTo(SimulationNode nextNode, NodeTrafficControll.VerticeTraffic trafficControl) {
        List<Pedestrian> set = new ArrayList<>();
        int maxCapacity = trafficControl.getPedestrianPerUpdate();

        for (int i = 0; i < _list.size() && set.size() < maxCapacity; ++i) {
            Pedestrian pedestrian = _list.get(i);
            if (!pedestrian.isPathFinised()) {

                SimulationNode node = pedestrian.getNextNode();
                String nodeName = node.getName();
                if (nodeName.compareTo(nextNode.getName()) == 0) {
                    set.add(pedestrian);
                }
            }
        }
        return set;
    }

    public Pedestrian peek() {
        return _list.get(0);
    }

    public Pedestrian getFirstNotFinisehd() {
        Pedestrian p = null;
        for (int i = 0; p == null && i < _list.size(); ++i) {
            p = _list.get(i).isPathFinised() ? null : _list.get(i);
        }
        return p;
    }

    public Pedestrian peek(int i) {
        return _list.get(i);
    }

    public void remove(Pedestrian p) {
        _list.remove(p);
    }

    public int size() {
        return _list.size();
    }

}
