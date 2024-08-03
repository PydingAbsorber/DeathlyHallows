package com.pyding.deathlyhallows.rituals;

import com.pyding.deathlyhallows.multiblocks.MultiBlock;
import com.pyding.deathlyhallows.utils.IMultiBlockHandler;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;

public class BlockList implements IMultiBlockHandler {
	private final MultiBlock mb;

	public BlockList() {
		mb = new MultiBlock();
	}
	
	public BlockList(int[] center, String[][][] blocks) {
		this();
		for(int y = 0; y < blocks.length; y++) {
			for(int x = 0; x < blocks[y].length; x++) {
				for(int z = 0; z < blocks[y][x].length; z++) {
					String stack = blocks[y][x][z];
					if(stack == null) {
						continue;
					}
					String[] spl = stack.split(" ");
					Block block = null;
					int meta = 0;
					NBTTagCompound nbt = null;
					if(spl.length > 0 && spl[0].length() > 0) {
						block = GameData.blockRegistry.get(spl[0]);
					}
					if(spl.length > 1) {
						try {
							meta = Integer.parseInt(spl[1]);
						}
						catch(Exception ignored) {

						}
					}
					if(spl.length > 2) {
						try {
							nbt = (NBTTagCompound)JsonToNBT.func_150315_a(spl[2].replace("\\\"", "\""));
						}
						catch(Exception ignored) {

						}
					}
					if(block != null) {
						mb.addComponent(x - center[0], y - center[1], z - center[2], block, meta, nbt);
					}
				}
			}
		}
	}

	@Override
	public MultiBlock getMultiBlock() {
		return mb;
	}
	
}
