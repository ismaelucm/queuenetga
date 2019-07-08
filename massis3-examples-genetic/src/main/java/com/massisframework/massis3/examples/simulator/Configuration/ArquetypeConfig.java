package com.massisframework.massis3.examples.simulator.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.massisframework.massis3.XMLHelpper;
import org.w3c.dom.*;

public class ArquetypeConfig {
    private static final String COMPONENT_CONFIG_LIST = "componentConfigList";
    private static final String COMPONENT_CONFIG = "ComponentConfig";
    private static final String ATTRIBUTE_LIST = "AttributeList";
    private static final String ATTRIBUTE = "Attribute";


    private String arquetypeName;
    private String tag;

    private List<ComponentConfig> componentConfigs;


    public ArquetypeConfig() {
        componentConfigs = new ArrayList<>();
    }

    public ArquetypeConfig(String name, Node xmlNodes) {
        this();
        arquetypeName = name;
        List<Node> componentConfigListNode = XMLHelpper.getChildsByName(xmlNodes, COMPONENT_CONFIG_LIST);
        if (componentConfigListNode.size() == 1) {
            List<Node> componentConfigList = XMLHelpper.getChildsByName(componentConfigListNode.get(0), COMPONENT_CONFIG);

            for (int i = 0; i < componentConfigList.size(); ++i) {
                Node componentConfigNode = componentConfigList.get(i);
                ComponentConfig component = new ComponentConfig(componentConfigNode);
                componentConfigs.add(component);
            }
        }
    }

    public String getArquetypeName() {
        return arquetypeName;
    }

    public void setArquetypeName(String arquetypeName) {
        this.arquetypeName = arquetypeName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<ComponentConfig> getComponentConfigs() {
        return componentConfigs;
    }

    public void setComponentConfigs(List<ComponentConfig> componentConfigs) {
        this.componentConfigs = componentConfigs;
    }

}
