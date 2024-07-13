package com.pyding.deathlyhallows.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.Random;

public class GastronomicTemptation extends Item {
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String coloredName;
		String currentLanguage = StatCollector.translateToLocal("language.name");

		if(currentLanguage.equals("Русский")) {
			coloredName = "Гастрономическое Искушение";
		}
		else {
			coloredName = "Gastronomic Temptation";
		}
		return getRainbowString(coloredName);
	}


	private String getRainbowString(String text) {
		StringBuilder coloredText = new StringBuilder();

		Random random = new Random();
		EnumChatFormatting[] colors = EnumChatFormatting.values();

		for(char letter: text.toCharArray()) {
			EnumChatFormatting color = colors[random.nextInt(colors.length - 1) + 1];
			coloredText.append(color).append(letter);
		}

		return coloredText.toString();
	}
}
