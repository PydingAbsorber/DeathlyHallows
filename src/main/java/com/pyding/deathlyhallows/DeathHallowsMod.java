package com.pyding.deathlyhallows;

import com.pyding.deathlyhallows.commands.CommandDamageLog;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

@Mod(modid = DeathHallowsMod.ID, name = "Deathly Hallows", version = "1.0", dependencies = "required-after:witchery;required-after:Baubles;after:Thaumcraft;after:AWWayofTime;after:Botania;after:AdvancedBotany")
public class DeathHallowsMod {

	public static final String ID = "dh";

	@Mod.Instance("dh")
	public static DeathHallowsMod Instance;

	public static final DHPacketProcessor network = new DHPacketProcessor();
	@SidedProxy(
			clientSide = "com.pyding.deathlyhallows.proxy.ClientProxy",
			serverSide = "com.pyding.deathlyhallows.proxy.CommonProxy"
	)
	public static CommonProxy proxy;


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
			return DHItems.tabItem;
		}
	};
}