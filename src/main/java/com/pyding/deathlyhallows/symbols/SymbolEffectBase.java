package com.pyding.deathlyhallows.symbols;

import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.utils.ElfUtils;
import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class SymbolEffectBase extends SymbolEffect {
	
	private final int infusionID;
	private final boolean elfOnly;
	private final int cooldown;
	
	public SymbolEffectBase(int id, String name, int cost, boolean isCurse, boolean isGravity, String knowledgeKey, int cooldown, boolean isVisible, int insusionID, boolean elfOnly) {
		super(id, "witchery.pott." + DeathlyHallows.MODID + "." + name, cost, isCurse, isGravity, knowledgeKey, cooldown, isVisible);
		this.infusionID = insusionID;
		this.elfOnly = elfOnly;
		this.cooldown = cooldown;
	}

	@Override
	public final void perform(World world, EntityPlayer p, int level) {
		if(infusionID != -1 && Infusion.getInfusionID(p) != infusionID) {
			ChatUtil.sendTranslated(EnumChatFormatting.RED, p, "witchery.infuse.branch.infernalrequired");
			SoundEffect.NOTE_SNARE.playAtPlayer(world, p);
			returnInfusion(world, p, level);
			return;
		}
		if(elfOnly && !ElfUtils.isElf(p)) {
			ChatUtil.sendTranslated(EnumChatFormatting.RED, p, "dh.chat.elfRequired");
			SoundEffect.NOTE_SNARE.playAtPlayer(world, p);
			returnInfusion(world, p, level);
			return;
		}
		// TODO coremod
		DeathlyProperties props1 = DeathlyProperties.get(p);
		props1.addSpell(this.getEffectID());
		doPerform(world, p, level + MathHelper.ceiling_float_int(ElfUtils.getElfLevel(p) / 3F));
	}

	private void returnInfusion(World world, EntityPlayer p, int level) {
		Infusion.setCurrentEnergy(p, Infusion.getCurrentEnergy(p) + getChargeCost(world, p, level));
	}

	protected abstract void doPerform(World world, EntityPlayer p, int level);

	@Override
	public void setOnCooldown(EntityPlayer player) {
		if(cooldown <= 0 || player.capabilities.isCreativeMode) {
			return;
		}
		NBTTagCompound tag = Infusion.getNBT(player);
		if(tag == null) {
			return;
		}
		if(!tag.hasKey("WITCSpellBook")) {
			tag.setTag("WITCSpellBook", new NBTTagCompound());
		}
		NBTTagCompound tagSpells = tag.getCompoundTag("WITCSpellBook");
		tagSpells.setLong(getEffectID() + "_LastUse", TimeUtil.getServerTimeInTicks());
	}

	// TODO coremod
	@Override
	public long cooldownRemaining(EntityPlayer p, NBTTagCompound tag) {
		if(cooldown <= 0 || !tag.hasKey("WITCSpellBook")) {
			return 0L;
		}
		int cooldown = MathHelper.floor_float(this.cooldown * (1F - ElfUtils.getElfLevel(p) / 20F));
		NBTTagCompound nbtSpells = tag.getCompoundTag("WITCSpellBook");
		long timePassed = TimeUtil.getServerTimeInTicks() - nbtSpells.getLong(getEffectID() + "_LastUse");
		if (timePassed < cooldown) {
			return cooldown - timePassed;
		}
		return 0L;
	}
	// TODO coremod
	@Override
	public int getChargeCost(World world, EntityPlayer p, int level) {
		return MathHelper.floor_float(super.getChargeCost(world, p, level) * (1F - ElfUtils.getElfLevel(p) / 20F));
	}
	
}
