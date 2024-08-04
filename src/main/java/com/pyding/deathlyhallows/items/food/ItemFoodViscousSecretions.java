package com.pyding.deathlyhallows.items.food;

import com.emoniph.witchery.util.ChatUtil;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.api.IWarpingGear;
import thaumcraft.common.Thaumcraft;

import java.util.List;

@Optional.Interface(iface = "thaumcraft.api.IWarpingGear", modid = DHIntegration.THAUMCRAFT, striprefs = true)
public class ItemFoodViscousSecretions extends ItemFoodBase implements IWarpingGear {
	public ItemFoodViscousSecretions() {
		super("viscousSecretions", 20, 40);
		setAlwaysEdible();
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
		if(!DHIntegration.thaumcraft) {
			return;
		}
		l.add(StatCollector.translateToLocal("dh.desc.visSec1"));
		l.add(StatCollector.translateToLocalFormatted("dh.desc.visSec2", "1%", "10%", "89%"));
	}

	@Optional.Method(modid = DHIntegration.THAUMCRAFT)
	@Override
	public int getWarp(ItemStack stack, EntityPlayer p) {
		return 2;
	}
	
}
