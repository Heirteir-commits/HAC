package com.heirteir.hac.util.dependency.types.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Github.List.class)
public @interface Github {
    String fileName();

    String pluginName();

    String githubRepoRelativeURL();

    int spigotId();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface List {
        Github[] value() default {};
    }
}
