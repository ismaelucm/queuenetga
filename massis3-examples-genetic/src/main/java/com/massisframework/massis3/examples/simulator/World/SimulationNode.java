package com.massisframework.massis3.examples.simulator.World;

import com.massisframework.massis3.Pair;
import com.massisframework.massis3.examples.simulator.Components.Globals;
import com.massisframework.massis3.examples.simulator.Configuration.NodeConfig;
import com.massisframework.massis3.examples.simulator.Core.Component;
import com.massisframework.massis3.examples.simulator.Entity.Entity;
import com.massisframework.massis3.examples.simulator.Graph.Node;
import com.massisframework.massis3.examples.simulator.Graph.VerticeNode;


import java.util.*;

public class SimulationNode {

    public static int MIN_CAPACITY = 1;
    public static int MAX_CAPACITY = 700;


    private String _name;

    private int _capacity;
    private Map<Integer, Entity> _entitys;
    private Node<SimulationNode> _graphNode;
    private boolean _capacityBlocked;


    public boolean isCapacityBlocked() {
        return _capacityBlocked;
    }




    public void setCapacityBlocked(boolean b) {
        _capacityBlocked = b;
    }



    public SimulationNode(NodeConfig nodeConfig) {
        _entitys = new LinkedHashMap<>();
        _name = nodeConfig.getNodeName();
        _capacity = nodeConfig.getCapacity();
        _capacityBlocked = nodeConfig.isBlockCapacity();
    }

    public int getCapacity() {
        return _capacity;
    }

    public void setCapacity(int capacity) {
        _capacity = capacity;
    }

    public void setGraphNode(Node<SimulationNode> graphNode) {
        _graphNode = graphNode;
    }

    public String getName() {
        return _name;
    }

    public Node<SimulationNode> getGraphNode() {
        return _graphNode;
    }


    public List<Pair<SimulationVertice, SimulationNode>> getNeighbors() throws Exception {
        List<Pair<SimulationVertice, SimulationNode>> neighborsList = new ArrayList<>();
        List<VerticeNode<SimulationVertice>> neighbors = _graphNode.<SimulationVertice>getGraph().getNeighbors(_graphNode.getName());
        for (VerticeNode<SimulationVertice> v : neighbors) {
            try {
                SimulationVertice sVer = v.getVerticeNode();
                String next = v.getNext();
                Node<SimulationNode> n = _graphNode.<SimulationVertice>getGraph().getNode(next);
                SimulationNode sn = n.getData();
                Pair<SimulationVertice, SimulationNode> pair = new Pair(sVer, sn);
                neighborsList.add(pair);
            } catch (Exception e) {
                System.out.println("next " + v.getNext() + " " + this.getName());
                e.printStackTrace();
                throw new Exception("");
            }
        }

        return neighborsList;
    }

    public void add(Entity e) {
        _entitys.put(e.ID(), e);
    }

    public void remove(Entity e) {
        _entitys.remove(e.ID());
    }

    public List<Entity> getEntitys() {
        List<Entity> list = new ArrayList<>();
        Object[] entities = _entitys.values().toArray();
        for (Object o : entities) {
            list.add((Entity) o);
        }
        return list;
    }

    public List<Entity> getEntitysByComponent(Class<? extends Component> clazz) {
        List<Entity> list = new ArrayList<>();
        List<Entity> entities = getEntitys();
        for (int i = 0; i < entities.size(); ++i) {
            if (entities.get(i).hasComponent(clazz))
                list.add(entities.get(i));
        }

        return list;
    }

    public <T extends Component> List<T> getComponentsInNode(Class<T> clazz) {
        List<T> list = new ArrayList<>();
        List<Entity> entities = getEntitys();
        for (int i = 0; i < entities.size(); ++i) {
            if (entities.get(i).hasComponent(clazz))
                list.add(entities.get(i).getComponent(clazz));
        }

        return list;
    }

    public List<Entity> getEntitysByTag(Globals.TAGS tag) {
        List<Entity> list = new ArrayList<>();
        List<Entity> entities = getEntitys();
        for (int i = 0; i < entities.size(); ++i) {
            if (entities.get(i).getTag().equals(tag))
                list.add(entities.get(i));
        }

        return list;
    }
}
