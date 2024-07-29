package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.utils.properties.ExtendedPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.List;

public class CreativeItem extends Item {
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(!player.worldObj.isRemote) {
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
			if(I18n.format("dh.util.language").equals("Ru")) {
				player.addChatComponentMessage(new ChatComponentText("§9Твой уровень Эльфа сейчас " + props.getElfLvl()));
			}
			else {
				player.addChatComponentMessage(new ChatComponentText("§9Your Elf lvl is now " + props.getElfLvl()));
			}
		}
		return super.onItemRightClick(stack, world, player);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
		ExtendedPlayer props = ExtendedPlayer.get(player);
		if(player != null) {
			//list.add("You have§9 " + props.getElfLvl() + " Elf Lvl");
		}
	}
}
