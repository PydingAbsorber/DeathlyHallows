package com.pyding.deathlyhallows.events;

import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.EntityDamageSourceIndirectSilver;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.utils.DHUtils;
import com.pyding.deathlyhallows.utils.ElfUtils;
import com.pyding.deathlyhallows.utils.properties.ExtendedPlayer;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBow;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

import java.awt.*;

public final class DHElfEvents {
	
	private static final DHElfEvents INSTANCE = new DHElfEvents();

	private static final long 
			firstShot = 1000,
			secondShot = 2000;
	
	private DHElfEvents() {
		
	}
	
	public static void init() {
		MinecraftForge.EVENT_BUS.register(INSTANCE);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void elfTheLiveEnjoyer(LivingEvent.LivingUpdateEvent event) {
		if(event.entity.worldObj.isRemote || !(event.entity instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer)event.entity;
		arrowEffects(player);
		ElfUtils.manageElf(player);
	}

	private void arrowEffects(EntityPlayer player) {
		if(player.getEntityData().getLong("DHArrow") > 0) {
			long time = System.currentTimeMillis() - player.getEntityData().getLong("DHArrow");
			boolean show = player.getEntityData().getBoolean("DHArrowShow");
			boolean show2 = player.getEntityData().getBoolean("DHArrowShow2");
			if(time >= firstShot && show) {
				player.getEntityData().setBoolean("DHArrowShow", false);
				DHUtils.spawnSphere(player, player.getPosition(1), 20, 3, Color.BLUE, 1, 3, 60, 1);
				player.worldObj.playSoundAtEntity(player, "dh:arrow.arrow_ready_1", 1F, 1F);
			}
			if(time >= secondShot && show2) {
				player.getEntityData().setBoolean("DHArrowShow2", false);
				DHUtils.spawnSphere(player, player.getPosition(1), 20, 3, Color.magenta, 1, 3, 60, 1);
				player.worldObj.playSoundAtEntity(player, "dh:arrow.arrow_ready_2", 1F, 1F);
			}
		}
	}

	@SubscribeEvent
	public void elfTheArcherShooter(ArrowNockEvent event) {
		EntityPlayer player = event.entityPlayer;
		player.getEntityData().setLong("DHArrow", System.currentTimeMillis());
		player.getEntityData().setBoolean("DHArrowShow", true);
		player.getEntityData().setBoolean("DHArrowShow2", true);
	}

	@SubscribeEvent
	public void elfTheArcherShooter2(ArrowLooseEvent event) {
		EntityPlayer p = event.entityPlayer;
		long time = System.currentTimeMillis() - p.getEntityData().getLong("DHArrow");
		p.getEntityData().setLong("DHArrow", 0);
		long perfectTime = secondShot + 150;
		if(time > 9 && (time > secondShot || p.getEntityData().getInteger("DHShot") > 0)) {
			if(p.getEntityData().getInteger("DHShot") > 0) {
				DHUtils.spawnArrow(p, 3);
				p.getEntityData().setInteger("DHShot", p.getEntityData().getInteger("DHShot") - 1);
				event.setCanceled(true);
			}
			else if(time < perfectTime) {
				DHUtils.spawnArrow(p, 3);
				p.getEntityData().setInteger("DHShot", 5);
				event.setCanceled(true);
			}
			else {
				DHUtils.spawnArrow(p, 2);
				event.setCanceled(true);
			}
			return;
		}
		if(time > 6 && time > firstShot) {
			DHUtils.spawnArrow(p, 1);
			event.setCanceled(true);
		}
	}


	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	public void elfTheVampireHunter(LivingDeathEvent event) {
		if(!event.isCanceled() || event.entity == null || event.source.getEntity() == null) {
			return;
		}
		Entity source = event.source.getEntity();
		if(!(event.entity instanceof EntityPlayer) || !(source instanceof EntityLivingBase)) {
			return;
		}
		if(ElfUtils.isElf((EntityLivingBase)source) && CreatureUtil.isVampire(event.entity)) {
			event.setCanceled(false);
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
			DHUtils.spawnSphere(entity, entity.getPosition(1), (int)(radius * 20), radius, Color.BLUE, 1, 6, 60, 1);
			for(EntityLiving target: DHUtils.getEntitiesAround(EntityLiving.class, entity, radius)) {
				if(target == entity) {
					continue;
				}

				target.setLastAttacker(source);
				ParticleEffect.MAGIC_CRIT.send(null, target, 2, 2, 64);
				target.attackEntityFrom(DamageSource.causePlayerDamage(source).setDamageIsAbsolute().setProjectile(), e.ammount * damageAoe);

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
				ExtendedPlayer.get(source).deadInside(e.entityLiving);
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
		ExtendedPlayer props = ExtendedPlayer.get(player);
		if(props.getElfLvl() >= 7
				&& player.getHeldItem() != null
				&& player.getHeldItem().getItem() instanceof ItemBow
				&& Math.random() < 0.75
		) {
			e.ammount = 0;
			e.source.setFireDamage();
			e.setCanceled(true);
		}
		if(props.getElfLvl() >= 1) {
			if(e.source.isMagicDamage()) {
				e.ammount = e.ammount * 0.1F;
			}
			else if(e.ammount * 10 < Float.MAX_VALUE) {
				e.ammount = e.ammount * 2;
			}
		}
	}

	@SubscribeEvent
	public void onFoodEaten(PlayerUseItemEvent.Finish event) {
		EntityPlayer p = event.entityPlayer;
		if(p.worldObj.isRemote) {
			return;
		}
		ExtendedPlayer props = ExtendedPlayer.get(p);
		int elfLevel = ElfUtils.getElfLevel(props);
		if(elfLevel == 5 && event.item.getItem() instanceof ItemAppleGold && event.item.getItemDamage() > 0) {
			props.setFoodEaten(props.getFoodEaten() + 1);
		}
		if(elfLevel == 8) {
			props.addFoodToCollection(event.item.getUnlocalizedName());
		}
	}

}
