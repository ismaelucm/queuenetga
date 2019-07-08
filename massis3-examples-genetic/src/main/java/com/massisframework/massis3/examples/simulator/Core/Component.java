package com.massisframework.massis3.examples.simulator.Core;

import com.massisframework.massis3.examples.simulator.Components.Attribute;
import com.massisframework.massis3.examples.simulator.Components.Transform;
import com.massisframework.massis3.examples.simulator.Configuration.ComponentConfig;
import com.massisframework.massis3.examples.simulator.Configuration.InstanciationConfig;
import com.massisframework.massis3.examples.simulator.Core.IRunnable;
import com.massisframework.massis3.examples.simulator.Entity.Entity;
import com.massisframework.massis3.examples.simulator.Entity.EntityTransform;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public abstract class Component implements IRunnable, IEnabled {

    private Entity _entity;
    private boolean _enable;


    public void setEntity(Entity e) {
        _entity = e;
    }

    public Entity getEntity() {
        return _entity;
    }

    public Transform getTransfor() {
        EntityTransform t = (EntityTransform) _entity;
        return t.getTransform();
    }

    public Component withEntity(Entity e) {
        setEntity(e);
        return this;
    }

    public Component() {
        _enable = true;
        _entity = null;
    }

    public ComponentConfig Configure(ComponentConfig conf, ComponentConfig instanciationConfig, Entity e) {
        setEntity(e);
        ComponentConfig confFinal = null;
        if (conf != null) {
            confFinal = conf.clone();
            if (instanciationConfig != null) {
                confFinal.setEnable(instanciationConfig.isEnable());
                Map<String, String> attr = instanciationConfig.getAttributes();
                Set<String> keys = attr.keySet();
                for (String key : keys) {
                    String v = instanciationConfig.getAttributes().get(key);
                    confFinal.getAttributes().put(key, v);
                }
            }
        } else
            confFinal = instanciationConfig;


        ConfigureComponent(confFinal);
        return confFinal;
    }


    @Override
    public final void start() throws Exception {
        onStart();
    }

    @Override
    public final void update(int time) throws Exception {
        onUpdate(time);
    }

    @Override
    public final void end() {
        onEnd();
    }

    @Override
    public final void enable() {
        onEnable();
    }

    @Override
    public final void disable() {
        onDisable();
    }

    public void setEnable() {
        if (!_enable) {
            _enable = true;
            _entity.enableComponent(this);
        }
    }

    public void setDisable() {
        if (_enable) {
            _enable = false;
            _entity.disableComponent(this);
        }
    }

    public boolean isEnable() {
        return _enable;
    }

    protected abstract void onStart() throws Exception;

    protected abstract void onUpdate(int time) throws Exception;

    protected abstract void onEnd();

    protected abstract void onEnable();

    protected abstract void onDisable();

    public abstract void init();

    protected void ConfigureComponent(ComponentConfig config) {
        try {
            AutoConfigureAttributes(config);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void AutoConfigureAttributes(ComponentConfig config) throws IllegalAccessException {
        Map<String, String> attributes = config.getAttributes();

        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            Attribute[] anotations = field.getAnnotationsByType(Attribute.class); //do something to these
            for (Attribute attr : anotations) {
                String attrName = attr.name();
                Map<String, String> attrMapping = config.getAttributes();
                if (attrMapping.containsKey(attrName)) {
                    String v = config.getAttributes().get(attrName);
                    Object value = DeserializerAttributes.create(v, attr.typeOfType(), attr.type());
                    field.set(this, value);
                }
            }
        }
    }
}
