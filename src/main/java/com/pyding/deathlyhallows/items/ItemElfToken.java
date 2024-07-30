package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.utils.ElfUtils;
import com.pyding.deathlyhallows.utils.properties.ExtendedPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemElfToken extends ItemBase {

	public ItemElfToken() {
		super("elfToken", 1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(player.worldObj.isRemote) {
			return super.onItemRightClick(stack, world, player);
		}
		ExtendedPlayer props = ExtendedPlayer.get(player);
		props.setAllNull();
		if(!player.isSneaking()) {
			if(props.getElfLvl() < 10) {
				props.increaseElfLvl();
			}
			else {
				props.nullifyElfLvl();
			}
			ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, player, 1.0, 2.0, 8);
		}
		else {
			if(props.getElfLvl() > 0) {
				props.decreaseElfLvl();
			}
			else {
				props.maxElfLvl();
			}
			ParticleEffect.INSTANT_SPELL.send(SoundEffect.NOTE_PLING, player, 1.0, 2.0, 8);
		}
		player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("dh.chat.elfToken", props.getElfLvl())));
		return super.onItemRightClick(stack, world, player);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		l.add(StatCollector.translateToLocalFormatted("dh.desc.elfToken", ElfUtils.getElfLevel(p)));
	}
	
}
