package com.heretere.hac.util.dependency.types.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Maven.List.class)
public @interface Maven {
    String groupId();

    String artifactId();

    String version();

    String repoUrl() default "https://repo1.maven.org/maven2";

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface List {
        Maven[] value() default {};
    }
}

