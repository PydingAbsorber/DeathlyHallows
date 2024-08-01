package com.pyding.deathlyhallows.items.baubles;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.pyding.deathlyhallows.items.ItemBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemBaubleBase extends ItemBase implements IBauble {
	
	private final BaubleType type;

	public ItemBaubleBase(String unlocalizedName, BaubleType type) {
		super(unlocalizedName, 1);
		this.type = type;
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return type;
	}

	@Override
	public void onWornTick(ItemStack tack, EntityLivingBase e) {

	}

	@Override
	public void onEquipped(ItemStack tack, EntityLivingBase e) {

	}

	@Override
	public void onUnequipped(ItemStack tack, EntityLivingBase e) {

	}

	@Override
	public boolean canEquip(ItemStack tack, EntityLivingBase e) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack tack, EntityLivingBase e) {
		return true;
	}
}
