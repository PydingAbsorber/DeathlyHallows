package com.pyding.deathlyhallows.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.Random;

public class ItemGastronomicTemptation extends ItemBase {


	public ItemGastronomicTemptation() {
		super("gastronomicTemptation", 64);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return getRainbowString(StatCollector.translateToLocal(super.getItemStackDisplayName(stack)));
	}

	private String getRainbowString(String text) {
		StringBuilder coloredText = new StringBuilder();
		EnumChatFormatting[] colors = EnumChatFormatting.values();
		for(char letter: text.toCharArray()) {
			EnumChatFormatting color = colors[new Random(System.currentTimeMillis() / 2000).nextInt(colors.length - 6) + 1];
			coloredText.append(color).append(letter);
		}
		return coloredText.toString();
	}
	
}
