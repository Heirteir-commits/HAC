package com.heirteir.hac.core.util.reflections.helper;

import com.flowpowered.math.vector.Vector3f;
import com.google.common.collect.Lists;
import com.heirteir.hac.api.API;
import com.heirteir.hac.api.util.reflections.Reflections;
import com.heirteir.hac.api.util.reflections.types.WrappedClass;
import com.heirteir.hac.api.util.reflections.types.WrappedConstructor;
import com.heirteir.hac.api.util.reflections.types.WrappedField;
import com.heirteir.hac.api.util.reflections.types.WrappedMethod;
import com.heirteir.hac.core.Core;
import com.heirteir.hac.core.util.math.Boxf;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BoundingBoxHelper extends AbstractCoreHelper<BoundingBoxHelper> {
    /* nms.BlockPosition */
    private WrappedField ZERO;
    private WrappedConstructor blockPositionConstructor;

    /* nms.AxisAlignedBB */
    private WrappedConstructor bbConstructorBlockPosition;
    private WrappedConstructor bbConstructorDoubles;
    private WrappedField minX;
    private WrappedField minY;
    private WrappedField minZ;
    private WrappedField maxX;
    private WrappedField maxY;
    private WrappedField maxZ;

    /* nms.World */
    private WrappedMethod getHandle;
    private WrappedMethod getType;

    /* nms.IBlockProperties */
    private WrappedMethod populateBoundingBoxes;

    public BoundingBoxHelper(Core core) {
        super(core, BoundingBoxHelper.class);

        Reflections reflections = API.INSTANCE.getReflections();

        try {
            /* nms.BlockPosition */
            WrappedClass blockPosition = reflections.getNMSClass("BlockPosition");
            this.ZERO = blockPosition.getFieldByName("ZERO");
            this.blockPositionConstructor = blockPosition.getConstructor(double.class, double.class, double.class);

            /* nms.AxisAlignedBB */
            WrappedClass axisAlignedBB = reflections.getNMSClass("AxisAlignedBB");
            this.bbConstructorBlockPosition = axisAlignedBB.getConstructor(blockPosition.getRaw());
            this.bbConstructorDoubles = axisAlignedBB.getConstructor(double.class, double.class, double.class,
                    double.class, double.class, double.class);
            this.minX = axisAlignedBB.getFieldByType(double.class, 0);
            this.minY = axisAlignedBB.getFieldByType(double.class, 1);
            this.minZ = axisAlignedBB.getFieldByType(double.class, 2);
            this.maxX = axisAlignedBB.getFieldByType(double.class, 3);
            this.maxY = axisAlignedBB.getFieldByType(double.class, 4);
            this.maxZ = axisAlignedBB.getFieldByType(double.class, 5);

            /* nms.World */
            WrappedClass world = reflections.getNMSClass("World");
            this.getHandle = reflections.getCBClass("CraftWorld").getMethod("getHandle");
            this.getType = world.getMethod("getType", blockPosition.getRaw());

            /* nms.IBlockProperties */
            this.populateBoundingBoxes = reflections.getNMSClass("IBlockProperties")
                    .getMethodByParams(
                            world.getRaw(),
                            blockPosition.getRaw(),
                            axisAlignedBB.getRaw(),
                            List.class,
                            reflections.getNMSClass("Entity").getRaw(),
                            boolean.class
                    );
        } catch (NoSuchMethodException | NoSuchFieldException e) {
            super.getCore().getLog().reportFatalError(e);
        }
    }

    public Boxf convertNMStoBoxf(Object axisAlignedBB, @Nullable Location location) {
        Boxf output;

        double locX;
        double locY;
        double locZ;

        if (location == null) {
            locX = 0;
            locY = 0;
            locZ = 0;
        } else {
            locX = location.getX();
            locY = location.getY();
            locZ = location.getZ();
        }

        try {
            output = new Boxf(
                    new Vector3f(
                            locX + this.minX.get(Double.class, axisAlignedBB),
                            locY + this.minY.get(Double.class, axisAlignedBB),
                            locZ + this.minZ.get(Double.class, axisAlignedBB)
                    ),
                    new Vector3f(
                            locX + this.maxX.get(Double.class, axisAlignedBB),
                            locY + this.maxY.get(Double.class, axisAlignedBB),
                            locZ + this.maxZ.get(Double.class, axisAlignedBB)
                    )
            );
        } catch (IllegalAccessException e) {
            output = null;
            super.getCore().getLog().reportFatalError(e);
        }

        return output;
    }

    public Object convertBoxftoNMS(Boxf box) {
        Object output;

        try {
            output = this.bbConstructorDoubles.newInstance(Object.class,
                    box.getMin().getX(),
                    box.getMin().getY(),
                    box.getMin().getZ(),
                    box.getMax().getX(),
                    box.getMax().getY(),
                    box.getMax().getZ()
            );
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            output = null;
            super.getCore().getLog().reportFatalError(e);
        }
        return output;
    }

    public Set<Boxf> getBlockBoundingBoxes(Block block, boolean applyLocation) {
        List<Object> boundingBoxes = Lists.newArrayList();
        try {
            Object world = this.getHandle.invoke(Object.class, block.getWorld());
            this.populateBoundingBoxes.invoke(void.class,
                    this.getType.invoke(
                            Object.class,
                            world,
                            this.blockPositionConstructor.newInstance(Object.class, block.getX(), block.getY(), block.getZ())
                    ),
                    world,
                    this.ZERO.get(Object.class, null),
                    this.bbConstructorBlockPosition.newInstance(Object.class, this.ZERO.get(Object.class, null)),
                    boundingBoxes,
                    null,
                    true
            );
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            super.getCore().getLog().reportFatalError(e);
        }

        return boundingBoxes.stream()
                .map(bb -> this.convertNMStoBoxf(bb, applyLocation ? block.getLocation() : null))
                .collect(Collectors.toSet());
    }
}
