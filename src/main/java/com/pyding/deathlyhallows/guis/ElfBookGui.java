//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.pyding.deathlyhallows.guis;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.events.DHMultiBlockRenderEvents;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.multiblocks.MultiBlock;
import com.pyding.deathlyhallows.multiblocks.PageMultiBlock;
import com.pyding.deathlyhallows.multiblocks.structures.DHStructures;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketElderBookPage;
import com.pyding.deathlyhallows.rituals.ElderRiteRegistry;
import com.pyding.deathlyhallows.utils.IMultiBlockHandler;
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

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class ElfBookGui extends GuiScreen {
	private static final ResourceLocation
			// TODO redraw in-color
			DOUBLE_BOOK_TEXTURE = new ResourceLocation(DHIntegration.WITCHERY, "textures/gui/bookDouble.png"),
			BOOK_TEXTURE = new ResourceLocation("textures/gui/book.png"),
			ICONS_TEXTURE = new ResourceLocation(DeathlyHallows.MODID, "textures/gui/book/icons.png");
	private final EntityPlayer player;
	private final ItemStack stack;
	private final int
			guiWidth = 256,
			guiHeight = 192,
			bookTotalPages;
	private int
			updateCount,
			currPage,
			visualizedPage = -1,
			recipePageCount,
			recipePageCurrent;
	private final NBTTagList bookPages;
	private GuiButtonNext
			buttonNextPage,
			buttonPreviousPage,
			buttonNextIngredientPage,
			buttonPreviousIngredientPage;
	private PageMultiBlock pageMultiBlock;

	public ElfBookGui(EntityPlayer player, ItemStack stack) {
		this.player = player;
		this.stack = stack;
		bookPages = new NBTTagList();
		NBTTagCompound tag = new NBTTagCompound();
		// TODO entry localization fix
		String intro = Witchery.resource("witchery.book.rites1");
		String intro2 = Witchery.resource("witchery.book.rites2");
		tag.setString("Summary", intro);
		tag.setString("Summary2", intro2);
		bookPages.appendTag(tag);
		for(ElderRiteRegistry.Ritual ritual: ElderRiteRegistry.instance().getRituals()) {
			if(!ritual.showInBook()) {
				continue;
			}
			tag = new NBTTagCompound();
			tag.setString("Summary", ritual.getDescription());
			tag.setInteger("RitualID", ritual.ritualID);
			bookPages.appendTag(tag);
		}
		bookTotalPages = bookPages.tagCount();
		final NBTTagCompound bookTag = stack.getTagCompound();
		if(bookTag != null) {
			if(bookTag.hasKey("CurrentPage")) {
				currPage = Math.min(Math.max(bookTag.getInteger("CurrentPage"), 0), Math.max(bookTotalPages - 1, 0));
			}
			if(bookTag.hasKey("VisualizedPage") && DHMultiBlockRenderEvents.currentMultiBlock != null) {
				visualizedPage = Math.min(Math.max(bookTag.getInteger("VisualizedPage"), 0), Math.max(bookTotalPages - 1, 0));
			}
		}
	}

	private void storeCurrentPage() {
		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}

		tag.setInteger("CurrentPage", currPage);
		if(visualizedPage != -1) {
			tag.setInteger("VisualizedPage", visualizedPage);
		}
		else {
			tag.removeTag("VisualizedPage");
		}
	}

	public void updateScreen() {
		super.updateScreen();
		++updateCount;
	}

	@SuppressWarnings("unchecked")
	public void initGui() {
		buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		buttonList.add(new GuiButton(0, width / 2 - 100, 4 + guiHeight, 200, 20, I18n.format("gui.done")));
		
		final int
				left = (width - guiWidth) / 2,
				top = 2;
		buttonList.add(buttonNextPage = new GuiButtonNext(1, left + 212, top + 154, true));
		buttonList.add(buttonPreviousPage = new GuiButtonNext(2, left + 142, top + 154, false));
		buttonList.add(buttonNextIngredientPage = new GuiButtonNext(10, left + 90, top + 154, true));
		buttonList.add(buttonPreviousIngredientPage = new GuiButtonNext(11, left + 20, top + 154, false));
		buttonList.add(new GuiButtonJump(9, left + 246, top + 138, 69, 48, 248));
		buttonList.add(new GuiButtonJump(8, left + 246, top + 118, 58, 40, 248));
		buttonList.add(new GuiButtonJump(7, left + 246, top + 98, 47, 32, 248));
		buttonList.add(new GuiButtonJump(6, left + 246, top + 78, 29, 24, 248));
		buttonList.add(new GuiButtonJump(5, left + 246, top + 58, 23, 16, 248));
		buttonList.add(new GuiButtonJump(4, left + 246, top + 38, 17, 8, 248));
		buttonList.add(new GuiButtonJump(3, left + 246, top + 18, 2, 0, 248));
		buttonList.add(new GuiButtonVisualize(12, left + 183, top + 156));
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
		if(player != null && currPage >= 0 && currPage < 1000 && player.inventory.currentItem >= 0) {
			ItemStack book = player.inventory.getCurrentItem();
			if(book != null && book.getItem() == DHItems.elderBook) {
				DHPacketProcessor.sendToServer(new PacketElderBookPage(player.inventory.currentItem, currPage, visualizedPage));
			}
		}
	}

	protected void actionPerformed(GuiButton button) {
		if(!button.enabled) {
			return;
		}
		if(button instanceof GuiButtonJump) {
			currPage = ((GuiButtonJump)button).jumpToPage - 1;
			recipePageCurrent = 0;
			recipePageCount = 1;
			storeCurrentPage();
		}
		else {
			switch(button.id) {
				case 0: {
					mc.displayGuiScreen(null);
					break;
				}

				case 1: {
					if(currPage < bookTotalPages - 1) {
						++currPage;
						recipePageCurrent = 0;
						recipePageCount = 1;
						storeCurrentPage();
					}
					break;
				}
				case 2: {
					if(currPage > 0) {
						--currPage;
						recipePageCurrent = 0;
						recipePageCount = 1;
						storeCurrentPage();
					}
					break;
				}
				case 10: {
					if(recipePageCurrent < recipePageCount - 1) {
						recipePageCurrent++;
					}
					break;
				}
				case 11: {
					if(recipePageCurrent > 0) {
						recipePageCurrent--;
					}
					break;
				}
				case 12: {
					if(visualizedPage == currPage) {
						visualizedPage = -1;
						PageMultiBlock.resetVisualization();
						break;
					}
					if(pageMultiBlock != null && pageMultiBlock.set != null) {
						visualizedPage = currPage;
						pageMultiBlock.setVisualization();
					}
					break;
				}
			}
		}
		updateButtons();
	}

	public void drawScreen(int x, int y, float partial) {
		glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(ElfBookGui.DOUBLE_BOOK_TEXTURE);
		final int 
				left = (width - guiWidth) / 2,
				top = 2;
		drawTexturedModalRect(left, top, 0, 0, guiWidth, guiHeight);
		final String s4 = StatCollector.translateToLocalFormatted("book.pageIndicator", currPage + 1, bookTotalPages);
		String s5 = "";
		MultiBlock mb = new MultiBlock();
		if(bookPages != null && currPage >= 0 && currPage < bookPages.tagCount()) {
			final NBTTagCompound compound = bookPages.getCompoundTagAt(currPage);
			int ritualID = (compound.getInteger("RitualID"));
			if(ritualID > 0) {
				ElderRiteRegistry.Ritual ritual = ElderRiteRegistry.instance().getRitual(ritualID);
				s5 = ritual.getDescription();
				IMultiBlockHandler[] circles = ritual.circles;
				for(IMultiBlockHandler c: circles) {
					mb.mergeMultiBlocks(c.getMultiBlock());
				}
			}
			else {
				s5 = compound.getString("Summary");
			}
		}
		final int l = fontRendererObj.getStringWidth(s4);
		fontRendererObj.drawString(s4, left - l + guiWidth - 16, top + 16, 0);
		@SuppressWarnings("unchecked")
		List<String> list = fontRendererObj.listFormattedStringToWidth(s5, 98);
		recipePageCount = list.size() / 15 + ((list.size() % 15) == 0 ? 0 : 1);
		int initialHeight = top + 16;
		int ingredientPos = (recipePageCurrent) * 15;
		for(int i = ingredientPos; i < Math.min((recipePageCurrent + 1) * 15, list.size()); i++) {
			fontRendererObj.drawString(list.get(i), left + 20, initialHeight, 0, false);
			initialHeight += fontRendererObj.FONT_HEIGHT;
		}

		pageMultiBlock = new PageMultiBlock(mb.makeSet(), left + guiWidth / 2, top, guiWidth / 2, guiHeight, updateCount);
		pageMultiBlock.renderScreen(x, y);
		((GuiButton)buttonList.get(12)).visible = !pageMultiBlock.mb.equals(DHStructures.EMPTY.getMultiBlock());
		updateButtons();
		super.drawScreen(x, y, partial);
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
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(BOOK_TEXTURE);
			boolean selected = x >= xPosition && x < xPosition + width && y >= yPosition && y < yPosition + height;
			drawTexturedModalRect(xPosition, yPosition, selected ? 23 : 0, right ? 192 : 205, 23, 13);
		}

	}

	private static class GuiButtonJump extends GuiButton {
		private final int jumpToPage;
		private final int iconX;
		private final int iconY;

		private GuiButtonJump(int id, int x, int y, int page, int iconX, int iconY) {
			super(id, x, y, 20, 20, "");
			jumpToPage = page;
			this.iconX = iconX;
			this.iconY = iconY;
		}

		public void drawButton(Minecraft mc, int x, int y) {
			if(!visible) {
				return;
			}
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			boolean flag = x >= xPosition && y >= yPosition && x < xPosition + width && y < yPosition + height;
			mc.getTextureManager().bindTexture(DOUBLE_BOOK_TEXTURE);
			drawTexturedModalRect(xPosition, yPosition, flag ? 15 : 3, 220, 9, 24);
			if(iconX >= 0 && iconY >= 0) {
				drawTexturedModalRect(xPosition, yPosition + 9, iconX, iconY, 8, 8);
			}

		}
	}

	public class GuiButtonVisualize extends GuiButton {

		public GuiButtonVisualize(int id, int x, int y) {
			super(id, x, y, 14, 14, "");
		}

		public void drawButton(Minecraft mc, int x, int y) {
			if(!visible) {
				return;
			}
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(ICONS_TEXTURE);
			boolean selected = x >= xPosition && x < xPosition + width && y >= yPosition && y < yPosition + height;
			boolean visualized = visualizedPage == currPage;
			drawTexturedModalRect(xPosition, yPosition, selected ? 12 : 0, visualized ? 10 : 0, 12, 10);
		}

	}

}
