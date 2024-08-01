package com.pyding.deathlyhallows.guis;

import com.emoniph.witchery.item.ItemLeonardsUrn;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class CardsGUI extends GuiContainer {
	private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("witchery", "textures/gui/urn.png");
	private final IInventory upperInventory;
	private final IInventory lowerInventory;

	public CardsGUI(IInventory inventoryPlayer, IInventory inventoryBag) {
		super(new ItemLeonardsUrn.ContainerLeonardsUrn(inventoryPlayer, inventoryBag, null));
		this.upperInventory = inventoryBag;
		this.lowerInventory = inventoryPlayer;
		ySize = 184;
	}

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRendererObj.drawString(StatCollector.translateToLocal(upperInventory.getInventoryName()), 8, 6, 0x404040);
		fontRendererObj.drawString(StatCollector.translateToLocal(lowerInventory.getInventoryName()), 8, ySize - 96 + 2, 0x404040);
	}

	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(TEXTURE_LOCATION);
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);

		for(int i = 0; i < upperInventory.getSizeInventory(); ++i) {
			Slot slot = inventorySlots.getSlotFromInventory(upperInventory, i);
			drawTexturedModalRect(left + slot.xDisplayPosition - 1, top + slot.yDisplayPosition - 1, xSize, 0, 18, 18);
		}

	}
}
