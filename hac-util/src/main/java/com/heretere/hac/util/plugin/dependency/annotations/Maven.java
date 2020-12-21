package com.heretere.hac.util.plugin.dependency.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Maven.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Maven.List.class)
public @interface Maven {
    /**
     * The Group id.
     *
     * @return the string
     */
    @NotNull
    String groupId();

    /**
     * The Artifact id.
     *
     * @return the string
     */
    @NotNull
    String artifactId();

    /**
     * The Version.
     *
     * @return the string
     */
    @NotNull
    String version();

    /**
     * Repo url.
     *
     * @return the string
     */
    @NotNull
    String repoUrl() default "https://repo1.maven.org/maven2/";

    /**
     * The interface List.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface List {
        /**
         * Maven annotation array.
         *
         * @return the maven []
         */
        @NotNull
        Maven[] value() default {};
    }

}
