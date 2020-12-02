package com.heirteir.hac.movement.dynamic.entity.human;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.heirteir.hac.api.API;
import com.heirteir.hac.api.util.reflections.types.WrappedClass;
import com.heirteir.hac.api.util.reflections.types.WrappedField;
import com.heirteir.hac.movement.Movement;
import com.heirteir.hac.movement.util.mapping.annotation.DeclaringClass;
import com.heirteir.hac.movement.util.mapping.annotation.MappingField;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GetterSetterInterceptor {
    private final Movement movement;
    private final Map<String, Set<WrappedField>> fields;

    public GetterSetterInterceptor(Movement movement) {
        this.movement = movement;
        this.fields = Maps.newHashMap();
    }

    private Set<WrappedField> getWrappedFields(Method method) {
        return this.fields.computeIfAbsent(method.getName(), name -> {
            WrappedClass declaring = API.INSTANCE.getReflections().getClass(method.getAnnotation(DeclaringClass.class).value());

            MappingField field;
            if (method.isAnnotationPresent(MappingField.List.class)) {
                field = Arrays.stream(method.getAnnotation(MappingField.List.class).value())
                        .filter(mappingField -> API.INSTANCE.getReflections().getVersion().constraint(mappingField.min(), mappingField.max()))
                        .findFirst()
                        .orElse(null);
            } else {
                field = method.getAnnotation(MappingField.class);
            }

            if (field == null) {
                this.movement.getLog().reportFatalError(new RuntimeException("Getter doesn't have a valid MappingField annotation."));
                return null;
            }

            Set<WrappedField> wrappedFields = Sets.newLinkedHashSet();

            try {
                if (field.obfuscatedName().contains(".")) {
                    Iterator<String> fieldIterator = Splitter.on('.').split(field.obfuscatedName()).iterator();

                    WrappedField parent = declaring.getFieldByName(fieldIterator.next());
                    wrappedFields.add(parent);
                    while (fieldIterator.hasNext()) {
                        declaring = API.INSTANCE.getReflections().getClass(parent.getRaw().getType());
                        WrappedField next = declaring.getFieldByName(fieldIterator.next());

                        wrappedFields.add(next);
                        parent = next;
                    }
                } else if (!field.obfuscatedName().isEmpty()) {
                    wrappedFields.add(declaring.getFieldByName(field.obfuscatedName()));
                }
            } catch (NoSuchFieldException e) {
                wrappedFields.clear();
                this.movement.getLog().reportFatalError(e);
            }
            return wrappedFields;
        });
    }

    @RuntimeType
    public Object interceptGetter(@This Object base, @Origin Method method) {
        Set<WrappedField> wrappedFields = this.getWrappedFields(method);

        Object value = base;

        try {
            for (WrappedField wrappedField : wrappedFields) {
                value = wrappedField.get(Object.class, value);
            }
        } catch (IllegalAccessException e) {
            this.movement.getLog().severe(e);
        }

        return value;
    }

    public void interceptSetter(@This Object base, @Origin Method method, @AllArguments Object[] arguments) {
        Iterator<WrappedField> wrappedFields = this.getWrappedFields(method).iterator();

        Object value = base;

        try {
            while (wrappedFields.hasNext()) {
                WrappedField current = wrappedFields.next();

                if (wrappedFields.hasNext()) {
                    value = current.get(Object.class, value);
                } else {
                    current.set(value, arguments[0]);
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            this.movement.getLog().severe(e);
        }
    }
}
