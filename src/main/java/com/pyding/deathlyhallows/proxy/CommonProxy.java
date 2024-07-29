package com.pyding.deathlyhallows.proxy;

import com.pyding.deathlyhallows.entities.DHEntities;
import com.pyding.deathlyhallows.events.DHEvents;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.TrickOrTreat;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.recipes.DHBrewRecipes;
import com.pyding.deathlyhallows.recipes.DHWorkbenchRecipes;
import com.pyding.deathlyhallows.rituals.DHRituals;
import com.pyding.deathlyhallows.utils.DHConfig;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		DHConfig.init(event.getSuggestedConfigurationFile());

		MinecraftForge.EVENT_BUS.register(new DHEvents());
		FMLCommonHandler.instance().bus().register(new DHEvents());

		DHPacketProcessor.preInit();
		DHIntegration.preInit();
	}

	public void init(FMLInitializationEvent event) {
		DHItems.init();
		DHEntities.init();
		DHPacketProcessor.init();
	}

	public void postInit(FMLPostInitializationEvent event) {
		DHWorkbenchRecipes.init();
		DHBrewRecipes.init();
		GuiHandler.init();
		DHRituals.init();
		DHIntegration.postInit();
		TrickOrTreat.initList();
	}

}