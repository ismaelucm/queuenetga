package com.massisframework.massis3.examples.simulator.Managers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TheSystemMgr {
    private static TheSystemMgr _instance = null;

    public static TheSystemMgr getInstance() {
        if (_instance != null) {
            _instance = new TheSystemMgr();
        }
        return _instance;
    }

    private Map<Class<?>, IManager> _manager;

    public TheSystemMgr addMgr(Class<? extends IManager> clazz) throws IllegalAccessException, InstantiationException {
        if (!_manager.containsKey(clazz))
            _manager.put(clazz, (IManager) clazz.newInstance());
        return this;
    }

    public TheSystemMgr removeMgr(Class<? extends IManager> clazz) {
        if (_manager.containsKey(clazz))
            _manager.remove(clazz);
        return this;
    }

    public <T extends IManager> T get(Class<? extends IManager> clazz) {
        if (_manager.containsKey(clazz))
            return (T) _manager.get(clazz);
        else
            return null;
    }

    public boolean exist(Class<? extends IManager> clazz) {
        return _manager.containsKey(clazz);
    }

    private TheSystemMgr() {
        _manager = new LinkedHashMap<>();
    }
}
