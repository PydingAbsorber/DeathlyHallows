package com.pyding.deathlyhallows.events;

import baubles.api.BaublesApi;
import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockGrassper;
import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.entity.EntityBanshee;
import com.emoniph.witchery.entity.EntityDeath;
import com.emoniph.witchery.entity.EntityGoblin;
import com.emoniph.witchery.entity.EntityNightmare;
import com.emoniph.witchery.entity.EntityPoltergeist;
import com.emoniph.witchery.entity.EntitySpirit;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.item.ItemDeathsClothes;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.EntityDamageSourceIndirectSilver;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.deathlyhallows.entities.EntityAbsoluteDeath;
import com.pyding.deathlyhallows.entities.EntityNimbus;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.DeadlyPrism;
import com.pyding.deathlyhallows.items.Nimbus3000;
import com.pyding.deathlyhallows.items.ResurrectionStone;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketAnimaMobRender;
import com.pyding.deathlyhallows.network.packets.PacketPlayerRender;
import com.pyding.deathlyhallows.network.packets.PacketRenderAbsoluteDeath;
import com.pyding.deathlyhallows.spells.SpellRegistry;
import com.pyding.deathlyhallows.utils.DHConfig;
import com.pyding.deathlyhallows.utils.DHUtils;
import com.pyding.deathlyhallows.utils.properties.ExtendedPlayer;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.BlockEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import static com.emoniph.witchery.infusion.Infusion.getNBT;

public class DHEvents {
	private static final String TAG_PLAYER_KEPT_DROPS = "Dh_playerKeptDrops";
	private static final String TAG_DROP_COUNT = "Dh_dropCount";
	private static final String TAG_DROP_PREFIX = "Dh_dropPrefix";
	Multimap<String, AttributeModifier> attributes = HashMultimap.create();

	@SubscribeEvent
	public void onPlayerDrops(PlayerDropsEvent event) {
		if(event.entity instanceof EntityPlayer && !event.entityPlayer.worldObj.isRemote && !event.isCanceled()) {
			List<EntityItem> saved = new ArrayList<>();
			for(EntityItem drop: event.drops) {
				if(drop == null) {
					continue;
				}
				ItemStack stack = drop.getEntityItem();
				if(stack.hasTagCompound() && stack.getTagCompound().hasKey("dhowner")) {
					if(stack.getTagCompound().getString("dhowner").equals(event.entityPlayer.getDisplayName())) {
						saved.add(drop);
					}
				}
			}
			if(saved.size() > 0) {
				event.drops.removeAll(saved);
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

				NBTTagCompound data = event.entityPlayer.getEntityData();
				if(!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
					data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
				}

				NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
				persist.setTag(TAG_PLAYER_KEPT_DROPS, cmp);
			}
		}
	}

	@SubscribeEvent
	public void onUsingItem(PlayerUseItemEvent event) {

	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		NBTTagCompound data = event.player.getEntityData();
		if(data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
			NBTTagCompound cmp = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			NBTTagCompound cmp1 = cmp.getCompoundTag(TAG_PLAYER_KEPT_DROPS);

			int count = cmp1.getInteger(TAG_DROP_COUNT);
			for(int i = 0; i < count; i++) {
				NBTTagCompound cmp2 = cmp1.getCompoundTag(TAG_DROP_PREFIX + i);
				ItemStack stack = ItemStack.loadItemStackFromNBT(cmp2);
				if(stack != null) {
					ItemStack copy = stack.copy();
					event.player.inventory.addItemStackToInventory(copy);
				}
			}
			cmp.setTag(TAG_PLAYER_KEPT_DROPS, new NBTTagCompound());
		}
		if(event.player.getEntityData().getInteger("Horcrux") == 0) {
			Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			ExtendedPlayer props = ExtendedPlayer.get(event.player);
			float hpBoost = 4 * props.getElfLvl();
			attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("DH HP", hpBoost, 0));
			event.player.getAttributeMap().applyAttributeModifiers(attributes);
		}
	}

	@SubscribeEvent
	public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
		EntityPlayer player = event.player;
		ExtendedPlayer props = ExtendedPlayer.get(player);
		if(!CrashReportCategory.getLocationInfo((int)player.posX, (int)player.posY, (int)player.posZ).isEmpty()) {
			props.setCurrentDuration(0);
		}
	}

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		ExtendedPlayer props = ExtendedPlayer.get(player);
		if(props.getCurrentDuration() > 0) {
			props.setCurrentDuration(0);
			props.deadInside(player);
		}
		if(MinecraftServer.getServer().getConfigurationManager() != null) {
			if(player.getDisplayName().equals("Pyding")) {
				MinecraftServer.getServer()
							   .getConfigurationManager()
							   .sendChatMsg(new ChatComponentText("§5Да здравствует Пудинг! Добро пожаловать на сервер!"));
			}
		}
		if(!DHIntegration.thaumcraft) {
			player.addChatMessage(new ChatComponentText("§5Deathly Hallows has Thaumcraft integration! Bet you didn't know..."));
		}
		if(!DHIntegration.botania) {
			player.addChatMessage(new ChatComponentText("§aDeathly Hallows has Botania integration! Bet you didn't know..."));
		}
		DHUtils.sync(player);
	}

	public static boolean shouldRemove = true;
	public static long elfInfusionCd = 0;
	public static int timeSurvived = 0;

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entityLivingBase = event.entityLiving;
		if(entityLivingBase.ticksExisted % 20 == 0 && entityLivingBase.getEntityData()
																	  .getInteger("DHMagicAvenger") >= 10) {
			if(entityLivingBase.getHealth() > entityLivingBase.getMaxHealth() * 0.01) {
				entityLivingBase.setHealth((float)(entityLivingBase.getHealth() - entityLivingBase.getMaxHealth() * 0.01));
			}
			else {
				DHUtils.deadInside(entityLivingBase, null);
			}
			if(entityLivingBase.getEntityData().getInteger("DHMagicAvenger") >= 30) {
				int potions = 0;
				for(Object potionEffect: entityLivingBase.getActivePotionEffects()) {
					PotionEffect effect = (PotionEffect)potionEffect;
					if(!Potion.potionTypes[effect.getPotionID()].isBadEffect()) {
						entityLivingBase.removePotionEffect(effect.getPotionID());
						potions++;
					}
				}
				entityLivingBase.hurtResistantTime = 0;
				entityLivingBase.attackEntityFrom(DamageSource.outOfWorld, 100 * potions);
			}
			if(entityLivingBase.getEntityData().getInteger("DHMagicAvenger") >= 100) {
				entityLivingBase.getEntityData().setInteger("DHMagicAvenger", 0);
				if(event.entity instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer)entityLivingBase;
					ExtendedPlayer props = ExtendedPlayer.get(player);
					props.setAvenger(true);
				}
				DHUtils.deadInside(entityLivingBase, null);
			}
		}
		if(event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entity;
			ExtendedPlayer props = ExtendedPlayer.get(player);
			if(player.ticksExisted % 20 == 0 && player.getEntityData().getInteger("DHMagicAvenger") >= 10) {
				Infusion.setCurrentEnergy(player, Math.min(0, Infusion.getCurrentEnergy(player) - 10));
			}
			if(player.ticksExisted % 20 == 0 && props.getAvenger()) {
				com.emoniph.witchery.common.ExtendedPlayer witchProps = com.emoniph.witchery.common.ExtendedPlayer.get(player);
				if(witchProps.isVampire()) {
					witchProps.setVampireLevel(0);
				}
				if(witchProps.getWerewolfLevel() > 0) {
					witchProps.setWerewolfLevel(0);
				}
				if(WorldProviderDreamWorld.getPlayerIsSpiritWalking(player)) {
					WorldProviderDreamWorld.setPlayerIsSpiritWalking(player, false);
				}
				Infusion.setEnergy(player, 0, 0, 0);
			}
			if(player.getEntityData().getInteger("mantlecd") > 0) {
				player.getEntityData().setInteger("mantlecd", player.getEntityData().getInteger("mantlecd") - 1);
			}
			if(player.getEntityData().getInteger("DopVoid") > 0) {
				player.getEntityData().setInteger("DopVoid", player.getEntityData().getInteger("DopVoid") - 1);
			}
			if(player.getEntityData().getInteger("mantlecd") <= 200) {
				player.getEntityData().setBoolean("mantleActive", false);
				player.setInvisible(false);
			}
			if(player.getEntityData().getBoolean("mantleActive")) {
				player.addPotionEffect(new PotionEffect(Potion.invisibility.id, player.getEntityData()
																					  .getInteger("mantlecd"), 250, true));
				player.setInvisible(true);
			}
			if(player.getEntityData().getInteger("invincible") > 0) {
				player.getEntityData().setInteger("invincible", player.getEntityData().getInteger("invincible") - 1);
			}
			if(DHIntegration.thaumcraft) {
				if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemWandCasting) {
					ItemWandCasting wand = (ItemWandCasting)player.getHeldItem().getItem();
					if(wand.getFocus(player.getHeldItem()) == DHItems.inferioisMutandis) {
						for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
							if(player.inventory.getStackInSlot(i) != null) {
								ItemStack stack = player.inventory.getStackInSlot(i);
								if(stack.getItem() == ConfigItems.itemResource && stack.getItemDamage() == 18) {
									if(wand.consumeVis(player.getHeldItem(), player, Aspect.ENTROPY, 100, false)) {
										player.inventory.decrStackSize(i, 1);
										player.inventory.addItemStackToInventory(Witchery.Items.GENERIC.itemMutandis.createStack());
									}
								}
							}
						}
					}
				}
			}
			if(player.ticksExisted % 20 == 0) {
				DHUtils.sync(player);
			}
		}
		if((event.entity instanceof EntityPlayer) && !(event.entity.worldObj.isRemote)) {
			EntityPlayer player = (EntityPlayer)event.entity;
			World world = player.worldObj;
			if(player.getEntityData().getLong("DHArrow") > 0) {
				long time = System.currentTimeMillis() - player.getEntityData().getLong("DHArrow");
				boolean show = player.getEntityData().getBoolean("DHArrowShow");
				boolean show2 = player.getEntityData().getBoolean("DHArrowShow2");
				if(time >= firstShot && show) {
					player.getEntityData().setBoolean("DHArrowShow", false);
					DHUtils.spawnSphere(player, player.getPosition(1), 20, 3, Color.BLUE, 1, 3, 60, 1);
					world.playSoundAtEntity(player, "dh:arrow.arrow_ready_1", 1F, 1F);
				}
				if(time >= secondShot && show2) {
					player.getEntityData().setBoolean("DHArrowShow2", false);
					DHUtils.spawnSphere(player, player.getPosition(1), 20, 3, Color.magenta, 1, 3, 60, 1);
					world.playSoundAtEntity(player, "dh:arrow.arrow_ready_2", 1F, 1F);
				}
			}
			ItemStack stack = player.getHeldItem();
			if(stack != null) {
				if(stack.getItem() == DHItems.elderWand && player.getEntityData().getBoolean("dhkey1")) {
					player.getEntityData().setBoolean("dhkey1", false);
					SpellRegistry spellRegistry = new SpellRegistry();
					if(stack.hasTagCompound()) {
						if(!stack.getTagCompound().hasKey("spell1")) {
							NBTTagCompound nbt = new NBTTagCompound();
							nbt.setInteger("spell1", 1);
							stack.setTagCompound(nbt);
							spellRegistry.performEffect(player, world, 1);
						}
						else {
							int spell1 = stack.getTagCompound().getInteger("spell1");
							spellRegistry.performEffect(player, world, spell1);
						}
					}
				}
			}
			if(shouldRemove) {
				removeDuplicatesFromInventory(player);
			}
			if(player.ticksExisted % 20 == 0 || player.getEntityData().getBoolean("dhkey2")) {
				boolean found = false;
				for(ItemStack itemStack: player.inventory.mainInventory) {
					if(itemStack != null && itemStack.getItem() == DHItems.nimbus) {
						found = true;
						if(player.getEntityData().getBoolean("dhkey2")) {
							Nimbus3000 nimbus3000 = (Nimbus3000)itemStack.getItem();
							nimbus3000.onItemRightClick(itemStack, player.worldObj, player);
						}
					}
				}
				if(!found && player.ridingEntity instanceof EntityNimbus) {
					Entity entity = player.ridingEntity;
					player.dismountEntity(entity);
					entity.setDead();
				}
				player.getEntityData().setBoolean("dhkey2", false);
			}
			if(ExtendedPlayer.get((EntityPlayer)event.entity) != null) {
				ExtendedPlayer props = ExtendedPlayer.get(player);
				if(props.getCurrentDuration() > 0) {
					if(props.getCurrentDuration() == 1) {
						props.deadInside(player);
					}
					props.lowerDuration();
					if(Math.abs(player.posX - props.getX()) >= 25 ||
							Math.abs(player.posY - props.getY()) >= 25 ||
							Math.abs(player.posZ - props.getZ()) >= 25) {
						player.setPositionAndUpdate(props.getX(), props.getY(), props.getZ());
						if(Math.random() > 0.5) {
							world.playSoundAtEntity(player, "dh:spell.anima2", 1F, 1F);
						}
						else {
							world.playSoundAtEntity(player, "dh:spell.anima1", 1F, 1F);
						}
						ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, player, 6.0D, 6.0D, 16);
					}
					if(player.dimension != props.getDimension()) {
						player.travelToDimension(props.getDimension());
					}
				}
				if(player.getEntityData().getInteger("casterCurse") > 0) {
					player.getEntityData()
						  .setInteger("casterCurse", player.getEntityData().getInteger("casterCurse") - 1);
					if(Math.abs(player.posX - props.getX()) >= 25 ||
							Math.abs(player.posY - props.getY()) >= 25 ||
							Math.abs(player.posZ - props.getZ()) >= 25) {
						player.setPositionAndUpdate(props.getX(), props.getY(), props.getZ());
						if(Math.random() > 0.5) {
							world.playSoundAtEntity(player, "dh:spell.anima2", 1F, 1F);
						}
						else {
							world.playSoundAtEntity(player, "dh:spell.anima1", 1F, 1F);
						}
						ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, player, 6.0D, 6.0D, 16);
					}
					if(player.dimension != props.getDimension()) {
						player.travelToDimension(props.getDimension());
					}
				}
				com.emoniph.witchery.common.ExtendedPlayer witchProps = com.emoniph.witchery.common.ExtendedPlayer.get(player);
				if(props.getElfLvl() == 0) {
					if(Infusion.getCurrentEnergy(player) == 0 && props.getTrigger() == 0 && Infusion.getInfusionID(player) != 0) {
						props.setTrigger(1);
					}
					if(Infusion.getCurrentEnergy(player) == Infusion.getMaxEnergy(player) && props.getTrigger() == 1) {
						props.setTrigger(0);
						props.setElfCount(props.getElfCount() + 1);
						if(props.getElfCount() == 1) {
							ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elf1");
						}
					}
					if(props.getElfCount() >= 20 && !witchProps.isVampire() && witchProps.getWerewolfLevel() == 0) {
						props.setElfCount(0);
						props.increaseElfLvl();
						ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elf2");
					}
				}
				else {
					if(witchProps.isVampire()) {
						witchProps.setVampireLevel(0);
						ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "dh.chat.no");
					}
					if(witchProps.getWerewolfLevel() > 0) {
						witchProps.setWerewolfLevel(0);
						ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "dh.chat.no");
					}
				}
				switch(props.getElfLvl()) {
					case 1: {
						if(player.experienceLevel >= DHConfig.getElfRequirements(props.getElfLvl() + 1)) {
							props.increaseElfLvl();
							ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elflvl1");
						}
						break;
					}
					case 2: {
						if(player.posY <= DHConfig.getElfRequirements(props.getElfLvl() + 1)) {
							props.increaseElfLvl();
							ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elflvl1");
						}
						break;
					}
					case 3: {
						if(totalLvl(player) >= DHConfig.getElfRequirements(props.getElfLvl() + 1)) {
							props.increaseElfLvl();
							ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elflvl1");
						}
						break;
					}
					case 4: {
						if(props.getMobsKilled() >= DHConfig.getElfRequirements(props.getElfLvl() + 1)) {
							props.increaseElfLvl();
							ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elflvl1");
						}
						break;
					}
					case 5: {
						if(props.getFoodEaten() >= DHConfig.getElfRequirements(props.getElfLvl() + 1)) {
							props.increaseElfLvl();
							ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elflvl1");
						}
						break;
					}
					case 6: {
						if(hasAmountOfPotions(player, 10, true)) {
							timeSurvived++;
							if(timeSurvived > DHConfig.getElfRequirements(props.getElfLvl() + 1)) {
								timeSurvived = 0;
								props.increaseElfLvl();
								ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elflvl1");
							}
						}
						break;
					}
					case 7: {
						if(WorldProviderDreamWorld.getPlayerIsSpiritWalking(player)) {
							timeSurvived++;
							if(timeSurvived > DHConfig.getElfRequirements(props.getElfLvl() + 1)) {
								timeSurvived = 0;
								props.increaseElfLvl();
								ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elflvl1");
							}
						}
						break;
					}
					case 8: {
						if(props.getFoodCollection() != null) {
							if(props.getFoodCollection()
									.size() > DHConfig.getElfRequirements(props.getElfLvl() + 1)) {
								props.increaseElfLvl();
								ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elflvl1");
							}
						}
						break;
					}
					case 9: {
						if(props.getSpellsUsed() >= DHConfig.getElfRequirements(props.getElfLvl() + 1)) {
							props.setAllNull();
							props.increaseElfLvl();
							ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elflvl1");
						}
						break;
					}
				}
				if(witchProps.isVampire() && witchProps.getWerewolfLevel() > 0 && Math.random() < 1.0 / 2592000) {
					ChatUtil.sendTranslated(EnumChatFormatting.AQUA, player, "dh.chat.prikol");
				}
				if(props.getElfLvl() == 10 && Infusion.getInfusionID(player) > 0) {
					NBTTagCompound nbt = getNBT(player);
					nbt.setInteger("witcheryInfusionChargesMax", 400);
				}
				if(props.getElfLvl() >= 1 && Infusion.getInfusionID(player) > 0 && System.currentTimeMillis() - elfInfusionCd > 5 * 1000 && Infusion.getCurrentEnergy(player) < Infusion.getMaxEnergy(player)) { //secs*1000 not Sex damn you pervert
					elfInfusionCd = System.currentTimeMillis();
					if(!(Infusion.getMaxEnergy(player) <= Infusion.getCurrentEnergy(player) + props.getElfLvl())) {
						Infusion.setCurrentEnergy(player, Infusion.getCurrentEnergy(player) + props.getElfLvl());
					}
					else {
						Infusion.setCurrentEnergy(player, Infusion.getMaxEnergy(player));
					}
				}
				if(Infusion.getMaxEnergy(player) < Infusion.getCurrentEnergy(player)) {
					Infusion.setCurrentEnergy(player, Infusion.getMaxEnergy(player));
				}
				if(!(player.getMaxHealth() > 0) || player.getHealth() != player.getHealth()) {
					props.deadInside(player);
				}
			}
		}
		if(event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entity;
			if(player.getEntityData().getInteger("dhcurse") > 0) {
				if(player.getEntityData().getInteger("dhcurse") == 200) {
					PacketPlayerRender packet = new PacketPlayerRender(player.getEntityData());
					DHPacketProcessor.sendToPlayer(packet, player);
				}
				player.getEntityData().setInteger("dhcurse", player.getEntityData().getInteger("dhcurse") - 1);
			}
		}
		if(event.entity instanceof EntityLiving) {
			EntityLiving entity = (EntityLiving)event.entityLiving;
			if(entity.getEntityData().getInteger("dhcurse") > 0) {
				if(entity.getEntityData().getInteger("dhcurse") % 10 == 0) {
					ParticleEffect.FLAME.send(SoundEffect.NONE, entity, 1, 1, 64);
				}
				PacketAnimaMobRender packet = new PacketAnimaMobRender(entity.getEntityData(), entity.getEntityId());
				NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 64);
				if(entity.getEntityData().getInteger("dhcurse") == 1200) {
					DHPacketProcessor.sendToAllAround(packet, targetPoint);
				}
				if(entity.getEntityData().getInteger("dhcurse") == 200) {
					DHPacketProcessor.sendToAllAround(packet, targetPoint);
				}
				if(entity.getEntityData().getInteger("dhcurse") == 1) {
					EntityUtil.instantDeath(entity, entity.getLastAttacker());
				}
				entity.getEntityData().setInteger("dhcurse", entity.getEntityData().getInteger("dhcurse") - 1);
			}
			if(entity.getEntityData().getDouble("chainX") != 0 && entity instanceof EntityCreature) {
				if(entity.getDistance(entity.getEntityData().getDouble("chainX"), entity.getEntityData()
																						.getDouble("chainY"), entity.getEntityData()
																													.getDouble("chainZ")) > 16) {
					entity.setPositionAndUpdate(entity.getEntityData().getDouble("chainX"), entity.getEntityData()
																								  .getDouble("chainY"), entity.getEntityData()
																															  .getDouble("chainZ"));
				}
			}
		}
		if(event.entity.getEntityData().getBoolean("immortal") && event.entity instanceof EntityGoblin) {
			((EntityGoblin)event.entity).setHealth(((EntityGoblin)event.entity).getMaxHealth());
		}
		if(event.entity instanceof EntityAbsoluteDeath) { //sethealth absorbtion
			EntityAbsoluteDeath death = (EntityAbsoluteDeath)event.entityLiving;
			if(death.isEntityAlive() && !death.worldObj.isRemote) {
				if(death.getEntityData().getFloat("dhhp") == 0) {
					death.getEntityData().setFloat("dhhp", death.getHealth());
				}
				if(death.getHealth() + 51 < death.getEntityData().getFloat("dhhp") && !death.getEntityData()
																							.getBoolean("dhdamaged")) {
					death.setHealth(death.getEntityData().getFloat("dhhp"));
					if(death.getLastAttacker() != null && death.getLastAttacker() instanceof EntityPlayer) {
						ChatUtil.sendTranslated(EnumChatFormatting.RED, (EntityPlayer)death.getLastAttacker(), "dh.chat.sethealth");
					}
				}
			}
		}
		if(event.entity instanceof EntityLivingBase) {
			EntityLivingBase entity = (EntityLivingBase)event.entity;
			if(entity.getEntityData().getInteger("SectumTime") > 0) {
				if(entity.getEntityData().getInteger("SectumTime") == 1) {
					attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("SectumHPAttribute", entity.getEntityData()
																																					 .getFloat("SectumHp"), 0));
					entity.getAttributeMap().applyAttributeModifiers(attributes);
					attributes.clear();
					entity.getEntityData().setFloat("SectumHp", 0);
				}
				entity.getEntityData().setInteger("SectumTime", entity.getEntityData().getInteger("SectumTime") - 1);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void attack(LivingAttackEvent event) {
		if(!event.entityLiving.worldObj.isRemote && !event.isCanceled() && event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			if(player.getEntityData()
					 .getBoolean("mantleActive") && (event.source.getEntity() != null || event.source == DamageSource.inWall)) {
				event.setCanceled(true);
				return;
			}
		}
		if(event.entity instanceof EntityAbsoluteDeath && !event.entity.worldObj.isRemote) {
			EntityAbsoluteDeath entity = (EntityAbsoluteDeath)event.entity;
			int absolute = entity.getEntityData().getInteger("absoluteblock");
			int earth = entity.getEntityData().getInteger("earthblock");
			int fire = entity.getEntityData().getInteger("fireblock");
			int generic = entity.getEntityData().getInteger("genericblock");
			int magic = entity.getEntityData().getInteger("magicblock");
			int wither = entity.getEntityData().getInteger("witherblock");
			int voed = entity.getEntityData().getInteger("voidblock");
			int water = entity.getEntityData().getInteger("waterblock");
			if(event.source == DamageSource.wither) {
				if(wither == 90) {
					entity.teleportRandomly();
					entity.rage = 100;
				}
				wither += 10;
			}
			else if(event.source.isDamageAbsolute()) {
				if(absolute == 90) {
					entity.teleportRandomly();
					entity.rage = 100;
				}
				absolute += 10;
			}
			else if(event.source == DamageSource.fall || event.source == DamageSource.inWall) {
				if(earth == 90) {
					entity.teleportRandomly();
					entity.rage = 100;
				}
				earth += 10;
			}
			else if(event.source == DamageSource.inFire || event.source == DamageSource.onFire || event.source == DamageSource.lava) {
				if(fire == 90) {
					entity.teleportRandomly();
					entity.rage = 100;
				}
				fire += 10;
			}
			else if(event.source == DamageSource.magic || event.source.isMagicDamage()) {
				if(magic == 90) {
					entity.teleportRandomly();
					entity.rage = 100;
				}
				magic += 10;
			}
			else if(event.source == DamageSource.outOfWorld) {
				if(voed == 90) {
					entity.teleportRandomly();
					entity.rage = 100;
				}
				voed += 10;
			}
			else if(event.source == DamageSource.drown) {
				if(water == 90) {
					entity.teleportRandomly();
					entity.rage = 100;
				}
				water += 10;
			}
			else {
				if(generic == 90) {
					entity.teleportRandomly();
					entity.rage = 100;
				}
				generic += 10;
			}
			entity.getEntityData().setInteger("witherblock", wither);
			entity.getEntityData().setInteger("absoluteblock", absolute);
			entity.getEntityData().setInteger("earthblock", earth);
			entity.getEntityData().setInteger("fireblock", fire);
			entity.getEntityData().setInteger("magicblock", magic);
			entity.getEntityData().setInteger("voidblock", voed);
			entity.getEntityData().setInteger("waterblock", water);
			entity.getEntityData().setInteger("genericblock", generic);
			PacketRenderAbsoluteDeath packet = new PacketRenderAbsoluteDeath(entity.getEntityData(), entity.getEntityId());
			NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 64);
			DHPacketProcessor.sendToAllAround(packet, target);
		}
		if(event.entity.getEntityData().getBoolean("immortal") && event.entity instanceof EntityGoblin) {
			event.setCanceled(true);
		}
	}


	public float getBlock(EntityLivingBase entity, String name, float damage) {
		switch(DHConfig.deathDifficulty) {
			case 3:
				return damage - (damage * ((float)entity.getEntityData().getInteger(name) / 100));
			case 2: {
				return damage - (float)(damage * Math.min(0.9, (entity.getEntityData().getInteger(name) / 100)));
			}
			default:
				return damage - (float)(damage * Math.min(0.5, (entity.getEntityData().getInteger(name) / 100)));
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void highestHit(LivingHurtEvent event) {
		if(event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entity;
			ExtendedPlayer props = ExtendedPlayer.get(player);
			if(props.getDamageLog()) {
				if(event.source.getEntity() != null) {
					player.addChatMessage(new ChatComponentText("Damage Source: " + event.source.damageType + " §7Victim: " + event.entity.getCommandSenderName() + " Dealer: " + event.source.getEntity()
																																															  .getCommandSenderName()));
				}
				else {
					player.addChatMessage(new ChatComponentText("Damage Source: " + event.source.damageType + " §7Victim: " + event.entity.getCommandSenderName()));
				}
				player.addChatMessage(new ChatComponentText("Amount: §5" + event.ammount));
			}
		}
		if(event.source.getEntity() != null && event.source.getEntity() instanceof EntityPlayer) {
			EntityPlayer playerSource = (EntityPlayer)event.source.getEntity();
			ExtendedPlayer props = ExtendedPlayer.get(playerSource);
			if(props.getDamageLog()) {
				playerSource.addChatMessage(new ChatComponentText("Damage Source: " + event.source.damageType + " §7Victim: " + event.entity.getCommandSenderName() + " Dealer: " + event.source.getEntity()
																																																.getCommandSenderName()));
				playerSource.addChatMessage(new ChatComponentText("Amount: §5" + event.ammount));
			}
		}
	}

	@SubscribeEvent
	public void bowUseStart(ArrowNockEvent event) {
		EntityPlayer player = event.entityPlayer;
		player.getEntityData().setLong("DHArrow", System.currentTimeMillis());
		player.getEntityData().setBoolean("DHArrowShow", true);
		player.getEntityData().setBoolean("DHArrowShow2", true);
	}

	long firstShot = 1000;
	long secondShot = 2000;

	@SubscribeEvent
	public void bowUse(ArrowLooseEvent event) {
		EntityPlayer player = event.entityPlayer;
		ExtendedPlayer props = ExtendedPlayer.get(player);
		long time = System.currentTimeMillis() - player.getEntityData().getLong("DHArrow");
		player.getEntityData().setLong("DHArrow", 0);
		long perfectTime = secondShot + 150;
		if(props.getElfLvl() >= 10 && (time > secondShot || player.getEntityData().getInteger("DHShot") > 0)) {

			if(player.getEntityData().getInteger("DHShot") > 0) {
				DHUtils.spawnArrow(player, 3);
				player.getEntityData().setInteger("DHShot", player.getEntityData().getInteger("DHShot") - 1);
				event.setCanceled(true);
			}
			else if(time < perfectTime) {
				DHUtils.spawnArrow(player, 3);
				player.getEntityData().setInteger("DHShot", 5);
				event.setCanceled(true);
			}
			else {
				DHUtils.spawnArrow(player, 2);
				event.setCanceled(true);
			}
			return;
		}
		if(props.getElfLvl() >= 7 && time > firstShot) {
			DHUtils.spawnArrow(player, 1);
			event.setCanceled(true);
		}
	}

	public long aoeCD = 0;

	@SubscribeEvent(priority = EventPriority.LOW)
	public void hurt(LivingHurtEvent event) {
		if(!event.isCanceled()) {
			if(event.source.getEntity() != null && event.entity instanceof EntityLivingBase) {
				EntityLivingBase entity = event.entityLiving;
				if(event.source.getEntity() instanceof EntityPlayer) {
					EntityPlayer attacker = (EntityPlayer)event.source.getEntity();
					if(entity instanceof EntityBanshee || entity instanceof EntitySpirit || entity instanceof EntityPoltergeist || entity instanceof EntityNightmare) {
						entity.getEntityData()
							  .setInteger("DHMagicAvenger", entity.getEntityData().getInteger("DHMagicAvenger") + 1);
					}
					else if(entity instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer)entity;
						com.emoniph.witchery.common.ExtendedPlayer witchProps = com.emoniph.witchery.common.ExtendedPlayer.get(player);
						if(witchProps.isVampire() || witchProps.getWerewolfLevel() > 0 || WorldProviderDreamWorld.getPlayerIsSpiritWalking(player)) {
							player.getEntityData()
								  .setInteger("DHMagicAvenger", entity.getEntityData()
																	  .getInteger("DHMagicAvenger") + 1);
						}
					}
					ExtendedPlayer props = ExtendedPlayer.get(attacker);
					if(event.source.isProjectile()) {
						float radius = 8;
						float damageAoe = 0.3F;
						if(props.getElfLvl() >= 9) {
							radius *= 2;
							damageAoe *= 2;
						}
						List entities = event.entity.worldObj.getEntitiesWithinAABB(EntityLiving.class, event.entity.boundingBox.expand(radius, radius, radius));
						if(props.getElfLvl() >= 1) {
							event.ammount = event.ammount * props.getElfLvl();
						}
						if(props.getElfLvl() >= 3) {
							event.source.setMagicDamage();
						}
						if(props.getElfLvl() >= 6 && !event.source.isDamageAbsolute()) {
							DHUtils.spawnSphere(entity, entity.getPosition(1), (int)(radius * 20), radius, Color.BLUE, 1, 6, 60, 1);
							for(Object o: entities) {
								if(o != attacker && o != event.entity) {
									EntityLiving target = (EntityLiving)o;
									target.setLastAttacker(attacker);
									ParticleEffect.MAGIC_CRIT.send(null, target, 2, 2, 64);
									target.attackEntityFrom(DamageSource.causePlayerDamage(attacker)
																		.setDamageIsAbsolute()
																		.setProjectile(), event.ammount * damageAoe);
								}
							}
						}
						if(props.getElfLvl() >= 9 && event.entity instanceof EntityPlayer) {
							EntityPlayer target = (EntityPlayer)event.entity;
							com.emoniph.witchery.common.ExtendedPlayer wprops = com.emoniph.witchery.common.ExtendedPlayer.get(target);
							if(wprops.isVampire()) {
								wprops.setBloodReserve(0);
								/*if(Math.random() < 0.05) {
									wprops.setVampireLevel(wprops.getVampireLevel() - 1);
								}*/
								target.attackEntityFrom(EntityUtil.DamageSourceVampireFire.magic, event.ammount * 10);
							}
							if(wprops.getWerewolfLevel() > 0) {
								/*if(Math.random() < 0.05) {
									wprops.setWerewolfLevel(wprops.getWerewolfLevel() - 1);
								}*/
								target.attackEntityFrom(EntityDamageSourceIndirectSilver.magic, event.ammount * 10);
							}
							if((wprops.isVampire() || wprops.getWerewolfLevel() > 0) && target.getHealth() <= target.getMaxHealth() * 0.1) {
								props.deadInside(target);
							}
						}
						if(props.getElfLvl() == 10) {
							if(event.entityLiving instanceof EntityPlayer && Math.random() < 0.1 && ((EntityPlayer)event.entity).getHealth() > ((EntityPlayer)event.entity).getMaxHealth() * 0.3) {
								EntityUtil.instantDeath(event.entityLiving, attacker);
							}
						}
					}
					else {
						if(props.getElfLvl() >= 1 && !(event.source.isMagicDamage())) {
							if(attacker.getHeldItem() != null && attacker.getHeldItem()
																		 .getItem() != DHItems.elderWand) {
								event.ammount = event.ammount * 0.05F;
							}
						}
					}
				}
			}
			if(event.entityLiving instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)event.entityLiving;
				if(event.source == DamageSource.outOfWorld && player.getEntityData().getInteger("DopVoid") > 0) {
					if(event.ammount * 10 < Float.MAX_VALUE) {
						event.ammount = event.ammount * 10;
					}
				}
				if(BaublesApi.getBaubles(player).getStackInSlot(3) != null) {
					if(player.getEntityData().getDouble("mantlecd") == 0 && BaublesApi.getBaubles(player)
																					  .getStackInSlot(3)
																					  .getItem() == DHItems.invisibilityMantle) {
						if(ItemDeathsClothes.isFullSetWorn(player) && player.getHeldItem() != null && player.getHeldItem()
																											.getItem() == Witchery.Items.DEATH_HAND) {
							event.ammount = Math.min(event.ammount - 1000, 4);
						}
						else {
							event.ammount = event.ammount - 50;
						}
					}
				}
				if(player.getEntityData().getInteger("invincible") > 0) {
					event.ammount = 0;
					event.setCanceled(true);
				}
				ExtendedPlayer props = ExtendedPlayer.get(player);
				if(props.getElfLvl() >= 7) {
					if(player.getHeldItem() != null) {
						if(player.getHeldItem().getItem() instanceof ItemBow && Math.random() < 0.75) {
							event.ammount = 0;
							event.source.setFireDamage();
							event.setCanceled(true);
						}
					}
				}
				if(props.getElfLvl() >= 1) {
					if(event.source.isMagicDamage()) {
						event.ammount = event.ammount * 0.1F;
					}
					else {
						if(event.ammount * 10 < Float.MAX_VALUE) {
							event.ammount = event.ammount * 2;
						}
					}
				}
				if(player.getEntityData().getLong("DHBanka") > System.currentTimeMillis()) {
					int warp = Thaumcraft.proxy.getPlayerKnowledge().getWarpTemp(player.getCommandSenderName())
							+ Thaumcraft.proxy.getPlayerKnowledge().getWarpSticky(player.getCommandSenderName()) * 5
							+ Thaumcraft.proxy.getPlayerKnowledge().getWarpPerm(player.getCommandSenderName()) * 10;
					event.ammount = DHUtils.absorbExponentially(event.ammount, warp);
				}
			}
			if(event.entityLiving instanceof EntityAbsoluteDeath && !event.isCanceled()) {
				int difficulty = DHConfig.deathDifficulty;
				EntityAbsoluteDeath entity = (EntityAbsoluteDeath)event.entityLiving;
				if(difficulty == 3) {
					double maxDamage = 300.0;
					double scaleFactor = 0.002;
					if(event.ammount < 300) {
						event.ammount = (float)(maxDamage * Math.log10(1.0 + scaleFactor * event.ammount));
					}
					else {
						scaleFactor = 0.00000002;
						event.ammount = (float)(maxDamage / (1 + scaleFactor * Math.pow(event.ammount, 3)));
					}
				}
				if(event.source == DamageSource.wither) {
					event.ammount = getBlock(entity, "witherblock", event.ammount);
				}
				else if(event.source.isDamageAbsolute()) {
					event.ammount = getBlock(entity, "absoluteblock", event.ammount);
				}
				else if(event.source == DamageSource.fall || event.source == DamageSource.inWall) {
					event.ammount = getBlock(entity, "earthblock", event.ammount);
				}
				else if(event.source == DamageSource.inFire || event.source == DamageSource.onFire || event.source == DamageSource.lava) {
					event.ammount = getBlock(entity, "fireblock", event.ammount);
					entity.extinguish();
				}
				else if(event.source == DamageSource.magic || event.source.isMagicDamage()) {
					event.ammount = getBlock(entity, "magicblock", event.ammount);
				}
				else if(event.source == DamageSource.outOfWorld) {
					event.ammount = getBlock(entity, "voidblock", event.ammount);
				}
				else if(event.source == DamageSource.drown) {
					event.ammount = getBlock(entity, "waterblock", event.ammount);
				}
				else {
					event.ammount = getBlock(entity, "genericblock", event.ammount);
				}
				if(event.ammount > 50) {
					event.ammount = 50;
				}
				if(event.ammount > 0) {
					entity.getEntityData().setBoolean("dhdamaged", true);
					entity.getEntityData().setFloat("dhhp", entity.getHealth());
				}
				else {
					entity.getEntityData().setBoolean("dhdamaged", false);
				}
				if(event.source.getEntity() != null) {
					NBTTagCompound nbt = new NBTTagCompound();
					String id = String.valueOf(event.source.getEntity().getEntityId());
					float damageDealt = event.ammount;
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
			}
			if(event.entity instanceof EntityGoblin && event.entity.getEntityData().getBoolean("immortal")) {
				event.ammount = 0;
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void lowestHit(LivingHurtEvent event) {
		if(event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entity;
			ExtendedPlayer props = ExtendedPlayer.get(player);
			double afterDamage = ISpecialArmor.ArmorProperties.ApplyArmor(player, player.inventory.armorInventory, event.source, event.ammount);
			if(props.getDamageLog()) {
				player.addChatMessage(new ChatComponentText("Amount after absorption: §5" + afterDamage));
			}
			if(player.getEntityData().getBoolean("adaptiveDamage")) {
				player.getEntityData().setBoolean("adaptiveDamage", false);
				player.getEntityData().setBoolean("absorbedDamage", afterDamage < 7);
			}
		}
		if(event.source.getEntity() != null && event.source.getEntity() instanceof EntityPlayer && event.entity instanceof EntityLivingBase) {
			EntityLivingBase entity = (EntityLivingBase)event.entity;
			EntityPlayer playerSource = (EntityPlayer)event.source.getEntity();
			ExtendedPlayer props = ExtendedPlayer.get(playerSource);
			if(entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)entity;
				double afterDamage = ISpecialArmor.ArmorProperties.ApplyArmor(player, player.inventory.armorInventory, event.source, event.ammount);
				playerSource.addChatMessage(new ChatComponentText("Amount after absorption: §5" + afterDamage));
			}
			if(props.getDamageLog()) {
				playerSource.addChatMessage(new ChatComponentText("Amount after absorption: §5" + event.ammount));
			}
		}

	}

	@SubscribeEvent
	public void onHeal(LivingHealEvent event) {
		EntityLivingBase entity = event.entityLiving;
		if(entity.getEntityData().getLong("DHMending") > System.currentTimeMillis() && entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)entity;
			for(ItemStack stack: player.inventory.armorInventory) {
				if(stack.isItemDamaged()) {
					stack.setItemDamage(stack.getItemDamage() - 1);
				}
			}
			for(ItemStack stack: player.inventory.mainInventory) {
				if(stack.isItemDamaged()) {
					stack.setItemDamage(stack.getItemDamage() - 1);
				}
			}
		}
	}

	@SubscribeEvent
	public void onSendMessage(ServerChatEvent event) {
		EntityPlayer player = event.player;
		ExtendedPlayer props = ExtendedPlayer.get(player);
		if(props.getChoice()) {
			if(event.message.contains("1")) {
				props.setChoice(false);
				player.inventory.addItemStackToInventory(new ItemStack(DHItems.elderWand));
			}
			if(event.message.contains("2")) {
				props.setChoice(false);
				player.inventory.addItemStackToInventory(new ItemStack(DHItems.resurrectionStone));
			}
			if(event.message.contains("3")) {
				props.setChoice(false);
				player.inventory.addItemStackToInventory(new ItemStack(DHItems.invisibilityMantle));
			}
			if(event.message.contains("4")) {
				double random = Math.random();
				if(random < 0.33) {
					player.inventory.addItemStackToInventory(new ItemStack(DHItems.elderWand));
				}
				else if(random < 0.66) {
					player.inventory.addItemStackToInventory(new ItemStack(DHItems.resurrectionStone));
				}
				else {
					player.inventory.addItemStackToInventory(new ItemStack(DHItems.invisibilityMantle));
				}
				props.setChoice(false);
			}
		}
		if(player.getEntityData().getBoolean("DeadlyPrism")) {
			player.getEntityData().setBoolean("DeadlyPrism", false);
			if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof DeadlyPrism) {
				DeadlyPrism prism = (DeadlyPrism)player.getHeldItem().getItem();
				if(event.message.matches("-?\\d+(\\.\\d+)?")) {
					prism.damageAmount = Float.parseFloat(event.message);
					player.addChatComponentMessage(new ChatComponentText("Damage set to: §5" + prism.damageAmount));
				}
				else {
					player.addChatComponentMessage(new ChatComponentText("It's not a number..."));
				}
			}
		}
	}

	@SubscribeEvent
	public void onClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
		NBTTagCompound compound = event.original.getEntityData();
		ExtendedPlayer.get(event.original).saveNBTData(compound);
		ExtendedPlayer.get(event.entityPlayer).loadNBTData(compound);
	}


	@SubscribeEvent
	public void onEntityConstructing(EntityEvent.EntityConstructing event) {
		if(event.entity != null) {
			if(event.entity instanceof EntityPlayer) {
				if(ExtendedPlayer.get((EntityPlayer)event.entity) == null) {
					ExtendedPlayer.register((EntityPlayer)event.entity);
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void death(LivingDeathEvent event) {
		if(event.entity instanceof EntityPlayer && !event.entity.worldObj.isRemote) {
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			if(ExtendedPlayer.get(player) != null) {
				ExtendedPlayer props = ExtendedPlayer.get(player);
				props.setCurrentDuration(0);
				props.setSource(null);
				for(Object o: getEntities(64, EntityPlayer.class, player)) {
					if(o instanceof EntityPlayer) {
						EntityPlayer playerIterator = (EntityPlayer)o;
						ExtendedPlayer props2 = ExtendedPlayer.get(playerIterator);
						if(props2.getSource() == player) {
							props2.setSource(null);
							props2.setCurrentDuration(0);
						}
					}
				}
			}
			ResurrectionStone rs = new ResurrectionStone();
			try {
				if(BaublesApi.getBaubles(player).getStackInSlot(1).getItem() != null) {
					ItemStack stack = BaublesApi.getBaubles(player).getStackInSlot(1);
					if(BaublesApi.getBaubles(player)
								 .getStackInSlot(1)
								 .getItem() == DHItems.resurrectionStone && rs.getCharges(stack) > 0) {
						rs.setCharges(stack, rs.getCharges(stack) - 1);
						event.setCanceled(true);
						player.setHealth(player.getMaxHealth());
						player.getEntityData().setInteger("invincible", 100);
					}
				}
				if(BaublesApi.getBaubles(player).getStackInSlot(2).getItem() != null) {
					ItemStack stack = BaublesApi.getBaubles(player).getStackInSlot(2);
					if(BaublesApi.getBaubles(player)
								 .getStackInSlot(1)
								 .getItem() == DHItems.resurrectionStone && rs.getCharges(stack) > 0) {
						rs.setCharges(stack, rs.getCharges(stack) - 1);
						event.setCanceled(true);
						player.setHealth(player.getMaxHealth());
						player.getEntityData().setInteger("invincible", 100);
					}
				}
			}
			catch(NullPointerException e) {
			}
			int lifes = player.getEntityData().getInteger("Horcrux");
			if(lifes > 0) {
				player.getEntityData().setInteger("Horcrux", lifes - 1);
				event.setCanceled(true);
				attributes.clear();
				attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("HorcruxBonusHp", SpellRegistry.hpCost, 0));
				player.getAttributeMap().applyAttributeModifiers(attributes);
				player.setHealth(player.getMaxHealth());
				attributes.clear();
				attributes.clear();
				if(Math.random() > 0.5 && !player.worldObj.isRemote) {
					player.worldObj.playSoundAtEntity(player, "dh:spell.death1", 1F, 1F);
				}
				else {
					player.worldObj.playSoundAtEntity(player, "dh:spell.death2", 1F, 1F);
				}
			}
		}
		if(event.entity instanceof EntityLiving) {
			if(event.source.getEntity() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)event.source.getEntity();
				ExtendedPlayer props = ExtendedPlayer.get(player);
				props.addMonster(event.entity.getCommandSenderName());
				props.setMobsKilled(props.getMobsKilled() + 1);
				Calendar currentDate = Calendar.getInstance();
				int currentMonth = currentDate.get(Calendar.MONTH);
				if((currentMonth == Calendar.OCTOBER || currentMonth == Calendar.NOVEMBER) && event.entity instanceof EntityLiving) {
					EntityLiving entityLiving = (EntityLiving)event.entity;
					if(Math.random() < 0.001 * entityLiving.getMaxHealth()) {
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
				}
			}
		}
		if(event.entity instanceof EntityGoblin) {
			if(Math.random() < 0.01) {
				event.entity.entityDropItem(new ItemStack(DHItems.hobgoblinSoul), 1);
			}
		}
	}

	@SubscribeEvent
	public void onFoodEaten(PlayerUseItemEvent.Finish event) {
		EntityPlayer player = event.entityPlayer;
		if(player.worldObj.isRemote) {
			return;
		}
		ExtendedPlayer props = ExtendedPlayer.get(player);
		if(props.getElfLvl() == 5 && event.item.getItem() instanceof ItemAppleGold && event.item.getItemDamage() > 0) {
			props.setFoodEaten(props.getFoodEaten() + 1);
		}
		if(props.getElfLvl() == 8) {
			props.addFoodToCollection(event.item.getUnlocalizedName());
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	public void lowestDeath(LivingDeathEvent event) {
		if(event.isCanceled() && event.entity != null && event.source.getEntity() != null) {
			if(event.entity instanceof EntityPlayer && event.source.getEntity() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)event.entity;
				EntityPlayer caster = (EntityPlayer)event.source.getEntity();
				ExtendedPlayer props = ExtendedPlayer.get(caster);
				com.emoniph.witchery.common.ExtendedPlayer witcheryProps = com.emoniph.witchery.common.ExtendedPlayer.get(player);
				if(props.getElfLvl() > 0 && witcheryProps.isVampire()) {
					event.setCanceled(false);
				}
			}
		}
	}

	@SubscribeEvent
	public void tooltipEvent(ItemTooltipEvent event) {
		Item item = event.itemStack.getItem();
		if(item.getUnlocalizedName().equals("tile.visconverter")) {
			if(I18n.format("dh.util.language").equals("Ru")) {
				event.toolTip.add("Если поставить на Алтарь, даст максимальную базовую энергию!");
				event.toolTip.add("Бонус: +10 к базе за каждый сантивис в ближайшем узле");
				event.toolTip.add("Если поставить на маг. верстак, будет заряжать палку 10 вис в секунду за 100 энергии");
			}
			else {
				event.toolTip.add("Place on the Altar to get maximum base energy!");
				event.toolTip.add("Bonus: +10 to base energy for each santi vis in nearest aura");
				event.toolTip.add("Place on magic workbench to get 10 vis per sec for your wand in cost of 100 energy");
			}
		}
	}

	@SubscribeEvent
	public void blockBreak(BlockEvent.BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		if(!player.capabilities.isCreativeMode && player.getHeldItem() != null && player.getHeldItem()
																						.getItem() == Witchery.Items.KOBOLDITE_PICKAXE && !event.world.isRemote && !player.worldObj.isRemote && hasDeathlyHallow(player) && (event.block.getUnlocalizedName()
																																																										.toLowerCase()
																																																										.contains("ore")
		)) {
			ItemStack stack = player.getHeldItem();
			double chance = 1;
			chance += EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
			for(Object o: player.getActivePotionEffects()) {
				PotionEffect potion = (PotionEffect)o;
				if(potion.getPotionID() == Witchery.Potions.FORTUNE.id) {
					chance *= 2;
				}
			}
			chance /= 1000;
			if(Math.random() < chance) {
				if(Math.random() > chance) {
					player.entityDropItem(Witchery.Items.GENERIC.itemKobolditeDust.createStack(), 1);
				}
				else {
					if(Math.random() > chance) {
						player.entityDropItem(Witchery.Items.GENERIC.itemKobolditeNugget.createStack(), 1);
					}
					else {
						player.entityDropItem(Witchery.Items.GENERIC.itemKobolditeIngot.createStack(), 1);
					}
				}
				event.world.setBlock(event.x, event.y, event.z, Blocks.air);
			}
            /*if(FurnaceRecipes.smelting().getSmeltingResult(new ItemStack(event.block)) != null){
                ItemStack smelt = FurnaceRecipes.smelting().getSmeltingResult(new ItemStack(event.block,2));
                EntityItem entityItem = new EntityItem(event.world, event.x, event.y, event.z, smelt);
                event.world.spawnEntityInWorld(entityItem);
            }*/
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void playerInteract(PlayerInteractEvent event) {
		if(DHIntegration.thaumcraft && event.entityPlayer.getHeldItem() != null) {
			ItemStack stack = event.entityPlayer.getHeldItem();
			if(stack.getItem().getUnlocalizedName().equals("item.witchery:ingredient") && stack.getItemDamage() == 14) {
				EntityPlayer player = event.entityPlayer;
				MovingObjectPosition rayTrace = rayTrace(player, 4.0);
				if(rayTrace != null) {
					if(rayTrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
						int blockX = rayTrace.blockX;
						int blockY = rayTrace.blockY;
						int blockZ = rayTrace.blockZ;
						TileEntity tileEntity = player.worldObj.getTileEntity(blockX, blockY, blockZ);
						if(tileEntity != null && tileEntity instanceof BlockGrassper.TileEntityGrassper) {
							BlockGrassper.TileEntityGrassper grassper = (BlockGrassper.TileEntityGrassper)tileEntity;
							ItemStack focus;
							if(((BlockGrassper.TileEntityGrassper)tileEntity).getStackInSlot(0)
																			 .getItem() == ConfigItems.itemFocusPech) {
								stack.stackSize = stack.stackSize - 1;
								focus = new ItemStack(DHItems.inferioisMutandis);
								ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, event.world, blockX, blockY, blockZ, 1.0, 1.0, 8);
								grassper.decrStackSize(0, 1);
								event.world.setBlock(blockX, blockY, blockZ, Blocks.air);
								player.inventory.addItemStackToInventory(focus);
							}
							else if(((BlockGrassper.TileEntityGrassper)tileEntity).getStackInSlot(0)
																				  .getItem() == DHItems.inferioisMutandis) {
								stack.stackSize = stack.stackSize - 1;
								focus = new ItemStack(ConfigItems.itemFocusPech);
								ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, event.world, blockX, blockY, blockZ, 1.0, 1.0, 8);
								grassper.decrStackSize(0, 1);
								event.world.setBlock(blockX, blockY, blockZ, Blocks.air);
								player.inventory.addItemStackToInventory(focus);
							}
						}
					}
				}
			}
		}
        /*EntityPlayer player = event.entityPlayer;
        ExtendedPlayer props = ExtendedPlayer.get(player);
        for(Object o: getEntities(1, EntityAnimal.class,player)){
            if(o instanceof EntityAnimal){
                EntityAnimal entity = (EntityAnimal) o;
                try{
                    Field privateField = EntityAITempt.class.getDeclaredField("field_151484_k");
                    privateField.setAccessible(true);
                    List entries = entity.tasks.taskEntries;
                    for(int i = 0;i < entries.size();i++){
                        *//*if(entries.get(i) == new EntityAITasks.EntityAITaskEntry(3,null)){

                        }*//*
                    }
                    Item item = (Item) privateField.get(entity.tasks.taskEntries.get(3));
                    System.out.println(item.getUnlocalizedName());
                    if(player.getHeldItem().getItem() == item){
                        System.out.println("srabotalo");
                        props.setMobsFed(props.getMobsFed()+1);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }*/
	}

	public boolean isHallow(ItemStack stack) {
		return stack.getItem() == DHItems.resurrectionStone || stack.getItem() == DHItems.elderWand || stack.getItem() == DHItems.invisibilityMantle;
	}


	private void removeDuplicatesFromInventory(EntityPlayer player) {
		int count = 0;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			if(player.inventory.getStackInSlot(i) != null) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if(isHallow(stack)) {
					if(!stack.hasTagCompound()) {
						NBTTagCompound nbt = new NBTTagCompound();
						nbt.setString("dhowner", player.getDisplayName());
						stack.setTagCompound(nbt);
					}
					else if(stack.hasTagCompound() && !(stack.getTagCompound().hasKey("dhowner"))) {
						NBTTagCompound nbt = stack.getTagCompound();
						nbt.setString("dhowner", player.getDisplayName());
						stack.setTagCompound(nbt);
					}
					else if(stack.hasTagCompound() && stack.getTagCompound().hasKey("dhowner")) {
						String dhowner = stack.getTagCompound().getString("dhowner");
						count++;
						if(player.getDisplayName().equals(dhowner)) {
							if(count > 1) {
								player.inventory.setInventorySlotContents(i, null);
								//player.entityDropItem(stack,1);
								ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "dh.chat.dupe");
							}
						}
						else {
							player.inventory.setInventorySlotContents(i, null);
							//player.entityDropItem(stack,1);
						}
					}
				}
			}
		}
		for(int i = 1; i < 4; i++) {
			if(BaublesApi.getBaubles(player).getStackInSlot(i) != null) {
				ItemStack stack = BaublesApi.getBaubles(player).getStackInSlot(i);
				if(isHallow(stack)) {
					if(stack.hasTagCompound() && stack.getTagCompound().hasKey("dhowner")) {
						count++;
						if(!stack.getTagCompound().getString("dhowner").equals(player.getDisplayName())) {
							//player.entityDropItem(stack,1);
							BaublesApi.getBaubles(player).setInventorySlotContents(i, null);
						}
						if(count > 1) {
							//player.entityDropItem(stack,1);
							BaublesApi.getBaubles(player).setInventorySlotContents(i, null);
							ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "dh.chat.dupe");
						}
					}
				}
			}
		}
	}

	public MovingObjectPosition rayTrace(EntityPlayer player, double distance) {
		Vec3 startVec = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		Vec3 lookVec = player.getLook(1.0F);
		Vec3 endVec = startVec.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);
		return player.worldObj.rayTraceBlocks(startVec, endVec);
	}

	public boolean hasDeathlyHallow(EntityPlayer player) {
		int count = 0;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			if(player.inventory.getStackInSlot(i) != null) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if(isHallow(stack)) {
					if(!stack.hasTagCompound()) {
						NBTTagCompound nbt = new NBTTagCompound();
						nbt.setString("dhowner", player.getDisplayName());
						stack.setTagCompound(nbt);
					}
					else if(stack.hasTagCompound() && !(stack.getTagCompound().hasKey("dhowner"))) {
						NBTTagCompound nbt = stack.getTagCompound();
						nbt.setString("dhowner", player.getDisplayName());
						stack.setTagCompound(nbt);
					}
					else if(stack.hasTagCompound() && stack.getTagCompound().hasKey("dhowner")) {
						String dhowner = stack.getTagCompound().getString("dhowner");
						count++;
					}
				}
			}
		}
		for(int i = 1; i < 4; i++) {
			if(BaublesApi.getBaubles(player).getStackInSlot(i) != null) {
				ItemStack stack = BaublesApi.getBaubles(player).getStackInSlot(i);
				if(isHallow(stack)) {
					if(stack.hasTagCompound() && stack.getTagCompound().hasKey("dhowner")) {
						count++;
					}
				}
			}
		}
		return count > 0;
	}

	public short totalLvl(EntityPlayer player) {
		short totalLvl = 0;
		InventoryPlayer inventory = player.inventory;
		for(int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);

			if(stack != null && stack.isItemEnchanted()) {
				NBTTagList enchantments = stack.getEnchantmentTagList();

				for(int j = 0; j < enchantments.tagCount(); j++) {
					NBTTagCompound enchantment = enchantments.getCompoundTagAt(j);
					short lvl = enchantment.getShort("lvl");
					totalLvl += lvl;
				}
			}
		}
		return totalLvl;
	}

	public boolean hasAmountOfPotions(EntityPlayer player, int amount, boolean bad) {
		Collection potions = player.getActivePotionEffects();
		int count = 0;
		for(Object o: potions) {
			PotionEffect effect = (PotionEffect)o;
			if(Potion.potionTypes[effect.getPotionID()].isBadEffect() && bad) {
				count++;
			}
			else if(!Potion.potionTypes[effect.getPotionID()].isBadEffect() && !bad) {
				count++;
			}
		}
		return count >= amount;
	}


	private int findItemStackSlot(IInventory inventory, ItemStack stack) {
		for(int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack existingStack = inventory.getStackInSlot(i);
			if(ItemStack.areItemStacksEqual(stack, existingStack)) {
				return i;
			}
		}
		return -1;
	}

	public List getEntities(double radius, Class target, EntityPlayer player) {
		List entities = player.worldObj.getEntitiesWithinAABB(target, player.boundingBox.expand(radius, radius, radius));
		return entities;
	}
}
