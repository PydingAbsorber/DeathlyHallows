package com.pyding.deathlyhallows.recipes;

import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.brewing.action.BrewAction;
import com.emoniph.witchery.brewing.action.BrewActionRitualRecipe;
import com.emoniph.witchery.crafting.KettleRecipes;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.items.DHItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import thaumcraft.common.config.ConfigItems;

import static com.emoniph.witchery.Witchery.Items;
import static net.minecraft.init.Items.nether_star;
import static net.minecraft.init.Items.redstone;
import static net.minecraft.init.Items.rotten_flesh;
import static net.minecraft.init.Items.water_bucket;

public final class DHBrewRecipes {

	private DHBrewRecipes() {

	}

	public static void init() {
		addBrewRitual(
				new BrewItemKey(water_bucket),
				new ItemStack(DHItems.soupWithSawdust),
				1000,

				new ItemStack(water_bucket),
				new ItemStack(water_bucket),
				new ItemStack(rotten_flesh),
				new ItemStack(Blocks.planks, 1, 0)
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
		addBrewRitual(
				new BrewItemKey(Blocks.iron_bars),
				new ItemStack(DHItems.hobgoblinChains),
				17000,

				new ItemStack(Blocks.iron_block),
				Items.GENERIC.itemKobolditeNugget.createStack(),
				Items.GENERIC.itemKobolditeDust.createStack(),
				new ItemStack(DHItems.hobgoblinSoul)
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

		addKettleRecipe(
				new ItemStack(DHItems.gastronomicTemptation, 8),
				2000F,
				0xFF_0B_CF_00,
				Items.GENERIC.itemDemonHeart.createStack(),
				Items.GENERIC.itemToeOfFrog.createStack(),
				Items.GENERIC.itemMellifluousHunger.createStack(),
				Items.GENERIC.itemOwletsWing.createStack(),
				Items.GENERIC.itemWormyApple.createStack(),
				Items.GENERIC.itemFrozenHeart.createStack()
		);
	}

	private static void addBrewRitual(BrewItemKey input, ItemStack output, int altarCost, ItemStack... recipe) {
		addBrewRecipe(new BrewActionRitualRecipe(input, new AltarPower(altarCost), new BrewActionRitualRecipe.Recipe(output, recipe)));
	}

	private static void addBrewRecipe(BrewAction recipe) {
		WitcheryBrewRegistry.INSTANCE.register(recipe);
	}

	private static void addKettleRecipe(ItemStack result, float powerCost, int color, ItemStack... inputs) {
		addKettleRecipe(result, powerCost, 0, 0, color, 0, true, inputs);
	}

	private static void addKettleRecipe(ItemStack result, float powerCost, int hatBonus, int familiarType, int color, int dimension, boolean inBook, ItemStack... inputs) {
		KettleRecipes.instance().addRecipe(result, hatBonus, familiarType, powerCost, color, dimension, inBook, inputs);
	}

}	
		