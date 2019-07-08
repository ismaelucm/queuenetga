package com.massisframework.massis3.examples.simulator.World;

import com.massisframework.massis3.Dice;
import com.massisframework.massis3.Pair;
import com.massisframework.massis3.examples.simulator.Configuration.NodeConfig;
import com.massisframework.massis3.examples.simulator.Configuration.VerticeConfig;
import com.massisframework.massis3.examples.simulator.Configuration.WorldConfig;
import com.massisframework.massis3.examples.simulator.Graph.*;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class World {
    private Graph<SimulationNode, SimulationVertice> _graph;
    private boolean _directed;

    public World() {
        _graph = new Graph<>();
    }

    public World(WorldConfig wConfig) {
        this();
        _directed = wConfig.isDirected();
        List<NodeConfig> nodeconfigList = wConfig.getNodeConfigs();
        List<VerticeConfig> verticesconfigList = wConfig.getVerticeConfigs();
        try {
            for (NodeConfig nodeconfig : nodeconfigList) {
                SimulationNode simNode = new SimulationNode(nodeconfig);
                Node<SimulationNode> graphNode = _graph.addNode(nodeconfig.getNodeName(), simNode);
                simNode.setGraphNode(graphNode);
            }

            for (VerticeConfig verticesconfig : verticesconfigList) {
                SimulationVertice simVertice = new SimulationVertice(verticesconfig.getTransportCapacity());
                _graph.addVertice(verticesconfig.getFirstNode(), verticesconfig.getSecondNode(), simVertice);
            }
        } catch (DuplicateNameException dne) {
            System.out.println(dne.getMessage());
        } catch (NodeNotFoundException nnfe) {
            System.out.println(nnfe.getMessage());
        }
    }


    public void configure(int[] arrayCapacity, int[] arrayTime, int[] arrayTransition, String[] orderNames, List<Pair<String, String>> transitionOrder) {
        for (int i = 0; i < orderNames.length; ++i) {
            String nodeName = orderNames[i];
            Node<SimulationNode> node = _graph.getNode(nodeName);
            SimulationNode simNode = node.getData();
            simNode.setCapacity(arrayCapacity[i]);
        }

        for (int i = 0; i < transitionOrder.size(); ++i) {
            Pair<String, String> transitionPair = transitionOrder.get(i);
            String nodeSource = transitionPair.get_first();
            String destination = transitionPair.get_second();
            try {
                SimulationVertice simVertice = _graph.getTransition(nodeSource, destination);
                simVertice.set_transportRate(arrayTransition[i]);
                simVertice.set_timeToCrossEdge(arrayTime[i]);
            }
            catch(Exception e)
            {
                throw new RuntimeException("nodeSource "+nodeSource+" destination "+destination);
            }
            SimulationVertice simVerticeInverseDir = _graph.getTransition(destination, nodeSource);
            simVerticeInverseDir.set_transportRate(arrayTransition[i]);
        }
    }

    public void configureRandom() {
        for (String nodeName : _graph.getAllNodes()) {
            Node<SimulationNode> node = _graph.getNode(nodeName);
            SimulationNode simNode = node.getData();

            if (!simNode.isCapacityBlocked()) {
                simNode.setCapacity(Dice.GetRange(SimulationNode.MIN_CAPACITY, SimulationNode.MAX_CAPACITY));
            }


            try {
                List<Pair<SimulationVertice, SimulationNode>> neighbors = simNode.getNeighbors();
                for (Pair<SimulationVertice, SimulationNode> pair : neighbors) {
                    SimulationVertice edge = pair.get_first();
                    if (!edge.isTransporRateBlocked()) {
                        edge.set_transportRate(Dice.GetRange(SimulationVertice.MIN_TRANSPORT_RATE, SimulationVertice.MAX_TRANSPORT_RATE));
                    }

                    if (!edge.isTimeToCrossEdgeBlocked()) {
                        edge.set_timeToCrossEdge(Dice.GetRange(SimulationVertice.MIN_TIME_TO_CROSS, SimulationVertice.MIN_TIME_TO_CROSS));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void configureNoise(float probSize, float probTime, float probTran) {
        for (String nodeName : _graph.getAllNodes()) {
            Node<SimulationNode> node = _graph.getNode(nodeName);
            SimulationNode simNode = node.getData();

            float randomSize = (float) Math.random();
            if (!simNode.isCapacityBlocked()) {
                if (randomSize < probSize) {
                    simNode.setCapacity(Dice.GetRange(SimulationNode.MIN_CAPACITY, SimulationNode.MAX_CAPACITY));
                }
            }



            try {
                List<Pair<SimulationVertice, SimulationNode>> neighbors = simNode.getNeighbors();
                for (Pair<SimulationVertice, SimulationNode> pair : neighbors) {
                    SimulationVertice edge = pair.get_first();
                    float randomTran = (float) Math.random();
                    if (!edge.isTransporRateBlocked()) {
                        if (randomTran < probTran) {
                            edge.set_transportRate(Dice.GetRange(SimulationVertice.MIN_TRANSPORT_RATE, SimulationVertice.MAX_TRANSPORT_RATE));
                        }
                    }

                    float randomTime = (float) Math.random();
                    if (!edge.isTimeToCrossEdgeBlocked()) {
                        if (randomTime < probTime) {
                            edge.set_timeToCrossEdge(Dice.GetRange(SimulationVertice.MIN_TIME_TO_CROSS, SimulationVertice.MIN_TIME_TO_CROSS));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public boolean isDirected() {
        return _directed;
    }

    public boolean exist(String name) {
        return _graph.existNode(name);
    }

    public SimulationNode getNode(String name) {
        return _graph.getNode(name).getData();
    }

    public List<SimulationNode> getNeighborn(String name) {
        List<SimulationNode> list = new ArrayList<>();
        List<VerticeNode<SimulationVertice>> neigh = _graph.getNeighbors(name);
        for (VerticeNode<SimulationVertice> v : neigh) {
            SimulationNode n = _graph.getNode(v.getNext()).getData();
            list.add(n);
        }
        return list;
    }

    public List<SimulationNode> getAllSimulationNode()
    {
        List<SimulationNode> simNodes = new ArrayList<>();
        List<String> allNodes = getAllNodes();
        for(int i = 0; i < allNodes.size(); ++i)
        {
            SimulationNode simNode = getNode(allNodes.get(i));
            simNodes.add(simNode);
        }
        return simNodes;
    }

    public List<String> getAllNodes() {
        return _graph.getAllNodes();
    }

    public List<SimulationVertice> getAllTansitionOnlyOneDirection()
    {
        List<SimulationVertice> simulationVerticeList = new ArrayList<>();
        List<String> allNodes = getAllNodes();
        Set<String> set = new HashSet<>();
        for (String nodeName : allNodes) {
            SimulationNode simNode = getNode(nodeName);
            try {
                List<Pair<SimulationVertice, SimulationNode>> neighbors = simNode.getNeighbors();
                for (Pair<SimulationVertice, SimulationNode> pair : neighbors) {

                    if(!set.contains(nodeName+" "+pair.get_second().getName())) {
                        SimulationVertice edge = pair.get_first();
                        simulationVerticeList.add(edge);
                        set.add(nodeName+" "+pair.get_second().getName());
                        set.add(pair.get_second().getName()+" "+nodeName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return simulationVerticeList;
    }


    /*
    if(!set.contains(name+" "+simNode.getName())) {
                    pairList.add(new Pair<String, String>(name, simNode.getName()));
                    set.add(name+" "+simNode.getName());
                    set.add(simNode.getName()+" "+name);
                }
     */

    public List<SimulationVertice> getAllSimulationTransitions() {
        List<SimulationVertice> simulationVerticeList = new ArrayList<>();
        List<String> allNodes = getAllNodes();
        for (String nodeName : allNodes) {
            SimulationNode simNode = getNode(nodeName);
            try {
                List<Pair<SimulationVertice, SimulationNode>> neighbors = simNode.getNeighbors();
                for (Pair<SimulationVertice, SimulationNode> pair : neighbors) {
                    SimulationVertice edge = pair.get_first();
                    simulationVerticeList.add(edge);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return simulationVerticeList;
    }


    public void simulate(String firstNode) {
        SimulationNode node = _graph.getNode(firstNode).getData();

    }


}
