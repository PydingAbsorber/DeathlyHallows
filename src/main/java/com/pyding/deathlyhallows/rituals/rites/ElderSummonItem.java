package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.predictions.PredictionManager;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.blocks.ElderRitualBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;

public class ElderSummonItem extends ElderRite {
	private final ItemStack itemToSummon;
	private final ElderSummonItem.Binding binding;

	public ElderSummonItem(ItemStack itemToSummon, ElderSummonItem.Binding binding) {
		this.itemToSummon = itemToSummon;
		this.binding = binding;
	}

	public void addSteps(ArrayList steps, int intialStage) {
		steps.add(new ElderSummonItem.StepSummonItem(this));
	}

	private static class StepSummonItem extends ElderRitualStep {
		private final ElderSummonItem rite;

		public StepSummonItem(ElderSummonItem rite) {
			super(false);
			this.rite = rite;
		}

		@Override
		public Result elderProcess(World world, int posX, int posY, int posZ, long ticks, ElderRitualBlock.TileEntityCircle.ActivatedElderRitual ritual) {
			if(ticks % 20L != 0L) {
				return Result.STARTING;
			}
			else {
				if(!world.isRemote) {
					ItemStack itemstack = ItemStack.copyItemStack(this.rite.itemToSummon);
					if(this.rite.binding == ElderSummonItem.Binding.LOCATION) {
						Witchery.Items.GENERIC.bindToLocation(world, posX, posY, posZ, world.provider.dimensionId, world.provider.getDimensionName(), itemstack);
					}
					else {
						boolean r;
						AxisAlignedBB bounds;
						Iterator i$;
						Object obj;
						EntityPlayer player;
						if(this.rite.binding == ElderSummonItem.Binding.ENTITY) {
							r = true;
							EntityLivingBase target = null;
							bounds = AxisAlignedBB.getBoundingBox(posX - 4, posY, posZ - 4, posX + 4, posY + 1, posZ + 4);
							i$ = world.getEntitiesWithinAABB(EntityPlayer.class, bounds).iterator();

							while(i$.hasNext()) {
								obj = i$.next();
								player = (EntityPlayer)obj;
								if(Coord.distance(player.posX, player.posY, player.posZ, posX, posY, posZ) <= 4.0) {
									target = player;
								}
							}

							if(target != null) {
								bounds = AxisAlignedBB.getBoundingBox(posX - 4, posY, posZ - 4, posX + 4, posY + 1, posZ + 4);
								i$ = world.getEntitiesWithinAABB(EntityLiving.class, bounds).iterator();

								while(i$.hasNext()) {
									obj = i$.next();
									EntityLiving entity = (EntityLiving)obj;
									if(Coord.distance(entity.posX, entity.posY, entity.posZ, posX, posY, posZ) <= 4.0) {
										target = entity;
									}
								}
							}

							if(target == null) {
								return Result.ABORTED_REFUND;
							}

							Witchery.Items.TAGLOCK_KIT.setTaglockForEntity(itemstack, null, target, false, 1);
						}
						else if(this.rite.binding != ElderSummonItem.Binding.PLAYER) {
							if(this.rite.binding == ElderSummonItem.Binding.COPY_LOCATION) {
								Iterator i$1 = ritual.sacrificedItems.iterator();

								while(i$1.hasNext()) {
									RitualStep.SacrificedItem item = (ElderRitualStep.SacrificedItem)i$1.next();
									if(Witchery.Items.GENERIC.hasLocationBinding(item.itemstack)) {
										Witchery.Items.GENERIC.copyLocationBinding(item.itemstack, itemstack);
										break;
									}
								}
							}
						}
						else {
							r = true;
							EntityLivingBase target = null;
							bounds = AxisAlignedBB.getBoundingBox(posX - 4, posY, posZ - 4, posX + 4, posY + 1, posZ + 4);
							i$ = world.getEntitiesWithinAABB(EntityPlayer.class, bounds).iterator();

							while(i$.hasNext()) {
								obj = i$.next();
								player = (EntityPlayer)obj;
								if(Coord.distance(player.posX, player.posY, player.posZ, posX, posY, posZ) <= 4.0) {
									target = player;
								}
							}

							if(target == null) {
								return Result.ABORTED_REFUND;
							}

							NBTTagCompound nbtRoot = new NBTTagCompound();
							nbtRoot.setString("WITCBoundPlayer", target.getCommandSenderName());
							itemstack.setTagCompound(nbtRoot);
						}
					}

					if(itemstack.getItem() == Item.getItemFromBlock(Witchery.Blocks.CRYSTAL_BALL)) {
						EntityPlayer player = ritual.getInitiatingPlayer(world);
						if(player != null) {
							PredictionManager.instance().setFortuneTeller(player, true);
						}
					}

					EntityItem entity = new EntityItem(world, 0.5 + (double)posX, (double)posY + 1.5, 0.5 + (double)posZ, itemstack);
					entity.motionX = 0.0;
					entity.motionY = 0.3;
					entity.motionZ = 0.0;
					world.spawnEntityInWorld(entity);
					ParticleEffect.SPELL.send(SoundEffect.RANDOM_FIZZ, entity, 0.5, 0.5, 16);
				}

				return Result.COMPLETED;
			}
		}
	}

	public enum Binding {
		NONE,
		LOCATION,
		ENTITY,
		COPY_LOCATION,
		PLAYER;

		Binding() {
		}
	}
}
