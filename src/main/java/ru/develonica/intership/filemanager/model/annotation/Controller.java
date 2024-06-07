package ru.develonica.intership.filemanager.model.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Аннотация для пометки классов контроллеров.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Controller {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
