package com.pyding.deathlyhallows.proxy;

import com.pyding.deathlyhallows.DeathHallowsMod;
import com.pyding.deathlyhallows.guis.ElderWitchbookGui;
import com.pyding.deathlyhallows.items.ElderBook;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Objects;

public class GuiHandler implements IGuiHandler {
	private static final GuiHandler instance = new GuiHandler();

	private GuiHandler() {

	}

	public static void init() {
		NetworkRegistry.INSTANCE.registerGuiHandler(DeathHallowsMod.ID, instance);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Object getClientGuiElement(int id, EntityPlayer p, World world, int x, int y, int z) {
		if(Objects.requireNonNull(GUI.values()[id]) == GUI.ELDER_RITUALS) {
			ItemStack b = p.getHeldItem();
			if(b != null && b.getItem() instanceof ElderBook) {
				return new ElderWitchbookGui(p, b);
			}
			return null;
		}
		return null;
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer p, World world, int x, int y, int z) {
		return null;
	}

	public enum GUI {
		ELDER_RITUALS
	}
}