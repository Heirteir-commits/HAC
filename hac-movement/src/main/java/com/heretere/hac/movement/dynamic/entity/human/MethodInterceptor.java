package com.heretere.hac.movement.dynamic.entity.human;

import com.google.common.collect.Maps;
import com.heretere.hac.api.API;
import com.heretere.hac.api.util.reflections.Reflections;
import com.heretere.hac.api.util.reflections.types.WrappedClass;
import com.heretere.hac.api.util.reflections.types.WrappedField;
import com.heretere.hac.api.util.reflections.types.WrappedMethod;
import com.heretere.hac.api.util.reflections.version.ServerVersion;
import com.heretere.hac.movement.Movement;
import com.heretere.hac.movement.util.mapping.annotation.DeclaringClass;
import com.heretere.hac.movement.util.mapping.annotation.MappingMethod;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;

public class MethodInterceptor {
    private static final String DATA_WATCHER = "DataWatcher";
    private static final String DATA_WATCHER_OBJECT = "DataWatcherObject";

    private final Movement movement;
    private final Map<String, WrappedMethod> methods;
    private WrappedField dataWatcherObject;
    private WrappedField dataWatcher;
    private WrappedMethod getByte;
    private WrappedMethod set;

    public MethodInterceptor(Movement movement) {
        this.movement = movement;
        this.methods = Maps.newHashMap();

        Reflections reflections = API.INSTANCE.getReflections();
        try {
            this.dataWatcher = reflections.getNMSClass("Entity").getFieldByName("datawatcher");

            if (reflections.getVersion().constraint(ServerVersion.EIGHT_R1, ServerVersion.EIGHT_R3)) {
                this.getByte = reflections.getNMSClass(MethodInterceptor.DATA_WATCHER)
                        .getMethod("getByte", int.class);
                this.set = reflections.getNMSClass(MethodInterceptor.DATA_WATCHER)
                        .getMethod("watch", int.class, Object.class);
            } else {
                this.dataWatcherObject = reflections.getNMSClass("Entity")
                        .getFieldByType(reflections.getNMSClass(MethodInterceptor.DATA_WATCHER_OBJECT).getRaw(), 0);
                this.getByte = reflections.getNMSClass(MethodInterceptor.DATA_WATCHER)
                        .getMethod("get", reflections.getNMSClass(MethodInterceptor.DATA_WATCHER_OBJECT).getRaw());
                this.set = reflections.getNMSClass(MethodInterceptor.DATA_WATCHER)
                        .getMethod("set", reflections.getNMSClass(MethodInterceptor.DATA_WATCHER_OBJECT).getRaw(), Object.class);
            }
        } catch (NoSuchMethodException | NoSuchFieldException e) {
            this.movement.getLog().reportFatalError(e);
        }
    }

    private WrappedMethod getWrappedMethod(Method method) {
        return this.methods.computeIfAbsent(method.getName(), name -> {
            WrappedClass declaring = API.INSTANCE.getReflections().getClass(method.getAnnotation(DeclaringClass.class).value());

            MappingMethod mappingMethod;
            if (method.isAnnotationPresent(MappingMethod.List.class)) {
                mappingMethod = Arrays.stream(method.getAnnotation(MappingMethod.List.class).value())
                        .filter(mapping -> API.INSTANCE.getReflections().getVersion().constraint(mapping.min(), mapping.max()))
                        .findFirst()
                        .orElse(null);
            } else {
                mappingMethod = method.getAnnotation(MappingMethod.class);
            }

            if (mappingMethod == null) {
                this.movement.getLog().reportFatalError(new RuntimeException("Method doesn't have a valid MappingMethod annotation."));
                return null;
            }

            WrappedMethod output;
            try {
                output = declaring.getMethod(mappingMethod.obfuscatedName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                output = null;
                this.movement.getLog().reportFatalError(e);
            }
            return output;
        });
    }

    public void interceptTick(@This Object base, @Origin Method method) {
        WrappedMethod wrappedMethod = this.getWrappedMethod(method);

        if (wrappedMethod == null) {
            return;
        }

        try {
            wrappedMethod.invoke(Object.class, base);
        } catch (InvocationTargetException | IllegalAccessException e) {
            if (!(e.getCause() instanceof NoSuchElementException)) {
                this.movement.getLog().severe(e);
            }
        }
    }

    public void interceptFall(@This Object base, @Argument(0) double distance, @Argument(1) boolean onGround) {
        EntityHumanAccessor accessor = (EntityHumanAccessor) base;
        if (onGround) {
            accessor.setFallDistance(0);
        } else {
            accessor.setFallDistance((float) (accessor.getFallDistance() - distance));
        }
    }

    public void interceptSetFlag(@This Object base, @Argument(0) int index, @Argument(1) boolean flag) {
        try {
            Object dataWatcherInstance = this.dataWatcher.get(Object.class, base);
            Object dataWatcherObjectInstance;

            if (this.dataWatcherObject == null) {
                dataWatcherObjectInstance = 0;
            } else {
                dataWatcherObjectInstance = this.dataWatcherObject.get(Object.class, null);
            }

            Byte value = this.getByte.invoke(Byte.class, dataWatcherInstance, dataWatcherObjectInstance);

            if (flag) {
                this.set.invoke(Object.class, dataWatcherInstance, dataWatcherObjectInstance, (byte) ((value & 0xff) | 1 << index));
            } else {
                this.set.invoke(Object.class, dataWatcherInstance, dataWatcherObjectInstance, (byte) (value & ~(1 << index)));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            this.movement.getLog().severe(e);
        }
    }
}
