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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.AbstractMap;
import java.util.function.Predicate;

@SuppressWarnings("unused")
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

	private static AbstractMap.SimpleEntry<String, Predicate<DamageSource>> entryFactory(String name, Predicate<DamageSource> condition) {
		return new AbstractMap.SimpleEntry<>(name + "block", condition);
	}
	
	private static void recalculateBlockAmount(EntityAbsoluteDeath death, EntityAbsoluteDeath.EnumResists resist, int points) {
		int absorbedAmount = death.getResist(resist);
		if(DHConfig.deathDifficulty < 2) {
			for(EntityAbsoluteDeath.EnumResists otherResist: EntityAbsoluteDeath.EnumResists.values()) {
				if(otherResist.equals(resist)) {
					continue;
				}
				death.setResist(otherResist, death.getResist(otherResist) - 1);
			}
		}
		if(absorbedAmount >= 90) {
			death.setRage();
		}
		if(absorbedAmount >= 100) {
			return;
		}
		death.setResist(resist, absorbedAmount + points);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void deathHurt(LivingHurtEvent e) {
		if(e.isCanceled()) {
			return;
		}
		if(!(e.entityLiving instanceof EntityAbsoluteDeath) || e.ammount < 0F) {
			return;
		}
		EntityAbsoluteDeath death = (EntityAbsoluteDeath)e.entity;
		// resists calculation and sync
		int points = Math.max(1, MathHelper.ceiling_float_int(100F * Math.min(e.ammount /  death.getMaxHealth(), 1F)));
		if(e.source == null) {
			recalculateBlockAmount(death, EntityAbsoluteDeath.EnumResists.GENERIC, points);
		}
		else {
			for(EntityAbsoluteDeath.EnumResists resist: EntityAbsoluteDeath.EnumResists.values()) {
				if(resist.isResisted(e.source)) {
					recalculateBlockAmount(death, resist, points);
					break;
				}
			}
		}
		PacketRenderAbsoluteDeath packet = new PacketRenderAbsoluteDeath(death);
		NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(death.dimension, death.posX, death.posY, death.posZ, 64);
		DHPacketProcessor.sendToAllAround(packet, target);
		
		// resists application
		NBTTagCompound tag = death.getEntityData();
		if(DHConfig.deathDifficulty > 1) {
			e.ammount = 0.5F * Math.min(death.getMaxHealth() / 8F, e.ammount);
			e.ammount *= Math.max(0.1F, death.getHealth() / death.getMaxHealth());
		}
		for(EntityAbsoluteDeath.EnumResists resist: EntityAbsoluteDeath.EnumResists.values()) {
			if(resist.isResisted(e.source)) {
				e.ammount = reduceDamage(death, resist, e.ammount);
				break;
			}
		}
		if(e.ammount > 0) {
			tag.setBoolean("dhdamaged", true);
			tag.setFloat("dhhp", death.getHealth());
		}
		else {
			tag.setBoolean("dhdamaged", false);
		}
		if(e.source == null || e.source.getEntity() == null) {
			return;
		}
		NBTTagCompound nbt = new NBTTagCompound();
		String id = String.valueOf(e.source.getEntity().getEntityId());
		float damageDealt = e.ammount;
		nbt.setFloat("damageDealt", damageDealt);
		if(tag.hasKey(id)) {
			NBTTagCompound nbt2 = new NBTTagCompound().getCompoundTag(id);
			nbt2.setFloat("damageDealt", damageDealt + nbt2.getFloat("damageDealt"));
			tag.setTag(id, nbt2);
		}
		else {
			tag.setTag(id, nbt);
		}
	}

	public float reduceDamage(EntityAbsoluteDeath e, EntityAbsoluteDeath.EnumResists resist, float damage) {
		return damage * Math.max(1F / (1 << (DHConfig.deathDifficulty * DHConfig.deathDifficulty)), 1 - (e.getResist(resist) / 100F));
	}

}
