package com.pyding.deathlyhallows.client.render.block;

import com.emoniph.witchery.blocks.BlockCoffin;
import com.emoniph.witchery.client.model.ModelCoffin;
import com.emoniph.witchery.client.model.ModelDemonHeart;
import com.emoniph.witchery.client.model.ModelMysticBranch;
import com.pyding.deathlyhallows.blocks.VisConverterTile;
import com.pyding.deathlyhallows.client.render.item.ModelWrapperDisplayList;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

public class ViscRender extends TileEntitySpecialRenderer {
    final ModelDemonHeart heart = new ModelDemonHeart();
    final ModelMysticBranch branch = new ModelMysticBranch();
    private final ResourceLocation modelPath = new ResourceLocation("dh", "models/deathShard.obj");
    private final IModelCustom shard = new ModelWrapperDisplayList((WavefrontObject) AdvancedModelLoader.loadModel(modelPath));

    private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/blocks/demonHeart.png");
    private static final ResourceLocation TEXTURE_URL2 = new ResourceLocation("witchery", "textures/entities/mysticbranch.png");
    private static final ResourceLocation TEXTURE_URL3 = new ResourceLocation("dh", "textures/items/shard.png");
    public static int ticks;


    public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) {
        ticks++;
        GL11.glPushMatrix();
        this.bindTexture(TEXTURE_URL);
        GL11.glTranslated(d+0.6,d1-0.5,d2+0.8);
        GL11.glScalef(0.6F,0.6F,0.6F);
        this.heart.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, 0);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(d+0.6,d1-0.5,d2);
        GL11.glScalef(0.6F,0.6F,0.6F);
        this.heart.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, 0);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(d+0.1,d1-0.5,d2+0.4);
        GL11.glScalef(0.6F,0.6F,0.6F);
        this.heart.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, 0);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(d+1,d1-0.5,d2+0.4);
        GL11.glScalef(0.6F,0.6F,0.6F);
        this.heart.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, 0);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.bindTexture(TEXTURE_URL2);
        GL11.glTranslated(d,d1,d2);
        GL11.glRotatef(140, -1.0F, 0.0F, 1.0F);
        GL11.glScalef(1.2F,1.2F,1.2F);
        this.branch.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(d+1,d1,d2);
        GL11.glRotatef(140, -1.0F, 0.0F, -1.0F);
        GL11.glScalef(1.2F,1.2F,1.2F);
        this.branch.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(d+1,d1,d2+1);
        GL11.glRotatef(140, 1.0F, 0.0F, -1.0F);
        GL11.glScalef(1.2F,1.2F,1.2F);
        this.branch.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(d,d1,d2+1);
        GL11.glRotatef(140, 1.0F, 0.0F, 1.0F);
        GL11.glScalef(1.2F,1.2F,1.2F);
        this.branch.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        this.bindTexture(TEXTURE_URL3);
        GL11.glTranslated(d+0.5,d1+1,d2+0.5);
        GL11.glScalef(0.4F,0.4F,0.4F);
        GL11.glRotatef(ticks, 0.0F, 1.0F, 0.0F);
        shard.renderAll();
        GL11.glPopMatrix();
    }
}
