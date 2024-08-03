package com.pyding.deathlyhallows.multiblocks.structures;

import com.pyding.deathlyhallows.multiblocks.MultiBlock;
import com.pyding.deathlyhallows.utils.IMultiBlockHandler;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;

public abstract class StructureBase implements IMultiBlockHandler {

	MultiBlock mb = new MultiBlock();

	public StructureBase() {
		fillStructure();
	}

	protected abstract void fillStructure();

	@Override
	public MultiBlock getMultiBlock() {
		return mb;
	}

	protected final void add(Block block, int meta, NBTTagCompound tag, int x, int y, int z) {
		mb.addComponent(x, y, z, block, meta, tag);
	}

	protected final void add(Block block, int meta, NBTTagCompound tag, ChunkCoordinates... chunks) {
		for(ChunkCoordinates pos: chunks) {
			mb.addComponent(pos.posX, pos.posY, pos.posZ, block, meta, tag);
		}
	}

	protected final void add(Block block, int meta, ChunkCoordinates... chunks) {
		for(ChunkCoordinates pos: chunks) {
			mb.addComponent(pos.posX, pos.posY, pos.posZ, block, meta);
		}
	}

	protected final void add(Block block, ChunkCoordinates... chunks) {
		for(ChunkCoordinates pos: chunks) {
			mb.addComponent(pos.posX, pos.posY, pos.posZ, block, -1);
		}
	}

	protected final ChunkCoordinates pos(int x, int y, int z) {
		return new ChunkCoordinates(x, y, z);
	}
	
}
