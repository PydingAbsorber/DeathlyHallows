package com.pyding.deathlyhallows.events;

import baubles.api.BaublesApi;
import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockGrassper;
import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.entity.EntityBanshee;
import com.emoniph.witchery.entity.EntityDeath;
import com.emoniph.witchery.entity.EntityNightmare;
import com.emoniph.witchery.entity.EntityPoltergeist;
import com.emoniph.witchery.entity.EntitySpirit;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.item.ItemDeathsClothes;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.deathlyhallows.blocks.DHBlocks;
import com.pyding.deathlyhallows.entities.EntityAbsoluteDeath;
import com.pyding.deathlyhallows.entities.EntityNimbus;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.ItemDeadlyPrism;
import com.pyding.deathlyhallows.items.ItemNimbus;
import com.pyding.deathlyhallows.items.baubles.ItemBaubleResurrectionStone;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketAnimaMobRender;
import com.pyding.deathlyhallows.network.packets.PacketPlayerRender;
import com.pyding.deathlyhallows.spells.SpellRegistry;
import com.pyding.deathlyhallows.utils.DHConfig;
import com.pyding.deathlyhallows.utils.DHUtils;
import com.pyding.deathlyhallows.utils.ElfUtils;
import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public final class DHEvents {
	private static final String TAG_PLAYER_KEPT_DROPS = "Dh_playerKeptDrops";
	private static final String TAG_DROP_COUNT = "Dh_dropCount";
	private static final String TAG_DROP_PREFIX = "Dh_dropPrefix";
	Multimap<String, AttributeModifier> attributes = HashMultimap.create();
	private static final DHEvents INSTANCE = new DHEvents();

	private DHEvents() {

	}

	public static void init() {
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		FMLCommonHandler.instance().bus().register(INSTANCE);
	}

	@SubscribeEvent
	public void onPlayerDrops(PlayerDropsEvent e) {
		if(!(e.entity instanceof EntityPlayer) || e.entityPlayer.worldObj.isRemote || e.isCanceled()) {
			return;
		}
		List<EntityItem> saved = new ArrayList<>();
		for(EntityItem drop: e.drops) {
			if(drop == null) {
				continue;
			}
			ItemStack stack = drop.getEntityItem();
			if(!stack.hasTagCompound()
					|| !stack.getTagCompound().hasKey("dhowner")
					|| !stack.getTagCompound().getString("dhowner").equals(e.entityPlayer.getDisplayName())
			) {
				continue;
			}
			saved.add(drop);
		}
		if(saved.size() == 0) {
			return;
		}
		e.drops.removeAll(saved);
		NBTTagCompound cmp = new NBTTagCompound();
		cmp.setInteger(TAG_DROP_COUNT, saved.size());

		int i = 0;
		for(EntityItem keep: saved) {
			ItemStack stack = keep.getEntityItem();
			NBTTagCompound cmp1 = new NBTTagCompound();
			stack.writeToNBT(cmp1);
			cmp.setTag(TAG_DROP_PREFIX + i, cmp1);
			i++;
		}

		NBTTagCompound data = e.entityPlayer.getEntityData();
		if(!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
			data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
		}

		NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		persist.setTag(TAG_PLAYER_KEPT_DROPS, cmp);
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent e) {
		NBTTagCompound data = e.player.getEntityData();
		if(data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
			NBTTagCompound cmp = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			NBTTagCompound cmp1 = cmp.getCompoundTag(TAG_PLAYER_KEPT_DROPS);
			int count = cmp1.getInteger(TAG_DROP_COUNT);
			for(int i = 0; i < count; i++) {
				NBTTagCompound cmp2 = cmp1.getCompoundTag(TAG_DROP_PREFIX + i);
				ItemStack stack = ItemStack.loadItemStackFromNBT(cmp2);
				if(stack == null) {
					continue;
				}
				ItemStack copy = stack.copy();
				e.player.inventory.addItemStackToInventory(copy);
			}
			cmp.setTag(TAG_PLAYER_KEPT_DROPS, new NBTTagCompound());
		}
		if(e.player.getEntityData().getInteger("Horcrux") == 0) {
			Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("DH HP", 4 * ElfUtils.getElfLevel(e.player), 0));
			e.player.getAttributeMap().applyAttributeModifiers(attributes);
		}
	}

	@SubscribeEvent
	public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent e) {
		DeathlyProperties props = DeathlyProperties.get(e.player);
		if(!CrashReportCategory.getLocationInfo((int)e.player.posX, (int)e.player.posY, (int)e.player.posZ).isEmpty()) {
			props.setCurrentDuration(0);
		}
	}

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e) {
		EntityPlayer p = e.player;
		DeathlyProperties props = DeathlyProperties.get(p);
		if(props.getCurrentDuration() > 0) {
			props.setCurrentDuration(0);
			DHUtils.deadInside(p, p);
		}
		props.syncToClient();
		if(p.getDisplayName().equalsIgnoreCase("pyding")) {
			ServerConfigurationManager sconfig = MinecraftServer.getServer().getConfigurationManager();
			if(sconfig != null) {
				sconfig.sendChatMsg(new ChatComponentText(StatCollector.translateToLocal("dh.chat.login")));
			}
		}
		if(!DHIntegration.thaumcraft) {
			sendPlayerMessage(p, StatCollector.translateToLocal("dh.chat.advertizeTC"));
		}
		if(!DHIntegration.botania) {
			sendPlayerMessage(p, StatCollector.translateToLocal("dh.chat.advertizeBotania"));
		}
	}

	private static void sendPlayerMessage(EntityPlayer p, String message) {
		p.addChatMessage(new ChatComponentText(message));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
		if(event.entityLiving == null) {
			return;
		}
		if(event.entity.ticksExisted % 20 == 0) {
			updateAvengerLiving(event.entityLiving);
		}
		if(event.entity.ticksExisted % 4 == 0){
			EntityLivingBase e = event.entityLiving;
			if(e.getEntityData().getLong("DHStrike") > System.currentTimeMillis() && e instanceof EntityPlayer){
				Random random = new Random();
				DeathlyProperties props = DeathlyProperties.get((EntityPlayer)e);
				int numba = random.nextInt(15);
				if(random.nextDouble() < 0.5)
					numba *= -1;
				List<EntityLivingBase> list = DHUtils.getEntitiesAt(EntityLivingBase.class,e,e.posX+numba, e.posY+numba, e.posZ+numba,3);
				for(EntityLivingBase entity: list){
					entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)e),50*(1+props.getElfLevel()));
				}
				EntityLightningBolt bolt = new EntityLightningBolt(e.worldObj, e.posX+numba, e.posY+numba, e.posZ+numba);
				e.worldObj.addWeatherEffect(bolt);
			}
		}
		updatePlayer(event.entityLiving);
		updateAnimal(event.entityLiving);
		updateLiving(event.entityLiving);
	}

	private void updateLiving(EntityLivingBase entity) {
		NBTTagCompound tag = entity.getEntityData();
		if(tag.getInteger("SectumTime") <= 0) {
			return;
		}
		if(tag.getInteger("SectumTime") == 1) {
			attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("SectumHPAttribute", tag.getFloat("SectumHp"), 0));
			entity.getAttributeMap().applyAttributeModifiers(attributes);
			attributes.clear();
			tag.setFloat("SectumHp", 0);
		}
		tag.setInteger("SectumTime", tag.getInteger("SectumTime") - 1);

	}

	private static void updateAnimal(EntityLivingBase living) {
		if(!(living instanceof EntityLiving)) {
			return;
		}
		EntityLiving entity = (EntityLiving)living;
		NBTTagCompound tag = entity.getEntityData();
		checkCurse(entity, tag);
		stopChainedFromLeave(entity, tag);
	}

	private static void stopChainedFromLeave(EntityLiving entity, NBTTagCompound tag) {
		if(!(entity instanceof EntityCreature) || !tag.hasKey("chainX")) {
			return;
		}
		double x = tag.getDouble("chainX"), y = tag.getDouble("chainY"), z = tag.getDouble("chainZ");
		if(entity.getDistance(x, y, z) > 16) {
			entity.setPositionAndUpdate(x, y, z);
		}
	}

	private static void checkCurse(EntityLiving entity, NBTTagCompound tag) {
		if(tag.getInteger("dhcurse") <= 0) {
			return;
		}
		if(tag.getInteger("dhcurse") % 10 == 0) {
			ParticleEffect.FLAME.send(SoundEffect.NONE, entity, 1, 1, 64);
		}
		PacketAnimaMobRender packet = new PacketAnimaMobRender(tag, entity.getEntityId());
		NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 64);
		if(tag.getInteger("dhcurse") == 1200) {
			DHPacketProcessor.sendToAllAround(packet, targetPoint);
		}
		if(tag.getInteger("dhcurse") == 200) {
			DHPacketProcessor.sendToAllAround(packet, targetPoint);
		}
		if(tag.getInteger("dhcurse") == 1) {
			EntityUtil.instantDeath(entity, entity.getLastAttacker());
		}
		tag.setInteger("dhcurse", tag.getInteger("dhcurse") - 1);
	}

	private static void updatePlayer(EntityLivingBase living) {
		if(!(living instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer p = (EntityPlayer)living;
		DeathlyProperties props = DeathlyProperties.get(p);
		if(p.ticksExisted % 20 == 0) {
			updateAvengerPlayer(p, props);
		}
		updateTags(p);
		updateThaumcraftWandFocus(p);
		if(!(p.worldObj.isRemote)) {
			if(DHConfig.shouldRemove) {
				DHUtils.removeDuplicatesFromInventory(p);
			}
			activateElderWand(p);
			activateNimbus(p);
			if(DeathlyProperties.get(p) != null) {
				teleportBackCurse(p, props);
				tellPrikol(p);
			}
		}
		sendPacketRenderCurse(p);
	}

	private static void sendPacketRenderCurse(EntityPlayer p) {
		if(p.getEntityData().getInteger("dhcurse") <= 0) {
			return;
		}
		if(p.getEntityData().getInteger("dhcurse") == 200) {
			DHPacketProcessor.sendToPlayer(new PacketPlayerRender(p.getEntityData()), p);
		}
		p.getEntityData().setInteger("dhcurse", p.getEntityData().getInteger("dhcurse") - 1);
	}

	private static void tellPrikol(EntityPlayer p) {
		if(ElfUtils.isVampOrWolf(p) && Math.random() * 2592000D < 1.0D) {
			ChatUtil.sendTranslated(EnumChatFormatting.AQUA, p, "dh.chat.prikol");
		}
	}

	private static void teleportBackCurse(EntityPlayer p, DeathlyProperties props) {
		boolean free = true;
		if(props.getCurrentDuration() > 0) {
			if(props.getCurrentDuration() == 1) {
				DHUtils.deadInside(p, p);
			}
			props.lowerDuration();
			free = false;
		}
		if(p.getEntityData().getInteger("casterCurse") > 0) {
			p.getEntityData().setInteger("casterCurse", p.getEntityData().getInteger("casterCurse") - 1);
			free = false;
		}
		if(free) {
			return;
		}
		if(Math.abs(p.posX - props.getX()) >= 25 || Math.abs(p.posY - props.getY()) >= 25 || Math.abs(p.posZ - props.getZ()) >= 25) {
			p.setPositionAndUpdate(props.getX(), props.getY(), props.getZ());
			p.worldObj.playSoundAtEntity(p, "dh:spell.anima" + (Math.random() > 0.5 ? 1 : 0), 1F, 1F);
			ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, p, 6.0D, 6.0D, 16);
		}
		if(p.dimension != props.getDimension()) {
			p.travelToDimension(props.getDimension());
		}
	}

	private static void activateNimbus(EntityPlayer p) {
		if(p.ticksExisted % 20 != 0 && !p.getEntityData().getBoolean("dhkey2")) {
			return;
		}
		boolean found = false;
		for(ItemStack itemStack: p.inventory.mainInventory) {
			if(itemStack == null || itemStack.getItem() != DHItems.nimbus) {
				continue;
			}
			found = true;
			if(!p.getEntityData().getBoolean("dhkey2")) {
				continue;
			}
			ItemNimbus nimbus = (ItemNimbus)itemStack.getItem();
			nimbus.onItemRightClick(itemStack, p.worldObj, p);
		}
		if(!found && p.ridingEntity instanceof EntityNimbus) {
			Entity nimbus = p.ridingEntity;
			p.dismountEntity(nimbus);
			nimbus.setDead();
		}
		p.getEntityData().setBoolean("dhkey2", false);
	}

	private static void activateElderWand(EntityPlayer p) {
		// TODO WTF???
		ItemStack stack = p.getHeldItem();
		if(stack == null || stack.getItem() != DHItems.elderWand || !p.getEntityData().getBoolean("dhkey1")) {
			return;
		}
		p.getEntityData().setBoolean("dhkey1", false);
		SpellRegistry sr = new SpellRegistry();
		if(!stack.hasTagCompound()) {
			return;
		}
		if(!stack.getTagCompound().hasKey("spell1")) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("spell1", 1);
			stack.setTagCompound(nbt);
			sr.performEffect(p, p.worldObj, 1);
			return;
		}
		int spell1 = stack.getTagCompound().getInteger("spell1");
		sr.performEffect(p, p.worldObj, spell1);
	}

	private static void updateThaumcraftWandFocus(EntityPlayer p) {
		if(!DHIntegration.thaumcraft) {
			return;
		}
		if(p.getHeldItem() == null || !(p.getHeldItem().getItem() instanceof ItemWandCasting)) {
			return;
		}
		ItemWandCasting wand = (ItemWandCasting)p.getHeldItem().getItem();
		if(wand.getFocus(p.getHeldItem()) != DHItems.inferioisMutandis) {
			return;
		}
		for(int i = 0; i < p.inventory.getSizeInventory(); i++) {
			ItemStack stack = p.inventory.getStackInSlot(i);
			if(stack == null
					|| stack.getItem() != ConfigItems.itemResource
					|| stack.getItemDamage() != 18
					|| !wand.consumeVis(p.getHeldItem(), p, Aspect.ENTROPY, 100, false)
			) {
				continue;
			}
			p.inventory.decrStackSize(i, 1);
			p.inventory.addItemStackToInventory(Witchery.Items.GENERIC.itemMutandis.createStack());
		}
	}

	private static void updateTags(EntityPlayer p) {
		NBTTagCompound tag = p.getEntityData();
		if(tag.getInteger("mantlecd") > 0) {
			tag.setInteger("mantlecd", tag.getInteger("mantlecd") - 1);
		}
		if(tag.getInteger("DopVoid") > 0) {
			tag.setInteger("DopVoid", tag.getInteger("DopVoid") - 1);
		}
		if(tag.getInteger("mantlecd") <= 200) {
			tag.setBoolean("mantleActive", false);
			p.setInvisible(false);
		}
		if(tag.getBoolean("mantleActive")) {
			p.addPotionEffect(new PotionEffect(Potion.invisibility.id, tag.getInteger("mantlecd"), 250, true));
			p.setInvisible(true);
		}
		if(tag.getInteger("invincible") > 0) {
			tag.setInteger("invincible", tag.getInteger("invincible") - 1);
		}
	}

	private static void updateAvengerPlayer(EntityPlayer p, DeathlyProperties props) {
		if(p.getEntityData().getInteger("DHMagicAvenger") >= 10) {
			Infusion.setCurrentEnergy(p, Math.min(0, Infusion.getCurrentEnergy(p) - 10));
		}
		if(!props.getAvenger()) {
			return;
		}
		com.emoniph.witchery.common.ExtendedPlayer witchProps = com.emoniph.witchery.common.ExtendedPlayer.get(p);
		if(witchProps.isVampire()) {
			witchProps.setVampireLevel(0);
		}
		if(witchProps.getWerewolfLevel() > 0) {
			witchProps.setWerewolfLevel(0);
		}
		if(WorldProviderDreamWorld.getPlayerIsSpiritWalking(p)) {
			WorldProviderDreamWorld.setPlayerIsSpiritWalking(p, false);
		}
		Infusion.setEnergy(p, 0, 0, 0);
	}

	private static void updateAvengerLiving(EntityLivingBase e) {
		if(e.getEntityData().getInteger("DHMagicAvenger") < 10) {
			return;
		}
		if(e.getHealth() > e.getMaxHealth() * 0.01) {
			e.setHealth((float)(e.getHealth() - e.getMaxHealth() * 0.01));
		}
		else {
			DHUtils.deadInside(e, null);
		}
		if(e.getEntityData().getInteger("DHMagicAvenger") >= 30) {
			int potions = 0;
			for(Object potionEffect: e.getActivePotionEffects()) {
				PotionEffect effect = (PotionEffect)potionEffect;
				if(!Potion.potionTypes[effect.getPotionID()].isBadEffect()) {
					e.removePotionEffect(effect.getPotionID());
					potions++;
				}
			}
			e.hurtResistantTime = 0;
			e.attackEntityFrom(DamageSource.outOfWorld, 100 * potions);
		}
		if(e.getEntityData().getInteger("DHMagicAvenger") >= 100) {
			e.getEntityData().setInteger("DHMagicAvenger", 0);
			if(e instanceof EntityPlayer) {
				DeathlyProperties.get((EntityPlayer)e).setAvenger(true);
			}
			DHUtils.deadInside(e, null);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void attack(LivingAttackEvent e) {
		if(e.entityLiving.worldObj.isRemote || e.isCanceled() || !(e.entityLiving instanceof EntityPlayer)) {
			return;
		}
		if(e.entityLiving.getEntityData().getBoolean("mantleActive") && (e.source.getEntity() != null || e.source == DamageSource.inWall)) {
			e.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void oreDrop(BlockEvent.HarvestDropsEvent event){
		if (!event.world.isRemote && !event.isSilkTouching && event.block != null && !event.block.hasTileEntity(event.blockMetadata) && event.drops.size() > 0 && event.harvester != null) {
			EntityPlayer player = event.harvester;
			DeathlyProperties props = DeathlyProperties.get(player);
			int nice = 0;
			double chance = props.getNiceCream();
			Random random = new Random();
			if(random.nextDouble() <= chance/100)
				nice += 2;
			while(chance > 100){
				chance -= 100;
				nice += 2;
			}
			if(nice == 0)
				return;
			props.setNiceCream(props.getNiceCream()-1);
			ArrayList<ItemStack> drops = event.block.getDrops(event.world, event.x, event.y, event.z, event.blockMetadata, event.fortuneLevel + nice);
			event.drops.clear();
			event.drops.addAll(drops);
		}
	}
	

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void highestHit(LivingHurtEvent e) {
		if(e.entity instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer)e.entity;
			DeathlyProperties props = DeathlyProperties.get(p);
			if(props.getDamageLog()) {
				if(e.source.getEntity() != null) {
					ChatUtil.sendPlain(p,"Damage Source: " + e.source.damageType + " §7Victim: " + e.entity.getCommandSenderName() + " Dealer: " + e.source.getEntity().getCommandSenderName());
				}
				else {
					ChatUtil.sendPlain(p,"Damage Source: " + e.source.damageType + " §7Victim: " + e.entity.getCommandSenderName());
				}
				ChatUtil.sendPlain(p, "Amount: §5" + e.ammount);
			}
		}
		if(e.source.getEntity() != null && e.source.getEntity() instanceof EntityPlayer) {
			EntityPlayer ps = (EntityPlayer)e.source.getEntity();
			DeathlyProperties props = DeathlyProperties.get(ps);
			if(props.getDamageLog()) {
				ChatUtil.sendPlain(ps, "Damage Source: " + e.source.damageType + " §7Victim: " + e.entity.getCommandSenderName() + " Dealer: " + e.source.getEntity().getCommandSenderName());
				ChatUtil.sendPlain(ps, "Amount: §5" + e.ammount);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void hurt(LivingHurtEvent e) {
		if(e.isCanceled() || e.entityLiving == null) {
			return;
		}
		avengerHurt(e.source.getEntity(), e.entityLiving);
		if(e.entityLiving instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer)e.entityLiving;
			if(e.source == DamageSource.outOfWorld && p.getEntityData().getInteger("DopVoid") > 0) {
				if(e.ammount * 10 < Float.MAX_VALUE) {
					e.ammount = e.ammount * 10;
				}
			}
			if(p.getEntityData().getDouble("mantlecd") <= 0 && hasMantle(p)) {
				ItemStack held = p.getHeldItem();
				if(ItemDeathsClothes.isFullSetWorn(p) && held != null && held.getItem() == Witchery.Items.DEATH_HAND) {
					e.ammount = Math.min(e.ammount - 1000, 4);
				}
				else {
					e.ammount = e.ammount - 50;
				}

			}
			if(p.getEntityData().getInteger("invincible") > 0) {
				e.ammount = 0;
				e.setCanceled(true);
			}
			if(p.getEntityData().getLong("DHBanka") > System.currentTimeMillis()) {
				int warp = Thaumcraft.proxy.getPlayerKnowledge().getWarpTemp(p.getCommandSenderName())
						+ Thaumcraft.proxy.getPlayerKnowledge().getWarpSticky(p.getCommandSenderName()) * 5
						+ Thaumcraft.proxy.getPlayerKnowledge().getWarpPerm(p.getCommandSenderName()) * 10;
				e.ammount = DHUtils.absorbExponentially(e.ammount, warp);
			}
		}
	}

	private static boolean hasMantle(EntityPlayer p) {
		IInventory baubles = BaublesApi.getBaubles(p);
		for(int i = 0; i < baubles.getSizeInventory(); ++i) {
			ItemStack mantle = baubles.getStackInSlot(i);
			if(mantle != null && mantle.getItem() == DHItems.invisibilityMantle) {
				return true;
			}
		}
		return false;
	}

	private static void avengerHurt(Entity source, EntityLivingBase entity) {
		if(source == null) {
			return;
		}
		if(!(source instanceof EntityPlayer)) {
			return;
		}
		NBTTagCompound tag = entity.getEntityData();
		if(entity instanceof EntityBanshee
				|| entity instanceof EntitySpirit
				|| entity instanceof EntityPoltergeist
				|| entity instanceof EntityNightmare
		) {
			tag.setInteger("DHMagicAvenger", tag.getInteger("DHMagicAvenger") + 1);
			return;
		}
		if(entity instanceof EntityPlayer
				&& (ElfUtils.isVampOrWolf(entity)
				|| WorldProviderDreamWorld.getPlayerIsSpiritWalking((EntityPlayer)entity)
		)
		) {
			tag.setInteger("DHMagicAvenger", tag.getInteger("DHMagicAvenger") + 1);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void lowestHit(LivingHurtEvent e) {
		if(e.entity instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer)e.entity;
			DeathlyProperties props = DeathlyProperties.get(p);
			double afterDamage = ISpecialArmor.ArmorProperties.ApplyArmor(p, p.inventory.armorInventory, e.source, e.ammount);
			if(props.getDamageLog()) {
				p.addChatMessage(new ChatComponentText("Amount after absorption: §5" + afterDamage));
			}
			if(p.getEntityData().getBoolean("adaptiveDamage")) {
				p.getEntityData().setBoolean("adaptiveDamage", false);
				p.getEntityData().setBoolean("absorbedDamage", afterDamage < 7);
			}
		}
		if(e.source.getEntity() != null && e.source.getEntity() instanceof EntityPlayer && e.entity instanceof EntityLivingBase) {
			EntityLivingBase entity = (EntityLivingBase)e.entity;
			EntityPlayer playerSource = (EntityPlayer)e.source.getEntity();
			DeathlyProperties props = DeathlyProperties.get(playerSource);
			if(entity instanceof EntityPlayer) {
				EntityPlayer p = (EntityPlayer)entity;
				double afterDamage = ISpecialArmor.ArmorProperties.ApplyArmor(p, p.inventory.armorInventory, e.source, e.ammount);
				playerSource.addChatMessage(new ChatComponentText("Amount after absorption: §5" + afterDamage));
			}
			if(props.getDamageLog()) {
				playerSource.addChatMessage(new ChatComponentText("Amount after absorption: §5" + e.ammount));
			}
		}

	}

	@SubscribeEvent
	public void onHeal(LivingHealEvent e) {
		if(!(e.entityLiving instanceof EntityPlayer)
				// TODO replace System.currentTimeMillis() to entity.tickExisted, considered more bug-safe
				|| e.entityLiving.getEntityData().getLong("DHMending") <= System.currentTimeMillis()
		) {
			return;
		}
		EntityPlayer p = (EntityPlayer)e.entityLiving;
		for(ItemStack stack: p.inventory.armorInventory) {
			if(stack.isItemDamaged()) {
				stack.setItemDamage(stack.getItemDamage() - 1);
			}
		}
		for(ItemStack stack: p.inventory.mainInventory) {
			if(stack.isItemDamaged()) {
				stack.setItemDamage(stack.getItemDamage() - 1);
			}
		}
	}

	@SubscribeEvent
	public void onSendMessage(ServerChatEvent e) {
		chooseHallow(e.message, e.player);
		configureDeadlyPrism(e.message, e.player);
	}

	private static void configureDeadlyPrism(String message, EntityPlayer p) {
		if(!p.getEntityData().getBoolean("DeadlyPrism")) {
			return;
		}
		p.getEntityData().setBoolean("DeadlyPrism", false);
		if(p.getHeldItem() == null || !(p.getHeldItem().getItem() instanceof ItemDeadlyPrism)) {
			return;
		}
		ItemDeadlyPrism prism = (ItemDeadlyPrism)p.getHeldItem().getItem();
		if(message.matches("-?\\d+(\\.\\d+)?")) {
			prism.damageAmount = Float.parseFloat(message);
			p.addChatComponentMessage(new ChatComponentText("Damage set to: §5" + prism.damageAmount));
			return;
		}
		p.addChatComponentMessage(new ChatComponentText("It's not a number..."));
	}

	private static void chooseHallow(String message, EntityPlayer p) {
		DeathlyProperties props = DeathlyProperties.get(p);
		if(!props.getChoice()) {
			return;
		}
		if(message.contains("1")) {
			props.setChoice(false);
			p.inventory.addItemStackToInventory(new ItemStack(DHItems.elderWand));
		}
		if(message.contains("2")) {
			props.setChoice(false);
			p.inventory.addItemStackToInventory(new ItemStack(DHItems.resurrectionStone));
		}
		if(message.contains("3")) {
			props.setChoice(false);
			p.inventory.addItemStackToInventory(new ItemStack(DHItems.invisibilityMantle));
		}
		if(message.contains("4")) {
			double random = Math.random();
			if(random < 0.33) {
				p.inventory.addItemStackToInventory(new ItemStack(DHItems.elderWand));
			}
			else if(random < 0.66) {
				p.inventory.addItemStackToInventory(new ItemStack(DHItems.resurrectionStone));
			}
			else {
				p.inventory.addItemStackToInventory(new ItemStack(DHItems.invisibilityMantle));
			}
			props.setChoice(false);
		}
	}

	@SubscribeEvent
	public void onClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone e) {
		DeathlyProperties.copy(e.original, e.entityPlayer);
	}


	@SubscribeEvent
	public void onEntityConstructing(EntityEvent.EntityConstructing e) {
		if(!(e.entity instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer p = (EntityPlayer)e.entity;
		if(DeathlyProperties.get(p) == null) {
			DeathlyProperties.register(p);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void death(LivingDeathEvent e) throws NoSuchMethodException {
		if(e.entityLiving == null) {
			return;
		}
		playerDeath(e);
		dropAnimalsSpecialLoot(e);
		dropNice(e);
	}
	
	private void dropNice(LivingDeathEvent e) {
		if(e.source == null || e.source.getEntity() == null || !(e.source.getEntity() instanceof EntityPlayer) || !(e.source.getEntity() instanceof EntityLivingBase))
			return;
		EntityPlayer player = (EntityPlayer)e.source.getEntity();
		DeathlyProperties props = DeathlyProperties.get(player);
		int nice = 0;
		double chance = props.getNiceCream();
		Random random = new Random();
		if(random.nextDouble() <= chance/100)
			nice += 4;
		while(chance > 100){
			chance -= 100;
			nice += 10;
		}
		if(nice == 0)
			return;
		props.setNiceCream(props.getNiceCream()-1);
		try {
			Method dropRareDropMethod = e.entityLiving.getClass().getDeclaredMethod("dropRareDrop", int.class);
			dropRareDropMethod.setAccessible(true);
			Method dropFewItemsMethod = e.entityLiving.getClass().getDeclaredMethod("dropFewItems", boolean.class, int.class);
			dropFewItemsMethod.setAccessible(true);
			if(random.nextDouble() < (double)nice/100+0.1)
				dropRareDropMethod.invoke(e.entityLiving,nice);
			dropFewItemsMethod.invoke(e.entityLiving,true,nice);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void playerDeath(LivingDeathEvent e) {
		if(!(e.entityLiving instanceof EntityPlayer) || e.entityLiving.worldObj.isRemote) {
			return;
		}
		EntityPlayer p = (EntityPlayer)e.entityLiving;
		kickAllAround(p);
		if(probablyRessurectByStone(p)) {
			e.isCanceled();
			return;
		}
		if(orHorcruxLives(p)) {
			e.isCanceled();
		}
	}

	private boolean orHorcruxLives(EntityPlayer p) {
		int lives = p.getEntityData().getInteger("Horcrux");
		if(lives < 1) {
			return false;
		}
		p.getEntityData().setInteger("Horcrux", lives - 1);
		attributes.clear();
		attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("HorcruxBonusHp", SpellRegistry.hpCost, 0));
		p.getAttributeMap().applyAttributeModifiers(attributes);
		p.setHealth(p.getMaxHealth());
		attributes.clear();
		if(!p.worldObj.isRemote) {
			p.worldObj.playSoundAtEntity(p, "dh:spell.death" + (Math.random() > 0.5 ? 1 : 2), 1F, 1F);
		}
		return true;
	}

	private static boolean probablyRessurectByStone(EntityPlayer p) {
		ItemBaubleResurrectionStone rs = new ItemBaubleResurrectionStone();
		IInventory baubles = BaublesApi.getBaubles(p);
		for(int i = 0; i < baubles.getSizeInventory(); ++i) {
			ItemStack bauble = baubles.getStackInSlot(i);
			if(bauble == null || bauble.getItem() != DHItems.resurrectionStone || ItemBaubleResurrectionStone.getCharges(bauble) <= 0) {
				continue;
			}
			ItemBaubleResurrectionStone.setCharges(bauble, ItemBaubleResurrectionStone.getCharges(bauble) - 1);
			p.setHealth(p.getMaxHealth());
			p.getEntityData().setInteger("invincible", 100);
			return true;
		}
		return false;
	}

	private static void kickAllAround(EntityPlayer p) {
		DeathlyProperties props = DeathlyProperties.get(p);
		props.setCurrentDuration(0);
		props.setSource(null);
		for(EntityPlayer playerIterator: DHUtils.getEntitiesAround(EntityPlayer.class, p, 64)) {
			if(playerIterator == p) {
				continue;
			}
			DeathlyProperties props2 = DeathlyProperties.get(playerIterator);
			if(props2.getSource() == p) {
				props2.setSource(null);
				props2.setCurrentDuration(0);
			}
		}
	}

	private static void dropAnimalsSpecialLoot(LivingDeathEvent e) {
		if(!(e.entity instanceof EntityLiving)) {
			return;
		}
		if(!(e.source.getEntity() instanceof EntityPlayer)) {
			return;
		}
		DeathlyProperties props = DeathlyProperties.get((EntityPlayer)e.source.getEntity());
		props.addMonster(e.entity.getCommandSenderName());
		props.setMobsKilled(props.getMobsKilled() + 1);
		Calendar currentDate = Calendar.getInstance();
		int currentMonth = currentDate.get(Calendar.MONTH);
		if(currentMonth != Calendar.OCTOBER && currentMonth != Calendar.NOVEMBER) {
			return;
		}
		EntityLiving entityLiving = (EntityLiving)e.entity;
		if(Math.random() > 0.001 * entityLiving.getMaxHealth()) {
			return;
		}
		int amount = 1;
		if(entityLiving.getMaxHealth() > 300) {
			amount += 1;
		}
		if(entityLiving instanceof EntityDeath) {
			amount += 7;
			if(Math.random() < 0.1) {
				amount *= 2;
			}
		}
		if(entityLiving instanceof EntityAbsoluteDeath) {
			amount += 32;
		}
		entityLiving.entityDropItem(new ItemStack(DHItems.trickOrTreat), amount);
	}

	@SubscribeEvent
	public void tooltipEvent(ItemTooltipEvent e) {
		if(e.itemStack.getItem() != Item.getItemFromBlock(DHBlocks.visConverter)) {
			return;
		}
		for(int i = 1; i < 4; ++i) { // it's over...
			e.toolTip.add(StatCollector.translateToLocal("dh.desc.visConverter" + i));
		}
	}

	@SubscribeEvent
	public void blockBreak(BlockEvent.BreakEvent e) {
		EntityPlayer p = e.getPlayer();
		if(p.capabilities.isCreativeMode
				|| e.world.isRemote || p.worldObj.isRemote
				// Если моя руда не содержит в ключе локализации слово ore? Мне нахер пойти тогда?)
				|| !DHUtils.hasDeathlyHallow(p) || (!e.block.getUnlocalizedName()
																 .toLowerCase()
																 .contains("ore")
		)
		) {
			return;
		}
		ItemStack stack = p.getHeldItem();
		if(stack == null || stack.getItem() != Witchery.Items.KOBOLDITE_PICKAXE) {
			return;
		}
		double chance = (1F + EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack)) / 1000F;
		@SuppressWarnings("unchecked")
		Collection<PotionEffect> effects = p.getActivePotionEffects();
		for(PotionEffect effect: effects) {
			if(effect.getPotionID() == Witchery.Potions.FORTUNE.id) {
				chance *= 2;
			}
		}
		// rolling 1d20
		if(Math.random() > chance) { // dice cancer
			return;
		}
		// TODO are you really sure that it IS needed here?
		e.world.setBlock(e.x, e.y, e.z, Blocks.air);
		if(Math.random() > chance) { // rerolling 1d20
			p.entityDropItem(Witchery.Items.GENERIC.itemKobolditeDust.createStack(), 1);
			return;
		}
		if(Math.random() > chance) { // rerolling 1d20
			p.entityDropItem(Witchery.Items.GENERIC.itemKobolditeNugget.createStack(), 1);
			return;
		}
		p.entityDropItem(Witchery.Items.GENERIC.itemKobolditeIngot.createStack(), 1); // nat 20!
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void playerInteract(PlayerInteractEvent e) {
		if(!DHIntegration.thaumcraft || e.entityPlayer.getHeldItem() == null) {
			return;
		}
		ItemStack stack = e.entityPlayer.getHeldItem();
		if(!stack.getItem()
				 .getUnlocalizedName()
				 .equals("item.witchery:ingredient") || stack.getItemDamage() != 14) {
			return;
		}
		EntityPlayer p = e.entityPlayer;
		MovingObjectPosition mop = DHUtils.rayTrace(p);
		if(mop == null) {
			return;
		}
		if(mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
			return;
		}
		int blockX = mop.blockX;
		int blockY = mop.blockY;
		int blockZ = mop.blockZ;
		TileEntity tile = p.worldObj.getTileEntity(blockX, blockY, blockZ);
		if(!(tile instanceof BlockGrassper.TileEntityGrassper)) {
			return;
		}
		IInventory grassper = (IInventory)tile;
		ItemStack inSlot = grassper.getStackInSlot(0);
		ItemStack focus;
		if(inSlot.getItem() == ConfigItems.itemFocusPech) {
			focus = new ItemStack(DHItems.inferioisMutandis);
		}
		else if(inSlot.getItem() == DHItems.inferioisMutandis) {
			focus = new ItemStack(ConfigItems.itemFocusPech);
		}
		else {
			return;
		}
		--stack.stackSize;
		ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, e.world, blockX, blockY, blockZ, 1.0, 1.0, 8);
		grassper.decrStackSize(0, 1);
		e.world.setBlock(blockX, blockY, blockZ, Blocks.air);
		p.inventory.addItemStackToInventory(focus);
	}

}
