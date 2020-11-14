package com.heirteir.hac.movement.dynamic.entity.human;

import com.heirteir.hac.api.API;
import com.heirteir.hac.api.util.reflections.types.WrappedConstructor;
import com.heirteir.hac.movement.Movement;
import com.heirteir.hac.movement.dynamic.AbstractDynamicClassCreator;
import com.heirteir.hac.movement.util.mapping.annotation.DeclaringClass;
import com.heirteir.hac.movement.util.mapping.annotation.MappingField;
import lombok.Getter;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

@Getter
public class HACHumanCreator extends AbstractDynamicClassCreator {
    private WrappedConstructor humanConstructor;
    private WrappedConstructor foodMetaDataConstructor;

    public HACHumanCreator(Movement movement) {
        super("HACHuman", movement);
    }

    private static String getCollideMethodName() {
        switch (API.INSTANCE.getReflections().getVersion()) {
            case EIGHT_R1:
                return "bK";
            case EIGHT_R2:
            case EIGHT_R3:
                return "bL";
            case NINE_R1:
                return "cn";
            case NINE_R2:
                return "co";
            case TEN_R1:
                return "cs";
            case ELEVEN_R1:
                return "ct";
            case TWELVE_R1:
                return "cB";
            case THIRTEEN_R1:
            case THIRTEEN_R2:
                return "cN";
            case FOURTEEN_R1:
            case FIFTEEN_R1:
            case SIXTEEN_R1:
            case SIXTEEN_R2:
                return "collideNearby";
            default:
                return null;
        }
    }

    @Override
    public void load() {
        Class<?> entityHuman = API.INSTANCE.getReflections().getNMSClass("EntityHuman").getRaw();

        Class<?> foodMetaData = API.INSTANCE.getReflections().getNMSClass("FoodMetaData").getRaw();
        foodMetaData = new ByteBuddy()
                .subclass(foodMetaData)
                .method(ElementMatchers.isDeclaredBy(foodMetaData).and(ElementMatchers.returns(void.class)))
                .intercept(FixedValue.originType())
                .make()
                .load(this.getClass().getClassLoader())
                .getLoaded();

        GetterSetterInterceptor getterSetterInterceptor = new GetterSetterInterceptor(super.getMovement());
        MethodInterceptor methodInterceptor = new MethodInterceptor(super.getMovement());
        Class<?> hacHuman = new ByteBuddy()
                .subclass(entityHuman)
                .implement(EntityHumanAccessor.class)
                .name("HACEntityHuman")
                .method(ElementMatchers.isDeclaredBy(EntityHumanAccessor.class)
                        .and(ElementMatchers.isAnnotatedWith(MappingField.class)
                                .or(ElementMatchers.isAnnotatedWith(MappingField.List.class)))
                        .and(ElementMatchers.isAnnotatedWith(DeclaringClass.class)))
                .intercept(MethodDelegation.to(getterSetterInterceptor))
                .method(ElementMatchers.isDeclaredBy(EntityHumanAccessor.class)
                        .and(ElementMatchers.named("hacTick").or(ElementMatchers.named("hacSetFlag"))))
                .intercept(MethodDelegation.to(methodInterceptor))
                .method(ElementMatchers.takesArguments(int.class, boolean.class)
                        .and(ElementMatchers.named("b").or(ElementMatchers.named("setFlag"))))
                .intercept(FixedValue.originType())
                .method(ElementMatchers.named("checkMovement"))
                .intercept(FixedValue.originType())
                .method(ElementMatchers.named(getCollideMethodName()))
                .intercept(FixedValue.originType())
                .method(ElementMatchers.takesArgument(0, API.INSTANCE.getReflections().getNMSClass("SoundEffect").getRaw()))
                .intercept(FixedValue.originType())
                .method(ElementMatchers.isDeclaredBy(entityHuman)
                        .and(ElementMatchers.takesArgument(0, API.INSTANCE.getReflections().getNMSClass("Entity").getRaw()))
                        .and(ElementMatchers.takesArguments(1))
                )
                .intercept(FixedValue.originType())
                .method(ElementMatchers.isDeclaredBy(API.INSTANCE.getReflections().getNMSClass("EntityLiving").getRaw())
                        .and(ElementMatchers.takesArgument(0, double.class))
                        .and(ElementMatchers.takesArgument(1, boolean.class))
                        .and(ElementMatchers.takesArguments(4))
                )
                .intercept(MethodDelegation.to(methodInterceptor))
                .method(ElementMatchers.named("isSpectator"))
                .intercept(FixedValue.value(false))
                .make()
                .load(EntityHumanAccessor.class.getClassLoader())
                .getLoaded();

        this.humanConstructor = API.INSTANCE.getReflections().getClass(hacHuman).getConstructorAtIndex(0);
        try {
            this.foodMetaDataConstructor = API.INSTANCE.getReflections().getClass(foodMetaData).getConstructor(API.INSTANCE.getReflections().getNMSClass("EntityHuman").getRaw());
        } catch (NoSuchMethodException e) {
            super.getMovement().getLog().reportFatalError(e);
        }
    }

    @Override
    public void unload() {
        //Nothing to unload
    }
}
