package com.pyding.deathlyhallows.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemTarotCards extends ItemBase {
	
	// that not exactly tartos; thats Chocolate Frog Card or Famous Witches and Wizards Cards)
	public ItemTarotCards() {
		super("tarotCards", 1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		/*
		DHUtils.spawnSphere(p,p.getPosition(1),30, 5, Color.BLUE,1,2,60,1);
		List<EntityLivingBase> entities = DHUtils.getEntitiesAround(EntityLivingBase.class, p, 4F);
		if(entities != null) {
			for(EntityLivingBase o: entities) {
				if(o instanceof EntityPlayer) {
					p.swingItem();
				}
				if(o instanceof EntityAbsoluteDeath && !p.worldObj.isRemote) {
					p.addChatMessage(new ChatComponentText("You haven't deserve yet"));
				}
			}
		}
		p.openGui(DeathlyHallows.Instance, 1, world, (int)p.posX, (int)p.posY, (int)p.posZ);
		*/
		return super.onItemRightClick(stack, world, p);
	}
}
