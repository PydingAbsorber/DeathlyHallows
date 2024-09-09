package com.pyding.deathlyhallows.items.wands;

import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.integrations.thaumcraft.DHThaumcraft;
import com.pyding.deathlyhallows.items.ItemBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemWandRod extends ItemBase {

	public enum Rods {
		
		rowan(125, 9), // magic capacity
		alder(100, 9), // pure charm
		hawthorn(100, 9); // misfortune curse

		public final int capacity, cost;
		public IIcon icon;

		Rods(int capacity, int cost) {
			this.capacity = capacity;
			this.cost = cost;
		}
		
	}

	public ItemWandRod() {
		super("wandRod", 64);
		hasSubtypes = true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister ir) {
		for(Rods type: Rods.values()) {
			type.icon = ir.registerIcon(DeathlyHallows.MODID + ":" + iconString + "_" + type.name());
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int meta) {
		return Rods.values()[meta].icon;
	}

	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(Rods type: Rods.values()) {
			list.add(new ItemStack(this, 1, type.ordinal()));
		}
		if(DHIntegration.thaumcraft) {
			DHThaumcraft.addWandsToTab(list);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + Rods.values()[stack.getItemDamage()].name();
	}

}
