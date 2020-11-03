package com.heirteir.hac.util.dependency.types.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Spigot.List.class)
public @interface Spigot {
    String groupId();

    String artifactId();

    String version();

    String repoUrl() default "https://repo1.maven.org/maven2";

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface List {
        Spigot[] value() default {};
    }
}
