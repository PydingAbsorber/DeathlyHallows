package com.pyding.deathlyhallows.client.render.entity;

import com.pyding.deathlyhallows.entity.AbsoluteDeath;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;

public class BossRender extends RenderLiving {
    private static final ResourceLocation generic_block = new ResourceLocation("dh", "textures/particles/generic_block.png");

    public BossRender(ModelBase p_i1262_1_, float p_i1262_2_) {
        super(p_i1262_1_, p_i1262_2_);
    }

    @Override
    public void doRender(Entity entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        if(entity instanceof AbsoluteDeath){
            /*bindTexture(generic_block);
            drawTexturedModalRect(entity.serverPosX, entity.serverPosY+2, 0, 0, 64, 64);*/
        }
        super.doRender(entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return generic_block;
    }
}
