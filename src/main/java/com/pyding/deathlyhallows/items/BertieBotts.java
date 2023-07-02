package com.pyding.deathlyhallows.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.util.Arrays;

public class BertieBotts extends ItemFood {
    public BertieBotts(int hunger, float saturation) {
        super(hunger, saturation, true);
    }
    public static int blackList[] = {0,28,27};

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        this.setAlwaysEdible();
        return super.onItemRightClick(stack,world,player);
    }

    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
        super.onEaten(stack, world, player);
        addBuff(player,world);
        return stack;
    }
    public void addBuff(EntityPlayer player,World world){
        if(!world.isRemote){
            int bounds = 126;
            int random = itemRand.nextInt(bounds)+1;
            int random2 = itemRand.nextInt(4)+1;
            try {
                while(isBlackListed(random)) {
                    random = itemRand.nextInt(bounds) + 1;
                }
                Potion potion = Potion.potionTypes[random];
                if (potion != null)
                    player.addPotionEffect(new PotionEffect(random,random2*600,random2));
                else {
                    blackList = addElement(blackList,random);
                    addBuff(player,world);
                }
            } catch (NullPointerException e) {
                System.out.println(e.getStackTrace());
            }
        }
    }
    public static boolean isBlackListed(int random){
        for (int i = 0; i < blackList.length;i++){
            if(random == blackList[i])
                return true;
        }
        return false;
    }

    public static int[] addElement(int[] array, int value) {
        int[] newArray = Arrays.copyOf(array, array.length + 1);
        newArray[newArray.length - 1] = value;
        return newArray;
    }
}
