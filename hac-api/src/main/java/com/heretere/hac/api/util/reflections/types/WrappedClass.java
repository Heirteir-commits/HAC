package com.heretere.hac.api.util.reflections.types;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public final class WrappedClass {

    private final Class<?> raw;
    private List<Field> fields = null;

    public WrappedConstructor getConstructorAtIndex(int index) {
        Constructor<?>[] constructors = this.raw.getDeclaredConstructors();

        if (index > constructors.length + 1) {
            throw new IndexOutOfBoundsException(String.format("There are only '%d' constructors in class '%s' but tried to access the '%d' constructor.", constructors.length + 1, this.raw.getName(), index));
        }

        return new WrappedConstructor(this, constructors[index]);
    }

    /**
     * Get constructor inside of class.
     *
     * @param types the parameters types for the specified constructor
     * @return WrappedConstructor instance of the constructor
     */
    public WrappedConstructor getConstructor(Class<?>... types) throws NoSuchMethodException {
        return new WrappedConstructor(this,
                Arrays.stream(this.raw.getDeclaredConstructors())
                        .filter(constructor -> Arrays.equals(types, constructor.getParameterTypes()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchMethodException(String.format("Constructor with parameters '%s' not found in class '%s'.", Arrays.toString(types), this.raw.getName())))
        );
    }

    private List<Field> getFields() {
        if (this.fields == null) {
            this.fields = Lists.newArrayList();

            Class<?> current = this.raw;

            while (current != Object.class) {
                this.fields.addAll(Arrays.asList(current.getDeclaredFields()));
                current = current.getSuperclass();
            }
        }

        return this.fields;
    }

    /**
     * Gets a specified field by type.
     *
     * @param type  type of field
     * @param index if multiple fields exist get value at specified index
     * @return WrappedField instance of the field
     */
    public WrappedField getFieldByType(Class<?> type, int index) {
        List<Field> typeFields = this.getFields()
                .stream()
                .filter(field -> field.getType().equals(type))
                .collect(Collectors.toList());

        if (index > typeFields.size() + 1) {
            throw new IndexOutOfBoundsException(String.format("There are only '%d' fields with type '%s' in class '%s' but tried to access the field at index '%d'.", this.getFields().size() + 1, type.getName(), this.raw.getName(), index));
        }

        return new WrappedField(this, typeFields.get(index));
    }

    /**
     * Get a field by name instead of type.
     *
     * @param name Name of the field
     * @return WrappedField instance of the field
     */
    public WrappedField getFieldByName(String name) throws NoSuchFieldException {
        return new WrappedField(this,
                this.getFields().stream()
                        .filter(field -> field.getName().equals(name))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchFieldException(String.format("Field with name '%s' not found in class '%s'.", name, this.raw.getSimpleName())))
        );
    }

    /**
     * Get a method using name and parameters.
     *
     * @param name       Method name
     * @param parameters Method parameters
     * @return WrappedMethod instance of the method
     */
    public WrappedMethod getMethod(String name, Class<?>... parameters) throws NoSuchMethodException {
        return new WrappedMethod(this,
                Arrays.stream(this.raw.getDeclaredMethods())
                        .filter(method -> method.getName().equals(name) && parameters.length == method.getParameterCount() && Arrays.equals(parameters, method.getParameterTypes()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchMethodException(String.format("Method with name '%s', and parameters '%s' doesn't not found in class '%s'.", name, Arrays.toString(parameters), this.raw.getName()))));
    }

    public WrappedMethod getMethodByParams(Class<?>... parameters) throws NoSuchMethodException {
        return new WrappedMethod(this,
                Arrays.stream(this.raw.getDeclaredMethods())
                        .filter(
                                method -> method.getParameterCount() == parameters.length &&
                                        Arrays.equals(method.getParameterTypes(), parameters)
                        )
                        .findFirst()
                        .orElseThrow(NoSuchMethodException::new)
        );
    }

    public <E extends Enum<?>> E getEnum(Class<E> clazz, String name) throws NoSuchFieldException {
        return Arrays.stream(clazz.getEnumConstants())
                .filter(e -> e.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new NoSuchFieldException(String.format("Enum with name '%s' doesn't exist in class '%s'.", name, clazz.getName())));
    }
}
