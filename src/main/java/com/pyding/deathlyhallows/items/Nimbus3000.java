package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.InfusedBrewEffect;
import com.pyding.deathlyhallows.entities.EntityNimbus;
import com.pyding.deathlyhallows.utils.DHKeys;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class Nimbus3000 extends Item {
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(!world.isRemote) {
			NBTTagCompound nbt = null;
			if(!stack.hasTagCompound() || stack.getTagCompound() == null) {
				nbt = new NBTTagCompound();
			}
			else {
				nbt = stack.getTagCompound();
			}
			if(nbt.getLong("NimbusCooldown") >= System.currentTimeMillis()) {
				return super.onItemRightClick(stack, world, player);
			}
			if(nbt.getLong("NimbusDuration") <= System.currentTimeMillis()) {
				nbt.setLong("NimbusDuration", (long)(160 * 1000 * lifeModifier(player)) + System.currentTimeMillis());
				EntityNimbus nimbus = new EntityNimbus(world);
				//nimbus.setBrushColor(666);
				nimbus.setPosition(player.posX, player.posY, player.posZ);
				world.spawnEntityInWorld(nimbus);
				nimbus.interactFirst(player);
			}
			else {
				if(player.ridingEntity != null) {
					Entity entity = player.ridingEntity;
					entity.setDead();
					if(stack.getTagCompound().getLong("NimbusDuration") > System.currentTimeMillis()) {
						stack.getTagCompound()
							 .setLong("NimbusCooldown", (long)(System.currentTimeMillis() + (System.currentTimeMillis() - (stack.getTagCompound()
																																.getLong("NimbusDuration") - 160 * 1000 * lifeModifier(player)
							 )
							 ) / 2
							 ));
					}
					else {
						stack.getTagCompound()
							 .setLong("NimbusCooldown", (long)(System.currentTimeMillis() + (160 * 1000 * lifeModifier(player)) / 2));
					}
					nbt.setLong("NimbusDuration", 0);
				}
			}
			stack.setTagCompound(nbt);
		}
		return super.onItemRightClick(stack, world, player);
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

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
		long cd = 0;
		long duration = 0;
		if(stack.getTagCompound() != null) {
			cd = stack.getTagCompound().getLong("NimbusCooldown");
			duration = stack.getTagCompound().getLong("NimbusDuration");
		}
		if(cd > System.currentTimeMillis()) {
			list.add(StatCollector.translateToLocalFormatted("dh.desc.broom0", (cd - System.currentTimeMillis()) / 1000));
		}
		if((duration - System.currentTimeMillis()) > 0) {
			list.add(StatCollector.translateToLocalFormatted("dh.desc.broom1", (duration - System.currentTimeMillis()) / 1000));
		}
		list.add(StatCollector.translateToLocalFormatted("dh.desc.broom2", Keyboard.getKeyName(DHKeys.BROOM.getKeyCode())));
		list.add(StatCollector.translateToLocal("dh.desc.broom3"));
	}

	public boolean hasEffect(final ItemStack par1ItemStack, final int pass) {
		return true;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {
		return 1;
	}
}
