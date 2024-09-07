package com.pyding.deathlyhallows.integrations.thaumcraft.wand;

import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.util.Coord;
import com.pyding.deathlyhallows.items.wands.ItemWandCap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.IWandRodOnUpdate;
import thaumcraft.api.wands.WandCap;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.util.List;

public class DHWandUpdate implements IWandRodOnUpdate {

	private final float powerPerVis;

	private final int visPerOperation, operationTime, radius;

	public DHWandUpdate(float powerPerVis, int visPerOperation, int operationTime, int radius) {
		this.powerPerVis = powerPerVis;
		this.visPerOperation = visPerOperation;
		this.operationTime = operationTime;
		this.radius = radius;
	}

	@Override
	public void onUpdate(ItemStack stack, EntityPlayer p) {
		if(p.ticksExisted % operationTime != 0) {
			return;
		}
		float localCost = powerPerVis;
		ItemWandCasting api = ((ItemWandCasting)stack.getItem());
		WandCap caps = api.getCap(stack);
		if(!shouldCharge(stack, api)) {
			return;
		}
		if(caps.getTag().equals(ItemWandCap.Caps.koboldite.name())) {
			localCost *= 0.75;
		}
		IPowerSource altar = findClosestPowerSource(p, radius);
		if(altar == null || altar.getCurrentPower() < localCost) {
			return;
		}
		for(Aspect aspect: api.getAspectsWithRoom(stack).getAspects()) {
			if(altar.getCurrentPower() < localCost) {
				return;
			}
			api.addRealVis(stack, aspect, visPerOperation, true);
			altar.consumePower(localCost);
		}
	}

	public static IPowerSource findClosestPowerSource(Entity e, int radius) {
		List<PowerSources.RelativePowerSource> sources = PowerSources.instance() != null ? PowerSources.instance().get(e.worldObj, new Coord((int)e.posX, (int)e.posY, (int)e.posZ), radius) : null;
		return sources != null && sources.size() > 0 ? sources.get(0).source() : null;
	}

	private static boolean shouldCharge(ItemStack stack, ItemWandCasting api) {
		for(Aspect primal : Aspect.getPrimalAspects()) {
			if(api.getVis(stack, primal) < api.getMaxVis(stack)) {
				return true;
			}
		}
		return false;
	}

}
