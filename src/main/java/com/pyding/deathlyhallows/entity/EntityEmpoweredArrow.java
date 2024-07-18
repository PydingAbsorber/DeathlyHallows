package com.pyding.deathlyhallows.entity;

import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.DHUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Random;

public class EntityEmpoweredArrow extends Entity{
	EntityLivingBase owner = null;
	float damage = 0;
	float radius = 0;
	DamageSource source = null;

	public EntityEmpoweredArrow(World world) {
		super(world);
		setSize(0.5F, 0.5F);
	}

	public EntityEmpoweredArrow(World world, EntityLivingBase owner, float damage, float radius, DamageSource source) {
		super(world);
		setSize(0.5F, 0.5F);
		this.owner = owner;
		this.damage = damage;
		this.radius = radius;
		this.source = source;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if(owner != null){
			this.noClip = true;
			float speed = 1;
			Vec3 lookVec = owner.getLookVec();
			this.motionX = lookVec.xCoord * speed;
			this.motionY = lookVec.yCoord * speed;
			this.motionZ = lookVec.zCoord * speed;
			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			//this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.setRotation(owner.rotationYaw, owner.rotationPitch);
			Random random = new Random();
			float radius = 0.5f;
			double angle = Math.toRadians(ticksExisted * 20);
			int particleCount = 1;
			for (int i = 0; i < particleCount; i++) {
				double numba = random.nextDouble();
				double offsetX = radius * Math.cos(angle);
				double offsetZ = radius * Math.sin(angle);
				double offsetY = radius * Math.cos(angle);
				double particleX = posX + offsetX + numba;
				double particleY = posY + offsetY + numba;
				double particleZ = posZ + offsetZ + numba;
				ParticleEffect.MAGIC_CRIT.send(null, worldObj, particleX, particleY, particleZ, 1, 1, 64);
			}
			radius += 1;
			particleCount *= 2;
			for (int i = 0; i < particleCount; i++) {
				double numba = random.nextDouble();
				double offsetX = radius * Math.cos(angle);
				double offsetZ = radius * Math.sin(angle);
				double offsetY = radius * Math.cos(angle);
				double particleX = posX + offsetX + numba;
				double particleY = posY + offsetY + numba;
				double particleZ = posZ + offsetZ + numba;
				ParticleEffect.MAGIC_CRIT.send(null, worldObj, particleX, particleY, particleZ, 1, 1, 64);
			}
			ParticleEffect.MAGIC_CRIT.send(null, worldObj, posX, posY, posZ, 1, 1, 64);
			if(ticksExisted % 5 == 0) {
				for(EntityLivingBase entity: DHUtil.getEntitiesAround(this, this.radius, false)) {
					if(entity != owner) {
						entity.hurtResistantTime = 0;
						entity.attackEntityFrom(source,damage);
					}
				}
			}
			if(ticksExisted > 200)
				this.kill();
		}
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {

	}
}
