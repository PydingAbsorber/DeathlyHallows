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
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityNimbus extends Entity {

	private static final int
			WATCHER_NAME = 10,
			WATCHER_COLOR = 16;
	private EntityPlayer rider = null;

	public EntityNimbus(World world) {
		super(world);
		setSize(1.2F, 0.5F);
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(10, "");
		dataWatcher.addObject(16, 0x0);
	}

	public void setBrushColor(int color) {
		dataWatcher.updateObject(WATCHER_COLOR, color);
	}

	public int getBrushColor() {
		return dataWatcher.getWatchableObjectInt(WATCHER_COLOR);
	}

	public void setCustomNameTag(String name) {
		dataWatcher.updateObject(WATCHER_NAME, name);
	}

	public String getCustomNameTag() {
		return dataWatcher.getWatchableObjectString(WATCHER_NAME);
	}
	
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
			ParticleEffect.FLAME.send(SoundEffect.NONE, this, 0.25D, 0.25D, 128);
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
	public boolean attackEntityFrom(DamageSource source, float damage) {
		return false;
	}

	@Override
	protected void dealFireDamage(int damage) {

	}

	@Override
	public float getShadowSize() {
		return 0F;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		if(tag.getString("CustomName").length() > 0) {
			setCustomNameTag(tag.getString("CustomName"));
		}
		if(tag.hasKey("BrushColor")) {
			setBrushColor(tag.getInteger("BrushColor"));
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {
		tag.setString("CustomName", getCustomNameTag());
		tag.setInteger("BrushColor", getBrushColor());
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

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target) {
		return new ItemStack(DHItems.nimbus);
	}

}
