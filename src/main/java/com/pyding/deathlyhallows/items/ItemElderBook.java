package com.pyding.deathlyhallows.items;

import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.proxy.GuiHandler;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemElderBook extends ItemBase {

	public ItemElderBook() {
		super("elderBook", 64);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World w, EntityPlayer p) {
		FMLNetworkHandler.openGui(p, DeathlyHallows.Instance, GuiHandler.GUI.ELDER_RITUALS.ordinal(), w, (int)p.posX, (int)p.posY, (int)p.posZ);
		return stack;
	}

}
