package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.ritual.Sacrifice;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;

public class ElderSacrificeMultiple extends ElderSacrifice{
	private final ElderSacrifice[] sacrifices;

	public ElderSacrificeMultiple(ElderSacrifice... sacrifices) {
		this.sacrifices = sacrifices;
	}

	public void addDescription(StringBuffer sb) {
		ElderSacrifice[] arr$ = this.sacrifices;
		int len$ = arr$.length;

		for(int i$ = 0; i$ < len$; ++i$) {
			ElderSacrifice sacrifice = arr$[i$];
			sacrifice.addDescription(sb);
		}

	}

	public boolean isMatch(World world, int posX, int posY, int posZ, int maxDistance, ArrayList<Entity> entities, ArrayList<ItemStack> grassperStacks) {
		ElderSacrifice[] arr$ = this.sacrifices;
		int len$ = arr$.length;

		for(int i$ = 0; i$ < len$; ++i$) {
			ElderSacrifice sacrifice = arr$[i$];
			if (!sacrifice.isMatch(world, posX, posY, posZ, maxDistance, entities, grassperStacks)) {
				return false;
			}
		}

		return true;
	}

	public void addSteps(ArrayList<ElderRitualStep> steps, AxisAlignedBB bounds, int maxDistance) {
		ElderSacrifice[] arr$ = this.sacrifices;
		int len$ = arr$.length;

		for(int i$ = 0; i$ < len$; ++i$) {
			ElderSacrifice sacrifice = arr$[i$];
			sacrifice.addSteps(steps, bounds, maxDistance);
		}

	}
}
