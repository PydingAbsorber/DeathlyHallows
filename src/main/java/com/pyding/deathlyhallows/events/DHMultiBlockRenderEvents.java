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
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import static net.minecraftforge.common.util.ForgeDirection.*;
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
		if(currentMultiBlock == null || anchor != null || e.action == Action.LEFT_CLICK_BLOCK || e.entityPlayer != mc.thePlayer) {
			return;
		}
		if(e.action == Action.RIGHT_CLICK_BLOCK) {
			anchor = new ChunkCoordinates(e.x, e.y + 1, e.z);
		}
		else {
			MovingObjectPosition mop = mc.objectMouseOver;
			anchor = new ChunkCoordinates(mop.blockX, mop.blockY + 1, mop.blockZ);
		}
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
		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
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
		p.addChatComponentMessage(new ChatComponentTranslation("dh.chat.structureComplete").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
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
		doRenderComponent(comp, x, y, z, 0.4F);
		glPopMatrix();
		return true;
	}

	public static void renderMultiBlockOnPage(MultiBlock mb, int layer) {
		glTranslated(-0.5, -0.5, -0.5);
		blockAccess.update(null, mb, mb.anchorX, mb.anchorY, mb.anchorZ);
		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		for(MultiBlockComponent comp: mb.getComponents()) {
			ChunkCoordinates pos = comp.getRelativePosition();
			if(pos.posY - mb.minY > layer) {
				continue;
			}
			doRenderComponent(comp, pos.posX + mb.anchorX, pos.posY + mb.anchorY, pos.posZ + mb.anchorZ, 1);
		}
	}

	private static void doRenderComponent(MultiBlockComponent comp, int x, int y, int z, float alpha) {
		Block block = comp.getBlock();
		int meta = comp.getMeta();
		if(block == null) {
			return;
		}
		blockRender.useInventoryTint = false;
		glPushMatrix();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		if(block.getRenderType() != -1) {
			if(block.isOpaqueCube() || block.renderAsNormalBlock() || !comp.doFancyRender) {
				renderBlock(x, y, z, alpha, comp, block, meta);
			}
			else {
				renderBlockSpecial(comp, x, y, z, alpha, block, meta);
			}
		}
		if(blockAccess.hasBlockAccess()) {
			// render in-world
			renderTileEntity(x, y, z, comp, block, meta);
		}
		else {
			// render in-book, using world as buffer for block data, so need to be placed at player in order to ensure that chunk is exists
			int
					posX = MathHelper.floor_double(mc.thePlayer.posX),
					posY = MathHelper.floor_double(mc.thePlayer.posY),
					posZ = MathHelper.floor_double(mc.thePlayer.posZ);
			glTranslated(-posX, -posY, -posZ);
			glTranslated(x, y, z);
			renderTileEntity(posX, posY, posZ, comp, block, meta);
		}
		glColor4f(1F, 1F, 1F, 1F);
		glEnable(GL_DEPTH_TEST);
		glPopMatrix();
	}

	private static void renderBlockSpecial(MultiBlockComponent comp, int x, int y, int z, float alpha, Block block, int meta) {
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
		boolean fake = !blockAccess.hasBlockAccess();
		if(fake) {
			blockAccess.setOriginal(mc.theWorld);
		}
		blockRender.renderAllFaces = true;
		if(block.getRenderType() == 0) { // handle witchery reshaped blocks
			try {
				glPushMatrix();
				glTranslated(x + 0.5, y + 0.5, z + 0.5);
				renderInventory(block);
				glPopMatrix();
			}
			catch(Exception ignored) {
				comp.doFancyRender = false;
			}
		}
		else { // vanilla blocks or forge will handle
			Tessellator t = Tessellator.instance;
			t.startDrawingQuads();
			try {
				t.disableColor();
				blockRender.renderBlockByRenderType(block, x, y, z);
			}
			catch(Exception ignored) {
				comp.doFancyRender = false;
			}
			t.draw();
		}
		blockRender.renderAllFaces = false;
		blockRender.blockAccess = old;
		if(fake) {
			blockAccess.setOriginal(null);
		}
	}

	private static void renderInventory(Block block) {
		IBlockAccess access = blockRender.blockAccess;
		Tessellator t = Tessellator.instance;
		t.disableColor();
		block.setBlockBoundsForItemRender();
		glTranslatef(-0.5F, -0.5F, -0.5F);
		blockRender.setRenderBoundsFromBlock(block);
		if(block.shouldSideBeRendered(access, DOWN.offsetX, DOWN.offsetY, DOWN.offsetZ, DOWN.ordinal())) {
			t.startDrawingQuads();
			t.setNormal(0.0F, -1.0F, 0.0F);
			blockRender.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, blockRender.getBlockIcon(block, access, 0, 0, 0, 0));
			t.draw();
		}
		if(block.shouldSideBeRendered(access, UP.offsetX, UP.offsetY, UP.offsetZ, UP.ordinal())) {
			t.startDrawingQuads();
			t.setNormal(0.0F, 1.0F, 0.0F);
			blockRender.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, blockRender.getBlockIcon(block, access, 0, 0, 0, 1));
			t.draw();
		}
		if(block.shouldSideBeRendered(access, NORTH.offsetX, NORTH.offsetY, NORTH.offsetZ, NORTH.ordinal())) {
			t.startDrawingQuads();
			t.setNormal(0.0F, 0.0F, -1.0F);
			blockRender.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, blockRender.getBlockIcon(block, access, 0, 0, 0, 2));
			t.draw();
		}
		if(block.shouldSideBeRendered(access, SOUTH.offsetX, SOUTH.offsetY, SOUTH.offsetZ, SOUTH.ordinal())) {
			t.startDrawingQuads();
			t.setNormal(0.0F, 0.0F, 1.0F);
			blockRender.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, blockRender.getBlockIcon(block, access, 0, 0, 0, 3));
			t.draw();
		}
		if(block.shouldSideBeRendered(access, WEST.offsetX, WEST.offsetY, WEST.offsetZ, WEST.ordinal())) {
			t.startDrawingQuads();
			t.setNormal(-1.0F, 0.0F, 0.0F);
			blockRender.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, blockRender.getBlockIcon(block, access, 0, 0, 0, 4));
			t.draw();
		}
		if(block.shouldSideBeRendered(access, EAST.offsetX, EAST.offsetY, EAST.offsetZ, EAST.ordinal())) {
			t.startDrawingQuads();
			t.setNormal(1.0F, 0.0F, 0.0F);
			blockRender.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, blockRender.getBlockIcon(block, access, 0, 0, 0, 5));
			t.draw();
		}
	}

	private static void renderBlock(int x, int y, int z, float alpha, MultiBlockComponent comp, Block block, int meta) {
		int color = block.getRenderColor(meta);
		float red = (color >> 16 & 255) / 255.0F;
		float green = (color >> 8 & 255) / 255.0F;
		float blue = (color & 255) / 255.0F;
		glColor4f(red, green, blue, alpha);
		glTranslated(x + 0.5, y + 0.5, z + 0.5);
		glRotatef(-90F, 0F, 1F, 0F);
		blockRender.renderBlockAsItem(block, meta, 1F);
	}

	private static void renderTileEntity(int x, int y, int z, MultiBlockComponent comp, Block block, int meta) {
		if(!block.hasTileEntity(meta)) {
			return;
		}
		World w = mc.theWorld;
		TileEntity tile = block.createTileEntity(w, meta);
		if(!TileEntityRendererDispatcher.instance.hasSpecialRenderer(tile)) {
			return;
		}
		// memorizing real block
		Block trueBlock = w.getBlock(x, y, z);
		int trueMeta = w.getBlockMetadata(x, y, z);
		// memorizing real tile to buffer world
		TileEntity trueTile = getTileSilent(w, x, y, z);
		// actually render
		try {
			// setting block and tile to buffer world in order to render some tiles correctly
			setBlockSilent(w, x, y, z, block, meta);
			// setting tile data
			tile.xCoord = x;
			tile.yCoord = y;
			tile.zCoord = z;
			tile.setWorldObj(w);
			if(comp.tag != null) {
				tile.readFromNBT(comp.tag);
			}
			setTileSilent(w, x, y, z, tile);
			glPushMatrix();
			TileEntityRendererDispatcher.instance.getSpecialRenderer(tile).renderTileEntityAt(tile, x, y, z, 0);
			glDisable(GL_LIGHTING);
		}
		finally {
			// putting things back
			setBlockSilent(w, x, y, z, trueBlock, trueMeta);
			setTileSilent(w, x, y, z, trueTile);
			glPopMatrix();
			mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		}
	}

	private static void setBlockSilent(World world, int x, int y, int z, Block block, int meta) {
		Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
		ChunkPosition pos = new ChunkPosition(x & 15, y, z & 15);
		ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[y >> 4];
		if (extendedblockstorage == null) {
			if (block == Blocks.air) {
				return;
			}
			extendedblockstorage = chunk.getBlockStorageArray()[y >> 4] = new ExtendedBlockStorage(y >> 4 << 4, !world.provider.hasNoSky);
		}
		extendedblockstorage.func_150818_a(pos.chunkPosX, pos.chunkPosY & 15, pos.chunkPosZ, block);
		extendedblockstorage.setExtBlockMetadata(pos.chunkPosX, pos.chunkPosY & 15, pos.chunkPosZ, meta);
	}
	
	private static void setTileSilent(World world, int x, int y, int z, TileEntity tile) {
		Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
		ChunkPosition pos = new ChunkPosition(x & 15, y, z & 15);
		TileEntity trueTile = (TileEntity)chunk.chunkTileEntityMap.get(pos);
		if(trueTile != null) {
			chunk.chunkTileEntityMap.remove(pos);
			world.loadedTileEntityList.remove(trueTile);
		}
		if(tile != null) {
			chunk.chunkTileEntityMap.put(pos, tile);
			world.loadedTileEntityList.add(tile);
		}
	}

	private static TileEntity getTileSilent(World world, int x, int y, int z) {
		Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
		return (TileEntity)chunk.chunkTileEntityMap.get(new ChunkPosition(x & 15, y, z & 15));
	}

}
