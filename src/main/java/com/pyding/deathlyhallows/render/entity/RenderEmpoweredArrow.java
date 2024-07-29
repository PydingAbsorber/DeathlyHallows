package com.pyding.deathlyhallows.render.entity;

import com.pyding.deathlyhallows.DeathHallowsMod;
import com.pyding.deathlyhallows.entity.EntityEmpoweredArrow;
import com.pyding.deathlyhallows.render.ModelWrapperDisplayList;
import com.pyding.deathlyhallows.render.entity.model.ModelEmpoweredArrow;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;

import static org.lwjgl.opengl.GL11.*;

public class RenderEmpoweredArrow extends Render {

	private final ResourceLocation
			effectTexture = new ResourceLocation(DeathHallowsMod.ID, "textures/models/empoweredArrow/effect.png"),
			mainTexture = new ResourceLocation(DeathHallowsMod.ID, "textures/models/empoweredArrow/arrow.png"),
			modelPath = new ResourceLocation(DeathHallowsMod.ID, "models/empoweredArrowEffect.obj");
	private final IModelCustom effectModel = new ModelWrapperDisplayList((WavefrontObject)AdvancedModelLoader.loadModel(modelPath));
	private final ModelEmpoweredArrow arrowModel = new ModelEmpoweredArrow();

	public RenderEmpoweredArrow() {
	}

	public void renderArrow(EntityEmpoweredArrow e, double x, double y, double z, float f0, float partialTicks) {
		glPushMatrix();
		// render arrow
		glTranslated(x, y + 0.5D, z);

		glRotatef(180F - e.rotationYaw, 0.0F, 1.0F, 0.0F);
		glRotatef(-e.rotationPitch, 1.0F, 0.0F, 0.0F);

		bindTexture(mainTexture);
		arrowModel.render(e, 0, 0, 0, 0, 0, 0.0625F);

		// render glow effect
		glMatrixMode(GL_TEXTURE);
		glPushMatrix();
		bindTexture(effectTexture);
		float moveY = (e.ticksExisted % 20) / 10F;
		glTranslatef(0.0F, moveY, 0.0F);
		glScalef(1.0F, 0.0625F, 1.0F);
		effectModel.renderAll();
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);

		glPopMatrix();
	}

	public void doRender(Entity e, double x, double y, double z, float f0, float partialTicks) {
		renderArrow((EntityEmpoweredArrow)e, x, y, z, f0, partialTicks);
	}

	protected ResourceLocation getEntityTexture(Entity e) {
		return mainTexture;
	}

}
