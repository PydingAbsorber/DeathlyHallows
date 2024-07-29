package com.pyding.deathlyhallows.recipes;

import com.pyding.deathlyhallows.items.DHItems;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

import static com.emoniph.witchery.Witchery.Items;
import static net.minecraft.init.Items.dye;

public final class DHWorkbenchRecipes {

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
	}

	private static void addShapelessRecipe(ItemStack output, Object... params) {
		GameRegistry.addShapelessRecipe(output, params);
	}

}
