package com.pyding.deathlyhallows.integrations.thaumcraft.wand;

import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.wands.ItemWandRod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.wands.IWandRodOnUpdate;
import thaumcraft.api.wands.WandRod;

public class DHWandRod extends WandRod {

	public DHWandRod(ItemWandRod.Rods type) {
		super(type.name(), type.capacity, new ItemStack(DHItems.wandRod, 1, type.ordinal()), type.cost, new ResourceLocation(DeathlyHallows.MODID, "textures/models/wand/wand_rod_" + type.name() + ".png"));
	}

	public DHWandRod setWandUpdate(IWandRodOnUpdate onUpdate) {
		setOnUpdate(onUpdate);
		return this;
	}

}
