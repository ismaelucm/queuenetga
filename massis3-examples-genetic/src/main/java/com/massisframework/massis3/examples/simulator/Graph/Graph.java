package com.massisframework.massis3.examples.simulator.Graph;

import com.massisframework.massis3.examples.simulator.Entity.Entity;

import java.util.*;

public class Graph<T, Q> {
    Map<String, Node<T>> _nodes;
    Map<String, Vertice<Q>> _vertices;

    public Graph() {
        _nodes = new LinkedHashMap<>();
        _vertices = new LinkedHashMap<>();
    }


    public boolean existNode(String name) {
        return _nodes.containsKey(name);
    }

    public void addNode(Node<T> node) throws DuplicateNameException {
        if (!_nodes.containsKey(node.getName()))
            _nodes.put(node.getName(), node);
        else
            throw new DuplicateNameException(" The key " + node.getName() + " already exist");
    }

    public Node<T> addNode(String name, T data) throws DuplicateNameException {
        if (!_nodes.containsKey(name)) {
            Node<T> node = new Node<T>(name, data, this);
            _nodes.put(name, node);
            return node;
        } else
            throw new DuplicateNameException(" The key " + name + " already exist");
    }

    public void addVertice(Node<T> node, Vertice<Q> vertice) {
        _vertices.put(node.getName(), vertice);
    }

    public void addVertice(String source, String destination, Q vertice) throws NodeNotFoundException {
        if (_nodes.containsKey(source)) {
            Vertice<Q> v = _vertices.containsKey(source) ? _vertices.get(source) : new Vertice<>();
            v.addNeighbors(new VerticeNode<>(destination, vertice));
            _vertices.put(source, v);
        } else
            throw new NodeNotFoundException("The node " + source + " not found");
    }

    public List<String> getAllNodes() {
        List<String> list = new ArrayList<>();
        for (String s : _nodes.keySet()) {
            list.add(s);
        }
        return list;
    }

    public Node<T> getNode(String nodeName) {
        return _nodes.get(nodeName);
    }

    public Q getTransition(String nodeSource, String nodeDestination) {
        Vertice<Q> vertice = _vertices.get(nodeSource);
        Q ret = vertice.getNeighbor(nodeDestination);
        return ret;
    }

    public List<VerticeNode<Q>> getNeighbors(String nodeName) {

        Vertice<Q> v = _vertices.get(nodeName);
        List<VerticeNode<Q>> vArray = (v != null) ? v.getNeighbors() : new ArrayList<>();
        return vArray;
    }
}
