package com.massisframework.massis3.examples.geneticexamples;

import com.massisframework.massis3.examples.Helpers.GeneticExampleHelper;
import com.massisframework.massis3.examples.simulator.Components.Pedestrian;
import com.massisframework.massis3.examples.simulator.Core.Simulation;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.PedestrianGA;
import com.massisframework.massis3.examples.utils.MainUtils;

public class GeneticBehaviorTest {

    public static final String queuNetConfig = "/SolutionQN_96_05p_25_04.txt";
    public static final String dafaulSimulationResult = "/massis3-examples-genetic/src/main/resources/Entrada_Clase.txt";

    public static void main(String[] args)
    {
        String currentPath = MainUtils.getCurrentPath();
        String dataFile = currentPath + dafaulSimulationResult;
        Simulation simulation = GeneticBehavior.createSimulationWithInitialValues(queuNetConfig,dataFile,false);


        String path1 = "MainGate,MainHall.Entrance,MainHall.Library,LobbyEventRoom.Hall,ClassesLobby.Class1,Class1";
        String path2 = "MainGate,MainHall.Entrance,MainHall.Library,LobbyEventRoom.Hall,LobbyEventRoom.EventRoom,LobbyEventRoom.Final";
        String path2Bis = "LobbyEventRoom.EventRoom,LobbyEventRoom.Hall,ClassesLobby.Class1,ClassesLobby.Class2,Class2,"+ Pedestrian.WAIT;

        //pointCross1 = 3
        //point cross2 = 1
        String sepectedChild1 = "LobbyEventRoom.EventRoom,LobbyEventRoom.Hall,ClassesLobby.Class1,Class1,NO_OP";
        String sepectedChild2 = "MainGate,MainHall.Entrance,MainHall.Library,LobbyEventRoom.Hall,ClassesLobby.Class1,ClassesLobby.Class2";
        GeneticExampleHelper.createPedestrianDecoders();

        PedestrianGA pedestrianGA1 = new PedestrianGA(0.5f,path1.split(","));
        PedestrianGA pedestrianGA2 = new PedestrianGA(0.5f,path2.split(","));

        System.out.println("Ind 1= "+pedestrianGA1.toString());
        System.out.println("Ind 2= "+pedestrianGA2.toString());
        PedestrianGA[] child = pedestrianGA1.cross(pedestrianGA2,simulation.getSimulator().getWorld());

        System.out.println("Ind 1 after cross "+child[0].toString());
        System.out.println("Ind 2 after cross "+child[1].toString());

        System.out.println("muation 0");
        child[0].mutate(simulation.getSimulator().getWorld());
        System.out.println("muation 1");
        child[1].mutate(simulation.getSimulator().getWorld());

        System.out.println("Ind 1 after mutate "+child[0].toString());
        System.out.println("Ind 2 after mutate "+child[1].toString());

        System.out.println("Ind 1 after mutate "+child[0].toStringPosition());
        System.out.println("Ind 2 after mutate "+child[1].toStringPosition());
        System.out.println("");
    }
}
