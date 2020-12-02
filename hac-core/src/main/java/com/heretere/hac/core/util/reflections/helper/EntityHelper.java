package com.heretere.hac.core.util.reflections.helper;

import com.heretere.hac.api.API;
import com.heretere.hac.api.util.reflections.types.WrappedConstructor;
import com.heretere.hac.api.util.reflections.types.WrappedField;
import com.heretere.hac.api.util.reflections.types.WrappedMethod;
import com.heretere.hac.api.util.reflections.version.ServerVersion;
import com.heretere.hac.core.Core;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public final class EntityHelper extends AbstractCoreHelper<EntityHelper> {
    private static final String ENTITY = "Entity";
    private static final int ELYTRA_FLYING_FLAG = 7;


    /* Player - Get */
    private WrappedMethod getHandle;
    private WrappedField playerConnection;
    /* Player - Elytra */
    private WrappedMethod getFlag;
    /* Player - Position */
    private WrappedConstructor blockPositionConstructor;
    /* Entity - BoundingBox */
    private WrappedMethod getBoundingBox;

    public EntityHelper(Core core) {
        super(core, EntityHelper.class);
        try {
            /* Player - Get */
            this.getHandle = API.INSTANCE.getReflections().getCBClass("entity.CraftEntity").getMethod("getHandle");
            this.playerConnection = API.INSTANCE.getReflections().getNMSClass("EntityPlayer").getFieldByName("playerConnection");

            /* Player - Elytra */
            if (API.INSTANCE.getReflections().getVersion().greaterThanOrEqual(ServerVersion.NINE_R1)) {
                this.getFlag = API.INSTANCE.getReflections().getNMSClass(EntityHelper.ENTITY).getMethod("getFlag", int.class);
            }

            /* Player - Position */
            this.blockPositionConstructor = API.INSTANCE.getReflections().getNMSClass("BlockPosition").getConstructor(double.class, double.class, double.class);

            /* Entity - BoundingBox */
            this.getBoundingBox = API.INSTANCE.getReflections().getNMSClass(EntityHelper.ENTITY).getMethod("getBoundingBox");
        } catch (NoSuchMethodException | NoSuchFieldException e) {
            super.getCore().getLog().reportFatalError(e);
        }
    }

    @Nullable
    public Object getBlockPosition(Player player) {
        Object output;
        try {
            output = this.blockPositionConstructor != null ? this.blockPositionConstructor.newInstance(Object.class, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()) : null;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            output = null;
            super.getCore().getLog().reportFatalError(e);
        }
        return output;
    }

    /**
     * Returns whether or not the specified player is flying using Elytra.
     *
     * @param player The Bukkit Player
     * @return true if player is flying with elytra false if not;
     */
    public boolean isElytraFlying(Player player) {
        return this.isElytraFlying(this.getNMSEntity(player));
    }

    public boolean isElytraFlying(Object nmsPlayer) {
        boolean output;

        try {
            output = this.getFlag != null && this.getFlag.invoke(Boolean.class, nmsPlayer, EntityHelper.ELYTRA_FLYING_FLAG);
        } catch (InvocationTargetException | IllegalAccessException e) {
            output = false;
            super.getCore().getLog().reportFatalError(e);
        }

        return output;
    }

    /**
     * Convert a entity object to an nms Entity object.
     *
     * @param entity The Bukkit Entity
     * @return An EntityPlayer object.
     */
    public Object getNMSEntity(Entity entity) {
        Object output;
        try {
            output = this.getHandle.invoke(Object.class, entity);
        } catch (InvocationTargetException | IllegalAccessException e) {
            output = null;
            super.getCore().getLog().reportFatalError(e);
        }
        return output;
    }

    /**
     * Retrieves the PlayerConnection from the player.
     *
     * @param player The Bukkit Player
     * @return A PlayerConnection object.
     */
    public Object getPlayerConnection(Player player) {
        Object output;
        try {
            output = this.playerConnection.get(Object.class, this.getNMSEntity(player));
        } catch (IllegalAccessException e) {
            output = null;
            super.getCore().getLog().reportFatalError(e);
        }
        return output;
    }

    public Object getBoundingBox(Entity entity) {
        return this.getBoundingBox(this.getNMSEntity(entity));
    }

    public Object getBoundingBox(Object nmsEntity) {
        Object output;
        try {
            output = this.getBoundingBox.invoke(Object.class, nmsEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            output = null;
            super.getCore().getLog().reportFatalError(e);
        }
        return output;
    }
}

