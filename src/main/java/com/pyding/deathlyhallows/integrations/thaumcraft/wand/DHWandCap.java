package com.pyding.deathlyhallows.integrations.thaumcraft.wand;

import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.wands.ItemWandCap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.wands.WandCap;

public class DHWandCap extends WandCap {

	public DHWandCap(ItemWandCap.Caps type) {
		super(type.name(), type.discount, type.aspects, type.specialDiscount, new ItemStack(DHItems.wandCap, 1, type.ordinal()), type.cost);
		setTexture(new ResourceLocation(DeathlyHallows.MODID, "textures/models/wand/wand_cap_" + type.name() + ".png"));
	}

}
