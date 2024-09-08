package com.pyding.deathlyhallows.recipes;

import am2.items.ItemEssence;
import am2.items.ItemsCommonProxy;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.wands.ItemWandCap;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static com.emoniph.witchery.Witchery.Items;
import static net.minecraft.init.Items.dye;

public final class DHWorkbenchRecipes {

	private DHWorkbenchRecipes() {

	}

	public static void init() {
		addShapeledRecipe(
				new ItemStack(DHItems.wandCap, 1, ItemWandCap.Caps.koboldite.ordinal()),
				"KKK",
				"K K",
				'K', Items.GENERIC.itemKobolditeIngot.createStack()
		);
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
		if(DHIntegration.arsMagica) {
			addShapelessRecipe(
					new ItemStack(ItemsCommonProxy.essence, 8, ItemEssence.META_LIGHTNING),
					new ItemStack(DHItems.lightningInBag)
			);
		}
	}

	public static void addShapelessRecipe(ItemStack output, Object... params) {
		GameRegistry.addShapelessRecipe(output, params);
	}

	public static void addShapedRecipe(ItemStack output, Object... params) {
		GameRegistry.addShapedRecipe(output, params);
	}

	private static void addShapeledRecipe(ItemStack i, Object... o) {
		GameRegistry.addRecipe(new ShapedOreRecipe(i, o));
	}

}
