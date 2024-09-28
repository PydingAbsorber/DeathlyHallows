package com.pyding.deathlyhallows.rituals.sacrifices;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import com.pyding.deathlyhallows.blocks.BlockElderRitual;
import com.pyding.deathlyhallows.rituals.steps.StepBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;

public class SacrificePower extends SacrificeBase {
	public final float powerRequired;
	public final int powerFrequencyInTicks;

	public SacrificePower(float powerRequired, int powerFrequencyInTicks) {
		this.powerRequired = powerRequired;
		this.powerFrequencyInTicks = powerFrequencyInTicks;
	}

	public void addDescription(StringBuffer sb) {
		sb.append(String.format("\n§8%s§0 %s\n", Witchery.resource("witchery.book.altarpower"), MathHelper.floor_float(this.powerRequired)));
	}

	public boolean isMatch(World world, int posX, int posY, int posZ, int maxDistance, ArrayList<Entity> entities, ArrayList<ItemStack> grassperStacks) {
		return true;
	}

	public void addSteps(ArrayList<RitualStep> steps, AxisAlignedBB bounds, int maxDistance) {
		steps.add(new SacrificePower.SacrificePowerStep(this));
	}

	private static class SacrificePowerStep extends StepBase {
		private final SacrificePower sacrifice;

		public SacrificePowerStep(SacrificePower sacrifice) {
			super(false);
			this.sacrifice = sacrifice;
		}

		@Override
		public Result elderProcess(World world, int x, int y, int z, long ticks, BlockElderRitual.TileEntityCircle.ActivatedElderRitual ritual) {
			if(ticks % (long)this.sacrifice.powerFrequencyInTicks != 0L) {
				return Result.STARTING;
			}
			IPowerSource powerSource = this.findNewPowerSource(world, x, y, z);
			if(powerSource == null) {
				RiteRegistry.RiteError("witchery.rite.missingpowersource", ritual.getInitiatingPlayerName(), world);
				return Result.ABORTED_REFUND;
			}
			else if(powerSource.consumePower(this.sacrifice.powerRequired)) {
				return Result.COMPLETED;
			}
			else {
				RiteRegistry.RiteError("witchery.rite.insufficientpower", ritual.getInitiatingPlayerName(), world);
				return Result.ABORTED_REFUND;
			}
		}
		
	}
	
}
