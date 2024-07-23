package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.blocks.BlockGrassper;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.ritual.Sacrifice;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Const;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.blocks.ElderRitualBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ElderSacrificeItem extends ElderSacrifice{
	final ItemStack[] itemstacks;

	public ElderSacrificeItem(ItemStack... itemstacks) {
		this.itemstacks = itemstacks;
	}

	public void addDescription(StringBuffer sb) {
		ItemStack[] arr$ = this.itemstacks;
		int len$ = arr$.length;

		for(int i$ = 0; i$ < len$; ++i$) {
			ItemStack itemstack = arr$[i$];
			sb.append("§8>§0 ");
			if (itemstack.getItem() == Items.potionitem) {
				List list = Items.potionitem.getEffects(itemstack);
				if (list != null && !list.isEmpty()) {
					PotionEffect effect = (PotionEffect)list.get(0);
					String s = itemstack.getDisplayName();
					if (effect.getAmplifier() > 0) {
						s = s + " " + StatCollector.translateToLocal("potion.potency." + effect.getAmplifier()).trim();
					}

					if (effect.getDuration() > 20) {
						s = s + " (" + Potion.getDurationString(effect) + ")";
					}

					sb.append(s);
				} else {
					sb.append(itemstack.getDisplayName());
				}
			} else {
				sb.append(itemstack.getDisplayName());
			}

			sb.append(Const.BOOK_NEWLINE);
		}

	}

	public boolean isMatch(World world, int posX, int posY, int posZ, int maxDistance, ArrayList<Entity> entities, ArrayList<ItemStack> grassperStacks) {
		ArrayList<Entity> itemsToRemove = new ArrayList();
		ArrayList<ItemStack> otherItemsToRemove = new ArrayList();

		for(int j = 0; j < this.itemstacks.length; ++j) {
			boolean found = false;

			int i;
			for(i = 0; i < entities.size(); ++i) {
				if (entities.get(i) instanceof EntityItem) {
					EntityItem entity = (EntityItem)entities.get(i);
					if (isItemEqual(this.itemstacks[j], entity.getEntityItem()) && !itemsToRemove.contains(entity) && distance(entity.posX, entity.posY, entity.posZ, (double)posX, (double)posY, (double)posZ) <= (double)maxDistance) {
						itemsToRemove.add(entity);
						found = true;
						break;
					}
				}
			}

			for(i = 0; i < grassperStacks.size(); ++i) {
				if (isItemEqual(this.itemstacks[j], (ItemStack)grassperStacks.get(i)) && !otherItemsToRemove.contains(grassperStacks.get(i))) {
					otherItemsToRemove.add(grassperStacks.get(i));
					found = true;
					break;
				}
			}

			if (!found) {
				return false;
			}
		}

		Iterator i$ = itemsToRemove.iterator();

		while(i$.hasNext()) {
			Entity itemToRemove = (Entity)i$.next();
			entities.remove(itemToRemove);
		}

		i$ = otherItemsToRemove.iterator();

		while(i$.hasNext()) {
			ItemStack itemToRemove = (ItemStack)i$.next();
			grassperStacks.remove(itemToRemove);
		}

		return true;
	}

	public static boolean isItemEqual(ItemStack itemstackToFind, ItemStack itemstackFound) {
		return (itemstackFound.getItem() == Witchery.Items.ARTHANA || itemstackFound.getItem() == Witchery.Items.BOLINE) && itemstackFound.getItem() == itemstackToFind.getItem() || itemstackFound.isItemEqual(itemstackToFind);
	}

	public void addSteps(ArrayList<ElderRitualStep> steps, AxisAlignedBB bounds, int maxDistance) {
		ItemStack[] arr$ = this.itemstacks;
		int len$ = arr$.length;

		for(int i$ = 0; i$ < len$; ++i$) {
			ItemStack itemstack = arr$[i$];
			steps.add(new ElderSacrificeItem.StepElderSacrificeItem(itemstack, bounds, maxDistance));
		}

	}

	protected static class StepElderSacrificeItem extends ElderRitualStep {
		protected final ItemStack itemstack;
		protected final AxisAlignedBB bounds;
		protected final int maxDistance;
		protected boolean showMessages;

		public StepElderSacrificeItem(ItemStack itemstack, AxisAlignedBB bounds, int maxDistance) {
			super(false);
			this.itemstack = itemstack;
			this.bounds = bounds;
			this.maxDistance = maxDistance;
			this.showMessages = false;
		}


		@Override
		public Result elderProcess(World world, int posX, int posY, int posZ, long ticks, ElderRitualBlock.TileEntityCircle.ActivatedElderRitual ritual) {
			if (ticks % 20L != 0L) {
				return Result.STARTING;
			} else {
				if (!world.isRemote) {
					List itemEntities = world.getEntitiesWithinAABB(EntityItem.class, this.bounds);
					Iterator i$;
					Object obj;
					EntityItem entity;
					ItemStack foundItemstack;
					if (Config.instance().traceRites()) {
						i$ = itemEntities.iterator();

						while(i$.hasNext()) {
							obj = i$.next();
							entity = (EntityItem)obj;
							foundItemstack = entity.getEntityItem();
							Log.instance().traceRite(String.format(" * found: %s, distance: %f", foundItemstack.toString(), ElderSacrifice.distance(entity.posX, entity.posY, entity.posZ, (double)posX, (double)posY, (double)posZ)));
						}
					}

					i$ = itemEntities.iterator();

					while(i$.hasNext()) {
						obj = i$.next();
						entity = (EntityItem)obj;
						foundItemstack = entity.getEntityItem();
						if (ElderSacrificeItem.isItemEqual(this.itemstack, foundItemstack) && ElderSacrifice.distance(entity.posX, entity.posY, entity.posZ, (double)posX, (double)posY, (double)posZ) <= (double)this.maxDistance) {
							ItemStack sacrificedItemstack = ItemStack.copyItemStack(foundItemstack);
							sacrificedItemstack.stackSize = 1;
							ritual.sacrificedItems.add(new RitualStep.SacrificedItem(sacrificedItemstack, new Coord(entity)));
							if (foundItemstack.isStackable() && foundItemstack.stackSize > 1) {
								--foundItemstack.stackSize;
							} else {
								world.removeEntity(entity);
							}

							ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_POP, entity, 0.5, 1.0, 16);
							return Result.COMPLETED;
						}
					}
					for(int x = posX - 5; x <= posX + 5; ++x) {
						for(int z = posZ - 5; z <= posZ + 5; ++z) {
							Block blockID = world.getBlock(x, posY, z);
							if (blockID == Witchery.Blocks.GRASSPER) {
								TileEntity tile = world.getTileEntity(x, posY, z);
								if (tile != null && tile instanceof BlockGrassper.TileEntityGrassper) {
									BlockGrassper.TileEntityGrassper grassper = (BlockGrassper.TileEntityGrassper)tile;
									ItemStack stack = grassper.getStackInSlot(0);
									if (stack != null && ElderSacrificeItem.isItemEqual(this.itemstack, stack)) {
										ItemStack sacrificedItemstack = ItemStack.copyItemStack(stack);
										sacrificedItemstack.stackSize = 1;
										ritual.sacrificedItems.add(new RitualStep.SacrificedItem(sacrificedItemstack, new Coord(tile)));
										if (stack.isStackable() && stack.stackSize > 1) {
											--stack.stackSize;
										} else {
											grassper.setInventorySlotContents(0, (ItemStack)null);
										}

										ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_POP, world, 0.5 + (double)x, 0.8 + (double)posY, 0.5 + (double)z, 0.5, 1.0, 16);
										return Result.COMPLETED;
									}
								}
							}
						}
					}
				}

				if (this.showMessages) {
					RiteRegistry.RiteError("witchery.rite.missingitem", ritual.getInitiatingPlayerName(), world);
				}

				return Result.ABORTED_REFUND;
			}
		}

		public String toString() {
			return String.format("StepElderSacrificeItem: %s, maxDistance: %d", this.itemstack.toString(), this.maxDistance);
		}
	}
}
