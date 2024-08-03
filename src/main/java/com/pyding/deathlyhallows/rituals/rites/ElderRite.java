package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.ritual.RitualStep;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class ElderRite {
	
	public ElderRite() {
	}

	public abstract void addSteps(ArrayList<RitualStep> steps, int idk);

	public ArrayList<EntityItem> getItemsInRadius(World world, int x, int y, int z, float radius) {
		float RADIUS_SQ = radius * radius;
		double midX = 0.5 + (double)x;
		double midZ = 0.5 + (double)z;
		ArrayList<EntityItem> resultList = new ArrayList<>();
		AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(midX - (double)radius, y, midZ - (double)radius, midX + (double)radius, 1.0 + (double)y, midZ + (double)radius);
		@SuppressWarnings("unchecked")
		List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, bounds);

		for(EntityItem entity: items) {
			if(entity.getDistanceSq(midX, y, midZ) <= RADIUS_SQ) {
				resultList.add(entity);
			}
		}

		return resultList;
	}
	
}
