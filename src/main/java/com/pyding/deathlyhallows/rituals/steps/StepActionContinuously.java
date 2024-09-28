package com.pyding.deathlyhallows.rituals.steps;

import com.emoniph.witchery.common.IPowerSource;
import com.pyding.deathlyhallows.blocks.BlockElderRitual;
import net.minecraft.world.World;

public class StepActionContinuously extends StepAction {
	
	private final float upkeepPowerCost;
	private final int ticksToLive;
	private int ticksSoFar = 0;

	public StepActionContinuously(Action action, float upkeepPowerCost, int ticksToLive) {
		super(action);
		this.upkeepPowerCost = upkeepPowerCost;
		this.ticksToLive = ticksToLive;
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
		if(upkeepPowerCost > 0.0F) {
			IPowerSource powerSource = getPowerSource(world, x, y, z);
			if(powerSource == null) {
				return Result.ABORTED;
			}
			powerSourceCoord = powerSource.getLocation();
			if(!powerSource.consumePower(upkeepPowerCost)) {
				return Result.ABORTED;
			}
		}

		if(ticksToLive > 0 && ++ticksSoFar >= ticksToLive) {
			return Result.COMPLETED;
		}

		return action.preformAction(world, x, y, z, ticks);
	}

}
