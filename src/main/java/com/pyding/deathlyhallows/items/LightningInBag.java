package com.pyding.deathlyhallows.items;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class LightningInBag extends Item {
	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List list, boolean p_77624_4_) {
		list.add(I18n.format("dh.light"));
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
			list.add(I18n.format("dh.light2"));
		super.addInformation(p_77624_1_, p_77624_2_, list, p_77624_4_);
	}
}
