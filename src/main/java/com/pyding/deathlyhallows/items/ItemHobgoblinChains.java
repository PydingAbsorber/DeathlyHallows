package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.entity.EntityGoblin;
import com.emoniph.witchery.util.ChatUtil;
import com.pyding.deathlyhallows.entities.EntityAbsoluteDeath;
import com.pyding.deathlyhallows.utils.DHConfig;
import com.pyding.deathlyhallows.utils.DHUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemHobgoblinChains extends ItemBase {

	public ItemHobgoblinChains() {
		super("hobgoblinChains", 64);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		List<EntityCreature> entities = DHUtils.getEntitiesAround(EntityCreature.class, p, 2);
		if(entities == null) {
			return super.onItemRightClick(stack, world, p);
		}
		for(EntityCreature laCreatura: entities) {
			if(laCreatura instanceof EntityAbsoluteDeath && !p.worldObj.isRemote) {
				ChatUtil.sendTranslated(EnumChatFormatting.RED, p, "dh.chat.freak");
				continue;
			}
			if(!(laCreatura instanceof EntityGoblin)) {
				continue;
			}
			EntityGoblin goboo = (EntityGoblin)laCreatura;
			p.swingItem();
			p.inventory.consumeInventoryItem(p.getHeldItem().getItem());
			NBTTagCompound tag = goboo.getEntityData();
			tag.setDouble("chainX", goboo.posX);
			tag.setDouble("chainY", goboo.posY);
			tag.setDouble("chainZ", goboo.posZ);
			if(DHConfig.hob) {
				tag.setBoolean("immortal", true);
			}
			break;
		}
		return super.onItemRightClick(stack, world, p);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		l.add(StatCollector.translateToLocal("dh.desc.hobChains"));
	}
	
}
