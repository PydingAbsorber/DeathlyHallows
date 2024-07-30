package com.pyding.deathlyhallows.blocks.tiles;

import com.emoniph.witchery.blocks.BlockAltar;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.util.ParticleEffect;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.Constants;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.TileArcaneWorkbench;
import thaumcraft.common.tiles.TileNodeEnergized;

public class TileEntityVisConverter extends TileEntity {
	@Override
	public void updateEntity() {
		if(worldObj.isRemote) {
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
		NBTTagCompound tagCompound = new NBTTagCompound();
		aura.writeToNBT(tagCompound);
		int aer = 0;
		int terra = 0;
		int ignis = 0;
		int ordo = 0;
		int perditio = 0;
		int aqua = 0;
		if(tagCompound.hasKey("AEB")) {
			NBTTagList aebList = tagCompound.getTagList("AEB", Constants.NBT.TAG_COMPOUND);
			for(int i = 0; i < aebList.tagCount(); i++) {
				NBTTagCompound aebCompound = aebList.getCompoundTagAt(i);
				int amount = aebCompound.getInteger("amount");
				String key = aebCompound.getString("key");
				if("aer".equals(key)) {
					aer = amount;
				}
				else if("terra".equals(key)) {
					terra = amount;
				}
				else if("ignis".equals(key)) {
					ignis = amount;
				}
				else if("ordo".equals(key)) {
					ordo = amount;
				}
				else if("perditio".equals(key)) {
					perditio = amount;
				}
				else if("aqua".equals(key)) {
					aqua = amount;
				}
			}
		}
		int total = aer + terra + ignis + ordo + perditio + aqua;
		if(worldObj.getTotalWorldTime() % 20 == 0) {
			ParticleEffect.PORTAL.send(null, worldObj, xCoord, yCoord, zCoord + 1, 2, 1, 16);
		}

		NBTTagCompound altarNBT = new NBTTagCompound();
		altar.writeToNBT(altarNBT);
		int x = altarNBT.getInteger("CoreX");
		int y = altarNBT.getInteger("CoreY");
		int z = altarNBT.getInteger("CoreZ");
		BlockAltar.TileEntityAltar coreAltar = (BlockAltar.TileEntityAltar)worldObj.getTileEntity(x, y, z);
		if(coreAltar != null) {
			NBTTagCompound coreNBT = new NBTTagCompound();
			coreAltar.writeToNBT(coreNBT);
			float maxPower = coreAltar.getCoreMaxPower();
			coreNBT.setFloat("MaxPower", 3850 + total * 10);
			coreNBT.setInteger("RechargeScale", 12 + total / 10);
			coreAltar.readFromNBT(coreNBT);
			coreAltar.markDirty();
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
		for(Aspect aspect: itemWand.getAspectsWithRoom(wand).getAspects()) {
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

	@Override
	public boolean canUpdate() {
		return true;
	}
	
}
