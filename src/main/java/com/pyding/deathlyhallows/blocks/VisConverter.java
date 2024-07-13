package com.pyding.deathlyhallows.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class VisConverter extends BlockContainer {
	public VisConverter() {
		super(Material.wood);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new VisConverterTile();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}
}
