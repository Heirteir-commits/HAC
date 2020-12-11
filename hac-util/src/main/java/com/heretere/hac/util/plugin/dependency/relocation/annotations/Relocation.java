package com.heretere.hac.util.plugin.dependency.relocation.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Relocation.List.class)
public @interface Relocation {
    @NotNull
    String from();

    @NotNull
    String to();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface List {
        @NotNull
        Relocation[] value() default {};
    }
}
