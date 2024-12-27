package com.pyding.deathlyhallows.multiblocks.structures;

import com.pyding.deathlyhallows.blocks.DHBlocks;
import net.minecraft.init.Blocks;

public class StructureMending extends StructureBase {

	@Override
	protected void fillStructure() {
		add(DHBlocks.elderGlyph, 0, pos(0, 0, 0));
		add(Blocks.anvil, 0, StructureBase.SIMPLE_ROTATOR, pos(0, -1, 0));
		add(Blocks.emerald_ore, pos(0, -2, 0));
		add(Blocks.lava, pos(-1, -2, -1), pos(0, -2, -1), pos(1, -2, -1), pos(-1, -2, 0), pos(1, -2, 0), pos(-1, -2, 1), pos(0, -2, 1), pos(1, -2, 1));
		add(Blocks.stonebrick, 0, pos(-2, -2, -2), pos(-2, -2, -1), pos(-1, -2, -2), pos(2, -2, -2), pos(1, -2, -2), pos(2, -2, -1), pos(-2, -2, 2), pos(-1, -2, 2), pos(-2, -2, 1), pos(2, -2, 2), pos(1, -2, 2), pos(2, -2, 1));
		add(Blocks.stonebrick, 2, pos(2, -2, 0), pos(-2, -2, 0), pos(0, -2, -2), pos(0, -2, 2));
		for(int y = -1; y < 1; ++y) {
			add(Blocks.iron_bars, pos(2, y, 2), pos(-2, y, 2), pos(2, y, -2), pos(-2, y, -2));
		}
		add(Blocks.netherrack, pos(2, 1, 2), pos(-2, 1, 2), pos(2, 1, -2), pos(-2, 1, -2));
		add(Blocks.iron_bars, pos(1, 1, 2), pos(2, 1, 1), pos(-1, 1, 2), pos(-2, 1, 1), pos(1, 1, -2), pos(2, 1, -1), pos(-1, 1, -2), pos(-2, 1, -1));
		add(Blocks.fire, pos(2, 2, 2), pos(-2, 2, 2), pos(2, 2, -2), pos(-2, 2, -2));
	}

}
