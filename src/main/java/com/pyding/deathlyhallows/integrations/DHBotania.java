package com.pyding.deathlyhallows.integrations;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.blocks.flowers.Screamily;
import com.pyding.deathlyhallows.blocks.flowers.Spawnlesia;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lexicon.BLexiconEntry;
import vazkii.botania.common.lexicon.page.PageText;

import static com.pyding.deathlyhallows.items.DHItems.deathShard;
import static com.pyding.deathlyhallows.items.DHItems.hobgoblinSoul;

public final class DHBotania {

	public static RecipeRuneAltar
			screamilyRecipe,
			spawnlesiaRecipe;

	public static final KnowledgeType lexusTab = new KnowledgeType("deathlyHallows", EnumChatFormatting.RED, true);

	public static LexiconEntry
			screamilyLexus,
			spawnlesiawLexus;

	private DHBotania() {

	}

	public static void init() {
		petalApothecaryRecipes();
		lexicon();
	}

	private static void petalApothecaryRecipes() {
		screamilyRecipe = runeAltarFlowerRecipe(
				Screamily.NAME,
				1000000,
				new ItemStack(hobgoblinSoul),
				new ItemStack(ModItems.rune, 1, 7),
				new ItemStack(ModItems.rune, 1, 11),
				new ItemStack(ModItems.rune, 1, 15),
				new ItemStack(ModItems.manaResource, 1, 14),
				new ItemStack(ModItems.cacophonium),
				new ItemStack(ModItems.cosmetic, 1, 11),
				new ItemStack(Witchery.Items.EARMUFFS),
				Witchery.Items.GENERIC.itemCondensedFear.createStack(),
				Witchery.Items.GENERIC.itemSpectralDust.createStack(),
				Witchery.Items.GENERIC.itemNecroStone.createStack(),
				new ItemStack(Witchery.Items.SPECTRAL_STONE, 1, 18)
		);
		spawnlesiaRecipe = runeAltarFlowerRecipe(
				Spawnlesia.NAME,
				100000000,
				new ItemStack(deathShard),
				new ItemStack(ModItems.rune, 1, 9),
				new ItemStack(ModItems.blackHoleTalisman),
				new ItemStack(ModItems.rune, 1, 15),
				new ItemStack(ModItems.tinyPlanet),
				ItemBlockSpecialFlower.ofType("tigerseye"),
				ItemBlockSpecialFlower.ofType("pollidisiac"),
				new ItemStack(ModItems.spawnerMover),
				Witchery.Items.GENERIC.itemKobolditePentacle.createStack(),
				new ItemStack(Blocks.dragon_egg)
		);
	}

	private static RecipeRuneAltar runeAltarFlowerRecipe(String flowerName, int manaCost, Object... recipe) {
		return BotaniaAPI.registerRuneAltarRecipe(ItemBlockSpecialFlower.ofType(flowerName), manaCost, recipe);
	}

	private static void lexicon() {
		screamilyLexus = new DHLexiconEntry("screamily", BotaniaAPI.categoryGenerationFlowers)
				.setLexiconPages(
						new PageText(".page1"),
						petalRecipe(screamilyRecipe)
				);

		spawnlesiawLexus = new DHLexiconEntry("spawnlesia", BotaniaAPI.categoryFunctionalFlowers)
				.setLexiconPages(
						new PageText(".page1"),
						petalRecipe(spawnlesiaRecipe)
				);
	}

	private static LexiconPage petalRecipe(RecipeRuneAltar recipe) {
		return BotaniaAPI.internalHandler.petalRecipePage(".altarCraft", recipe);
	}

	private static class DHLexiconEntry extends BLexiconEntry {

		public DHLexiconEntry(String name, LexiconCategory category) {
			super(name, category);
			setKnowledgeType(lexusTab);
		}

	}

}
