package com.pyding.deathlyhallows.items;

import com.pyding.deathlyhallows.DeathlyHallows;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBase extends Item {

	public ItemBase(String unlocalizedName, int maxStackSize, CreativeTabs tab) {
		setUnlocalizedName(unlocalizedName);
		setTextureName(unlocalizedName);
		setMaxStackSize(maxStackSize);
		setCreativeTab(tab);
	}

	public ItemBase(String unlocalizedName, int maxStackSize) {
		this(unlocalizedName, maxStackSize, DeathlyHallows.tabDeathlyHallows);
	}

	@Override
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean devMode) {
		addTooltip(stack, player, list, devMode);
	}
	
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister ir) {
		itemIcon = ir.registerIcon(DeathlyHallows.MODID + ":" + iconString);
	}
	
}
