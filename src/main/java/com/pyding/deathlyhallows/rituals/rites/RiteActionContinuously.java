package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.ritual.RitualStep;
import com.pyding.deathlyhallows.blocks.BlockElderRitual;
import com.pyding.deathlyhallows.rituals.steps.StepBase;
import net.minecraft.world.World;

import java.util.ArrayList;

public class RiteActionContinuously extends RiteAction {
	private final float upkeepPowerCost;
	private final int ticksToLive;

	public RiteActionContinuously(Action action, float upkeepPowerCost, int ticksToLive) {
		super(action);
		this.upkeepPowerCost = upkeepPowerCost;
		this.ticksToLive = ticksToLive;
	}

	public void addSteps(ArrayList<RitualStep> steps, int initialStage) {
		steps.add(new StepUpkeep(this, initialStage));
	}

	private static class StepUpkeep extends StepBase {
		private final RiteActionContinuously rite;
		protected int ticksSoFar;

		public StepUpkeep(RiteActionContinuously rite, int ticksSoFar) {
			super(false);
			this.rite = rite;
			this.ticksSoFar = ticksSoFar;
		}

		public int getCurrentStage() {
			return this.ticksSoFar;
		}

		@Override
		public Result elderProcess(World world, int x, int y, int z, long ticks, BlockElderRitual.TileEntityCircle.ActivatedElderRitual ritual) {
			if(ticks % 20L != 0L) {
				return Result.STARTING;
			}

			if(world.isRemote) {
				return Result.UPKEEP;
			}
			if(rite.upkeepPowerCost > 0.0F) {
				IPowerSource powerSource = getPowerSource(world, x, y, z);
				if(powerSource == null) {
					return Result.ABORTED;
				}
				powerSourceCoord = powerSource.getLocation();
				if(!powerSource.consumePower(rite.upkeepPowerCost)) {
					return Result.ABORTED;
				}
			}

			if(rite.ticksToLive > 0 && ++ticksSoFar >= rite.ticksToLive) {
				return Result.COMPLETED;
			}
			
			return rite.action.preformAction(rite, world, x, y, z, ticks);
		}

	}

}
