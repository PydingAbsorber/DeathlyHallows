package com.pyding.deathlyhallows.events;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.util.Config;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.ItemElderWand;
import com.pyding.deathlyhallows.symbols.SymbolEffectBase;
import com.pyding.deathlyhallows.utils.DHConfig;
import com.pyding.deathlyhallows.utils.DHUtils;
import com.pyding.deathlyhallows.utils.ElfUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;

import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;
import static codechicken.lib.gui.GuiDraw.getStringWidth;
import static com.emoniph.witchery.client.PlayerRender.drawString;
import static org.lwjgl.opengl.GL11.*;

@SuppressWarnings("unused")
public final class DHPlayerRenderEvents {
	private static final Minecraft mc = Minecraft.getMinecraft();
	public static long ticksSinceActive = 0L;
	public static boolean moveCameraActive = false;
	private int lastY = 0;
	private static final int[] glyphOffsetX = new int[]{0, 0, 1, -1, 1, -1, -1, 1};
	private static final int[] glyphOffsetY = new int[]{-1, 1, 0, 0, -1, 1, -1, 1};
	private static final ResourceLocation
			STROKES_TEXTURE = new ResourceLocation(DHIntegration.WITCHERY, "textures/gui/grid.png"),
			RADIAL_TEXTURE = new ResourceLocation(DHIntegration.WITCHERY, "textures/gui/radial.png"),
			ANIMA_TEXTURE = new ResourceLocation(DeathlyHallows.MODID, "textures/particles/anima.png"),
			ANIMA2_TEXTURE = new ResourceLocation(DeathlyHallows.MODID, "textures/particles/anima2.png");
	private static final DHPlayerRenderEvents INSTANCE = new DHPlayerRenderEvents();

	private DHPlayerRenderEvents() {

	}

	public static void init() {
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		FMLCommonHandler.instance().bus().register(INSTANCE);
	}

	@SubscribeEvent
	public void renderElfEars(RenderPlayerEvent.Specials.Pre e) {
		EntityPlayer p = e.entityPlayer;
		int elfLevel = ElfUtils.getElfLevel(p);
		if(elfLevel < 0) {
			return;
		}
		ModelRenderer head = e.renderer.modelBipedMain.bipedHead;
		if(head.isHidden || !head.showModel) {
			return;
		}
		if(p.isInvisible()) {
			if(p.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer)) {
				return;
			}
			glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
		}
		// note that axis are inverted at some point
		glPushMatrix();
		if(head.offsetX != 0F || head.offsetY != 0F || head.offsetZ != 0F) {
			glTranslatef(head.offsetX, head.offsetY, head.offsetZ);
		}
		if(head.rotationPointX != 0F || head.rotationPointY != 0F || head.rotationPointZ != 0F) {
			final float size = 0.0625F;
			glTranslatef(head.rotationPointX * size, head.rotationPointY * size, head.rotationPointZ * size);
		}
		if(head.rotateAngleZ != 0F) {
			glRotatef(head.rotateAngleZ * (180F / (float)Math.PI), 0F, 0F, 1F);
		}
		if(head.rotateAngleY != 0F) {
			glRotatef(head.rotateAngleY * (180F / (float)Math.PI), 0F, 1F, 0F);
		}
		if(head.rotateAngleX != 0F) {
			glRotatef(head.rotateAngleX * (180F / (float)Math.PI), 1F, 0F, 0F);
		}
		glTranslatef(0F, -0.25F + 0.03125F, -0.125F);
		glEnable(GL_CULL_FACE);
		// minecraft skins is usually 64x32
		drawEar(17 / 64D, 12 / 32D, 1D / 64D, 1D / 32D, true);
		glScalef(-1F, 1F, 1F);
		drawEar(7 / 64D, 12 / 32D, -1D / 64D, 1D / 32D, false);
		glDisable(GL_CULL_FACE);
		glPopMatrix();
	}

	private static void drawEar(double startU, double startV, double u, double v, boolean left) {
		glPushMatrix();
		// y-axis is inverted, it points down
		glTranslatef(0.25F, 0F, 0F);
		glRotatef(30, 0F, -1F, 0F);
		// minecraft skin faces is usually 16x16, so subdivide x2
		final double x = (left ? 1D : -1D) / 32D, y = 1D / 32D;
		Tessellator t = Tessellator.instance;
		if(!left) {
			glScalef(-1F, 1F, 1F);
		}
		elfEar(t, x, y, startU, startV, u, v, !left);
		glScalef(1F, 1F, -1F);
		elfEar(t, x, y, startU, startV, u, v, left);
		glPopMatrix();
	}

	private static void elfEar(Tessellator t, double x, double y, double startU, double startV, double u, double v, boolean invertNormals) {
		// quads
		t.startDrawingQuads();
		setEarNormals(t, invertNormals);
		// color channel 1
		quad(t, 0, 0, x, 2 * y, startU, startV, u / 4, v, invertNormals);
		quad(t, x, 0, 3 * x, y, startU + u / 4, startV, u / 2, v, invertNormals);
		quad(t, 4 * x, -y, x, y, startU + u / 4, startV, u / 2, v, invertNormals);
		quad(t, 5 * x, -y, x, y, startU + 3 * u / 4, startV, u / 4, v, invertNormals);
		// color channel 2
		quad(t, 0, 2 * y, x, y, startU + u, startV, u / 4, v, invertNormals);
		quad(t, 2 * x, y, x, y, startU + u + u / 4, startV, u / 2, v, invertNormals);
		quad(t, 7 * x, -3 * y, x, y, startU + u + 3 * u / 4, startV, u / 4, v, invertNormals);
		// color channel 3
		quad(t, 0, -y, 2 * x, y, startU, startV + v, u / 4, v, invertNormals);
		quad(t, 2 * x, -y - y / 2, 2 * x, y + y / 2, startU + u / 4, startV + v + v / 4, u / 2, 3 * v / 4, invertNormals);
		quad(t, 4 * x, -2 * y, 2 * x, y, startU + u / 4, startV + v + v / 4, u / 2, 3 * v / 4, invertNormals);
		quad(t, 6 * x, -2 * y - 2 * y / 3, x, y + 2 * y / 3, startU + 3 * u / 4, startV + v, u / 4, v, invertNormals);
		quad(t, x, y, x, y, startU + u / 4, startV + v, u / 2, v / 4, invertNormals);
		// color channel 4
		quad(t, x, 2 * y, x, y, startU + u, startV + v, u / 4, v, invertNormals);
		quad(t, 3 * x, y, x, y, startU + u + u / 4, startV + v, u / 4, v, invertNormals);
		quad(t, 4 * x, 0, 2 * x, y, startU + u + u / 2, startV + v, u / 4, v, invertNormals);
		quad(t, 6 * x, -y, x, y, startU + u + 3 * u / 4, startV + v, u / 4, v, invertNormals);
		quad(t, 7 * x, -2 * y, x, y, startU + u + 3 * u / 4, startV + v, u / 4, v, invertNormals);
		t.draw();
		// tris
		t.startDrawing(GL_TRIANGLES);
		setEarNormals(t, invertNormals);
		tris(t, 0, -y, 2 * x, -y / 2, startU, startV + v, u / 8, v, invertNormals);
		tris(t, 2 * x, -y - y / 2, 2 * x, -y / 2, startU + u / 4, startV + v + v / 4, u / 2, 3 * v / 4, invertNormals);
		tris(t, 4 * x, -2 * y, 2 * x, -2 * y / 3, startU + u / 4, startV + v + v / 4, u / 2, 3 * v / 4, invertNormals);
		tris(t, 6 * x, -2 * y - 2 * y / 3, x, -y / 3, startU + 3 * u / 4, startV + v, u / 4, v, invertNormals);
		t.draw();
	}

	private static void setEarNormals(Tessellator t, boolean invertNormals) {
		t.setNormal(invertNormals ? -1F : 1F, 0F, 0F);
	}

	private static void quad(Tessellator t, double x, double y, double xLen, double yLen, double minU, double minV, double uLen, double vLen, boolean inverted) {
		double maxV = minV + vLen;
		double maxU = minU + uLen;
		if(inverted) {
			t.addVertexWithUV(x, y + yLen, 0D, minU, maxV);
			t.addVertexWithUV(x + xLen, y + yLen, 0D, maxU, maxV);
			t.addVertexWithUV(x + xLen, y, 0D, maxU, minV);
			t.addVertexWithUV(x, y, 0D, minU, minV);
			return;
		}
		t.addVertexWithUV(x, y, 0D, minU, minV);
		t.addVertexWithUV(x + xLen, y, 0D, maxU, minV);
		t.addVertexWithUV(x + xLen, y + yLen, 0D, maxU, maxV);
		t.addVertexWithUV(x, y + yLen, 0D, minU, maxV);
	}

	private static void tris(Tessellator t, double x, double y, double xLen, double yLen, double minU, double minV, double uLen, double vLen, boolean inverted) {
		double maxV = minV + vLen;
		double maxU = minU + uLen;
		if(inverted) {
			t.addVertexWithUV(x + xLen, y, 0D, minU, maxV);
			t.addVertexWithUV(x + xLen, y + yLen, 0D, maxU, minV);
			t.addVertexWithUV(x, y, 0D, minU, minV);
			return;
		}
		t.addVertexWithUV(x, y, 0D, minU, minV);
		t.addVertexWithUV(x + xLen, y + yLen, 0D, maxU, minV);
		t.addVertexWithUV(x + xLen, y, 0D, minU, maxV);
	}

	@SubscribeEvent
	public void cancelRenderWithMantle(RenderLivingEvent.Pre e) {
		if(e.entity.getEntityData().getBoolean("mantleActive")) {
			e.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void renderAnimaInteritus(RenderLivingEvent.Post e) {
		NBTTagCompound tag = e.entity.getEntityData();
		if(!tag.hasKey("dhcurse")) {
			return;
		}
		int curse = tag.getInteger("dhcurse");
		glPushMatrix();
		float bobbing = 0.1F * (MathHelper.sin(2F * (float)Math.PI * (e.entity.ticksExisted % 180) / 180F) + 1F);
		glTranslated(e.x, e.y + e.entity.height + 0.5D + bobbing, e.z);
		EntityPlayer p = mc.thePlayer;
		glRotatef(-p.rotationYaw, 0F, 1F, 0F);
		glRotatef(p.rotationPitch, 1F, 0F, 0F);
		RenderManager.instance.renderEngine.bindTexture(curse > 200 ? ANIMA_TEXTURE : ANIMA2_TEXTURE);
		glDisable(GL_LIGHTING);
		glDisable(GL_CULL_FACE);
		drawImage();
		glEnable(GL_CULL_FACE);
		glEnable(GL_LIGHTING);
		//glRotatef(180F, 0F, 1F, 0F);
		//drawImage();
		glPopMatrix();
	}

	private static void drawImage() {
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.addVertexWithUV(-0.5D, -0.5D, 0D, 1D, 1D);
		t.addVertexWithUV(0.5D, -0.5D, 0D, 0D, 1D);
		t.addVertexWithUV(0.5D, 0.5D, 0D, 0D, 0D);
		t.addVertexWithUV(-0.5D, 0.5D, 0D, 1D, 0D);
		t.draw();
	}

	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent e) {
		if(e.phase == TickEvent.Phase.START) {
			renderPre();
			return;
		}
		if(e.phase == TickEvent.Phase.END) {
			renderPast();
		}
	}

	private static void renderPre() {
		if(mc.thePlayer == null || mc.currentScreen != null) {
			return;
		}
		if(Minecraft.getSystemTime() - ticksSinceActive > 3000L) {
			moveCameraActive = false;
		}
	}

	private void renderPast() {
		EntityClientPlayerMP p = mc.thePlayer;
		if(p == null || mc.currentScreen != null) {
			return;
		}
		mc.renderViewEntity = p;
		ItemStack stack = p.getItemInUse();
		ScaledResolution scale = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		if(stack == null) {
			return;
		}
		if(stack.getItem() == DHItems.elderWand) {
			if(!p.isSneaking()) {
				renderHotSpells(scale, p, ItemElderWand.getLastSpells(stack), ItemElderWand.getIndex(stack.getTagCompound()));
				return;
			}
			renderStrokes(scale, p.getEntityData().getByteArray("Strokes"));
		}
	}

	private static void renderHotSpells(ScaledResolution scale, EntityPlayer p, NBTTagList list, int index) {
		glPushMatrix();
		try {
			int x = scale.getScaledWidth() / 2 - 8;
			int y = scale.getScaledHeight() / 2 - 8;
			mc.getTextureManager().bindTexture(RADIAL_TEXTURE);
			glPushMatrix();
			float s = 0.33333334F;
			glTranslatef(x - 42F + 5F, y - 42F + 5F, 0.0F);
			glScalef(s, s, s);
			glColor4f(1F, 1F, 1F, 1F);
			drawTexturedModalRect(8, 8, 0, 0, 256, 256);
			glPopMatrix();
			drawSpells(ItemElderWand.getXY(p), list, index, x, y);
		}
		finally {
			glPopMatrix();
		}
	}

	private static void drawSpells(float[] pointer, NBTTagList list, int index, int x, int y) {
		final float spellRange = 32F;
		boolean full = list.tagCount() >= DHConfig.elderWandMaxSpells;
		int length = list.tagCount() + (full ? 0 : 1);
		if(index == -1) {
			final float pointerRange = 1.5F;
			drawPointer(x + pointer[0] * pointerRange, y + pointer[1] * pointerRange);
			for(int i = 0; i < list.tagCount(); ++i) {
				drawSpellSlot(getSpell(list, i), x, y, i * (float)Math.PI * 2F / length, spellRange, 0x77_FF_FF_FF);
			}
			if(!full) {
				drawSpellSlot(null, x, y, (length - 1) * (float)Math.PI * 2F / length, spellRange, 0x77_FF_FF_FF);
			}
			return;
		}
		if(full) {
			drawSpellSlot(getSpell(list, index), x, y, index * (float)Math.PI * 2F / length, spellRange, 0xFF_FF_FF);
			return;
		}
		if(index == list.tagCount()) {
			drawSpellSlot(null, x, y, (length - 1) * (float)Math.PI * 2F / length, spellRange, 0xFF_FF_FF);
			return;
		}
		drawSpellSlot(list.tagCount() < DHConfig.elderWandMaxSpells && index == list.tagCount() ? null : getSpell(list, index), x, y, index * (float)Math.PI * 2F / length, spellRange, 0xFF_FF_FF);
	}

	private static SymbolEffect getSpell(NBTTagList list, int index) {
		return EffectRegistry.instance().getEffect(DHUtils.getBytesFromTagList(list, index));
	}

	private static void drawPointer(float x, float y) {
		glColor3f(0F, 0F, 0F);
		glPushMatrix();
		glTranslatef(x + 4.5F, y + 4.5F, 0F);
		glScalef(1F / 32F, 1F / 32F, 1F);
		// TODO texture
		drawTexturedModalRect(0, 0, 0, 0, 256, 256);
		glPopMatrix();
		glColor3f(1F, 1F, 1F);
	}

	private static void drawSpellSlot(SymbolEffect effect, float x, float y, float angle, float radius, int color) {
		float sin = -MathHelper.cos(angle), cos = MathHelper.sin(angle); // I know what I'm doing. -cos is just shortcuts for sin(x + pi/2), but it's still Y axis.
		x += cos * radius;
		y += sin * radius;
		String s = "New Spell";
		ItemStack icon = new ItemStack(Items.book);
		if(effect != null) {
			s = effect.getLocalizedName();
			if(effect instanceof SymbolEffectBase) {
				icon = new ItemStack(DHItems.elderBook);
			}
			else {
				icon = Witchery.Items.GENERIC.itemBookWands.createStack();
			}
		}
		drawItem(x, y, icon);
		float
				superCos = Math.abs(cos) < 0.1F ? 0F : Math.signum(cos),
				superSin = (1F - (float)Math.pow(Math.abs(cos), 0.75D)) * Math.signum(sin);
		x += 9F - mc.fontRenderer.getStringWidth(s) / 2F + superCos * (12F + mc.fontRenderer.getStringWidth(s) / 2F);
		y += mc.fontRenderer.FONT_HEIGHT / 2F + superSin * (6F + mc.fontRenderer.FONT_HEIGHT);
		drawString(s, x, y, color);
	}

	private static final RenderItem drawItems = new RenderItem();

	private static void drawItem(float x, float y, ItemStack stack) {
		glEnable(GL_LIGHTING);
		glEnable(GL_DEPTH_TEST);
		drawItems.zLevel += 100F;
		glPushMatrix();
		FontRenderer fr = getFontRenderer(stack);
		try {
			glTranslatef(x, y, 0F);
			drawItems.renderItemAndEffectIntoGUI(fr, mc.renderEngine, stack, 0, 0);
			drawItems.renderItemOverlayIntoGUI(fr, mc.renderEngine, stack, 0, 0);
		}
		finally {
			glPopMatrix();
		}
		drawItems.zLevel -= 100F;
		glDisable(GL_LIGHTING);
		glDisable(GL_DEPTH_TEST);
	}

	private static FontRenderer getFontRenderer(ItemStack stack) {
		if(stack == null || stack.getItem() == null) {
			return mc.fontRenderer;
		}
		FontRenderer f = stack.getItem().getFontRenderer(stack);
		if(f == null) {
			return mc.fontRenderer;
		}
		return f;
	}

	private void renderStrokes(ScaledResolution scale, byte[] strokes) {
		mc.getTextureManager().bindTexture(STROKES_TEXTURE);
		glPushMatrix();
		try {
			int x = scale.getScaledWidth() / 2 - 8;
			int y = scale.getScaledHeight() / 2 - 8;
			lastY = lastY == 120 ? 0 : lastY + 1;
			int offsetY = lastY / 8;
			offsetY = offsetY > 7 ? 15 - offsetY : offsetY;
			int offsetX = (Config.instance().branchIconSet == 1 ? 64 : 0);
			for(byte stroke: strokes) {
				x += glyphOffsetX[stroke] * 16;
				y += glyphOffsetY[stroke] * 16;
				drawTexturedModalRect(x, y, offsetX + stroke * 16, offsetY * 16, 16, 16);
			}
			SymbolEffect symbol = EffectRegistry.instance().getEffect(strokes);
			if(symbol != null) {
				String name = symbol.getLocalizedName();
				int tx = scale.getScaledWidth() / 2 - (int)(getStringWidth(name) / 2.0D);
				int ty = scale.getScaledHeight() / 2 + 20;
				drawString(name, tx, ty, 16777215);
			}
		}
		finally {
			glPopMatrix();
		}
	}

}
