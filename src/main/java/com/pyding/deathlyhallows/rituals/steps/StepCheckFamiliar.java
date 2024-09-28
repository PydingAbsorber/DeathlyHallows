package com.pyding.deathlyhallows.rituals.steps;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class StepCheckFamiliar extends RitualStep {
	private final String familiar;

	public StepCheckFamiliar(String familiar) {
		super(false);
		this.familiar = familiar;
	}

	@Override
	public Result process(final World world, final int posX, final int posY, final int posZ, final long ticks, final BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
		if(ticks % 20L != 0L) {
			return Result.STARTING;
		}

		if(!world.isRemote) {
			int familiarType;
			switch(familiar) {
				case "cat":
					familiarType = 1;
					break;
				case "toad":
					familiarType = 2;
					break;
				case "owl":
					familiarType = 3;
					break;
				default:
					familiarType = 0;
					break;
			}

			if(familiarType == 0) {
				return Result.COMPLETED;
			}

			EntityPlayer player = ritual.getInitiatingPlayer(world);
			if(player != null && Familiar.getActiveFamiliarType(player) == familiarType) {
				return Result.COMPLETED;
			}
		}

		RiteRegistry.RiteError("witchery.rite.missingfamiliar", ritual.getInitiatingPlayerName(), world);
		return Result.ABORTED_REFUND;
	}
}
