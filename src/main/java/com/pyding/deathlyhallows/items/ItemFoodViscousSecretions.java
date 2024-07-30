package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.util.ChatUtil;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;

import java.util.List;

public class ItemFoodViscousSecretions extends ItemFoodBase {
	public ItemFoodViscousSecretions() {
		super("ViscousSecretions", 20, 40);
		setAlwaysEdible();
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World w, EntityPlayer p) {
		return super.onItemRightClick(stack, w, p);
	}

	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer p) {
		super.onEaten(stack, world, p);
		if(!world.isRemote) {
			ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, p, "dh.chat.food");
		}
		p.getEntityData().setInteger("DopVoid", 3000);
		if(world.isRemote || !DHIntegration.thaumcraft) {
			return stack;
		}
		double random = Math.random();
		if(random < 0.01) {
			Thaumcraft.addWarpToPlayer(p, 1, false);
		}
		else if(random < 0.1) {
			Thaumcraft.addStickyWarpToPlayer(p, 1);
		}
		else {
			Thaumcraft.addWarpToPlayer(p, 1, true);
		}
		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		l.add(StatCollector.translateToLocal("dh.desc.visSec1"));
		l.add(StatCollector.translateToLocalFormatted("dh.desc.visSec2", "1%", "10%", "89%"));
	}
	
}
