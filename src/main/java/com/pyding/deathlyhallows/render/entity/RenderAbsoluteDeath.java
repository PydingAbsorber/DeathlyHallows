package com.pyding.deathlyhallows.render.entity;

import com.emoniph.witchery.client.model.ModelDeath;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.entities.EntityAbsoluteDeath;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.render.ModelWrapperDisplayList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderAbsoluteDeath extends RenderLiving {
	private float modelRotation;
	private static final ResourceLocation
			modelPath = new ResourceLocation(DeathlyHallows.MODID, "models/generic_block.obj"),
			TEXTURE_URL = new ResourceLocation(DHIntegration.WITCHERY, "textures/entities/death.png"),
			absolute = new ResourceLocation(DeathlyHallows.MODID, "textures/particles/absolute_block.png"),
			earth = new ResourceLocation(DeathlyHallows.MODID, "textures/particles/earth_block.png"),
			fire = new ResourceLocation(DeathlyHallows.MODID, "textures/particles/fire_block.png"),
			generic = new ResourceLocation(DeathlyHallows.MODID, "textures/particles/generic_block.png"),
			magic = new ResourceLocation(DeathlyHallows.MODID, "textures/particles/magic_block.png"),
			poison = new ResourceLocation(DeathlyHallows.MODID, "textures/particles/poison_block.png"),
			dark = new ResourceLocation(DeathlyHallows.MODID, "textures/particles/void_block.png"),
			water = new ResourceLocation(DeathlyHallows.MODID, "textures/particles/water_block.png");
	private final IModelCustom shieldModel = new ModelWrapperDisplayList((WavefrontObject)AdvancedModelLoader.loadModel(modelPath));

	public RenderAbsoluteDeath() {
		super(new ModelDeath(), 0.5F);
	}

	public void doRenderDeath(EntityAbsoluteDeath entity, double x, double y, double z, float yaw, float partial) {
		super.doRender(entity, x, y, z, yaw, partial);
		BossStatus.setBossStatus(entity, true);
		modelRotation += 1.0F;
		int shieldAmount = 100;
		NBTTagCompound nbt = entity.getEntityData().getCompoundTag("dhData");
		if(nbt.getInteger("absoluteblock") >= shieldAmount) {
			GL11.glPushMatrix();
			bindTexture(absolute);
			GL11.glTranslatef((float)x + 1, (float)y + 0.5F, (float)z);
			GL11.glRotatef(modelRotation, 0.0F, 1.0F, 0.0F);
			shieldModel.renderAll();
			GL11.glPopMatrix();
		}
		if(nbt.getInteger("earthblock") >= shieldAmount) {
			GL11.glPushMatrix();
			bindTexture(earth);
			GL11.glTranslatef((float)x - 1, (float)y + 0.5F, (float)z);
			GL11.glRotatef(modelRotation, 0.0F, 1.0F, 0.0F);
			shieldModel.renderAll();
			GL11.glPopMatrix();
		}
		if(nbt.getInteger("fireblock") >= shieldAmount) {
			GL11.glPushMatrix();
			bindTexture(fire);
			GL11.glTranslatef((float)x + 1, (float)y + 0.5F, (float)z + 1);
			GL11.glRotatef(modelRotation, 0.0F, 1.0F, 0.0F);
			shieldModel.renderAll();
			GL11.glPopMatrix();
		}
		if(nbt.getInteger("genericblock") >= shieldAmount) {
			GL11.glPushMatrix();
			bindTexture(generic);
			GL11.glTranslatef((float)x - 1, (float)y + 0.5F, (float)z + 1);
			GL11.glRotatef(modelRotation, 0.0F, 1.0F, 0.0F);
			shieldModel.renderAll();
			GL11.glPopMatrix();
		}
		if(nbt.getInteger("magicblock") >= shieldAmount) {
			GL11.glPushMatrix();
			bindTexture(magic);
			GL11.glTranslatef((float)x + 1, (float)y + 0.5F, (float)z - 1);
			GL11.glRotatef(modelRotation, 0.0F, 1.0F, 0.0F);
			shieldModel.renderAll();
			GL11.glPopMatrix();
		}
		if(nbt.getInteger("witherblock") >= shieldAmount) {
			GL11.glPushMatrix();
			bindTexture(poison);
			GL11.glTranslatef((float)x - 1, (float)y + 0.5F, (float)z - 1);
			GL11.glRotatef(modelRotation, 0.0F, 1.0F, 0.0F);
			shieldModel.renderAll();
			GL11.glPopMatrix();
		}
		if(nbt.getInteger("voidblock") >= shieldAmount) {
			GL11.glPushMatrix();
			bindTexture(dark);
			GL11.glTranslatef((float)x, (float)y + 0.5F, (float)z + 1);
			GL11.glRotatef(modelRotation, 0.0F, 1.0F, 0.0F);
			shieldModel.renderAll();
			GL11.glPopMatrix();
		}
		if(nbt.getInteger("waterblock") >= shieldAmount) {
			GL11.glPushMatrix();
			bindTexture(water);
			GL11.glTranslatef((float)x, (float)y + 0.5F, (float)z - 1);
			GL11.glRotatef(modelRotation, 0.0F, 1.0F, 0.0F);
			shieldModel.renderAll();
			GL11.glPopMatrix();
		}
	}

	public void doRender(EntityLiving entity, double x, double y, double z, float yaw, float partial) {
		this.doRenderDeath((EntityAbsoluteDeath)entity, x, y, z, yaw, partial);
	}

	protected ResourceLocation getEntityTexture(Entity e) {
		return TEXTURE_URL;
	}
	
}

