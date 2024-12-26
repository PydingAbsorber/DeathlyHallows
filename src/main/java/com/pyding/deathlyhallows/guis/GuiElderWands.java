package com.pyding.deathlyhallows.guis;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketElderBookPage;
import com.pyding.deathlyhallows.symbols.DHSymbols;
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
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class GuiElderWands extends GuiScreen {

	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/book.png");
	private final EntityPlayer player;
	private final ItemStack stack;
	private final int bookTotalPages, bookImageWidth = 192, bookImageHeight = 192, linesPerPage = 13;
	private int currPage;
	private final NBTTagList bookPages;
	private GuiButtonNext
			buttonNextPage,
			buttonPreviousPage;

	public GuiElderWands(EntityPlayer player, ItemStack stack) {
		this.player = player;
		this.stack = stack;
		bookPages = new NBTTagList();
		addTextToBook(Witchery.resource("dh.book.wands1"));
		for(SymbolEffect effect: DHSymbols.ELDER_SYMBOLS) {
			addTextToBook(effect.getDescription());
		}
		bookTotalPages = bookPages.tagCount();
		NBTTagCompound tag = stack.getTagCompound();
		if(tag != null && tag.hasKey("CurrentPage")) {
			currPage = Math.min(Math.max(tag.getInteger("CurrentPage"), 0), Math.max(bookTotalPages, 1) - 1);
		}
	}

	private void addTextToBook(String text) {
		@SuppressWarnings("unchecked")
		List<String> description = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(trimStringNewline(text), 116);
		for(int i = 0; i < description.size(); i += linesPerPage) {
			int arrayEnd = i + Math.min(linesPerPage, description.size() - i);
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("Summary", String.join("\n", description.subList(i, arrayEnd)));
			bookPages.appendTag(tag);
		}
	}

	private String trimStringNewline(String s) {
		while(s != null && s.endsWith("\n")) {
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}

	private void storeCurrentPage() {
		if(stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setInteger("CurrentPage", currPage);
	}

	public void initGui() {
		super.buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		buttonList.add(new GuiButton(0, width / 2 - 100, 4 + bookImageHeight, 200, 20, I18n.format("gui.done")));
		int i = (width - bookImageWidth) / 2;
		buttonList.add(buttonNextPage = new GuiButtonNext(1, i + 120, 156, true));
		buttonList.add(buttonPreviousPage = new GuiButtonNext(2, i + 38, 156, false));
		updateButtons();
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		sendBookToServer();
	}

	private void updateButtons() {
		buttonNextPage.visible = currPage < bookTotalPages - 1;
		buttonPreviousPage.visible = currPage > 0;
	}

	private void sendBookToServer() {
		if(player == null || currPage < 0 || currPage >= 1000 || player.inventory.currentItem < 0) {
			return;
		}
		ItemStack book = player.inventory.getCurrentItem();
		if(book == null || book.getItem() != DHItems.elderBook) {
			return;
		}
		DHPacketProcessor.sendToServer(new PacketElderBookPage(player.inventory.currentItem, currPage, -1));
	}

	protected void actionPerformed(GuiButton button) {
		if(!button.enabled) {
			return;
		}
		if(button.id == 0) {
			mc.displayGuiScreen(null);
		}
		else if(button.id == 1) {
			if(currPage < bookTotalPages - 1) {
				++currPage;
				storeCurrentPage();
			}
		}
		else if(button.id == 2) {
			if(currPage > 0) {
				--currPage;
				storeCurrentPage();
			}
		}
		updateButtons();
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int wid = (width - bookImageWidth) / 2;
		NBTTagCompound compound;
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(wid, (byte)2, 0, 0, bookImageWidth, bookImageHeight);
		String s = I18n.format("book.pageIndicator", currPage + 1, bookTotalPages);
		String s1 = "";
		String s2 = "";
		if(bookPages != null && currPage >= 0 && currPage < bookPages.tagCount()) {
			compound = bookPages.getCompoundTagAt(currPage);
			s1 = compound.getString("Summary");
			s2 = compound.getString("Details");
		}
		fontRendererObj.drawString(s, wid - fontRendererObj.getStringWidth(s) + bookImageWidth - 44, 2 + 16, 0);
		fontRendererObj.drawSplitString(s1, wid + 36, 2 + 32, 116, 0);
		if(s2 != null && !s2.isEmpty()) {
			fontRendererObj.drawSplitString(s2, wid + 36, 2 + 32 + 34, 116, 0);
		}

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
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(TEXTURE);
			boolean selected = x >= xPosition && x < xPosition + width && y >= yPosition && y < yPosition + height;
			drawTexturedModalRect(xPosition, yPosition, selected ? 23 : 0, right ? 192 : 205, 23, 13);
		}

	}

}
