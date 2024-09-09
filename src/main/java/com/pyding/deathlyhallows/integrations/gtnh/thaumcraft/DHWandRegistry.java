package com.pyding.deathlyhallows.integrations.gtnh.thaumcraft;

import com.gtnewhorizons.tcwands.api.GTTier;
import com.gtnewhorizons.tcwands.api.IWandRegistry;
import com.gtnewhorizons.tcwands.api.TCWandAPI;
import com.gtnewhorizons.tcwands.api.WandRecipeCreator;
import com.gtnewhorizons.tcwands.api.wrappers.CapWrapper;
import com.pyding.deathlyhallows.items.wands.ItemWandCap.Caps;
import com.pyding.deathlyhallows.items.wands.ItemWandRod.Rods;

import static com.pyding.deathlyhallows.items.wands.ItemWandCap.Caps.cotton;
import static com.pyding.deathlyhallows.items.wands.ItemWandCap.Caps.koboldite;
import static com.pyding.deathlyhallows.items.wands.ItemWandRod.Rods.alder;
import static com.pyding.deathlyhallows.items.wands.ItemWandRod.Rods.hawthorn;
import static com.pyding.deathlyhallows.items.wands.ItemWandRod.Rods.rowan;

public final class DHWandRegistry implements IWandRegistry {

	public static final DHWandRegistry INSTANCE = new DHWandRegistry();

	private DHWandRegistry() {

	}
	
	public static void init() {
		TCWandAPI.addRegistry(DHWandRegistry.INSTANCE);
	}

	@Override
	public void register() {
		core(rowan).regWandRecipe(115, 15, GTTier.EV).regSceptreRecipe(1.1F);
		core(alder).regWandRecipe(115, 15, GTTier.EV).regSceptreRecipe(1.1F);
		core(hawthorn).regWandRecipe(115, 15, GTTier.EV).regSceptreRecipe(1.1F);
		caps(koboldite, 7);
		caps(cotton, 0);
	}

	private static WandRecipeCreator core(Rods rod) {
		return new WandRecipeCreator(rod.name());
	}

	private static void caps(Caps caps, int cost) {
		TCWandAPI.regCap(new CapWrapper(caps.name(), cost));
	}

}
