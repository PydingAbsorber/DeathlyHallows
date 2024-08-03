package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockGrassper;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Const;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.blocks.BlockElderRitual;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class ElderSacrificeItem extends ElderSacrifice {
	final ItemStack[] stacks;

	public ElderSacrificeItem(ItemStack... stacks) {
		this.stacks = stacks;
	}

	public void addDescription(StringBuffer sb) {
		for(ItemStack tack: stacks) {
			sb.append("§8>§0 ");
			if(tack.getItem() != Items.potionitem) {
				sb.append(tack.getDisplayName());
				sb.append(Const.BOOK_NEWLINE);
				continue;
			}
			@SuppressWarnings("unchecked")
			List<PotionEffect> list = (List<PotionEffect>)Items.potionitem.getEffects(tack);
			if(list == null || list.isEmpty()) {
				sb.append(tack.getDisplayName());
				sb.append(Const.BOOK_NEWLINE);
				continue;
			}
			PotionEffect effect = list.get(0);
			String s = tack.getDisplayName();
			if(effect.getAmplifier() > 0) {
				s = s + " " + StatCollector.translateToLocal("potion.potency." + effect.getAmplifier()).trim();
			}
			if(effect.getDuration() > 20) {
				s = s + " (" + Potion.getDurationString(effect) + ")";
			}
			sb.append(s);
			sb.append(Const.BOOK_NEWLINE);
		}

	}

	public boolean isMatch(World world, int posX, int posY, int posZ, int maxDistance, ArrayList<Entity> entities, ArrayList<ItemStack> grassperStacks) {
		ArrayList<Entity> itemsToRemove = new ArrayList<>();
		ArrayList<ItemStack> otherItemsToRemove = new ArrayList<>();

		for(ItemStack stack: this.stacks) {
			boolean found = false;

			for(Entity value: entities) {
				if(!(value instanceof EntityItem)) {
					continue;
				}
				EntityItem entity = (EntityItem)value;
				if(!isItemEqual(stack, entity.getEntityItem()) || itemsToRemove.contains(entity) || !(distance(entity.posX, entity.posY, entity.posZ, posX, posY, posZ) <= (double)maxDistance)) {
					continue;
				}
				itemsToRemove.add(entity);
				found = true;
				break;
			}

			for(ItemStack stackInGrassper: grassperStacks) {
				if(!isItemEqual(stack, stackInGrassper) || otherItemsToRemove.contains(stackInGrassper)) {
					continue;
				}
				otherItemsToRemove.add(stackInGrassper);
				found = true;
				break;
			}

			if(!found) {
				return false;
			}
		}

		for(Entity itemToRemove: itemsToRemove) {
			entities.remove(itemToRemove);
		}

		for(ItemStack itemToRemove: otherItemsToRemove) {
			grassperStacks.remove(itemToRemove);
		}

		return true;
	}

	public static boolean isItemEqual(ItemStack toFind, ItemStack found) {
		return (found.getItem() == Witchery.Items.ARTHANA || found.getItem() == Witchery.Items.BOLINE) && found.getItem() == toFind.getItem() || OreDictionary.itemMatches(found, toFind, false);
	}

	public void addSteps(ArrayList<RitualStep> steps, AxisAlignedBB bounds, int maxDistance) {
		for(ItemStack stack: stacks) {
			steps.add(new StepElderSacrificeItem(stack, bounds, maxDistance));
		}
	}

	protected static class StepElderSacrificeItem extends ElderRitualStep {
		private static final int RADIUS = 16;		
		protected final ItemStack stack;
		protected final AxisAlignedBB bounds;
		protected final int maxDistance;
		protected boolean showMessages;

		public StepElderSacrificeItem(ItemStack stack, AxisAlignedBB bounds, int maxDistance) {
			super(false);
			this.stack = stack;
			this.bounds = bounds;
			this.maxDistance = maxDistance;
			this.showMessages = false;
		}


		@Override
		public Result elderProcess(World world, int posX, int posY, int posZ, long ticks, BlockElderRitual.TileEntityCircle.ActivatedElderRitual ritual) {
			if(ticks % 20L != 0L) {
				return Result.STARTING;
			}

			if(world.isRemote) {
				if(showMessages) {
					RiteRegistry.RiteError("witchery.rite.missingitem", ritual.getInitiatingPlayerName(), world);
				}

				return Result.ABORTED_REFUND;
			}

			@SuppressWarnings("unchecked")
			List<EntityItem> eis = (List<EntityItem>)world.getEntitiesWithinAABB(EntityItem.class, bounds);
			if(Config.instance().traceRites()) {
				for(EntityItem e: eis) {
					Log.instance().traceRite(String.format(" * found: %s, distance: %f", e.getEntityItem().toString(), ElderSacrifice.distance(e.posX, e.posY, e.posZ, posX, posY, posZ)));
				}
			}

			for(EntityItem e: eis) {
				ItemStack stack = e.getEntityItem();
				if(!ElderSacrificeItem.isItemEqual(this.stack, stack) || !(ElderSacrifice.distance(e.posX, e.posY, e.posZ, posX, posY, posZ) <= (double)this.maxDistance)) {
					continue;
				}
				ItemStack sacrificedItemstack = ItemStack.copyItemStack(stack);
				sacrificedItemstack.stackSize = 1;
				ritual.sacrificedItems.add(new SacrificedItem(sacrificedItemstack, new Coord(e)));
				if(stack.isStackable() && stack.stackSize > 1) {
					--stack.stackSize;
				}
				else {
					world.removeEntity(e);
				}

				ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_POP, e, 0.5, 1.0, RADIUS);
				return Result.COMPLETED;
			}
			for(int x = posX - 5; x <= posX + 5; ++x) {
				for(int z = posZ - 5; z <= posZ + 5; ++z) {
					Block blockID = world.getBlock(x, posY, z);
					if(blockID != Witchery.Blocks.GRASSPER) {
						continue;
					}
					TileEntity tile = world.getTileEntity(x, posY, z);
					if(!(tile instanceof BlockGrassper.TileEntityGrassper)) {
						continue;
					}
					IInventory grassper = (IInventory)tile;
					ItemStack stack = grassper.getStackInSlot(0);
					if(stack == null || !ElderSacrificeItem.isItemEqual(this.stack, stack)) {
						continue;
					}
					ItemStack sacrificedItemstack = ItemStack.copyItemStack(stack);
					sacrificedItemstack.stackSize = 1;
					ritual.sacrificedItems.add(new SacrificedItem(sacrificedItemstack, new Coord(tile)));
					if(stack.isStackable() && stack.stackSize > 1) {
						--stack.stackSize;
					}
					else {
						grassper.setInventorySlotContents(0, null);
					}
					ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_POP, world, 0.5 + (double)x, 0.8 + (double)posY, 0.5 + (double)z, 0.5, 1.0, RADIUS);
					return Result.COMPLETED;
				}
			}
			
			if(showMessages) {
				RiteRegistry.RiteError("witchery.rite.missingitem", ritual.getInitiatingPlayerName(), world);
			}
			return Result.ABORTED_REFUND;
		}

		public String toString() {
			return String.format("StepElderSacrificeItem: %s, maxDistance: %d", this.stack.toString(), this.maxDistance);
		}
		
	}
	
}
