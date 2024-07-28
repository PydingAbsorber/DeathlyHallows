package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.ritual.RitualStep;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ElderRite {
	protected boolean canRelocate = false;

	public ElderRite() {
	}

	public abstract void addSteps(ArrayList var1, int var2);

	public ArrayList<EntityItem> getItemsInRadius(World world, int x, int y, int z, float radius) {
		float RADIUS_SQ = radius * radius;
		double midX = 0.5 + (double)x;
		double midZ = 0.5 + (double)z;
		ArrayList<EntityItem> resultList = new ArrayList();
		AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(midX - (double)radius, (double)y, midZ - (double)radius, midX + (double)radius, 1.0 + (double)y, midZ + (double)radius);
		List items = world.getEntitiesWithinAABB(EntityItem.class, bounds);
		Iterator i$ = items.iterator();

		while(i$.hasNext()) {
			Object obj = i$.next();
			EntityItem entity = (EntityItem)obj;
			if (entity.getDistanceSq(midX, (double)y, midZ) <= (double)RADIUS_SQ) {
				resultList.add(entity);
			}
		}

		return resultList;
	}

	public boolean relocatable() {
		return this.canRelocate;
	}
}
