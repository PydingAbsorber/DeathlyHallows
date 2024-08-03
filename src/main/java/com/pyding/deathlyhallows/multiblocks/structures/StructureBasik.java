package com.pyding.deathlyhallows.multiblocks.structures;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.blocks.DHBlocks;
import thaumcraft.common.config.ConfigBlocks;

public class StructureBasik extends StructureBase {

	@Override
	protected void fillStructure() {
		add(DHBlocks.elderRitualRune, 0, pos(0, 0, 0));
		add(Witchery.Blocks.GLYPH_INFERNAL, pos(-2, 0, -1), pos(-2, 0, 1), pos(-1, 0, -2), pos(-1, 0, 2), pos(1, 0, -2), pos(1, 0, 2), pos(2, 0, -1), pos(2, 0, 1));
		add(Witchery.Blocks.GLYPH_OTHERWHERE, pos(-2, 0, 0), pos(0, 0, -2), pos(0, 0, 2), pos(2, 0, 0));
		add(ConfigBlocks.blockCosmeticSolid, 4, pos(-2, -2, -2), pos(-2, -2, 0), pos(-2, -2, 2), pos(0, -2, -2), pos(0, -2, 2), pos(2, -2, -2), pos(2, -2, 0), pos(2, -2, 2), pos(-2, -1, -1), pos(-2, -1, 1), pos(-1, -1, -2), pos(-1, -1, 2), pos(1, -1, -2), pos(1, -1, 2), pos(2, -1, -1), pos(2, -1, 1));
		add(ConfigBlocks.blockCosmeticSolid, 11, pos(-2, -2, -1), pos(-2, -2, 1), pos(-1, -2, -2), pos(-1, -2, 2), pos(1, -2, -2), pos(1, -2, 2), pos(2, -2, -1), pos(2, -2, 1), pos(-2, -1, 0), pos(0, -1, -2), pos(0, -1, 2), pos(2, -1, 0));
		add(ConfigBlocks.blockTaint, 2, pos(-1, -2, -1), pos(-1, -2, 1), pos(0, -2, 0), pos(1, -2, -1), pos(1, -2, 1));
		add(ConfigBlocks.blockCosmeticOpaque, 0, pos(-1, -2, 0), pos(0, -2, -1), pos(0, -2, 1), pos(1, -2, 0));
		add(ConfigBlocks.blockCosmeticSolid, 7, pos(-2, -1, -2), pos(-2, -1, 2), pos(2, -1, -2), pos(2, -1, 2));
		add(ConfigBlocks.blockFluidDeath, 3, pos(-1, -1, -1), pos(-1, -1, 0), pos(-1, -1, 1), pos(0, -1, -1), pos(0, -1, 1), pos(1, -1, -1), pos(1, -1, 1));
		add(ConfigBlocks.blockStoneDevice, 12, pos(0, -1, 0));
		add(ConfigBlocks.blockFluidDeath, 2, pos(1, -1, 0));
		add(Witchery.Blocks.BLOOD_ROSE, 1, pos(-2, 0, -2), pos(-2, 0, 2), pos(2, 0, -2), pos(2, 0, 2));
	}
	
}
