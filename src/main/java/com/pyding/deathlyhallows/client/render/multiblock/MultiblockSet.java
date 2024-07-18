package com.pyding.deathlyhallows.client.render.multiblock;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

/**
 * A set of Multiblock objects for various rotations.
 */
public class MultiblockSet {

    private final Multiblock[] mbs;

    public MultiblockSet(Multiblock[] mbs) {
        this.mbs = mbs;
    }

    public MultiblockSet(Multiblock mb) {
        this(mb.createRotations());
    }

    public Multiblock getForEntity(Entity e) {
        return getForRotation(e.rotationYaw);
    }

    public Multiblock getForRotation(double rotation) {
        int facing = MathHelper.floor_double(rotation * 4.0 / 360.0 + 0.5) & 3;
        return getForIndex(facing);
    }

    public Multiblock getForIndex(int index) {
        return mbs[Math.min(mbs.length - 1, index)];
    }

    public boolean equals(MultiblockSet other) {
        if(other == null) return false;
        if(mbs.length!=other.mbs.length) return false;
        for (int i = 0; i < mbs.length; i++) {
            if (!mbs[i].equals(other.mbs[i])) {
                return false;
            }
        }
        return true;
    }
}
