package com.pyding.deathlyhallows.client.render.item;

import com.emoniph.witchery.client.model.ModelBroom;
import com.emoniph.witchery.entity.EntityBroom;
import com.pyding.deathlyhallows.entity.EmpoweredArrowEntity;
import com.pyding.deathlyhallows.entity.Nimbus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class EmpoweredEntityRender extends Render {
    private final ResourceLocation modelPath = new ResourceLocation("dh", "models/ArrowModel.obj");
	private final ResourceLocation effectTexture = new ResourceLocation("dh", "models/arrow_anim.png");
    private final ResourceLocation mainTexture = new ResourceLocation("dh", "models/arrow_pallete.png");
    private final IModelCustom arrow = new ModelWrapperDisplayList((WavefrontObject) AdvancedModelLoader.loadModel(modelPath));

	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return this.func_110832_a((EmpoweredArrowEntity) par1Entity);
	}

	public EmpoweredEntityRender() {
	}

	protected ResourceLocation func_110832_a(EmpoweredArrowEntity par1Entity) {
		return mainTexture;
	}

	public void renderArrow(EmpoweredArrowEntity par1EntityBoat, double par2, double par4, double par6, float par8, float par9) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) par2, (float) par4 + 1.0F, (float) par6);
		GL11.glMatrixMode(GL_TEXTURE);
		GL11.glPushMatrix();
		this.bindTexture(mainTexture);
		float moveY = (par1EntityBoat.ticksExisted % 20) / 20F;
		GL11.glTranslatef(0,moveY,0.0f);

		this.arrow.renderAll();

		GL11.glPopMatrix();
		GL11.glMatrixMode(GL_MODELVIEW);

		GL11.glPopMatrix();
	}

	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.renderArrow((EmpoweredArrowEntity) par1Entity, par2, par4, par6, par8, par9);
	}
}
