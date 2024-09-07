package com.pyding.deathlyhallows.integrations;

import com.pyding.deathlyhallows.blocks.flowers.DHFlowers;
import com.pyding.deathlyhallows.integrations.gtnh.thaumcraft.DHWandRegistry;
import com.pyding.deathlyhallows.integrations.thaumcraft.DHThaumcraft;
import cpw.mods.fml.common.Loader;

public class DHIntegration {

	public static final String
			WITCHERY = "witchery", // just for reference
			THAUMCRAFT = "Thaumcraft",
			GTNHTCWANDS = "gtnhtcwands",
			BOTANIA = "Botania",
			BLOOD_MAGIC = "AWWayofTime",
			ARS_MAGICA = "arsmagica2";

	public static boolean
			thaumcraft = false,
			gtnhWands = false,
			botania = false,
			bloodMagic = false,
			arsMagica = false;

	public static void preInit() {
		thaumcraft = Loader.isModLoaded(THAUMCRAFT);
		gtnhWands = Loader.isModLoaded(GTNHTCWANDS);
		botania = Loader.isModLoaded(BOTANIA);
		bloodMagic = Loader.isModLoaded(BLOOD_MAGIC);
		arsMagica = Loader.isModLoaded(ARS_MAGICA);
	}

	public static void init() {
		if(DHIntegration.thaumcraft) {
			DHThaumcraft.init();
		}
		if(DHIntegration.gtnhWands) {
			DHWandRegistry.init();
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
