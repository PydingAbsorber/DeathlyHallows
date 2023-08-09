package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.InfusedBrewEffect;
import com.pyding.deathlyhallows.client.handler.KeyHandler;
import com.pyding.deathlyhallows.entity.Nimbus;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class Nimbus3000 extends Item {
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if(!world.isRemote) {
            NBTTagCompound nbt = null;
            if (!stack.hasTagCompound() || stack.getTagCompound() == null) {
                nbt = new NBTTagCompound();
            } else nbt = stack.getTagCompound();
            if (nbt.getInteger("NimbusCooldown") == 0) {
                nbt.setInteger("NimbusCooldown", 4000);
                Nimbus nimbus = new Nimbus(world);
                nimbus.setBrushColor(666);
                nimbus.setPosition(player.posX, player.posY, player.posZ);
                world.spawnEntityInWorld(nimbus);
                nimbus.interactFirst(player);
            } else {
                if (player.ridingEntity != null) {
                    Entity entity = player.ridingEntity;
                    entity.setDead();
                    nbt.setInteger("NimbusCooldown", 0);
                }
            }
            stack.setTagCompound(nbt);
            this.riderHasOwlFamiliar = Familiar.hasActiveBroomMasteryFamiliar(player);
            this.riderHasSoaringBrew = InfusedBrewEffect.Soaring.isActive(player);
        }
        return super.onItemRightClick(stack, world, player);
    }
    boolean riderHasOwlFamiliar;
    boolean riderHasSoaringBrew;
    public float lifeModifier(){
        float modifier = 1;
        if(riderHasOwlFamiliar)
            modifier += 0.5;
        if(riderHasSoaringBrew)
            modifier += 0.2;
        return modifier;
    }
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        int cd = 0;
        if(stack.getTagCompound() != null)
        cd = stack.getTagCompound().getInteger("NimbusCooldown");
        if(cd > 0) {
            if (I18n.format("dh.util.language").equals("Ru")) {
                list.add("Осталось сил на: §e" + (int)cd/20*lifeModifier() + " §rсекунд");
            } else {
                list.add("Powers remain for : §e" + (int)cd/20*lifeModifier() + " §rseconds more");
            }
        }
        if (I18n.format("dh.util.language").equals("Ru")) {
            list.add("Можно нажать на клавишу для быстрого призыва");
            list.add("Получается при помощи ритуала ,,Охотник за облаками,,");
        } else {
            list.add("You can press the button for quick summon");
            list.add("Can be obtained through ritual ,,Cloud chaser,,");
        }
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 1;
    }
}
