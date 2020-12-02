package com.heretere.hac.movement.dynamic.entity.human;


import com.heretere.hac.api.util.reflections.version.ServerVersion;
import com.heretere.hac.movement.util.mapping.annotation.DeclaringClass;
import com.heretere.hac.movement.util.mapping.annotation.MappingField;
import com.heretere.hac.movement.util.mapping.annotation.MappingMethod;

public interface EntityHumanAccessor {
    String ENTITY = "%nms%.Entity";
    String ENTITY_LIVING = "%nms%.EntityLiving";
    String ENTITY_HUMAN = "%nms%.EntityHuman";

    /* Static Mappings */

    void setLocation(double x, double y, double z, float yaw, float pitch);

    /* Getters and Setters */
    void setSprinting(boolean sprinting);

    boolean isSprinting();

    void setSneaking(boolean sneaking);

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "world")
    void setWorld(Object world);

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "justCreated")
    void setJustCreated(boolean justCreated);

    @DeclaringClass(EntityHumanAccessor.ENTITY_HUMAN)
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "foodData")
    void setFoodData(Object foodData);

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.GETTER, obfuscatedName = "onGround")
    boolean getOnGround();

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "onGround")
    void setOnGround(boolean onGround);

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.GETTER, obfuscatedName = "fallDistance")
    float getFallDistance();

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "fallDistance")
    void setFallDistance(float distance);

    @DeclaringClass(EntityHumanAccessor.ENTITY_LIVING)
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.EIGHT_R1, max = ServerVersion.EIGHT_R1, obfuscatedName = "bl")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.EIGHT_R2, max = ServerVersion.EIGHT_R3, obfuscatedName = "bn")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.NINE_R1, max = ServerVersion.NINE_R1, obfuscatedName = "bA")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.NINE_R2, max = ServerVersion.NINE_R2, obfuscatedName = "bB")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.TEN_R1, max = ServerVersion.ELEVEN_R1, obfuscatedName = "bC")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.TWELVE_R1, max = ServerVersion.TWELVE_R1, obfuscatedName = "bD")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.THIRTEEN_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "bJ")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.FOURTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "jumpTicks")
    void setJumpTicks(int ticks);

    @DeclaringClass(EntityHumanAccessor.ENTITY_HUMAN)
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "abilities.isFlying")
    void setFlying(boolean flying);

    @DeclaringClass(EntityHumanAccessor.ENTITY_HUMAN)
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "abilities.isInvulnerable")
    void setInvulnerable(boolean invulnerable);

    @DeclaringClass(EntityHumanAccessor.ENTITY_HUMAN)
    @MappingField(type = MappingField.Type.GETTER, obfuscatedName = "abilities.flySpeed")
    float getFlySpeed();

    @DeclaringClass(EntityHumanAccessor.ENTITY_HUMAN)
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "abilities.flySpeed")
    void setFlySpeed(float speed);
    /* Dynamic Mappings */

    void hacSetFlag(int index, boolean flag);

    @DeclaringClass(EntityHumanAccessor.ENTITY_HUMAN)
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.EIGHT_R1, max = ServerVersion.EIGHT_R1, obfuscatedName = "s_")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.EIGHT_R2, max = ServerVersion.EIGHT_R3, obfuscatedName = "t_")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.NINE_R1, max = ServerVersion.NINE_R1, obfuscatedName = "i")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.NINE_R2, max = ServerVersion.TEN_R1, obfuscatedName = "m")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.ELEVEN_R1, max = ServerVersion.ELEVEN_R1, obfuscatedName = "A_")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.TWELVE_R1, max = ServerVersion.TWELVE_R1, obfuscatedName = "B_")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.THIRTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "tick")
    void hacTick();

    /* Getters & Setters */
    @DeclaringClass(EntityHumanAccessor.ENTITY_LIVING)
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.EIGHT_R1, max = ServerVersion.EIGHT_R1, obfuscatedName = "aW")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.EIGHT_R2, max = ServerVersion.EIGHT_R3, obfuscatedName = "aY")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.NINE_R1, max = ServerVersion.NINE_R1, obfuscatedName = "bc")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.NINE_R2, max = ServerVersion.NINE_R2, obfuscatedName = "bd")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.TEN_R1, max = ServerVersion.TEN_R1, obfuscatedName = "be")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.ELEVEN_R1, max = ServerVersion.ELEVEN_R1, obfuscatedName = "bd")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.TWELVE_R1, max = ServerVersion.TWELVE_R1, obfuscatedName = "bd")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.THIRTEEN_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "bg")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.FOURTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "jumping")
    void setJumping(boolean jumping);

    @DeclaringClass(EntityHumanAccessor.ENTITY_LIVING)
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.EIGHT_R1, max = ServerVersion.EIGHT_R1, obfuscatedName = "aX")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.EIGHT_R2, max = ServerVersion.EIGHT_R3, obfuscatedName = "aZ")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.NINE_R1, max = ServerVersion.NINE_R1, obfuscatedName = "bd")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.NINE_R2, max = ServerVersion.NINE_R2, obfuscatedName = "be")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.TEN_R1, max = ServerVersion.TEN_R1, obfuscatedName = "bf")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.ELEVEN_R1, max = ServerVersion.ELEVEN_R1, obfuscatedName = "be")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.TWELVE_R1, max = ServerVersion.TWELVE_R1, obfuscatedName = "be")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.THIRTEEN_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "bh")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.FOURTEEN_R1, max = ServerVersion.FOURTEEN_R1, obfuscatedName = "bb")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.FIFTEEN_R1, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "aZ")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R1, obfuscatedName = "aY")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.SIXTEEN_R2, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "aR")
    void setStrafe(float strafe);

    @DeclaringClass(EntityHumanAccessor.ENTITY_LIVING)
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.EIGHT_R1, max = ServerVersion.EIGHT_R1, obfuscatedName = "aY")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.EIGHT_R2, max = ServerVersion.EIGHT_R3, obfuscatedName = "ba")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.NINE_R1, max = ServerVersion.NINE_R1, obfuscatedName = "be")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.NINE_R2, max = ServerVersion.NINE_R2, obfuscatedName = "bf")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.TEN_R1, max = ServerVersion.TEN_R1, obfuscatedName = "bg")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.ELEVEN_R1, max = ServerVersion.ELEVEN_R1, obfuscatedName = "bf")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.TWELVE_R1, max = ServerVersion.TWELVE_R1, obfuscatedName = "bg")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.THIRTEEN_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "bj")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.FOURTEEN_R1, max = ServerVersion.FOURTEEN_R1, obfuscatedName = "bd")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.FIFTEEN_R1, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "bb")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R1, obfuscatedName = "ba")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.SIXTEEN_R2, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "aT")
    void setForward(float forward);

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "locX")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "locX")
    double getLocX();

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "locY")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "locY")
    double getLocY();

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "locZ")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "locZ")
    double getLocZ();

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "motX")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "getMot")
    double getMotX();

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "motX")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "getMot")
    void setMotX(double motX);

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "motY")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "getMot")
    double getMotY();

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "motY")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "getMot")
    void setMotY(double motY);

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "motZ")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "getMot")
    double getMotZ();

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "motZ")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "getMot")
    void setMotZ(double motZ);

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.MIN, max = ServerVersion.EIGHT_R3, obfuscatedName = "H")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.NINE_R1, max = ServerVersion.TWELVE_R1, obfuscatedName = "E")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.THIRTEEN_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "F")
    boolean getInWeb();

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.MIN, max = ServerVersion.EIGHT_R3, obfuscatedName = "H")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.NINE_R1, max = ServerVersion.TWELVE_R1, obfuscatedName = "E")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.THIRTEEN_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "F")
    void setInWeb(boolean inWeb);

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.EIGHT_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.FOURTEEN_R1, max = ServerVersion.FOURTEEN_R1, obfuscatedName = "B")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.FIFTEEN_R1, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "y")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "x")
    Object getMotOffset();

    @DeclaringClass(EntityHumanAccessor.ENTITY)
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.EIGHT_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.FOURTEEN_R1, max = ServerVersion.FOURTEEN_R1, obfuscatedName = "B")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.FIFTEEN_R1, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "y")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "x")
    void setMotOffset(Object offset);

    @DeclaringClass(EntityHumanAccessor.ENTITY_LIVING)
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.EIGHT_R1, max = ServerVersion.EIGHT_R1, obfuscatedName = "aK")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.EIGHT_R2, max = ServerVersion.EIGHT_R3, obfuscatedName = "aM")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.NINE_R1, max = ServerVersion.NINE_R1, obfuscatedName = "aQ")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.NINE_R2, max = ServerVersion.NINE_R2, obfuscatedName = "aR")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.TEN_R1, max = ServerVersion.TEN_R1, obfuscatedName = "aS")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.ELEVEN_R1, max = ServerVersion.TWELVE_R1, obfuscatedName = "aR")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.THIRTEEN_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "aU")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.FOURTEEN_R1, max = ServerVersion.FOURTEEN_R1, obfuscatedName = "aO")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.FIFTEEN_R1, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "aM")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R1, obfuscatedName = "aL")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.SIXTEEN_R2, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "aE")
    float getJumpMovementFactor();

    @DeclaringClass(EntityHumanAccessor.ENTITY_LIVING)
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.EIGHT_R1, max = ServerVersion.EIGHT_R1, obfuscatedName = "aK")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.EIGHT_R2, max = ServerVersion.EIGHT_R3, obfuscatedName = "aM")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.NINE_R1, max = ServerVersion.NINE_R1, obfuscatedName = "aQ")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.NINE_R2, max = ServerVersion.NINE_R2, obfuscatedName = "aR")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.TEN_R1, max = ServerVersion.TEN_R1, obfuscatedName = "aS")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.ELEVEN_R1, max = ServerVersion.TWELVE_R1, obfuscatedName = "aR")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.THIRTEEN_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "aU")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.FOURTEEN_R1, max = ServerVersion.FOURTEEN_R1, obfuscatedName = "aO")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.FIFTEEN_R1, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "aM")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R1, obfuscatedName = "aL")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.SIXTEEN_R2, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "aE")
    void setJumpMovementFactor(float factor);
}
