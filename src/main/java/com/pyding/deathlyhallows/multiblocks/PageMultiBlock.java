package com.pyding.deathlyhallows.multiblocks;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.events.DHMultiBlockRenderEvents;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class PageMultiBlock {

	public int left, top, width, height, zlevel = 100;
	public MultiBlockSet set;
	public MultiBlock mb;
	int ticksElapsed;

	public PageMultiBlock(MultiBlockSet set, int left, int top, int width, int height, int ticksElapsed) {
		mb = set.getForIndex(0);
		this.set = set;
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		this.ticksElapsed = ticksElapsed;
	}

	@SideOnly(Side.CLIENT)
	public void renderScreen(int mx, int my) {
		TextureManager render = Minecraft.getMinecraft().renderEngine;

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_ALPHA_TEST);
		glColor4f(1F, 1F, 1F, 1F);
		glDisable(GL_BLEND);
		glEnable(GL_ALPHA_TEST);
		glPushMatrix();
		glTranslatef(left - 2 + width / 2, top + height / 2, zlevel + 100F);
		float
				h = mb.getYSize(),
				w = h < 2 ? (Math.max(Math.abs(mb.getXSize()), Math.abs(mb.getZSize()))) : (float)Math.sqrt(mb.getXSize() * mb.getXSize() + mb.getZSize() * mb.getZSize());
		float scale = -0.8F * Math.min(width / w, this.height / h);
		glScalef(scale, scale, scale);
		if(h < 2) {
			glRotatef(-75F, 1, 0, 0);
			glRotatef(0, 0, 1, 0);
		}
		else {
			glRotatef(-25F, 1, 0, 0);
			glRotatef(ticksElapsed, 0, 1, 0);
		}
		DHMultiBlockRenderEvents.renderMultiBlockOnPage(mb);
		glPopMatrix();
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableGUIStandardItemLighting();
		int x = left + 15;
		int y = top + 25;
		RenderItem.getInstance().renderItemIntoGUI(font, render, new ItemStack(Witchery.Items.CHALK_OTHERWHERE), x, y);
		RenderHelper.disableStandardItemLighting();
		glDisable(GL12.GL_RESCALE_NORMAL);
		glPushMatrix();
		glTranslatef(0F, 0F, 200F);
		if(mx >= x && mx < x + 16 && my >= y && my < y + 16) {
			List<String> mats = new ArrayList<>();
			mats.add(StatCollector.translateToLocal("witchery.book.materialsRequired"));
			for(ItemStack stack: mb.materials) {
				if(stack != null && stack.getItem() != null) {
					String size = String.valueOf(stack.stackSize);
					if(size.length() < 2) {
						size = "0" + size;
					}
					mats.add(" " + EnumChatFormatting.AQUA + size + " " + EnumChatFormatting.GRAY + stack.getDisplayName());
				}
			}

			renderTooltip(mx, my, mats);
		}
		glPopMatrix();
	}

	@SideOnly(Side.CLIENT)
	public void setVisualization() {
		DHMultiBlockRenderEvents.setMultiBlock(set);
	}

	@SideOnly(Side.CLIENT)
	public static void resetVisualization() {
		DHMultiBlockRenderEvents.setMultiBlock(null);
	}

	public static void renderTooltip(int x, int y, List<String> tooltipData) {
		int color = 0x505000FF;
		int color2 = 0xF0100010;

		renderTooltip(x, y, tooltipData, color, color2);
	}

	public static void renderTooltip(int x, int y, List<String> tooltipData, int color, int color2) {
		boolean lighting = glGetBoolean(GL_LIGHTING);
		if(lighting) {
			RenderHelper.disableStandardItemLighting();
		}

		if(!tooltipData.isEmpty()) {
			int var5 = 0;
			int var6;
			int var7;
			FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
			for(var6 = 0; var6 < tooltipData.size(); ++var6) {
				var7 = fontRenderer.getStringWidth(tooltipData.get(var6));
				if(var7 > var5) {
					var5 = var7;
				}
			}
			var6 = x + 12;
			var7 = y - 12;
			int var9 = 8;
			if(tooltipData.size() > 1) {
				var9 += 2 + (tooltipData.size() - 1) * 10;
			}
			float z = 300F;
			drawGradientRect(var6 - 3, var7 - 4, z, var6 + var5 + 3, var7 - 3, color2, color2);
			drawGradientRect(var6 - 3, var7 + var9 + 3, z, var6 + var5 + 3, var7 + var9 + 4, color2, color2);
			drawGradientRect(var6 - 3, var7 - 3, z, var6 + var5 + 3, var7 + var9 + 3, color2, color2);
			drawGradientRect(var6 - 4, var7 - 3, z, var6 - 3, var7 + var9 + 3, color2, color2);
			drawGradientRect(var6 + var5 + 3, var7 - 3, z, var6 + var5 + 4, var7 + var9 + 3, color2, color2);
			int var12 = (color & 0xFFFFFF) >> 1 | color & -16777216;
			drawGradientRect(var6 - 3, var7 - 3 + 1, z, var6 - 3 + 1, var7 + var9 + 3 - 1, color, var12);
			drawGradientRect(var6 + var5 + 2, var7 - 3 + 1, z, var6 + var5 + 3, var7 + var9 + 3 - 1, color, var12);
			drawGradientRect(var6 - 3, var7 - 3, z, var6 + var5 + 3, var7 - 3 + 1, color, color);
			drawGradientRect(var6 - 3, var7 + var9 + 2, z, var6 + var5 + 3, var7 + var9 + 3, var12, var12);

			glDisable(GL_DEPTH_TEST);
			for(int var13 = 0; var13 < tooltipData.size(); ++var13) {
				String var14 = tooltipData.get(var13);
				fontRenderer.drawStringWithShadow(var14, var6, var7, -1);
				if(var13 == 0) {
					var7 += 2;
				}
				var7 += 10;
			}
			glEnable(GL_DEPTH_TEST);
		}
		if(!lighting) {
			RenderHelper.disableStandardItemLighting();
		}
		glColor4f(1F, 1F, 1F, 1F);
	}

	public static void drawGradientRect(int par1, int par2, float z, int par3, int par4, int par5, int par6) {
		float var7 = (par5 >> 24 & 255) / 255F;
		float var8 = (par5 >> 16 & 255) / 255F;
		float var9 = (par5 >> 8 & 255) / 255F;
		float var10 = (par5 & 255) / 255F;
		float var11 = (par6 >> 24 & 255) / 255F;
		float var12 = (par6 >> 16 & 255) / 255F;
		float var13 = (par6 >> 8 & 255) / 255F;
		float var14 = (par6 & 255) / 255F;
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glDisable(GL_ALPHA_TEST);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glShadeModel(GL_SMOOTH);
		Tessellator var15 = Tessellator.instance;
		var15.startDrawingQuads();
		var15.setColorRGBA_F(var8, var9, var10, var7);
		var15.addVertex(par3, par2, z);
		var15.addVertex(par1, par2, z);
		var15.setColorRGBA_F(var12, var13, var14, var11);
		var15.addVertex(par1, par4, z);
		var15.addVertex(par3, par4, z);
		var15.draw();
		glShadeModel(GL_FLAT);
		glDisable(GL_BLEND);
		glEnable(GL_ALPHA_TEST);
		glEnable(GL_TEXTURE_2D);
	}

}
