package com.pyding.deathlyhallows.entity;

import com.emoniph.witchery.entity.EntityBroom;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.InfusedBrewEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class Nimbus extends EntityBroom {
    boolean riderHasOwlFamiliar;
    boolean riderHasSoaringBrew;
    public float speedModifier(){
        float modifier = 1;
        if(riderHasOwlFamiliar)
            modifier += 0.5;
        if(riderHasSoaringBrew)
            modifier += 0.2;
        return modifier;
    }
    public Nimbus(World world) {
        super(world);
        this.setSize(1.2F, 0.5F);
    }
    public void onUpdate() {
        if(ticksExisted > 12000*speedModifier())
        if(this.riddenByEntity != null){
            if(this.riddenByEntity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) this.riddenByEntity;
                float yaw = player.rotationYaw;
                float pitch = player.rotationPitch;
                double speed = 0.7 * speedModifier();
                if (player.isSprinting())
                    speed *= 2;
                double motionX = -Math.sin(Math.toRadians(yaw)) * speed;
                double motionZ = Math.cos(Math.toRadians(yaw)) * speed;
                double motionY = Math.sin(Math.toRadians(-pitch)) * speed;
                this.setVelocity(motionX, motionY, motionZ);
            }
        }
    }

    @Override
    public void setBrushColor(int color) {
        super.setBrushColor(color);
    }

    @Override
    public int getBrushColor() {
        return super.getBrushColor();
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        return false;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {

    }
    public boolean interactFirst(EntityPlayer player) {
        if (super.riddenByEntity == null || !(super.riddenByEntity instanceof EntityPlayer) || super.riddenByEntity == player) {
            if (!super.worldObj.isRemote) {
                this.riderHasOwlFamiliar = Familiar.hasActiveBroomMasteryFamiliar(player);
                this.riderHasSoaringBrew = InfusedBrewEffect.Soaring.isActive(player);
                player.mountEntity(this);
            }
        }
        return true;
    }
}
