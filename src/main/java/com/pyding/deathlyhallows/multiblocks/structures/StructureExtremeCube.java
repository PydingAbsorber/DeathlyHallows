package com.pyding.deathlyhallows.multiblocks.structures;

import com.pyding.deathlyhallows.blocks.DHBlocks;
import net.minecraft.init.Blocks;

public class StructureExtremeCube extends StructureBase {

	@Override
	protected void fillStructure() {
		add(DHBlocks.elderGlyph, 0, pos(0, 0, 0));
		for(int x = -7; x <= 7; ++x) {
			for(int y = -7; y <= 7; ++y) {
				for(int z = -7; z <= 7; ++z) {
					if(Math.abs(x) != 7 || Math.abs(y) != 7 || Math.abs(z) != 7) {
						continue;
					}
					add(Blocks.glass, 0, x, y, z);
				}
			}
		}
	}

}
