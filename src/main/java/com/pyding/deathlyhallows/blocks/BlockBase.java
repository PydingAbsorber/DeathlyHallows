package com.pyding.deathlyhallows.blocks;

import com.pyding.deathlyhallows.DeathlyHallows;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;

public class BlockBase extends Block {
	
	protected BlockBase(String name, Material mat, CreativeTabs tab) {
		super(mat);
		setBlockName(name);
		setBlockTextureName(name);
		setCreativeTab(tab);
		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister ir) {
		blockIcon = ir.registerIcon(DeathlyHallows.MODID + ":" + textureName);
	}
	
}
