package com.heretere.hac.util.plugin.dependency.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Maven.List.class)
public @interface Maven {
    @NotNull
    String groupId();

    @NotNull
    String artifactId();

    @NotNull
    String version();

    @NotNull
    String repoUrl() default "https://repo1.maven.org/maven2";

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface List {
        @NotNull
        Maven[] value() default {};
    }
}
