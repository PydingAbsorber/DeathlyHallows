package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.utils.ElfUtils;
import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemElfToken extends ItemBase {

	public ItemElfToken() {
		super("elfToken", 1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		if(p.worldObj.isRemote) {
			return super.onItemRightClick(stack, world, p);
		}
		ElfUtils.resetQuestData(p);
		int level = ElfUtils.getElfLevel(p);
		if(!p.isSneaking()) {
			if(level < 10) {
				ElfUtils.setElfLevel(p, level + 1);
			}
			else {
				ElfUtils.setElfLevel(p, 0);
			}
			ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, p, 1.0, 2.0, 8);
		}
		else {
			if(level > 0) {
				ElfUtils.setElfLevel(p, level - 1);
			}
			else {
				ElfUtils.setElfLevel(p, DeathlyProperties.MAX_ELF_LEVEL);
			}
			ParticleEffect.INSTANT_SPELL.send(SoundEffect.NOTE_PLING, p, 1.0, 2.0, 8);
		}
		ChatUtil.sendTranslated(EnumChatFormatting.BLUE, p, "dh.chat.elfToken", ElfUtils.getElfLevel(p));
		return super.onItemRightClick(stack, world, p);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		l.add(StatCollector.translateToLocalFormatted("dh.desc.elfToken", ElfUtils.getElfLevel(p)));
	}

}
