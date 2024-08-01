package com.pyding.deathlyhallows.rituals.rites;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;

public abstract class ElderSacrifice {
	public ElderSacrifice() {
	}

	public abstract boolean isMatch(World var1, int var2, int var3, int var4, int var5, ArrayList<Entity> var6, ArrayList<ItemStack> var7);

	protected static double distance(double firstX, double firstY, double firstZ, double secondX, double secondY, double secondZ) {
		double dX = firstX - secondX;
		double dY = firstY - secondY;
		double dZ = firstZ - secondZ;
		double distance = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
		return distance;
	}

	public void addSteps(ArrayList<ElderRitualStep> steps, AxisAlignedBB bounds, int maxDistance) {
	}

	public void addDescription(StringBuffer sb) {
	}
}
