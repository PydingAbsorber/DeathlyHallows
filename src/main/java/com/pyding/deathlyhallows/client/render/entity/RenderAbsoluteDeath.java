package com.pyding.deathlyhallows.client.render.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.client.RenderEntityViewer;
import com.emoniph.witchery.client.model.ModelDeath;
import com.emoniph.witchery.client.renderer.RenderInfusionEnergyBar;
import com.emoniph.witchery.entity.EntityDeath;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.util.Config;
import com.pyding.deathlyhallows.DeathHallowsMod;
import com.pyding.deathlyhallows.client.render.item.ModelWrapperDisplayList;
import com.pyding.deathlyhallows.entity.AbsoluteDeath;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.UUID;

import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;
import static com.emoniph.witchery.client.PlayerRender.drawString;
import static com.emoniph.witchery.client.PlayerRender.getStringWidth;
import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class RenderAbsoluteDeath extends RenderLiving {
    private float modelRotation;

    private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/entities/death.png");

    private static final ResourceLocation absolute = new ResourceLocation("dh", "textures/particles/absolute_block.png");
    private static final ResourceLocation earth = new ResourceLocation("dh", "textures/particles/earth_block.png");
    private static final ResourceLocation fire = new ResourceLocation("dh", "textures/particles/fire_block.png");
    private static final ResourceLocation generic = new ResourceLocation("dh", "textures/particles/generic_block.png");
    private static final ResourceLocation magic = new ResourceLocation("dh", "textures/particles/magic_block.png");
    private static final ResourceLocation poison = new ResourceLocation("dh", "textures/particles/poison_block.png");
    private static final ResourceLocation dark = new ResourceLocation("dh", "textures/particles/void_block.png");
    private static final ResourceLocation water = new ResourceLocation("dh", "textures/particles/water_block.png");

    private final ResourceLocation modelPath = new ResourceLocation("dh", "models/generic_block.obj");
    private final IModelCustom shieldModel = new ModelWrapperDisplayList((WavefrontObject) AdvancedModelLoader.loadModel(modelPath));
    public RenderAbsoluteDeath() {
        super(new ModelDeath(), 0.5F);
    }
    public void doRenderDeath(AbsoluteDeath entity, double par2, double par4, double par6, float par8, float par9) {
        super.doRender(entity, par2, par4, par6, par8, par9);
        BossStatus.setBossStatus(entity, true);
        modelRotation += 1.0F;
        int shieldAmount = 100;
        NBTTagCompound nbt = entity.getEntityData().getCompoundTag("dhData");
        if(nbt.getInteger("absoluteblock") >= shieldAmount){
            GL11.glPushMatrix();
            bindTexture(absolute);
            GL11.glTranslatef((float) par2 + 1, (float) par4 + 0.5F, (float) par6);
            GL11.glRotatef(modelRotation,0.0F,1.0F,0.0F);
            shieldModel.renderAll();
            GL11.glPopMatrix();
        }
        if(nbt.getInteger("earthblock") >= shieldAmount){
            GL11.glPushMatrix();
            bindTexture(earth);
            GL11.glTranslatef((float) par2 - 1, (float) par4 + 0.5F, (float) par6);
            GL11.glRotatef(modelRotation,0.0F,1.0F,0.0F);
            shieldModel.renderAll();
            GL11.glPopMatrix();
        }
        if(nbt.getInteger("fireblock") >= shieldAmount){
            GL11.glPushMatrix();
            bindTexture(fire);
            GL11.glTranslatef((float) par2 + 1, (float) par4 + 0.5F, (float) par6 + 1);
            GL11.glRotatef(modelRotation,0.0F,1.0F,0.0F);
            shieldModel.renderAll();
            GL11.glPopMatrix();
        }
        if(nbt.getInteger("genericblock") >= shieldAmount){
            GL11.glPushMatrix();
            bindTexture(generic);
            GL11.glTranslatef((float) par2 - 1, (float) par4 + 0.5F, (float) par6 + 1);
            GL11.glRotatef(modelRotation,0.0F,1.0F,0.0F);
            shieldModel.renderAll();
            GL11.glPopMatrix();
        }
        if(nbt.getInteger("magicblock") >= shieldAmount){
            GL11.glPushMatrix();
            bindTexture(magic);
            GL11.glTranslatef((float) par2 + 1, (float) par4 + 0.5F, (float) par6 - 1);
            GL11.glRotatef(modelRotation,0.0F,1.0F,0.0F);
            shieldModel.renderAll();
            GL11.glPopMatrix();
        }
        if(nbt.getInteger("witherblock") >= shieldAmount){
            GL11.glPushMatrix();
            bindTexture(poison);
            GL11.glTranslatef((float) par2 - 1, (float) par4 + 0.5F, (float) par6 - 1);
            GL11.glRotatef(modelRotation,0.0F,1.0F,0.0F);
            shieldModel.renderAll();
            GL11.glPopMatrix();
        }
        if(nbt.getInteger("voidblock") >= shieldAmount){
            GL11.glPushMatrix();
            bindTexture(dark);
            GL11.glTranslatef((float) par2, (float) par4 + 0.5F, (float) par6 + 1);
            GL11.glRotatef(modelRotation,0.0F,1.0F,0.0F);
            shieldModel.renderAll();
            GL11.glPopMatrix();
        }
        if(nbt.getInteger("waterblock") >= shieldAmount){
            GL11.glPushMatrix();
            bindTexture(water);
            GL11.glTranslatef((float) par2, (float) par4 + 0.5F, (float) par6 - 1);
            GL11.glRotatef(modelRotation,0.0F,1.0F,0.0F);
            shieldModel.renderAll();
            GL11.glPopMatrix();
        }
    }

    public void doRender(EntityLiving entity, double par2, double par4, double par6, float par8, float par9) {
        this.doRenderDeath((AbsoluteDeath)entity, par2, par4, par6, par8, par9);
    }

    protected ResourceLocation getEntityTexture(Entity par1Entity) {
        return this.func_110832_a((AbsoluteDeath)par1Entity);
    }

    protected ResourceLocation func_110832_a(AbsoluteDeath par1Entity) {
        return TEXTURE_URL;
    }

}

