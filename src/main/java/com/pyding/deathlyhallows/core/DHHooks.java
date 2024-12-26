package com.pyding.deathlyhallows.core;

import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.pyding.deathlyhallows.symbols.ElderSymbolTraits;
import com.pyding.deathlyhallows.symbols.SymbolEffectBase;
import com.pyding.deathlyhallows.utils.BookHelper;
import com.pyding.deathlyhallows.utils.ElfUtils;
import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unused")
public final class DHHooks {

	private DHHooks() {

	}

	public static boolean witcheryBranchCanPerform(SymbolEffect effect, World world, EntityPlayer p, int level) {
		// cancel spell if... trait check fails
		if(effect instanceof SymbolEffectBase) {
			for(ElderSymbolTraits trait: ((SymbolEffectBase)effect).getTraits()) {
				if(trait.check(effect, world, p, level)) {
					continue;
				}
				trait.onFail(p);
				return false;
			}
		}
		return true;
	}

	public static int witcheryBranchPerformLevel(int level, SymbolEffect effect, World world, EntityPlayer p) {
		// put spell in elf quest list
		DeathlyProperties props = DeathlyProperties.get(p);
		props.addSpell(effect.getEffectID());
		// and increase effect level
		return level + MathHelper.ceiling_float_int(ElfUtils.getElfLevel(p) / 3F);
	}

	public static int witcherySymbolGetChargeCost(int cost, SymbolEffect effect, World world, EntityPlayer p, int level) {
		int elfLevel = ElfUtils.getElfLevel(p);
		if(elfLevel > 0) {
			return MathHelper.floor_float(cost * (1F - elfLevel / 20F));
		}
		return cost;
	}

	public static boolean witcherySymbolCooldownOverride(SymbolEffect effect, EntityPlayer p, NBTTagCompound tag) {
		return ElfUtils.isElf(p);
	}

	public static long witcherySymbolCooldownRemaining(long lastUse, long now, int cooldown, SymbolEffect effect, EntityPlayer p, NBTTagCompound tag) {
		// elf cooldown
		if(ElfUtils.isElf(p)) { // while elf is the only way to cut cooldowns, if isn't necessary
			cooldown = MathHelper.floor_float(cooldown * (1F - ElfUtils.getElfLevel(p) / 20F));
		}
		return Math.max(0L, cooldown + lastUse - now);
	}

	public static int witcheryInfuse(Infusion infusion, EntityPlayer p, int charges) {
		if(p.worldObj.isRemote) {
			return charges;
		}
		if(ElfUtils.getElfLevel(p) == 10) {
			return charges * 2;
		}
		return charges;
	}

	public static String witcheryFormatMarkdownBook(String text, EntityPlayer p) {
		if(text == null || text.isEmpty()) {
			return text;
		}
		for(Map.Entry<String, Function<EntityPlayer, String>> entry: BookHelper.bookFormatter.entrySet()) {
			String key = "&" + entry.getKey();
			if(text.contains(key)) {
				text = text.replace(key, entry.getValue().apply(p));
			}
		}
		return text;
	}

}
