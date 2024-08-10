package com.pyding.deathlyhallows.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemLightningInBag extends ItemBase {

	public ItemLightningInBag() {
		super("lightningInBag", 1);
		setContainerItem(DHItems.bag);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		l.add(StatCollector.translateToLocal("dh.desc.light"));
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			l.add(StatCollector.translateToLocal("dh.desc.light2"));
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		p.getEntityData().setLong("DHStrike", System.currentTimeMillis() + 3000);
		return super.onItemRightClick(stack, world, p);
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		return new ItemStack(DHItems.bag);
	}
	
}
