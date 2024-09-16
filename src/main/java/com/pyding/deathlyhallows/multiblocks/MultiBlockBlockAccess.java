package com.pyding.deathlyhallows.multiblocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class MultiBlockBlockAccess implements IBlockAccess {

	protected IBlockAccess originalBlockAccess;
	protected MultiBlock multiblock;
	protected int anchorX, anchorY, anchorZ;
	protected boolean hasBlockAccess = false;

	@Override
	public Block getBlock(int x, int y, int z) {
		MultiBlockComponent comp = getComponent(x, y, z);
		if(comp == null) {
			return hasBlockAccess ? originalBlockAccess.getBlock(x, y, z) : Blocks.air;
		}
		return comp.getBlock();
	}

	@Override
	public TileEntity getTileEntity(int x, int y, int z) {
		MultiBlockComponent comp = getComponent(x, y, z);
		if(comp == null) {
			return hasBlockAccess ? originalBlockAccess.getTileEntity(x, y, z) : null;
		}
		return comp.getTileEntity();
	}

	@Override
	public int getLightBrightnessForSkyBlocks(int x, int y, int z, int light) {
		return hasBlockAccess? originalBlockAccess.getLightBrightnessForSkyBlocks(x, y, z, light) :0xF00000;
	}

	@Override
	public int getBlockMetadata(int x, int y, int z) {
		MultiBlockComponent comp = getComponent(x, y, z);
		if(comp == null) {
			return hasBlockAccess ? originalBlockAccess.getBlockMetadata(x, y, z) : 0;
		}
		return comp.getMeta();
	}

	@Override
	public int isBlockProvidingPowerTo(int x, int y, int z, int direction) {
		return 0;
	}

	@Override
	public boolean isAirBlock(int x, int y, int z) {
		return getComponent(x, y, z) == null && (!hasBlockAccess || originalBlockAccess.isAirBlock(x, y, z));
	}

	@Override
	public BiomeGenBase getBiomeGenForCoords(int x, int z) {
		return hasBlockAccess ? originalBlockAccess.getBiomeGenForCoords(x, z) : null;
	}

	@Override
	public int getHeight() {
		return hasBlockAccess ? originalBlockAccess.getHeight() : 256;
	}

	@Override
	public boolean extendedLevelsInChunkCache() {
		return hasBlockAccess && originalBlockAccess.extendedLevelsInChunkCache();
	}

	@Override
	public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
		return hasBlockAccess ? originalBlockAccess.isSideSolid(x, y, z, side, _default) : _default;
	}

	public void update(IBlockAccess access, MultiBlock mb, int anchorX, int anchorY, int anchorZ) {
		multiblock = mb;
		this.anchorX = anchorX;
		this.anchorY = anchorY;
		this.anchorZ = anchorZ;
		setOriginal(access);
	}

	protected MultiBlockComponent getComponent(int x, int y, int z) {
		return multiblock.getComponentForLocation(x - anchorX, y - anchorY, z - anchorZ);
	}

	public boolean hasBlockAccess() {
		return hasBlockAccess;
	}

	public void setOriginal(IBlockAccess originalBlockAccess) {
		this.originalBlockAccess = originalBlockAccess;
		hasBlockAccess = originalBlockAccess != null;
	}
	
}
