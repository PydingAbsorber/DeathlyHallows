package com.pyding.deathlyhallows.blocks;

import com.pyding.deathlyhallows.blocks.tiles.VisConverterTile;
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
			elderRitualBlock,
			visConverter;

	private DHBlocks() {

	}

	public static void init() {
		elderRitualBlock = new ElderRitualBlock().setBlockName("elder_ritual_block")
												 .setBlockTextureName("dh:ritual");
		registerTile(ElderRitualBlock.TileEntityCircle.class, "elder_ritual_block_tile");
	}

	private static void registerTile(Class<? extends TileEntity> tile, String name) {
		GameRegistry.registerTileEntity(tile, name);
	}

	@SideOnly(Side.CLIENT)
	public static void initClient() {
		renderTile(VisConverterTile.class, new RenderTileVisConverter());
	}

	@SideOnly(Side.CLIENT)
	private static void renderTile(Class<? extends TileEntity> clazz, TileEntitySpecialRenderer render) {
		ClientRegistry.bindTileEntitySpecialRenderer(clazz, render);
	}

}
