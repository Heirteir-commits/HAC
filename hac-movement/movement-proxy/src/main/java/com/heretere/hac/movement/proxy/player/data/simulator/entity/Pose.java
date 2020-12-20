package com.heretere.hac.movement.proxy.player.data.simulator.entity;

import com.heretere.hac.movement.proxy.util.math.box.MutableBox3F;

public class Pose {
    private State state;
    private MutableBox3F currentBox;

    public Pose() {
        this.updateState(State.STANDING);
    }

    public void updateState(State state) {
        if (this.state != state) {
            this.state = state;
            this.currentBox = this.state.getBoxCopy();
        }
    }

    public State getState() {
        return this.state;
    }

    public MutableBox3F getBox() {
        return currentBox;
    }

    public enum State {
        STANDING(new MutableBox3F(
                -0.3,
                0,
                -0.3,
                0.3,
                1.8,
                0.3
        )),
        SNEAKING(new MutableBox3F(
                -0.3,
                0,
                -0.3,
                0.3,
                1.65,
                0.3
        ));

        private final MutableBox3F box;

        State(MutableBox3F box) {this.box = box;}

        public MutableBox3F getBoxCopy() {
            return this.box.copy();
        }
    }
}
