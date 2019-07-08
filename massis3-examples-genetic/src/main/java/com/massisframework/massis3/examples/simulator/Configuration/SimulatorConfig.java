package com.massisframework.massis3.examples.simulator.Configuration;

import com.massisframework.massis3.XMLHelpper;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimulatorConfig {

    private static String INSTANCIATION_CONFIG = "InstanciationConfig";
    private String sceneName;
    private String arquetypeName;
    private WorldConfig _worldConfig;

    private List<InstanciationConfig> _instanciations;

    public SimulatorConfig() {
        _instanciations = new ArrayList<>();
        sceneName = null;
        arquetypeName = null;
        _worldConfig = null;
    }

    /*
            <InstanciationConfig numElements="25" arquetype="Pedestrian" place="Lobby">
                <ComponentConfigOverrideList>
                    <ComponentConfig component="Pedestrian">
                        <AttributeList>
                            <Attribute name="path" value="Lobby,CorridorToRoomA,CorridorToRoomB,RoomA" type=""/>
                        </AttributeList>
                    </ComponentConfig>
                </ComponentConfigOverrideList>
            </InstanciationConfig>
     */

    public SimulatorConfig(String sceneName, String arquetypeName, Node worldConfig, Node instatiatonListNode) {
        this.sceneName = sceneName;
        _instanciations = new ArrayList<>();
        this.arquetypeName = arquetypeName;
        _worldConfig = new WorldConfig(worldConfig);
        List<Node> instatiatonList = XMLHelpper.getChildsByName(instatiatonListNode, INSTANCIATION_CONFIG);
        createInstanciationList(instatiatonList);
    }

    protected void createInstanciationList(List<Node> instatiatonList) {
        for (int i = 0; i < instatiatonList.size(); ++i) {
            Node instatiaton = instatiatonList.get(i);
            InstanciationConfig inst = new InstanciationConfig(instatiaton);
            _instanciations.add(inst);
        }
    }


    public WorldConfig get_worldConfig() {
        return _worldConfig;
    }

    public void set_worldConfig(WorldConfig _worldConfig) {
        this._worldConfig = _worldConfig;
    }

    public List<InstanciationConfig> get_instanciations() {
        return _instanciations;
    }

    public void set_instanciations(List<InstanciationConfig> _instanciations) {
        this._instanciations = _instanciations;
    }

    public String getArquetypeName() {
        return arquetypeName;
    }

    public void setArquetypeName(String arquetypeName) {
        this.arquetypeName = arquetypeName;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }
}
