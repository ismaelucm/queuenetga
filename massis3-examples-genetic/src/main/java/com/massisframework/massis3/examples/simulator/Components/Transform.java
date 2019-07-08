package com.massisframework.massis3.examples.simulator.Components;

import com.massisframework.massis3.examples.simulator.Core.Component;
import com.massisframework.massis3.examples.simulator.World.SimulationNode;

import static com.massisframework.massis3.examples.simulator.Core.SerializableType.STRING;
import static com.massisframework.massis3.examples.simulator.Core.TypeOfType.LIST;
import static com.massisframework.massis3.examples.simulator.Core.TypeOfType.SIMPLE;

public class Transform extends Component {

    @Attribute(name = "nodeName", type = STRING, typeOfType = SIMPLE)
    public String _nodeName;

    private SimulationNode _node;


    public SimulationNode getPosition() {
        return _node;
    }

    public void setPosition(SimulationNode node) {
        if (_node != null)
            _node.remove(this.getEntity());
        _node = node;
        _node.add(this.getEntity());
    }

    @Override
    public void init() {

        if (_nodeName != null && _nodeName.compareTo("") != 0) {
            setPosition(this.getEntity().getWorld().getNode(_nodeName));
        }
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onUpdate(int time) {

    }

    @Override
    protected void onEnd() {

    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }


}
