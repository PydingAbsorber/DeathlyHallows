package com.pyding.deathlyhallows.multiblocks.structures;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.blocks.DHBlocks;

public class StructureHunt extends StructureBase {

	@Override
	protected void fillStructure() {
		add(DHBlocks.elderGlyph, 0, pos(0, 0, 0));
		for(int x = -1; x < 2; x += 2) {
			for(int z = -1; z < 2; z += 2) {
				for(int y = 0; y < 4; ++y) {
					add(Witchery.Blocks.LOG, 1, pos(x, y, z));
				}
				add(Witchery.Blocks.GLYPH_INFERNAL, pos(x, 4, z));
				add(Witchery.Blocks.GLYPH_INFERNAL, pos(2 * x, 0, z));
				add(Witchery.Blocks.GLYPH_INFERNAL, pos(3 * x, 0, z));
				add(Witchery.Blocks.GLYPH_INFERNAL, pos(x, 0, 2 * z));
				add(Witchery.Blocks.GLYPH_INFERNAL, pos(x, 0, 3 * z));
			}
		}
		add(Witchery.Blocks.WICKER_BUNDLE, 1, pos(1, 3, 0), pos(-1, 3, 0), pos(0, 3, 1), pos(0, 3, -1));
		add(Witchery.Blocks.GLYPH_INFERNAL, pos(1, 4, 0), pos(-1, 4, 0), pos(0, 4, 1), pos(0, 4, -1));
	}

}
