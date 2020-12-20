package com.heretere.hac.movement.proxy;

import com.heretere.hac.core.util.math.vector.MutableVector3F;
import com.heretere.hac.movement.proxy.util.math.box.MutableBox3F;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.AbstractMap;
import java.util.stream.Stream;

public interface TestWorldHelper {
    Stream<MutableBox3F> getCollisions(World world, MutableVector3F location, MutableBox3F box);

    AbstractMap.SimpleImmutableEntry<Material, Float> getBlockStats(World world, MutableVector3F location);
}
