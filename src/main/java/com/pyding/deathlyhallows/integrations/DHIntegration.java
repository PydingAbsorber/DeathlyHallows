package com.pyding.deathlyhallows.integrations;

import com.pyding.deathlyhallows.blocks.flowers.DHFlowers;
import cpw.mods.fml.common.Loader;

public class DHIntegration {

	public static final String
			THAUMCRAFT = "Thaumcraft",
			BOTANIA = "Botania",
			BLOOD_MAGIC = "AWWayofTime",
			ARS_MAGICA = "ArsMagica2";

	public static boolean
			thaumcraft = false,
			botania = false,
			bloodMagic = false,
			arsMagica = false;

	public static void preInit() {
		thaumcraft = Loader.isModLoaded(THAUMCRAFT);
		botania = Loader.isModLoaded(BOTANIA);
		bloodMagic = Loader.isModLoaded(BLOOD_MAGIC);
		arsMagica = Loader.isModLoaded(ARS_MAGICA);
	}

	public static void init() {
		if(DHIntegration.thaumcraft) {
			DHThaumcraft.init();
		}
		if(DHIntegration.botania) {
			DHFlowers.init();
		}
	}

	public static void postInit() {
		if(thaumcraft) {
			DHThaumcraft.recipes();
			DHThaumcraft.aspects();
		}
		if(DHIntegration.botania) {
			DHBotania.init();
		}
	}

}
