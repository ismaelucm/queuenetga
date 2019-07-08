package com.massisframework.massis3.examples.simulator.Configuration;

import com.massisframework.massis3.XMLHelpper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ArquetypeSetDescription {
    private static final String ARQUETYPE_SET = "ArquetypeSet";
    private static final String ARQUETYPE_CONFIG_LIST = "ArquetypeConfigList";
    private static final String ARQUETYPE_CONFIG = "ArquetypeConfig";


    private static final String NAME = "name";
    private static final String ARQUETYPE_NAME = "arquetypeName";


    private String name;
    private Map<String, ArquetypeConfig> _arquetypes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArquetypeSetDescription() {
        name = null;
        _arquetypes = new LinkedHashMap<>();
    }

    /*

     <ArquetypeSet name="ArquetypeSimulationConfigExample">
        <ArquetypeConfigList>
            <ArquetypeConfig arquetypeName="Pedestrian">
                <componentConfigList>
                    <ComponentConfig component="Pedestrian">
                        <AttributeList>
                            <Attribute name="path" value="" type=""/>
                        </AttributeList>
                      </ComponentConfig>
                </componentConfigList>
            </<ArquetypeConfig>
        <ArquetypeConfigList>
      </ArquetypeSet>
     */

    public ArquetypeSetDescription(InputStream stream) {
        this();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(stream);
            NodeList ARQUETYPENodeList = document.getElementsByTagName(ARQUETYPE_SET); //[ArquetypeSet]
            Node ARQUETYPENode = ARQUETYPENodeList.item(0); //ArquetypeSet
            Map<String, String> attrMap = XMLHelpper.getAttributes(ARQUETYPENode);

            name = attrMap.get(NAME);

            List<Node> ARQUETYPEConfigList = XMLHelpper.getChildsByName(ARQUETYPENode, ARQUETYPE_CONFIG_LIST);
            Node ARQUETYPEConfigListNode = ARQUETYPEConfigList.get(0); //ArquetypeConfigList
            System.out.println("-" + ARQUETYPEConfigListNode.getNodeName());
            List<Node> arquetypeConfigNodeList = XMLHelpper.getChildsByName(ARQUETYPEConfigListNode, ARQUETYPE_CONFIG); //[ArquetypeConfig]
            for (int i = 0; i < arquetypeConfigNodeList.size(); ++i) {
                Node arquetypeConfigXml = arquetypeConfigNodeList.get(i);
                System.out.println("-" + arquetypeConfigXml.getNodeName());
                Map<String, String> arConfigMap = XMLHelpper.getAttributes(arquetypeConfigXml);
                String arquetypeName = arConfigMap.get(ARQUETYPE_NAME);

                ArquetypeConfig arquetypeConfig = new ArquetypeConfig(arquetypeName, arquetypeConfigXml);
                _arquetypes.put(arquetypeName, arquetypeConfig);
            }
        } catch (Exception e) {
            System.out.println("Error in the arquetype creation " + e.getMessage());
        }
    }

    public ArquetypeConfig[] get_arquetypes() {
        return (ArquetypeConfig[]) _arquetypes.values().toArray();
    }

    public void setArquetypes(List<ArquetypeConfig> arquetypes) {
        for (ArquetypeConfig a : arquetypes) {
            _arquetypes.put(a.getArquetypeName(), a);
        }
    }

    public void set_arquetypes(ArquetypeConfig[] arquetypes) {
        for (ArquetypeConfig a : arquetypes) {
            _arquetypes.put(a.getArquetypeName(), a);
        }
    }

    public ArquetypeConfig getArquetype(String arquetypeName) {
        return _arquetypes.get(arquetypeName);
    }

    public void addArquetype(ArquetypeConfig a) {
        _arquetypes.put(a.getArquetypeName(), a);
    }
}
