package com.pyding.deathlyhallows.blocks;

import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.blocks.tiles.TileEntityVisConverter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import static com.pyding.deathlyhallows.DeathlyHallows.tabDeathlyHallows;

public class BlockVisConverter extends BlockContainer {
	public BlockVisConverter() {
		super(Material.wood);
		setBlockName("visConverter");
		setBlockTextureName("visConverter");
		setCreativeTab(tabDeathlyHallows);
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

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister ir) {
		blockIcon = ir.registerIcon(DeathlyHallows.MODID + ":" + textureName);
	}
	
}
