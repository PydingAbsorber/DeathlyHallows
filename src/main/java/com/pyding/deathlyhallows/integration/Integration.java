package com.pyding.deathlyhallows.integration;

import com.pyding.deathlyhallows.common.RegisterFlowers;
import cpw.mods.fml.common.Loader;

public class Integration {
	public static boolean thaumcraft = false;
	public static boolean botania = false;
	public static boolean bloodMagic = false;
	public static boolean arsMagica = false;

	public static void preInit() {
		thaumcraft = Loader.isModLoaded("Thaumcraft");
		botania = Loader.isModLoaded("Botania");
		bloodMagic = Loader.isModLoaded("AWWayofTime");
		arsMagica = Loader.isModLoaded("ArsMagica2");
	}

	public static void init() {
		if(Integration.thaumcraft) {
			ThaumcraftDH.register();
		}
		if(Integration.botania) {
			RegisterFlowers.init();
		}
	}

	public static void postInit() {
		if(thaumcraft) {
			ThaumcraftDH.recipes();
			ThaumcraftDH.aspects();
		}
		if(Integration.botania) {
			BotaniaDH.lexus();
		}
	}
}
