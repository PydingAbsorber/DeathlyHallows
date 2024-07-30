package com.pyding.deathlyhallows.events;

import com.emoniph.witchery.entity.EntityGoblin;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.utils.properties.ExtendedPlayer;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public final class DHGoblinEvents {

	private static final DHGoblinEvents INSTANCE = new DHGoblinEvents();

	private DHGoblinEvents() {

	}

	public static void init() {
		MinecraftForge.EVENT_BUS.register(INSTANCE);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void IamJapaneseGoblin(LivingEvent.LivingUpdateEvent e) {
		if(e.entityLiving.getEntityData().getBoolean("immortal") && e.entityLiving instanceof EntityGoblin) {
			e.entityLiving.setHealth(e.entityLiving.getMaxHealth());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void japaneseGoblin(LivingAttackEvent e) {
		if(e.entityLiving.worldObj.isRemote || e.isCanceled()) {
			return;
		}
		if(e.entity.getEntityData().getBoolean("immortal") && e.entity instanceof EntityGoblin) {
			e.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void weAreJapaneseGoblin(LivingHurtEvent event) {
		if(event.isCanceled()) {
			return;
		}
		if(event.entity instanceof EntityGoblin && event.entity.getEntityData().getBoolean("immortal")) {
			event.ammount = 0;
		}
	}
	
	@SubscribeEvent
	public void onEntityConstructing(EntityEvent.EntityConstructing event) {
		if(event.entity == null) {
			return;
		}
		if(!(event.entity instanceof EntityPlayer) || ExtendedPlayer.get((EntityPlayer)event.entity) != null) {
			return;
		}
		ExtendedPlayer.register((EntityPlayer)event.entity);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void oniSlayer(LivingDeathEvent event) {
		if(event.entity instanceof EntityGoblin && Math.random() < 0.01) {
			event.entity.entityDropItem(new ItemStack(DHItems.hobgoblinSoul), 1);
		}
	}

}
