package com.pyding.deathlyhallows.rituals.sacrifices;

import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.Const;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.blocks.BlockElderRitual;
import com.pyding.deathlyhallows.rituals.steps.StepBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class SacrificeLiving extends SacrificeBase {
	final Class<? extends EntityLiving> entityLivingClass;

	public SacrificeLiving(Class<? extends EntityLiving> entityLivingClass) {
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

	public void addSteps(ArrayList<RitualStep> steps, AxisAlignedBB bounds, int maxDistance) {
		steps.add(new SacrificeLiving.StepSacrificeLiving(this, bounds, maxDistance));
	}

	private static class StepSacrificeLiving extends StepBase {
		private static final int RADIUS = 16;
		private final SacrificeLiving sacrifice;
		private final AxisAlignedBB bounds;
		private final int maxDistance;

		public StepSacrificeLiving(SacrificeLiving sacrifice, AxisAlignedBB bounds, int maxDistance) {
			super(false);
			this.sacrifice = sacrifice;
			this.bounds = bounds;
			this.maxDistance = maxDistance + 1;
		}

		@Override
		public Result elderProcess(World world, int x, int y, int z, long ticks, BlockElderRitual.TileEntityCircle.ActivatedElderRitual ritual) {
			if(ticks % 20L != 0L) {
				return Result.STARTING;
			}

			@SuppressWarnings("unchecked")
			List<EntityLiving> entities = (List<EntityLiving>)world.getEntitiesWithinAABB(sacrifice.entityLivingClass, bounds);
			if(entities == null || entities.isEmpty()) {
				RiteRegistry.RiteError("witchery.rite.missinglivingsacrifice", ritual.getInitiatingPlayerName(), world);
				return Result.ABORTED_REFUND;
			}
			for(EntityLiving e: entities) {
				if(SacrificeBase.distance(x, y, z, e.posX, e.posY, e.posZ) > maxDistance) {
					continue;
				}
				if(!world.isRemote) {
					e.setDead();
					ParticleEffect.PORTAL.send(SoundEffect.RANDOM_POP, e, 1.0, 2.0, RADIUS);
				}
				return Result.COMPLETED;
			}
			RiteRegistry.RiteError("witchery.rite.missinglivingsacrifice", ritual.getInitiatingPlayerName(), world);
			return Result.ABORTED_REFUND;
		}
	}
}
