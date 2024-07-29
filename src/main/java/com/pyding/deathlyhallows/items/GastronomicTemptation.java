package com.pyding.deathlyhallows.items;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.Random;

public class GastronomicTemptation extends Item {
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return getRainbowString(I18n.format("dh.desc.gastro"));
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
