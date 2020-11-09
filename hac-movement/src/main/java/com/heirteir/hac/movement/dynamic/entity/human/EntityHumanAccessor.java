package com.heirteir.hac.movement.dynamic.entity.human;


import com.heirteir.hac.api.util.reflections.version.ServerVersion;
import com.heirteir.hac.movement.util.mapping.builder.annotation.DeclaringClass;
import com.heirteir.hac.movement.util.mapping.builder.annotation.MappingField;
import com.heirteir.hac.movement.util.mapping.builder.annotation.MappingMethod;
import com.heirteir.hac.movement.util.mapping.builder.annotation.RawType;

public interface EntityHumanAccessor {
    /* Static Mappings */
    @DeclaringClass("%nms%.EntityHuman")
    @MappingMethod(type = MappingMethod.Type.REPLACE, obfuscatedName = "isSpectator")
    @MappingMethod.Body(
            "public %raw_type% %mapped%() {" +
                    "return false;" +
                    "}"
    )
    @RawType(boolean.class)
    boolean isSpectator();

    void setLocation(double x, double y, double z, float yaw, float pitch);


    /* Getters and Setters */
    void setSprinting(boolean sprinting);

    void setSneaking(boolean sneaking);

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

    @DeclaringClass("%nms%.EntityHuman")
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "abilities.isFlying")
    @RawType(boolean.class)
    void setFlying(boolean flying);

    @DeclaringClass("%nms%.EntityHuman")
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "abilities.isInvulnerable")
    @RawType(boolean.class)
    void setInvulnerable(boolean invulnerable);

    @DeclaringClass("%nms%.EntityHuman")
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "abilities.flySpeed")
    @RawType(float.class)
    void setFlySpeed(float speed);
    /* Dynamic Mappings */

    @DeclaringClass("%nms%.EntityHuman")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.EIGHT_R3, max = ServerVersion.EIGHT_R3, obfuscatedName = "t_")
    @MappingMethod.Body("public void %mapped%(){" +
            "super.%obfuscated%();" +
            "}")
    @RawType(Object.class)
    void tick();

    /* Getters & Setters */

    @DeclaringClass("%nms%.EntityLiving")
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "aZ")
    @RawType(float.class)
    void setStrafe(float strafe);

    @DeclaringClass("%nms%.EntityLiving")
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "ba")
    @RawType(float.class)
    void setForward(float forward);

    @DeclaringClass("%nms%.EntityLiving")
    @MappingField(type = MappingField.Type.SETTER, obfuscatedName = "aY")
    @RawType(boolean.class)
    void setJumping(boolean jumping);

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

    @DeclaringClass("%nms%.EntityLiving")
    @MappingMethod(type = MappingMethod.Type.REPLACE, obfuscatedName = "bn")
    @MappingMethod.Body("public void %mapped%(int amount){" +
            "super.%obfuscated% = amount;" +
            "}")
    @RawType(int.class)
    void setJumpTicks(int amount);

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
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.EIGHT_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.FOURTEEN_R1, max = ServerVersion.FOURTEEN_R1, obfuscatedName = "B")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.FIFTEEN_R1, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "y")
    @MappingField(type = MappingField.Type.GETTER, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "x")
    @MappingMethod.Body(min = ServerVersion.EIGHT_R1, max = ServerVersion.THIRTEEN_R2, value =
            "public %raw_type% %mapped%(){ return null; }")
    @RawType(Object.class)
    Object getMotOffset();

    @DeclaringClass("%nms%.Entity")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.EIGHT_R1, max = ServerVersion.THIRTEEN_R2, obfuscatedName = "")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.FOURTEEN_R1, max = ServerVersion.FOURTEEN_R1, obfuscatedName = "B")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.FIFTEEN_R1, max = ServerVersion.FIFTEEN_R1, obfuscatedName = "y")
    @MappingMethod(type = MappingMethod.Type.REPLACE, min = ServerVersion.SIXTEEN_R1, max = ServerVersion.SIXTEEN_R2, obfuscatedName = "x")
    @MappingMethod.Body(min = ServerVersion.EIGHT_R1, max = ServerVersion.THIRTEEN_R2, value =
            "public void %mapped%(Object vec3d){}")
    @MappingMethod.Body(min = ServerVersion.FOURTEEN_R1, max = ServerVersion.SIXTEEN_R2, value =
            "public void %mapped%(Object vec3d){" +
                    "this.%obfuscated% = (%nms%.Vec3D) vec3d;" +
                    "}")
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
