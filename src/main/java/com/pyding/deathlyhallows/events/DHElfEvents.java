package com.pyding.deathlyhallows.events;

import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.EntityDamageSourceIndirectSilver;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.utils.DHUtils;
import com.pyding.deathlyhallows.utils.ElfUtils;
import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

import java.awt.*;

@SuppressWarnings("unused")
public final class DHElfEvents {

	private static final DHElfEvents INSTANCE = new DHElfEvents();

	private static long firstShot(EntityPlayer player){
		if(ElfUtils.getElfLevel(player) >= 10)
			return 1000;
		return 2000;
	}

	private static long secondShot(EntityPlayer player){
		if(ElfUtils.getElfLevel(player) >= 10)
			return 2000;
		return 4000;
	}

	private DHElfEvents() {

	}

	public static void init() {
		MinecraftForge.EVENT_BUS.register(INSTANCE);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void elfTheLiveEnjoyer(LivingEvent.LivingUpdateEvent e) {
		if(e.entity.worldObj.isRemote || !(e.entity instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer)e.entity;
		arrowEffects(player);
		ElfUtils.manageElf(player);
	}

	private void arrowEffects(EntityPlayer p) {
		NBTTagCompound entityTag = p.getEntityData();
		if(entityTag.getLong("DHArrow") <= 0) {
			return;
		}
		long time = System.currentTimeMillis() - entityTag.getLong("DHArrow");
		if(time >= firstShot(p) && entityTag.getBoolean("DHArrowShow")) {
			entityTag.setBoolean("DHArrowShow", false);
			DHUtils.spawnSphere(p, DHUtils.getPosition(p), 20, 3, Color.BLUE, 1, 3, 60, 1);
			p.worldObj.playSoundAtEntity(p, "dh:arrow.arrow_ready_1", 1F, 1F);
		}
		if(time >= secondShot(p) && entityTag.getBoolean("DHArrowShow2")) {
			entityTag.setBoolean("DHArrowShow2", false);
			DHUtils.spawnSphere(p, DHUtils.getPosition(p), 20, 3, Color.magenta, 1, 3, 60, 1);
			p.worldObj.playSoundAtEntity(p, "dh:arrow.arrow_ready_2", 1F, 1F);
		}
	}

	@SubscribeEvent
	public void elfTheArcherShooter(ArrowNockEvent e) {
		NBTTagCompound entityTag = e.entityPlayer.getEntityData();
		if(ElfUtils.getElfLevel(e.entityPlayer) >= 3) {
			entityTag.setLong("DHArrow", System.currentTimeMillis());
			entityTag.setBoolean("DHArrowShow", true);
			if(ElfUtils.getElfLevel(e.entityPlayer) >= 7)
				entityTag.setBoolean("DHArrowShow2", true);
		}
	}

	@SubscribeEvent
	public void elfTheArcherShooter2(ArrowLooseEvent e) {
		EntityPlayer p = e.entityPlayer;
		DeathlyProperties props = DeathlyProperties.get(p);
		NBTTagCompound tag = p.getEntityData();
		long time = System.currentTimeMillis() - tag.getLong("DHArrow");
		tag.setLong("DHArrow", 0);
		int elfLevel = ElfUtils.getElfLevel(props);
		long perfectTime;
		if(elfLevel >= 10)
			perfectTime = secondShot(p) + 200;
		else perfectTime = secondShot(p) + 150;
		if(elfLevel >= 7 && (time > secondShot(p) || tag.getInteger("DHShot") > 0)) {
			if(tag.getInteger("DHShot") > 0) {
				DHUtils.spawnArrow(p, 3);
				tag.setInteger("DHShot", tag.getInteger("DHShot") - 1);
				e.setCanceled(true);
			}
			else if(time < perfectTime) {
				DHUtils.spawnArrow(p, 3);
				tag.setInteger("DHShot", 5);
				e.setCanceled(true);
			}
			else {
				DHUtils.spawnArrow(p, 2);
				e.setCanceled(true);
			}
			return;
		}
		if(elfLevel >= 3 && time > firstShot(p)) {
			DHUtils.spawnArrow(p, 1);
			e.setCanceled(true);
		}
	}


	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	public void elfTheVampireHunter(LivingDeathEvent e) {
		if(!e.isCanceled() || e.entity == null || e.source.getEntity() == null) {
			return;
		}
		Entity source = e.source.getEntity();
		if(!(e.entity instanceof EntityPlayer) || !(source instanceof EntityLivingBase)) {
			return;
		}
		if(ElfUtils.isElf((EntityLivingBase)source) && CreatureUtil.isVampire(e.entity)) {
			e.setCanceled(false);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void elfTheFaceDamageEnjoyer(LivingHurtEvent e) {
		if(e.isCanceled()) {
			return;
		}
		handleElfDamageEffects(e);
		handleElfResistance(e);
	}

	private static void handleElfDamageEffects(LivingHurtEvent e) {
		if(e.entityLiving == null || e.source.getEntity() == null || !(e.source.getEntity() instanceof EntityPlayer)) {
			return;
		}
		EntityLivingBase entity = e.entityLiving;
		EntityPlayer source = (EntityPlayer)e.source.getEntity();
		int elfLevel = ElfUtils.getElfLevel(source);
		if(!e.source.isProjectile()) {
			if(elfLevel < 1 || e.source.isMagicDamage()) {
				return;
			}
			if(source.getHeldItem() != null && source.getHeldItem().getItem() != DHItems.elderWand) {
				e.ammount = e.ammount * 0.05F;
			}
			return;
		}

		float radius = 8;
		float damageAoe = 0.3F;
		if(elfLevel > 8) {
			radius *= 2;
			damageAoe *= 2;
		}
		if(elfLevel > 0) {
			e.ammount = e.ammount * elfLevel;
		}
		if(elfLevel > 2) {
			e.source.setMagicDamage();
		}
		if(elfLevel > 5 && !e.source.isDamageAbsolute()) {
			DHUtils.spawnSphere(entity, DHUtils.getPosition(entity), (int)(radius * 20), radius, Color.BLUE, 1, 6, 60, 1);
			for(EntityLiving el: DHUtils.getEntitiesAround(EntityLiving.class, entity, radius)) {
				if(el == entity) {
					continue;
				}
				el.setLastAttacker(source);
				ParticleEffect.MAGIC_CRIT.send(null, el, 2, 2, 64);
				el.attackEntityFrom(DamageSource.causePlayerDamage(source)
												.setDamageIsAbsolute()
												.setProjectile(), e.ammount * damageAoe);

			}
		}
		if(elfLevel > 8 && ElfUtils.isVampOrWolf(e.entityLiving)) {
			if(CreatureUtil.isVampire(e.entityLiving)) {
				com.emoniph.witchery.common.ExtendedPlayer.get(source).setBloodReserve(0);
				e.entityLiving.attackEntityFrom(EntityUtil.DamageSourceVampireFire.magic, e.ammount * 10);
			}
			if(CreatureUtil.isWerewolf(e.entityLiving, true)) {
				e.entityLiving.attackEntityFrom(EntityDamageSourceIndirectSilver.magic, e.ammount * 10);
			}
			if(e.entityLiving.getHealth() <= e.entityLiving.getMaxHealth() * 0.1) {
				DHUtils.deadInside(e.entityLiving, source);
			}
		}
		if(elfLevel == 10
				&& e.entityLiving instanceof EntityPlayer
				&& Math.random() < 0.1
				&& e.entityLiving.getHealth() > e.entityLiving.getMaxHealth() * 0.3) {
			EntityUtil.instantDeath(e.entityLiving, source);
		}
	}

	private static void handleElfResistance(LivingHurtEvent e) {
		if(!(e.entityLiving instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer)e.entityLiving;
		DeathlyProperties props = DeathlyProperties.get(player);
		if(props.getElfLevel() >= 7
				&& player.getHeldItem() != null
				&& player.getHeldItem().getItem() instanceof ItemBow
				&& Math.random() < 0.75
		) {
			e.ammount = 0;
			e.source.setFireDamage();
			e.setCanceled(true);
		}
		if(props.getElfLevel() >= 1) {
			if(e.source.isMagicDamage()) {
				e.ammount = e.ammount * 0.1F;
			}
			else if(e.ammount * 10 < Float.MAX_VALUE) {
				e.ammount = e.ammount * 2;
			}
		}
	}

	@SubscribeEvent
	public void onFoodEaten(PlayerUseItemEvent.Finish e) {
		EntityPlayer p = e.entityPlayer;
		if(p.worldObj.isRemote) {
			return;
		}
		DeathlyProperties props = DeathlyProperties.get(p);
		int elfLevel = ElfUtils.getElfLevel(props);
		if(elfLevel == 5 && e.item.getItem() instanceof ItemAppleGold && e.item.getItemDamage() > 0) {
			props.setFoodEaten(props.getFoodEaten() + 1);
		}
		if(elfLevel == 8) {
			props.addFoodToCollection(e.item.getUnlocalizedName());
		}
	}

}
