package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.util.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ViscousSecretions extends ItemFood {
    public ViscousSecretions() {
        super(20,40,true);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        this.setAlwaysEdible();
        return super.onItemRightClick(p_77659_1_, p_77659_2_, p_77659_3_);
    }

    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
        super.onEaten(stack, world, player);
        if(!world.isRemote)
        ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player,  "dh.chat.food", new Object[0]);
        player.getEntityData().setInteger("DopVoid",3000);
        return stack;
    }
}
