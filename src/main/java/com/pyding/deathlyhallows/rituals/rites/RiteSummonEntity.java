package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.entity.EntityImp;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TameableUtil;
import com.pyding.deathlyhallows.blocks.BlockElderRitual;
import com.pyding.deathlyhallows.rituals.steps.StepBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class RiteSummonEntity extends RiteBase {
	private final Class<? extends EntityCreature> creatureToSummon;
	private final boolean bindTameable;

	public RiteSummonEntity(Class<? extends EntityCreature> creatureToSummon, boolean bindTameable) {
		this.creatureToSummon = creatureToSummon;
		this.bindTameable = bindTameable;
	}

	public void addSteps(ArrayList<RitualStep> steps, int initialStage) {
		steps.add(new RiteSummonEntity.StepSummonCreature(this));
	}

	private static class StepSummonCreature extends StepBase {
		private final RiteSummonEntity rite;

		public StepSummonCreature(RiteSummonEntity rite) {
			super(false);
			this.rite = rite;
		}

		@Override
		public Result elderProcess(World world, int posX, int posY, int posZ, long ticks, BlockElderRitual.TileEntityCircle.ActivatedElderRitual ritual) {
			if(ticks % 20L != 0L) {
				return Result.STARTING;
			}

			if(world.isRemote) {
				return Result.COMPLETED;
			}
			if(hasObsticles(world, posX, posY, posZ)) {
				ParticleEffect.LARGE_SMOKE.send(SoundEffect.NOTE_SNARE, world, posX, posY, posZ, 0.5, 2.0, 16);
				RiteRegistry.RiteError("witchery.rite.obstructedcircle", ritual.getInitiatingPlayerName(), world);
				return Result.ABORTED_REFUND;
			}

			try {
				Constructor<? extends EntityCreature> ctor = this.rite.creatureToSummon.getConstructor(World.class);
				EntityCreature e = ctor.newInstance(world);
				EntityPlayer player;
				if(!(e instanceof EntityDemon)) {
					if(e instanceof EntityImp && ritual.covenSize == 0) {
						player = ritual.getInitiatingPlayer(world);
						SoundEffect.NOTE_SNARE.playAt(world, posX, posY, posZ);
						if(player != null) {
							ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.rite.coventoosmall");
						}
						return Result.ABORTED_REFUND;
					}
					if(this.rite.bindTameable && e instanceof EntityTameable) {
						((EntityTameable)e).setTamed(true);
						TameableUtil.setOwner((EntityTameable)e, ritual.getInitiatingPlayer(world));
					}
				}
				else {
					((EntityDemon)e).setPlayerCreated(true);
				}

				e.setLocationAndAngles(0.5 + (double)posX, 1.0 + (double)posY, 0.5 + (double)posZ, 1.0F, 0.0F);
				world.spawnEntityInWorld(e);
				e.onSpawnWithEgg(null);
				ParticleEffect.PORTAL.send(SoundEffect.RANDOM_FIZZ, e, 0.5, 1.0, 16);
			}
			catch(NoSuchMethodException |
				  InvocationTargetException |
				  InstantiationException |
				  IllegalAccessException ignored) {
			}
			return Result.COMPLETED;
		}

		private static boolean hasObsticles(World world, int posX, int posY, int posZ) {
			int[][] PATTERN = new int[][]{
					{0, 0, 1, 1, 1, 0, 0}, 
					{0, 1, 1, 1, 1, 1, 0},
					{1, 1, 1, 1, 1, 1, 1}, 
					{1, 1, 1, 2, 1, 1, 1}, 
					{1, 1, 1, 1, 1, 1, 1},
					{0, 1, 1, 1, 1, 1, 0}, 
					{0, 0, 1, 1, 1, 0, 0}
			};
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
						if(val == 1) {
							material = world.getBlock(worldX, y, worldZ).getMaterial();
							if(material != null && material.isSolid()) {
								++obstructions;
							}
						}
						else if(val == 2) {
							material = world.getBlock(worldX, y, worldZ).getMaterial();
							if(material != null && material.isSolid()) {
								obstructions += 100;
							}
						}
					}
				}
			}
			return obstructions > 1;
		}

	}
	
}
