package com.heirteir.hac.movement.util.mapping.builder;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.heirteir.hac.api.API;
import com.heirteir.hac.api.util.reflections.helper.StringHelper;
import com.heirteir.hac.movement.Movement;
import com.heirteir.hac.movement.util.mapping.builder.annotation.DeclaringClass;
import com.heirteir.hac.movement.util.mapping.builder.annotation.MappingField;
import com.heirteir.hac.movement.util.mapping.builder.annotation.MappingMethod;
import com.heirteir.hac.movement.util.mapping.builder.annotation.RawType;
import com.heirteir.hac.movement.util.mapping.type.MappingMethodBuilder;
import com.heirteir.hac.movement.util.mapping.type.MappingMethodFieldBuilder;
import javassist.ClassPool;
import javassist.CtClass;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public final class MappingUtil {
    private MappingUtil() {
        throw new IllegalStateException("Utility Class");
    }

    private static void addFieldMappings(Movement movement, CtClass mapTo, CtClass declaringClass, Class<?> rawType, MappingBuilder builder, Method method) {
        Set<MappingField> fieldMappings = null;

        if (method.isAnnotationPresent(MappingField.List.class)) {
            fieldMappings = Sets.newHashSet(method.getAnnotation(MappingField.List.class).value());
        }

        if (method.isAnnotationPresent(MappingField.class)) {
            fieldMappings = Sets.newHashSet(method.getAnnotation(MappingField.class));
        }

        if (fieldMappings != null) {
            fieldMappings.forEach(mappingField -> {
                boolean valid = API.INSTANCE.getReflections().getVersion().constraint(mappingField.min(), mappingField.max());

                if (valid) {
                    builder.setValid(true);

                    MappingMethodFieldBuilder fieldBuilder = new MappingMethodFieldBuilder(movement, mapTo, declaringClass, rawType, method.getName(), mappingField.obfuscatedName());
                    switch (mappingField.type()) {
                        case SETTER:
                            fieldBuilder.createMappedSetter();
                            break;
                        case GETTER:
                            fieldBuilder.createMappedGetter();
                            break;
                        default:
                            break;
                    }
                    builder.addMapping(fieldBuilder);
                }
            });
        }
    }

    private static void addMethodMappings(Movement movement, ClassPool pool, CtClass mapTo, CtClass declaringClass, Class<?> rawType, MappingBuilder builder, Method method) {
        Set<MappingMethod> methodMappings = null;

        if (method.isAnnotationPresent(MappingMethod.List.class)) {
            methodMappings = Sets.newHashSet(method.getAnnotation(MappingMethod.List.class).value());
        }

        if (method.isAnnotationPresent(MappingMethod.class)) {
            methodMappings = Sets.newHashSet(method.getAnnotation(MappingMethod.class));
        }

        if (methodMappings != null) {
            Set<MappingMethod.Body> methodBodies = null;

            if (method.isAnnotationPresent(MappingMethod.Body.List.class)) {
                methodBodies = Sets.newHashSet(method.getAnnotation(MappingMethod.Body.List.class).value());
            } else if (method.isAnnotationPresent(MappingMethod.Body.class)) {
                methodBodies = Sets.newHashSet(method.getAnnotation(MappingMethod.Body.class));
            }

            assert methodBodies != null;

            String body = methodBodies.stream()
                    .filter(methodBody -> API.INSTANCE.getReflections().getVersion().constraint(methodBody.min(), methodBody.max()))
                    .map(MappingMethod.Body::value)
                    .findAny()
                    .orElse(null);

            methodMappings.forEach(methodMapping -> {
                boolean valid = API.INSTANCE.getReflections().getVersion().constraint(methodMapping.min(), methodMapping.max());

                if (valid) {
                    builder.setValid(true);

                    MappingMethodBuilder methodBuilder = new MappingMethodBuilder(movement, mapTo, declaringClass, rawType, method.getName(), methodMapping.obfuscatedName(), Arrays.stream(methodMapping.params()).map(param -> pool.getOrNull(API.INSTANCE.getReflections().getHelpers().getHelper(StringHelper.class).replaceString(param))).toArray(CtClass[]::new));

                    switch (methodMapping.type()) {
                        case COPY:
                            methodBuilder.createMappedCopy();
                            break;
                        case EMPTY:
                            methodBuilder.createMappedEmpty();
                            break;
                        case REPLACE:
                            methodBuilder.createMappedReplace(body);
                            break;
                        default:
                            break;
                    }

                    builder.addMapping(methodBuilder);
                }
            });
        }
    }

    public static MappingBuilder mapFromInterface(Movement movement, ClassPool pool, CtClass mapTo, Class<?> clazz) {
        Preconditions.checkArgument(clazz.isInterface(), String.format("Class '%s' is not an interface", clazz.getName()));

        MappingBuilder builder = new MappingBuilder();
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(DeclaringClass.class)) {
                continue;
            }
            assert method.isAnnotationPresent(RawType.class);

            CtClass declaringClass = pool.getOrNull(API.INSTANCE.getReflections().getHelpers().getHelper(StringHelper.class).replaceString(method.getAnnotation(DeclaringClass.class).value()));
            Class<?> rawType = method.getAnnotation(RawType.class).value();

            MappingUtil.addFieldMappings(movement, mapTo, declaringClass, rawType, builder, method);
            MappingUtil.addMethodMappings(movement, pool, mapTo, declaringClass, rawType, builder, method);
        }

        return builder;
    }
}
