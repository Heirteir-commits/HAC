package com.heretere.hac.api.config.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface ConfigSection.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigSection {
    /**
     * The key of the section in a yaml file.
     *
     * @return the string
     */
    @NotNull String key();

    /**
     * Comments attached to the section.
     *
     * @return the string [ ]
     */
    @NotNull String[] comments() default {};
}
