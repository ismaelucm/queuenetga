package com.massisframework.massis3.examples.simulator.Entity;

import com.massisframework.massis3.examples.simulator.Components.Globals;
import com.massisframework.massis3.examples.simulator.Components.Transform;
import com.massisframework.massis3.examples.simulator.Configuration.ArquetypeConfig;
import com.massisframework.massis3.examples.simulator.Configuration.ComponentConfig;
import com.massisframework.massis3.examples.simulator.Configuration.InstanciationConfig;
import com.massisframework.massis3.examples.simulator.Core.*;
import com.massisframework.massis3.examples.simulator.World.World;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class Entity implements IRunnable {
    private List<Component> _enableComponents;
    private List<Component> _disableComponents;
    private Map<Class<? extends Component>, Component> _componentMapper;
    private Simulator _simulator;
    private Globals.TAGS _tag;
    private int _id;
    private String _name;

    private static int _entityIDGenerator = 0;

    private static int getNextID() {
        return _entityIDGenerator++;
    }

    private List<Component> _componentsToBeDisabled;
    private List<Component> _componentsToBeEnabled;

    private boolean _active;

    public Entity(String name) {
        _name = name;
        _enableComponents = new ArrayList<>();
        _disableComponents = new ArrayList<>();
        _componentsToBeDisabled = new ArrayList<>();
        _componentsToBeEnabled = new ArrayList<>();
        _componentMapper = new LinkedHashMap<>();
        _id = getNextID();
    }

    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    public int ID() {
        return _id;
    }

    public World getWorld() {
        return _simulator.getWorld();
    }

    public Entity(String name,ArquetypeConfig arquetypeConfig, InstanciationConfig instanciationConfig, Simulator sim) throws EntityCreationException {
        this(name);
        if (arquetypeConfig.getTag() != null)
            setTag(arquetypeConfig.getTag());
        _simulator = sim;
        Map<String, ComponentConfig> instComponentConfigList = instanciationConfig.getComponentConfigOverrideMap();
        List<ComponentConfig> componentConfigList = arquetypeConfig.getComponentConfigs();
        for (int i = 0; i < componentConfigList.size(); ++i) {
            ComponentConfig cConfig = componentConfigList.get(i);

            try {
                Component component = ComponentFactory.Create(cConfig.getComponent());
                ComponentConfig instanciatonComponent = instComponentConfigList.get(cConfig.getComponent());

                ComponentConfig finalConfig = component.Configure(cConfig, instanciatonComponent, this);
                addComponent(component);

                if (!finalConfig.isEnable())
                    component.setDisable();
            } catch (Exception e) {
                throw new EntityCreationException("Error in the Component creation " + e.getMessage());
            }
        }
    }

    public Globals.TAGS getTag() {
        return _tag;
    }

    public void setTag(String tag) {
        _tag = Globals.TAGS.valueOf(tag);
    }

    public void setTag(Globals.TAGS tag) {
        _tag = tag;
    }

    @Override
    public void start() throws Exception {
        for (int i = 0; i < _enableComponents.size(); ++i) {
            _enableComponents.get(i).start();
        }
    }

    @Override
    public void update(int time) throws Exception {

        completeDefferredDisableComponents();
        completeDefferredEnableComponents();

        for (int i = 0; i < _enableComponents.size(); ++i) {
            //System.out.println("update component "+_enableComponents.get(i).getClass().getName());
            _enableComponents.get(i).update(time);
        }


    }

    @Override
    public void end() {

        for (int i = 0; i < _enableComponents.size(); ++i) {
            _enableComponents.get(i).end();
        }

        for (int i = 0; i < _disableComponents.size(); ++i) {
            _disableComponents.get(i).end();
        }
    }

    public void Destroy() {
        _enableComponents.clear();
        _disableComponents.clear();
        _componentsToBeDisabled.clear();
        _componentsToBeEnabled.clear();
        _componentMapper.clear();
    }


    public Component addComponent(Class<? extends Component> clazz) throws IllegalAccessException, InstantiationException {
        Component c = (Component) clazz.newInstance();
        return addComponent(c);
    }

    public Component addComponent(Component c) {
        _enableComponents.add(c);
        _componentMapper.put(c.getClass(), c);
        c.setEntity(this);
        c.init();
        return c;
    }

    public <T extends Component> T getComponent(Class<? extends Component> clazz) {
        return (T) _componentMapper.get(clazz);
    }

    public boolean hasComponent(Class<? extends Component> clazz) {
        return _componentMapper.containsKey(clazz);
    }


    public void disableComponent(Component c) {
        if (_enableComponents.indexOf(c) >= 0)
            _componentsToBeDisabled.add(c);
    }

    public void enableComponent(Component c) {
        if (_disableComponents.indexOf(c) >= 0)
            _componentsToBeEnabled.add(c);
    }

    public void active() {
        if (!_active) {
            _active = true;
            _simulator.activateEntity(this);
        }
    }

    public void desactive() {
        if (_active) {
            _active = false;
            _simulator.desactivateEntity(this);
        }
    }

    protected void completeDefferredDisableComponents() {
        for (int i = 0; i < _componentsToBeDisabled.size(); ++i) {
            _enableComponents.remove(_componentsToBeDisabled.get(i));
            _disableComponents.add(_componentsToBeDisabled.get(i));
        }
        _componentsToBeDisabled.clear();
    }

    protected void completeDefferredEnableComponents() {
        for (int i = 0; i < _componentsToBeEnabled.size(); ++i) {
            _disableComponents.remove(_componentsToBeEnabled.get(i));
            _enableComponents.add(_componentsToBeEnabled.get(i));
        }
        _componentsToBeEnabled.clear();
    }


    public Simulator getSimulator() {
        return _simulator;
    }


}
