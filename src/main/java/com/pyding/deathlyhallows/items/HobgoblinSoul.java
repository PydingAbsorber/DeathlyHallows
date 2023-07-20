package com.pyding.deathlyhallows.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class HobgoblinSoul extends Item {
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        if (I18n.format("dh.util.language").equals("Ru")) {
            list.add("Редко выпадает с хобгоблинов");
        } else {
            list.add("Rarely drops from hobgoblins");
        }
    }
}
