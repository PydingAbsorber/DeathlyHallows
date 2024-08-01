package com.pyding.deathlyhallows.blocks.flowers;

import net.minecraft.util.IIcon;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileEntity;

public class DHFlowers {
	IIcon icon;

	public static void init() {
		registerFlower(Screamily.class, "screamily");
		registerFlower(Spawnlesia.class, "spawnlesia");
	}

	public static void registerFlower(Class<? extends SubTileEntity> subTile, String name) {
		BotaniaAPI.registerSubTile(name, subTile);
		BotaniaAPI.registerSubTileSignature(subTile, new DHSubTileSignature(name));
		BotaniaAPI.addSubTileToCreativeMenu(name);
	}
}
