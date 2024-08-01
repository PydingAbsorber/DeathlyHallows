package com.pyding.deathlyhallows.particles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;
import java.util.Queue;

@SideOnly(Side.CLIENT)
public class GenericBlock extends EntityFX {
	private static final ResourceLocation particleTexture = new ResourceLocation("dh", "textures/particles/generic_block.png");
	public static Queue<GenericBlock> queuedRenders = new ArrayDeque();
	public static Queue<GenericBlock> queuedDepthIgnoringRenders = new ArrayDeque();
	float f;
	float f1;
	float f2;
	float f3;
	float f4;
	float f5;

	public GenericBlock(World world, double x, double y, double z) {
		super(world, x, y, z);
		super.noClip = true;
		super.particleMaxAge = 2;
	}

	@Override
	public void onUpdate() {
		if(!super.worldObj.isRemote || super.particleAge >= 2) {
			this.setDead();
		}
		super.motionX = 0;
		super.motionY = 0;
		super.motionZ = 0;
	}

	public static void dispatchQueuedRenders(Tessellator tessellator) {
		Minecraft.getMinecraft().renderEngine.bindTexture(particleTexture);

		if(!queuedRenders.isEmpty()) {
			tessellator.startDrawingQuads();
			for(GenericBlock genericBlock: queuedRenders) {
				genericBlock.renderQueued(tessellator, true);
			}
			tessellator.draw();
		}

		if(!queuedDepthIgnoringRenders.isEmpty()) {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			tessellator.startDrawingQuads();
			for(GenericBlock genericBlock: queuedDepthIgnoringRenders) {
				genericBlock.renderQueued(tessellator, false);
			}
			tessellator.draw();
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}

		queuedRenders.clear();
		queuedDepthIgnoringRenders.clear();
	}

	private void renderQueued(Tessellator tessellator, boolean depthEnabled) {
		float f10 = 1F;
		float f11 = (float)posX;
		float f12 = (float)posY;
		float f13 = (float)posZ;

		tessellator.setBrightness(240);
		tessellator.setColorRGBA_F(particleRed, particleGreen, particleBlue, 0.5F);
		tessellator.addVertexWithUV(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10, 0, 1);
		tessellator.addVertexWithUV(f11 - f1 * f10 + f4 * f10, f12 + f2 * f10, f13 - f3 * f10 + f5 * f10, 1, 1);
		tessellator.addVertexWithUV(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10, 1, 0);
		tessellator.addVertexWithUV(f11 + f1 * f10 - f4 * f10, f12 - f2 * f10, f13 + f3 * f10 - f5 * f10, 0, 0);
	}

	public void renderParticle(Tessellator tess, float partialTicks, float par3, float par4, float par5, float par6, float par7) {

		this.f3 = f3;
		this.f4 = f4;
		this.f5 = f5;

		queuedRenders.add(this);
	}
}
