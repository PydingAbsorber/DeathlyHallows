package com.pyding.deathlyhallows.client.gui;

import com.pyding.deathlyhallows.DeathHallowsMod;
import com.pyding.deathlyhallows.items.ElderBook;
import com.pyding.deathlyhallows.rituals.ElderWitchbookGui;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GuiRegister implements IGuiHandler  {
	private static final GuiRegister instance = new GuiRegister();

	private GuiRegister() {

	}

	public static void init() {
		NetworkRegistry.INSTANCE.registerGuiHandler(DeathHallowsMod.ID, instance);
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer p, World world, int x, int y, int z) {
		switch(GUI.values()[id]) {
			case ELDER_RITUALS: {
				ItemStack b = p.getHeldItem();
				if(b != null && b.getItem() instanceof ElderBook) {
					return new ElderWitchbookGui(p,b);
				}
				return null;
			}
			default:
				return null;
		}
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer p, World world, int x, int y, int z) {
		switch(GUI.values()[id]) {
			default:
				return null;
		}
	}

	public enum GUI {
		ELDER_RITUALS
	}
}
