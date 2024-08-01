package com.pyding.deathlyhallows.proxy;

import com.pyding.deathlyhallows.blocks.DHBlocks;
import com.pyding.deathlyhallows.entities.DHEntities;
import com.pyding.deathlyhallows.events.DHMultiBlockRenderEvents;
import com.pyding.deathlyhallows.events.DHPlayerRenderEvents;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.utils.DHKeys;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		DHKeys.register();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		DHItems.initClient();
		DHBlocks.initClient();
		DHEntities.initClient();

		DHMultiBlockRenderEvents.init();
		DHPlayerRenderEvents.init();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

}
