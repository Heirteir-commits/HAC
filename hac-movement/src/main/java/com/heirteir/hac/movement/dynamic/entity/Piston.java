package com.heirteir.hac.movement.dynamic.entity;

import com.flowpowered.math.vector.Vector3f;
import com.heirteir.hac.api.API;
import com.heirteir.hac.core.util.math.Boxf;
import com.heirteir.hac.core.util.reflections.helper.BoundingBoxHelper;
import com.heirteir.hac.core.util.reflections.helper.EntityHelper;
import com.heirteir.hac.core.util.reflections.helper.WorldHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Piston implements Listener {
    private final BoundingBoxHelper boundingBoxHelper;
    private final EntityHelper entityHelper;
    private final WorldHelper worldHelper;

    public Piston() {
        this.boundingBoxHelper = API.INSTANCE.getReflections().getHelpers().getHelper(BoundingBoxHelper.class);
        this.entityHelper = API.INSTANCE.getReflections().getHelpers().getHelper(EntityHelper.class);
        this.worldHelper = API.INSTANCE.getReflections().getHelpers().getHelper(WorldHelper.class);
    }

    private static float getOffset(Boxf playerBox, BlockFace direction, Boxf offsetBox) {
        switch (direction.getOppositeFace()) {
            case EAST:
                return playerBox.getMax().getX() - offsetBox.getMin().getX();
            case WEST:
                return offsetBox.getMax().getX() - playerBox.getMin().getX();
            case UP:
                return playerBox.getMax().getY() - offsetBox.getMin().getY();
            case DOWN:
                return offsetBox.getMax().getY() - playerBox.getMin().getY();
            case SOUTH:
                return playerBox.getMax().getZ() - offsetBox.getMin().getZ();
            case NORTH:
                return offsetBox.getMax().getZ() - playerBox.getMin().getZ();
            default:
                throw new NotImplementedException();
        }
    }

    private static boolean validPushBlock(Block block) {
        return block.getType().isSolid() && !block.getPistonMoveReaction().equals(PistonMoveReaction.BREAK);
    }

    @EventHandler
    public void pistonExtend(BlockPistonExtendEvent e) {
        e.getBlocks().stream()
                .filter(block -> Piston.validPushBlock(block) && !Piston.validPushBlock(block.getRelative(e.getDirection())))
                .forEach(block -> {
                    Set<Player> players = this.worldHelper.getPlayersNearLocation(block.getLocation(), 2);

                    if (players.isEmpty()) {
                        return;
                    }

                    Set<Boxf> boxes = this.boundingBoxHelper.getBlockBoundingBoxes(block, true).stream()
                            .map(boxf -> boxf.offset(e.getDirection().getModX(), e.getDirection().getModY(), e.getDirection().getModZ()))
                            .collect(Collectors.toSet());

                    players.forEach(player -> {
                        Boxf playerBoundingBox = this.boundingBoxHelper.convertNMStoBoxf(this.entityHelper.getBoundingBox(player), null);

                        double maxOffset = 0;

                        for (Boxf boundingBox : boxes) {
                            if (!boundingBox.intersects(playerBoundingBox)) {
                                continue;
                            }

                            double offset = Piston.getOffset(playerBoundingBox, e.getDirection(), boundingBox);

                            maxOffset = Math.max(maxOffset, offset);

                            if (maxOffset >= 1) {
                                break;
                            }
                        }

                        System.out.println(new Vector3f(e.getDirection().getModX() * maxOffset, e.getDirection().getModY() * maxOffset, e.getDirection().getModZ() * maxOffset));
                    });
                });
    }

}
