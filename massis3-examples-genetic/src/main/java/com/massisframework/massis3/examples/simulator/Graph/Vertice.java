package com.massisframework.massis3.examples.simulator.Graph;

import java.util.*;

public class Vertice<T> {
    private Map<String, VerticeNode<T>> _adjacents;

    public Vertice() {
        _adjacents = new LinkedHashMap<>();
    }

    public List<VerticeNode<T>> getNeighbors() {
        List<VerticeNode<T>> list = new ArrayList<>();
        Object[] objs = _adjacents.values().toArray();
        for (Object ob : objs) {
            list.add((VerticeNode<T>) ob);
        }
        return list;
    }

    public T getNeighbor(String destination) {
        VerticeNode<T> des = _adjacents.get(destination);
        return des.getVerticeNode();
    }

    public void addNeighbors(VerticeNode<T> node) {
        if (node == null)
            return;
        _adjacents.put(node.getNext(), node);
    }

    public void addNeighbors(String next, T vData) throws DuplicateNameException {
        if (next == null)
            return;
        if (!_adjacents.containsKey(next))
            _adjacents.put(next, new VerticeNode<T>(next, vData));
        else
            throw new DuplicateNameException("The key " + next + " already exist");

    }

    public void removeNeightbor(VerticeNode<T> node) throws NodeNotFoundException {
        if (node == null)
            return;
        if (_adjacents.containsKey(node.getNext()))
            _adjacents.remove(node.getNext());
        else
            throw new NodeNotFoundException("The key " + node.getNext() + " not found in the neightbor list");
    }


}
