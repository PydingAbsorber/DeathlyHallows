package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.util.Const;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.blocks.BlockElderRitual;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;

public class ElderSacrificeLiving extends ElderSacrifice {
	final Class<? extends EntityLiving> entityLivingClass;

	public ElderSacrificeLiving(Class<? extends EntityLiving> entityLivingClass) {
		this.entityLivingClass = entityLivingClass;
	}

	public void addDescription(StringBuffer sb) {
		String s = (String)EntityList.classToStringMapping.get(this.entityLivingClass);
		if(s == null) {
			s = "generic";
		}

		sb.append("§8>§0 ");
		sb.append(StatCollector.translateToLocal("entity." + s + ".name"));
		sb.append(Const.BOOK_NEWLINE);
	}

	public boolean isMatch(World world, int posX, int posY, int posZ, int maxDistance, ArrayList<Entity> entities, ArrayList<ItemStack> grassperStacks) {
		return true;
	}

	public void addSteps(ArrayList<ElderRitualStep> steps, AxisAlignedBB bounds, int maxDistance) {
		steps.add(new ElderSacrificeLiving.StepSacrificeLiving(this, bounds, maxDistance));
	}

	private static class StepSacrificeLiving extends ElderRitualStep {
		private final ElderSacrificeLiving sacrifice;
		private final AxisAlignedBB bounds;
		private final int maxDistance;

		public StepSacrificeLiving(ElderSacrificeLiving sacrifice, AxisAlignedBB bounds, int maxDistance) {
			super(false);
			this.sacrifice = sacrifice;
			this.bounds = bounds;
			this.maxDistance = maxDistance + 1;
		}


		@Override
		public Result elderProcess(World worldObj, int xCoord, int yCoord, int zCoord, long ticks, BlockElderRitual.TileEntityCircle.ActivatedElderRitual ritual) {
			if(ticks % 20L != 0L) {
				return Result.STARTING;
			}
			else {
				Iterator i$ = worldObj.getEntitiesWithinAABB(EntityLiving.class, this.bounds).iterator();

				EntityLiving entity;
				do {
					if(!i$.hasNext()) {
						RiteRegistry.RiteError("witchery.rite.missinglivingsacrifice", ritual.getInitiatingPlayerName(), worldObj);
						return Result.ABORTED_REFUND;
					}

					Object obj = i$.next();
					entity = (EntityLiving)obj;
				}
				while(!this.sacrifice.entityLivingClass.isInstance(entity) || !(ElderSacrifice.distance(xCoord, yCoord, zCoord, entity.posX, entity.posY, entity.posZ) <= (double)this.maxDistance));

				if(!worldObj.isRemote) {
					entity.setDead();
					ParticleEffect.PORTAL.send(SoundEffect.RANDOM_POP, entity, 1.0, 2.0, 16);
				}

				return Result.COMPLETED;
			}
		}
	}
}
