package com.heretere.hac.api.config.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Config key.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigKey {
    /**
     * The config file path.
     *
     * @return the string
     */
    @NotNull String path();

    /**
     * The name of the getter method for this key.
     *
     * @return the getter
     */
    @NotNull String getter();

    /**
     * The name of the setter method for this key.
     *
     * @return the setter
     */
    @NotNull String setter();

    /**
     * Comments that should be added to the key.
     *
     * @return The comments
     */
    @NotNull String[] comments() default {};
}
