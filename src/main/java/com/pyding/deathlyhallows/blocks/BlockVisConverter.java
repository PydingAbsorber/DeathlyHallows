package com.pyding.deathlyhallows.blocks;

import com.pyding.deathlyhallows.blocks.tiles.TileEntityVisConverter;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import static com.pyding.deathlyhallows.DeathlyHallows.tabDeathlyHallows;

public class BlockVisConverter extends BlockBase implements ITileEntityProvider {
	
	public BlockVisConverter() {
		super("visConverter", Material.wood, tabDeathlyHallows);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityVisConverter();
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
