package com.pyding.deathlyhallows.integrations;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.blocks.DHBlocks;
import net.minecraft.item.ItemStack;

public class NEIDHConfig implements IConfigureNEI {
	
	@Override
	public void loadConfig() {
		API.hideItem(new ItemStack(DHBlocks.elderGlyph));
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
