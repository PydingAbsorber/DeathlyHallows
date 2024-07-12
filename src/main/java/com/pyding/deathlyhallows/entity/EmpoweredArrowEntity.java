package com.pyding.deathlyhallows.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EmpoweredArrowEntity extends Entity {
	EntityLivingBase owner = null;
	float damage = 0;
	float radius = 0;
	DamageSource source = null;
	public EmpoweredArrowEntity(World p_i1582_1_) {
		super(p_i1582_1_);
	}

	public EmpoweredArrowEntity(World p_i1582_1_, EntityLivingBase owner, float damage, float radius, DamageSource source) {
		super(p_i1582_1_);
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
