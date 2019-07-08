package com.massisframework.massis3.examples.simulator.Core;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class ComponentFactory {

    private static Map<String, Class<? extends Component>> _components = null;

    public static Component Create(String componentName) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (_components == null)
            _components = FindAllComponents();
        Class<? extends Component> clazz = _components.get(componentName);
        return Create(clazz);
    }

    public static Component Create(Class<? extends Component> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> ctor = clazz.getConstructor(null);
        Object component = ctor.newInstance();
        return (Component) component;
    }

    protected static Map<String, Class<? extends Component>> FindAllComponents() {
        Map<String, Class<? extends Component>> componentMapper = new LinkedHashMap<>();
        Reflections reflections = new Reflections("com.massisframework.massis3.examples.simulator");
        Set<Class<? extends Component>> components = reflections.getSubTypesOf(Component.class);
        for (Class<? extends Component> clazz : components) {
            componentMapper.put(clazz.getSimpleName(), clazz);
        }
        return componentMapper;
    }
}
