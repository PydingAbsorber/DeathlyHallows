package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.entity.EntityGoblin;
import com.emoniph.witchery.util.ChatUtil;
import com.pyding.deathlyhallows.entities.EntityAbsoluteDeath;
import com.pyding.deathlyhallows.utils.DHConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class HobgoblinChains extends Item {
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		double radius = 2;
		List entities = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(player.posX - radius, player.posY - radius, player.posZ - radius, player.posX + radius, player.posY + radius, player.posZ + radius));
		if(entities != null) {
			for(Object o: entities) {
				if(o instanceof EntityGoblin) {
					EntityGoblin goblin = (EntityGoblin)o;
					player.swingItem();
					player.inventory.consumeInventoryItem(player.getHeldItem().getItem());
					goblin.getEntityData().setDouble("chainX", goblin.posX);
					goblin.getEntityData().setDouble("chainY", goblin.posY);
					goblin.getEntityData().setDouble("chainZ", goblin.posZ);
					if(DHConfig.hob) {
						goblin.getEntityData().setBoolean("immortal", true);
					}
					break;
				}
				if(o instanceof EntityAbsoluteDeath) {
					if(!player.worldObj.isRemote) {
						ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "dh.chat.freak");
					}
				}
			}
		}
		return super.onItemRightClick(stack, world, player);
	}


	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
		list.add(StatCollector.translateToLocal("dh.desc.hobChains"));
	}
}
