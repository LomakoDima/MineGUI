package ru.dimalab.minegui.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MineGUIAPI {
    String modId() default "";
    Class<?> mainClass() default Void.class;
}
