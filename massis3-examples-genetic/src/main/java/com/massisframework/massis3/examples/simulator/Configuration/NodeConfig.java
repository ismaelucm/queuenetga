package com.massisframework.massis3.examples.simulator.Configuration;

public class NodeConfig {
    /*Simulamos los nodos como estancias, bien sean salas o pasillos. Todas tienen un tiempo para de trasnporte. Asumimos que el tiemp ode trasnporte es el mismo para todas las salidas*/
    private String nodeName;
    private int capacity;
    private boolean blockCapacity;


    public NodeConfig() {
        nodeName = null;
        capacity = 0;
        blockCapacity = false;
    }

    public NodeConfig(String nd, int cap) {
        nodeName = nd;
        capacity = cap;
        blockCapacity = false;
    }

    public boolean isBlockCapacity() {
        return blockCapacity;
    }

    public void setBlockCapacity(boolean blockCapacity) {
        this.blockCapacity = blockCapacity;
    }


    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int queueSize) {
        this.capacity = queueSize;
    }
}
