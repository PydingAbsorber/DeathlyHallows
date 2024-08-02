//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.pyding.deathlyhallows.guis;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.network.PacketItemUpdate;
import com.pyding.deathlyhallows.DeathlyHallows;
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
			BOOK_TEXTURE = new ResourceLocation("textures/gui/book.png"),
			ICONS_TEXTURE = new ResourceLocation(DeathlyHallows.MODID, "textures/gui/book/icons.png");
	private final EntityPlayer player;
	private final ItemStack stack;
	private int updateCount;
	private int bookImageWidth = 192;
	private final int bookImageHeight = 192;
	private final int bookTotalPages;
	private int currPage;
	private int recipePageCount;
	private int recipePageCurrent;
	private final NBTTagList bookPages;
	private GuiButtonNext
			buttonNextPage,
			buttonPreviousPage,
			buttonNextIngredientPage,
			buttonPreviousIngredientPage;
	private PageMultiBlock pageMultiBlock;

	private static PageMultiBlock visualizedMultiBlock;
	public static int visualizedPage;

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
		final int
				x = (width - bookImageWidth) / 2,
				y = 2;
		buttonList.add(buttonNextPage = new GuiButtonNext(1, x + 180, y + 154, true));
		buttonList.add(buttonPreviousPage = new GuiButtonNext(2, x + 110, y + 154, false));
		buttonList.add(buttonNextIngredientPage = new GuiButtonNext(10, x + 58, y + 154, true));
		buttonList.add(buttonPreviousIngredientPage = new GuiButtonNext(11, x - 12, y + 154, false));
		buttonList.add(new GuiButtonJump(9, x + 214, y + 138, 69, 48, 248));
		buttonList.add(new GuiButtonJump(8, x + 214, y + 118, 58, 40, 248));
		buttonList.add(new GuiButtonJump(7, x + 214, y + 98, 47, 32, 248));
		buttonList.add(new GuiButtonJump(6, x + 214, y + 78, 29, 24, 248));
		buttonList.add(new GuiButtonJump(5, x + 214, y + 58, 23, 16, 248));
		buttonList.add(new GuiButtonJump(4, x + 214, y + 38, 17, 8, 248));
		buttonList.add(new GuiButtonJump(3, x + 214, y + 18, 2, 0, 248));
		buttonList.add(new GuiButtonVisualize(12, x + 151, y + 156));
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
		if(!button.enabled) {
			return;
		}
		if(button instanceof GuiButtonJump) {
			currPage = ((GuiButtonJump)button).jumpToPage - 1;
			recipePageCurrent = 0;
			recipePageCount = 1;
			storeCurrentPage();
		}
		else switch(button.id) {
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
		updateButtons();
	}

	protected void keyTyped(final char par1, final int par2) {
		super.keyTyped(par1, par2);
	}

	public void drawScreen(final int x, final int y, final float par3) {
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

		pageMultiBlock = new PageMultiBlock(mb.makeSet(), k + 116, b0 + 3, bookImageWidth / 2 + 20, bookImageHeight, updateCount);
		pageMultiBlock.renderScreen(this, x, y);
		if(!pageMultiBlock.mb.equals(new MultiBlock())) {
			((GuiButton)buttonList.get(12)).displayString = pageMultiBlock.getButtonStr();
			((GuiButton)buttonList.get(12)).visible = true;
		}
		else {
			((GuiButton)buttonList.get(12)).visible = false;
		}
		updateButtons();
		super.drawScreen(x, y, par3);
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
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

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
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(ICONS_TEXTURE);
			boolean selected = x >= xPosition && x < xPosition + width && y >= yPosition && y < yPosition + height;
			boolean visualized = visualizedPage == currPage;
			drawTexturedModalRect(xPosition, yPosition, selected ? 12 : 0, visualized ? 10 : 0, 12, 10);
		}

	}

}
