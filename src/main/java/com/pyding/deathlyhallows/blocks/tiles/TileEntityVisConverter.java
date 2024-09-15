package com.pyding.deathlyhallows.blocks.tiles;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockAltar;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.util.ParticleEffect;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.TileArcaneWorkbench;
import thaumcraft.common.tiles.TileNodeEnergized;

public class TileEntityVisConverter extends TileEntity {
	
	public long clientTicks = 0L;
	
	@Override
	public void updateEntity() {
		if(worldObj.isRemote) {
			++clientTicks;
			return;
		}
		TileEntity down = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
		if(down instanceof TileArcaneWorkbench && worldObj.getTotalWorldTime() % 20 == 0) {
			tryChargeWand((TileArcaneWorkbench)down);
			return;
		}
		if(!(down instanceof BlockAltar.TileEntityAltar)) {
			return;
		}
		BlockAltar.TileEntityAltar altar = (BlockAltar.TileEntityAltar)down;
		TileNodeEnergized aura = findNearestEnergizedNode(MathHelper.ceiling_float_int(altar.getRange()));
		if(aura == null) {
			return;
		}
		
		int total = aura.getAspects().visSize();
		if(worldObj.getTotalWorldTime() % 20 == 0) {
			ParticleEffect.PORTAL.send(null, worldObj, xCoord, yCoord, zCoord + 1, 2, 1, 16);
		}
		NBTTagCompound altarNBT = new NBTTagCompound();
		altar.writeToNBT(altarNBT);
		int x = altarNBT.getInteger("CoreX");
		int y = altarNBT.getInteger("CoreY");
		int z = altarNBT.getInteger("CoreZ");
		BlockAltar.TileEntityAltar core = (BlockAltar.TileEntityAltar)worldObj.getTileEntity(x, y, z);
		if(core != null) {
			NBTTagCompound coreNBT = new NBTTagCompound();
			core.writeToNBT(coreNBT);
			// vanilla softcap is 4490
			coreNBT.setFloat("MaxPower", 4400 + total * 10);
			coreNBT.setInteger("RechargeScale", 12 + total / 10);
			int range = 1;
			boolean pentacle = false;
			boolean arthana = false;
			for(int i = 1; i < 3; i++){
				for(int j = 1; j < 3; j++){
					Block block = core.getWorld().getBlock(x+i,y+1,z+j);
					if(!arthana && Witchery.Items.ARTHANA == Item.getItemFromBlock(block)) {
						range++;
						arthana = true;
					}
					if(!pentacle && Witchery.Items.GENERIC.itemKobolditePentacle.isMatch(new ItemStack(Item.getItemFromBlock(block)))) {
						range *= 2;
						pentacle = true;
					}
				}
			}
			range *= 4;
			coreNBT.setInteger("RangeScale",range);
			core.readFromNBT(coreNBT);
			core.markDirty();
			worldObj.markBlockForUpdate(x, y, z);
		}
	}

	private TileNodeEnergized findNearestEnergizedNode(int radius) {
		TileNodeEnergized aura = null;
		double dist = Double.MAX_VALUE;
		for(int x = -radius; x <= radius; ++x) {
			for(int y = -radius; y <= radius; ++y) {
				for(int z = -radius; z <= radius; ++z) {
					TileEntity tile = worldObj.getTileEntity(xCoord + x, yCoord + y, zCoord + z);
					if(!(tile instanceof TileNodeEnergized)) {
						continue;
					}
					double newDist = tile.getDistanceFrom(xCoord + 0.5D, yCoord  + 0.5D, zCoord  + 0.5D);
					if(newDist < dist) {
						dist = newDist;
						aura = (TileNodeEnergized)tile;
					}
				}
			}
		}
		return aura;
	}

	private void tryChargeWand(IInventory tile) {
		IPowerSource altar = PowerSources.findClosestPowerSource(this);
		if(altar == null || !(altar.getCurrentPower() > 100)) {
			return;
		}
		ItemStack wand = tile.getStackInSlot(10);
		if(wand == null || !(wand.getItem() instanceof ItemWandCasting)) {
			return;
		}
		ItemWandCasting itemWand = ((ItemWandCasting)wand.getItem());
		// .getAspects() gives Aspect[1]{null}, .getAspectsSorted() gives Aspect[0]{} when there is empty list
		for(Aspect aspect: itemWand.getAspectsWithRoom(wand).getAspectsSorted()) {
			int drain = Math.min(100, itemWand.getMaxVis(tile.getStackInSlot(10)) - itemWand.getVis(tile.getStackInSlot(10), aspect));
			if(drain <= 0) {
				continue;
			}
			itemWand.addRealVis(tile.getStackInSlot(10), aspect, 1000, true);
			altar.consumePower(100);
			ParticleEffect.PORTAL.send(null, worldObj, xCoord, yCoord, zCoord, 2, 2, 16);
			ParticleEffect.PORTAL.send(null, worldObj, altar.getLocation(), 2, 2, 16);
		}
	}

}
