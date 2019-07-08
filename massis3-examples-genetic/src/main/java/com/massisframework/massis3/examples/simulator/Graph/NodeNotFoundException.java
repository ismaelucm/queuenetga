package com.massisframework.massis3.examples.simulator.Graph;

import com.massisframework.massis3.examples.simulator.Graph.GraphException;

public class NodeNotFoundException extends GraphException {
    public NodeNotFoundException() {
        super();
    }

    public NodeNotFoundException(String msg) {
        super(msg);
    }
}
