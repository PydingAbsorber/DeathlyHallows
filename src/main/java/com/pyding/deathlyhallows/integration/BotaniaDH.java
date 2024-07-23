package com.pyding.deathlyhallows.integration;

import com.emoniph.witchery.Witchery;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lexicon.BLexiconEntry;
import vazkii.botania.common.lexicon.page.PageText;

import static com.pyding.deathlyhallows.items.ModItems.deathShard;
import static com.pyding.deathlyhallows.items.ModItems.hobgoblinSoul;

public class BotaniaDH {
	public static void lexus() {
		KnowledgeType lexusTab = new KnowledgeType("deathlyHallows", EnumChatFormatting.RED, true);
		RecipeRuneAltar screamilyRecipe = BotaniaAPI.registerRuneAltarRecipe(ItemBlockSpecialFlower.ofType("screamily"), 1000000, new ItemStack(hobgoblinSoul), new ItemStack(ModItems.rune, 1, 7), new ItemStack(ModItems.rune, 1, 11), new ItemStack(ModItems.rune, 1, 15), new ItemStack(ModItems.manaResource, 1, 14), new ItemStack(ModItems.cacophonium), new ItemStack(ModItems.cosmetic, 1, 11), new ItemStack(Witchery.Items.EARMUFFS), Witchery.Items.GENERIC.itemCondensedFear.createStack(), Witchery.Items.GENERIC.itemSpectralDust.createStack(), Witchery.Items.GENERIC.itemNecroStone.createStack(), new ItemStack(Witchery.Items.SPECTRAL_STONE, 1, 18));
		LexiconEntry screamilyLexus = new BLexiconEntry("screamily", BotaniaAPI.categoryGenerationFlowers);
		screamilyLexus.setKnowledgeType(lexusTab).setLexiconPages(new PageText("abobka"),
				BotaniaAPI.internalHandler.petalRecipePage(".altarCraft", screamilyRecipe));
		RecipeRuneAltar spawnlesiaRecipe = BotaniaAPI.registerRuneAltarRecipe(ItemBlockSpecialFlower.ofType("spawnlesia"), 100000000, new ItemStack(deathShard), new ItemStack(ModItems.rune, 1, 9), new ItemStack(ModItems.blackHoleTalisman), new ItemStack(ModItems.rune, 1, 15), new ItemStack(ModItems.tinyPlanet), ItemBlockSpecialFlower.ofType("tigerseye"), ItemBlockSpecialFlower.ofType("pollidisiac"), new ItemStack(ModItems.spawnerMover), Witchery.Items.GENERIC.itemKobolditePentacle.createStack(), new ItemStack(Blocks.dragon_egg));
		LexiconEntry spawnlesiawLexus = new BLexiconEntry("spawnlesia", BotaniaAPI.categoryFunctionalFlowers);
		spawnlesiawLexus.setKnowledgeType(lexusTab).setLexiconPages(new PageText("abobka"),
				BotaniaAPI.internalHandler.petalRecipePage(".altarCraft", spawnlesiaRecipe));
	}
}
