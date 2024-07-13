package com.pyding.deathlyhallows.blocks;

import com.emoniph.witchery.blocks.BlockAltar;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.util.ParticleEffect;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.tiles.TileArcaneWorkbench;
import thaumcraft.common.tiles.TileMagicWorkbench;
import thaumcraft.common.tiles.TileNodeEnergized;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class VisConverterTile extends TileEntity {
	@Override
	public void updateEntity() {
		if(!worldObj.isRemote) {
			EntityPlayer player = null;
			double radius = 20;
			List entities = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(this.xCoord - radius, this.yCoord - radius, this.zCoord - radius, this.xCoord + radius, this.zCoord + radius, this.zCoord + radius));
			for(Object o: entities) {
				player = (EntityPlayer)o;
				break;
			}
			int posX = xCoord;
			int posY = yCoord;
			int posZ = zCoord;
			if(worldObj.getTileEntity(posX, posY - 1, posZ) != null && worldObj.getTileEntity(posX, posY - 1, posZ) instanceof BlockAltar.TileEntityAltar) {
				BlockAltar.TileEntityAltar altar = (BlockAltar.TileEntityAltar)worldObj.getTileEntity(posX, posY - 1, posZ);
				int searchRadius = (int)altar.getRange();
				double nearestDistanceSq = Double.MAX_VALUE;
				int nearestBlockX = 0;
				int nearestBlockY = 0;
				int nearestBlockZ = 0;
				for(int offsetX = -searchRadius; offsetX <= searchRadius; offsetX++) {
					for(int offsetY = -searchRadius; offsetY <= searchRadius; offsetY++) {
						for(int offsetZ = -searchRadius; offsetZ <= searchRadius; offsetZ++) {
							int currentX = posX + offsetX;
							int currentY = posY + offsetY;
							int currentZ = posZ + offsetZ;

							TileEntity block = worldObj.getTileEntity(currentX, currentY, currentZ);
							if(block != null && block instanceof TileNodeEnergized) {
								double distanceSq = (posX - currentX) * (posX - currentX) +
										(posY - currentY) * (posY - currentY) +
										(posZ - currentZ) * (posZ - currentZ);

								if(distanceSq < nearestDistanceSq) {
									nearestDistanceSq = distanceSq;
									nearestBlockX = currentX;
									nearestBlockY = currentY;
									nearestBlockZ = currentZ;
								}
							}
						}
					}
				}

				if(nearestBlockX != 0 || nearestBlockY != 0 || nearestBlockZ != 0) {
					TileEntity aura = worldObj.getTileEntity(nearestBlockX, nearestBlockY, nearestBlockZ);
					NBTTagCompound tagCompound = new NBTTagCompound();
					aura.writeToNBT(tagCompound);
					int aer = 0;
					int terra = 0;
					int ignis = 0;
					int ordo = 0;
					int perditio = 0;
					int aqua = 0;
					if(tagCompound != null && tagCompound.hasKey("AEB")) {
						NBTTagList aebList = tagCompound.getTagList("AEB", 10); // 10 is the tag type for compound tags

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
						ParticleEffect.PORTAL.send(null, worldObj, posX, posY, posZ + 1, 2, 1, 16);
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
			}
			else if(worldObj.getTileEntity(posX, posY - 1, posZ) != null && worldObj.getTileEntity(posX, posY - 1, posZ) instanceof TileArcaneWorkbench && worldObj.getTotalWorldTime() % 20 == 0) {
				IPowerSource altar = PowerSources.findClosestPowerSource(this);
				TileEntity te = this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
				if(altar != null && altar.getCurrentPower() > 100) {
					if(te != null && te instanceof TileMagicWorkbench) {
						TileMagicWorkbench tm = (TileMagicWorkbench)te;
						ItemStack wand = tm.getStackInSlot(10);
						if(wand != null && wand.getItem() instanceof ItemWandCasting) {
							AspectList al = ((ItemWandCasting)wand.getItem()).getAspectsWithRoom(wand);
							if(al.size() > 0) {
								Aspect[] arr$ = al.getAspects();
								int len$ = arr$.length;

								for(int i$ = 0; i$ < len$; ++i$) {
									Aspect aspect = arr$[i$];
									int drain = Math.min(100, ((ItemWandCasting)wand.getItem()).getMaxVis(tm.getStackInSlot(10)) - ((ItemWandCasting)wand.getItem()).getVis(tm.getStackInSlot(10), aspect));
									if(drain > 0) {
										((ItemWandCasting)wand.getItem()).addRealVis(tm.getStackInSlot(10), aspect, 1000, true);
										altar.consumePower(100);
										ParticleEffect.PORTAL.send(null, worldObj, posX, posY, posZ, 2, 2, 16);
										ParticleEffect.PORTAL.send(null, worldObj, altar.getLocation(), 2, 2, 16);
									}
								}
							}
						}
					}
				}
			}
		}
		super.updateEntity();
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
	}

	public void vzlomJopi2(BlockAltar.TileEntityAltar altar, int power) throws NoSuchFieldException, IllegalAccessException {
		Class<?> tileEntityAltarClass = BlockAltar.TileEntityAltar.class;
		Field rechargeScale = tileEntityAltarClass.getDeclaredField("rechargeScale");
		rechargeScale.setAccessible(true);
		rechargeScale.setInt(altar, altar.getCoreSpeed() + power / 10);
		Field maxPowerField = tileEntityAltarClass.getDeclaredField("maxPower");
		maxPowerField.setAccessible(true);
		maxPowerField.setFloat(altar, altar.getCoreMaxPower() + power * 10);
	}

	public void vzlomJopi(int power) {
		try {
			Class<?> blockAltarClass = BlockAltar.class;
			Class<?>[] declaredClasses = blockAltarClass.getDeclaredClasses();
			for(Class<?> declaredClass: declaredClasses) {
				if(declaredClass.getSimpleName().equals("PowerSource")) {
					Method createInMapMethod = declaredClass.getDeclaredMethod("createInMap", Map.class, Block.class, int.class, int.class);
					Object powerSourceInstance = declaredClass.newInstance();
					createInMapMethod.setAccessible(true);
					//createInMapMethod.invoke(powerSourceInstance,);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
