package com.pyding.deathlyhallows;

import com.pyding.deathlyhallows.commands.CommandDamageLog;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.proxy.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
		modid = DeathlyHallows.MODID,
		name = DeathlyHallows.NAME,
		version = DeathlyHallows.VERSION,
		dependencies = "before:gtnhtcwands;"
				+ "required-after:witchery;"
				+ "required-after:Baubles;"
				+ "after:Thaumcraft;"
				+ "after:AWWayofTime;"
				+ "after:Botania;"
				+ "after:AdvancedBotany"
)
public class DeathlyHallows {

	public static final String
			MODID = "dh",
			NAME = "Deathly Hallows",
			VERSION = "1.0.1";

	@Mod.Instance("dh")
	public static DeathlyHallows Instance;

	@SidedProxy(
			clientSide = "com.pyding.deathlyhallows.proxy.ClientProxy",
			serverSide = "com.pyding.deathlyhallows.proxy.CommonProxy"
	)
	public static CommonProxy proxy;

	public static final Logger LOG = LogManager.getLogger(MODID);

	@Mod.EventHandler
	public void preInt(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);

	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandDamageLog());
	}

	public static CreativeTabs tabDeathlyHallows = new CreativeTabs("tabDeathlyHallows") {
		@Override
		public Item getTabIconItem() {
			return DHItems.itemLogo;
		}
	};

	// Little gift
	// thanks!
	public static void initAlfheimCompat() {
		if(Loader.isModLoaded("alfheim")) {
			LOG.error("Alfheim is not supported");
			FMLCommonHandler.instance().handleExit(0);
		}
	}

}