package com.pyding.deathlyhallows.items.food;

import com.pyding.deathlyhallows.DeathlyHallows;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemFoodBase extends ItemFood {

	public ItemFoodBase(String unlocalizedName, int hunger, float saturation, int maxStackSize, CreativeTabs tab) {
		super(hunger, saturation, false);
		setUnlocalizedName(unlocalizedName);
		setTextureName(unlocalizedName);
		setMaxStackSize(maxStackSize);
		setCreativeTab(tab);
	}

	public ItemFoodBase(String unlocalizedName, int hunger, float saturation) {
		this(unlocalizedName, hunger, saturation, 64, DeathlyHallows.tabDeathlyHallows);
	}

	@Override
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean devMode) {
		addTooltip(stack, player, list, devMode);
	}

	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister ir) {
		itemIcon = ir.registerIcon(DeathlyHallows.MODID + ":" + iconString);
	}

}
