package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.blocks.BlockAltar;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.entity.EntityBanshee;
import com.emoniph.witchery.entity.EntityNightmare;
import com.emoniph.witchery.entity.EntityPoltergeist;
import com.emoniph.witchery.entity.EntitySpirit;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.blocks.ElderRitualBlock;
import com.pyding.deathlyhallows.utils.DHConfig;
import com.pyding.deathlyhallows.utils.DHUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class SummonSpiritRite extends ElderRite {
	private final float upkeepPowerCost;
	private final int ticksToLive;
	private final int radius;

	public SummonSpiritRite(int radius, float upkeepPowerCost, int ticksToLive) {
		this.radius = radius;
		this.upkeepPowerCost = upkeepPowerCost;
		this.ticksToLive = ticksToLive;
	}

	public void addSteps(ArrayList steps, int intialStage) {
		steps.add(new StepVanish(this, intialStage));
	}

	private static class StepVanish extends ElderRitualStep {
		private final SummonSpiritRite rite;
		private final boolean activated = false;
		protected int ticksSoFar;
		Coord powerSourceCoord;
		static final int POWER_SOURCE_RADIUS = 16;

		public StepVanish(SummonSpiritRite rite, int ticksSoFar) {
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
							Random random = new Random();
							if(random.nextDouble() < 0.05) {
								String randomEntityName = DHUtils.getEntitiesNames()
																 .get(world.rand.nextInt(DHUtils.getEntitiesNames()
																								.size() - 1));
								int tries = 0;
								while(DHUtils.contains(DHConfig.sonatRitual, randomEntityName) && tries <= 1000) {
									randomEntityName = DHUtils.getEntitiesNames()
															  .get(world.rand.nextInt(DHUtils.getEntitiesNames()
																							 .size() - 1));
									tries++;
								}
								if(tries <= 1000) {
									return Result.ABORTED_REFUND;
								}
								Entity entity = EntityList.createEntityByName(randomEntityName, world);
								world.spawnEntityInWorld(entity);
							}
							else {
								Class entityClass;
								for(int i = 0; i < DHConfig.randomSpirits; i++) {
									switch(random.nextInt(4)) {
										case 0:
											entityClass = EntityBanshee.class;
											break;
										case 1:
											entityClass = EntitySpirit.class;
											break;
										case 2:
											entityClass = EntityNightmare.class;
											break;
										case 3:
											entityClass = EntityPoltergeist.class;
											break;
										default:
											entityClass = null;
									}
									DHUtils.spawnEntity(world, posX, posY + 1, posZ, entityClass);
								}
							}
							ParticleEffect.INSTANT_SPELL.send(SoundEffect.WITCHERY_MOB_BABA_LIVING, player, 1.0, 2.0, 8);
							ParticleEffect.INSTANT_SPELL.send(SoundEffect.WITCHERY_MOB_IMP_LAUGH, player, 1.0, 2.0, 8);
							return Result.COMPLETED;
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
