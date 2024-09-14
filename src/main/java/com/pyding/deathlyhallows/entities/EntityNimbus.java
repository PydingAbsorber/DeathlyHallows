package com.pyding.deathlyhallows.entities;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityBroom;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.ItemNimbus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityNimbus extends EntityBroom {

	private static final int
			WATCHER_NAME = 10,
			WATCHER_COLOR = 16,
			WATCHER_TIMESINCEHIT = 17,
			WATCHER_FORWARD = 18,
			WATCHER_DAMAGE = 19;

	public EntityNimbus(World world) {
		super(world);
		setSize(1.2F, 0.5F);
	}

	@Override
	protected void entityInit() {
		// override withcery reg cuz need to set another default color
		dataWatcher.addObject(WATCHER_NAME, "");
		dataWatcher.addObject(WATCHER_COLOR, -1);
		dataWatcher.addObject(WATCHER_TIMESINCEHIT, 0);
		dataWatcher.addObject(WATCHER_FORWARD, 1);
		dataWatcher.addObject(WATCHER_DAMAGE, 0F);
	}

	@Override
	public void setBrushColor(int color) {
		dataWatcher.updateObject(WATCHER_COLOR, color);
	}

	@Override
	public int getBrushColor() {
		return dataWatcher.getWatchableObjectInt(WATCHER_COLOR);
	}

	@Override
	public void onUpdate() {
		if(riddenByEntity == null || riddenByEntity.isDead) {
			setDead();
			return;
		}
		noClip = riddenByEntity.noClip;
		setInvisible(riddenByEntity.isInvisible());
		if(worldObj.isRemote 
				&& !isInvisible() 
				&& Vec3.createVectorHelper(motionX, motionY, motionZ)
					   .squareDistanceTo(0, 0,0) > 0.01D
		) {
			double speed  = Vec3.createVectorHelper(motionX, motionY, motionZ).squareDistanceTo(0, 0,0);
			float yaw = (float)Math.PI / 180F * rotationYaw;
			if(ticksExisted % 2 == 0) {
				Witchery.proxy.showParticleEffect(worldObj,
						prevPosX + MathHelper.sin(yaw),
						prevPosY,
						prevPosZ - MathHelper.cos(yaw),
						0.05D, 0.05D,
						SoundEffect.NONE,
						0xFFFFFF,
						ParticleEffect.FLAME
				);
			}
			Witchery.proxy.showParticleEffect(worldObj,
					prevPosX + MathHelper.sin(yaw),
					prevPosY,
					prevPosZ - MathHelper.cos(yaw),
					0.25D, 0.25D,
					SoundEffect.NONE,
					0xA0A0A0,
					ParticleEffect.SMOKE
			);
		}
		// preserving motion to surpass witchery superclass code
		double motionX = this.motionX;
		double motionY = this.motionY;
		double motionZ = this.motionZ;
		this.motionX = 0;
		this.motionY = 0;
		this.motionZ = 0;
		super.onUpdate();
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
		if(riddenByEntity == null) { // something setting this no null in super.onUpdate()
			return;
		}
		setRotation(riddenByEntity.rotationYaw % 360, riddenByEntity.rotationPitch % 360);
		moveBroom();
		if(ticksExisted % 100 == 0) {
			ItemNimbus.addNumbusCooldown(riddenByEntity, 5 + ticksExisted / 20);
			NBTTagCompound tag = riddenByEntity.getEntityData();
			if(tag.getLong("NimbusDuration") < System.currentTimeMillis()) {
				tag.removeTag("NimbusDuration");
				if(!worldObj.isRemote) {
					setDead();
				}
			}
		}
	}

	private void moveBroom() {
		// vec3 is mutable; this is not okay usually but this time it's okay
		Vec3 look = riddenByEntity.getLookVec();
		if(look == null) { // generic case
			return;
		}
		if(riddenByEntity instanceof EntityLivingBase) {
			EntityLivingBase e = (EntityLivingBase)riddenByEntity;
			float forward = e.moveForward;
			look.xCoord *= forward;
			look.yCoord *= forward * 2;
			look.zCoord *= forward;
			float strafe = e.moveStrafing;
			float yaw = (float)Math.PI / 180F * rotationYaw;
			look.xCoord += MathHelper.cos(yaw) * strafe;
			look.zCoord += MathHelper.sin(yaw) * strafe;
			look = look.normalize();
		}
		float speed = 0.7F * ItemNimbus.modifier(riddenByEntity);
		if(riddenByEntity.getEntityData().getBoolean("DHSprint")) {
			speed *= 4;
		}
		// relaxation determines how fast new motion trajectory will be applied
		// relaxation = 0 - never;
		// relaxation = 1 - imminently;
		// can be considered as non-linear acceleration
		final float relaxation = 0.15F;
		motionX += (look.xCoord * speed - motionX) * relaxation;
		motionY += (look.yCoord * speed - motionY) * 0.8;
		motionZ += (look.zCoord * speed - motionZ) * relaxation;
		moveEntity(motionX, motionY, motionZ);
	}
	
	@Override
	public boolean shouldDismountInWater(Entity rider) {
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float damage) {
		return false;
	}

	@Override
	public boolean interactFirst(EntityPlayer player) {
		if(riddenByEntity != null && riddenByEntity instanceof EntityPlayer && riddenByEntity != player) {
			return true;
		}
		if(!worldObj.isRemote) {
			player.mountEntity(this);
		}
		return true;
	}

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target) {
		return new ItemStack(DHItems.nimbus);
	}

}
