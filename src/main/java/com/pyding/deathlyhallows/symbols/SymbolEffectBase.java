package com.pyding.deathlyhallows.symbols;

import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.util.TimeUtil;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.core.DHHooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class SymbolEffectBase extends SymbolEffect {
	
	private final ElderSymbolTraits[] traits;
	private final int cooldown;
	private final String nameKey;
	
	public SymbolEffectBase(int id, String name, int cost, boolean isCurse, boolean isGravity, String knowledgeKey, int cooldown, boolean isVisible, ElderSymbolTraits... traits) {
		super(id, "witchery.pott." + DeathlyHallows.MODID + "." + name, cost, isCurse, isGravity, knowledgeKey, cooldown, isVisible);
		this.traits = traits;
		this.cooldown = cooldown;
		this.nameKey = name;
	}

	public ElderSymbolTraits[] getTraits() {
		return traits;
	}

	@Override
	public abstract void perform(World world, EntityPlayer p, int level);
	
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
		tagSpells.setLong(nameKey + "LastUse", TimeUtil.getServerTimeInTicks());
	}
	
	@Override
	public long cooldownRemaining(EntityPlayer p, NBTTagCompound tag) {
		if(cooldown <= 0 || !tag.hasKey("WITCSpellBook")) {
			return 0L;
		}
		return DHHooks.witcherySymbolCooldownRemaining(
				tag.getCompoundTag("WITCSpellBook").getLong(nameKey + "LastUse"),
				TimeUtil.getServerTimeInTicks(),
				cooldown,
				this,
				p,
				tag
		);
	}
	
}
