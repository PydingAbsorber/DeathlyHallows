package com.pyding.deathlyhallows.rituals.steps;

import com.emoniph.witchery.blocks.BlockAltar;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Coord;
import com.pyding.deathlyhallows.blocks.BlockElderRitual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

public abstract class StepBase extends RitualStep {

	protected Coord powerSourceCoord;
	
	protected StepBase(boolean canRelocate) {
		super(canRelocate);
	}

	@Override
	public Result process(World world, int i, int i1, int i2, long l, BlockCircle.TileEntityCircle.ActivatedRitual activatedRitual) {
		return Result.ABORTED_REFUND;
	}

	protected IPowerSource findNewPowerSource(World world, int posX, int posY, int posZ) {
		PowerSources powerSources = PowerSources.instance();
		if(powerSources == null) {
			return null;
		}
		List<PowerSources.RelativePowerSource> sources = powerSources.get(world, new Coord(posX, posY, posZ), 0);
		return sources.size() > 0 ? sources.get(0).source() : null;
	}

	protected IPowerSource getPowerSource(World world, int posX, int posY, int posZ) {
		if(this.powerSourceCoord == null || world.rand.nextInt(5) == 0) {
			return findNewPowerSource(world, posX, posY, posZ);
		}
		TileEntity tileEntity = powerSourceCoord.getBlockTileEntity(world);
		if(tileEntity instanceof BlockAltar.TileEntityAltar) {
			BlockAltar.TileEntityAltar altarTileEntity = (BlockAltar.TileEntityAltar)tileEntity;
			return !altarTileEntity.isValid() ? findNewPowerSource(world, posX, posY, posZ) : altarTileEntity;
		}
		return findNewPowerSource(world, posX, posY, posZ);
	}
	
	public Result elderRun(World world, int x, int y, int z, long ticks, BlockElderRitual.TileEntityCircle.ActivatedElderRitual ritual) {
		sourceX = x;
		sourceZ = z;
		sourceY = y;
		if(!canRelocate || ritual.getLocation() == null) {
			return elderProcess(world, x, y, z, ticks, ritual);
		}
		
		Coord l = ritual.getLocation();
		int maxDistance = 50 + 50 * ritual.covenSize;
		int maxDistanceSq = maxDistance * maxDistance;
		if(l.distanceSqTo(sourceX, sourceY, sourceZ) > maxDistanceSq) {
			EntityPlayer player = ritual.getInitiatingPlayer(world);
			if(player != null) {
				ChatUtil.sendTranslated(player, "witchery.rite.toofaraway");
			}
			return RitualStep.Result.ABORTED_REFUND;
		}
		x = l.x;
		y = l.y;
		z = l.z;

		return elderProcess(world, x, y, z, ticks, ritual);
	}

	public abstract Result elderProcess(World world, int x, int y, int z, final long ticks, final BlockElderRitual.TileEntityCircle.ActivatedElderRitual ritual);
}
