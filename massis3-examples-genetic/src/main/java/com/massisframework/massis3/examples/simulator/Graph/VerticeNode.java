package com.massisframework.massis3.examples.simulator.Graph;

public class VerticeNode<T> {
    private String _next;
    private T _verticeNode;

    public VerticeNode() {
        this(null, null);
    }

    public VerticeNode(String next) {
        this(next, null);
    }

    public VerticeNode(String next, T verticeNode) {
        _next = next;
        _verticeNode = verticeNode;
    }

    public String getNext() {
        return _next;
    }

    public void setNext(String next) {
        this._next = next;
    }

    public T getVerticeNode() {
        return _verticeNode;
    }

    public void setVerticeNode(T verticeNode) {
        this._verticeNode = verticeNode;
    }
}
