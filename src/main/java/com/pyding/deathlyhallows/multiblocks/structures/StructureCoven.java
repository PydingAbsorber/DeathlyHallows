package com.pyding.deathlyhallows.multiblocks.structures;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.blocks.DHBlocks;
import net.minecraft.init.Blocks;

public class StructureCoven extends StructureBase {

	@Override
	protected void fillStructure() {
		add(DHBlocks.elderGlyph, 0, pos(0,0,0));
		add(Blocks.lit_pumpkin, 0, pos(-5,0,-6), pos(5,0,-6));
		add(Blocks.lit_pumpkin, 1, pos(6,0,-5), pos(6,0,5));
		add(Blocks.lit_pumpkin, 2, pos(-5,0,6), pos(5,0,6));
		add(Blocks.lit_pumpkin, 3, pos(-6,0,-5), pos(-6,0,5));
		add(Blocks.stained_hardened_clay, 7, pos(-7,-1,-5), pos(-7,-1,-4), pos(-7,-1,4), pos(-7,-1,5), pos(-6,-1,-6), pos(-6,-1,-5), pos(-6,-1,5), pos(-6,-1,6), pos(-5,-1,-7), pos(-5,-1,-6), pos(-5,-1,6), pos(-5,-1,7), pos(-4,-1,-7), pos(-4,-1,7), pos(4,-1,-7), pos(4,-1,7), pos(5,-1,-7), pos(5,-1,-6), pos(5,-1,6), pos(5,-1,7), pos(6,-1,-6), pos(6,-1,-5), pos(6,-1,5), pos(6,-1,6), pos(7,-1,-5), pos(7,-1,-4), pos(7,-1,4), pos(7,-1,5));
		add(Blocks.stained_hardened_clay, 15, pos(-7,-1,-3), pos(-7,-1,-2), pos(-7,-1,-1), pos(-7,-1,0), pos(-7,-1,1), pos(-7,-1,2), pos(-7,-1,3), pos(-6,-1,-4), pos(-6,-1,-3), pos(-6,-1,-2), pos(-6,-1,-1), pos(-6,-1,0), pos(-6,-1,1), pos(-6,-1,2), pos(-6,-1,3), pos(-6,-1,4), pos(-5,-1,-5), pos(-5,-1,-4), pos(-5,-1,-3), pos(-5,-1,-2), pos(-5,-1,-1), pos(-5,-1,0), pos(-5,-1,1), pos(-5,-1,2), pos(-5,-1,3), pos(-5,-1,4), pos(-5,-1,5), pos(-4,-1,-6), pos(-4,-1,-5), pos(-4,-1,-4), pos(-4,-1,-3), pos(-4,-1,-2), pos(-4,-1,-1), pos(-4,-1,0), pos(-4,-1,1), pos(-4,-1,2), pos(-4,-1,3), pos(-4,-1,4), pos(-4,-1,5), pos(-4,-1,6), pos(-3,-1,-7), pos(-3,-1,-6), pos(-3,-1,-5), pos(-3,-1,-4), pos(-3,-1,-3), pos(-3,-1,-2), pos(-3,-1,-1), pos(-3,-1,0), pos(-3,-1,1), pos(-3,-1,2), pos(-3,-1,3), pos(-3,-1,4), pos(-3,-1,5), pos(-3,-1,6), pos(-3,-1,7), pos(-2,-1,-7), pos(-2,-1,-6), pos(-2,-1,-5), pos(-2,-1,-4), pos(-2,-1,-3), pos(-2,-1,-2), pos(-2,-1,-1), pos(-2,-1,0), pos(-2,-1,1), pos(-2,-1,2), pos(-2,-1,3), pos(-2,-1,4), pos(-2,-1,5), pos(-2,-1,6), pos(-2,-1,7), pos(-1,-1,-7), pos(-1,-1,-6), pos(-1,-1,-5), pos(-1,-1,-4), pos(-1,-1,-3), pos(-1,-1,-2), pos(-1,-1,-1), pos(-1,-1,1), pos(-1,-1,2), pos(-1,-1,3), pos(-1,-1,4), pos(-1,-1,5), pos(-1,-1,6), pos(-1,-1,7), pos(0,-1,-7), pos(0,-1,-6), pos(0,-1,-5), pos(0,-1,-4), pos(0,-1,-3), pos(0,-1,-2), pos(0,-1,2), pos(0,-1,3), pos(0,-1,4), pos(0,-1,5), pos(0,-1,6), pos(0,-1,7), pos(1,-1,-7), pos(1,-1,-6), pos(1,-1,-5), pos(1,-1,-4), pos(1,-1,-3), pos(1,-1,-2), pos(1,-1,-1), pos(1,-1,1), pos(1,-1,2), pos(1,-1,3), pos(1,-1,4), pos(1,-1,5), pos(1,-1,6), pos(1,-1,7), pos(2,-1,-7), pos(2,-1,-6), pos(2,-1,-5), pos(2,-1,-4), pos(2,-1,-3), pos(2,-1,-2), pos(2,-1,-1), pos(2,-1,0), pos(2,-1,1), pos(2,-1,2), pos(2,-1,3), pos(2,-1,4), pos(2,-1,5), pos(2,-1,6), pos(2,-1,7), pos(3,-1,-7), pos(3,-1,-6), pos(3,-1,-5), pos(3,-1,-4), pos(3,-1,-3), pos(3,-1,-2), pos(3,-1,-1), pos(3,-1,0), pos(3,-1,1), pos(3,-1,2), pos(3,-1,3), pos(3,-1,4), pos(3,-1,5), pos(3,-1,6), pos(3,-1,7), pos(4,-1,-6), pos(4,-1,-5), pos(4,-1,-4), pos(4,-1,-3), pos(4,-1,-2), pos(4,-1,-1), pos(4,-1,0), pos(4,-1,1), pos(4,-1,2), pos(4,-1,3), pos(4,-1,4), pos(4,-1,5), pos(4,-1,6), pos(5,-1,-5), pos(5,-1,-4), pos(5,-1,-3), pos(5,-1,-2), pos(5,-1,-1), pos(5,-1,0), pos(5,-1,1), pos(5,-1,2), pos(5,-1,3), pos(5,-1,4), pos(5,-1,5), pos(6,-1,-4), pos(6,-1,-3), pos(6,-1,-2), pos(6,-1,-1), pos(6,-1,0), pos(6,-1,1), pos(6,-1,2), pos(6,-1,3), pos(6,-1,4), pos(7,-1,-3), pos(7,-1,-2), pos(7,-1,-1), pos(7,-1,0), pos(7,-1,1), pos(7,-1,2), pos(7,-1,3));
		add(Blocks.gold_block, 0, pos(-1,-1,0), pos(0,-1,-1), pos(0,-1,0), pos(0,-1,1), pos(1,-1,0));
		add(Witchery.Blocks.FETISH_WITCHS_LADDER, 2, pos(-7,0,-4), pos(4,0,-7), pos(7,0,-4), pos(7,0,4), pos(-7,1,-4), pos(4,1,-7), pos(7,1,-4), pos(7,1,4));
		add(Witchery.Blocks.FETISH_WITCHS_LADDER, 3, pos(-7,0,4), pos(-4,0,7), pos(-7,1,4), pos(-4,1,7));
		add(Witchery.Blocks.FETISH_WITCHS_LADDER, 4, pos(-4,0,-7), pos(-4,1,-7));
		add(Witchery.Blocks.FETISH_WITCHS_LADDER, 5, pos(4,0,7), pos(4,1,7));
		add(Witchery.Blocks.GLYPH_INFERNAL, pos(-7,0,-3), pos(-7,0,-2), pos(-7,0,-1), pos(-7,0,0), pos(-7,0,1), pos(-7,0,2), pos(-7,0,3), pos(-6,0,-4), pos(-6,0,-3), pos(-6,0,-1), pos(-6,0,0), pos(-6,0,1), pos(-6,0,2), pos(-6,0,3), pos(-6,0,4), pos(-5,0,-5), pos(-5,0,-4), pos(-5,0,-2), pos(-5,0,4), pos(-5,0,5), pos(-4,0,-6), pos(-4,0,-5), pos(-4,0,-2), pos(-4,0,-1), pos(-4,0,6), pos(-3,0,-7), pos(-3,0,-6), pos(-3,0,-2), pos(-3,0,-1), pos(-3,0,2), pos(-3,0,3), pos(-3,0,4), pos(-3,0,6), pos(-3,0,7), pos(-2,0,-7), pos(-2,0,-6), pos(-2,0,-2), pos(-2,0,0), pos(-2,0,1), pos(-2,0,3), pos(-2,0,6), pos(-2,0,7), pos(-1,0,-7), pos(-1,0,-6), pos(-1,0,-3), pos(-1,0,-2), pos(-1,0,-1), pos(-1,0,1), pos(-1,0,2), pos(-1,0,6), pos(-1,0,7), pos(0,0,-7), pos(0,0,-5), pos(0,0,-4), pos(0,0,-2), pos(0,0,2), pos(0,0,6), pos(0,0,7), pos(1,0,-7), pos(1,0,-6), pos(1,0,-3), pos(1,0,-2), pos(1,0,-1), pos(1,0,1), pos(1,0,2), pos(1,0,6), pos(1,0,7), pos(2,0,-7), pos(2,0,-6), pos(2,0,-2), pos(2,0,0), pos(2,0,1), pos(2,0,3), pos(2,0,6), pos(2,0,7), pos(3,0,-7), pos(3,0,-6), pos(3,0,-2), pos(3,0,-1), pos(3,0,2), pos(3,0,3), pos(3,0,4), pos(3,0,6), pos(3,0,7), pos(4,0,-6), pos(4,0,-5), pos(4,0,-2), pos(4,0,-1), pos(4,0,6), pos(5,0,-5), pos(5,0,-4), pos(5,0,-2), pos(5,0,4), pos(5,0,5), pos(6,0,-4), pos(6,0,-3), pos(6,0,-1), pos(6,0,0), pos(6,0,1), pos(6,0,2), pos(6,0,3), pos(6,0,4), pos(7,0,-3), pos(7,0,-2), pos(7,0,-1), pos(7,0,0), pos(7,0,1), pos(7,0,2), pos(7,0,3));
		add(Witchery.Blocks.CANDELABRA, 0, pos(-6,0,-2), pos(-4,0,5), pos(0,0,-6), pos(4,0,5), pos(6,0,-2));
		add(Witchery.Blocks.FETISH_TREANT_IDOL, 5, pos(-3,0,0));
		add(Witchery.Blocks.STATUE_GODDESS, 3, pos(0,0,-3));
		add(Witchery.Blocks.DECURSE_DIRECTED, 2, pos(0,0,3));
		add(Witchery.Blocks.DECURSE_TELEPORT, 4, pos(3,0,0));
		add(Witchery.Blocks.GLINT_WEED, 0, pos(-6,1,-5), pos(-6,1,5), pos(-5,1,-6), pos(-5,1,6), pos(5,1,-6), pos(5,1,6), pos(6,1,-5), pos(6,1,5));
	}
	
}
