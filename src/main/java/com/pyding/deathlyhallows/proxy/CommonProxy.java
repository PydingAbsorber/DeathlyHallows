package com.pyding.deathlyhallows.proxy;

import com.pyding.deathlyhallows.blocks.DHBlocks;
import com.pyding.deathlyhallows.entities.DHEntities;
import com.pyding.deathlyhallows.events.DHDeathEvents;
import com.pyding.deathlyhallows.events.DHElfEvents;
import com.pyding.deathlyhallows.events.DHEvents;
import com.pyding.deathlyhallows.events.DHGoblinEvents;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.ItemTrickOrTreat;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.recipes.DHBrewRecipes;
import com.pyding.deathlyhallows.recipes.DHGrassperRecipes;
import com.pyding.deathlyhallows.recipes.DHWorkbenchRecipes;
import com.pyding.deathlyhallows.rituals.DHRituals;
import com.pyding.deathlyhallows.symbols.DHSymbols;
import com.pyding.deathlyhallows.utils.DHConfig;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		DHIntegration.preInit();
		DHConfig.init(event.getSuggestedConfigurationFile());
		DHPacketProcessor.preInit();
	}

	public void init(FMLInitializationEvent event) {
		DHItems.init();
		DHBlocks.init();
		DHEntities.init();
		DHEvents.init();
		DHElfEvents.init();
		DHDeathEvents.init();
		DHGoblinEvents.init();
		DHSymbols.init();
		DHPacketProcessor.init();
		DHIntegration.init();
	}

	public void postInit(FMLPostInitializationEvent event) {
		DHWorkbenchRecipes.init();
		DHBrewRecipes.init();
		DHGrassperRecipes.init();
		GuiHandler.init();
		DHRituals.init();
		DHIntegration.postInit();
		ItemTrickOrTreat.initList();
	}

}
