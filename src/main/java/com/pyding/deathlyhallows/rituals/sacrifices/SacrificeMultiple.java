package com.pyding.deathlyhallows.rituals.sacrifices;

import com.emoniph.witchery.ritual.RitualStep;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class SacrificeMultiple extends SacrificeBase {
	private final List<SacrificeBase> sacrifices;

	public SacrificeMultiple(SacrificeBase... sacrifices) {
		this.sacrifices = Lists.newArrayList(sacrifices);
	}
	
	public void add(SacrificeBase sacrifice) {
		this.sacrifices.add(sacrifice);
	}

	public void addDescription(StringBuffer sb) {
		for(SacrificeBase sacrifice: sacrifices) {
			sacrifice.addDescription(sb);
		}

	}

	public boolean isMatch(World world, int posX, int posY, int posZ, int maxDistance, ArrayList<Entity> entities, ArrayList<ItemStack> grassperStacks) {
		for(SacrificeBase sacrifice: sacrifices) {
			if(!sacrifice.isMatch(world, posX, posY, posZ, maxDistance, entities, grassperStacks)) {
				return false;
			}
		}
		return true;
	}

	public void addSteps(ArrayList<RitualStep> steps, AxisAlignedBB bounds, int maxDistance) {
		for(SacrificeBase sacrifice: sacrifices) {
			sacrifice.addSteps(steps, bounds, maxDistance);
		}
	}
	
}
