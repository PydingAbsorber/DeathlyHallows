package com.pyding.deathlyhallows.items;

import com.pyding.deathlyhallows.DeathHallowsMod;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ElderBook extends Item {
	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
		FMLNetworkHandler.openGui(p_77659_3_, DeathHallowsMod.Instance,0,p_77659_2_,(int)p_77659_3_.posX,(int)p_77659_3_.posY,(int)p_77659_3_.posZ);
		return super.onItemRightClick(p_77659_1_, p_77659_2_, p_77659_3_);
	}
}
