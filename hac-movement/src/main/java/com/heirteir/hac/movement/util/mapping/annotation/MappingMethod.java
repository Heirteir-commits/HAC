package com.heirteir.hac.movement.util.mapping.annotation;


import com.heirteir.hac.api.util.reflections.version.ServerVersion;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(MappingMethod.List.class)
public @interface MappingMethod {

    Type type();

    ServerVersion min() default ServerVersion.MIN;

    ServerVersion max() default ServerVersion.MAX;

    String obfuscatedName();

    String[] params() default {};

    enum Type {
        COPY,
        EMPTY,
        REPLACE,
        PREPEND
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface List {
        MappingMethod[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Repeatable(Body.List.class)
    @interface Body {
        ServerVersion min() default ServerVersion.MIN;

        ServerVersion max() default ServerVersion.MAX;

        String value() default "";

        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface List {
            Body[] value();
        }
    }
}
