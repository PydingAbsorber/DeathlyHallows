package com.pyding.deathlyhallows.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityEmpoweredArrow extends Entity {
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
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {

	}

}
