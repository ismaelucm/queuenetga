package com.massisframework.massis3.examples.simulator.Configuration;

import com.massisframework.massis3.XMLHelpper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

public class InstanciationConfig {
    private static final String COMPONENT_CONFIG_OVERRIDE = "ComponentConfigOverrideList";
    private static final String COMPONENT_CONFIG = "ComponentConfig";
    private static final String NUM_ELEMENTS = "numElements";
    private static final String ARQUETYPE = "arquetype";
    private static final String PLACE = "place";
    private static final String NAME = "name";

    private int numElements;



    private String name;
    private String arquetype;
    private String place; //Por simplicidad, usamos place para simplificar la creación de elementos que tengan una posición en el grafo. Es opcional
    List<ComponentConfig> componentConfigOverride;

    public InstanciationConfig() {
        numElements = 0;
        arquetype = null;
        place = null;
        componentConfigOverride = new ArrayList<>();
    }

    /*
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
     */
    public InstanciationConfig(Node instanciationConfig) {
        this();
        Map<String, String> instatiatonAttrMap = XMLHelpper.getAttributes(instanciationConfig);

        arquetype = instatiatonAttrMap.get(ARQUETYPE);
        numElements = Integer.parseInt(instatiatonAttrMap.get(NUM_ELEMENTS));
        name = instatiatonAttrMap.get(NAME);
        if (instatiatonAttrMap.containsKey(PLACE))
            place = instatiatonAttrMap.get(PLACE);

        List<Node> componentConfigOverrideList = XMLHelpper.getChildsByName(instanciationConfig, COMPONENT_CONFIG_OVERRIDE);
        if (componentConfigOverrideList.size() > 0) {
            Node componentConfigOverrideListNode = componentConfigOverrideList.get(0);
            List<Node> componentConfigList = XMLHelpper.getChildsByName(componentConfigOverrideListNode, COMPONENT_CONFIG);


            for (int i = 0; i < componentConfigList.size(); ++i) {
                ComponentConfig componentConfig = new ComponentConfig(componentConfigList.get(i));
                componentConfigOverride.add(componentConfig);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumElements() {
        return numElements;
    }

    public void setNumElements(int numElements) {
        this.numElements = numElements;
    }


    public String getArquetype() {
        return arquetype;
    }

    public void setArquetype(String arquetype) {
        this.arquetype = arquetype;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<ComponentConfig> getComponentConfigOverride() {
        return componentConfigOverride;
    }

    public Map<String, ComponentConfig> getComponentConfigOverrideMap() {
        Map<String, ComponentConfig> map = new LinkedHashMap<>();
        for (ComponentConfig cc : componentConfigOverride) {
            map.put(cc.getComponent(), cc);
        }
        return map;
    }

    public void setComponentConfigOverride(List<ComponentConfig> componentConfigOverride) {
        this.componentConfigOverride = componentConfigOverride;
    }
}
