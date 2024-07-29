package com.pyding.deathlyhallows.multiblocks;

import net.minecraft.init.Blocks;

public class DHMultiBlocks {
	public static MultiBlockSet temp;

	static {
		MultiBlock mb = new MultiBlock();
		for(int i = -7; i < 8; i++) {
			for(int j = -7; j < 8; j++) {
				mb.addComponent(i, 0, j, Blocks.obsidian, 0);
			}
		}
		temp = mb.makeSet();
	}
}
