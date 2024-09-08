package com.pyding.deathlyhallows.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public class ItemBag extends ItemBase {
	
	public static final int COOLDOWN = 500;

	public ItemBag() {
		super("bag", 64);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {

	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		NBTTagCompound tag = p.getEntityData();
		if(tag.getLong("DHBag") < System.currentTimeMillis()) {
			tag.setLong("DHBag", System.currentTimeMillis() + COOLDOWN);
		}
		return super.onItemRightClick(stack, world, p);
	}

}
