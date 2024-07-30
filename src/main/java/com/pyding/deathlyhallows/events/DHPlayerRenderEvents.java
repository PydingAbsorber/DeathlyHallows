package com.pyding.deathlyhallows.events;

import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.util.Config;
import com.pyding.deathlyhallows.items.DHItems;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;
import static codechicken.lib.gui.GuiDraw.getStringWidth;
import static com.emoniph.witchery.client.PlayerRender.drawString;

public final class DHPlayerRenderEvents {
	private static final Minecraft mc = Minecraft.getMinecraft();
	public static long ticksSinceActive = 0L;
	public static boolean moveCameraActive = false;
	private int lastY = 0;
	private static final int[] glyphOffsetX = new int[]{0, 0, 1, -1, 1, -1, -1, 1};
	private static final int[] glyphOffsetY = new int[]{-1, 1, 0, 0, -1, 1, -1, 1};
	private static final ResourceLocation TEXTURE_GRID = new ResourceLocation("witchery", "textures/gui/grid.png");

	private static final DHPlayerRenderEvents INSTANCE = new DHPlayerRenderEvents();

	private DHPlayerRenderEvents() {

	}

	public static void init() {
		FMLCommonHandler.instance().bus().register(INSTANCE);
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
		EntityClientPlayerMP player = mc.thePlayer;
		if(player == null || mc.currentScreen != null) {
			return;
		}
		mc.renderViewEntity = player;
		ItemStack stack = player.getItemInUse();
		ScaledResolution scale = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		if(stack == null || stack.getItem() != DHItems.elderWand) {
			return;
		}
		byte[] strokes = player.getEntityData().getByteArray("Strokes");
		mc.getTextureManager().bindTexture(TEXTURE_GRID);
		GL11.glPushMatrix();
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
				String var49 = symbol.getLocalizedName();
				int tx = scale.getScaledWidth() / 2 - (int)(getStringWidth(var49) / 2.0D);
				int ty = scale.getScaledHeight() / 2 + 20;
				drawString(var49, tx, ty, 16777215);
			}
		}
		finally {
			GL11.glPopMatrix();
		}
	}
	
}
