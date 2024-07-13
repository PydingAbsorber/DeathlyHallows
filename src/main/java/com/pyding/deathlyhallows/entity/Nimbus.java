package com.pyding.deathlyhallows.entity;

import com.emoniph.witchery.entity.EntityBroom;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.InfusedBrewEffect;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.DeathHallowsMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class Nimbus extends EntityBroom {
	boolean riderHasOwlFamiliar;
	boolean riderHasSoaringBrew;

	public float speedModifier() {
		float modifier = 1;
		if(riderHasOwlFamiliar) {
			modifier += 0.7;
		}
		if(riderHasSoaringBrew) {
			modifier += 0.6;
		}
		return modifier;
	}

	public Nimbus(World world) {
		super(world);
		this.setSize(1.2F, 0.5F);
	}

	private EntityPlayer rider = null;

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if(ticksExisted > 4000 * speedModifier()) {
			this.setDead();
		}
		if(this.riddenByEntity != null) {
			if(this.riddenByEntity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)this.riddenByEntity;
				if(rider == null) {
					rider = player;
				}
				float yaw = player.rotationYaw;
				float pitch = player.rotationPitch;
				double speed = 0.7 * speedModifier();
				if(player.isSprinting() || this.isSprinting()) {
					speed *= 2;
				}
				double motionX = -Math.sin(Math.toRadians(yaw)) * speed;
				double motionZ = Math.cos(Math.toRadians(yaw)) * speed;
				double motionY = Math.sin(Math.toRadians(-pitch)) * speed * 2;
				this.motionX = motionX;
				this.motionY = motionY;
				this.motionZ = motionZ;
				this.rotationYaw = yaw - 90;
				ParticleEffect.FLAME.send(SoundEffect.NONE, player, 1, 1, 64);
			}
		}
		else {
			if(rider != null) {
				int count = 0;
				for(int i = 0; i < rider.inventory.getSizeInventory(); i++) {
					if(rider.inventory.getStackInSlot(i) != null && count == 0) {
						ItemStack stack = rider.inventory.getStackInSlot(i);
						if(stack.getItem() == DeathHallowsMod.nimbus && stack.getTagCompound() != null) {
							stack.getTagCompound().setInteger("NimbusCooldown", ticksExisted);
							count++;
						}
					}
				}
				this.setDead();
			}
		}
	}

	@Override
	public void setBrushColor(int color) {
		super.setBrushColor(color);
	}

	@Override
	public int getBrushColor() {
		return super.getBrushColor();
	}

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
				this.riderHasOwlFamiliar = Familiar.hasActiveBroomMasteryFamiliar(player);
				this.riderHasSoaringBrew = InfusedBrewEffect.Soaring.isActive(player);
				player.mountEntity(this);
			}
		}
		return true;
	}
}
