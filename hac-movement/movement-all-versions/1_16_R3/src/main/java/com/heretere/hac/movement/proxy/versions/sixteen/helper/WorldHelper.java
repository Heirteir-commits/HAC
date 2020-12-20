package com.heretere.hac.movement.proxy.versions.sixteen.helper;

import com.heretere.hac.core.util.math.vector.MutableVector3F;
import com.heretere.hac.movement.proxy.TestWorldHelper;
import com.heretere.hac.movement.proxy.util.math.box.MutableBox3F;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

import java.util.AbstractMap;
import java.util.stream.Stream;

public class WorldHelper implements TestWorldHelper {
    public Stream<MutableBox3F> getCollisions(org.bukkit.World world, MutableVector3F location, MutableBox3F box) {
        return ((ICollisionAccess) ((CraftWorld) world).getHandle())
                .b(null, new AxisAlignedBB(
                        location.getX() + box.getMin().getX(),
                        location.getY() + box.getMin().getY(),
                        location.getZ() + box.getMin().getZ(),
                        location.getX() + box.getMax().getX(),
                        location.getY() + box.getMax().getY(),
                        location.getZ() + box.getMax().getZ()
                ))
                .flatMap(shape -> shape.d().stream())
                .map(aabb -> new MutableBox3F(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ));
    }

    @Override
    public AbstractMap.SimpleImmutableEntry<org.bukkit.Material, Float> getBlockStats(org.bukkit.World world, MutableVector3F location) {
        World nmsWorld = ((CraftWorld) world).getHandle();
        BlockPosition bp = new BlockPosition(MathHelper.floor(location.getX()), MathHelper.floor(location.getY()), MathHelper.floor(location.getZ()));

        org.bukkit.Material material = org.bukkit.Material.AIR;
        float slipperiness = 0.91F;
        if (nmsWorld.isChunkLoaded(bp.getX() >> 4, bp.getZ() >> 4)) {
            IBlockData data = nmsWorld.getType(bp);
            material = data.getBukkitMaterial();
            slipperiness = data.getBlock().getFrictionFactor();
        }
        return new AbstractMap.SimpleImmutableEntry<>(material, slipperiness);
    }
}
