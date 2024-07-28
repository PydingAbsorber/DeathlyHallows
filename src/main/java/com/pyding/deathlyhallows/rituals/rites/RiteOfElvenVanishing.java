package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.blocks.BlockAltar;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.blocks.ElderRitualBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RiteOfElvenVanishing extends ElderRite {
	private final float upkeepPowerCost;
	private final int ticksToLive;
	private final int radius;


	public RiteOfElvenVanishing(int radius, float upkeepPowerCost, int ticksToLive) {
		this.radius = radius;
		this.upkeepPowerCost = upkeepPowerCost;
		this.ticksToLive = ticksToLive;
	}

	public void addSteps(ArrayList steps, int intialStage) {
		steps.add(new StepVanish(this, intialStage));
	}

	private static class StepVanish extends ElderRitualStep {
		private final RiteOfElvenVanishing rite;
		private final boolean activated = false;
		protected int ticksSoFar;
		Coord powerSourceCoord;
		static final int POWER_SOURCE_RADIUS = 16;

		public StepVanish(RiteOfElvenVanishing rite, int ticksSoFar) {
			super(false);
			this.rite = rite;
			this.ticksSoFar = ticksSoFar;
		}

		public int getCurrentStage() {
			return this.ticksSoFar;
		}

		@Override
		public Result elderProcess(World world, int posX, int posY, int posZ, long ticks, ElderRitualBlock.TileEntityCircle.ActivatedElderRitual ritual) {
			if(ticks % 20L != 0L) {
				return Result.STARTING;
			}
			else {
				if(!world.isRemote) {
					if(this.rite.upkeepPowerCost > 0.0F) {
						IPowerSource powerSource = this.getPowerSource(world, posX, posY, posZ);
						if(powerSource == null) {
							return Result.ABORTED;
						}

						this.powerSourceCoord = powerSource.getLocation();
						if(!powerSource.consumePower(this.rite.upkeepPowerCost)) {
							return Result.ABORTED;
						}
					}

					if(this.rite.ticksToLive > 0 && ticks % 20L == 0L && ++this.ticksSoFar >= this.rite.ticksToLive) {
						return Result.COMPLETED;
					}

					int r = this.rite.radius;
					AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(posX - r, posY, posZ - r, posX + r, posY + 1, posZ + r);
					Iterator i$ = world.getEntitiesWithinAABB(EntityPlayer.class, bounds).iterator();

					while(i$.hasNext()) {
						Object obj = i$.next();
						EntityPlayer player = (EntityPlayer)obj;
						if(Coord.distance(player.posX, player.posY, player.posZ, posX, posY, posZ) <= (double)r) {
							com.pyding.deathlyhallows.common.properties.ExtendedPlayer props = com.pyding.deathlyhallows.common.properties.ExtendedPlayer.get(player);
							if(props.getElfLvl() > 0) {
								props.nullifyElfLvl();
								ParticleEffect.INSTANT_SPELL.send(SoundEffect.NOTE_PLING, player, 1.0, 2.0, 8);
								ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, player, 1.0, 2.0, 8);
								return Result.COMPLETED;
							}
						}
					}
				}

				return Result.UPKEEP;
			}
		}

		IPowerSource getPowerSource(World world, int posX, int posY, int posZ) {
			if(this.powerSourceCoord != null && world.rand.nextInt(5) != 0) {
				TileEntity tileEntity = this.powerSourceCoord.getBlockTileEntity(world);
				if(!(tileEntity instanceof BlockAltar.TileEntityAltar)) {
					return this.findNewPowerSource(world, posX, posY, posZ);
				}
				else {
					BlockAltar.TileEntityAltar altarTileEntity = (BlockAltar.TileEntityAltar)tileEntity;
					return !altarTileEntity.isValid() ? this.findNewPowerSource(world, posX, posY, posZ) : altarTileEntity;
				}
			}
			else {
				return this.findNewPowerSource(world, posX, posY, posZ);
			}
		}

		private IPowerSource findNewPowerSource(World world, int posX, int posY, int posZ) {
			List<PowerSources.RelativePowerSource> sources = PowerSources.instance() != null ? PowerSources.instance()
																										   .get(world, new Coord(posX, posY, posZ), 16) : null;
			return sources != null && sources.size() > 0 ? sources.get(0).source() : null;
		}
	}
}
