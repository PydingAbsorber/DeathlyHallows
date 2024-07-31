package com.pyding.deathlyhallows.entities;

import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.InfusedBrewEffect;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.items.DHItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityNimbus extends Entity {

	public EntityNimbus(World world) {
		super(world);
		setSize(1.2F, 0.5F);
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
				setDead();
				for(ItemStack stack: player.inventory.mainInventory) {
					if(stack == null || stack.getItem() != DHItems.nimbus) {
						continue;
					}
					NBTTagCompound tag = stack.getTagCompound();
					tag.setLong("NimbusCooldown", System.currentTimeMillis() + (tag.getLong("NimbusDuration") - System.currentTimeMillis()) / 2);
					tag.setLong("NimbusDuration", 0);
				}
			}
			float yaw = player.rotationYaw % 360;
			float pitch = player.rotationPitch % 360;
			setRotation(yaw, pitch);
			double speed = 0.7 * speedModifier(player);
			if(player.getEntityData().getBoolean("DHSprint")) {
				speed *= 4;
			}
			double motionX = -Math.sin(Math.toRadians(yaw)) * speed;
			double motionY = Math.sin(Math.toRadians(-pitch)) * speed * 2;
			double motionZ = Math.cos(Math.toRadians(yaw)) * speed;
			moveEntity(motionX, motionY, motionZ);
			rotationYaw = yaw - 90;
			ParticleEffect.FLAME.send(SoundEffect.NONE, this, 1, 1, 64);
			return;
		}

		int count = 0;
		if(rider == null) {
			setDead();
			return;
		}

		for(int i = 0; i < rider.inventory.getSizeInventory(); i++) {
			if(rider.inventory.getStackInSlot(i) == null || count != 0) {
				continue;
			}
			ItemStack stack = rider.inventory.getStackInSlot(i);
			if(stack.getItem() != DHItems.nimbus || stack.getTagCompound() == null) {
				continue;
			}
			NBTTagCompound tag = stack.getTagCompound();
			
			if(tag.getLong("NimbusDuration") > System.currentTimeMillis()) {
				tag.setLong("NimbusCooldown", (long)(System.currentTimeMillis() + (System.currentTimeMillis() - (tag.getLong("NimbusDuration") - 160 * 1000 * speedModifier(rider))) / 2));
			}
			else {
				tag.setLong("NimbusCooldown", (long)(System.currentTimeMillis() + (160 * 1000 * speedModifier(rider)) / 2));
			}
			tag.setLong("NimbusDuration", 0);
			count++;
		}
		setDead();
	}

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

	@Override
	public boolean shouldDismountInWater(Entity rider) {
		return false;
	}
	
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
		if(riddenByEntity != null && riddenByEntity instanceof EntityPlayer && riddenByEntity != player) {
			return true;
		}
		if(!worldObj.isRemote) {
			player.mountEntity(this);
		}
		noClip = false;
		return true;
	}
}
