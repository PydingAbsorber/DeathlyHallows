package com.pyding.deathlyhallows.integrations.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.blocks.DHBlocks;
import net.minecraft.item.ItemStack;

public class NEIDHConfig implements IConfigureNEI {

	@Override
	public void loadConfig() {
		API.hideItem(new ItemStack(DHBlocks.elderGlyph));
		TemplateRecipeHandler grassperRecipes = new NEIDHGrassperRecipeHandler();
		API.registerRecipeHandler(grassperRecipes);
		API.registerUsageHandler(grassperRecipes);
	}

	@Override
	public String getName() {
		return DeathlyHallows.NAME;
	}

	@Override
	public String getVersion() {
		return DeathlyHallows.VERSION;
	}
	
}
