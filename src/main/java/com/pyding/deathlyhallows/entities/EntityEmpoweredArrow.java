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

	private static final int WATCHER_SHOOTER = 4;
	private EntityPlayer shooter;
	private float 
			damage = 0,
			radius = 0;
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
		setPositionAndRotation(shooter.posX, shooter.posY + shooter.eyeHeight, shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
		setRotation(rotationYaw, rotationPitch);
		this.damage = damage;
		this.radius = radius;
		this.source = source;
		this.type = type;
	}

	@Override
	protected void entityInit() {
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
		if(ticksExisted > 200) {
			kill();
		}
		if(getShooter() == null) {
			return;
		}
		switch(type) {
			default:
			case 1:
				penetratingArrowUpdate();
				break;
			case 2:
			case 3:
				arrowUpdate();
				break;
		}
	}

	private void penetratingArrowUpdate() {
		noClip = true;
		Vec3 lookVec = getShooter().getLookVec();
		moveArrowToLook(lookVec, 1F, getShooter().rotationYaw, getShooter().rotationPitch);
		spawnParticles();
		if(ticksExisted % 5 == 0) {
			attackInRadius();
		}
	}

	private void arrowUpdate() {
		if(ticksExisted > 20) {
			kill();
		}
		Vec3 lookVec = getShooter().getLookVec();
		moveArrowToLook(lookVec, 10F, getShooter().rotationYaw, getShooter().rotationPitch);
		spawnParticlesAt(lookVec);
		attackInRadius();
	}

	private void moveArrowToLook(Vec3 lookVec, float speed, float yaw, float pitch) {
		motionX = lookVec.xCoord * speed;
		motionY = lookVec.yCoord * speed;
		motionZ = lookVec.zCoord * speed;
		setPosition(posX + motionX, posY + motionY, posZ + motionZ);
		setRotation(yaw, pitch);
	}

	private void attackInRadius() {
		for(EntityLivingBase entity: DHUtils.getEntitiesAround(EntityLivingBase.class, this, radius)) {
			if(entity == getShooter()) {
				continue;
			}
			int hrt = entity.hurtResistantTime;
			entity.hurtResistantTime = 0;
			entity.attackEntityFrom(source, damage);
			entity.hurtResistantTime = hrt;
		}
	}

	private void spawnParticlesAt(Vec3 lookVec) {
		float scale = radius * 8;
		Color color = type == 2 ? Color.BLUE : Color.MAGENTA;

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
	protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {

	}

}
