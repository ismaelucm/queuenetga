package com.massisframework.massis3.examples.simulator.Core;


import java.util.ArrayList;
import java.util.List;

public final class DeserializerAttributes {

    public static Object create(String value, TypeOfType tt, SerializableType t) {
        if (tt == TypeOfType.SIMPLE)
            return createSimple(value, t);
        else if (tt == TypeOfType.LIST)
            return createList(value, t);
        else
            return null;
    }

    public static Object createSimple(String v, SerializableType t) {
        Object ret = null;
        switch (t) {
            case BOOLEAN:
                ret = Boolean.parseBoolean(v);
                break;
            case FLOAT:
                ret = Float.parseFloat(v);
                break;
            case STRING:
                ret = v;
                break;
            case INT:
                ret = Integer.parseInt(v);
                break;
        }

        return ret;
    }

    public static Object createList(String value, SerializableType t) {
        List<Object> list = new ArrayList<>();
        String[] elements = value.split(",");
        for (int i = 0; i < elements.length; ++i) {
            Object o = createSimple(elements[i], t);
            list.add(o);
        }
        return list;
    }
}
