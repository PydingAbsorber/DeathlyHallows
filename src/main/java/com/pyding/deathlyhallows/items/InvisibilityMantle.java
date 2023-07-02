package com.pyding.deathlyhallows.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.common.properties.ExtendedPlayer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ibxm.Player;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class InvisibilityMantle extends Item implements IBauble {
    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.BELT;
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if(!entityLiving.worldObj.isRemote){
        }
        return super.onEntitySwing(entityLiving, stack);
    }

    @Override
    public void onWornTick(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        World world = entityLivingBase.worldObj;
        EntityPlayer player = (EntityPlayer) entityLivingBase;
        if(player.isSneaking())
        {
            if(player.getEntityData().getInteger("mantlecd") == 0)
            {
                player.getEntityData().setInteger("mantlecd",1200);
                player.getEntityData().setBoolean("mantleActive",true);
                if(Math.random() > 0.5)
                {
                    if(Math.random() > 0.5)
                        world.playSoundAtEntity(entityLivingBase,"dh:mantle.1",1F,1F);
                        else world.playSoundAtEntity(entityLivingBase,"dh:mantle.2",1F,1F);
                } else world.playSoundAtEntity(entityLivingBase, "dh:mantle.3", 1F, 1F);
            }
            if (player.getEntityData().getBoolean("mantleActive")){
                float yaw = player.rotationYaw;
                float pitch = player.rotationPitch;
                double speed = 0.7;
                double motionX = -Math.sin(Math.toRadians(yaw)) * speed;
                double motionZ = Math.cos(Math.toRadians(yaw)) * speed;
                double motionY = Math.sin(Math.toRadians(-pitch)) * speed;
                player.setVelocity(motionX, motionY, motionZ);
                player.noClip = true;
            } else player.noClip = false;
        }  else player.noClip = false;
    }

    @Override
    public void onEquipped(ItemStack itemStack, EntityLivingBase entityLivingBase) {

    }

    @Override
    public void onUnequipped(ItemStack itemStack, EntityLivingBase entityLivingBase) {

    }

    @Override
    public boolean canEquip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        String currentLanguage = StatCollector.translateToLocal("language.name");

        if (I18n.format("dh.util.language").equals("Ru")) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                list.add("Ты получаешь абсолютную невидимость и неуязвимость к атакам лишь на 50 секунд");
                list.add("С абсолютной невидимостью тебя не видят мобы, но твоя экипировка всё ещё видна");
                list.add("Абсолютная невидимость не может быть развеяна");
                list.add("Перезарядка способности 1 минута");
            } else {
                list.add("Нажми §9shift §7пока надето, чтобы получить невидимость и неуязвимость к атакам");
                list.add("Нажми §9shift §7пока активированна способность, чтобы войти в форму призрака");
                list.add("Нажми §9shift §7для дополнительной информации");
            }
            if(stack.hasTagCompound()){
                if(stack.getTagCompound().hasKey("dhowner")) {
                    list.add("Владелец §9" + stack.getTagCompound().getString("dhowner"));
                }
            } else list.add("Владелец §9Смерть");
            list.add("Возможно иметь лишь один дар у себя в инвентаре");
        } else {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                list.add("You gain absolute invisibility and attacks negation only for 50 seconds");
                list.add("With absolute invisibility mobs cant see or target you but tools still visible");
                list.add("Absolute invisibility cant be dispelled");
                list.add("Cooldown of the skill is 1 minute");
            } else {
                list.add("Press §9shift §7when worn to get invisibility and attacks negation");
                list.add("Press §9shift §7when ability is active to enter ghost form");
                list.add("Press §9shift §7for additional information");
            }
            if(stack.hasTagCompound()){
                if(stack.getTagCompound().hasKey("dhowner")) {
                    list.add("Owner §9" + stack.getTagCompound().getString("dhowner"));
                }
            } else list.add("Owner §9Death");
            list.add("You can have only one Hallow at the time");
        }
    }
}
