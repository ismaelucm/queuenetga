package com.massisframework.massis3.examples.simulator.Components;


import com.massisframework.massis3.Pair;
import com.massisframework.massis3.examples.simulator.Core.*;
import com.massisframework.massis3.examples.simulator.World.SimulationNode;
import com.massisframework.massis3.examples.simulator.World.SimulationVertice;


import java.util.*;


public class NodeTrafficControll extends Component {
    private static final boolean debug = true;

    public class VerticeTraffic {
        private int pedestrianPerUpdate;
        private int numPedestrianMoved;
        private int timeToCrossEdge;
        private NodeTrafficControll trafficControll;


        public int getTimeToCrossEdge() {
            return timeToCrossEdge;
        }

        public void setTimeToCrossEdge(int timeToCrossEdge) {
            this.timeToCrossEdge = timeToCrossEdge;
        }

        public VerticeTraffic(int ppu, int numPed, int time, NodeTrafficControll tc) {
            pedestrianPerUpdate = ppu;
            numPedestrianMoved = numPed;
            timeToCrossEdge = time;
            trafficControll = tc;
        }


        public void move() {
            numPedestrianMoved++;
        }

        public NodeTrafficControll getTrafficControll() {
            return trafficControll;
        }

        public void clear() {
            numPedestrianMoved = 0;
        }

        public int getPedestrianPerUpdate() {
            return pedestrianPerUpdate;
        }


        public void setPedestrianPerUpdate(int f) {
            pedestrianPerUpdate = f;
        }

        public int getNumPedestrianMoved() {
            return numPedestrianMoved;
        }

        public void setNumPedestrianMoved(int i) {
            numPedestrianMoved = i;
        }
    }

    private SimulationQueue _queue;
    private int _queueSize;
    private SimulationNode _myNode;

    private List<VerticeTraffic> pedestrianMove;


    @Override
    public void init() {
        _queue = new SimulationQueue();
        getEntity().setTag(Globals.TAGS.TRAFFIC_CONTROLL);
    }

    @Override
    protected void onStart() throws Exception {
        _queueSize = this.getTransfor().getPosition().getCapacity();
        _myNode = this.getTransfor().getPosition();
        List<Pedestrian> pedestrians = _myNode.getComponentsInNode(Pedestrian.class);
        for (int i = 0; i < pedestrians.size(); ++i) {
            enqueuePedestrian(pedestrians.get(i));
        }
        CreatePedestrianMoving(_myNode);
    }

    @Override
    protected void onUpdate(int time) throws Exception {

        ClearPedestrianMove();
        int numMoved = 0;
        //if(getTransfor().getPosition().getName().compareTo("PasilloAscensores") == 0)
        //System.out.println("PasilloAscensores doe traffico controll in "+_queue.size());
        /*List<Pedestrian> pedestrianwithIncorrectPath = _queue.getPedestrianWithIncorrectPath(pedestrianMove);
        if (pedestrianwithIncorrectPath.size() > 0) {
            String msg = "Error in " + getTransfor().getPosition().getName() + ", there are " + pedestrianwithIncorrectPath.size() + " pedestrian that have a incorrect path ";
            System.out.println(msg);
            if (debug) {
                Pedestrian pedestrian = pedestrianwithIncorrectPath.get(0);
                List<String> pathList = pedestrian.getPath();
                String debugPath = "";
                for (int i = 0; i < pathList.size(); ++i) {
                    if (i < (pathList.size() - 1))
                        debugPath += pedestrianwithIncorrectPath.get(0).getNextNode().getName() + ",";
                    else
                        debugPath += pedestrianwithIncorrectPath.get(0).getNextNode().getName();
                }
                throw new Exception(msg + debugPath);
            }
        }*/
        if (_queue.size() > 0) {
            for (VerticeTraffic vTrafic : pedestrianMove) {
                SimulationNode simNode = vTrafic.getTrafficControll().getTransfor().getPosition();
                List<Pedestrian> pedestrianToMove = _queue.getPedestrianGoTo(simNode, vTrafic);
                for (int i = 0; i < pedestrianToMove.size(); ++i) {
                    Pedestrian pedestrian = pedestrianToMove.get(i);
                    if (pedestrian.canMove(vTrafic.getTimeToCrossEdge())) {
                        if (TrayToMovePedestrian(pedestrian, vTrafic)) {
                            vTrafic.move();
                            numMoved++;
                        }
                    }
                }
            }
        }


        //if(numMoved > 0)
        // DebugMovements();
    }

    private int CalcuNumPedestrianPAthFinished() {
        int numPathFinished = 0;
        for (int i = 0; i < _queue.size(); ++i) {
            numPathFinished += _queue.peek(i).isPathFinised() ? 1 : 0;
        }
        return numPathFinished;
    }

    private void CreatePedestrianMoving(SimulationNode node) throws Exception {
        pedestrianMove = new ArrayList<>();
        List<Pair<SimulationVertice, SimulationNode>> neighborList = node.getNeighbors();


        for (int i = 0; i < neighborList.size(); ++i) {
            Pair<SimulationVertice, SimulationNode> pair = neighborList.get(i);
            SimulationNode snNext = pair.get_second();
            List<NodeTrafficControll> nextNTCList = snNext.getComponentsInNode(NodeTrafficControll.class);
            if(nextNTCList.size() > 0)
                pedestrianMove.add(new VerticeTraffic(pair.get_first().get_transportRate(), 0, pair.get_first().get_timeToCrossEdge(), nextNTCList.get(0)));
            else
                System.out.println("Node traffic control in "+snNext.getName()+ " is not present");
        }
    }


    private void ClearPedestrianMove() {
        for (VerticeTraffic vTrafic : pedestrianMove) {
            vTrafic.clear();
        }
    }

    private void DebugMovements() {
        SimulationNode position = this.getTransfor().getPosition();
        for (VerticeTraffic vTrafic : pedestrianMove) {
            if (vTrafic.numPedestrianMoved > 0)
                System.out.println("Moved " + vTrafic.numPedestrianMoved + " from " + position.getName() + " to " + vTrafic.getTrafficControll().getTransfor().getPosition().getName());
        }
    }

    private boolean TrayToMovePedestrian(Pedestrian pedestrian, VerticeTraffic vertice) {
        boolean moved = false;

        if (vertice.getTrafficControll().enqueuePedestrian(pedestrian)) {
            pedestrian.move();
            _queue.remove(pedestrian);
            moved = true;
        }

        return moved;
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


    protected boolean enqueuePedestrian(Pedestrian e) {
        boolean enqueued = false;
        if (_queue.size() < _queueSize) {
            _queue.enqueue(e);
            enqueued = true;
        }
        return enqueued;
    }


}
