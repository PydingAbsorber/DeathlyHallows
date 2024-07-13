package com.pyding.deathlyhallows.common;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.api.subtile.signature.PassiveFlower;
import vazkii.botania.api.subtile.signature.SubTileSignature;
import vazkii.botania.common.block.BlockSpecialFlower;

import java.util.List;

public class DHSubTileSignature extends SubTileSignature {
	String name;
	IIcon icon;

	public DHSubTileSignature(String name) {
		this.name = name;
	}

	public void registerIcons(IIconRegister reg) {
		this.icon = reg.registerIcon("dh:flower_" + this.name);
		BlockSpecialFlower.icons.put(name, icon);
	}

	public IIcon getIconForStack(ItemStack item) {
		return this.icon;
	}

	public String getUnlocalizedNameForStack(ItemStack item) {
		return unlocalizedName("");
	}

	private String unlocalizedName(String end) {
		return "dh.flower." + this.name + end;
	}

	public String getUnlocalizedLoreTextForStack(ItemStack stack) {
		return unlocalizedName(".reference");
	}

	public String getType() {
		Class<? extends SubTileEntity> clazz = BotaniaAPI.getSubTileMapping(this.name);
		if(clazz == null) {
			return "uwotm8";
		}
		if(clazz.getAnnotation(PassiveFlower.class) != null) {
			return "botania.flowerType.passiveGenerating";
		}
		if(SubTileGenerating.class.isAssignableFrom(clazz)) {
			return "botania.flowerType.generating";
		}
		if(SubTileFunctional.class.isAssignableFrom(clazz)) {
			return "botania.flowerType.functional";
		}
		return "botania.flowerType.misc";
	}

	public void addTooltip(ItemStack stack, EntityPlayer player, List<String> tooltip) {
		tooltip.add(EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal(getType()));
        /*if (I18n.format("dh.util.language").equals("Ru")){
            tooltip.add(EnumChatFormatting.RED + "Кричи за ману! Кто же кричит больше всех?...");
        } else {
            tooltip.add(EnumChatFormatting.RED + "Scream for mana! Who's screaming the most?...");
        }*/
	}
}
