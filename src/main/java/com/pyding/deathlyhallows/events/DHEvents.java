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
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.deathlyhallows.blocks.DHBlocks;
import com.pyding.deathlyhallows.entities.EntityAbsoluteDeath;
import com.pyding.deathlyhallows.entities.EntityNimbus;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.ItemBag;
import com.pyding.deathlyhallows.items.ItemDeadlyPrism;
import com.pyding.deathlyhallows.items.baubles.ItemBaubleInvisibilityMantle;
import com.pyding.deathlyhallows.items.baubles.ItemBaubleResurrectionStone;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketNBTSync;
import com.pyding.deathlyhallows.network.packets.PacketPropertiesSync;
import com.pyding.deathlyhallows.recipes.DHGrassperRecipes;
import com.pyding.deathlyhallows.symbols.SymbolHorcrux;
import com.pyding.deathlyhallows.utils.DHConfig;
import com.pyding.deathlyhallows.utils.DHUtils;
import com.pyding.deathlyhallows.utils.ElfUtils;
import com.pyding.deathlyhallows.utils.IItemDyeable;
import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
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
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public final class DHEvents {
	public static final String
			PLAYER_KEPT_DROPS_TAG = "Dh_playerKeptDrops",
			DROP_COUNT_TAG = "Dh_dropCount",
			DROP_PREFIX_TAG = "Dh_dropPrefix",
			DHOWNER_TAG = "dhowner",
			DHCURSE_TAG = "dhcurse",
			HORCRUX_TAG = "Horcrux",
			DHSPRINT_TAG = "DHSprint";
	private static final DHEvents INSTANCE = new DHEvents();

	private DHEvents() {

	}

	public static void init() {
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		FMLCommonHandler.instance().bus().register(INSTANCE);
	}

	@SubscribeEvent
	public void washInCauldron(PlayerInteractEvent e) {
		if(e.world.isRemote || e.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		ItemStack stack = e.entityPlayer.getHeldItem();
		if(stack == null || !(stack.getItem() instanceof IItemDyeable) || !((IItemDyeable)stack.getItem()).canWashInCauldron(stack)) {
			return;
		}
		Block block = e.world.getBlock(e.x, e.y, e.z);
		if(block != Blocks.cauldron) {
			return;
		}
		BlockCauldron cauldron = (BlockCauldron)block;
		int waterLevel = BlockCauldron.func_150027_b(e.world.getBlockMetadata(e.x, e.y, e.z));
		if(waterLevel > 0) {
			cauldron.func_150024_a(e.world, e.x, e.y, e.z, waterLevel - 1);
			((IItemDyeable)stack.getItem()).removeDyedColor(stack);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onJoinSyncCosmetic(EntityJoinWorldEvent e) {
		if(!(e.entity instanceof EntityPlayer)) {
			return;
		}
		String target = e.entity.getCommandSenderName();
		DHPacketProcessor.sendToServer(new PacketPropertiesSync.Server(
				PacketPropertiesSync.Type.COSMETIC,
				target
		));
	}

	@SubscribeEvent
	public void onStruck(EntityStruckByLightningEvent e) {
		if(!(e.entity instanceof EntityPlayer) || e.lightning == null || e.lightning.isDead) {
			return;
		}
		EntityPlayer p = (EntityPlayer)e.entity;
		NBTTagCompound tag = p.getEntityData();
		ItemStack stack = p.getHeldItem();
		// you can dupe it but you will need at least 1 friend
		if(tag.getLong("DHStrike") > System.currentTimeMillis() || tag.getLong("DHBag") <= System.currentTimeMillis() || stack == null || !(stack.getItem() instanceof ItemBag)) {
			return;
		}
		tag.setLong("DHBag", System.currentTimeMillis() + ItemBag.COOLDOWN);
		stack.splitStack(1);
		p.inventory.addItemStackToInventory(new ItemStack(DHItems.lightningInBag));
		p.inventoryContainer.detectAndSendChanges();
		e.lightning.setDead();
		e.setCanceled(true);
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
					|| !stack.getTagCompound().hasKey(DHOWNER_TAG)
					|| !stack.getTagCompound().getString(DHOWNER_TAG).equals(e.entityPlayer.getDisplayName())
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
		cmp.setInteger(DROP_COUNT_TAG, saved.size());

		int i = 0;
		for(EntityItem keep: saved) {
			ItemStack stack = keep.getEntityItem();
			NBTTagCompound cmp1 = new NBTTagCompound();
			stack.writeToNBT(cmp1);
			cmp.setTag(DROP_PREFIX_TAG + i, cmp1);
			i++;
		}

		NBTTagCompound data = e.entityPlayer.getEntityData();
		if(!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
			data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
		}

		NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		persist.setTag(PLAYER_KEPT_DROPS_TAG, cmp);
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent e) {
		NBTTagCompound data = e.player.getEntityData();
		if(data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
			NBTTagCompound cmp = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			NBTTagCompound cmp1 = cmp.getCompoundTag(PLAYER_KEPT_DROPS_TAG);
			int count = cmp1.getInteger(DROP_COUNT_TAG);
			for(int i = 0; i < count; i++) {
				NBTTagCompound cmp2 = cmp1.getCompoundTag(DROP_PREFIX_TAG + i);
				ItemStack stack = ItemStack.loadItemStackFromNBT(cmp2);
				if(stack == null) {
					continue;
				}
				ItemStack copy = stack.copy();
				e.player.inventory.addItemStackToInventory(copy);
			}
			cmp.setTag(PLAYER_KEPT_DROPS_TAG, new NBTTagCompound());
		}
		if(e.player.getEntityData().getInteger(HORCRUX_TAG) == 0) {
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
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
		if(e.entityLiving == null) {
			return;
		}
		if(e.entity.ticksExisted % 20 == 0) {
			updateAvengerLiving(e.entityLiving);
		}
		if(e.entity.ticksExisted % 4 == 0) {
			EntityLivingBase l = e.entityLiving;
			if(l.getEntityData().getLong("DHStrike") > System.currentTimeMillis() && l instanceof EntityPlayer) {
				Random random = new Random();
				DeathlyProperties props = DeathlyProperties.get((EntityPlayer)l);
				int numba = random.nextInt(15);
				if(random.nextDouble() < 0.5) {
					numba *= -1;
				}
				List<EntityLivingBase> list = DHUtils.getEntitiesAt(EntityLivingBase.class, l.worldObj, l.posX + numba, l.posY + numba, l.posZ + numba, 3);
				for(EntityLivingBase entity: list) {
					entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)l), 50 * (1 + props.getElfLevel()));
				}
				EntityLightningBolt bolt = new EntityLightningBolt(l.worldObj, l.posX + numba, l.posY + numba, l.posZ + numba);
				l.worldObj.addWeatherEffect(bolt);
			}
		}
		updatePlayer(e.entityLiving);
		updateAnimal(e.entityLiving);
		updateLiving(e.entityLiving);
	}

	private void updateLiving(EntityLivingBase entity) {
		NBTTagCompound tag = entity.getEntityData();
		checkSectum(entity, tag);
		checkCurse(entity, tag);
	}

	private static void checkSectum(EntityLivingBase e, NBTTagCompound tag) {
		if(!tag.hasKey("SectumTime")) {
			return;
		}
		int time = tag.getInteger("SectumTime");
		float hp = tag.getFloat("SectumHp");
		if(hp > 0F && time < 5) {
			Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("SectumHPAttribute", hp, 0));
			e.getAttributeMap().applyAttributeModifiers(attributes);
			tag.removeTag("SectumHp");
		}
		if(time > 0) {
			tag.setInteger("SectumTime", time - 1);
		}
		else {
			tag.removeTag("SectumTime");
			tag.removeTag("SectumHp");
		}
	}

	private static void checkCurse(EntityLivingBase e, NBTTagCompound tag) {
		if(!tag.hasKey(DHCURSE_TAG)) {
			return;
		}
		int curse = tag.getInteger(DHCURSE_TAG);
		if(curse % 20 == 0) {
			ParticleEffect.FLAME.send(SoundEffect.NONE, e.worldObj, e.posX, e.posY + e.height / 2D, e.posZ, 0.75D, 0.75D, 64);
			DHPacketProcessor.sendToAllAround(new PacketNBTSync(tag, e.getEntityId()), new NetworkRegistry.TargetPoint(e.dimension, e.posX, e.posY, e.posZ, 64));
		}
		if(!e.isDead && curse < 1) {
			e.setHealth(0);
			DHUtils.deadInside(e, e.getLastAttacker());
		}
		if(curse > 0) {
			tag.setInteger(DHCURSE_TAG, curse - 1);
		}
		else {
			tag.removeTag(DHCURSE_TAG);
		}
	}

	private static void updateAnimal(EntityLivingBase living) {
		if(!(living instanceof EntityLiving)) {
			return;
		}
		EntityLiving entity = (EntityLiving)living;
		NBTTagCompound tag = entity.getEntityData();
		stopChainedFromLeave(entity, tag);
	}

	private static void stopChainedFromLeave(EntityLiving e, NBTTagCompound tag) {
		if(!(e instanceof EntityCreature) || !tag.hasKey("chainX")) {
			return;
		}
		teleportBack(e, tag.getDouble("chainX"), tag.getDouble("chainY"), tag.getDouble("chainZ"));
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
		updateMantle(p);
		updateThaumcraftWandFocus(p);
		if(!(p.worldObj.isRemote)) {
			if(DHConfig.shouldRemove) {
				DHUtils.removeDuplicatesFromInventory(p);
			}
			if(DeathlyProperties.get(p) != null) {
				teleportBackCurse(p, props);
				tellPrikol(p);
			}
		}
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
		teleportBack(p, props.getX(), props.getY(), props.getZ());
		if(p.dimension != props.getDimension()) {
			p.travelToDimension(props.getDimension());
		}
	}

	private static void teleportBack(EntityLivingBase e, double x, double y, double z) {
		final double range = 16F;
		if(e.getDistanceSq(x, y, z) > range * range) {
			e.setPositionAndUpdate(x, y, z);
			e.worldObj.playSoundAtEntity(e, "dh:spell.anima" + DHUtils.getRandomInt(2), 1F, 1F);
			ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, e, 6.0D, 6.0D, 16);
		}
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
		if(tag.getInteger("invincible") > 0) {
			tag.setInteger("invincible", tag.getInteger("invincible") - 1);
		}
		if(tag.getInteger("DopVoid") > 0) {
			tag.setInteger("DopVoid", tag.getInteger("DopVoid") - 1);
		}
	}

	private static void updateMantle(EntityPlayer p) {
		NBTTagCompound tag = p.getEntityData();
		if(tag.getInteger("mantlecd") > 0) {
			tag.setInteger("mantlecd", tag.getInteger("mantlecd") - 1);
		}
		final int mantleDisableCD = 600;
		if(!ItemBaubleInvisibilityMantle.isMantleActive(p)) {
			if(p.isSneaking() && tag.getInteger("mantlecd") <= 0 && DHUtils.hasMantle(p)) {
				tag.setInteger("mantlecd", mantleDisableCD + 600);
				tag.setBoolean("mantleActive", true);
				p.worldObj.playSoundAtEntity(p, "dh:mantle." + DHUtils.getRandomInt(1, 3), 1F, 1F);
			}
			ItemBaubleInvisibilityMantle.setMantleAbilityState(p, false);
			return;
		}
		int mantleCD = tag.getInteger("mantlecd") - mantleDisableCD;
		if(mantleCD <= 0 || !DHUtils.hasMantle(p)) {
			tag.removeTag("mantleActive");
			ItemBaubleInvisibilityMantle.setMantleAbilityState(p, false);
			ItemBaubleInvisibilityMantle.setMantleState(p, false);
			PotionEffect invisibility = p.getActivePotionEffect(Potion.invisibility);
			if(invisibility != null && invisibility.getDuration() <= 0) {
				p.removePotionEffect(Potion.invisibility.id);
			}
		}
		else {
			ItemBaubleInvisibilityMantle.setMantleState(p, true);
			p.addPotionEffect(new PotionEffect(Potion.invisibility.id, mantleCD, -1));
		}
		if(!p.isSneaking()) {
			ItemBaubleInvisibilityMantle.setMantleAbilityState(p, false);
			return;
		}
		ItemBaubleInvisibilityMantle.setMantleAbilityState(p, true);
		p.fallDistance = 0F;
		if(p.worldObj.isRemote) {
			Vec3 vel = p.getLookVec();
			/* uncomment if you want more control for flight, but health steal feature will be too OP (stay still and damage a lot? nah)
			vel.xCoord = p.moveForward * vel.xCoord;
			vel.yCoord = p.moveForward * vel.yCoord;
			vel.zCoord = p.moveForward * vel.zCoord;
			 */
			if(p.moveStrafing * p.moveStrafing > 0.01F) {
				float yaw = (float)Math.PI / 180F * p.rotationYaw;
				vel.xCoord += MathHelper.cos(yaw) * p.moveStrafing;
				vel.zCoord += MathHelper.sin(yaw) * p.moveStrafing;
			}
			vel = vel.normalize();
			final double speed = 0.75D;
			p.motionX = vel.xCoord * speed;
			p.motionY = vel.yCoord * speed;
			p.motionZ = vel.zCoord * speed;
		}
		if(p.ticksExisted % 4 != 0) {
			return;
		}
		@SuppressWarnings("unchecked")
		List<EntityLivingBase> entities = p.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, p.boundingBox.expand(1, 0, 1));
		for(EntityLivingBase living: entities) {
			if(living == p
					|| living.isEntityInvulnerable()
					|| living instanceof EntityAbsoluteDeath
					|| living instanceof EntityPlayer && ((EntityPlayer)living).capabilities.isCreativeMode
			) {
				continue;
			}
			float health = living.getHealth();
			float steal = Math.min(health, living.getMaxHealth() * ItemBaubleInvisibilityMantle.HEALTH_STEAL_RATIO);
			living.setHealth(health - steal);
			p.heal(steal);
			int hrt = living.hurtResistantTime;
			living.hurtResistantTime = 0;
			living.attackEntityFrom(DamageSource.outOfWorld, 0.1F);
			living.hurtResistantTime = hrt;
			if(living.getHealth() <= 0.0F) {
				living.onDeath(DamageSource.causePlayerDamage(p).setDamageIsAbsolute().setDamageBypassesArmor());
			}
			p.worldObj.playSoundAtEntity(p, "dh:mantle." + DHUtils.getRandomInt(1, 3), 0.5F, 1.5F);
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
	public void oreDrop(BlockEvent.HarvestDropsEvent event) {
		if(!event.world.isRemote && !event.isSilkTouching && event.block != null && !event.block.hasTileEntity(event.blockMetadata) && event.drops.size() > 0 && event.harvester != null) {
			EntityPlayer player = event.harvester;
			DeathlyProperties props = DeathlyProperties.get(player);
			int nice = 0;
			double chance = props.getNiceCream();
			Random random = new Random();
			if(random.nextDouble() <= chance / 100) {
				nice += 2;
			}
			while(chance > 100) {
				chance -= 100;
				nice += 2;
			}
			if(nice == 0) {
				return;
			}
			props.setNiceCream(props.getNiceCream() - 1);
			ArrayList<ItemStack> drops = event.block.getDrops(event.world, event.x, event.y, event.z, event.blockMetadata, event.fortuneLevel + nice);
			event.drops.clear();
			event.drops.addAll(drops);
		}
	}

	@SubscribeEvent
	public void hurt(LivingSetAttackTargetEvent e) {
		if(!(e.target instanceof EntityPlayer)) {
			return;
		}
		if(!e.target.getEntityData().hasKey("mantleActive")) {
			return;
		}
		e.entityLiving.setRevengeTarget(null);
		if(e.entityLiving instanceof EntityLiving) {
			((EntityLiving)e.entityLiving).setAttackTarget(null);
		}
	}

	@SubscribeEvent
	public void hurt(LivingEvent.LivingUpdateEvent e) {
		EntityLivingBase target = e.entityLiving.getAITarget();
		if(target != null && target.getEntityData().hasKey("mantleActive")) {
			e.entityLiving.setRevengeTarget(null);
		}
		target = e.entityLiving.getLastAttacker();
		if(target != null && target.getEntityData().hasKey("mantleActive")) {
			e.entityLiving.setLastAttacker(null);
		}
		if(e.entityLiving instanceof EntityLiving) {
			EntityLiving living = ((EntityLiving)e.entityLiving);
			EntityLivingBase livingTarget = living.getAttackTarget();
			if(livingTarget != null && livingTarget.getEntityData().hasKey("mantleActive")) {
				living.setAttackTarget(null);
			}
		}
		if(e.entityLiving instanceof EntityCreature) {
			EntityCreature creature = ((EntityCreature)e.entityLiving);
			Entity creatureTarget = creature.getEntityToAttack();
			if(creatureTarget != null && creatureTarget.getEntityData().hasKey("mantleActive")) {
				creature.setTarget(null);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void hurt(LivingHurtEvent e) {
		if(e.isCanceled() || e.entityLiving == null) {
			return;
		}
		avengerHurt(e.source.getEntity(), e.entityLiving);
		if(!(e.entityLiving instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer p = (EntityPlayer)e.entityLiving;
		if(e.source == DamageSource.outOfWorld && p.getEntityData().getInteger("DopVoid") > 0 && e.ammount * 10 < Float.MAX_VALUE) {
			e.ammount = e.ammount * 10;
		}
		if(p.getEntityData().getDouble("mantlecd") <= 0 && DHUtils.hasMantle(p)) {
			ItemStack held = p.getHeldItem();
			if(ItemDeathsClothes.isFullSetWorn(p) && held != null && held.getItem() == Witchery.Items.DEATH_HAND) {
				if(e.ammount > 1F) {
					final float limitSqrt = 31F;
					e.ammount = e.ammount > limitSqrt * limitSqrt ? MathHelper.sqrt_float(e.ammount) - limitSqrt + 1F : 1F;
				}
				e.entityLiving.hurtResistantTime = e.entityLiving.maxHurtResistantTime * 4;
			}
			else {
				e.ammount = e.ammount - 8;
			}

		}
		if(p.getEntityData().getInteger("invincible") > 0) {
			e.ammount = 0;
			e.setCanceled(true);
		}
		DeathlyProperties props = DeathlyProperties.get(p);
		if(props.getBanka() > System.currentTimeMillis()) {
			int warp = Thaumcraft.proxy.getPlayerKnowledge().getWarpTemp(p.getCommandSenderName())
					+ Thaumcraft.proxy.getPlayerKnowledge().getWarpSticky(p.getCommandSenderName()) * 5
					+ Thaumcraft.proxy.getPlayerKnowledge().getWarpPerm(p.getCommandSenderName()) * 10;
			e.ammount = DHUtils.absorbExponentially(e.ammount, warp);
		}
		if(e.ammount < 0.001F) {
			e.setCanceled(true);
		}
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

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void highestHit(LivingHurtEvent e) {
		if(e.entity instanceof EntityPlayer) {
			tryDamageLogFor((EntityPlayer)e.entity, e.source, e.ammount);
		}
		if(e.source.getEntity() != null && e.source.getEntity() instanceof EntityPlayer) {
			tryDamageLogFor((EntityPlayer)e.source.getEntity(), e.source, e.ammount);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void lowestHit(LivingHurtEvent e) {
		if(e.entity instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer)e.entity;
			float afterDamage = getAbsorption(p, e.source, e.ammount);
			TryAndGetAbsorptionLog(p, afterDamage);
			if(p.getEntityData().getBoolean("adaptiveDamage")) {
				p.getEntityData().setBoolean("adaptiveDamage", false);
				p.getEntityData().setBoolean("absorbedDamage", afterDamage < 7);
			}
		}
		if(e.source.getEntity() != null
				&& e.source.getEntity() instanceof EntityPlayer
				&& e.entity instanceof EntityLivingBase
		) {
			EntityPlayer playerSource = (EntityPlayer)e.source.getEntity();
			TryAndGetAbsorptionLog(playerSource, getAbsorption(playerSource, e.source, e.ammount));
		}
	}

	private static float getAbsorption(EntityPlayer p, DamageSource source, float amount) {
		return ISpecialArmor.ArmorProperties.ApplyArmor(p, p.inventory.armorInventory, source, amount);
	}

	private static void TryAndGetAbsorptionLog(EntityPlayer p, float amount) {
		DeathlyProperties props = DeathlyProperties.get(p);
		if(props != null && props.getDamageLog()) {
			ChatUtil.sendTranslated(p, "dh.chat.damageLog.absorption", amount);
		}
	}

	private void tryDamageLogFor(EntityPlayer victim, DamageSource source, float amount) {
		DeathlyProperties props = DeathlyProperties.get(victim);
		if(props == null || !props.getDamageLog()) {
			return;
		}
		if(source.getEntity() == null) {
			ChatUtil.sendTranslated(victim, "dh.chat.damageLog.victim", source.damageType, victim.getCommandSenderName());
		}
		else {
			ChatUtil.sendTranslated(victim, "dh.chat.damageLog.victimAndDealer", source.damageType, victim.getCommandSenderName(), source.getEntity().getCommandSenderName());
		}
		ChatUtil.sendTranslated(victim, "dh.chat.damageLog.damage", amount);
	}

	@SubscribeEvent()
	public void onHeal(LivingHealEvent e) {
		if(!(e.entityLiving instanceof EntityPlayer)) {
			return;
		}

		EntityPlayer p = (EntityPlayer)e.entityLiving;
		DeathlyProperties props = DeathlyProperties.get(p);
		if(props.getCursed() > System.currentTimeMillis()) {
			e.setCanceled(true);
		}
		if(props.getHeal() > System.currentTimeMillis()) {
			List<ItemStack> items = new ArrayList<>();
			Collections.addAll(items, p.inventory.armorInventory);
			Collections.addAll(items, p.inventory.mainInventory);
			for(ItemStack stack: items) {
				if(stack == null || !stack.isItemDamaged()) {
					continue;
				}
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
		ItemStack stack = p.getHeldItem();
		if(stack == null || !(stack.getItem() instanceof ItemDeadlyPrism)) {
			return;
		}
		if(!message.matches("-?\\d+(\\.\\d+)?")) {
			p.addChatComponentMessage(new ChatComponentText("It's not a number..."));
			return;
		}
		float damage = Float.parseFloat(message);
		ItemDeadlyPrism.setPrismDamage(stack, damage);
		p.addChatComponentMessage(new ChatComponentText("Damage set to: §5" + damage));

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
			p.inventory.addItemStackToInventory(getRandomDeathlyHallow());
			props.setChoice(false);
		}
	}

	private static ItemStack getRandomDeathlyHallow() {
		switch(DHUtils.getRandomInt(3)) {
			default:
			case 0:
				return new ItemStack(DHItems.elderWand);

			case 1:
				return new ItemStack(DHItems.resurrectionStone);

			case 2:
				return new ItemStack(DHItems.invisibilityMantle);
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
	public void death(LivingDeathEvent e) {
		if(e.entityLiving == null) {
			return;
		}
		playerDeath(e);
		dropAnimalsSpecialLoot(e);
		dropNice(e);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	public void lowestDeath(LivingDeathEvent event) {
		if(event.entityLiving == null) {
			return;
		}
		if(event.isCanceled() && event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			DeathlyProperties props = DeathlyProperties.get(player);
			if(props.getCursed() > System.currentTimeMillis()) {
				event.setCanceled(false);
				player.setHealth(0);
			}
		}
	}

	private void dropNice(LivingDeathEvent e) {
		if(e.source == null || e.source.getEntity() == null || !(e.source.getEntity() instanceof EntityPlayer) || !(e.source.getEntity() instanceof EntityLivingBase)) {
			return;
		}
		EntityPlayer player = (EntityPlayer)e.source.getEntity();
		DeathlyProperties props = DeathlyProperties.get(player);
		int nice = 0;
		double chance = props.getNiceCream();
		Random random = new Random();
		if(random.nextDouble() <= chance / 100) {
			nice += 4;
		}
		while(chance > 100) {
			chance -= 100;
			nice += 10;
		}
		if(nice == 0) {
			return;
		}
		props.setNiceCream(props.getNiceCream() - 1);
		try {
			Method dropRareDropMethod = e.entityLiving.getClass().getDeclaredMethod("dropRareDrop", int.class);
			dropRareDropMethod.setAccessible(true);
			Method dropFewItemsMethod = e.entityLiving.getClass()
													  .getDeclaredMethod("dropFewItems", boolean.class, int.class);
			dropFewItemsMethod.setAccessible(true);
			if(random.nextDouble() < (double)nice / 100 + 0.1) {
				dropRareDropMethod.invoke(e.entityLiving, nice);
			}
			dropFewItemsMethod.invoke(e.entityLiving, true, nice);
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
	}

	private void playerDeath(LivingDeathEvent e) {
		if(!(e.entityLiving instanceof EntityPlayer) || e.entityLiving.worldObj.isRemote) {
			return;
		}
		EntityPlayer p = (EntityPlayer)e.entityLiving;
		kickAllAround(p);
		if(evenDeathCantFind(p) || probablyRessurectByStone(p) || orHorcruxLives(p)) {
			e.setCanceled(true);
		}
	}

	private boolean orHorcruxLives(EntityPlayer p) {
		int lives = p.getEntityData().getInteger(HORCRUX_TAG);
		if(lives < 1) {
			return false;
		}
		p.getEntityData().setInteger(HORCRUX_TAG, lives - 1);
		Multimap<String, AttributeModifier> attributes = HashMultimap.create();
		attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("HorcruxBonusHp", SymbolHorcrux.HP_COST, 0));
		p.getAttributeMap().applyAttributeModifiers(attributes);
		p.setHealth(p.getMaxHealth());
		if(!p.worldObj.isRemote) {
			p.worldObj.playSoundAtEntity(p, "dh:spell.death" + DHUtils.getRandomInt(1, 2), 1F, 1F);
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

	private static boolean evenDeathCantFind(EntityPlayer p) {
		return p.getEntityData().hasKey("mantleActive");
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
				|| !DHUtils.hasDeathlyHallow(p)
				// Если моя руда не содержит в ключе локализации слово ore? Мне нахер пойти тогда?)
				|| !e.block.getUnlocalizedName().toLowerCase().contains("ore")
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

		e.world.setBlock(e.x, e.y, e.z, Blocks.air); // TODO are you really sure that it IS needed here?
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
	public void createMutandisInferiois(PlayerInteractEvent e) {
		// TODO maybe ritual will be better
		if(!DHIntegration.thaumcraft || e.entityPlayer.getHeldItem() == null || e.world.isRemote) {
			return;
		}
		ItemStack catalyst = e.entityPlayer.getHeldItem();
		EntityPlayer p = e.entityPlayer;
		TileEntity tile = p.worldObj.getTileEntity(e.x, e.y, e.z);
		if(!(tile instanceof BlockGrassper.TileEntityGrassper)) {
			return;
		}
		IInventory grassper = (IInventory)tile;
		ItemStack output = DHGrassperRecipes.getResult(grassper.getStackInSlot(0), catalyst);
		if(output == null) {
			return;
		}
		--catalyst.stackSize;
		ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, e.world, e.x + 0.5D, e.y + 0.5D, e.z + 0.5D, 1.0, 1.0, 8);
		grassper.setInventorySlotContents(0, output);
		e.setCanceled(true);
	}

	public static void processDHKeys(EntityPlayer p, int key, boolean pressed) {
		switch(key) {
			case 1: {
				activateNimbus(p);
				return;
			}
			case 2: {
				p.getEntityData().setBoolean(DHSPRINT_TAG, pressed);
			}
		}
	}


	private static void activateNimbus(EntityPlayer p) {
		if(p.ridingEntity == null) {
			for(int i = 0; i < p.inventory.getSizeInventory(); ++i) {
				ItemStack stack = p.inventory.getStackInSlot(i);
				if(stack == null || stack.getItem() != DHItems.nimbus) {
					continue;
				}
				DHItems.nimbus.onItemRightClick(stack, p.worldObj, p);
				return;
			}
			return;
		}
		if(p.ridingEntity instanceof EntityNimbus) {
			EntityNimbus nimbus = (EntityNimbus)p.ridingEntity;
			p.dismountEntity(nimbus);
			nimbus.setDead();
		}
	}

}
