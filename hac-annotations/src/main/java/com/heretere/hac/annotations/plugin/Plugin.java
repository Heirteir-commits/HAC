package com.heretere.hac.annotations.plugin;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Plugin {
    @NotNull
    String name();

    @NotNull
    String version();

    @NotNull
    String apiVersion() default "1.13";

    @NotNull
    String prefix() default "";

    @NotNull
    String[] softDepends() default {};
}
