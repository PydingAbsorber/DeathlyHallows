package com.pyding.deathlyhallows.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class SoapWithSawdust extends ItemFood {
	public SoapWithSawdust() {
		super(13, 20, false);
	}

	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		super.onEaten(stack, world, player);
		player.addPotionEffect(new PotionEffect(Potion.confusion.id, 1200, 255));
		player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 1200, 255));
		return stack;
	}
}
