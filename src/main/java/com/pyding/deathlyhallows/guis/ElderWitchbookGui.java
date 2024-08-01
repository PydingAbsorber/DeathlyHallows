//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.pyding.deathlyhallows.guis;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.network.PacketItemUpdate;
import com.pyding.deathlyhallows.multiblocks.MultiBlock;
import com.pyding.deathlyhallows.multiblocks.PageMultiBlock;
import com.pyding.deathlyhallows.rituals.ElderRiteRegistry;
import com.pyding.deathlyhallows.rituals.Figure;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ElderWitchbookGui extends GuiScreen {
	private static final ResourceLocation
			DOUBLE_BOOK_TEXTURE = new ResourceLocation("witchery", "textures/gui/bookDouble.png"),
			TEXTURE = new ResourceLocation("textures/gui/book.png");
	private final EntityPlayer player;
	private final ItemStack stack;
	private int updateCount,
			bookImageWidth = 192,
			bookImageHeight = 192,
			bookTotalPages,
			currPage,
			recipePageCount,
			recipePageCurrent;
	private final NBTTagList bookPages;
	private GuiButtonNext
			buttonNextPage,
			buttonPreviousPage,
			buttonNextIngredientPage,
			buttonPreviousIngredientPage;
	private PageMultiBlock pageMultiblock;

	public ElderWitchbookGui(final EntityPlayer player, final ItemStack stack) {
		this.player = player;
		this.stack = stack;
		bookPages = new NBTTagList();
		NBTTagCompound compound = new NBTTagCompound();
		// TODO entry localization fix
		String intro = Witchery.resource("witchery.book.rites1");
		String intro2 = Witchery.resource("witchery.book.rites2");
		compound.setString("Summary", intro);
		compound.setString("Summary2", intro2);
		bookPages.appendTag(compound);
		for(ElderRiteRegistry.Ritual ritual: ElderRiteRegistry.instance().getRituals()) {
			if(!ritual.showInBook()) {
				continue;
			}
			compound = new NBTTagCompound();
			compound.setString("Summary", ritual.getDescription());
			compound.setInteger("RitualID", ritual.ritualID);
			bookPages.appendTag(compound);
		}
		bookTotalPages = bookPages.tagCount();
		final NBTTagCompound stackCompound = stack.getTagCompound();
		if(stackCompound != null && stackCompound.hasKey("CurrentPage")) {
			currPage = Math.min(Math.max(stackCompound.getInteger("CurrentPage"), 0), Math.max(bookTotalPages - 1, 0));
		}
	}

	private void storeCurrentPage() {
		if(stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setInteger("CurrentPage", currPage);
	}

	public void updateScreen() {
		super.updateScreen();
		++updateCount;
	}

	@SuppressWarnings("unchecked")
	public void initGui() {
		buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		buttonList.add(new GuiButton(0, width / 2 - 100, 4 + bookImageHeight, 200, 20, I18n.format("gui.done")));
		final int i = (width - bookImageWidth) / 2;
		final byte b0 = 2;
		buttonList.add(buttonNextPage = new GuiButtonNext(1, i + 180, b0 + 154, true));
		buttonList.add(buttonPreviousPage = new GuiButtonNext(2, i + 110, b0 + 154, false));
		buttonList.add(buttonNextIngredientPage = new GuiButtonNext(10, i + 58, b0 + 154, true));
		buttonList.add(buttonPreviousIngredientPage = new GuiButtonNext(11, i - 12, b0 + 154, false));
		buttonList.add(new GuiButtonJump(9, i + 214, b0 + 138, 69, 48, 248));
		buttonList.add(new GuiButtonJump(8, i + 214, b0 + 118, 58, 40, 248));
		buttonList.add(new GuiButtonJump(7, i + 214, b0 + 98, 47, 32, 248));
		buttonList.add(new GuiButtonJump(6, i + 214, b0 + 78, 29, 24, 248));
		buttonList.add(new GuiButtonJump(5, i + 214, b0 + 58, 23, 16, 248));
		buttonList.add(new GuiButtonJump(4, i + 214, b0 + 38, 17, 8, 248));
		buttonList.add(new GuiButtonJump(3, i + 214, b0 + 18, 2, 0, 248));
		buttonList.add(new GuiButton(12, i + 125, b0 + 135, 70, 20, "botaniamisc.visualize"));
		((GuiButton)buttonList.get(12)).visible = false;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		sendBookToServer();
	}

	private void updateButtons() {
		buttonNextPage.visible = (currPage < bookTotalPages - 1);
		buttonPreviousPage.visible = (currPage > 0);
		if(buttonNextIngredientPage != null && buttonPreviousIngredientPage != null) {
			buttonNextIngredientPage.visible = (recipePageCurrent < recipePageCount - 1);
			buttonPreviousIngredientPage.visible = (recipePageCurrent > 0);
		}
	}

	private void sendBookToServer() {
		// TODO WHAT?
		if(player != null && currPage >= 0 && currPage < 1000 && player.inventory.currentItem >= 0 && player.inventory.getCurrentItem() != null) {
			Witchery.packetPipeline.sendToServer(new PacketItemUpdate(player.inventory.currentItem, currPage, player.inventory.getCurrentItem()));
		}
	}

	protected void actionPerformed(final GuiButton button) {
		if(button.enabled) {
			if(button.id == 0) {
				mc.displayGuiScreen(null);
			}
			else if(button.id == 1) {
				if(currPage < bookTotalPages - 1) {
					++currPage;
					recipePageCurrent = 0;
					recipePageCount = 1;
					storeCurrentPage();
				}
			}
			else if(button.id == 2) {
				if(currPage > 0) {
					--currPage;
					recipePageCurrent = 0;
					recipePageCount = 1;
					storeCurrentPage();
				}
			}
			else if(button.id == 10) {
				if(recipePageCurrent < recipePageCount - 1) {
					recipePageCurrent++;
				}
			}
			else if(button.id == 11) {
				if(recipePageCurrent > 0) {
					recipePageCurrent--;
				}
			}
			else if(button.id == 12) {
				if(pageMultiblock != null && pageMultiblock.set != null) {
					pageMultiblock.changeVisualization(button);
				}
			}
			else if(button instanceof GuiButtonJump) {
				final GuiButtonJump but = (GuiButtonJump)button;
				currPage = but.nextPage - 1;
				recipePageCurrent = 0;
				recipePageCount = 1;
				storeCurrentPage();
			}
			updateButtons();
		}
	}

	protected void keyTyped(final char par1, final int par2) {
		super.keyTyped(par1, par2);
	}

	public void drawScreen(final int par1, final int par2, final float par3) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(ElderWitchbookGui.DOUBLE_BOOK_TEXTURE);
		bookImageWidth = 256;
		final int k = (width - bookImageWidth) / 2;
		final byte b0 = 2;
		drawTexturedModalRect(k, b0, 0, 0, bookImageWidth, bookImageHeight);
		final String s4 = StatCollector.translateToLocalFormatted("book.pageIndicator", currPage + 1, bookTotalPages);
		String s5 = "";
		MultiBlock mb = new MultiBlock();
		if(bookPages != null && currPage >= 0 && currPage < bookPages.tagCount()) {
			final NBTTagCompound compound = bookPages.getCompoundTagAt(currPage);
			int ritualID = (compound.getInteger("RitualID"));
			if(ritualID > 0) {
				ElderRiteRegistry.Ritual ritual = ElderRiteRegistry.instance().getRitual(ritualID);
				s5 = ritual.getDescription();
				Figure[] circles = ritual.circles;
				for(Figure c: circles) {
					mb.mergeMultiblocks(c.getMultiblock());
				}
			}
			else {
				s5 = compound.getString("Summary");
			}
		}
		final int l = fontRendererObj.getStringWidth(s4);
		fontRendererObj.drawString(s4, k - l + bookImageWidth - 16, b0 + 16, 0);
		@SuppressWarnings("unchecked")
		List<String> list = fontRendererObj.listFormattedStringToWidth(s5, 98);
		recipePageCount = list.size() / 15 + ((list.size() % 15) == 0 ? 0 : 1);
		int initialHeight = b0 + 16;
		int ingredientPos = (recipePageCurrent) * 15;
		for(int i = ingredientPos; i < Math.min((recipePageCurrent + 1) * 15, list.size()); i++) {
			fontRendererObj.drawString(list.get(i), k + 20, initialHeight, 0, false);
			initialHeight += fontRendererObj.FONT_HEIGHT;
		}

		pageMultiblock = new PageMultiBlock(mb.makeSet(), k + 116, b0 + 3, bookImageWidth / 2 + 20, bookImageHeight, updateCount);
		pageMultiblock.renderScreen(this, par1, par2);
		if(!pageMultiblock.mb.equals(new MultiBlock())) {
			((GuiButton)buttonList.get(12)).displayString = pageMultiblock.getButtonStr();
			((GuiButton)buttonList.get(12)).visible = true;
		}
		else {
			((GuiButton)buttonList.get(12)).visible = false;
		}
		updateButtons();
		super.drawScreen(par1, par2, par3);
	}

	private static class GuiButtonNext extends GuiButton {

		private final boolean right;

		private GuiButtonNext(int id, int x, int y, boolean right) {
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

	private static class GuiButtonJump extends GuiButton {
		private final int nextPage;
		private final int iconX;
		private final int iconY;

		private GuiButtonJump(int commandID, int x, int y, int pageIndex, int iconX, int iconY) {
			super(commandID, x, y, 20, 20, "");
			nextPage = pageIndex;
			this.iconX = iconX;
			this.iconY = iconY;
		}

		public void drawButton(Minecraft mc, int x, int y) {
			if(!visible) {
				return;
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			boolean flag = x >= xPosition && y >= yPosition && x < xPosition + width && y < yPosition + height;
			mc.getTextureManager().bindTexture(DOUBLE_BOOK_TEXTURE);
			drawTexturedModalRect(xPosition, yPosition, flag ? 15 : 3, 220, 9, 24);
			if(iconX >= 0 && iconY >= 0) {
				drawTexturedModalRect(xPosition, yPosition + 9, iconX, iconY, 8, 8);
			}

		}
	}

}
