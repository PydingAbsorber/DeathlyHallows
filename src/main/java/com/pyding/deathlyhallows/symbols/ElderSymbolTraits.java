package com.pyding.deathlyhallows.symbols;

import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.utils.ElfUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import static net.minecraft.util.EnumChatFormatting.RED;

public abstract class ElderSymbolTraits {

	private final String failMessage;
	private final EnumChatFormatting messageColor;

	ElderSymbolTraits(EnumChatFormatting messageColor, String failMessage) {
		this.failMessage = failMessage;
		this.messageColor = messageColor;
	}

	public abstract boolean check(SymbolEffect effect, World world, EntityPlayer p, int level);

	public void onFail(EntityPlayer p) {
		ChatUtil.sendTranslated(messageColor, p, failMessage);
		SoundEffect.NOTE_SNARE.playAtPlayer(p.worldObj, p);
	}

	public static ElderSymbolTraits INFUSION(int infusionID) {
		return new ElderSymbolTraits(RED, "witchery.infuse.branch.infernalrequired") {
			@Override
			public boolean check(SymbolEffect effect, World world, EntityPlayer p, int level) {
				return Infusion.getInfusionID(p) == infusionID;
			}
		};
	}

	public static ElderSymbolTraits ELF(int elfLevel) {
		return new ElderSymbolTraits(RED, "dh.chat.elfRequired") {
			@Override
			public boolean check(SymbolEffect effect, World world, EntityPlayer p, int level) {
				return ElfUtils.getElfLevel(p) >= elfLevel;
			}
		};
	}

	public static final ElderSymbolTraits ELDER =
			new ElderSymbolTraits(RED, "dh.chat.elderWandRequired") {
				@Override
				public boolean check(SymbolEffect effect, World world, EntityPlayer p, int level) {
					return p.getHeldItem() != null && p.getHeldItem().getItem() == DHItems.elderWand;
				}
			};


}
