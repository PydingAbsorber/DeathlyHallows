package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.util.Coord;
import com.pyding.deathlyhallows.blocks.BlockElderRitual;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ElderSacrificePower extends ElderSacrifice {
	public final float powerRequired;
	public final int powerFrequencyInTicks;

	public ElderSacrificePower(float powerRequired, int powerFrequencyInTicks) {
		this.powerRequired = powerRequired;
		this.powerFrequencyInTicks = powerFrequencyInTicks;
	}

	public void addDescription(StringBuffer sb) {
		sb.append(String.format("\n§8%s§0 %s\n", Witchery.resource("witchery.book.altarpower"), MathHelper.floor_float(this.powerRequired)));
	}

	public boolean isMatch(World world, int posX, int posY, int posZ, int maxDistance, ArrayList<Entity> entities, ArrayList<ItemStack> grassperStacks) {
		return true;
	}

	public void addSteps(ArrayList<ElderRitualStep> steps, AxisAlignedBB bounds, int maxDistance) {
		steps.add(new ElderSacrificePower.SacrificePowerStep(this));
	}

	private static class SacrificePowerStep extends ElderRitualStep {
		private final ElderSacrificePower sacrifice;
		private static final int POWER_SOURCE_RADIUS = 16;

		public SacrificePowerStep(ElderSacrificePower sacrifice) {
			super(false);
			this.sacrifice = sacrifice;
		}


		@Override
		public Result elderProcess(World world, int posX, int posY, int posZ, long ticks, BlockElderRitual.TileEntityCircle.ActivatedElderRitual ritual) {
			if(ticks % (long)this.sacrifice.powerFrequencyInTicks != 0L) {
				return Result.STARTING;
			}
			else {
				IPowerSource powerSource = this.findNewPowerSource(world, posX, posY, posZ);
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

		private IPowerSource findNewPowerSource(World world, int posX, int posY, int posZ) {
			List<PowerSources.RelativePowerSource> sources = PowerSources.instance() != null ? PowerSources.instance()
																										   .get(world, new Coord(posX, posY, posZ), 16) : null;
			return sources != null && sources.size() > 0 ? sources.get(0).source() : null;
		}
	}
}
