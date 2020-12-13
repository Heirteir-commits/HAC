package com.heretere.hac.api.config.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigKey {
    @NotNull
    String path();

    @NotNull
    String getter();

    @NotNull
    String setter();

    @NotNull
    String[] comments() default {};
}
