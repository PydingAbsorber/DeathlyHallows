package com.pyding.deathlyhallows.render.entity;

import com.emoniph.witchery.client.model.ModelBroom;
import com.pyding.deathlyhallows.entities.EntityNimbus;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderNimbus extends Render {

	private static final ResourceLocation TEXTURE_URL = new ResourceLocation(DHIntegration.WITCHERY, "textures/entities/Broom.png");
	protected ModelBase modelBroom = new ModelBroom();
	
	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return this.func_110832_a((EntityNimbus)par1Entity);
	}

	public RenderNimbus() {
		super.shadowSize = 0.5F;
	}

	protected ResourceLocation func_110832_a(EntityNimbus e) {
		return TEXTURE_URL;
	}

	public void renderBroom(EntityNimbus nimbus, double x, double y, double z, float yaw, float partial) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y + 1.0F, (float)z);
		GL11.glRotatef(90.0F - yaw, 0.0F, 1.0F, 0.0F);
		float f4 = 0.75F;
		GL11.glScalef(f4, f4, f4);
		GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);
		this.bindEntityTexture(nimbus);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		this.modelBroom.render(nimbus, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

	public void doRender(Entity par1Entity, double x, double y, double z, float yaw, float partial) {
		this.renderBroom((EntityNimbus)par1Entity, x, y, z, yaw, partial);
	}

}
