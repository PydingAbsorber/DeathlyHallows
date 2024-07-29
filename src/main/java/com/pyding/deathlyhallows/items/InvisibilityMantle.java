package com.pyding.deathlyhallows.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class InvisibilityMantle extends Item implements IBauble {
	@Override
	public BaubleType getBaubleType(ItemStack itemStack) {
		return BaubleType.BELT;
	}
	
	@Override
	public void onWornTick(ItemStack itemStack, EntityLivingBase entityLivingBase) {
		World world = entityLivingBase.worldObj;
		EntityPlayer player = (EntityPlayer)entityLivingBase;
		if(player.isSneaking()) {
			if(player.getEntityData().getInteger("mantlecd") == 0) {
				player.getEntityData().setInteger("mantlecd", 1200);
				player.getEntityData().setBoolean("mantleActive", true);
				if(Math.random() > 0.5) {
					if(Math.random() > 0.5) {
						world.playSoundAtEntity(entityLivingBase, "dh:mantle.1", 1F, 1F);
					}
					else {
						world.playSoundAtEntity(entityLivingBase, "dh:mantle.2", 1F, 1F);
					}
				}
				else {
					world.playSoundAtEntity(entityLivingBase, "dh:mantle.3", 1F, 1F);
				}
			}
			if(player.getEntityData().getBoolean("mantleActive")) {
				float yaw = player.rotationYaw;
				float pitch = player.rotationPitch;
				double speed = 0.7;
				double motionX = -Math.sin(Math.toRadians(yaw)) * speed;
				double motionZ = Math.cos(Math.toRadians(yaw)) * speed;
				double motionY = Math.sin(Math.toRadians(-pitch)) * speed;
				player.setVelocity(motionX, motionY, motionZ);
				player.noClip = true;
			}
			else {
				player.noClip = false;
			}
		}
		else {
			player.noClip = false;
		}
	}

	@Override
	public void onEquipped(ItemStack itemStack, EntityLivingBase entityLivingBase) {

	}

	@Override
	public void onUnequipped(ItemStack itemStack, EntityLivingBase entityLivingBase) {

	}

	@Override
	public boolean canEquip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {

		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			list.add(StatCollector.translateToLocal("dh.desc.mantle1"));
			list.add(StatCollector.translateToLocal("dh.desc.mantle2"));
			list.add(StatCollector.translateToLocal("dh.desc.mantle3"));
			list.add(StatCollector.translateToLocal("dh.desc.mantle4"));
		}
		else {
			list.add(StatCollector.translateToLocal("dh.desc.mantle5"));
			list.add(StatCollector.translateToLocal("dh.desc.mantle6"));
			list.add(StatCollector.translateToLocal("dh.desc.mantle7"));
		}
		if(stack.hasTagCompound()) {
			if(stack.getTagCompound().hasKey("dhowner")) {
				list.add(StatCollector.translateToLocalFormatted("dh.desc.mantle8", stack.getTagCompound().getString("dhowner")));
			}
		}
		else {
			list.add(StatCollector.translateToLocal("dh.desc.mantle9"));
		}
		list.add(StatCollector.translateToLocal("dh.desc.mantle10"));
	}
}
