package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.util.ChatUtil;
import com.pyding.deathlyhallows.common.properties.ExtendedPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class CreativeItem extends Item {
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if(!player.worldObj.isRemote) {
            ExtendedPlayer props = ExtendedPlayer.get(player);
            props.setAllNull();
            if(!player.isSneaking()){
                if(props.getElfLvl() < 10)
                    props.increaseElfLvl();
                else props.nullifyElfLvl();
            }
            else {
                if(props.getElfLvl() > 0)
                    props.decreaseElfLvl();
                else props.maxElfLvl();
            }
            if(I18n.format("dh.util.language").equals("Ru")){
                player.addChatComponentMessage(new ChatComponentText("§9Твой уровень Эльфа сейчас " + props.getElfLvl()));
            } else player.addChatComponentMessage(new ChatComponentText("§9Your Elf lvl is now " + props.getElfLvl()));
        }
        return super.onItemRightClick(stack, world, player);
    }
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        ExtendedPlayer props = ExtendedPlayer.get(player);
        if (player != null) {
            //list.add("You have§9 " + props.getElfLvl() + " Elf Lvl");
        }
    }
}
