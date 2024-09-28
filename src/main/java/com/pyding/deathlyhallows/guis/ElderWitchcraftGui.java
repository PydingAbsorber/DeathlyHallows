package com.pyding.deathlyhallows.guis;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.events.DHMultiBlockRenderEvents;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.multiblocks.MultiBlock;
import com.pyding.deathlyhallows.multiblocks.PageMultiBlock;
import com.pyding.deathlyhallows.multiblocks.structures.DHStructures;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketElderBookPage;
import com.pyding.deathlyhallows.rituals.ElderRites;
import com.pyding.deathlyhallows.utils.DHUtils;
import com.pyding.deathlyhallows.utils.IMultiBlockHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
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
public class ElderWitchcraftGui extends GuiScreen {
	private static final ResourceLocation TEXTURE = new ResourceLocation(DeathlyHallows.MODID, "textures/gui/bookDouble.png");
	private final EntityPlayer player;
	private final ItemStack stack;
	private final int
			guiWidth = 256,
			guiHeight = 192,
			bookTotalPages;
	private int
			updateCount,
			currPage,
			visualizedPage = 0,
			recipePageCount,
			recipePageCurrent;
	private final NBTTagList bookPages;
	private GuiButtonNext
			buttonNextPage,
			buttonPreviousPage,
			buttonNextIngredientPage,
			buttonPreviousIngredientPage;
	private PageMultiBlock pageMultiBlock;

	private int currentLayer = -1;

	public ElderWitchcraftGui(EntityPlayer player, ItemStack stack) {
		this.player = player;
		this.stack = stack;
		bookPages = new NBTTagList();
		NBTTagCompound tag = new NBTTagCompound();
		// TODO entry localization fix
		String intro = Witchery.resource("dh.book.rites1");
		tag.setString("Summary", intro);
		bookPages.appendTag(tag);
		for(ElderRites.ElderRitual ritual: ElderRites.getSortedRituals()) {
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
		buttonList.add(new GuiButton(0, width / 2 - 100, 4 + guiHeight, 200, 20, StatCollector.translateToLocal("gui.done")));
		final int
				left = (width - guiWidth) / 2,
				top = 2;
		buttonList.add(buttonNextPage = new GuiButtonNext(1, left + 212, top + 154, true));
		buttonList.add(buttonPreviousPage = new GuiButtonNext(2, left + 142, top + 154, false));
		buttonList.add(buttonNextIngredientPage = new GuiButtonNext(3, left + 90, top + 154, true));
		buttonList.add(buttonPreviousIngredientPage = new GuiButtonNext(4, left + 20, top + 154, false));
		buttonList.add(new GuiButtonVisualize(5, left + 183, top + 155));
		buttonList.add(new GuiButtonLayer(6, left + 230, top + 26));
		ElderRites.Category[] values = ElderRites.Category.values();
		int pages = bookTotalPages + 1;
		for(int i = values.length - 1; i >= 0; --i) {
			int categorySize = ElderRites.getRituals(values[i]).size();
			if(categorySize == 0) {
				continue; // no need to draw category then.
			}
			// don't ask why. it's just reverse dynamic page calculation, because f*ck static constants
			pages -= categorySize;
			buttonList.add(new GuiButtonJump(7 + i, left + 246 + 10 * (i / 7), top + 18 + 20 * (i % 7), pages, 8 * values[i].ordinal(), 248));
		}
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
		if(player == null || currPage < 0 || currPage >= 1000 || player.inventory.currentItem < 0) {
			return;
		}
		ItemStack book = player.inventory.getCurrentItem();
		if(book == null || book.getItem() != DHItems.elderBook) {
			return;
		}
		DHPacketProcessor.sendToServer(new PacketElderBookPage(player.inventory.currentItem, currPage, visualizedPage));
	}

	protected void actionPerformed(GuiButton button) {
		if(!button.enabled) {
			return;
		}
		if(button instanceof GuiButtonLayer) {
			++currentLayer;
			currentLayer %= pageMultiBlock.mb.getYSize();
			updateButtons();
			return;
		}
		currentLayer = -1;
		if(button instanceof GuiButtonJump) {
			currPage = ((GuiButtonJump)button).jumpToPage - 1;
			recipePageCurrent = 0;
			recipePageCount = 1;
			storeCurrentPage();
			updateButtons();
			return;
		}
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
			case 3: {
				if(recipePageCurrent < recipePageCount - 1) {
					recipePageCurrent++;
				}
				break;
			}
			case 4: {
				if(recipePageCurrent > 0) {
					recipePageCurrent--;
				}
				break;
			}
			case 5: {
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
		updateButtons();
	}

	public void drawScreen(int x, int y, float partial) {
		glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(TEXTURE);
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
				ElderRites.ElderRitual ritual = ElderRites.getRitual(ritualID);
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
		if(currentLayer == -1) {
			currentLayer = pageMultiBlock.mb.getYSize() - 1;
		}
		pageMultiBlock.renderScreen(x, y, currentLayer);
		if(currPage > 0) {
			pageMultiBlock.renderMaterialsTooltip(x, y);
		}
		boolean hasBultiblock = !pageMultiBlock.mb.equals(DHStructures.EMPTY.getMultiBlock());
		((GuiButton)buttonList.get(5)).visible = hasBultiblock;
		((GuiButton)buttonList.get(6)).visible = hasBultiblock && pageMultiBlock.mb.getYSize() > 1;
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
			mc.getTextureManager().bindTexture(TEXTURE);
			boolean selected = x >= xPosition && x < xPosition + width && y >= yPosition && y < yPosition + height;
			drawTexturedModalRect(xPosition, yPosition, selected ? 23 : 0, right ? 192 : 205, 23, 13);
		}

	}

	private static class GuiButtonJump extends GuiButton {
		private final int jumpToPage;
		private final int iconX;
		private final int iconY;

		private GuiButtonJump(int id, int x, int y, int page, int iconX, int iconY) {
			super(id, x, y, 10, 20, "");
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
			mc.getTextureManager().bindTexture(TEXTURE);
			if(flag) {
				zLevel += 100;
			}
			drawTexturedModalRect(xPosition, yPosition, flag ? 15 : 3, 220, 9, 24);
			// long
			if(id > 8 + 5) {
				int width = 10;
				DHUtils.drawTexturedRect(xPosition - width, yPosition, (int)zLevel, width, 24, flag ? 15 : 3, 220, 1, 24, 256, 256);
			}
			if(iconX >= 0 && iconY >= 0) {
				drawTexturedModalRect(xPosition, yPosition + 9, iconX, iconY, 8, 8);
			}
			if(flag) {
				zLevel -= 100;
			}
		}

	}

	public class GuiButtonVisualize extends GuiButton {

		public GuiButtonVisualize(int id, int x, int y) {
			super(id, x, y, 12, 12, "");
			visible = false;
		}

		public void drawButton(Minecraft mc, int x, int y) {
			if(!visible) {
				return;
			}
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(TEXTURE);
			boolean selected = x >= xPosition && x < xPosition + width && y >= yPosition && y < yPosition + height;
			boolean visualized = visualizedPage == currPage;
			drawTexturedModalRect(xPosition, yPosition + 1, (selected ? 12 : 0) + 45, (visualized ? 10 : 0) + 193, 12, 10);
		}

	}

	public class GuiButtonLayer extends GuiButton {

		public GuiButtonLayer(int id, int x, int y) {
			super(id, x, y, 11, 11, "");
			visible = false;
		}

		public void drawButton(Minecraft mc, int x, int y) {
			if(!visible) {
				return;
			}
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(TEXTURE);
			boolean selected = x >= xPosition && x < xPosition + width && y >= yPosition && y < yPosition + height;
			drawTexturedModalRect(xPosition, yPosition, (selected ? 11 : 0) + 69, 193, 11, 11);
		}

	}

}
