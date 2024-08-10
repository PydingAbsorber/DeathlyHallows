package com.pyding.deathlyhallows.items.food;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemFoodSoupWithSawdust extends ItemFoodBase {
	
	public ItemFoodSoupWithSawdust() {
		super("soupWithSawdust",4, 4);
		setAlwaysEdible();
		setContainerItem(Items.bucket);
	}

	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer p) {
		p.addPotionEffect(new PotionEffect(Potion.confusion.id, 480, 255));
		p.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 120, 255));
		return super.onEaten(stack, world, p);
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		return new ItemStack(Items.bucket);
	}
	
}
