package com.heretere.hac.movement.player.data.simulator;

import com.heretere.hac.api.player.HACPlayer;
import com.heretere.hac.movement.proxy.MovementVersionProxy;

public class Simulator {
    private final MovementVersionProxy proxy;
    private final HACPlayer player;

//    private final SimulatorEntity entity;
//    private final SimulatorEntity.Data data;

    public Simulator(
        MovementVersionProxy proxy,
        HACPlayer player
    ) {
        this.proxy = proxy;
        this.player = player;

//        this.entity = new SimulatorEntity(this.proxy, player);
//        this.data = new SimulatorEntity.Data();
//
//        PlayerData data = player.getDataManager().getData(PlayerData.class);
//        this.data.setLocation(
//                data.getCurrent().getLocation().getX(),
//                data.getCurrent().getLocation().getY(),
//                data.getCurrent().getLocation().getZ()
//        );
    }

    public void update() {
//        this.data.setWorld(this.player.getBukkitPlayer().getWorld());
//        this.data.setOnGround(this.player.getDataManager().getData(PlayerData.class).getCurrent().isOnGround());
//
//        SimulatorEntity.Data prev = this.data.copy();
//
//        this.entity.tick(this.data);
    }

}
