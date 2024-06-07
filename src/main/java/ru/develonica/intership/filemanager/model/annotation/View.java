package ru.develonica.intership.filemanager.model.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Аннотация для пометки классов представлений.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface View {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
