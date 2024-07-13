package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.util.ChatUtil;
import com.pyding.deathlyhallows.integration.Integration;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;

import java.util.List;

public class ViscousSecretions extends ItemFood {
	public ViscousSecretions() {
		super(20, 40, true);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
		this.setAlwaysEdible();
		return super.onItemRightClick(p_77659_1_, p_77659_2_, p_77659_3_);
	}

	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		super.onEaten(stack, world, player);
		if(!world.isRemote) {
			ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "dh.chat.food");
		}
		player.getEntityData().setInteger("DopVoid", 3000);
		if(!world.isRemote && Integration.thaumcraft) {
			double random = Math.random();
			if(random < 0.01) {
				Thaumcraft.addWarpToPlayer(player, 1, false);
			}
			else if(random < 0.1) {
				Thaumcraft.addStickyWarpToPlayer(player, 1);
			}
			else {
				Thaumcraft.addWarpToPlayer(player, 1, true);
			}
		}
		return stack;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
		if(Integration.thaumcraft) {
			if(I18n.format("dh.util.language").equals("Ru")) {
				list.add("§5Нежное искажение: ");
				list.add("§5Шанс 1% на 1 постоянное, 10% на липкое, 89% на временное искажение");
			}
			else {
				list.add("§5Gentle Warp: ");
				list.add("§5Chance 1% for 1 permanent, 10% for sticky, 89% for temporary warp");
			}
		}
	}
}
