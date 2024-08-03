package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.ritual.RitualStep;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;

public abstract class ElderSacrifice {
	
	public ElderSacrifice() {
		
	}

	public abstract boolean isMatch(World w, int x, int y, int z, int side, ArrayList<Entity> entities, ArrayList<ItemStack> stacks);

	protected static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
		double dX = x1 - x2;
		double dY = y1 - y2;
		double dZ = z1 - z2;
		return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
	}

	public abstract void addSteps(ArrayList<RitualStep> steps, AxisAlignedBB bounds, int maxDistance);

	public abstract void addDescription(StringBuffer sb);
	
}
