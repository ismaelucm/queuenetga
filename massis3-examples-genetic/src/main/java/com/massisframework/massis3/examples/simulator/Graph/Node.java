package com.massisframework.massis3.examples.simulator.Graph;

public class Node<T> {
    private T _data;
    private String _name;
    private Graph<T, ?> _graph;


    public Node() {
        this(null, null, null);
    }

    public Node(String name) {
        this(name, null, null);
    }

    public Node(String name, T data, Graph<T, ?> graph) {
        _name = name;
        _data = data;
        _graph = graph;
    }

    public void setGraph(Graph<T, ?> graph) {
        _graph = graph;
    }

    public <Q> Graph<T, Q> getGraph() {
        return (Graph<T, Q>) _graph;
    }

    public T getData() {
        return _data;
    }

    public void setData(T data) {
        this._data = data;
    }

    public String getName() {
        return _name;
    }

    public void setName(String id) {
        this._name = id;
    }
}
