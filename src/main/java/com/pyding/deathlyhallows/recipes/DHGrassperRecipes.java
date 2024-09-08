package com.pyding.deathlyhallows.recipes;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.wands.ItemWandCap;
import net.minecraft.item.ItemStack;
import thaumcraft.common.config.ConfigItems;

import java.util.HashMap;
import java.util.Map;

public final class DHGrassperRecipes {
	
	private static final Map<String, ItemStack> grassperRecipes = new HashMap<>();
	
	private DHGrassperRecipes() {
		
	}
	
	public static void init() {
		if(DHIntegration.thaumcraft) {
			addRecipe(Witchery.Items.GENERIC.itemMutandis.createStack(), new ItemStack(ConfigItems.itemFocusPech), new ItemStack(DHItems.inferioisMutandis));
			addRecipe(Witchery.Items.GENERIC.itemMutandis.createStack(), new ItemStack(DHItems.inferioisMutandis), new ItemStack(ConfigItems.itemFocusPech));
			addRecipe(Witchery.Items.GENERIC.itemFancifulThread.createStack(), new ItemStack(ConfigItems.itemWandCap), new ItemStack(DHItems.wandCap, 1, ItemWandCap.Caps.cotton.ordinal()));
		}
	}
	
	public static void addRecipe(ItemStack playerItem, ItemStack grassperItem, ItemStack resultItem) {
		grassperRecipes.put(getRecipeKey(playerItem, grassperItem), resultItem);
	}

	public static ItemStack getResult(ItemStack playerItem, ItemStack grassperItem) {
		ItemStack result = grassperRecipes.get(getRecipeKey(playerItem, grassperItem));
		return result == null ? null : result.copy(); // should always copy to prevent recipe output changes, but can't copy on null
	}
	
	private static String getRecipeKey(ItemStack playerItem, ItemStack grassperItem) {
		return "p:" + stackToKey(playerItem) + ";g:" + stackToKey(grassperItem);
	}
	
	private static String stackToKey(ItemStack stack) {
		return stack == null ? null : (stack.getItem().getUnlocalizedName(stack) + ":" + stack.getItemDamage());
	}
	
}
