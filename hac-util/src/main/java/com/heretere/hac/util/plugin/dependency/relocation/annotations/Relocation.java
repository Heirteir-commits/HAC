package com.heretere.hac.util.plugin.dependency.relocation.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Relocation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Relocation.List.class)
public @interface Relocation {
    /**
     * The original package location of the relocation.
     * <p>
     * replace '.' with '|'. to avoid issues with gradle/maven relocation.
     *
     * @return the string
     */
    @NotNull String from();

    /**
     * Where the package should be relocated to.
     * <p>
     * replace '.' with '|'. to avoid issues with gradle/maven relocation.
     *
     * @return the string
     */
    @NotNull String to();

    /**
     * The interface List.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface List {
        /**
         * Value relocation [ ].
         *
         * @return the relocation [ ]
         */
        @NotNull Relocation[] value() default {};
    }
}
