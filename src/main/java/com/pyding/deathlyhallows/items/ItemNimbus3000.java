package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.InfusedBrewEffect;
import com.pyding.deathlyhallows.entities.EntityNimbus;
import com.pyding.deathlyhallows.utils.DHKeys;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemNimbus3000 extends ItemBase {
	
	public ItemNimbus3000() {
		super("nimbus3000", 1);
	}

	// TODO remove currentTimeMillis in favor of tickExisted
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		if(world.isRemote) {
			return super.onItemRightClick(stack, world, p);
		}
		NBTTagCompound tag;
		if(!stack.hasTagCompound() || stack.getTagCompound() == null) {
			tag = new NBTTagCompound();
		}
		else {
			tag = stack.getTagCompound();
		}
		if(tag.getLong("NimbusCooldown") >= System.currentTimeMillis()) {
			return super.onItemRightClick(stack, world, p);
		}
		if(tag.getLong("NimbusDuration") <= System.currentTimeMillis()) {
			tag.setLong("NimbusDuration", (long)(160 * 1000 * lifeModifier(p)) + System.currentTimeMillis());
			EntityNimbus nimbus = new EntityNimbus(world);
			nimbus.setPosition(p.posX, p.posY, p.posZ);
			world.spawnEntityInWorld(nimbus);
			nimbus.interactFirst(p);
		}
		else {
			if(p.ridingEntity != null) {
				Entity entity = p.ridingEntity;
				entity.setDead();
				if(stack.getTagCompound().getLong("NimbusDuration") > System.currentTimeMillis()) {
					stack.getTagCompound().setLong("NimbusCooldown", (long)(System.currentTimeMillis() + (System.currentTimeMillis() - (stack.getTagCompound().getLong("NimbusDuration") - 160 * 1000 * lifeModifier(p))) / 2));
				}
				else {
					stack.getTagCompound().setLong("NimbusCooldown", (long)(System.currentTimeMillis() + (160 * 1000 * lifeModifier(p)) / 2));
				}
				tag.setLong("NimbusDuration", 0);
			}
		}
		stack.setTagCompound(tag);
		return super.onItemRightClick(stack, world, p);
	}


	public float lifeModifier(EntityPlayer player) {
		float modifier = 1;
		if(Familiar.hasActiveBroomMasteryFamiliar(player)) {
			modifier += 0.7f;
		}
		if(InfusedBrewEffect.Soaring.isActive(player)) {
			modifier += 0.6f;
		}
		return modifier;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		long cd = 0;
		long duration = 0;
		if(stack.getTagCompound() != null) {
			cd = stack.getTagCompound().getLong("NimbusCooldown");
			duration = stack.getTagCompound().getLong("NimbusDuration");
		}
		if(cd > System.currentTimeMillis()) {
			l.add(StatCollector.translateToLocalFormatted("dh.desc.broom0", (cd - System.currentTimeMillis()) / 1000));
		}
		if((duration - System.currentTimeMillis()) > 0) {
			l.add(StatCollector.translateToLocalFormatted("dh.desc.broom1", (duration - System.currentTimeMillis()) / 1000));
		}
		l.add(StatCollector.translateToLocalFormatted("dh.desc.broom2", Keyboard.getKeyName(DHKeys.BROOM.getKeyCode())));
		l.add(StatCollector.translateToLocal("dh.desc.broom3"));
	}

	@Override
	public boolean hasEffect(final ItemStack stack, final int pass) {
		return true;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {
		return 1;
	}
	
}
