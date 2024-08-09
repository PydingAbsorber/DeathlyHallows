package com.pyding.deathlyhallows.render.item;

import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.render.ModelWrapperDisplayList;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;

import static org.lwjgl.opengl.GL11.*;

public class RenderEldenWand implements IItemRenderer {
	private final ResourceLocation 
			modelPath = new ResourceLocation(DeathlyHallows.MODID, "models/wand.obj"),
			wandTexturePath = new ResourceLocation(DeathlyHallows.MODID, "textures/models/wand.png");
	private final IModelCustom wandModel = new ModelWrapperDisplayList((WavefrontObject)AdvancedModelLoader.loadModel(modelPath));

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch(type) {
			case EQUIPPED_FIRST_PERSON:
			case EQUIPPED:
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return type == ItemRenderType.ENTITY && helper == ItemRendererHelper.ENTITY_ROTATION;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		Minecraft.getMinecraft().renderEngine.bindTexture(wandTexturePath);
		glPushMatrix();
		if(type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			glTranslatef(2F, 0.6F, 1.5F);
			glScalef(0.5F, 0.5F, 0.5F);
			glRotatef(100F, 0F, 60F, 250F);
		}
		else if(type == ItemRenderType.EQUIPPED) {
			glTranslatef(0.3F, 0.85F, 0F);
			glScalef(0.2F, 0.2F, 0.2F);
			glRotatef(130F, 0F, 0F, 250F);
		}
		wandModel.renderAll();
		glPopMatrix();
	}
	
}
