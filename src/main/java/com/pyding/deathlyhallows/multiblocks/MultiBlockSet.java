package com.pyding.deathlyhallows.multiblocks;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

/**
 A set of Multiblock objects for various rotations.
 */
public class MultiBlockSet {

	private final MultiBlock[] mbs;

	public MultiBlockSet(MultiBlock[] mbs) {
		this.mbs = mbs;
	}

	public MultiBlockSet(MultiBlock mb) {
		this(mb.createRotations());
	}

	public MultiBlock getForEntity(Entity e) {
		return getForRotation(e.rotationYaw);
	}

	public MultiBlock getForRotation(double rotation) {
		int facing = MathHelper.floor_double(rotation * 4.0 / 360.0 + 0.5) & 3;
		return getForIndex(facing);
	}

	public MultiBlock getForIndex(int index) {
		return mbs[Math.min(mbs.length - 1, index)];
	}

	public boolean equals(MultiBlockSet other) {
		if(other == null) {
			return false;
		}
		if(mbs.length != other.mbs.length) {
			return false;
		}
		for(int i = 0; i < mbs.length; i++) {
			if(!mbs[i].equals(other.mbs[i])) {
				return false;
			}
		}
		return true;
	}
}
