package com.pyding.deathlyhallows.items.baubles;

import baubles.api.BaubleType;
import com.pyding.deathlyhallows.utils.DHUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
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
		NBTTagCompound tag = p.getEntityData();
		if(!p.isSneaking()) {
			if(tag.hasKey("mantleActive")) {
				p.noClip = false;
			}
			return;
		}
		if(tag.getInteger("mantlecd") <= 0) {
			tag.setInteger("mantlecd", 1200);
			tag.setBoolean("mantleActive", true);
			world.playSoundAtEntity(e, "dh:mantle." + DHUtils.getRandomInt(1, 3), 1F, 1F);
		}
		if(tag.hasKey("mantleActive")) {
			Vec3 vel = p.getLookVec();
			float speed = 0.7F;
			p.setVelocity(vel.xCoord * speed, vel.yCoord * speed, vel.zCoord * speed);
			p.noClip = true;
		}
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
		String owner = stack.hasTagCompound() ? stack.getTagCompound().getString("dhowner") : "";
		if(owner.equals("")) {
			owner = StatCollector.translateToLocal("dh.desc.defaultOwner");
		}
		l.add(StatCollector.translateToLocalFormatted("dh.desc.owner", owner));
		l.add(StatCollector.translateToLocal("dh.desc.hallow"));
	}

}
