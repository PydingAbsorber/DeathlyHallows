package com.pyding.deathlyhallows.utils;

import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.CreatureUtil;
import com.pyding.deathlyhallows.utils.properties.ExtendedPlayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collection;

import static com.emoniph.witchery.infusion.Infusion.getNBT;
import static com.pyding.deathlyhallows.utils.properties.ExtendedPlayer.get;

public final class ElfUtils {

	private ElfUtils() {

	}

	public static int getSecondsSurvived(EntityPlayer p) {
		return get(p).elfTimeSurvived / 20;
	}

	public static int getElfLevel(EntityPlayer p) {
		return getElfLevel(get(p));
	}

	public static int getElfLevel(ExtendedPlayer props) {
		return props.getElfLvl();
	}

	public static boolean isElf(EntityLivingBase living) {
		// TODO Either disable Alfheim or make it compatible
		if(living instanceof EntityPlayer) {
			return getElfLevel((EntityPlayer)living) > 0;
		}
		return false;
	}

	public static boolean isVampOrWolf(EntityLivingBase living) {
		return CreatureUtil.isVampire(living) || CreatureUtil.isWerewolf(living, true);
	}

	private static void demystify(EntityPlayer p) {
		ChatUtil.sendTranslated(EnumChatFormatting.RED, p, "dh.chat.no");
		if(CreatureUtil.isVampire(p)) {
			com.emoniph.witchery.common.ExtendedPlayer.get(p).setVampireLevel(0);
		}
		if(CreatureUtil.isWerewolf(p, true)) {
			com.emoniph.witchery.common.ExtendedPlayer.get(p).setWerewolfLevel(0);
		}
	}

	public static void manageElf(EntityPlayer p) {
		int elfLevel = getElfLevel(p);
		if(!isElf(p)) {
			checkAndStartElfQuest(p);
			return;
		}
		if(isVampOrWolf(p)) {
			demystify(p);
			return;
		}
		regenInfusion(p, elfLevel);
		checkAndLevelUpElf(p, elfLevel);
		NBTTagCompound witcheryTag = getNBT(p);
		if(elfLevel == 10 && Infusion.getInfusionID(p) > 0) {
			witcheryTag.setInteger("witcheryInfusionChargesMax", 2 * Infusion.getMaxEnergy(p));
		}
	}

	private static void regenInfusion(EntityPlayer p, int elfLevel) {
		ExtendedPlayer props = get(p);
		if(Infusion.getInfusionID(p) > 0 && p.ticksExisted - props.elfInfusionTimer > 50 && Infusion.getCurrentEnergy(p) < Infusion.getMaxEnergy(p)) { // no sex damn you pervert.
			props.elfInfusionTimer = p.ticksExisted;
			Infusion.setCurrentEnergy(p, Math.min(Infusion.getMaxEnergy(p), Infusion.getCurrentEnergy(p) + elfLevel));
		}
		if(Infusion.getMaxEnergy(p) < Infusion.getCurrentEnergy(p)) {
			Infusion.setCurrentEnergy(p, Infusion.getMaxEnergy(p));
		}
	}

	private static void checkAndStartElfQuest(EntityPlayer p) {
		ExtendedPlayer props = get(p);
		if(Infusion.getCurrentEnergy(p) == 0 && props.getTrigger() == 0 && Infusion.getInfusionID(p) != 0) {
			props.setTrigger(1);
		}
		if(Infusion.getCurrentEnergy(p) == Infusion.getMaxEnergy(p) && props.getTrigger() == 1) {
			props.setTrigger(0);
			props.setElfCount(props.getElfCount() + 1);
			if(props.getElfCount() == 1) {
				ChatUtil.sendTranslated(EnumChatFormatting.BLUE, p, "dh.chat.elf1");
			}
		}
		if(props.getElfCount() >= 20 && !isVampOrWolf(p)) {
			props.setElfCount(0);
			props.increaseElfLvl();
			ChatUtil.sendTranslated(EnumChatFormatting.BLUE, p, "dh.chat.elf2");
		}
	}

	private static void checkAndLevelUpElf(EntityPlayer p, int elfLevel) {
		ExtendedPlayer props = get(p);
		switch(elfLevel) {
			case 1: {
				if(p.experienceLevel >= DHConfig.getElfRequirements(2)) {
					props.increaseElfLvl();
					messageChatLevelUp(p);
				}
				break;
			}
			case 2: {
				if(p.posY <= DHConfig.getElfRequirements(3)) {
					props.increaseElfLvl();
					messageChatLevelUp(p);
				}
				break;
			}
			case 3: {
				if(getTotalEnchantmentLevels(p) >= DHConfig.getElfRequirements(4)) {
					props.increaseElfLvl();
					messageChatLevelUp(p);
				}
				break;
			}
			case 4: {
				if(props.getMobsKilled() >= DHConfig.getElfRequirements(5)) {
					props.increaseElfLvl();
					messageChatLevelUp(p);
				}
				break;
			}
			case 5: {
				if(props.getFoodEaten() >= DHConfig.getElfRequirements(6)) {
					props.increaseElfLvl();
					messageChatLevelUp(p);
				}
				break;
			}
			case 6: {
				if(hasAmountOfPotions(p, 10, true) && ++props.elfTimeSurvived > DHConfig.getElfRequirements(7)) {
					props.elfTimeSurvived = 0;
					props.increaseElfLvl();
					messageChatLevelUp(p);
				}
				break;
			}
			case 7: {
				if(WorldProviderDreamWorld.getPlayerIsSpiritWalking(p) && ++props.elfTimeSurvived > DHConfig.getElfRequirements(8)) {
					props.elfTimeSurvived = 0;
					props.increaseElfLvl();
					messageChatLevelUp(p);
				}
				break;
			}
			case 8: {
				if(props.getFoodCollection() != null && props.getFoodCollection().size() > DHConfig.getElfRequirements(9)) {
					props.increaseElfLvl();
					messageChatLevelUp(p);
				}
				break;
			}
			case 9: {
				if(props.getSpellsUsed() >= DHConfig.getElfRequirements(10)) {
					props.setAllNull();
					props.increaseElfLvl();
					messageChatLevelUp(p);
				}
				break;
			}
		}
	}

	private static void messageChatLevelUp(EntityPlayer p) {
		ChatUtil.sendTranslated(EnumChatFormatting.BLUE, p, "dh.chat.elflvlup");
	}

	public static int getTotalEnchantmentLevels(EntityPlayer player) {
		int total = 0;
		InventoryPlayer inventory = player.inventory;
		for(int i = 0; i < inventory.getSizeInventory(); ++i) {
			ItemStack stack = inventory.getStackInSlot(i);
			if(stack == null || !stack.isItemEnchanted() || stack.getItem() == Items.enchanted_book) {
				continue;
			}
			@SuppressWarnings("unchecked")
			Collection<Integer> levels = EnchantmentHelper.getEnchantments(stack).values();
			for(Integer level: levels) {
				total += level;
			}
		}
		return total;
	}

	private static boolean hasAmountOfPotions(EntityPlayer player, int amount, boolean bad) {
		@SuppressWarnings("unchecked")
		Collection<PotionEffect> potions = player.getActivePotionEffects();
		int count = 0;
		for(PotionEffect effect: potions) {
			if(Potion.potionTypes[effect.getPotionID()].isBadEffect() == bad) {
				count++;
			}
		}
		return count >= amount;
	}

}
