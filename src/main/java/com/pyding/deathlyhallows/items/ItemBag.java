package com.pyding.deathlyhallows.items;

import com.pyding.deathlyhallows.utils.DHUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemBag extends ItemBase {

	public ItemBag() {
		super("bag", 64);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		List<EntityLightningBolt> list = DHUtils.getEntitiesAround(EntityLightningBolt.class,p,3);
		if(!list.isEmpty()){
			stack.splitStack(1);
			p.inventory.addItemStackToInventory(new ItemStack(DHItems.lightningInBag));
		}
		return super.onItemRightClick(stack, world, p);
	}
	
}
