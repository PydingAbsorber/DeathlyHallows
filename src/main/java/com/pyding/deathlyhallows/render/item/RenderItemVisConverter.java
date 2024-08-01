package com.pyding.deathlyhallows.render.item;

import com.pyding.deathlyhallows.blocks.tiles.TileEntityVisConverter;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class RenderItemVisConverter implements IItemRenderer {
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch(type) {
			case EQUIPPED_FIRST_PERSON:
			case EQUIPPED:
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return type == ItemRenderType.ENTITY && helper == ItemRendererHelper.ENTITY_ROTATION;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		TileEntityVisConverter dummy = new TileEntityVisConverter(); // this is NOT a hack; all normal people renders using that way;
		dummy.clientTicks = data[1] instanceof EntityClientPlayerMP ? ((EntityClientPlayerMP) data[1]).ticksExisted : 0;
		TileEntityRendererDispatcher.instance.renderTileEntityAt(dummy, 0D,0D,0D, 0F);
	}
	
}
