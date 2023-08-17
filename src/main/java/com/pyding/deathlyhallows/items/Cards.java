package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.entity.EntityGoblin;
import com.emoniph.witchery.util.ChatUtil;
import com.pyding.deathlyhallows.DeathHallowsMod;
import com.pyding.deathlyhallows.entity.AbsoluteDeath;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class Cards extends Item {
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        double radius = 4;
        List entities = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(player.posX-radius,player.posY-radius,player.posZ-radius,player.posX+radius,player.posY+radius,player.posZ+radius));
        if(entities != null){
            for(Object o: entities){
                if(o instanceof EntityPlayer){
                    EntityPlayer player2 = (EntityPlayer) o;
                    player.swingItem();
                }
                if(o instanceof AbsoluteDeath){
                    if(!player.worldObj.isRemote)
                        player.addChatMessage(new ChatComponentText("You haven't deserve yet"));
                }
            }
        }
        player.openGui(DeathHallowsMod.Instance,1,world,(int)player.posX,(int)player.posY,(int)player.posZ);
        return super.onItemRightClick(stack,world,player);
    }
}
