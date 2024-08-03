package com.pyding.deathlyhallows.multiblocks.structures;

import com.pyding.deathlyhallows.multiblocks.MultiBlock;
import com.pyding.deathlyhallows.utils.IMultiBlockHandler;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;

public abstract class StructureBase implements IMultiBlockHandler {

	private final MultiBlock multiBlock = new MultiBlock();

	public StructureBase() {
		fillStructure();
	}

	protected abstract void fillStructure();

	@Override
	public MultiBlock getMultiBlock() {
		return multiBlock;
	}

	protected final void add(Block block, int meta, NBTTagCompound tag, int x, int y, int z) {
		multiBlock.addComponent(x, y, z, block, meta, tag);
	}

	protected final void add(Block block, int meta, int x, int y, int z) {
		multiBlock.addComponent(x, y, z, block, meta);
	}

	protected final void add(Block block, int x, int y, int z) {
		multiBlock.addComponent(x, y, z, block, -1);
	}

	protected final void add(Block block, int meta, NBTTagCompound tag, ChunkCoordinates... chunks) {
		for(ChunkCoordinates pos: chunks) {
			multiBlock.addComponent(pos, block, meta, tag);
		}
	}

	protected final void add(Block block, int meta, ChunkCoordinates... chunks) {
		for(ChunkCoordinates pos: chunks) {
			multiBlock.addComponent(pos, block, meta);
		}
	}

	protected final void add(Block block, ChunkCoordinates... chunks) {
		for(ChunkCoordinates pos: chunks) {
			multiBlock.addComponent(pos, block, -1);
		}
	}

	protected final ChunkCoordinates pos(int x, int y, int z) {
		return new ChunkCoordinates(x, y, z);
	}
	
}
