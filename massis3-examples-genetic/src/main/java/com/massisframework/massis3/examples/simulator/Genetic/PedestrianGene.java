package com.massisframework.massis3.examples.simulator.Genetic;

import com.massisframework.massis3.examples.genetic.Gene;
import com.massisframework.massis3.examples.simulator.Components.Pedestrian;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PedestrianGene implements Gene {

    public int[] _pathCodificated;

    private static Map<Integer, String> _decodificationMap = null;
    private static Map<String, Integer> _codificationMap = null;


    public PedestrianGene(int size) {
        _pathCodificated = new int[size];
    }

    public PedestrianGene(String path) {
        String[] splitResult = path.split(",");
        for (int i = 0; i < splitResult.length; ++i) {
            _pathCodificated[i] = PedestrianGene.codificate(splitResult[i]);
        }
    }


    public void set(int position, int v) {
        _pathCodificated[position] = v;
    }

    public String getPath() {
        String path = "";
        for (int i = 0; i < _pathCodificated.length; ++i) {
            if (i < (_pathCodificated.length - 1))
                path += PedestrianGene.decodificate(_pathCodificated[i]) + ",";
            else
                path += PedestrianGene.decodificate(_pathCodificated[i]);

        }
        return path;
    }

    public int get(int index) {
        return _pathCodificated[index];
    }

    public static void Init(List<String> nodeList) {
        _decodificationMap = new LinkedHashMap<>();
        _codificationMap = new LinkedHashMap<>();

        for (int i = 0; i < nodeList.size(); ++i) {
            String s = nodeList.get(i);
            _codificationMap.put(s, i);
            _decodificationMap.put(i, s);
        }
    }

    public static boolean isInit() {
        return _decodificationMap != null;
    }

    public static int codificate(String nodeName) {
        if (nodeName.compareTo(Pedestrian.WAIT) == 0)
            return Integer.MAX_VALUE;
        else if (isInit())
            return _codificationMap.get(nodeName);
        else
            return Integer.MIN_VALUE;
    }

    public static String decodificate(int code) {
        if (code == Integer.MAX_VALUE)
            return Pedestrian.WAIT;
        else if (isInit())
            return _decodificationMap.get(code);
        else
            return null;
    }


    public int size() {
        return this._pathCodificated.length;
    }

    @Override
    public void showGene() {
        String fin = setializate();
        System.out.println(fin);
    }

    @Override
    public String setializate() {
        String fin = "";
        for (int i = 0; i < _pathCodificated.length; ++i) {
            String s = PedestrianGene.decodificate(_pathCodificated[i]);
            if (i < (_pathCodificated.length - 1))
                fin += s + ",";
            else
                fin += s;
        }
        return fin;
    }
}
