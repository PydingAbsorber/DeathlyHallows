package com.pyding.deathlyhallows.entities;

import com.pyding.deathlyhallows.utils.DHUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.awt.*;
import java.util.Random;

public class EntityEmpoweredArrow extends Entity {
	
	private static final int WATCHER_SHOOTER = 22;
	private EntityPlayer shooter;
	private float damage = 0;
	private float radius = 0;
	private DamageSource source = null;
	private int type = 0;

	public EntityEmpoweredArrow(World world) {
		super(world);
		setSize(0.5F, 0.5F);
	}

	public EntityEmpoweredArrow(World world, EntityPlayer shooter, float damage, float radius, DamageSource source, int type) {
		this(world);
		setSize(0.5F, 0.5F);
		setShooter(shooter);
		setRotation(shooter.rotationYaw, shooter.rotationPitch);
		this.damage = damage;
		this.radius = radius;
		this.source = source;
		this.type = type;
	}
	
	@Override
	protected void entityInit () {
		dataWatcher.addObject(WATCHER_SHOOTER, "");
	}

	private void setShooter(EntityPlayer shooter) {
		this.shooter = shooter;
		dataWatcher.updateObject(WATCHER_SHOOTER, shooter.getCommandSenderName());
	}
	
	private EntityPlayer getShooter() {
		if(shooter == null) {
			shooter = worldObj.getPlayerEntityByName(dataWatcher.getWatchableObjectString(WATCHER_SHOOTER));
		}
		return shooter;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if(ticksExisted <= 200) {
			return;
		}
		kill();
		if(!worldObj.isRemote) {
			if(getShooter() == null) {
				return;
			}
		}
		if(getShooter() != null && type == 1) {
			noClip = true;
			float speed = 1;
			Vec3 lookVec = getShooter().getLookVec();
			motionX = lookVec.xCoord * speed;
			motionY = lookVec.yCoord * speed;
			motionZ = lookVec.zCoord * speed;
			posX += motionX;
			posY += motionY;
			posZ += motionZ;
			spawnParticles();

			if(ticksExisted % 5 == 0) {
				for(EntityLivingBase entity: DHUtils.getEntitiesAround(this, radius, false)) {
					if(entity != getShooter()) {
						entity.hurtResistantTime = 0;
						entity.attackEntityFrom(source, damage);
					}
				}
			}
		}
		if(getShooter() != null && (type == 2 || type == 3)) {
			if(ticksExisted > 20) {
				kill();
			}
			float speed = 10;
			Vec3 lookVec = getShooter().getLookVec();
			motionX = lookVec.xCoord * speed;
			motionY = lookVec.yCoord * speed;
			motionZ = lookVec.zCoord * speed;
			posX += motionX;
			posY += motionY;
			posZ += motionZ;
			spawnParticlesAt(lookVec);
			for(EntityLivingBase entity: DHUtils.getEntitiesAround(this, radius, false)) {
				if(entity != getShooter()) {
					entity.hurtResistantTime = 0;
					entity.attackEntityFrom(source, damage + (entity.getDistanceToEntity(getShooter())));
				}
			}
		}
	}

	private void spawnParticlesAt(Vec3 lookVec) {
		float scale = radius * 8;
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
			DHUtils.spawnParticle(this, posX, posY, posZ, color, 0.9f, scale, 60, 1, 0, 0, 0);
		}
	}

	private void spawnParticles() {
		Random random = new Random();
		float radius = 0.5f;
		double angle = Math.toRadians(ticksExisted * 20);
		DHUtils.spawnParticle(this, posX, posY, posZ, Color.BLUE, 1, 2, 60, 1, 0, 0, 0);
		int particleCount = 1;
		for(int i = 0; i < particleCount; i++) {
			double numba = random.nextDouble();
			double offsetX = radius * Math.cos(angle);
			double offsetZ = radius * Math.sin(angle);
			double offsetY = radius * Math.cos(angle);
			double particleX = posX + offsetX + numba;
			double particleY = posY + offsetY + numba;
			double particleZ = posZ + offsetZ + numba;
			DHUtils.spawnParticle(this, particleX, particleY, particleZ, Color.BLUE, 1, 2, 60, 1, 0, 0, 0);
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
			DHUtils.spawnParticle(this, particleX, particleY, particleZ, Color.CYAN, 1, 2, 60, 1, 0, 0, 0);
		}
	}

	@Override
	protected void readEntityFromNBT (NBTTagCompound nbtTagCompound){

	}

	@Override
	protected void writeEntityToNBT (NBTTagCompound nbtTagCompound){

	}
	
}
