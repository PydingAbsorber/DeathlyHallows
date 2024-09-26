package com.pyding.deathlyhallows.multiblocks.structures;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.blocks.DHBlocks;
import net.minecraft.init.Blocks;

public class StructurePurify extends StructureBase {

	@Override
	protected void fillStructure() {
		add(DHBlocks.elderGlyph, 0, pos(0, 0, 0));
		add(Blocks.sponge, 0, pos(-2, -2, -2), pos(-2, -2, -1), pos(-2, -2, 0), pos(-2, -2, 1), pos(-2, -2, 2), pos(-1, -2, -2), pos(-1, -2, -1), pos(-1, -2, 0), pos(-1, -2, 1), pos(-1, -2, 2), pos(0, -2, -2), pos(0, -2, -1), pos(0, -2, 0), pos(0, -2, 1), pos(0, -2, 2), pos(1, -2, -2), pos(1, -2, -1), pos(1, -2, 0), pos(1, -2, 1), pos(1, -2, 2), pos(2, -2, -2), pos(2, -2, -1), pos(2, -2, 0), pos(2, -2, 1), pos(2, -2, 2));
		add(Blocks.snow, 0, pos(-3, -1, -2), pos(-3, -1, -1), pos(-3, -1, 0), pos(-3, -1, 1), pos(-3, -1, 2), pos(-2, -1, -3), pos(-2, -1, -2), pos(-2, -1, -1), pos(-2, -1, 0), pos(-2, -1, 1), pos(-2, -1, 2), pos(-2, -1, 3), pos(-1, -1, -3), pos(-1, -1, -2), pos(-1, -1, -1), pos(-1, -1, 0), pos(-1, -1, 1), pos(-1, -1, 2), pos(-1, -1, 3), pos(0, -1, -3), pos(0, -1, -2), pos(0, -1, -1), pos(0, -1, 0), pos(0, -1, 1), pos(0, -1, 2), pos(0, -1, 3), pos(1, -1, -3), pos(1, -1, -2), pos(1, -1, -1), pos(1, -1, 0), pos(1, -1, 1), pos(1, -1, 2), pos(1, -1, 3), pos(2, -1, -3), pos(2, -1, -2), pos(2, -1, -1), pos(2, -1, 0), pos(2, -1, 1), pos(2, -1, 2), pos(2, -1, 3), pos(3, -1, -2), pos(3, -1, -1), pos(3, -1, 0), pos(3, -1, 1), pos(3, -1, 2));
		add(Blocks.lapis_block, 0, pos(-3, 0, -1), pos(-3, 0, 1), pos(-1, 0, -3), pos(-1, 0, 3), pos(1, 0, -3), pos(1, 0, 3), pos(3, 0, -1), pos(3, 0, 1), pos(-3, 1, -1), pos(-3, 1, 1), pos(-1, 1, -3), pos(-1, 1, 3), pos(1, 1, -3), pos(1, 1, 3), pos(3, 1, -1), pos(3, 1, 1), pos(-3, 2, -1), pos(-3, 2, 1), pos(-1, 2, -3), pos(-1, 2, 3), pos(1, 2, -3), pos(1, 2, 3), pos(3, 2, -1), pos(3, 2, 1), pos(-2, 3, -1), pos(-2, 3, 1), pos(-1, 3, -2), pos(-1, 3, 2), pos(1, 3, -2), pos(1, 3, 2), pos(2, 3, -1), pos(2, 3, 1));
		add(Blocks.carpet, 3, pos(-1, 0, -1), pos(-1, 0, 0), pos(-1, 0, 1), pos(0, 0, -1), pos(0, 0, 1), pos(1, 0, -1), pos(1, 0, 0), pos(1, 0, 1));
		add(Witchery.Blocks.SHADED_GLASS, 11, pos(-3, 2, 0), pos(-2, 2, -2), pos(-2, 2, 2), pos(0, 2, -3), pos(0, 2, 3), pos(2, 2, -2), pos(2, 2, 2), pos(3, 2, 0), pos(-2, 3, 0), pos(0, 3, -2), pos(0, 3, 2), pos(2, 3, 0));
		add(Witchery.Blocks.SNOW_STAIRS, 4, SIMPLE_ROTATOR, pos(-3, -2, -2), pos(-3, -2, -1), pos(-3, -2, 0), pos(-3, -2, 1), pos(-3, -2, 2));
		add(Witchery.Blocks.SNOW_STAIRS, 6, SIMPLE_ROTATOR, pos(-2, -2, -3), pos(-1, -2, -3), pos(0, -2, -3), pos(1, -2, -3), pos(2, -2, -3));
		add(Witchery.Blocks.SNOW_STAIRS, 7, SIMPLE_ROTATOR, pos(-2, -2, 3), pos(-1, -2, 3), pos(0, -2, 3), pos(1, -2, 3), pos(2, -2, 3));
		add(Witchery.Blocks.SNOW_STAIRS, 5, SIMPLE_ROTATOR, pos(3, -2, -2), pos(3, -2, -1), pos(3, -2, 0), pos(3, -2, 1), pos(3, -2, 2));
		add(Witchery.Blocks.FETISH_WITCHS_LADDER, pos(0, 0, -3), pos(0, 1, -3), pos(-3, 1, 0), pos(0, 1, 3), pos(3, 1, 0));
		add(Witchery.Blocks.FETISH_SCARECROW, 4, HELL_ROTATOR, pos(3, 0, 0));
		add(Witchery.Blocks.FETISH_TREANT_IDOL, 5, HELL_ROTATOR, pos(-3, 0, 0));
		add(Witchery.Blocks.STATUE_OF_WORSHIP, 2, HELL_ROTATOR, pos(0, 0, 3));
	}

}
