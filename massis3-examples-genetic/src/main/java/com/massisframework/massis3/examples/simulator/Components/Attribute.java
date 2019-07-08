package com.massisframework.massis3.examples.simulator.Components;

import com.massisframework.massis3.examples.simulator.Core.DeserializerAttributes;
import com.massisframework.massis3.examples.simulator.Core.SerializableType;
import com.massisframework.massis3.examples.simulator.Core.TypeOfType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //can use in method only.
public @interface Attribute {
    String name();

    SerializableType type();

    TypeOfType typeOfType() default TypeOfType.SIMPLE;

}
