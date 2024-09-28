package com.pyding.deathlyhallows.rituals.steps;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;

public class StepCheckCoven extends RitualStep {

	private final int convenRequired;

	public StepCheckCoven(int convenRequired) {
		super(false);
		this.convenRequired = convenRequired;
	}

	@Override
	public Result process(final World world, final int posX, final int posY, final int posZ, final long ticks, final BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
		if(ticks % 20L != 0L) {
			return Result.STARTING;
		}

		if(!world.isRemote) {
			if(ritual.covenSize >= convenRequired) {
				return Result.COMPLETED;
			}
		}

		RiteRegistry.RiteError(I18n.format("witchery.rite.missingcoven", convenRequired - ritual.covenSize), ritual.getInitiatingPlayerName(), world);
		return Result.ABORTED_REFUND;
	}
}
