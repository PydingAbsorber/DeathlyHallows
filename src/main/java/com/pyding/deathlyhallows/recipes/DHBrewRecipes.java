package com.pyding.deathlyhallows.recipes;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.brewing.action.BrewAction;
import com.emoniph.witchery.brewing.action.BrewActionRitualRecipe;
import com.emoniph.witchery.crafting.KettleRecipes;
import com.emoniph.witchery.familiar.Familiar;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.recipes.actions.BrewActionIngredient;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import thaumcraft.common.config.ConfigItems;

import static com.emoniph.witchery.Witchery.Items;
import static net.minecraft.init.Items.bucket;
import static net.minecraft.init.Items.dye;
import static net.minecraft.init.Items.nether_star;
import static net.minecraft.init.Items.redstone;

public final class DHBrewRecipes {

	private DHBrewRecipes() {

	}

	public static void init() {
		addChalkRecipe(
				new ItemStack(DHItems.elderChalk),

				Items.GENERIC.itemGraveyardDust.createStack(),
				Items.GENERIC.itemNullCatalyst.createStack(),
				new ItemStack(DHItems.hobgoblinSoul)
		);
		
		addBrewRitual(
				new BrewItemKey(DHItems.gastronomicTemptation),
				new ItemStack(DHItems.viscousSecretions),
				5000,

				new ItemStack(nether_star),
				Items.GENERIC.itemTearOfTheGoddess.createStack(),
				new ItemStack(redstone),
				Items.GENERIC.itemBatWool.createStack(),
				Items.GENERIC.itemDiamondVapour.createStack()
		);
		if(DHIntegration.thaumcraft) {
			addBrewRitual(
					new BrewItemKey(ConfigItems.itemBucketDeath),
					new ItemStack(DHItems.viscousSecretions),
					7000,

					new ItemStack(nether_star),
					Items.GENERIC.itemTearOfTheGoddess.createStack(),
					new ItemStack(redstone),
					Items.GENERIC.itemBatWool.createStack(),
					Items.GENERIC.itemDiamondVapour.createStack()
			);
		}
		addBrewRitual(
				new BrewItemKey(Blocks.iron_bars),
				new ItemStack(DHItems.hobgoblinChains),
				17000,

				new ItemStack(Blocks.iron_block),
				Items.GENERIC.itemKobolditeNugget.createStack(),
				Items.GENERIC.itemKobolditeDust.createStack(),
				new ItemStack(DHItems.hobgoblinSoul)
		);
		for(ItemStack plank: OreDictionary.getOres("plankWood")) {
			addBrewRitual(
					new BrewItemKey(bucket),
					new ItemStack(DHItems.soupWithSawdust),
					500,
					Items.GENERIC.itemMandrakeRoot.createStack(),
					new ItemStack(dye, 1, 15),
					plank,
					Items.GENERIC.itemOddPorkCooked.createStack()
			);
		}

		addKettleRecipe(
				new ItemStack(DHItems.gastronomicTemptation, 8),
				2000F,
				2,
				Familiar.FAMILIAR_NONE,
				0xFF0BCF00,
				0,
				true,
				Items.GENERIC.itemDemonHeart.createStack(),
				Items.GENERIC.itemToeOfFrog.createStack(),
				Items.GENERIC.itemMellifluousHunger.createStack(),
				Items.GENERIC.itemOwletsWing.createStack(),
				Items.GENERIC.itemWormyApple.createStack(),
				Items.GENERIC.itemFrozenHeart.createStack()
		);
	}

	private static void addChalkRecipe(ItemStack resultChalk, ItemStack... ingredients) {
		ItemStack chalk = new ItemStack(Witchery.Items.CHALK_RITUAL);
		BrewActionRitualRecipe chalkRecipes = (BrewActionRitualRecipe)WitcheryBrewRegistry.INSTANCE.getActionForItemStack(chalk);
		ensureIngredients(ingredients);
		
		chalkRecipes.recipes = ArrayUtils.add(chalkRecipes.recipes, new BrewActionRitualRecipe.Recipe(resultChalk, ingredients));
		chalkRecipes.getExpandedRecipes().add(new BrewActionRitualRecipe.Recipe(resultChalk, ingredients, chalk));
	}
		
	private static void addBrewRitual(BrewItemKey input, ItemStack output, int altarCost, ItemStack... ingredients) {
		ensureIngredients(ingredients);
		addBrewRecipe(new BrewActionRitualRecipe(input, new AltarPower(altarCost), new BrewActionRitualRecipe.Recipe(output, ingredients)));
	}

	private static void addBrewItemKey(BrewItemKey key) {
		// stub that makes possible to use key as ingredient
		addBrewRecipe(new BrewActionIngredient(key));
	}

	private static void ensureIngredients(ItemStack... recipe) {
		for(ItemStack stack: recipe) {
			if(WitcheryBrewRegistry.INSTANCE.getActionForItemStack(stack) == null) {
				addBrewItemKey(BrewItemKey.fromStack(stack));
			}
		}
	}

	private static void addBrewRecipe(BrewAction recipe) {
		WitcheryBrewRegistry.INSTANCE.register(recipe);
	}

	private static void addKettleRecipe(ItemStack result, float powerCost, int hatBonus, int familiarType, int color, int dimension, boolean inBook, ItemStack... inputs) {
		KettleRecipes.instance().addRecipe(result, hatBonus, familiarType, powerCost, color, dimension, inBook, inputs);
	}

}	
		