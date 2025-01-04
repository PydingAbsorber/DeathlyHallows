package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.Witchery;
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
		p.openGui(Witchery.instance, WITCHERY_MARKUP_BOOK_GUI_ID, w, 0, 0, 0);
		return stack;
	}

}
