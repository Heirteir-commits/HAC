package com.heirteir.hac.movement.util.mapping.type;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.heirteir.hac.api.API;
import com.heirteir.hac.api.util.reflections.helper.StringHelper;
import com.heirteir.hac.movement.Movement;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.util.AbstractMap;
import java.util.Set;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractMappedMethodBuilder {
    private final Movement movement;
    private final CtClass mapTo;
    private final CtClass declaringClass;
    private final Class<?> rawType;
    private final String mappedName;
    private final String obfuscatedName;
    private final ImmutableSet<AbstractMap.SimpleImmutableEntry<String, String>> tokens;
    @Getter(AccessLevel.PUBLIC)
    private final Set<CtMethod> methods;

    protected AbstractMappedMethodBuilder(Movement movement, CtClass mapTo, CtClass declaringClass, Class<?> rawType, String mappedName, String obfuscatedName) {
        this.movement = movement;
        this.mapTo = mapTo;
        this.declaringClass = declaringClass;
        this.rawType = rawType;
        this.mappedName = mappedName;
        this.obfuscatedName = obfuscatedName;
        this.tokens = ImmutableSet.of(
                new AbstractMap.SimpleImmutableEntry<>("%raw_type%", this.rawType.getSimpleName()),
                new AbstractMap.SimpleImmutableEntry<>("%mapped%", this.mappedName),
                new AbstractMap.SimpleImmutableEntry<>("%obfuscated%", this.obfuscatedName)
        );
        this.methods = Sets.newHashSet();
    }

    protected final String replaceCodes(String input) {
        String output = API.INSTANCE.getReflections().getHelpers().getHelper(StringHelper.class).replaceString(input);

        for (AbstractMap.SimpleImmutableEntry<String, String> token : this.tokens) {
            output = StringUtils.replace(output, token.getKey(), token.getValue());
        }

        return output;
    }
}
