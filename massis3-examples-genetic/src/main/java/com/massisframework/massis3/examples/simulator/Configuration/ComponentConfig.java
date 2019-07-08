package com.massisframework.massis3.examples.simulator.Configuration;

import java.util.*;


import java.util.Map;

import com.massisframework.massis3.XMLHelpper;
import org.w3c.dom.*;

public class ComponentConfig {
    private String component;
    private boolean enable;
    Map<String, String> attributes;

    private static final String COMPONENT = "component";
    private static final String ENABLE = "enable";
    private static final String ATTRIBUTE_LIST = "AttributeList";
    private static final String ATTRIBUTE = "Attribute";
    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String TYPE = "type";

    public ComponentConfig() {
        attributes = new LinkedHashMap<>();
        enable = true;
        component = null;
    }

    /*
                      <ComponentConfig component="Pedestrian">
                        <AttributeList>
                            <Attribute name="path" value="" type=""/>
                        </AttributeList>
                      </ComponentConfig>
     */
    public ComponentConfig(Node xmlNode) {
        this();
        Map<String, String> componentConfigAttr = XMLHelpper.getAttributes(xmlNode);
        component = componentConfigAttr.get(COMPONENT);
        if (componentConfigAttr.containsKey(ENABLE))
            enable = Boolean.parseBoolean(componentConfigAttr.get(ENABLE));
        else
            enable = true;

        List<Node> attributeList = XMLHelpper.getChildsByName(xmlNode, ATTRIBUTE_LIST);
        if (attributeList.size() == 1) {
            List<Node> attributes = XMLHelpper.getChildsByName(attributeList.get(0), ATTRIBUTE);

            for (int i = 0; i < attributes.size(); ++i) {
                Node attributeNode = attributes.get(i);
                Map<String, String> attributeMap = XMLHelpper.getAttributes(attributeNode);
                String name = attributeMap.get(NAME);
                String value = attributeMap.get(VALUE);
                this.attributes.put(name, value);
            }
        }
    }

    public ComponentConfig(String co, Map<String, String> attr) {
        component = co;
        attributes = attr;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public ComponentConfig clone() {
        Map<String, String> newAttributes = new LinkedHashMap<>();
        Set<String> keys = attributes.keySet();
        for (String key : keys) {
            String value = attributes.get(key);
            newAttributes.put(key, value);
        }
        ComponentConfig cc = new ComponentConfig(component, newAttributes);
        cc.setEnable(enable);
        return cc;
    }


    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
