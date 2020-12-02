package com.heirteir.hac.movement.util.mapping.builder;

import com.google.common.collect.Sets;
import com.heirteir.hac.movement.util.mapping.type.AbstractMappedMethodBuilder;
import javassist.CtMethod;
import lombok.Setter;

import java.util.Set;

public final class MappingBuilder {
    private final Set<AbstractMappedMethodBuilder> mappings;

    @Setter
    private boolean valid;

    public MappingBuilder() {
        this.mappings = Sets.newHashSet();
        this.valid = true;
    }

    public void addMapping(AbstractMappedMethodBuilder mapping) {
        if (valid) {
            this.mappings.add(mapping);
        }
    }

    public Set<CtMethod> build() {
        Set<CtMethod> output = Sets.newHashSet();

        mappings.forEach(mapping -> output.addAll(mapping.getMethods()));
        return output;
    }
}
