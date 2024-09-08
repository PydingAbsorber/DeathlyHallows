package com.pyding.deathlyhallows.core;

import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.pyding.deathlyhallows.items.wands.ItemWandCap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldProviderSurface;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.wands.ItemWandCasting;

public final class DHThaumcraftHooks {
	
	private DHThaumcraftHooks() {
		
	}

	public static float thaumcraftWandVisDiscountPost(float modifier, ItemWandCasting api, ItemStack stack, EntityPlayer p, Aspect aspect, boolean crafting) {
		if(api.getCap(stack).getTag().equals(ItemWandCap.Caps.koboldite.name()) 
				&& p.worldObj.provider instanceof WorldProviderSurface
				&& aspect != Aspect.EARTH
		) {
			final double upperBound = 64, lowerBound = 8D;
			modifier -= (MathHelper.clamp_double((upperBound - p.posY) / (upperBound - lowerBound), 0D, 1D)) / 10D;
		}
		return modifier;
	}

	public static float thaumcraftWandVisDiscountUnlimited(float modifier, ItemWandCasting api, ItemStack stack, EntityPlayer p, Aspect aspect, boolean crafting) {
		if(api.getCap(stack).getTag().equals(ItemWandCap.Caps.cotton.name()) 
				&& p.worldObj.provider instanceof WorldProviderDreamWorld
		) {
			return 0F; // totally free
		}
		return modifier;
	}
	
}
