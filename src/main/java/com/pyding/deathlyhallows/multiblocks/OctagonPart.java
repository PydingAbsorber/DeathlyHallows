package com.pyding.deathlyhallows.multiblocks;

import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class OctagonPart {

	private final Block runeBlock;
	private final int runeMeta;
	private final int radius;

	public OctagonPart(Block runeBlock) {
		this(runeBlock, 0);
	}

	public OctagonPart(Block runeBlock, int radius) {
		this(runeBlock, -1, radius);
	}

	public OctagonPart(Block runeBlock, int runeMeta, int radius) {
		this.runeBlock = runeBlock;
		this.runeMeta = runeMeta;
		this.radius = radius;
	}

	public Block getRune() {
		return runeBlock;
	}

	public int getMeta() {
		return runeMeta;
	}

	public ChunkCoordinates[] getPositions() {
		if(radius <= 0) {
			return new ChunkCoordinates[]{new ChunkCoordinates(0, 0, 0)};
		}
		List<ChunkCoordinates> coords = new ArrayList<>();
		for(int x = -radius; x <= radius; ++x) {
			for(int z = -radius; z <= radius; ++z) {
				int absX = MathHelper.abs_int(x);
				int absZ = MathHelper.abs_int(z);
				if(absX + absZ == (3 * radius - 1) / 2 
						|| absX == radius && absZ < (radius) / 2
						|| absZ == radius && absX < (radius) / 2
				) {
					coords.add(new ChunkCoordinates(x, 0, z));
				}
			}
		}
		return coords.toArray(new ChunkCoordinates[0]);
	}

}
