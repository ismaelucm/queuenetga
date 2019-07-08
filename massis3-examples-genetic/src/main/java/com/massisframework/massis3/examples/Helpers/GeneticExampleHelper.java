package com.massisframework.massis3.examples.Helpers;

import com.massisframework.massis3.examples.simulator.Components.Pedestrian;
import com.massisframework.massis3.examples.simulator.Genetic.Individuals.PedestrianGA;

import java.util.LinkedHashMap;
import java.util.Map;

public class GeneticExampleHelper {


    /*
            <NodeConfig nodeName="MainGate" capacity="10000" timeToCrossTheNode="2" blockCapacity="true"/>
            <NodeConfig nodeName="MainHall.Entrance" capacity="100" timeToCrossTheNode="6"/>
            <NodeConfig nodeName="MainHall.Library" capacity="100" timeToCrossTheNode="7"/>
            <NodeConfig nodeName="LobbyEventRoom.Hall" capacity="35" timeToCrossTheNode="7"/>
            <NodeConfig nodeName="LobbyEventRoom.EventRoom" capacity="35" timeToCrossTheNode="7"/>
            <NodeConfig nodeName="LobbyEventRoom.Final" capacity="35" timeToCrossTheNode="7"/>
            <NodeConfig nodeName="Library" capacity="390" timeToCrossTheNode="12"/>
            <NodeConfig nodeName="EventRoom" capacity="429" timeToCrossTheNode="12"/>
            <NodeConfig nodeName="LobbyCafeteria" capacity="50" timeToCrossTheNode="6"/>
            <NodeConfig nodeName="Council" capacity="20" timeToCrossTheNode="5"/>
            <NodeConfig nodeName="Council2" capacity="20" timeToCrossTheNode="5"/>
            <NodeConfig nodeName="ElevatorsLobby" capacity="40" timeToCrossTheNode="8"/>
            <NodeConfig nodeName="ClassesLobby.Class1" capacity="30" timeToCrossTheNode="8"/>
            <NodeConfig nodeName="ClassesLobby.Class2" capacity="30" timeToCrossTheNode="8"/>
            <NodeConfig nodeName="ClassesLobby.Class3" capacity="30" timeToCrossTheNode="8"/>
            <NodeConfig nodeName="ClassesLobby.Class4" capacity="30" timeToCrossTheNode="8"/>
            <NodeConfig nodeName="ClassesLobby.Class5" capacity="30" timeToCrossTheNode="8"/>
            <NodeConfig nodeName="CafeteriaHall" capacity="30" timeToCrossTheNode="3"/>
            <NodeConfig nodeName="ProfessorsCafeteria" capacity="120" timeToCrossTheNode="3"/>
            <NodeConfig nodeName="StudentsCafeteria" capacity="400" timeToCrossTheNode="22"/>
            <NodeConfig nodeName="HallBackGate.Cafeteria" capacity="50" timeToCrossTheNode="6"/>
            <NodeConfig nodeName="HallBackGate.Lobby" capacity="50" timeToCrossTheNode="6"/>
            <NodeConfig nodeName="BackGate" capacity="10000" timeToCrossTheNode="16" blockCapacity="true"/>
            <NodeConfig nodeName="Class1" capacity="200" timeToCrossTheNode="8"/>
            <NodeConfig nodeName="Class2" capacity="200" timeToCrossTheNode="8"/>
            <NodeConfig nodeName="Class3" capacity="200" timeToCrossTheNode="8"/>
            <NodeConfig nodeName="Class4" capacity="200" timeToCrossTheNode="8"/>
            <NodeConfig nodeName="Class5" capacity="200" timeToCrossTheNode="8"/>
     */
    public static void createPedestrianDecoders()
    {
        Map<String, Integer> encoder = new LinkedHashMap<>();
        encoder.put(Pedestrian.WAIT,-1);
        encoder.put("MainGate",0);
        encoder.put("MainHall.Entrance",1);
        encoder.put("MainHall.Library",2);
        encoder.put("LobbyEventRoom.Hall",3);
        encoder.put("LobbyEventRoom.EventRoom",4);
        encoder.put("LobbyEventRoom.Final",5);
        encoder.put("Library",6);
        encoder.put("EventRoom",7);
        encoder.put("LobbyCafeteria",8);
        encoder.put("Council",9);
        encoder.put("Council2",10);
        encoder.put("ElevatorsLobby",11);
        encoder.put("ClassesLobby.Class1",12);
        encoder.put("ClassesLobby.Class2",13);
        encoder.put("ClassesLobby.Class3",14);
        encoder.put("ClassesLobby.Class4",15);
        encoder.put("ClassesLobby.Class5",16);
        encoder.put("CafeteriaHall",17);
        encoder.put("ProfessorsCafeteria",18);
        encoder.put("StudentsCafeteria",19);
        encoder.put("HallBackGate.Cafeteria",20);
        encoder.put("HallBackGate.Lobby",21);
        encoder.put("BackGate",22);
        encoder.put("Class1",23);
        encoder.put("Class2",24);
        encoder.put("Class3",25);
        encoder.put("Class4",26);
        encoder.put("Class5",27);



        Map<Integer, String> decoder = new LinkedHashMap<>();
        decoder.put(-1,Pedestrian.WAIT);
        decoder.put(0,"MainGate");
        decoder.put(1,"MainHall.Entrance");
        decoder.put(2,"MainHall.Library");
        decoder.put(3,"LobbyEventRoom.Hall");
        decoder.put(4,"LobbyEventRoom.EventRoom");
        decoder.put(5,"LobbyEventRoom.Final");
        decoder.put(6,"Library");
        decoder.put(7,"EventRoom");
        decoder.put(8,"LobbyCafeteria");
        decoder.put(9,"Council");
        decoder.put(10,"Council2");
        decoder.put(11,"ElevatorsLobby");
        decoder.put(12,"ClassesLobby.Class1");
        decoder.put(13,"ClassesLobby.Class2");
        decoder.put(14,"ClassesLobby.Class3");
        decoder.put(15,"ClassesLobby.Class4");
        decoder.put(16,"ClassesLobby.Class5");
        decoder.put(17,"CafeteriaHall");
        decoder.put(18,"ProfessorsCafeteria");
        decoder.put(19,"StudentsCafeteria");
        decoder.put(20,"HallBackGate.Cafeteria");
        decoder.put(21,"HallBackGate.Lobby");
        decoder.put(22,"BackGate");
        decoder.put(23,"Class1");
        decoder.put(24,"Class2");
        decoder.put(25,"Class3");
        decoder.put(26,"Class4");
        decoder.put(27,"Class5");

        PedestrianGA.SetDecoder(decoder);
        PedestrianGA.SetEncoder(encoder);
    }
}
