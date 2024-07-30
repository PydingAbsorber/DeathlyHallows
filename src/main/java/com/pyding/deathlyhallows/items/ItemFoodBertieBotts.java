package com.pyding.deathlyhallows.items;

import com.pyding.deathlyhallows.DeathlyHallows;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ItemFoodBertieBotts extends ItemFoodBase {

	private static final List<Integer> blackList = new ArrayList<>();
	
	public ItemFoodBertieBotts() {
		super("BertieBotts", 8, 20);
		setAlwaysEdible();
	}

	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		super.onEaten(stack, world, player);
		addBuff(player, world);
		return stack;
	}

	public static void addBuff(EntityPlayer player, World world) {
		if(world.isRemote) {
			return;
		}
		int bounds = 126;
		int random = itemRand.nextInt(bounds) + 1;
		int random2 = itemRand.nextInt(4) + 1;
		try {
			while(isBlackListed(random)) {
				random = itemRand.nextInt(bounds) + 1;
			}
			Potion potion = Potion.potionTypes[random];
			if(potion != null) {
				player.addPotionEffect(new PotionEffect(random, random2 * 600, random2));
			}
			else {
				blackList.add(random);
				// TODO maybe a lil explosion would be better than recursion.
				addBuff(player, world);
			}
		}
		catch(NullPointerException e) {
			DeathlyHallows.LOG.error(e);
			e.printStackTrace();
		}
	}

	public static boolean isBlackListed(int id) {
		for(Integer integer: blackList) {
			if(id == integer) {
				return true;
			}
		}
		return false;
	}
	
	public static void addToBlackList(int potionID) {
		blackList.add(potionID);
	}

	public static int[] getDefaultBlackList() {
		// guarantees immutability
		return new int[]{0, 28, 27, 107};
	}

}
