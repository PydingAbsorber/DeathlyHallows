package com.pyding.deathlyhallows.items;

import baubles.api.BaubleType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemBaubleInvisibilityMantle extends ItemBaubleBase {

	public ItemBaubleInvisibilityMantle() {
		super("invisibilityMantle", BaubleType.BELT);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase e) {
		World world = e.worldObj;
		EntityPlayer p = (EntityPlayer)e;
		if(!p.isSneaking()) {
			p.noClip = false;
			return;
		}
		NBTTagCompound tag = p.getEntityData();
		if(tag.getInteger("mantlecd") == 0) {
			tag.setInteger("mantlecd", 1200);
			tag.setBoolean("mantleActive", true);
			world.playSoundAtEntity(e, "dh:mantle." + (Math.random() > 0.5 ? (Math.random() > 0.5 ? 1 : 2) : 3), 1F, 1F);
		}
		if(tag.getBoolean("mantleActive")) {
			float yaw = (float)Math.PI / 180F * p.rotationYaw;
			float pitch = (float)Math.PI / 180F * p.rotationPitch;
			float speed = 0.7F;
			p.setVelocity(
					-MathHelper.sin(yaw) * speed, 
					MathHelper.cos(yaw) * speed,
					-MathHelper.sin(pitch) * speed
			);
			p.noClip = true;
			return;
		}
		p.noClip = false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			l.add(StatCollector.translateToLocal("dh.desc.mantle1"));
			l.add(StatCollector.translateToLocal("dh.desc.mantle2"));
			l.add(StatCollector.translateToLocal("dh.desc.mantle3"));
			l.add(StatCollector.translateToLocal("dh.desc.mantle4"));
		}
		else {
			l.add(StatCollector.translateToLocal("dh.desc.mantle5"));
			l.add(StatCollector.translateToLocal("dh.desc.mantle6"));
			l.add(StatCollector.translateToLocal("dh.desc.mantle7"));
		}
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("dhowner")) {
			l.add(StatCollector.translateToLocalFormatted("dh.desc.mantle8", stack.getTagCompound().getString("dhowner")));
		}
		else {
			l.add(StatCollector.translateToLocal("dh.desc.mantle9"));
		}
		l.add(StatCollector.translateToLocal("dh.desc.mantle10"));
	}
	
}
