package com.massisframework.massis3.examples.simulator.Configuration;

import com.massisframework.massis3.Pair;
import com.massisframework.massis3.XMLHelpper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimulationDescription {
    private SimulationConfig simulationConfig;
    private SimulatorConfig simulatorConfig;

    private static final String SCENE = "Scene";
    private static final String SIMULATION_CONFIG = "SimulationConfig";
    private static final String WORLD_CONFIG = "WorldConfig";
    private static final String INSTANCIATION_CONFIG_LIST = "InstanciationConfigList";


    private static final String ARQUETYPE_CONFIG = "arquetypeConfig";
    private static final String SCENE_NAME = "sceneName";

    public SimulationDescription() {
        simulationConfig = new SimulationConfig();
        simulatorConfig = null;
    }

    /*
    <Scene arquetypeConfig="GraphSimulationConfigExample" sceneName="SeceneExample" >
        <SimulationConfig stepTime="1" numSteps="100" simulationTime="10"/>

        <!-- WorldConfig => Configure the graph that represent the world-->
        <WorldConfig>
            <NodeConfigList>
                <NodeConfig nodeName="Lobby" capacity="200" timeToCrossTheNode="0"/>
            </NodeConfigList>

            <VerticeConfigList>
                <VerticeConfig source="Lobby" destination="CorridorToRoomA" transportRate="2"/>
            </VerticeConfigList>
        </WorldConfig>

        <InstanciationConfigList>
            <InstanciationConfig numElements="25" arquetype="Pedestrian" place="Lobby">
                <ComponentConfigOverrideList>
                    <ComponentConfig component="Pedestrian">
                        <AttributeList>
                            <Attribute name="path" value="Lobby,CorridorToRoomA,CorridorToRoomB,RoomA" type=""/>
                        </AttributeList>
                    </ComponentConfig>
                </ComponentConfigOverrideList>
            </InstanciationConfig>

            <InstanciationConfig numElements="25" arquetype="Pedestrian" place="Lobby">
                <ComponentConfigOverrideList>
                    <ComponentConfig component="Pedestrian">
                        <AttributeList>
                            <Attribute name="path" value="Lobby,CorridorToRoomA,CorridorToRoomB,RoomB" type=""/>
                        </AttributeList>
                    </ComponentConfig>
                    <!-- N component Configs-->
                </ComponentConfigOverrideList>
            </InstanciationConfig>
        </InstanciationConfigList>
    </Scene>
     */



    public SimulationDescription(InputStream stream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(stream);

            NodeList sceneNodeList = document.getElementsByTagName(SCENE);
            Map<String, String> sceneAttrMap = XMLHelpper.getAttributes(sceneNodeList.item(0));
            String arquetypeSet = sceneAttrMap.get(ARQUETYPE_CONFIG);
            String sceneName = sceneAttrMap.get(SCENE_NAME);

            NodeList simulationConfigList = document.getElementsByTagName(SIMULATION_CONFIG);
            simulationConfig = new SimulationConfig(simulationConfigList.item(0));

            NodeList worldConfigList = document.getElementsByTagName(WORLD_CONFIG);
            NodeList instanciationConfigList = document.getElementsByTagName(INSTANCIATION_CONFIG_LIST);
            simulatorConfig = new SimulatorConfig(sceneName, arquetypeSet, worldConfigList.item(0), instanciationConfigList.item(0));

        } catch (Exception e) {
            System.out.println("Error in the Simulation DEscription deserialized " + e.getMessage());
        }
    }


    public SimulatorConfig getSimulatorConfig() {
        return simulatorConfig;
    }

    public void setSimulatorConfig(SimulatorConfig simulatorConfig) {
        this.simulatorConfig = simulatorConfig;
    }

    public SimulationConfig getSimulationConfig() {
        return simulationConfig;
    }

    public void setSimulationConfig(SimulationConfig simulationConfig) {
        this.simulationConfig = simulationConfig;
    }
}
