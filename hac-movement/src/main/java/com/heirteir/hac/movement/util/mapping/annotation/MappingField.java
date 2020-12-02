package com.heirteir.hac.movement.util.mapping.annotation;

import com.heirteir.hac.api.util.reflections.version.ServerVersion;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(MappingField.List.class)
public @interface MappingField {
    Type type();

    ServerVersion min() default ServerVersion.MIN;

    ServerVersion max() default ServerVersion.MAX;

    String obfuscatedName();

    enum Type {
        SETTER,
        GETTER
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface List {
        MappingField[] value();
    }
}
