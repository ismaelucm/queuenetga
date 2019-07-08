package com.massisframework.massis3.examples.simulator.Entity;

import com.massisframework.massis3.examples.simulator.Components.Transform;
import com.massisframework.massis3.examples.simulator.Configuration.ArquetypeConfig;
import com.massisframework.massis3.examples.simulator.Configuration.ComponentConfig;
import com.massisframework.massis3.examples.simulator.Configuration.InstanciationConfig;
import com.massisframework.massis3.examples.simulator.Core.ComponentFactory;
import com.massisframework.massis3.examples.simulator.Core.EntityCreationException;
import com.massisframework.massis3.examples.simulator.Core.Simulator;

import java.util.List;

public class EntityTransform extends Entity {

    private Transform _transform;

    public EntityTransform(String name) throws InstantiationException, IllegalAccessException {
        super(name);
        this.addComponent(Transform.class);
    }

    public EntityTransform(String name,ArquetypeConfig arquetypeConfig, InstanciationConfig instanciationConfig, Simulator sim) throws EntityCreationException, InstantiationException, IllegalAccessException {
        super(name,arquetypeConfig, instanciationConfig, sim);
        _transform = (Transform) this.addComponent(Transform.class);
        _transform.setPosition(this.getWorld().getNode(instanciationConfig.getPlace()));
    }

    public Transform getTransform() {
        return _transform;
    }
}
