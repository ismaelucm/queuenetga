package com.massisframework.massis3.examples.simulator.Configuration;

import com.massisframework.massis3.XMLHelpper;
import org.w3c.dom.Node;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorldConfig {
    List<NodeConfig> nodeConfigs;
    List<VerticeConfig> verticeConfigs;
    private boolean directed;

    private static final String WORLD_CONFIG = "WorldConfig";
    private static final String NODE_CONFIG_LIST = "NodeConfigList";
    private static final String NODE_CONFIG = "NodeConfig";
    private static final String VERTICE_CONFIG_LIST = "VerticeConfigList";
    private static final String VERTICE_CONFIG = "VerticeConfig";
    private static final String NODE_NAME = "nodeName";
    private static final String CAPACITY = "capacity";
    private static final String TIME_CROSS_EDGE = "timeToCrossTheEdge";
    private static final String BLOCK_CAPACITY = "blockCapacity";
    private static final String BLOCK_TIME_TO_CROSS = "blockTimeToCross";


    private static final String SOURCE = "source";
    private static final String DESTINATION = "destination";
    private static final String TRANSPORT_RATE = "transportRate";
    private static final String DIRECTED = "directed";

    /*
    <!-- WorldConfig => Configure the graph that represent the world-->
        <WorldConfig>
            <NodeConfigList>
                <NodeConfig nodeName="Lobby" capacity="200" timeToCrossTheNode="0"/>
            </NodeConfigList>

            <VerticeConfigList>
                <VerticeConfig source="Lobby" destination="CorridorToRoomA" transportRate="2"/>
            </VerticeConfigList>
        </WorldConfig>
     */

    public WorldConfig() {
        nodeConfigs = new ArrayList<>();
        verticeConfigs = new ArrayList<>();
        directed = true;
    }

    public WorldConfig(Node worldConfig) {
        this();
        Map<String, String> worldConfigAttrMap = XMLHelpper.getAttributes(worldConfig);
        if (worldConfigAttrMap.containsKey(DIRECTED))
            directed = Boolean.parseBoolean(worldConfigAttrMap.get(DIRECTED));
        List<Node> nodeConfigList = XMLHelpper.getChildsByName(worldConfig, NODE_CONFIG_LIST);
        Node nodeConfigListNode = nodeConfigList.get(0);
        List<Node> verticeConfigList = XMLHelpper.getChildsByName(worldConfig, VERTICE_CONFIG_LIST);
        Node verticeConfigListNode = verticeConfigList.get(0);


        List<Node> nodeConfigs = XMLHelpper.getChildsByName(nodeConfigListNode, NODE_CONFIG);

        for (int i = 0; i < nodeConfigs.size(); ++i) {
            Node nodeConfig = nodeConfigs.get(i);
            Map<String, String> nodeConfigAttrMap = XMLHelpper.getAttributes(nodeConfig);
            String nodeName = nodeConfigAttrMap.get(NODE_NAME);
            int capacity = Integer.parseInt(nodeConfigAttrMap.get(CAPACITY));

            NodeConfig nodeConfigData = new NodeConfig(nodeName, capacity);

            String blockCapacityStr = nodeConfigAttrMap.get(BLOCK_CAPACITY);
            if(blockCapacityStr != null)
            {
                if(blockCapacityStr.compareToIgnoreCase("true")==0)
                    nodeConfigData.setBlockCapacity(true);
            }

            this.nodeConfigs.add(nodeConfigData);
        }

        List<Node> verticeConfigs = XMLHelpper.getChildsByName(verticeConfigListNode, VERTICE_CONFIG);

        for (int i = 0; i < verticeConfigs.size(); ++i) {

            Node verticeConfig = verticeConfigs.get(i);
            Map<String, String> verticeConfigAttrMap = XMLHelpper.getAttributes(verticeConfig);
            String source = verticeConfigAttrMap.get(SOURCE);
            String destination = verticeConfigAttrMap.get(DESTINATION);
            int transportRate = Integer.parseInt(verticeConfigAttrMap.get(TRANSPORT_RATE));
            int timeCrossEdge = Integer.parseInt(verticeConfigAttrMap.get(TIME_CROSS_EDGE));

            boolean blockTimeToCross = false;
            //BLOCK_TIME_TO_CROSS
            String blockTimeToCrossStr = verticeConfigAttrMap.get(BLOCK_TIME_TO_CROSS);
            if(blockTimeToCrossStr != null)
            {
                if(blockTimeToCrossStr.compareToIgnoreCase("true")==0)
                    blockTimeToCross = true;
            }

            VerticeConfig verticeConfigData = new VerticeConfig(source, destination, transportRate,timeCrossEdge,blockTimeToCross);
            this.verticeConfigs.add(verticeConfigData);
            if (!directed) {
                VerticeConfig verticeConfigDataAnotherDirection = new VerticeConfig(destination, source, transportRate,timeCrossEdge,blockTimeToCross);
                this.verticeConfigs.add(verticeConfigDataAnotherDirection);
            }


        }


    }

    public List<NodeConfig> getNodeConfigs() {
        return nodeConfigs;
    }

    public void setNodeConfigs(List<NodeConfig> nodeConfigs) {
        this.nodeConfigs = nodeConfigs;
    }

    public List<VerticeConfig> getVerticeConfigs() {
        return verticeConfigs;
    }

    public void setVerticeConfigs(List<VerticeConfig> verticeConfigs) {
        this.verticeConfigs = verticeConfigs;
    }

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }
}
