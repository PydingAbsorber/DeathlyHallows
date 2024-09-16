package com.pyding.deathlyhallows.multiblocks.structures;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.blocks.DHBlocks;
import net.minecraft.init.Blocks;

public class StructureSonata extends StructureBase {

	@Override
	protected void fillStructure() {
		add(DHBlocks.elderGlyph, 0, pos(0, 0, 0));
		add(Witchery.Blocks.GLYPH_RITUAL, pos(-4,0,4), pos(-1,0,-4), pos(0,0,-4), pos(1,0,-4), pos(4,0,4));
		add(Witchery.Blocks.GLYPH_OTHERWHERE, pos(-3,0,0), pos(-3,0,2), pos(-2,0,-2), pos(-1,0,0), pos(-1,0,2), pos(0,0,-2), pos(1,0,0), pos(1,0,2), pos(2,0,-2), pos(3,0,0), pos(3,0,2));
		add(Blocks.planks, 5, pos(-4,-1,4), pos(-3,-1,0), pos(-3,-1,2), pos(-2,-1,-2), pos(-1,-1,-4), pos(-1,-1,0), pos(-1,-1,2), pos(0,-1,-4), pos(0,-1,-2), pos(1,-1,-4), pos(1,-1,0), pos(1,-1,2), pos(2,-1,-2), pos(3,-1,0), pos(3,-1,2), pos(4,-1,4));
		add(Blocks.daylight_detector, pos(4,0,3));
		add(Witchery.Blocks.WOLFHEAD, pos(-4,0,0), pos(4,0,0));
		add(Blocks.skull, pos(-2,0,3), pos(0,0,-5), pos(2,0,3));
		add(Witchery.Blocks.PLANKS, 2, pos(-5,-1,2), pos(-5,-1,3), pos(-5,-1,4), pos(-4,-1,-3), pos(-4,-1,-2), pos(-4,-1,-1), pos(-4,-1,0), pos(-4,-1,1), pos(-4,-1,2), pos(-4,-1,3), pos(-4,-1,5), pos(-3,-1,-4), pos(-3,-1,-3), pos(-3,-1,-2), pos(-3,-1,-1), pos(-3,-1,1), pos(-3,-1,3), pos(-3,-1,4), pos(-3,-1,5), pos(-2,-1,-4), pos(-2,-1,-3), pos(-2,-1,-1), pos(-2,-1,0), pos(-2,-1,1), pos(-2,-1,2), pos(-2,-1,3), pos(-2,-1,4), pos(-1,-1,-5), pos(-1,-1,-3), pos(-1,-1,-2), pos(-1,-1,-1), pos(-1,-1,1), pos(-1,-1,3), pos(-1,-1,4), pos(0,-1,-5), pos(0,-1,-3), pos(0,-1,-1), pos(0,-1,0), pos(0,-1,1), pos(0,-1,2), pos(0,-1,3), pos(1,-1,-5), pos(1,-1,-3), pos(1,-1,-2), pos(1,-1,-1), pos(1,-1,1), pos(1,-1,3), pos(1,-1,4), pos(2,-1,-4), pos(2,-1,-3), pos(2,-1,-1), pos(2,-1,0), pos(2,-1,1), pos(2,-1,2), pos(2,-1,3), pos(2,-1,4), pos(3,-1,-4), pos(3,-1,-3), pos(3,-1,-2), pos(3,-1,-1), pos(3,-1,1), pos(3,-1,3), pos(3,-1,4), pos(3,-1,5), pos(4,-1,-3), pos(4,-1,-2), pos(4,-1,-1), pos(4,-1,0), pos(4,-1,1), pos(4,-1,2), pos(4,-1,3), pos(4,-1,5), pos(5,-1,2), pos(5,-1,3), pos(5,-1,4));
		add(Witchery.Blocks.CRYSTAL_BALL, 0, pos(-4,0,3));
		add(Witchery.Blocks.CHALICE, 1, pos(-4,0,5));
		add(Witchery.Blocks.CANDELABRA, 0, pos(4,0,5));
	}
	
}
