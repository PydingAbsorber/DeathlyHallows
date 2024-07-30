package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Coord;
import com.pyding.deathlyhallows.blocks.BlockElderRitual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class ElderRitualStep extends RitualStep {
	protected ElderRitualStep(boolean canRelocate) {
		super(canRelocate);
	}

	@Override
	public Result process(World world, int i, int i1, int i2, long l, BlockCircle.TileEntityCircle.ActivatedRitual activatedRitual) {
		return null;
	}

	public Result elderRun(World world, int posX, int posY, int posZ, long ticks, BlockElderRitual.TileEntityCircle.ActivatedElderRitual ritual) {
		this.sourceX = posX;
		this.sourceZ = posZ;
		this.sourceY = posY;
		if(this.canRelocate && ritual.getLocation() != null) {
			Coord l = ritual.getLocation();
			int maxDistance = 50 + 50 * ritual.covenSize;
			int maxDistanceSq = maxDistance * maxDistance;
			if(l.distanceSqTo(this.sourceX, this.sourceY, this.sourceZ) > (double)maxDistanceSq) {
				EntityPlayer player = ritual.getInitiatingPlayer(world);
				if(player != null) {
					ChatUtil.sendTranslated(player, "witchery.rite.toofaraway");
				}

				return RitualStep.Result.ABORTED_REFUND;
			}

			posX = l.x;
			posY = l.y;
			posZ = l.z;
		}

		return this.elderProcess(world, posX, posY, posZ, ticks, ritual);
	}

	public abstract Result elderProcess(final World p0, final int p1, final int p2, final int p3, final long p4, final BlockElderRitual.TileEntityCircle.ActivatedElderRitual p5);
}
