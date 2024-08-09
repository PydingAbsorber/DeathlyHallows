package com.pyding.deathlyhallows.render.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import static org.lwjgl.opengl.GL11.*;

public class RenderTransparentItem implements IItemRenderer {

	@Override
	public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		// thanks forge, that's all I needed. Now go back to trashcan of vanilla graphics settings disrespection
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {

	}

}
