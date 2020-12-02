package com.heirteir.hac.movement.util.mapping.type;

import com.heirteir.hac.movement.Movement;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtNewMethod;

public final class MappingMethodFieldBuilder extends AbstractMappedMethodBuilder {
    public MappingMethodFieldBuilder(Movement movement, CtClass mapTo, CtClass declaringClass, Class<?> rawType, String mappedName, String obfuscatedName) {
        super(movement, mapTo, declaringClass, rawType, mappedName, obfuscatedName);
    }

    public void createMappedSetter() {
        try {
            super.getMethods().add(CtNewMethod.make(super.replaceCodes(
                    "public void %mapped%(%raw_type% a){" +
                            "this.%obfuscated% = a;" +
                            "}"
            ), super.getMapTo()));
        } catch (CannotCompileException e) {
            super.getMovement().getLog().severe(e);
        }
    }

    public void createMappedGetter() {
        try {
            super.getMethods().add(CtNewMethod.make(super.replaceCodes(
                    "public %raw_type% %mapped%(){" +
                            "return this.%obfuscated%;" +
                            "}"
            ), super.getMapTo()));
        } catch (CannotCompileException e) {
            super.getMovement().getLog().severe(e);
        }
    }
}
