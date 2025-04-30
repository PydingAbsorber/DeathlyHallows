package com.pyding.deathlyhallows.recipes;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.integrations.DHArsMagica2;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.wands.ItemWandCap;
import com.pyding.deathlyhallows.recipes.grid.RecipeDyeable;
import com.pyding.deathlyhallows.recipes.grid.RecipeUnDyeable;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static com.emoniph.witchery.Witchery.Items;
import static com.pyding.deathlyhallows.blocks.DHBlocks.visConverter;
import static com.pyding.deathlyhallows.items.DHItems.deathShard;
import static net.minecraft.init.Items.dye;

public final class DHWorkbenchRecipes {
	
	public static IRecipe VISC;

	private DHWorkbenchRecipes() {

	}

	public static void init() {
		addShapelessRecipe(
				new ItemStack(DHItems.bertieBots),
				new ItemStack(dye, 1, 3),
				new ItemStack(DHItems.gastronomicTemptation)
		);
		addShapelessRecipe(
				new ItemStack(DHItems.monsterBook),
				Items.GENERIC.itemNullifiedLeather.createStack(),
				Items.GENERIC.itemOwletsWing.createStack(),
				Items.GENERIC.itemNullifiedLeather.createStack(),
				Items.GENERIC.itemToeOfFrog.createStack(),
				Items.GENERIC.itemBookBurning.createStack(),
				Items.GENERIC.itemToeOfFrog.createStack(),
				Items.GENERIC.itemVampireBookPage.createStack(),
				Items.GENERIC.itemContractTorment.createStack(),
				Items.GENERIC.itemDarkCloth.createStack()
		);
		addShapedRecipe(
				new ItemStack(DHItems.bag),
				" S ",
				"LCL",
				'S', Items.GENERIC.itemGoldenThread.createStack(),
				'C', Items.GENERIC.itemDropOfLuck.createStack(),
				'L', Items.GENERIC.itemImpregnatedLeather.createStack()
		);
		if(DHIntegration.arsMagica) {
			addShapelessRecipe(
					DHArsMagica2.essence(8, 7),
					new ItemStack(DHItems.lightningInBag)
			);
		}
		if(DHIntegration.thaumcraft) {
			addShapedOreRecipe(
					new ItemStack(DHItems.wandCap, 1, ItemWandCap.Caps.koboldite.ordinal()),
					"KKK",
					"K K",
					'K', Items.GENERIC.itemKobolditeNugget.createStack()
			);
			VISC = addShapedRecipe(
					new ItemStack(visConverter),
					"BHB",
					"HSH",
					"BHB",
					'B', new ItemStack(Witchery.Items.MYSTIC_BRANCH),
					'H', Witchery.Items.GENERIC.itemDemonHeart.createStack(),
					'S', new ItemStack(deathShard));
		}
		recipe(new RecipeDyeable(), "dyeable", RecipeSorter.Category.SHAPELESS);
		recipe(new RecipeUnDyeable(), "undyeable", RecipeSorter.Category.SHAPELESS);
	}
	
	public static void addShapelessRecipe(ItemStack output, Object... params) {
		GameRegistry.addShapelessRecipe(output, params);
	}

	public static IRecipe addShapedRecipe(ItemStack output, Object... params) {
		return GameRegistry.addShapedRecipe(output, params);
	}

	private static IRecipe addShapedOreRecipe(ItemStack i, Object... o) {
		IRecipe recipe = new ShapedOreRecipe(i, o);
		GameRegistry.addRecipe(recipe);
		return recipe;
	}

	private static void recipe(IRecipe recipe, String name, RecipeSorter.Category category) {
		GameRegistry.addRecipe(recipe);
		RecipeSorter.register(DeathlyHallows.MODID + ":" + name, recipe.getClass(), category, "");
	}
	
}
