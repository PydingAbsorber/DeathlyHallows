package com.pyding.deathlyhallows.items.wands;

import com.google.common.collect.Lists;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.items.ItemBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import thaumcraft.api.aspects.Aspect;

import java.util.List;

public class ItemWandCap extends ItemBase {

	public enum Caps {
		
		koboldite(0.8F, 6, 0.5F, Aspect.EARTH),
		cotton(2F, 0);

		public final List<Aspect> aspects;
		public final int cost;
		public final float discount, specialDiscount;
		public IIcon icon;

		Caps(float discount, int cost, float specialDiscount, Aspect... special) {
			this.discount = discount;
			this.cost = cost;
			this.specialDiscount = specialDiscount;
			this.aspects = special == null ? null : Lists.newArrayList(special);
		}

		Caps(float discount, int cost) {
			this(discount, cost, 0F);
		}

	}

	public ItemWandCap() {
		super("wandCap", 64);
		hasSubtypes = true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister ir) {
		for(Caps type: Caps.values()) {
			type.icon = ir.registerIcon(DeathlyHallows.MODID + ":" + iconString + "_" + type.name());
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int meta) {
		return Caps.values()[meta].icon;
	}

	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(Caps type: Caps.values()) {
			list.add(new ItemStack(this, 1, type.ordinal()));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + Caps.values()[stack.getItemDamage()].name();
	}

}
