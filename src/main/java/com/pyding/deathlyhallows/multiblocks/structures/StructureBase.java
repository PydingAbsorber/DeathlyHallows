package com.pyding.deathlyhallows.multiblocks.structures;

import com.pyding.deathlyhallows.multiblocks.MultiBlock;
import com.pyding.deathlyhallows.multiblocks.MultiBlockComponent;
import com.pyding.deathlyhallows.utils.IMultiBlockHandler;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class StructureBase implements IMultiBlockHandler {

	public static final MultiBlockComponent.BlockMetaRotator
			SIMPLE_ROTATOR = (meta, intAngle) -> (meta + intAngle) % 3,
			HELL_ROTATOR = (meta, intAngle) -> { // I hate forge & minecraft 1.7.10 rotations SOOOO much
				switch(intAngle & 3) {
					default:
						return meta;
					case 1:
						return ForgeDirection.ROTATION_MATRIX[1][meta];
					case 2:
						return ForgeDirection.OPPOSITES[meta];
					case 3:
						return ForgeDirection.ROTATION_MATRIX[0][meta];
				}
			},
			STAIRS_ROTATOR = (meta, intAngle) -> (HELL_ROTATOR.rotate(((meta + 2) % 4) + 2, intAngle) % 4) + 4 * (meta / 4);

	private final MultiBlock multiBlock = new MultiBlock();
	
	public StructureBase() {
		fillStructure();
	}

	protected abstract void fillStructure();

	@Override
	public MultiBlock getMultiBlock() {
		return multiBlock;
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

	protected final void add(Block block, int meta, MultiBlockComponent.BlockMetaRotator axisToRotation, ChunkCoordinates... chunks) {
		for(ChunkCoordinates pos: chunks) {
			multiBlock.addComponent(pos, block, meta, axisToRotation);
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
