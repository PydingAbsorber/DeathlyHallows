package com.pyding.deathlyhallows.client.render.multiblock;

import java.util.ArrayList;
import java.util.List;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PageMultiblock {

    private static final ResourceLocation multiblockOverlay = new ResourceLocation("witchery", "textures/gui/multiblockOverlay.png");
    public int left,top,width, height, zlevel=100;
    public MultiblockSet set;
    public Multiblock mb;
    int ticksElapsed;

    public PageMultiblock(MultiblockSet set, int left, int top, int width, int height, int ticksElapsed) {
        mb = set.getForIndex(0);
        this.set = set;
        this.left=left;
        this.top=top;
        this.width=width;
        this.height=height;
        this.ticksElapsed=ticksElapsed;
    }

    @SideOnly(Side.CLIENT)
    public void renderScreen(GuiScreen gui, int mx, int my) {
        TextureManager render = Minecraft.getMinecraft().renderEngine;
        render.bindTexture(multiblockOverlay);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        final float maxX = 90, maxY = 60;
        GL11.glPushMatrix();
        GL11.glTranslatef(left + width / 2, top + 90, zlevel + 100F);

        float diag = (float) Math.sqrt(mb.getXSize() * mb.getXSize() + mb.getZSize() * mb.getZSize());
        float height = mb.getYSize();
        float scaleX = maxX / diag;
        float scaleY = maxY / height;
        float scale = -Math.min(scaleY, scaleX)*1.5f;
        GL11.glScalef(scale, scale, scale);
        if(set.getForIndex(0).maxY<=1) {
            GL11.glRotatef(-60F, 1, 0, 0);
            GL11.glRotatef(0, 0, 1, 0);
        }else{
            GL11.glRotatef(-20F, 1, 0, 0);
            GL11.glRotatef(ticksElapsed, 0, 1, 0);
        }
        MultiblockRenderHandler.renderMultiblockOnPage(mb);

        GL11.glPopMatrix();
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
/*
        boolean unicode = font.getUnicodeFlag();
        String s = EnumChatFormatting.BOLD + StatCollector.translateToLocal(getUnlocalizedName());
        font.setUnicodeFlag(true);
        font.drawString(s, gui.getLeft() + gui.getWidth() / 2 - font.getStringWidth(s) / 2, gui.getTop() + 16, 0x000000);
        font.setUnicodeFlag(unicode);*/

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        int x = left + 15;
        int y = top + 25;
        RenderItem.getInstance().renderItemIntoGUI(font, render, new ItemStack(Witchery.Items.CHALK_OTHERWHERE), x, y);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        GL11.glPushMatrix();
        GL11.glTranslatef(0F, 0F, 200F);
        //System.out.println(""+mx+" "+my);
        if(mx >= x && mx < x + 16 && my >= y && my < y + 16) {
            List<String> mats = new ArrayList();
            mats.add(StatCollector.translateToLocal("witchery.book.materialsRequired"));
            for(ItemStack stack : mb.materials) {
                if(stack!=null && stack.getItem()!=null) {
                    String size = "" + stack.stackSize;
                    if (size.length() < 2)
                        size = "0" + size;
                    mats.add(" " + EnumChatFormatting.AQUA + size + " " + EnumChatFormatting.GRAY + stack.getDisplayName());
                }
            }

            renderTooltip(mx, my, mats);
        }
        GL11.glPopMatrix();
    }

//    public void onOpened(IGuiLexiconEntry gui) {
//        button = new GuiButton(101, gui.getLeft() + 30, gui.getTop() + gui.getHeight() - 50, gui.getWidth() - 60, 20, getButtonStr());
//        gui.getButtonList().add(button);
//    }

    public String getButtonStr() {
        if(MultiblockRenderHandler.currentMultiblock==null) return StatCollector.translateToLocal("botaniamisc.visualize");
        return StatCollector.translateToLocal(set.equals(MultiblockRenderHandler.currentMultiblock) ? "botaniamisc.unvisualize" : "botaniamisc.visualize");
    }


    @SideOnly(Side.CLIENT)
    public void changeVisualization(GuiButton button) {
        if (MultiblockRenderHandler.currentMultiblock!=null && MultiblockRenderHandler.currentMultiblock.equals(set)) {
            MultiblockRenderHandler.setMultiblock(null);
        }else {
            MultiblockRenderHandler.setMultiblock(set);
        }
        button.displayString = getButtonStr();
    }

    public static void renderTooltip(int x, int y, List<String> tooltipData) {
        int color = 0x505000ff;
        int color2 = 0xf0100010;

        renderTooltip(x, y, tooltipData, color, color2);
    }

    public static void renderTooltipOrange(int x, int y, List<String> tooltipData) {
        int color = 0x50a06600;
        int color2 = 0xf01e1200;

        renderTooltip(x, y, tooltipData, color, color2);
    }

    public static void renderTooltipGreen(int x, int y, List<String> tooltipData) {
        int color = 0x5000a000;
        int color2 = 0xf0001e00;

        renderTooltip(x, y, tooltipData, color, color2);
    }

    public static void renderTooltip(int x, int y, List<String> tooltipData, int color, int color2) {
        boolean lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
        if(lighting)
            RenderHelper.disableStandardItemLighting();

        if (!tooltipData.isEmpty()) {
            int var5 = 0;
            int var6;
            int var7;
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            for (var6 = 0; var6 < tooltipData.size(); ++var6) {
                var7 = fontRenderer.getStringWidth(tooltipData.get(var6));
                if (var7 > var5)
                    var5 = var7;
            }
            var6 = x + 12;
            var7 = y - 12;
            int var9 = 8;
            if (tooltipData.size() > 1)
                var9 += 2 + (tooltipData.size() - 1) * 10;
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

            GL11.glDisable(GL11.GL_DEPTH_TEST);
            for (int var13 = 0; var13 < tooltipData.size(); ++var13) {
                String var14 = tooltipData.get(var13);
                fontRenderer.drawStringWithShadow(var14, var6, var7, -1);
                if (var13 == 0)
                    var7 += 2;
                var7 += 10;
            }
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
        if(!lighting)
            RenderHelper.disableStandardItemLighting();
        GL11.glColor4f(1F, 1F, 1F, 1F);
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
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator var15 = Tessellator.instance;
        var15.startDrawingQuads();
        var15.setColorRGBA_F(var8, var9, var10, var7);
        var15.addVertex(par3, par2, z);
        var15.addVertex(par1, par2, z);
        var15.setColorRGBA_F(var12, var13, var14, var11);
        var15.addVertex(par1, par4, z);
        var15.addVertex(par3, par4, z);
        var15.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }


    @SideOnly(Side.CLIENT)
    public void updateScreen() {
        ++ticksElapsed;
    }

}
