package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.entity.EntityGoblin;
import com.emoniph.witchery.util.ChatUtil;
import com.pyding.deathlyhallows.common.handler.ConfigHandler;
import com.pyding.deathlyhallows.common.properties.ExtendedPlayer;
import com.pyding.deathlyhallows.entity.AbsoluteDeath;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class HobgoblinChains extends Item {
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        double radius = 2;
        List entities = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(player.posX-radius,player.posY-radius,player.posZ-radius,player.posX+radius,player.posY+radius,player.posZ+radius));
        if(entities != null){
            for(Object o: entities){
                if(o instanceof EntityGoblin){
                    EntityGoblin goblin = (EntityGoblin) o;
                    player.swingItem();
                    player.inventory.consumeInventoryItem(player.getHeldItem().getItem());
                    goblin.getEntityData().setDouble("chainX",goblin.posX);
                    goblin.getEntityData().setDouble("chainY",goblin.posY);
                    goblin.getEntityData().setDouble("chainZ",goblin.posZ);
                    if(ConfigHandler.hob)
                        goblin.getEntityData().setBoolean("immortal",true);
                    break;
                }
                if(o instanceof AbsoluteDeath){
                    if(!player.worldObj.isRemote)
                    ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "dh.chat.freak", new Object[0]);
                }
            }
        }
        return super.onItemRightClick(stack,world,player);
    }


    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        if (I18n.format("dh.util.language").equals("Ru")) {
            list.add("Привязывают Хобгоблина к зоне 16х16 от его текущей позиции навсегда и делают его бессертным");
        } else {
            list.add("Binds Hobgoblin to box 16x16 blocks from its current position forever and makes him immortal");
        }
    }
}
