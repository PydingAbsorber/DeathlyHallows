package com.pyding.deathlyhallows.items.food;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemFoodSoupWithSawdust extends ItemFoodBase {
	
	public ItemFoodSoupWithSawdust() {
		super("soupWithSawdust",13, 20, 64, null);
	}

	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer p) {
		p.addPotionEffect(new PotionEffect(Potion.confusion.id, 1200, 255));
		p.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 1200, 255));
		return super.onEaten(stack, world, p);
	}
	
}
