package com.pyding.deathlyhallows.client.render.entity;

import com.pyding.deathlyhallows.client.render.item.ModelWrapperDisplayList;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

public class AnimaInteritusMobRender extends RenderLiving {
    private static final ResourceLocation anima = new ResourceLocation("dh", "textures/particles/anima.png");
    private static final ResourceLocation anima2 = new ResourceLocation("dh", "textures/particles/anima2.png");
    private final ResourceLocation modelPath = new ResourceLocation("dh", "models/anima.obj");
    private final IModelCustom curseModel = new ModelWrapperDisplayList((WavefrontObject) AdvancedModelLoader.loadModel(modelPath));
    private final ResourceLocation modelPath2 = new ResourceLocation("dh", "models/anima2.obj");
    private final IModelCustom curseModel2 = new ModelWrapperDisplayList((WavefrontObject) AdvancedModelLoader.loadModel(modelPath2));

    public AnimaInteritusMobRender() {
        super(null, 0);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return null;
    }

    @Override
    public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
        super.doRender(entity, par2, par4, par6, par8, par9);
        NBTTagCompound mobNbt = entity.getEntityData().getCompoundTag("dhData");
        int curse2 = mobNbt.getInteger("dhcurse");
        System.out.println(curse2);
        if (curse2 > 0) {
            GL11.glPushMatrix();
            if (curse2 > 200) {
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
    }
}
