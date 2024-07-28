package com.pyding.deathlyhallows.entity;

import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.InfusedBrewEffect;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.items.ModItems;
import com.pyding.deathlyhallows.items.Nimbus3000;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class Nimbus extends Entity {

	public float speedModifier(EntityPlayer player) {
		float modifier = 1;
		if(Familiar.hasActiveBroomMasteryFamiliar(player)) {
			modifier += 0.7f;
		}
		if(InfusedBrewEffect.Soaring.isActive(player)) {
			modifier += 0.6f;
		}
		return modifier;
	}

	public Nimbus(World world) {
		super(world);
		this.setSize(1.2F, 0.5F);
	}

	@Override
	protected void entityInit() {
		
	}
	
	private EntityPlayer rider = null;

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if(this.riddenByEntity != null) {
			EntityPlayer player = (EntityPlayer)this.riddenByEntity;
			rider = player;
			if(ticksExisted > 4000 * speedModifier(player)) {
				this.setDead();
				for(ItemStack itemStack : player.inventory.mainInventory){
					if(itemStack != null && itemStack.getItem() == ModItems.nimbus) {
						NBTTagCompound nbt = itemStack.getTagCompound();
						nbt.setLong("NimbusCooldown",System.currentTimeMillis()+(nbt.getLong("NimbusDuration")-System.currentTimeMillis())/2);
						nbt.setLong("NimbusDuration", 0);
					}
				}
			}
			float yaw = player.rotationYaw % 360;
			float pitch = player.rotationPitch % 360;
			this.setRotation(yaw,pitch);
			double speed = 0.7 * speedModifier(player);
			if(player.getEntityData().getBoolean("DHSprint")) {
				speed *= 4;
			}
			double motionX = -Math.sin(Math.toRadians(yaw)) * speed;
			double motionY = Math.sin(Math.toRadians(-pitch)) * speed * 2;
			double motionZ = Math.cos(Math.toRadians(yaw)) * speed;
			this.moveEntity(motionX,motionY,motionZ);
			this.rotationYaw = yaw - 90;
			ParticleEffect.FLAME.send(SoundEffect.NONE, this, 1, 1, 64);
		} else {
			int count = 0;
			if(rider != null) {
				for(int i = 0; i < rider.inventory.getSizeInventory(); i++) {
					if(rider.inventory.getStackInSlot(i) != null && count == 0) {
						ItemStack stack = rider.inventory.getStackInSlot(i);
						if(stack.getItem() == ModItems.nimbus && stack.getTagCompound() != null) {
							if(stack.getTagCompound().getLong("NimbusDuration") > System.currentTimeMillis())
								stack.getTagCompound().setLong("NimbusCooldown", (long)(System.currentTimeMillis()+(System.currentTimeMillis()-(stack.getTagCompound().getLong("NimbusDuration")-160*1000*speedModifier(rider)))/2));
							else stack.getTagCompound().setLong("NimbusCooldown", (long)(System.currentTimeMillis()+(160*1000*speedModifier(rider))/2));
							stack.getTagCompound().setLong("NimbusDuration", 0);
							count++;
						}
					}
				}
			}
			this.setDead();
		}
	}

	@Override
	public boolean shouldDismountInWater(Entity rider) {
		return false;
	}

	/*@Override
	public void setBrushColor(int color) {
		super.setBrushColor(color);
	}

	@Override
	public int getBrushColor() {
		return super.getBrushColor();
	}*/
	
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		return false;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {

	}
	
	public boolean interactFirst(EntityPlayer player) {
		if(super.riddenByEntity == null || !(super.riddenByEntity instanceof EntityPlayer) || super.riddenByEntity == player) {
			if(!super.worldObj.isRemote) {
				player.mountEntity(this);
			}
			this.noClip = false;
		}
		return true;
	}
}
