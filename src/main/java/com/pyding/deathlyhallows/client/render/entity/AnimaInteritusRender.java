package com.pyding.deathlyhallows.client.render.entity;

import com.pyding.deathlyhallows.client.render.item.ModelWrapperDisplayList;
import com.pyding.deathlyhallows.common.properties.ExtendedPlayer;
import com.pyding.deathlyhallows.entity.AbsoluteDeath;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class AnimaInteritusRender extends RenderPlayer {
    private static final ResourceLocation anima = new ResourceLocation("dh", "textures/particles/anima.png");
    private static final ResourceLocation anima2 = new ResourceLocation("dh", "textures/particles/anima2.png");
    private final ResourceLocation modelPath = new ResourceLocation("dh", "models/anima.obj");
    private final IModelCustom curseModel = new ModelWrapperDisplayList((WavefrontObject) AdvancedModelLoader.loadModel(modelPath));
    private final ResourceLocation modelPath2 = new ResourceLocation("dh", "models/anima2.obj");
    private final IModelCustom curseModel2 = new ModelWrapperDisplayList((WavefrontObject) AdvancedModelLoader.loadModel(modelPath2));

    @Override
    public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
        super.doRender(entity, par2, par4, par6, par8, par9);
        NBTTagCompound nbt = entity.getEntityData().getCompoundTag("dhRenderData");
        int curse = nbt.getInteger("dhcurse");
        if(curse > 0){
            GL11.glPushMatrix();
            if(curse > 200){
                bindTexture(anima);
                GL11.glTranslatef((float) par2, (float) par4 + 0.5F, (float) par6);
                curseModel.renderAll();
            } else {
                bindTexture(anima2);
                GL11.glTranslatef((float) par2, (float) par4 + 0.5F, (float) par6);
                curseModel2.renderAll();
            }
            GL11.glPopMatrix();
        }
        /*List entities = entity.worldObj.getEntitiesWithinAABB(EntityLiving.class,entity.boundingBox.expand(64,64,64));
        for (Object o: entities){
            EntityLiving mob = (EntityLiving) o;

        }*/
    }
}
