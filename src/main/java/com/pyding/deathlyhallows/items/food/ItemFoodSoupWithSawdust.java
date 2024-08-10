package com.pyding.deathlyhallows.items.food;

import com.pyding.deathlyhallows.DeathlyHallows;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemFoodSoupWithSawdust extends ItemFoodBase {

	public ItemFoodSoupWithSawdust() {
		super("soupWithSawdust", 4, 4, 1, DeathlyHallows.tabDeathlyHallows);
		setAlwaysEdible();
		setContainerItem(Items.bucket);
	}
	
	@Override
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer p) {
		if(world.isRemote) {
			return;
		}
		p.addPotionEffect(new PotionEffect(Potion.confusion.id, 480, 255));
		p.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 120, 255));
	}

	@Override
	public EnumAction getItemUseAction(ItemStack p_77661_1_) {
		return EnumAction.drink;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		return new ItemStack(Items.bucket);
	}

}
