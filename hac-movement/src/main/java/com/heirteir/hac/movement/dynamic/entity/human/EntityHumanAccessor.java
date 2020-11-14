package com.heirteir.hac.movement.dynamic.entity.human;


import com.heirteir.hac.api.util.reflections.version.ServerVersion;
import com.heirteir.hac.movement.util.mapping.annotation.DeclaringClass;
import com.heirteir.hac.movement.util.mapping.annotation.MappingField;
import com.heirteir.hac.movement.util.mapping.annotation.MappingMethod;
import com.heirteir.hac.movement.util.mapping.annotation.RawType;

public interface EntityHumanAccessor {
    /* Static Mappings */

    void setLocation(double x, double y, double z, float yaw, float pitch);

    /* Getters and Setters */
    void setSprinting(boolean sprinting);

    boolean isSprinting();

    void setSneaking(boolean sneaking);

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "world")
    @RawType(Object.class)
    void setWorld(Object world);

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "justCreated")
    @RawType(boolean.class)
    void setJustCreated(boolean justCreated);

    @DeclaringClass("%nms%.EntityHuman")
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "foodData")
    @RawType(Object.class)
    void setFoodData(Object foodData);

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.GETTER, obfuscatedName = "onGround")
    @RawType(boolean.class)
    boolean getOnGround();

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "onGround")
    @RawType(boolean.class)
    void setOnGround(boolean onGround);

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.GETTER, obfuscatedName = "fallDistance")
    @RawType(float.class)
    float getFallDistance();

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "fallDistance")
    @RawType(float.class)
    void setFallDistance(float distance);

    @DeclaringClass("%nms%.EntityLiving")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.EIGHT_R1, max = ServerVersion.EIGHT_R1, obfuscatedName = "bl")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.EIGHT_R2, max = ServerVersion.EIGHT_R3, obfuscatedName = "bn")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.NINE_R1, max = ServerVersion.NINE_R1, obfuscatedName = "bA")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.NINE_R2, max = ServerVersion.NINE_R2, obfuscatedName = "bB")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.TEN_R1, max = ServerVersion.ELEVEN_R1, obfuscatedName = "bC")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.TWELVE_R1, max = ServerVersion.TWELVE_R1, obfuscatedName = "bD")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.THIRTEEN_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "bJ")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.FOURTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "jumpTicks")
    @RawType(int.class)
    void setJumpTicks(int ticks);

    @DeclaringClass("%nms%.EntityHuman")
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "abilities.isFlying")
    @RawType(boolean.class)
    void setFlying(boolean flying);

    @DeclaringClass("%nms%.EntityHuman")
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "abilities.isInvulnerable")
    @RawType(boolean.class)
    void setInvulnerable(boolean invulnerable);

    @DeclaringClass("%nms%.EntityHuman")
    @MappingField(type = MappingField.Type.GETTER, obfuscatedName = "abilities.flySpeed")
    @RawType(float.class)
    float getFlySpeed();

    @DeclaringClass("%nms%.EntityHuman")
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "abilities.flySpeed")
    @RawType(float.class)
    void setFlySpeed(float speed);
    /* Dynamic Mappings */

    void hacSetFlag(int index, boolean flag);

    @DeclaringClass("%nms%.EntityHuman")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.EIGHT_R1, max = ServerVersion.EIGHT_R1, obfuscatedName = "s_")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.EIGHT_R2, max = ServerVersion.EIGHT_R3, obfuscatedName = "t_")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.NINE_R1, max = ServerVersion.NINE_R1, obfuscatedName = "i")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.NINE_R2, max = ServerVersion.TEN_R1, obfuscatedName = "m")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.ELEVEN_R1, max = ServerVersion.ELEVEN_R1, obfuscatedName = "A_")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.TWELVE_R1, max = ServerVersion.TWELVE_R1, obfuscatedName = "B_")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.THIRTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "tick")
    @MappingMethod.Body("public void %mapped%(){" +
            "super.%obfuscated%();" +
            "}")
    @RawType(Object.class)
    void hacTick();

    /* Getters & Setters */
    @DeclaringClass("%nms%.EntityLiving")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.EIGHT_R1, max = ServerVersion.EIGHT_R1, obfuscatedName = "aW")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.EIGHT_R2, max = ServerVersion.EIGHT_R3, obfuscatedName = "aY")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.NINE_R1, max = ServerVersion.NINE_R1, obfuscatedName = "bc")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.NINE_R2, max = ServerVersion.NINE_R2, obfuscatedName = "bd")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.TEN_R1, max = ServerVersion.TEN_R1, obfuscatedName = "be")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.ELEVEN_R1, max = ServerVersion.ELEVEN_R1, obfuscatedName = "bd")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.TWELVE_R1, max = ServerVersion.TWELVE_R1, obfuscatedName = "bd")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.THIRTEEN_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "bg")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.FOURTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "jumping")
    @RawType(boolean.class)
    void setJumping(boolean jumping);

    @DeclaringClass("%nms%.EntityLiving")
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
    @RawType(float.class)
    void setStrafe(float strafe);

    @DeclaringClass("%nms%.EntityLiving")
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
    @RawType(float.class)
    void setForward(float forward);

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "locX")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "locX")
    @MappingMethod.Body(min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, value =
            "public %raw_type% %mapped%(){" +
                    "return this.%obfuscated%().x;" +
                    "}")
    @RawType(double.class)
    double getLocX();

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "locY")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "locY")
    @MappingMethod.Body(min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, value =
            "public %raw_type% %mapped%(){" +
                    "return this.%obfuscated%().y;" +
                    "}")
    @RawType(double.class)
    double getLocY();

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "locZ")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "locZ")
    @MappingMethod.Body(min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, value =
            "public %raw_type% %mapped%(){" +
                    "return this.%obfuscated%().z;" +
                    "}")
    @RawType(double.class)
    double getLocZ();

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "motX")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "getMot")
    @MappingMethod.Body(min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, value =
            "public %raw_type% %mapped%(){" +
                    "return this.%obfuscated%().x;" +
                    "}")
    @RawType(double.class)
    double getMotX();

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "motX")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "getMot")
    @MappingMethod.Body(min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, value =
            "public void %mapped%(double motX){" +
                    "this.setMot(motX, this.%obfuscated%().y, this.%obfuscated%().z);" +
                    "}")
    @RawType(double.class)
    void setMotX(double motX);

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "motY")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "getMot")
    @MappingMethod.Body(min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, value =
            "public %raw_type% %mapped%(){" +
                    "return this.%obfuscated%().y;" +
                    "}")
    @RawType(double.class)
    double getMotY();

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "motY")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "getMot")
    @MappingMethod.Body(min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, value =
            "public void %mapped%(double motY){" +
                    "this.setMot(this.%obfuscated%().x, motY, this.%obfuscated%().z);" +
                    "}")
    @RawType(double.class)
    void setMotY(double motY);

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "motZ")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "getMot")
    @MappingMethod.Body(min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, value =
            "public %raw_type% %mapped%(){" +
                    "return this.%obfuscated%().z;" +
                    "}")
    @RawType(double.class)
    double getMotZ();

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.MIN, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "motZ")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "getMot")
    @MappingMethod.Body(min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, value =
            "public void %mapped%(double motZ){" +
                    "this.setMot(this.%obfuscated%().x, this.%obfuscated%().y, motZ);" +
                    "}")
    @RawType(double.class)
    void setMotZ(double motZ);

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.MIN, max = ServerVersion.EIGHT_R3, obfuscatedName = "H")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.NINE_R1, max = ServerVersion.TWELVE_R1, obfuscatedName = "E")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.THIRTEEN_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "F")
    @RawType(boolean.class)
    boolean getInWeb();

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.MIN, max = ServerVersion.EIGHT_R3, obfuscatedName = "H")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.NINE_R1, max = ServerVersion.TWELVE_R1, obfuscatedName = "E")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.THIRTEEN_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "F")
    @RawType(boolean.class)
    void setInWeb(boolean inWeb);

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.EIGHT_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.FOURTEEN_R1, max = ServerVersion.FOURTEEN_R1, obfuscatedName = "B")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.FIFTEEN_R1, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "y")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "x")
    @RawType(Object.class)
    Object getMotOffset();

    @DeclaringClass("%nms%.Entity")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.EIGHT_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.FOURTEEN_R1, max = ServerVersion.FOURTEEN_R1, obfuscatedName = "B")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.FIFTEEN_R1, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "y")
    @MappingField(type = MappingField.Type.SETTER, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "x")
    @RawType(Object.class)
    void setMotOffset(Object offset);

    @DeclaringClass("%nms%.EntityLiving")
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
    @RawType(float.class)
    float getJumpMovementFactor();

    @DeclaringClass("%nms%.EntityLiving")
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
    @RawType(float.class)
    void setJumpMovementFactor(float factor);
}
