package com.pyding.deathlyhallows.core;

import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.dimension.WorldProviderTorment;
import com.pyding.deathlyhallows.integrations.thaumcraft.DHThaumcraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldProviderSurface;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.WandCap;
import thaumcraft.common.items.wands.ItemWandCasting;

public final class DHThaumcraftHooks {

	private DHThaumcraftHooks() {

	}

	public static float thaumcraftWandVisDiscountPost(float modifier, ItemWandCasting api, ItemStack stack, EntityPlayer p, Aspect aspect, boolean crafting) {
		WandCap caps = api.getCap(stack);
		if(caps == DHThaumcraft.wandCapKoboldite
				&& p.worldObj.provider instanceof WorldProviderSurface
				&& aspect != Aspect.EARTH
		) {
			final double upperBound = 64, lowerBound = 8D;
			modifier -= (MathHelper.clamp_double((upperBound - p.posY) / (upperBound - lowerBound), 0D, 1D)) / 10D;
		}
		if(caps == DHThaumcraft.wandCapCotton) {
			if(p.worldObj.provider instanceof WorldProviderDreamWorld) {
				if(WorldProviderDreamWorld.getPlayerHasNightmare(p) == 1) {
					modifier -= 1.1F; // nightmare
				}
				else {
					modifier -= 1.4F; // good dream
				}
			}
			if(p.worldObj.provider instanceof WorldProviderTorment) {
				modifier -= 1.4F; // tormentum
			}
		}
		return modifier;
	}

	public static float thaumcraftWandVisDiscountUnlimited(float modifier, ItemWandCasting api, ItemStack stack, EntityPlayer p, Aspect aspect, boolean crafting) {
		if(api.getCap(stack) == DHThaumcraft.wandCapCotton
				&& p.worldObj.provider instanceof WorldProviderDreamWorld
				&& WorldProviderDreamWorld.getPlayerHasNightmare(p) == 2
		) {
			return 0.0F; // demonic nightmare
		}
		return modifier;
	}

}
