package com.pyding.deathlyhallows.events;

import com.emoniph.witchery.util.ChatUtil;
import com.pyding.deathlyhallows.entities.EntityAbsoluteDeath;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketRenderAbsoluteDeath;
import com.pyding.deathlyhallows.utils.DHConfig;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public final class DHDeathEvents {
	
	private static final DHDeathEvents INSTANCE = new DHDeathEvents();

	private DHDeathEvents() {

	}

	public static void init() {
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		FMLCommonHandler.instance().bus().register(INSTANCE);
	}	
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void deathAlive(LivingEvent.LivingUpdateEvent e) {
		if(!(e.entityLiving instanceof EntityAbsoluteDeath) || e.entityLiving.worldObj.isRemote || !e.entityLiving.isEntityAlive()) {
			return;
		}
		//sethealth absorption
		EntityAbsoluteDeath death = (EntityAbsoluteDeath)e.entityLiving;
		NBTTagCompound entityTag = death.getEntityData();
		if(entityTag.getFloat("dhhp") == 0) {
			entityTag.setFloat("dhhp", death.getHealth());
		}
		if(!(death.getHealth() + 51 < entityTag.getFloat("dhhp")) || entityTag.getBoolean("dhdamaged")) {
			return;
		}
		death.setHealth(entityTag.getFloat("dhhp"));
		if(death.getLastAttacker() instanceof EntityPlayer) {
			ChatUtil.sendTranslated(EnumChatFormatting.RED, (EntityPlayer)death.getLastAttacker(), "dh.chat.sethealth");
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void deathAttack(LivingAttackEvent e) {
		if(!(e.entity instanceof EntityAbsoluteDeath) || e.entity.worldObj.isRemote) {
			return;
		}
		EntityAbsoluteDeath death = (EntityAbsoluteDeath)e.entity;
		int absolute = death.getEntityData().getInteger("absoluteblock");
		int earth = death.getEntityData().getInteger("earthblock");
		int fire = death.getEntityData().getInteger("fireblock");
		int generic = death.getEntityData().getInteger("genericblock");
		int magic = death.getEntityData().getInteger("magicblock");
		int wither = death.getEntityData().getInteger("witherblock");
		int voed = death.getEntityData().getInteger("voidblock");
		int water = death.getEntityData().getInteger("waterblock");
		if(e.source == DamageSource.wither) {
			if(wither == 90) {
				death.teleportRandomly();
				death.setRage();
			}
			wither += 10;
		}
		else if(e.source.isDamageAbsolute()) {
			if(absolute == 90) {
				death.teleportRandomly();
				death.setRage();
			}
			absolute += 10;
		}
		else if(e.source == DamageSource.fall || e.source == DamageSource.inWall) {
			if(earth == 90) {
				death.teleportRandomly();
				death.setRage();
			}
			earth += 10;
		}
		else if(e.source == DamageSource.inFire || e.source == DamageSource.onFire || e.source == DamageSource.lava) {
			if(fire == 90) {
				death.teleportRandomly();
				death.setRage();
			}
			fire += 10;
		}
		else if(e.source == DamageSource.magic || e.source.isMagicDamage()) {
			if(magic == 90) {
				death.teleportRandomly();
				death.setRage();
			}
			magic += 10;
		}
		else if(e.source == DamageSource.outOfWorld) {
			if(voed == 90) {
				death.teleportRandomly();
				death.setRage();
			}
			voed += 10;
		}
		else if(e.source == DamageSource.drown) {
			if(water == 90) {
				death.teleportRandomly();
				death.setRage();
			}
			water += 10;
		}
		else {
			if(generic == 90) {
				death.teleportRandomly();
				death.setRage();
			}
			generic += 10;
		}
		death.getEntityData().setInteger("witherblock", wither);
		death.getEntityData().setInteger("absoluteblock", absolute);
		death.getEntityData().setInteger("earthblock", earth);
		death.getEntityData().setInteger("fireblock", fire);
		death.getEntityData().setInteger("magicblock", magic);
		death.getEntityData().setInteger("voidblock", voed);
		death.getEntityData().setInteger("waterblock", water);
		death.getEntityData().setInteger("genericblock", generic);
		PacketRenderAbsoluteDeath packet = new PacketRenderAbsoluteDeath(death.getEntityData(), death.getEntityId());
		NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(death.dimension, death.posX, death.posY, death.posZ, 64);
		DHPacketProcessor.sendToAllAround(packet, target);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void deathHurt(LivingHurtEvent e) {
		if(e.isCanceled()) {
			return;
		}
		if(!(e.entityLiving instanceof EntityAbsoluteDeath)) {
			return;
		}
		int difficulty = DHConfig.deathDifficulty;
		EntityAbsoluteDeath entity = (EntityAbsoluteDeath)e.entityLiving;
		if(difficulty > 1) {
			e.ammount = (float)(e.ammount < 200D
					? (395.872D * Math.log1p(0.002 * e.ammount))
					: (77.256D / (1 + 0.00000002 * e.ammount * e.ammount * e.ammount))
			);
		}
		if(e.source == DamageSource.wither) {
			e.ammount = reduceDamage(entity, "witherblock", e.ammount);
		}
		else if(e.source.isDamageAbsolute()) {
			e.ammount = reduceDamage(entity, "absoluteblock", e.ammount);
		}
		else if(e.source == DamageSource.fall || e.source == DamageSource.inWall) {
			e.ammount = reduceDamage(entity, "earthblock", e.ammount);
		}
		else if(e.source == DamageSource.inFire || e.source == DamageSource.onFire || e.source == DamageSource.lava) {
			e.ammount = reduceDamage(entity, "fireblock", e.ammount);
			entity.extinguish();
		}
		else if(e.source == DamageSource.magic || e.source.isMagicDamage()) {
			e.ammount = reduceDamage(entity, "magicblock", e.ammount);
		}
		else if(e.source == DamageSource.outOfWorld) {
			e.ammount = reduceDamage(entity, "voidblock", e.ammount);
		}
		else if(e.source == DamageSource.drown) {
			e.ammount = reduceDamage(entity, "waterblock", e.ammount);
		}
		else {
			e.ammount = reduceDamage(entity, "genericblock", e.ammount);
		}
		if(e.ammount > 0) {
			entity.getEntityData().setBoolean("dhdamaged", true);
			entity.getEntityData().setFloat("dhhp", entity.getHealth());
		}
		else {
			entity.getEntityData().setBoolean("dhdamaged", false);
		}
		if(e.source.getEntity() == null) {
			return;
		}
		NBTTagCompound nbt = new NBTTagCompound();
		String id = String.valueOf(e.source.getEntity().getEntityId());
		float damageDealt = e.ammount;
		nbt.setFloat("damageDealt", damageDealt);
		if(entity.getEntityData().hasKey(id)) {
			NBTTagCompound nbt2 = new NBTTagCompound().getCompoundTag(id);
			nbt2.setFloat("damageDealt", damageDealt + nbt2.getFloat("damageDealt"));
			entity.getEntityData().setTag(id, nbt2);
		}
		else {
			entity.getEntityData().setTag(id, nbt);
		}
	}

	public float reduceDamage(EntityLivingBase e, String name, float damage) {
		return damage * Math.min(1F / (1 << (DHConfig.deathDifficulty * DHConfig.deathDifficulty)), e.getEntityData().getInteger(name) / 100F);
	}
	
}
