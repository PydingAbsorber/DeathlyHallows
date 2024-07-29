package com.pyding.deathlyhallows.entities;

import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketArrowSync;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.awt.*;
import java.util.Random;

public class EntityEmpoweredArrow extends Entity {
	EntityPlayer owner;
	float damage = 0;
	float radius = 0;
	DamageSource source = null;
	int type = 0;

	public EntityEmpoweredArrow(World world) {
		super(world);
		setSize(0.5F, 0.5F);
	}

	public EntityEmpoweredArrow(World world, EntityLivingBase entity, float damage, float radius, DamageSource source, int type) {
		super(world);
		setSize(0.5F, 0.5F);
		this.owner = (EntityPlayer)entity;
		getEntityData().setInteger("DHOwner", owner.getEntityId());
		this.damage = damage;
		this.radius = radius;
		this.source = source;
		this.type = type;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if(ticksExisted > 200) {
			this.kill();
			if(!worldObj.isRemote) {
				if(owner == null) {
					return;
				}
				NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(this.dimension, this.posX, this.posY, this.posZ, 64);
				DHPacketProcessor.sendToAllAround(new PacketArrowSync(owner.getEntityId(), this.getEntityId()), targetPoint);
			}
			if(worldObj.isRemote && owner == null && getEntityData().getInteger("DHOwner") != 0) {
				owner = (EntityPlayer)worldObj.getEntityByID(getEntityData().getInteger("DHOwner"));
			}
			if(owner != null && type == 1) {
				this.noClip = true;
				float speed = 1;
				Vec3 lookVec = owner.getLookVec();
				this.motionX = lookVec.xCoord * speed;
				this.motionY = lookVec.yCoord * speed;
				this.motionZ = lookVec.zCoord * speed;
				this.posX += this.motionX;
				this.posY += this.motionY;
				this.posZ += this.motionZ;
				Random random = new Random();
				float radius = 0.5f;
				double angle = Math.toRadians(ticksExisted * 20);
				DHUtil.spawnParticle(this, this.posX, this.posY, this.posZ, Color.BLUE, 1, 2, 60, 1, 0, 0, 0);
				int particleCount = 1;
				for(int i = 0; i < particleCount; i++) {
					double numba = random.nextDouble();
					double offsetX = radius * Math.cos(angle);
					double offsetZ = radius * Math.sin(angle);
					double offsetY = radius * Math.cos(angle);
					double particleX = posX + offsetX + numba;
					double particleY = posY + offsetY + numba;
					double particleZ = posZ + offsetZ + numba;
					DHUtil.spawnParticle(this, particleX, particleY, particleZ, Color.BLUE, 1, 2, 60, 1, 0, 0, 0);
				}
				radius += 1;
				particleCount *= 2;
				for(int i = 0; i < particleCount; i++) {
					double numba = random.nextDouble();
					double offsetX = radius * Math.cos(angle);
					double offsetZ = radius * Math.sin(angle);
					double offsetY = radius * Math.cos(angle);
					double particleX = posX + offsetX + numba;
					double particleY = posY + offsetY + numba;
					double particleZ = posZ + offsetZ + numba;
					DHUtil.spawnParticle(this, particleX, particleY, particleZ, Color.CYAN, 1, 2, 60, 1, 0, 0, 0);
				}
				//this.moveEntity(this.motionX, this.motionY, this.motionZ);
				this.setRotation(owner.rotationYaw, owner.rotationPitch);
				if(ticksExisted % 5 == 0) {
					for(EntityLivingBase entity: DHUtil.getEntitiesAround(this, this.radius, false)) {
						if(entity != owner) {
							entity.hurtResistantTime = 0;
							entity.attackEntityFrom(source, damage);
						}
					}
				}
			}
			if(owner != null && (type == 2 || type == 3)) {
				if(ticksExisted > 20) {
					this.kill();
				}
				float speed = 10;
				Vec3 lookVec = owner.getLookVec();
				this.motionX = lookVec.xCoord * speed;
				this.motionY = lookVec.yCoord * speed;
				this.motionZ = lookVec.zCoord * speed;
				this.posX += this.motionX;
				this.posY += this.motionY;
				this.posZ += this.motionZ;
				float scale = this.radius * 8;
				Color color;
				if(type == 2) {
					color = Color.BLUE;
				}
				else {
					color = Color.MAGENTA;
				}
				double posX = this.posX;
				double posY = this.posY;
				double posZ = this.posZ;
				for(int i = 0; i < 10; i++) {
					posX += lookVec.xCoord * i;
					posY += lookVec.yCoord * i;
					posZ += lookVec.zCoord * i;
					DHUtil.spawnParticle(this, posX, posY, posZ, color, 0.9f, scale, 60, 1, 0, 0, 0);
				}
				for(EntityLivingBase entity: DHUtil.getEntitiesAround(this, this.radius, false)) {
					if(entity != owner) {
						entity.hurtResistantTime = 0;
						entity.attackEntityFrom(source, damage + (entity.getDistanceToEntity(owner)));
					}
				}
			}
		}

		@Override
		protected void entityInit () {

		}

		@Override
		protected void readEntityFromNBT (NBTTagCompound nbtTagCompound){

		}

		@Override
		protected void writeEntityToNBT (NBTTagCompound nbtTagCompound){

		}
	}
