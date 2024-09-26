package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.blocks.BlockAltar;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.blocks.BlockElderRitual;
import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RiteWithEffect extends ElderRite {
	private final float upkeepPowerCost;
	private final int ticksToLive;
	private final int radius;
	private final int id;
	private final Long time;

	public RiteWithEffect(int radius, float upkeepPowerCost, int ticksToLive, int id, Long time) {
		this.radius = radius;
		this.upkeepPowerCost = upkeepPowerCost;
		this.ticksToLive = ticksToLive;
		this.id = id;
		this.time = time;
	}
	
	public void addSteps(ArrayList<RitualStep> steps, int intialStage) {
		steps.add(new StepVanish(this, intialStage));
	}

	private static class StepVanish extends ElderRitualStep {
		private final RiteWithEffect rite;
		protected int ticksSoFar;
		Coord powerSourceCoord;

		public StepVanish(RiteWithEffect rite, int ticksSoFar) {
			super(false);
			this.rite = rite;
			this.ticksSoFar = ticksSoFar;
		}

		public int getCurrentStage() {
			return ticksSoFar;
		}


		@Override
		public Result elderProcess(World world, int posX, int posY, int posZ, long ticks, BlockElderRitual.TileEntityCircle.ActivatedElderRitual ritual) {
			if(ticks % 20L != 0L) {
				return Result.STARTING;
			}

			if(world.isRemote) {
				return Result.UPKEEP;
			}
			if(rite.upkeepPowerCost > 0.0F) {
				IPowerSource powerSource = getPowerSource(world, posX, posY, posZ);
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

			int r = rite.radius;
			AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(posX - r, posY, posZ - r, posX + r, posY + 1, posZ + r);
			for(Object obj: world.getEntitiesWithinAABB(EntityPlayer.class, bounds)) {
				EntityPlayer player = (EntityPlayer)obj;
				if(Coord.distanceSq(player.posX, player.posY, player.posZ, posX, posY, posZ) > r * r) {
					continue;
				}
				DeathlyProperties props = DeathlyProperties.get(player);
				switch(rite.id) {
					case 1: {
						props.setBanka(rite.time);
					}
					case 2: {
						props.setHunt(rite.time);
					}
					case 3: {
						props.setHeal(rite.time);
					}
				}
				ParticleEffect.INSTANT_SPELL.send(SoundEffect.WITCHERY_MOB_BABA_LIVING, player, 1.0, 2.0, 8);
				ParticleEffect.INSTANT_SPELL.send(SoundEffect.WITCHERY_MOB_IMP_LAUGH, player, 1.0, 2.0, 8);
				return Result.COMPLETED;
			}

			return Result.UPKEEP;

		}

		IPowerSource getPowerSource(World world, int posX, int posY, int posZ) {
			if(powerSourceCoord == null || world.rand.nextInt(5) == 0) {
				return findNewPowerSource(world, posX, posY, posZ);
			}

			TileEntity tileEntity = powerSourceCoord.getBlockTileEntity(world);
			if(!(tileEntity instanceof BlockAltar.TileEntityAltar)) {
				return findNewPowerSource(world, posX, posY, posZ);
			}
			BlockAltar.TileEntityAltar altarTileEntity = (BlockAltar.TileEntityAltar)tileEntity;
			return !altarTileEntity.isValid() ? findNewPowerSource(world, posX, posY, posZ) : altarTileEntity;
		}

		private IPowerSource findNewPowerSource(World world, int posX, int posY, int posZ) {
			PowerSources powerSources = PowerSources.instance();
			if(powerSources == null) {
				return null;
			}
			List<PowerSources.RelativePowerSource> sources = powerSources.get(world, new Coord(posX, posY, posZ), 0);
			return sources.size() > 0 ? sources.get(0).source() : null;
		}
	}
}
