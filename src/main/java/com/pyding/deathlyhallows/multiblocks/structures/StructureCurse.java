package com.pyding.deathlyhallows.multiblocks.structures;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.blocks.DHBlocks;
import net.minecraft.init.Blocks;

public class StructureCurse extends StructureBase {

	@Override
	protected void fillStructure() {
		add(DHBlocks.elderGlyph, 0, pos(0, 0, 0));
		add(Blocks.quartz_block, 0, pos(-4,-4,-4), pos(-4,-4,-2), pos(-4,-4,-1), pos(-4,-4,1), pos(-4,-4,2), pos(-4,-4,4), pos(-3,-4,-3), pos(-3,-4,-1), pos(-3,-4,0), pos(-3,-4,1), pos(-3,-4,2), pos(-3,-4,3), pos(-2,-4,-4), pos(-2,-4,-3), pos(-2,-4,-2), pos(-2,-4,-1), pos(-2,-4,0), pos(-2,-4,1), pos(-2,-4,2), pos(-2,-4,3), pos(-2,-4,4), pos(-1,-4,-4), pos(-1,-4,-2), pos(-1,-4,-1), pos(-1,-4,0), pos(-1,-4,1), pos(-1,-4,2), pos(-1,-4,3), pos(-1,-4,4), pos(0,-4,-4), pos(0,-4,-3), pos(0,-4,-2), pos(0,-4,-1), pos(0,-4,0), pos(0,-4,1), pos(0,-4,2), pos(0,-4,3), pos(1,-4,-4), pos(1,-4,-2), pos(1,-4,-1), pos(1,-4,0), pos(1,-4,1), pos(1,-4,2), pos(1,-4,3), pos(2,-4,-3), pos(2,-4,-2), pos(2,-4,-1), pos(2,-4,0), pos(2,-4,1), pos(2,-4,2), pos(2,-4,4), pos(3,-4,-3), pos(3,-4,-2), pos(3,-4,-1), pos(3,-4,0), pos(3,-4,1), pos(3,-4,3), pos(4,-4,-4), pos(4,-4,-1), pos(4,-4,0), pos(4,-4,2), pos(4,-4,4), pos(-2,-3,-2), pos(-2,-3,-1), pos(-2,-3,1), pos(-2,-3,2), pos(-1,-3,-1), pos(-1,-3,0), pos(-1,-3,1), pos(-1,-3,2), pos(0,-3,-2), pos(0,-3,-1), pos(0,-3,0), pos(0,-3,1), pos(1,-3,-1), pos(1,-3,0), pos(1,-3,1), pos(1,-3,2), pos(2,-3,-2), pos(2,-3,-1), pos(2,-3,1), pos(2,-3,2), pos(-1,-2,-1), pos(-1,-2,1), pos(0,-2,0), pos(0,-2,1), pos(1,-2,-1), pos(1,-2,0));
		add(Blocks.quartz_block, 1, pos(-4,-4,0), pos(-4,-4,5), pos(-3,-4,-2), pos(-1,-4,-3), pos(0,-4,4), pos(1,-4,-3), pos(1,-4,4), pos(2,-4,-4), pos(2,-4,3), pos(3,-4,2), pos(4,-4,-2), pos(4,-4,1), pos(4,-4,5), pos(5,-4,4), pos(-4,-3,-5), pos(-4,-3,3), pos(-3,-3,4), pos(-2,-3,0), pos(-1,-3,-2), pos(0,-3,2), pos(1,-3,-2), pos(2,-3,0), pos(3,-3,-4), pos(4,-3,5), pos(5,-3,-4), pos(-5,-2,-4), pos(-5,-2,4), pos(-1,-2,0), pos(0,-2,-1), pos(1,-2,1), pos(4,-2,-5), pos(4,-2,3), pos(-4,-1,3), pos(-4,-1,5), pos(0,-1,0), pos(3,-1,4), pos(4,-1,-3));
		add(Blocks.quartz_block, 2, pos(-5,-4,-4), pos(-5,-4,4), pos(-4,-4,-5), pos(-4,-4,-3), pos(-4,-4,3), pos(-3,-4,-4), pos(-3,-4,4), pos(3,-4,-4), pos(3,-4,4), pos(4,-4,-5), pos(4,-4,-3), pos(4,-4,3), pos(5,-4,-4), pos(-5,-3,-4), pos(-5,-3,4), pos(-4,-3,-3), pos(-4,-3,5), pos(3,-3,4), pos(4,-3,-5), pos(4,-3,-3), pos(4,-3,3), pos(5,-3,4), pos(-4,-2,-5), pos(-4,-2,3), pos(-4,-2,5), pos(-3,-2,4), pos(3,-2,-4), pos(3,-2,4), pos(4,-2,-3), pos(4,-2,5), pos(5,-2,-4), pos(-5,-1,4), pos(-3,-1,4), pos(3,-1,-4), pos(4,-1,-5), pos(4,-1,3), pos(5,-1,-4));
		add(Blocks.skull, pos(-3,-3,-1), pos(-3,-3,0), pos(-3,-3,1), pos(-3,-3,2), pos(-3,-3,3), pos(-1,-3,-3), pos(0,-3,-3), pos(0,-3,3), pos(1,-3,-3), pos(2,-3,-3), pos(3,-3,-3), pos(3,-3,1), pos(3,-3,2), pos(3,-3,3), pos(-2,-2,-2), pos(-2,-2,-1), pos(-2,-2,0), pos(-2,-2,2), pos(-1,-2,-2), pos(-1,-2,2), pos(0,-2,2), pos(1,-2,2), pos(2,-2,0), pos(2,-2,1), pos(-1,-1,-1), pos(-1,-1,0), pos(-1,-1,1), pos(0,-1,-1), pos(1,-1,0), pos(1,-1,1));
		add(Witchery.Blocks.WOLFHEAD, -1, pos(-3,-3,-3), pos(-2,-3,-3), pos(-2,-3,3), pos(-1,-3,3), pos(2,-3,3), pos(3,-3,-1), pos(3,-3,0), pos(0,-2,-2), pos(1,-2,-2), pos(2,-2,-1), pos(2,-2,2), pos(1,-1,-1));
		add(Witchery.Blocks.ALLURING_SKULL, 0, pos(-3,-3,-2), pos(1,-3,3), pos(3,-3,-2), pos(-2,-2,1), pos(2,-2,-2), pos(0,-1,1));
	}

}
