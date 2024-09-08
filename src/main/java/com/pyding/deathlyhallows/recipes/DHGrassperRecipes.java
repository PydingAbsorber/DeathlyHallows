package com.pyding.deathlyhallows.recipes;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.wands.ItemWandCap;
import com.pyding.deathlyhallows.items.wands.ItemWandRod;
import net.minecraft.item.ItemStack;
import thaumcraft.common.config.ConfigItems;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class DHGrassperRecipes {
	
	private static final Map<String, ItemStack> grassperRecipes = new HashMap<>();

	private static final Map<String, ItemStack[]> grassperRecipeIngredients = new HashMap<>();
	
	private DHGrassperRecipes() {
		
	}
	
	public static void init() {
		if(DHIntegration.thaumcraft) {
			ItemStack mutandis = Witchery.Items.GENERIC.itemMutandis.createStack();
			addRecipe(new ItemStack(ConfigItems.itemFocusPech), mutandis, new ItemStack(DHItems.inferioisMutandis));
			addRecipe(new ItemStack(DHItems.inferioisMutandis), mutandis, new ItemStack(ConfigItems.itemFocusPech));
			addRecipe(new ItemStack(ConfigItems.itemWandCap), Witchery.Items.GENERIC.itemFancifulThread.createStack(), new ItemStack(DHItems.wandCap, 1, ItemWandCap.Caps.cotton.ordinal()));
			addRecipe(new ItemStack(Witchery.Blocks.SAPLING, 1, 0), Witchery.Items.GENERIC.itemGraveyardDust.createStack(), new ItemStack(DHItems.wandRod, 1, ItemWandRod.Rods.rowan.ordinal()));
			addRecipe(new ItemStack(Witchery.Blocks.SAPLING, 1, 1), Witchery.Items.GENERIC.itemRefinedEvil.createStack(), new ItemStack(DHItems.wandRod, 1, ItemWandRod.Rods.alder.ordinal()));
			addRecipe(new ItemStack(Witchery.Blocks.SAPLING, 1, 2), Witchery.Items.GENERIC.itemNullCatalyst.createStack(), new ItemStack(DHItems.wandRod, 1, ItemWandRod.Rods.hawthorn.ordinal()));
		}
	}
	
	public static void addRecipe(ItemStack input, ItemStack catalyst, ItemStack resultItem) {
		String key = getRecipeKey(input, catalyst);
		grassperRecipes.put(key, resultItem);
		grassperRecipeIngredients.put(key, new ItemStack[]{input, catalyst});
	}
	
	public static ItemStack[] getInputsFromKey(String key) {
		return grassperRecipeIngredients.get(key);
	}

	public static ItemStack getResult(ItemStack input, ItemStack catalyst) {
		return getResult(getRecipeKey(input, catalyst));
	}

	public static ItemStack getResult(String key) {
		ItemStack result = grassperRecipes.get(key);
		return result == null ? null : result.copy(); // should always copy to prevent recipe output changes, but can't copy on null
	}
	
	public static Collection<String> recipeKeys() {
		return grassperRecipes.keySet();
	}
	
	public static String getRecipeKey(ItemStack input, ItemStack catalyst) {
		return "i:" + stackToKey(input) + ";c:" + stackToKey(catalyst);
	}
	
	public static String stackToKey(ItemStack stack) {
		return stack == null ? null : (stack.getItem().getUnlocalizedName(stack) + ":" + stack.getItemDamage());
	}
	
}
