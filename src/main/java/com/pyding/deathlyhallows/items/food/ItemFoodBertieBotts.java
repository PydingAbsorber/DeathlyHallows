package com.pyding.deathlyhallows.items.food;

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
		super("bertieBotts", 8, 20);
		setAlwaysEdible();
	}

	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer p) {
		addBuff(p, world);
	}

	public static void addBuff(EntityPlayer p, World world) {
		if(world.isRemote) {
			return;
		}
		int bounds = Potion.potionTypes.length;
		int random = itemRand.nextInt(bounds) + 1;
		try {
			while(isBlackListed(random)) {
				random = itemRand.nextInt(bounds) + 1;
			}
			Potion potion = Potion.potionTypes[random];
			if(potion != null) {
				p.addPotionEffect(new PotionEffect(random, (1 + itemRand.nextInt(15)) * 200, itemRand.nextInt(4)));
			}
			else {
				blackList.add(random);
				// TODO maybe a lil explosion would be better than recursion.
				addBuff(p, world);
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
