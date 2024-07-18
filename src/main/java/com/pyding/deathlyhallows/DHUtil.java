package com.pyding.deathlyhallows;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

public class DHUtil {
	public static List<EntityLivingBase> getEntitiesAround(Entity entity, float radius, boolean self){
		List<EntityLivingBase> list = new ArrayList<>();
		for(Object o : entity.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(entity.posX - radius, entity.posY - radius, entity.posZ - radius, entity.posX + radius, entity.posY + radius, entity.posZ + radius))){
			if(o instanceof EntityLivingBase){
				if(o.equals(entity) && !self)
					continue;
				list.add((EntityLivingBase)o);
			}
		}
		return list;
	}

	public static Vec3 rayCords(EntityLivingBase entity,double distance, boolean stopOnBlocks) {
		Vec3 startVec = Vec3.createVectorHelper(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
		Vec3 lookVec = entity.getLookVec();
		Vec3 endVec = startVec.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);
		MovingObjectPosition hitResult;
		if (stopOnBlocks) {
			hitResult = entity.worldObj.rayTraceBlocks(startVec, endVec, true);
		} else {
			hitResult = entity.worldObj.func_147447_a(startVec, endVec, false, false, false);
		}
		if (hitResult != null) {
			return hitResult.hitVec;
		} else {
			return endVec;
		}
	}
}
