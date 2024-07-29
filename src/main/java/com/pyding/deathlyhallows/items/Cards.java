package com.pyding.deathlyhallows.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Cards extends Item {
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		//DHUtil.spawnSphere(player,player.getPosition(1),30, 5, Color.BLUE,1,2,60,1);
		/*double radius = 4;
		List entities = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(player.posX - radius, player.posY - radius, player.posZ - radius, player.posX + radius, player.posY + radius, player.posZ + radius));
		if(entities != null) {
			for(Object o: entities) {
				if(o instanceof EntityPlayer) {
					EntityPlayer player2 = (EntityPlayer)o;
					player.swingItem();
				}
				if(o instanceof AbsoluteDeath) {
					if(!player.worldObj.isRemote) {
						player.addChatMessage(new ChatComponentText("You haven't deserve yet"));
					}
				}
			}
		}
		player.openGui(DeathHallowsMod.Instance, 1, world, (int)player.posX, (int)player.posY, (int)player.posZ);*/
		return super.onItemRightClick(stack, world, player);
	}
}
