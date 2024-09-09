package com.pyding.deathlyhallows.items.baubles;

import baubles.api.BaubleType;
import com.pyding.deathlyhallows.entities.EntityAbsoluteDeath;
import com.pyding.deathlyhallows.utils.DHUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemBaubleInvisibilityMantle extends ItemBaubleBase {
	
	private static final float HEALTH_STEAL_RATIO = 0.05F;

	public ItemBaubleInvisibilityMantle() {
		super("invisibilityMantle", BaubleType.BELT);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase e) {
		if(!(e instanceof EntityPlayer)) {
			return;
		}
		World world = e.worldObj;
		EntityPlayer p = (EntityPlayer)e;
		NBTTagCompound tag = p.getEntityData();
		if(!p.isSneaking()) {
			if(isMantleActive(p)) {
				setMantleAbilityState(p, false);
			}
			return;
		}
		if(tag.getInteger("mantlecd") <= 0) {
			tag.setInteger("mantlecd", 1200);
			tag.setBoolean("mantleActive", true);
			world.playSoundAtEntity(e, "dh:mantle." + DHUtils.getRandomInt(1, 3), 1F, 1F);
		}
		if(!isMantleActive(p)) {
			return;
		}
		Vec3 vel = p.getLookVec();
		final float speed = 0.7F;
		p.motionX = vel.xCoord * speed;
		p.motionY = vel.yCoord * speed;
		p.motionZ = vel.zCoord * speed;
		setMantleAbilityState(p, true);
		p.fallDistance = 0F;
		if(e.ticksExisted % 4 != 0) {
			return;
		}
		@SuppressWarnings("unchecked")
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, p.boundingBox.expand(1, 0, 1));
		for(EntityLivingBase living : entities) {
			if(living == p 
					|| living.isEntityInvulnerable()
					|| living instanceof EntityAbsoluteDeath 
					|| living instanceof EntityPlayer && ((EntityPlayer)living).capabilities.isCreativeMode
			) {
				continue;
			}
			float health = living.getHealth();
			float steal = Math.min(health, living.getMaxHealth() * HEALTH_STEAL_RATIO);
			living.setHealth(health - steal);
			p.heal(steal);
			int hrt = living.hurtResistantTime;
			living.hurtResistantTime = 0;
			living.attackEntityFrom(DamageSource.outOfWorld, 0.1F);
			living.hurtResistantTime = hrt;
			if(living.getHealth() <= 0.0F) {
				living.onDeath(DamageSource.causePlayerDamage(p));
			}
		}
	}
	
	public static boolean isMantleActive(EntityLivingBase e) {
		return e.getEntityData().hasKey("mantleActive");
	}
	
	public static void setMantleAbilityState(EntityPlayer p, boolean active) {
		p.noClip = active;
	}

	public static void setMantleState(EntityPlayer p, boolean active) {
		if(!p.capabilities.isCreativeMode) {
			p.capabilities.disableDamage = active;
			p.hurtResistantTime = active ? 1000 : 0;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			l.add(StatCollector.translateToLocal("dh.desc.mantle1"));
			l.add(StatCollector.translateToLocal("dh.desc.mantle2"));
			l.add(StatCollector.translateToLocal("dh.desc.mantle3"));
		}
		else {
			l.add(StatCollector.translateToLocal("dh.desc.mantle4"));
			l.add(StatCollector.translateToLocal("dh.desc.mantle5"));
			l.add(StatCollector.translateToLocal("dh.desc.mantle6"));
		}
		String owner = stack.hasTagCompound() ? stack.getTagCompound().getString("dhowner") : "";
		if(owner.equals("")) {
			owner = StatCollector.translateToLocal("dh.desc.defaultOwner");
		}
		l.add(StatCollector.translateToLocalFormatted("dh.desc.owner", owner));
		l.add(StatCollector.translateToLocal("dh.desc.hallow"));
	}

}
