package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.Witchery;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemElfBook extends ItemBase {

	private static final int WITCHERY_MARKUP_BOOK_GUI_ID = 7;
	
	public ItemElfBook() {
		super("elfBook", 64);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World w, EntityPlayer p) {
		FMLNetworkHandler.openGui(p, Witchery.instance, WITCHERY_MARKUP_BOOK_GUI_ID, w, (int)p.posX, (int)p.posY, (int)p.posZ);
		return stack;
	}

}
