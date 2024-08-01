package com.pyding.deathlyhallows.events;

import com.pyding.deathlyhallows.multiblocks.MultiBlock;
import com.pyding.deathlyhallows.multiblocks.MultiBlockBlockAccess;
import com.pyding.deathlyhallows.multiblocks.MultiBlockComponent;
import com.pyding.deathlyhallows.multiblocks.MultiBlockSet;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import static org.lwjgl.opengl.GL11.*;

@SuppressWarnings("unused")
public final class DHMultiBlockRenderEvents {

	private static final Minecraft mc = Minecraft.getMinecraft();
	public static boolean rendering = false;
	private static final MultiBlockBlockAccess blockAccess = new MultiBlockBlockAccess();
	private static final RenderBlocks blockRender = RenderBlocks.getInstance();
	public static MultiBlockSet currentMultiBlock;
	public static ChunkCoordinates anchor;
	private static int
			angle,
			dimension;

	private static final DHMultiBlockRenderEvents INSTANCE = new DHMultiBlockRenderEvents();

	private DHMultiBlockRenderEvents() {

	}

	public static void init() {
		MinecraftForge.EVENT_BUS.register(INSTANCE);
	}

	public static void setMultiBlock(MultiBlockSet set) {
		currentMultiBlock = set;
		anchor = null;
		angle = 0;
		if(mc.theWorld != null) {
			dimension = mc.theWorld.provider.dimensionId;
		}
	}

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent e) {
		if(mc.thePlayer == null || mc.objectMouseOver == null || (mc.thePlayer.isSneaking() && anchor == null)) {
			return;
		}
		mc.thePlayer.getCurrentEquippedItem();
		renderPlayerLook(mc.thePlayer, mc.objectMouseOver);
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(currentMultiBlock == null || anchor != null || e.action != Action.RIGHT_CLICK_BLOCK || e.entityPlayer != mc.thePlayer) {
			return;
		}
		anchor = new ChunkCoordinates(e.x, e.y + 1, e.z);
		angle = MathHelper.floor_double(e.entityPlayer.rotationYaw * 4.0 / 360.0 + 0.5) & 3;
		e.setCanceled(true);
	}

	private void renderPlayerLook(EntityPlayer p, MovingObjectPosition mop) {
		if(currentMultiBlock == null || dimension != p.worldObj.provider.dimensionId) {
			return;
		}
		int anchorX = anchor != null ? anchor.posX : mop.blockX;
		int anchorY = anchor != null ? anchor.posY : mop.blockY + 1;
		int anchorZ = anchor != null ? anchor.posZ : mop.blockZ;

		glPushMatrix();
		glPushAttrib(GL_LIGHTING);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_LIGHTING);
		rendering = true;
		MultiBlock mb = anchor != null ? currentMultiBlock.getForIndex(angle) : currentMultiBlock.getForEntity(p);
		boolean didAny = false;

		blockAccess.update(p.worldObj, mb, anchorX, anchorY, anchorZ);

		for(MultiBlockComponent comp: mb.getComponents()) {
			if(renderComponentInWorld(p.worldObj, mb, comp, anchorX, anchorY, anchorZ)) {
				didAny = true;
			}
		}
		rendering = false;
		glPopAttrib();
		glPopMatrix();

		if(didAny) {
			return;
		}
		setMultiBlock(null);
		p.addChatComponentMessage(new ChatComponentTranslation("botaniamisc.structureComplete").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
	}

	private boolean renderComponentInWorld(World world, MultiBlock mb, MultiBlockComponent comp, int anchorX, int anchorY, int anchorZ) {
		ChunkCoordinates pos = comp.getRelativePosition();
		int x = pos.posX + anchorX;
		int y = pos.posY + anchorY;
		int z = pos.posZ + anchorZ;
		if(anchor != null && comp.matches(world, x, y, z)) {
			return false;
		}

		glPushMatrix();
		glTranslated(-RenderManager.renderPosX, -RenderManager.renderPosY, -RenderManager.renderPosZ);
		glDisable(GL_DEPTH_TEST);
		doRenderComponent(mb, comp, x, y, z, 0.4F);
		glPopMatrix();
		return true;
	}

	public static void renderMultiBlockOnPage(MultiBlock mb) {
		glTranslated(-0.5, -0.5, -0.5);
		blockAccess.update(null, mb, mb.offX, mb.offY, mb.offZ);
		for(MultiBlockComponent comp: mb.getComponents()) {
			ChunkCoordinates pos = comp.getRelativePosition();
			doRenderComponent(mb, comp, pos.posX + mb.offX, pos.posY + mb.offY, pos.posZ + mb.offZ, 1);
		}
	}

	private static void doRenderComponent(MultiBlock mb, MultiBlockComponent comp, int x, int y, int z, float alpha) {
		Block block = comp.getBlock();
		int meta = comp.getMeta();
		if(block == null) {
			return;
		}
		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		blockRender.useInventoryTint = false;
		
		glPushMatrix();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		if(block.isOpaqueCube()) {
			renderBlock(x, y, z, alpha, comp, block, meta);
			renderTileEntity(comp, block, meta);
		}
		else {
			renderBlockNonOpaque(comp, x, y, z, alpha, block);
		}
		glColor4f(1F, 1F, 1F, 1F);
		glEnable(GL_DEPTH_TEST);
		glPopMatrix();
	}

	private static void renderBlockNonOpaque(MultiBlockComponent comp, int x, int y, int z, float alpha, Block block) {
		int color;
		try {
			color = block.colorMultiplier(blockAccess, x, y, z);
		}
		catch(Exception e) {
			color = 0xFFFFFF;
		}
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		glColor4f(red, green, blue, alpha);
		IBlockAccess old = blockRender.blockAccess;
		blockRender.blockAccess = blockAccess;
		blockRender.renderAllFaces = true;
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		t.disableColor();
		try {
			blockRender.renderBlockByRenderType(block, x, y, z);
		}
		catch(Exception ignored) {
			comp.doFancyRender = false;
		}
		t.draw();
		blockRender.renderAllFaces = false;
		blockRender.blockAccess = old;
	}

	private static void renderBlock(int x, int y, int z, float alpha, MultiBlockComponent comp, Block block, int meta) {
		int color = block.getRenderColor(meta);
		float red = (color >> 16 & 255) / 255.0F;
		float green = (color >> 8 & 255) / 255.0F;
		float blue = (color & 255) / 255.0F;
		glColor4f(red, green, blue, alpha);
		glTranslated(x + 0.5, y + 0.5, z + 0.5);
		blockRender.renderBlockAsItem(block, meta, 1F);
	}

	private static void renderTileEntity(MultiBlockComponent comp, Block block, int meta) {
		if(block.getRenderType() != -1 || !block.hasTileEntity(meta)) {
			return;
		}
		TileEntity tile = block.createTileEntity(mc.theWorld, meta);
		if(!TileEntityRendererDispatcher.instance.hasSpecialRenderer(tile)) {
			return;
		}
		tile.blockMetadata = meta;
		if(comp.tag != null) {
			tile.readFromNBT(comp.tag);
		}
		try {
			glTranslated(-0.5, -0.5, -0.5);
			glPushMatrix();
			TileEntityRendererDispatcher.instance.getSpecialRenderer(tile).renderTileEntityAt(tile, 0, 0, 0, 0);
			glDisable(GL_LIGHTING);
		}
		finally {
			glPopMatrix();	
		}
	}

}
