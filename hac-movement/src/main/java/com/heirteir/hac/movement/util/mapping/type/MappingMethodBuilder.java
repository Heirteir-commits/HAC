package com.heirteir.hac.movement.util.mapping.type;

import com.heirteir.hac.movement.Movement;
import javassist.*;

public final class MappingMethodBuilder extends AbstractMappedMethodBuilder {
    private final CtClass[] params;

    public MappingMethodBuilder(Movement movement, CtClass mapTo, CtClass declaringClass, Class<?> rawType, String mappedName, String obfuscatedName, CtClass... params) {
        super(movement, mapTo, declaringClass, rawType, mappedName, obfuscatedName);
        this.params = params;
    }

    public void createMappedCopy() {
        try {
            super.getMethods().add(CtNewMethod.copy(
                    super.getDeclaringClass().getDeclaredMethod(super.getObfuscatedName(), this.params),
                    super.getMappedName(),
                    super.getMapTo(),
                    null
            ));
        } catch (CannotCompileException | NotFoundException e) {
            super.getMovement().getLog().reportFatalError(e.getMessage());
        }
    }

    public void createMappedEmpty() {
        try {
            super.getMethods().add(CtNewMethod.make(
                    ClassPool.getDefault().get(super.getRawType().getName()),
                    super.getMappedName(),
                    this.params,
                    new CtClass[]{},
                    "{}",
                    super.getMapTo()
            ));
        } catch (CannotCompileException | NotFoundException e) {
            super.getMovement().getLog().reportFatalError(e.getMessage());
        }
    }

    public void createMappedReplace(String method) {
        try {
            super.getMethods().add(CtNewMethod.make(
                    super.replaceCodes(method),
                    super.getMapTo()
            ));
        } catch (CannotCompileException e) {
            super.getMovement().getLog().reportFatalError(e.getMessage());
        }
    }
}
