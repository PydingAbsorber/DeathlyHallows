package com.pyding.deathlyhallows.items.baubles;

import baubles.api.BaubleType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemBaubleInvisibilityMantle extends ItemBaubleBase {
	
	public static final float HEALTH_STEAL_RATIO = 0.05F;

	public ItemBaubleInvisibilityMantle() {
		super("invisibilityMantle", BaubleType.BELT);
	}
	
	public static boolean isMantleActive(EntityLivingBase e) {
		return e.getEntityData().hasKey("mantleActive");
	}
	
	public static void setMantleAbilityState(EntityPlayer p, boolean active) {
		p.noClip = active;
	}

	public static void setMantleState(EntityPlayer p, boolean active) {
		if(!p.capabilities.isCreativeMode) {
			p.capabilities.disableDamage = active;
			p.hurtResistantTime = active ? 1000 : 0;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			l.add(StatCollector.translateToLocal("dh.desc.mantle1"));
			l.add(StatCollector.translateToLocal("dh.desc.mantle2"));
			l.add(StatCollector.translateToLocal("dh.desc.mantle3"));
		}
		else {
			l.add(StatCollector.translateToLocal("dh.desc.mantle4"));
			l.add(StatCollector.translateToLocal("dh.desc.mantle5"));
			l.add(StatCollector.translateToLocal("dh.desc.mantle6"));
		}
		String owner = stack.hasTagCompound() ? stack.getTagCompound().getString("dhowner") : "";
		if(owner.equals("")) {
			owner = StatCollector.translateToLocal("dh.desc.defaultOwner");
		}
		l.add(StatCollector.translateToLocalFormatted("dh.desc.owner", owner));
		l.add(StatCollector.translateToLocal("dh.desc.hallow"));
	}

}
