package com.pyding.deathlyhallows.multiblocks.structures;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.blocks.DHBlocks;
import net.minecraft.init.Blocks;

public class StructureIceCastle extends StructureBase {

	@Override
	protected void fillStructure() {
		add(DHBlocks.elderGlyph, 0, pos(0, 0, 0));
		add(Witchery.Blocks.GLYPH_OTHERWHERE, pos(-5,0,-2), pos(-5,0,-1), pos(-5,0,0), pos(-5,0,1), pos(-5,0,2), pos(-2,0,-5), pos(-2,0,5), pos(-1,0,-5), pos(-1,0,5), pos(0,0,-5), pos(0,0,5), pos(1,0,-5), pos(1,0,5), pos(2,0,-5), pos(2,0,5), pos(5,0,-2), pos(5,0,-1), pos(5,0,0), pos(5,0,1), pos(5,0,2));
		add(Witchery.Blocks.GLYPH_INFERNAL, pos(-4,0,-3), pos(-4,0,3), pos(-3,0,-4), pos(-3,0,4), pos(3,0,-4), pos(3,0,4), pos(4,0,-3), pos(4,0,3));
		add(Witchery.Blocks.GLYPH_RITUAL, pos(-1,0,-1), pos(-1,0,0), pos(-1,0,1), pos(0,0,-1), pos(0,0,1), pos(1,0,-1), pos(1,0,0), pos(1,0,1));
		add(Blocks.packed_ice, 0, pos(-5,-1,-2), pos(-5,-1,-1), pos(-5,-1,0), pos(-5,-1,1), pos(-5,-1,2), pos(-4,-1,-3), pos(-4,-1,3), pos(-3,-1,-4), pos(-3,-1,4), pos(-2,-1,-5), pos(-2,-1,0), pos(-2,-1,5), pos(-1,-1,-5), pos(-1,-1,-1), pos(-1,-1,0), pos(-1,-1,1), pos(-1,-1,5), pos(0,-1,-5), pos(0,-1,-2), pos(0,-1,-1), pos(0,-1,0), pos(0,-1,1), pos(0,-1,5), pos(1,-1,-5), pos(1,-1,-1), pos(1,-1,0), pos(1,-1,1), pos(1,-1,5), pos(2,-1,-5), pos(2,-1,0), pos(2,-1,5), pos(3,-1,-4), pos(3,-1,4), pos(4,-1,-3), pos(4,-1,3), pos(5,-1,-2), pos(5,-1,-1), pos(5,-1,0), pos(5,-1,1), pos(5,-1,2));
		add(Witchery.Blocks.STOCKADE_ICE, 0, pos(-3,0,-3), pos(-3,0,3), pos(-2,0,-4), pos(-2,0,4), pos(2,0,-4), pos(2,0,4), pos(3,0,-3), pos(3,0,3));
		add(Witchery.Blocks.PERPETUAL_ICE, 0, pos(-4,-1,-2), pos(-4,-1,-1), pos(-4,-1,0), pos(-4,-1,1), pos(-4,-1,2), pos(-3,-1,-3), pos(-3,-1,-2), pos(-3,-1,-1), pos(-3,-1,0), pos(-3,-1,1), pos(-3,-1,2), pos(-3,-1,3), pos(-2,-1,-4), pos(-2,-1,-3), pos(-2,-1,-2), pos(-2,-1,-1), pos(-2,-1,1), pos(-2,-1,2), pos(-2,-1,3), pos(-2,-1,4), pos(-1,-1,-4), pos(-1,-1,-3), pos(-1,-1,-2), pos(-1,-1,2), pos(-1,-1,3), pos(-1,-1,4), pos(0,-1,-4), pos(0,-1,-3), pos(0,-1,2), pos(0,-1,3), pos(0,-1,4), pos(1,-1,-4), pos(1,-1,-3), pos(1,-1,-2), pos(1,-1,2), pos(1,-1,3), pos(1,-1,4), pos(2,-1,-4), pos(2,-1,-3), pos(2,-1,-2), pos(2,-1,-1), pos(2,-1,1), pos(2,-1,2), pos(2,-1,3), pos(2,-1,4), pos(3,-1,-3), pos(3,-1,-2), pos(3,-1,-1), pos(3,-1,0), pos(3,-1,1), pos(3,-1,2), pos(3,-1,3), pos(4,-1,-2), pos(4,-1,-1), pos(4,-1,0), pos(4,-1,1), pos(4,-1,2), pos(-3,0,-2), pos(-3,0,2), pos(-2,0,-3), pos(-2,0,-1), pos(-2,0,1), pos(-2,0,3), pos(-1,0,-2), pos(-1,0,2), pos(0,0,2), pos(1,0,-2), pos(1,0,2), pos(2,0,-3), pos(2,0,-1), pos(2,0,1), pos(2,0,3), pos(3,0,-2), pos(3,0,2), pos(-3,1,-2), pos(-3,1,2), pos(-2,1,-3), pos(-2,1,-1), pos(-2,1,1), pos(-2,1,3), pos(-1,1,-2), pos(-1,1,2), pos(0,1,2), pos(1,1,-2), pos(1,1,2), pos(2,1,-3), pos(2,1,-1), pos(2,1,1), pos(2,1,3), pos(3,1,-2), pos(3,1,2), pos(-3,2,-2), pos(-3,2,2), pos(-2,2,-3), pos(-2,2,-1), pos(-2,2,0), pos(-2,2,1), pos(-2,2,3), pos(-1,2,-2), pos(-1,2,2), pos(0,2,-2), pos(0,2,2), pos(1,2,-2), pos(1,2,2), pos(2,2,-3), pos(2,2,-1), pos(2,2,0), pos(2,2,1), pos(2,2,3), pos(3,2,-2), pos(3,2,2), pos(-3,3,-2), pos(-3,3,2), pos(-2,3,-3), pos(-2,3,-1), pos(-2,3,1), pos(-2,3,3), pos(-1,3,-2), pos(-1,3,2), pos(1,3,-2), pos(1,3,2), pos(2,3,-3), pos(2,3,-1), pos(2,3,1), pos(2,3,3), pos(3,3,-2), pos(3,3,2));
		add(Witchery.Blocks.PERPETUAL_ICE_SLAB_SINGLE, 0, pos(0,2,-3));
		add(Witchery.Blocks.PERPETUAL_ICE_FENCE, 0, pos(-4,0,-2), pos(-4,0,-1), pos(-4,0,0), pos(-4,0,1), pos(-4,0,2), pos(4,0,-2), pos(4,0,-1), pos(4,0,0), pos(4,0,1), pos(4,0,2));
		add(Witchery.Blocks.PERPETUAL_ICE_DOOR, 0, pos(-2,0,0));
		add(Witchery.Blocks.PERPETUAL_ICE_DOOR, 2, pos(2,0,0));
		add(Witchery.Blocks.PERPETUAL_ICE_DOOR, 8, pos(-2,1,0), pos(2,1,0));
		add(Witchery.Blocks.PERPETUAL_ICE_STAIRS, 5, pos(-1,1,-3));
		add(Witchery.Blocks.PERPETUAL_ICE_STAIRS, 4, pos(1,1,-3));
		add(Witchery.Blocks.PERPETUAL_ICE_STAIRS, 2, pos(-1,2,-3), pos(1,2,-3), pos(-2,4,-3), pos(-2,4,1), pos(2,4,-3), pos(2,4,1));
		add(Witchery.Blocks.PERPETUAL_ICE_STAIRS, 0, pos(-2,3,0), pos(-3,4,-2), pos(-3,4,2), pos(1,4,-2), pos(1,4,2));
		add(Witchery.Blocks.PERPETUAL_ICE_STAIRS, 3, pos(0,3,2), pos(-2,4,-1), pos(-2,4,3), pos(2,4,-1), pos(2,4,3));
		add(Witchery.Blocks.PERPETUAL_ICE_STAIRS, 1, pos(2,3,0), pos(-1,4,-2), pos(-1,4,2), pos(3,4,-2), pos(3,4,2));
	}
	
}
