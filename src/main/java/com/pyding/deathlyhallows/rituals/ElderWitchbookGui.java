//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.pyding.deathlyhallows.rituals;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.network.PacketItemUpdate;
import com.pyding.deathlyhallows.client.render.multiblock.Multiblock;
import com.pyding.deathlyhallows.client.render.multiblock.PageMultiblock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ElderWitchbookGui extends GuiScreen
{
    private static final ResourceLocation field_110405_a;
    public static final ResourceLocation DOUBLE_BOOK_TEXTURE;
    private static final ResourceLocation[] field_110405_b;
    private static final String[] sizes;
    private final EntityPlayer player;
    private final ItemStack itemstack;
    private int updateCount;
    private int bookImageWidth;
    private final int bookImageHeight;
    private int bookTotalPages;
    private int currPage;
    private final NBTTagList bookPages;

    private int recipePageCount;
    private int recipePageCurrent;
    private String bookTitle;
    private GuiBookBase.GuiButtonNext buttonNextPage;
    private GuiBookBase.GuiButtonNext buttonPreviousPage;
    private GuiBookBase.GuiButtonNext buttonNextIngredientPage;
    private GuiBookBase.GuiButtonNext buttonPreviousIngredientPage;
    private GuiButton buttonDone;
    private ButtonJump buttonJumpPage1;
    private ButtonJump buttonJumpPage2;
    private ButtonJump buttonJumpPage3;
    private ButtonJump buttonJumpPage4;
    private ButtonJump buttonJumpPage5;
    private ButtonJump buttonJumpPage6;
    private ButtonJump buttonJumpPage7;
    private static final String CURRENT_PAGE_KEY = "CurrentPage";
    public PageMultiblock pageMultiblock;

    public ElderWitchbookGui(final EntityPlayer player, final ItemStack itemstack) {
        this.bookImageWidth = 192;
        this.bookImageHeight = 192;
        this.bookTotalPages = 1;
        this.bookTitle = "";
        this.player = player;
        this.itemstack = itemstack;
        this.bookTitle = itemstack.getDisplayName();
        this.bookPages = new NBTTagList();
		NBTTagCompound compound = new NBTTagCompound();
		final String intro = Witchery.resource("witchery.book.rites1");
		final String intro2 = Witchery.resource("witchery.book.rites2");
		final String anyCircles = Witchery.resource("witchery.book.rites.anycircle");
		compound.setString("Summary", intro);
		compound.setString("Summary2", intro2);
		this.bookPages.appendTag(compound);
		for (ElderRiteRegistry.Ritual ritual : ElderRiteRegistry.instance().getRituals()) {
			if (ritual.showInBook()) {
				compound = new NBTTagCompound();
				compound.setString("Summary", ritual.getDescription());
				compound.setInteger("RitualID", ritual.ritualID);
//                    final byte[] circles = ritual.getCircles();
//                    compound.setByteArray("Circles", circles);
//                    if (circles.length == 0) {
//                        compound.setString("Summary2", anyCircles);
//                    }
//                    else {
//                        final StringBuilder sb = new StringBuilder();
//                        for (final byte cir : circles) {
//                            if (sb.length() > 0) {
//                                sb.append(", ");
//                            }
//                            sb.append(GuiScreenWitchcraftBook.sizes[cir]);
//                        }
//                        compound.setString("Summary2", sb.toString());
//                    }
				this.bookPages.appendTag(compound);
			}
		}
        this.bookTotalPages = this.bookPages.tagCount();
        final NBTTagCompound stackCompound = itemstack.getTagCompound();
        if (stackCompound != null && stackCompound.hasKey("CurrentPage")) {
            this.currPage = Math.min(Math.max(stackCompound.getInteger("CurrentPage"), 0), Math.max(this.bookTotalPages, 1) - 1);
        }
    }

    private void storeCurrentPage() {
        if (this.itemstack.getTagCompound() == null) {
            this.itemstack.setTagCompound(new NBTTagCompound());
        }
        this.itemstack.getTagCompound().setInteger("CurrentPage", this.currPage);
    }

    public void updateScreen() {
        super.updateScreen();
        ++this.updateCount;
    }

    public void initGui() {
        buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        buttonList.add(this.buttonDone = new GuiButton(0, this.width / 2 - 100, 4 + this.bookImageHeight, 200, 20, I18n.format("gui.done")));
        final int i = (this.width - this.bookImageWidth) / 2;
        final byte b0 = 2;
		buttonList.add(this.buttonNextPage = new GuiBookBase.GuiButtonNext(1, i + 180, b0 + 154, true));
		buttonList.add(this.buttonPreviousPage = new GuiBookBase.GuiButtonNext(2, i + 110, b0 + 154, false));
		buttonList.add(this.buttonNextIngredientPage = new GuiBookBase.GuiButtonNext(10, i + 58, b0 + 154, true));
		buttonList.add(this.buttonPreviousIngredientPage = new GuiBookBase.GuiButtonNext(11, i - 12, b0 + 154, false));
		buttonList.add(this.buttonJumpPage7 = new ButtonJump(9, i + 214, b0 + 138, 69, 48, 248));
		buttonList.add(this.buttonJumpPage6 = new ButtonJump(8, i + 214, b0 + 118, 58, 40, 248));
		buttonList.add(this.buttonJumpPage5 = new ButtonJump(7, i + 214, b0 + 98, 47, 32, 248));
		buttonList.add(this.buttonJumpPage4 = new ButtonJump(6, i + 214, b0 + 78, 29, 24, 248));
		buttonList.add(this.buttonJumpPage3 = new ButtonJump(5, i + 214, b0 + 58, 23, 16, 248));
		buttonList.add(this.buttonJumpPage2 = new ButtonJump(4, i + 214, b0 + 38, 17, 8, 248));
		buttonList.add(this.buttonJumpPage1 = new ButtonJump(3, i + 214, b0 + 18, 2, 0, 248));
		buttonList.add(new GuiButton(12, i + 125, b0 + 135, 70, 20, "botaniamisc.visualize"));
		((GuiButton)buttonList.get(12)).visible=false;
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.sendBookToServer(false);
    }

    private void updateButtons() {
        this.buttonNextPage.visible = (this.currPage < this.bookTotalPages - 1);
        this.buttonPreviousPage.visible = (this.currPage > 0);
        if(buttonNextIngredientPage!=null && buttonPreviousIngredientPage!=null) {
            this.buttonNextIngredientPage.visible = (recipePageCurrent < recipePageCount - 1);
            this.buttonPreviousIngredientPage.visible = (recipePageCurrent > 0);
        }
    }

    private void sendBookToServer(final boolean par1) {
        if (this.player != null && this.currPage >= 0 && this.currPage < 1000 && this.player.inventory.currentItem >= 0 && this.player.inventory.getCurrentItem() != null) {
            Witchery.packetPipeline.sendToServer(new PacketItemUpdate(this.player.inventory.currentItem, this.currPage, this.player.inventory.getCurrentItem()));
        }
    }

    protected void actionPerformed(final GuiButton par1GuiButton) {
        if (par1GuiButton.enabled) {
            if (par1GuiButton.id == 0) {
                this.mc.displayGuiScreen(null);
            }
            else if (par1GuiButton.id == 1) {
                if (this.currPage < this.bookTotalPages - 1) {
                    ++this.currPage;
                    recipePageCurrent=0;
                    recipePageCount=1;
                    this.storeCurrentPage();
                }
            }
            else if (par1GuiButton.id == 2) {
                if (this.currPage > 0) {
                    --this.currPage;
                    recipePageCurrent=0;
                    recipePageCount=1;
                    this.storeCurrentPage();
                }
            }
            else if (par1GuiButton.id == 10) {
                if (recipePageCurrent < recipePageCount - 1) {
                    recipePageCurrent++;
                }
            }
            else if (par1GuiButton.id == 11) {
                if (recipePageCurrent > 0) {
                    recipePageCurrent--;
                }
            }
            else if (par1GuiButton.id == 12) {
                if(pageMultiblock !=null && pageMultiblock.set!=null){
                    pageMultiblock.changeVisualization(par1GuiButton);
                }
            }
            else if (par1GuiButton instanceof ButtonJump) {
                final ButtonJump but = (ButtonJump)par1GuiButton;
                this.currPage = but.nextPage - 1;
                recipePageCurrent=0;
                recipePageCount=1;
                this.storeCurrentPage();
            }
            this.updateButtons();
        }
    }

    protected void keyTyped(final char par1, final int par2) {
        super.keyTyped(par1, par2);
    }

    public void drawScreen(final int par1, final int par2, final float par3) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(ElderWitchbookGui.DOUBLE_BOOK_TEXTURE);
		this.bookImageWidth = 256;
		final int k = (this.width - this.bookImageWidth) / 2;
		final byte b0 = 2;
		this.drawTexturedModalRect(k, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
		final String s3 = "";
		final String s4 = I18n.format("book.pageIndicator", this.currPage + 1, this.bookTotalPages);
		String s5 = "";
		Multiblock mb = new Multiblock();
		if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
			final NBTTagCompound compound = this.bookPages.getCompoundTagAt(this.currPage);
			int ritualID = (compound.getInteger("RitualID"));
			s5 = "";

			if(ritualID>0){
				ElderRiteRegistry.Ritual ritual = ElderRiteRegistry.instance().getRitual(ritualID);
				s5=ritual.getDescription();
				Figure[] circles = ritual.circles;
				for(Figure c: circles){
					mb.mergeMultiblocks(c.getMultiblock());
				}
			}else{
				s5 = compound.getString("Summary");
			}
		}
		final int l = this.fontRendererObj.getStringWidth(s4);
		this.fontRendererObj.drawString(s4, k - l + this.bookImageWidth - 16, b0 + 16, 0);
		List<String> list = fontRendererObj.listFormattedStringToWidth(s5, 98);
		recipePageCount=list.size()/15+((list.size()%15)==0?0:1);
		int initialHeight = b0 + 16;
		int ingredPos = (recipePageCurrent)*15;
		for (int i=ingredPos;i<Math.min((recipePageCurrent+1)*15,list.size());i++)
		{
			fontRendererObj.drawString(list.get(i), k + 20 , initialHeight, 0, false);
			initialHeight+= fontRendererObj.FONT_HEIGHT;
		}

		pageMultiblock = new PageMultiblock(mb.makeSet(),k+116,b0+3,bookImageWidth/2+20,bookImageHeight,updateCount);
		pageMultiblock.renderScreen(this,par1,par2);
		if(!pageMultiblock.mb.equals(new Multiblock())){
			((GuiButton)buttonList.get(12)).displayString= pageMultiblock.getButtonStr();
			((GuiButton)buttonList.get(12)).visible=true;
		}else{
			((GuiButton)buttonList.get(12)).visible=false;
		}
		this.updateButtons();
        super.drawScreen(par1, par2, par3);
    }

    public static void drawTexturedQuadFit(final double x, final double y, final double width, final double height, final double zLevel) {
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0.0, y + height, zLevel, 0.0, 1.0);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, 1.0, 1.0);
        tessellator.addVertexWithUV(x + width, y + 0.0, zLevel, 1.0, 0.0);
        tessellator.addVertexWithUV(x + 0.0, y + 0.0, zLevel, 0.0, 0.0);
        tessellator.draw();
    }

    static ResourceLocation func_110404_g() {
        return ElderWitchbookGui.field_110405_a;
    }

    static {
        field_110405_a = new ResourceLocation("textures/gui/book.png");
        DOUBLE_BOOK_TEXTURE = new ResourceLocation("witchery", "textures/gui/bookDouble.png");
        field_110405_b = new ResourceLocation[] { new ResourceLocation("witchery", "textures/gui/circle_white_large.png"), new ResourceLocation("witchery", "textures/gui/circle_blue_large.png"), new ResourceLocation("witchery", "textures/gui/circle_red_large.png"), new ResourceLocation("witchery", "textures/gui/circle_white_medium.png"), new ResourceLocation("witchery", "textures/gui/circle_blue_medium.png"), new ResourceLocation("witchery", "textures/gui/circle_red_medium.png"), new ResourceLocation("witchery", "textures/gui/circle_white_small.png"), new ResourceLocation("witchery", "textures/gui/circle_blue_small.png"), new ResourceLocation("witchery", "textures/gui/circle_red_small.png") };
        sizes = new String[] { "§715x15§0", "§515x15§0", "§415x15§0", "§711x11§0", "§511x11§0", "§411x11§0", "§77x7§0", "§57x7§0", "§47x7§0" };
    }
}
