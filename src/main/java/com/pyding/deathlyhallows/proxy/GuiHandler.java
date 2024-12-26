package com.pyding.deathlyhallows.proxy;

import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.guis.GuiElderRituals;
import com.pyding.deathlyhallows.guis.GuiElderWands;
import com.pyding.deathlyhallows.items.ItemElderBook;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
	private static final GuiHandler instance = new GuiHandler();

	private GuiHandler() {

	}

	public static void init() {
		NetworkRegistry.INSTANCE.registerGuiHandler(DeathlyHallows.MODID, instance);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Object getClientGuiElement(int id, EntityPlayer p, World world, int x, int y, int z) {
		switch(GUI.values()[id]) {
			case ELDER_RITUALS: {
				ItemStack b = p.getHeldItem();
				if(b != null && b.getItem() instanceof ItemElderBook) {
					return new GuiElderRituals(p, b);
				}
				return null;
			}
			case ELDER_WANDS: {
				ItemStack b = p.getHeldItem();
				if(b != null && b.getItem() instanceof ItemElderBook) {
					return new GuiElderWands(p, b);
				}
				return null;
			}
		}
		return null;
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer p, World world, int x, int y, int z) {
		return null;
	}

	public enum GUI {
		ELDER_RITUALS,
		ELDER_WANDS
	}
}
