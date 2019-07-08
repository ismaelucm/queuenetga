package com.massisframework.massis3.examples.simulator.Genetic.Individuals;

import com.massisframework.massis3.Dice;
import com.massisframework.massis3.Pair;
import com.massisframework.massis3.examples.simulator.Components.HelpperClasses.SimulationResult;
import com.massisframework.massis3.examples.simulator.Components.Pedestrian;
import com.massisframework.massis3.examples.simulator.Core.Simulation;
import com.massisframework.massis3.examples.simulator.World.SimulationNode;
import com.massisframework.massis3.examples.simulator.World.SimulationVertice;
import com.massisframework.massis3.examples.simulator.World.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PedestrianGA {

    private static Map<Integer, String> _decodificationMap = null;
    private static Map<String, Integer> _codificationMap = null;


    protected int[] _pathCodificated;
    protected float _mutationProbability;

    public PedestrianGA(float mutProb)
    {
        _pathCodificated = null;
        _mutationProbability = mutProb;
    }

    public static void SetDecoder(Map<Integer, String> decoder)
    {
        _decodificationMap = decoder;
    }

    public static void SetEncoder(Map<String, Integer> encoder)
    {
        _codificationMap = encoder;
    }

    public PedestrianGA(float mutProb,String[] path)
    {
        this(mutProb,path.length);
        for(int i = 0; i < path.length; i++)
        {
            String p = path[i];
            Integer val = _codificationMap.get(p);
            _pathCodificated[i]=val;
        }
    }

    public int[] getPathEncode()
    {
        return _pathCodificated;
    }

    public List<String> getPath()
    {
        List<String> list = new ArrayList<>();
        for(int i = 0; i < _pathCodificated.length; ++i)
        {
            String point = new String(_decodificationMap.get(_pathCodificated[i]));
            list.add(point);
        }
        return list;
    }

    public PedestrianGA(float mutProb,int size)
    {
        _pathCodificated = new int[size];
        _mutationProbability = mutProb;
    }

    public void randomInit(World world)
    {
        //_pathCodificated = createValidRandomPath(_pathCodificated.length,world);
    }

    public PedestrianGA clone()
    {
        PedestrianGA gen = new PedestrianGA(_mutationProbability,_pathCodificated.length);

        for(int i = 0; i < gen._pathCodificated.length; ++i)
        {
            gen._pathCodificated[i] = _pathCodificated[i];
        }

        return gen;
    }

    public int getSize()
    {
        return _pathCodificated.length;
    }

    public String[] decode()
    {

        String[] strArr = new String[_pathCodificated.length];
        for(int i = 0; i < _pathCodificated.length; ++i)
        {
            strArr[i] = _decodificationMap.get(_pathCodificated[i]);
        }

        return strArr;
    }

    public PedestrianGA[] cross(PedestrianGA ind2, World world)
    {
        PedestrianGA[] childs = new PedestrianGA[2];
        //TODO: cruce....
        //Seleccionamos del individuo 2 un camino que esté en el individuo 1
        int[] pointsToCross = selectCrossPoint(ind2);
        if(pointsToCross != null)
        {
            PedestrianGA child2 = new PedestrianGA(this._mutationProbability,this.getSize());
            //copio la primera parte del padre 1 al hijo 2
            for(int i = 0; i <= pointsToCross[0]; ++i)
            {
                child2._pathCodificated[i] = _pathCodificated[i];
            }

            //copio la primera parte del padre 2 al hijo 1
            PedestrianGA child1 = new PedestrianGA(this._mutationProbability,this.getSize());
            for(int i = 0; i <= pointsToCross[1]; ++i)
            {
                child1._pathCodificated[i] = ind2._pathCodificated[i];
            }

            int child1Index = pointsToCross[1]+1;
            //copio la parte 2 del padre 1 al hijo1
            for(int i = pointsToCross[0]+1; i < _pathCodificated.length && child1Index < child1.getSize(); ++i)
            {
                child1._pathCodificated[child1Index] = _pathCodificated[i];
                child1Index++;
            }

            int child2Index = pointsToCross[0]+1;
            //copio la parte 2 del padre 1 al hijo1
            for(int i = pointsToCross[1]+1; i < ind2._pathCodificated.length && child2Index < child2.getSize(); ++i)
            {
                child2._pathCodificated[child2Index] = ind2._pathCodificated[i];
                child2Index++;
            }

            if(child2Index < child2.getSize())
            {
                String currentName = PedestrianGA._decodificationMap.get(child2._pathCodificated[child2Index-1]);
                if(currentName.compareTo(Pedestrian.WAIT) != 0) {
                    String beforeName = PedestrianGA._decodificationMap.get(child2._pathCodificated[child2Index - 2]);
                    SimulationNode currentNode = world.getNode(currentName);
                    //child2._pathCodificated = createNewPath(child2._pathCodificated, child2Index, child2.getSize(), currentNode, beforeName);
                }
                else
                {
                    while(child2Index < child2.getSize())
                    {
                        child2._pathCodificated[child2Index] = _codificationMap.get(Pedestrian.WAIT);
                        child2Index++;
                    }
                }
            }

            if(child1Index < child1.getSize()) {
                String currentName = PedestrianGA._decodificationMap.get(child1._pathCodificated[child1Index - 1]);
                if (currentName.compareTo(Pedestrian.WAIT) != 0) {
                    String beforeName = PedestrianGA._decodificationMap.get(child1._pathCodificated[child1Index - 2]);
                    SimulationNode currentNode = world.getNode(currentName);
                    //child1._pathCodificated = createNewPath(child1._pathCodificated, child1Index - 1, child1.getSize(), currentNode, beforeName);
                }
                else
                {
                    while(child1Index < child1.getSize())
                    {
                        child1._pathCodificated[child1Index] = _codificationMap.get(Pedestrian.WAIT);
                        child1Index++;
                    }
                }
            }

            childs[0] = child1;
            childs[1] = child2;
        }
        else
        {
            childs[0] = this.clone();
            childs[1] = ind2.clone();
        }

        return childs;
    }

    protected int[] selectCrossPoint(PedestrianGA ind2)
    {
        //Seleccionamos del individuo 2 un camino que esté en el individuo 1
        int index1Found = -1;
        int index2Found = -1;
        boolean found = false;
        for(int i = 1; !found && i < _pathCodificated.length-1; ++i)
        {
            int candidate1 = _pathCodificated[i];
            for(int j = 1; !found && j < ind2._pathCodificated.length-1; j++)
            {
                int candidate2 = ind2._pathCodificated[j];
                if(candidate1 == candidate2)
                {
                    index1Found = i;
                    index2Found = j;
                    found = true;
                }
            }
        }
        if(found)
        {
            int[] output = new int[2];
            output[0]=index1Found;
            output[1]=index2Found;
            return output;
        }
        return null;
    }

    public void mutate(World world)
    {
        for(int i = 0; i < _pathCodificated.length; ++i) {
            float prob = Dice.GetRange(0f, 1f);

            if (prob < _mutationProbability) {


                if(i == 0)
                {
                    int firsRandom = Dice.GetRange(0,numberOfDomineElements());
                    //_pathCodificated = createValidRandomPath(_pathCodificated.length,world);
                }
                else{
                    String position = _decodificationMap.get(_pathCodificated[i]);
                    if(position.compareTo(Pedestrian.WAIT) != 0) {
                        SimulationNode currentNode = world.getNode(position);
                        String before = _decodificationMap.get(_pathCodificated[i - 1]);
                        //_pathCodificated = createNewPath(_pathCodificated, i, _pathCodificated.length, currentNode, before);
                    }
                }
            }
        }
    }

    public static int[] createValidRandomPath(int size,World world, Set<String> validPositions)
    {
        int[] validPath = new int[size];
        List<String> allNodes = world.getAllNodes();
        List<String> validNodes =  filterByValidNodes(allNodes,validPositions);
        int numNodes = validNodes.size();
        int firstNode = Dice.GetRange(0,numNodes);
        String firsNodeStr = validNodes.get(firstNode);
        validPath[0] = _codificationMap.get(firsNodeStr);
        SimulationNode simNode = world.getNode(firsNodeStr);
        int num = 0;
        boolean finishPath = false;
        SimulationNode currentNode = simNode;
        String beforeNode = null;
        return createNewPath(validPath,0,size,simNode,null, validPositions);

        //return validPath;
    }

    public static  List<String> filterByValidNodes( List<String> allNodes, Set<String> vaidPositions)
    {
        List<String> filter = new ArrayList<>();
        //Filtramos aquellos nodos por los que hemos pasado. No tiene sentido generar individuos que pasen por nodos
        //que en la simulación no han sido transitados porque en ningun generaran individuos validos.
        for(int i = 0; i < filter.size(); ++i)
        {
            String name = filter.get(i);
            if(vaidPositions.contains(name))
            {
                filter.add(name);
            }
        }
        return filter;
    }

   /* public static   List<Pair<SimulationVertice, SimulationNode>>  filterByValidNodes( List<Pair<SimulationVertice, SimulationNode>> neighbors, Set<String> vaidPositions)
    {
        return null;
    }*/

    public static int[] createNewPath(int[] validPath,int num, int size,SimulationNode currentNode,String beforeNode, Set<String> validPositions)
    {
        boolean finishPath = false;
        while(num < size && ! finishPath) {
            //Al menos debe haber dos. Un nodo con 0 vecinos está aislado. Con 1 vecino es vecino del q
            List<Pair<SimulationVertice, SimulationNode>> neighbors = getNeighbors(currentNode, beforeNode);
            validPath[num] = _codificationMap.get(currentNode.getName());
            num++;
            //neighbors = filterByValidNodes(neighbors,validPositions);
            if (neighbors.size() > 0) {
                int nextPath = Dice.GetRange(0, neighbors.size());
                Pair<SimulationVertice, SimulationNode> pairNext = neighbors.get(nextPath);
                beforeNode = currentNode.getName();
                currentNode = pairNext.get_second();
            }
            else
            {
                finishPath = true;//completamos el path
                for(int i = num; i < size; ++i)
                {
                    validPath[i] = -1; //fin del path.
                }
            }
        }

        return validPath;
    }


    public static List<Pair<SimulationVertice, SimulationNode>> getNeighbors(SimulationNode simNode, String beforeNode)
    {
        List<Pair<SimulationVertice, SimulationNode>> finalPairList = new ArrayList<>();
        try {
            List<Pair<SimulationVertice, SimulationNode>> neighbors = simNode.getNeighbors();
            if(neighbors != null)
            {
                for(int i = 0; i < neighbors.size(); ++i)
                {
                    Pair<SimulationVertice, SimulationNode> pair = neighbors.get(i);
                    if(beforeNode == null || beforeNode.compareTo("")== 0 || pair.get_second().getName().compareTo(beforeNode) != 0 )
                    {
                        finalPairList.add(pair);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return finalPairList;
    }

    @Override
    public String toString()
    {
        String s = "";

        for(int i = 0; i < _pathCodificated.length; ++i)
        {
            s += _pathCodificated[i];
            if(i < (_pathCodificated.length-1))
            {
                s += ",";
            }
        }

        return s;
    }

    public String toStringPosition()
    {
        String s = "";

        for(int i = 0; i < _pathCodificated.length; ++i)
        {
            s += _decodificationMap.get(_pathCodificated[i]);
            if(i < (_pathCodificated.length-1))
            {
                s += ",";
            }
        }

        return s;
    }


    public static int numberOfDomineElements()
    {
        return _codificationMap.size();
    }
}
