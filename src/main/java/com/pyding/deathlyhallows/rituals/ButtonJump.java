package com.pyding.deathlyhallows.rituals;

import com.emoniph.witchery.client.gui.GuiScreenWitchcraftBook;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
class ButtonJump extends GuiButton {
	public final int nextPage;
	public final int iconX;
	public final int iconY;

	public ButtonJump(int commandID, int x, int y, int pageIndex, int iconX, int iconY) {
		super(commandID, x, y, 20, 20, "");
		this.nextPage = pageIndex;
		this.iconX = iconX;
		this.iconY = iconY;
	}

	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		if (this.visible) {
			boolean flag = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			par1Minecraft.getTextureManager().bindTexture(GuiScreenWitchcraftBook.DOUBLE_BOOK_TEXTURE);
			int k = 3;
			int l = 220;
			if (flag) {
				k += 12;
			}

			this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, 9, 24);
			if (this.iconX >= 0 && this.iconY >= 0) {
				this.drawTexturedModalRect(this.xPosition, this.yPosition + 9, this.iconX, this.iconY, 8, 8);
			}
		}

	}
}
