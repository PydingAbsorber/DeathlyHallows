package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.predictions.PredictionManager;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.blocks.BlockElderRitual;
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
import java.util.List;

import static com.emoniph.witchery.Witchery.Blocks;
import static com.emoniph.witchery.Witchery.Items;

public class ElderSummonItem extends ElderRite {
	private final ItemStack stack;
	private final Binding binding;

	public ElderSummonItem(ItemStack stack, Binding binding) {
		this.stack = stack;
		this.binding = binding;
	}

	public ElderSummonItem(ItemStack stack) {
		this(stack, Binding.NONE);
	}

	public void addSteps(ArrayList<RitualStep> steps, int intialStage) {
		steps.add(new ElderSummonItem.StepSummonItem(this));
	}

	private static class StepSummonItem extends ElderRitualStep {
		private final ElderSummonItem rite;

		public StepSummonItem(ElderSummonItem rite) {
			super(false);
			this.rite = rite;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Result elderProcess(World world, int posX, int posY, int posZ, long ticks, BlockElderRitual.TileEntityCircle.ActivatedElderRitual ritual) {
			if(ticks % 20L != 0L) {
				return Result.STARTING;
			}

			if(world.isRemote) {
				return Result.COMPLETED;
			}
			ItemStack itemstack = ItemStack.copyItemStack(this.rite.stack);
			if(this.rite.binding == ElderSummonItem.Binding.LOCATION) {
				Items.GENERIC.bindToLocation(world, posX, posY, posZ, world.provider.dimensionId, world.provider.getDimensionName(), itemstack);
			}
			else {
				AxisAlignedBB bounds;
				if(this.rite.binding == ElderSummonItem.Binding.ENTITY) {
					EntityLivingBase target = null;
					bounds = AxisAlignedBB.getBoundingBox(posX - 4, posY, posZ - 4, posX + 4, posY + 1, posZ + 4);
					for(EntityPlayer player: (List<EntityPlayer>)world.getEntitiesWithinAABB(EntityPlayer.class, bounds)) {
						if(Coord.distance(player.posX, player.posY, player.posZ, posX, posY, posZ) <= 4.0) {
							target = player;
						}
					}

					if(target != null) {
						bounds = AxisAlignedBB.getBoundingBox(posX - 4, posY, posZ - 4, posX + 4, posY + 1, posZ + 4);
						for(EntityLiving entity: (List<EntityLiving>)world.getEntitiesWithinAABB(EntityLiving.class, bounds)) {
							if(Coord.distance(entity.posX, entity.posY, entity.posZ, posX, posY, posZ) <= 4.0) {
								target = entity;
							}
						}
					}

					if(target == null) {
						return Result.ABORTED_REFUND;
					}

					Items.TAGLOCK_KIT.setTaglockForEntity(itemstack, null, target, false, 1);
				}
				else if(this.rite.binding != ElderSummonItem.Binding.PLAYER) {
					if(this.rite.binding == ElderSummonItem.Binding.COPY_LOCATION) {
						for(SacrificedItem item: ritual.sacrificedItems) {
							if(!Items.GENERIC.hasLocationBinding(item.itemstack)) {
								continue;
							}
							Items.GENERIC.copyLocationBinding(item.itemstack, itemstack);
							break;
						}
					}
				}
				else {
					EntityLivingBase target = null;
					bounds = AxisAlignedBB.getBoundingBox(posX - 4, posY, posZ - 4, posX + 4, posY + 1, posZ + 4);
					for(EntityPlayer player: (List<EntityPlayer>)world.getEntitiesWithinAABB(EntityPlayer.class, bounds)) {
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

			if(itemstack.getItem() == Item.getItemFromBlock(Blocks.CRYSTAL_BALL)) {
				EntityPlayer player = ritual.getInitiatingPlayer(world);
				if(player != null) {
					PredictionManager.instance().setFortuneTeller(player, true);
				}
			}

			EntityItem entity = new EntityItem(world, 0.5 + posX, posY + 1.5, 0.5 + posZ, itemstack);
			entity.motionX = 0.0;
			entity.motionY = 0.3;
			entity.motionZ = 0.0;
			world.spawnEntityInWorld(entity);
			ParticleEffect.SPELL.send(SoundEffect.RANDOM_FIZZ, entity, 0.5, 0.5, 16);
			return Result.COMPLETED;
		}

	}

	public enum Binding {
		NONE,
		LOCATION,
		ENTITY,
		COPY_LOCATION,
		PLAYER
	}
}
