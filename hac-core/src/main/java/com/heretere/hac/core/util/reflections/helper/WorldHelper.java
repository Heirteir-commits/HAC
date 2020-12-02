package com.heretere.hac.core.util.reflections.helper;

import com.flowpowered.math.vector.Vector3f;
import com.heretere.hac.api.API;
import com.heretere.hac.api.util.reflections.types.WrappedMethod;
import com.heretere.hac.core.Core;
import com.heretere.hac.core.util.math.Boxf;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WorldHelper extends AbstractCoreHelper<WorldHelper> {
    private WrappedMethod getHandle;
    private WrappedMethod getEntities;

    public WorldHelper(Core core) {
        super(core, WorldHelper.class);

        try {
            this.getHandle = API.INSTANCE.getReflections().getCBClass("CraftWorld").getMethod("getHandle");
            this.getEntities = API.INSTANCE.getReflections().getNMSClass("World").getMethod("getEntities",
                    API.INSTANCE.getReflections().getNMSClass("Entity").getRaw(),
                    API.INSTANCE.getReflections().getNMSClass("AxisAlignedBB").getRaw());
        } catch (NoSuchMethodException e) {
            super.getCore().getLog().reportFatalError(e);
        }
    }

    public Object getNMSWorld(World world) {
        Object output;
        try {
            output = this.getHandle.invoke(Object.class, world);
        } catch (IllegalAccessException | InvocationTargetException e) {
            output = null;
            super.getCore().getLog().reportFatalError(e);
        }
        return output;
    }

    public List<?> getEntitiesInBoundingBox(World world, Boxf boundingBox) {
        List<?> output;

        try {
            output = this.getEntities.invoke(List.class,
                    this.getNMSWorld(world),
                    null,
                    API.INSTANCE.getReflections().getHelpers().getHelper(BoundingBoxHelper.class)
                            .convertBoxftoNMS(boundingBox));
        } catch (IllegalAccessException | InvocationTargetException e) {
            output = null;
            super.getCore().getLog().reportFatalError(e);
        }
        return output;
    }

    public Set<Player> getPlayersNearLocation(Location location, float distance) {
        Boxf area = new Boxf(
                new Vector3f(location.getX() - distance, location.getY() - distance, location.getZ() - distance),
                new Vector3f(location.getX() + distance, location.getY() + distance, location.getZ() + distance)
        );

        return location.getWorld().getPlayers().stream()
                .filter(player -> area.contains(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()))
                .collect(Collectors.toSet());
    }
}
