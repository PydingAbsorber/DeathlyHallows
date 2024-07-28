package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.entity.EntityImp;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TameableUtil;
import com.pyding.deathlyhallows.blocks.ElderRitualBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ElderSummonCreature extends ElderRite{
	private final Class<? extends EntityCreature> creatureToSummon;
	private boolean bindTameable;

	public ElderSummonCreature(Class<? extends EntityCreature> creatureToSummon, boolean bindTameable) {
		this.creatureToSummon = creatureToSummon;
		this.bindTameable = bindTameable; 
	}
	
	public void addSteps(ArrayList steps, int intialStage) {
		steps.add(new ElderSummonCreature.StepSummonCreature(this));
	}

	private static class StepSummonCreature extends ElderRitualStep {
		private final ElderSummonCreature rite;

		public StepSummonCreature(ElderSummonCreature rite) {
			super(false);
			this.rite = rite;
		}

		@Override
		public Result elderProcess(World world, int posX, int posY, int posZ, long ticks, ElderRitualBlock.TileEntityCircle.ActivatedElderRitual ritual) {
			if (ticks % 20L != 0L) {
				return Result.STARTING;
			} else {
				if (!world.isRemote) {
					int[][] PATTERN = new int[][]{{0, 0, 1, 1, 1, 0, 0}, {0, 1, 1, 1, 1, 1, 0}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 2, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {0, 1, 1, 1, 1, 1, 0}, {0, 0, 1, 1, 1, 0, 0}};
					int obstructions = 0;

					for(int y = posY + 1; y <= posY + 3; ++y) {
						int offsetZ = (PATTERN.length - 1) / 2;

						for(int z = 0; z < PATTERN.length - 1; ++z) {
							int worldZ = posZ - offsetZ + z;
							int offsetX = (PATTERN[z].length - 1) / 2;

							for(int x = 0; x < PATTERN[z].length; ++x) {
								int worldX = posX - offsetX + x;
								int val = PATTERN[PATTERN.length - 1 - z][x];
								Material material;
								if (val == 1) {
									material = world.getBlock(worldX, y, worldZ).getMaterial();
									if (material != null && material.isSolid()) {
										++obstructions;
									}
								} else if (val == 2) {
									material = world.getBlock(worldX, y, worldZ).getMaterial();
									if (material != null && material.isSolid()) {
										obstructions += 100;
									}
								}
							}
						}
					}

					if (obstructions > 1) {
						ParticleEffect.LARGE_SMOKE.send(SoundEffect.NOTE_SNARE, world, (double)posX, (double)posY, (double)posZ, 0.5, 2.0, 16);
						RiteRegistry.RiteError("witchery.rite.obstructedcircle", ritual.getInitiatingPlayerName(), world);
						return Result.ABORTED_REFUND;
					}

					try {
						Constructor ctor = this.rite.creatureToSummon.getConstructor(World.class);
						EntityLiving entity = (EntityLiving)ctor.newInstance(world);
						EntityPlayer player;
						if (entity instanceof EntityDemon) {
							((EntityDemon)entity).setPlayerCreated(true);
						} else {
							if (entity instanceof EntityImp && ritual.covenSize == 0) {
								player = ritual.getInitiatingPlayer(world);
								SoundEffect.NOTE_SNARE.playAt(world, (double)posX, (double)posY, (double)posZ);
								if (player != null) {
									ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.rite.coventoosmall", new Object[0]);
								}

								return Result.ABORTED_REFUND;
							}

							if (this.rite.bindTameable && entity instanceof EntityTameable) {
								((EntityTameable)entity).setTamed(true);
								TameableUtil.setOwner((EntityTameable)entity, ritual.getInitiatingPlayer(world));
							}
						}

						entity.setLocationAndAngles(0.5 + (double)posX, 1.0 + (double)posY, 0.5 + (double)posZ, 1.0F, 0.0F);
						world.spawnEntityInWorld(entity);
						entity.onSpawnWithEgg(null);
						ParticleEffect.PORTAL.send(SoundEffect.RANDOM_FIZZ, entity, 0.5, 1.0, 16);
					} catch (NoSuchMethodException |
							 InvocationTargetException |
							 InstantiationException |
							 IllegalAccessException ignored) {
					}
				}

				return Result.COMPLETED;
			}
		}
	}
}
