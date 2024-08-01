package com.pyding.deathlyhallows.blocks;

import com.pyding.deathlyhallows.blocks.tiles.TileEntityVisConverter;
import com.pyding.deathlyhallows.render.block.RenderTileVisConverter;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public final class DHBlocks {

	public static Block
			elderRitualRune,
			visConverter;

	private DHBlocks() {

	}

	public static void init() {
		register(elderRitualRune = new BlockElderRitual());
		registerTile(BlockElderRitual.TileEntityCircle.class, "elderRitualTile");
	}
	
	public static void register(Block block) {
		GameRegistry.registerBlock(block, block.getUnlocalizedName().replaceFirst("tile.", ""));
	}

	public static void registerTile(Class<? extends TileEntity> tile, String name) {
		GameRegistry.registerTileEntity(tile, name);
	}

	@SideOnly(Side.CLIENT)
	public static void initClient() {
		renderTile(TileEntityVisConverter.class, new RenderTileVisConverter());
	}

	@SideOnly(Side.CLIENT)
	public static void renderTile(Class<? extends TileEntity> clazz, TileEntitySpecialRenderer render) {
		ClientRegistry.bindTileEntitySpecialRenderer(clazz, render);
	}

}
