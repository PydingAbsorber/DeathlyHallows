package com.pyding.deathlyhallows.rituals;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.common.properties.ExtendedPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiBookBase extends GuiScreen {

	// TODO can't alterate spell, nor view it's actuall shape group upon casting. Maybe don't care..?
	private static final String
			SUMMARY = "Summary",
			STROKES = "Strokes";
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/book.png");
	private static final int
			GUI_WIDTH = 192,
			GUI_HEIGHT = 192;
	private int bookTotalPages;
	private int currPage;
	private final NBTTagList bookPages;
	private GuiButtonNext
			buttonNext,
			buttonPrev;


	private final EntityPlayer player;

	public GuiBookBase(EntityPlayer player) {
		this.player = player;
		bookPages = new NBTTagList();
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString(SUMMARY, Witchery.resource("witchery.am2pg.spellbook.info"));
		bookPages.appendTag(tag);

		ExtendedPlayer props = ExtendedPlayer.get(player);
		
		bookTotalPages = bookPages.tagCount();
		currPage = MathHelper.clamp_int(props.page, 0, Math.max(this.bookTotalPages  - 1, 0));
	}

	@SuppressWarnings("unchecked")
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, 4 + GUI_HEIGHT, 200, 20, I18n.format("gui.done")));
		int x = (width - GUI_WIDTH) / 2;
		buttonList.add(buttonNext = new GuiButtonNext(1, x + 120, 156, true));
		buttonList.add(buttonPrev = new GuiButtonNext(2, x + 38, 156, false));
		updateButtons();
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		savePage();
	}

	private void updateButtons() {
		buttonNext.visible = currPage < bookTotalPages - 1;
		buttonPrev.visible = currPage > 0;
	}

	private void savePage() {
		if(player == null || currPage < 0) {
			return;
		}
		ExtendedPlayer.get(player).page = currPage;
	}

	protected void actionPerformed(GuiButton button) {
		if(!button.enabled) {
			return;
		}
		switch(button.id) {
			case 0: {
				mc.displayGuiScreen(null);
				return;
			}
			case 1: {
				if(currPage < bookTotalPages - 1) {
					++currPage;
				}
				break;
			}
			case 2: {
				if(currPage > 0) {
					--currPage;
				}
				break;
			}
		}
		updateButtons();
	}

	public void drawScreen(int x, int y, float f) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(TEXTURE);
		int width = (this.width - GUI_WIDTH) / 2;
		drawTexturedModalRect(width, 2, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		String pages = I18n.format("book.pageIndicator", currPage + 1, bookTotalPages);
		fontRendererObj.drawString(pages, width - fontRendererObj.getStringWidth(pages) + GUI_WIDTH - 44, 2 + 16, 0);
		if(bookPages != null && currPage >= 0 && currPage < bookPages.tagCount()) {
			NBTTagCompound compound = bookPages.getCompoundTagAt(currPage);
			fontRendererObj.drawSplitString(compound.getString(SUMMARY), width + 36, 2 + 32, 116, 0);
		}
		super.drawScreen(x, y, f);
	}

	public static class GuiButtonNext extends GuiButton {

		private final boolean right;

		public GuiButtonNext(int id, int x, int y, boolean right) {
			super(id, x, y, 23, 13, "");
			this.right = right;
		}

		public void drawButton(Minecraft mc, int x, int y) {
			if(!visible) {
				return;
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(TEXTURE);
			boolean selected = x >= xPosition && x < xPosition + width && y >= yPosition && y < yPosition + height;
			drawTexturedModalRect(xPosition, yPosition, selected ? 23 : 0, right ? 192 : 205, 23, 13);
		}

	}


}
