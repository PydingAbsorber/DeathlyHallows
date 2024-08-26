package com.pyding.deathlyhallows.render.entity;

import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.render.entity.model.ModelNimbus;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class RenderNimbus extends Render {

	private static final ResourceLocation TEXTURE = new ResourceLocation(DeathlyHallows.MODID, "textures/models/nimbus.png");
	protected ModelBase broom = new ModelNimbus();
	
	public RenderNimbus() {
		shadowSize = 0.5F;
	}

	@Override
	public void doRender(Entity e, double x, double y, double z, float yaw, float partial) {
		glPushMatrix();
		glTranslated(x, y + 1.0D, z);
		yaw = interpolateRotation(e.prevRotationYaw, e.rotationYaw, partial);
		glRotatef(180F - yaw, 0.0F, 1.0F, 0.0F);
		bindEntityTexture(e);
		glScalef(-1.0F, -1.0F, 1.0F);
		broom.render(e, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		glPopMatrix();
	}

	private float interpolateRotation(float from, float to, float by) {
		return (from + MathHelper.wrapAngleTo180_float(to - from) * by) % 360.0f;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return TEXTURE;
	}

}
