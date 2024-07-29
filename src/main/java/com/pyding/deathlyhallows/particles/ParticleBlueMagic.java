package com.pyding.deathlyhallows.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ParticleBlueMagic extends EntityFX {

	public float resizeRate;

	public static final ResourceLocation resource = new ResourceLocation("textures/particle/particles.png");

	public static ResourceLocation orbFx = new ResourceLocation("dh", "textures/particles/orbfx.png");


	public ParticleBlueMagic(World world, double d, double d1, double d2, Color color, float resizeSpeed, float scale, int age) {
		super(world, d, d1, d2, 0.0, 0.0, 0.0);
		this.particleScale = scale;
		this.particleMaxAge = age;
		this.setSize(0.01F, 0.01F);
		this.resizeRate = resizeSpeed;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.setRBGColorF((float)color.getRed() / 255, (float)color.getGreen() / 255, (float)color.getBlue() / 255);
	}

	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		float var12 = 0.1F * this.particleScale;
		float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f - interpPosX);
		float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f - interpPosY);
		float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f - interpPosZ);
		float var16 = 1.0F;
		tessellator.draw();
		GL11.glPushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(orbFx);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_LINES);
		tessellator.startDrawingQuads();
		tessellator.setBrightness(240);
		tessellator.setColorRGBA_F(this.particleRed * var16, this.particleGreen * var16, this.particleBlue * var16, 1.0F);
		tessellator.addVertexWithUV(var13 - f1 * var12 - f4 * var12, var14 - f2 * var12, var15 - f3 * var12 - f5 * var12, 1, 1);
		tessellator.addVertexWithUV(var13 - f1 * var12 + f4 * var12, var14 + f2 * var12, var15 - f3 * var12 + f5 * var12, 1, 0);
		tessellator.addVertexWithUV(var13 + f1 * var12 + f4 * var12, var14 + f2 * var12, var15 + f3 * var12 + f5 * var12, 0, 0);
		tessellator.addVertexWithUV(var13 + f1 * var12 - f4 * var12, var14 - f2 * var12, var15 + f3 * var12 - f5 * var12, 0, 1);
		tessellator.draw();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glPopMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(resource);
		tessellator.startDrawingQuads();
	}

	public int getFXLayer() {
		return 0;
	}

	public void onUpdate() {
		this.particleScale *= this.resizeRate;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setDead();
		}
	}
}
