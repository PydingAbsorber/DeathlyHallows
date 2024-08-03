package com.pyding.deathlyhallows.rituals;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.multiblocks.MultiBlock;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.ArrayList;

public class Circle extends BlockList {
	// TODO remake on Struct
	private static final int[][] PATTERN =
			{
					{0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
					{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
					{0, 0, 1, 0, 0, 2, 2, 2, 2, 2, 0, 0, 1, 0, 0},
					{0, 1, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 1, 0},
					{1, 0, 0, 2, 0, 0, 3, 3, 3, 0, 0, 2, 0, 0, 1},
					{1, 0, 2, 0, 0, 3, 0, 0, 0, 3, 0, 0, 2, 0, 1},
					{1, 0, 2, 0, 3, 0, 0, 0, 0, 0, 3, 0, 2, 0, 1},
					{1, 0, 2, 0, 3, 0, 0, 0, 0, 0, 3, 0, 2, 0, 1},
					{1, 0, 2, 0, 3, 0, 0, 0, 0, 0, 3, 0, 2, 0, 1},
					{1, 0, 2, 0, 0, 3, 0, 0, 0, 3, 0, 0, 2, 0, 1},
					{1, 0, 0, 2, 0, 0, 3, 3, 3, 0, 0, 2, 0, 0, 1},
					{0, 1, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 1, 0},
					{0, 0, 1, 0, 0, 2, 2, 2, 2, 2, 0, 0, 1, 0, 0},
					{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
					{0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
			};

	int numRitualGlyphs;
	int numOtherwhereGlyphs;
	int numInfernalGlyphs;
	final int requiredGlyphs;

	public Circle(final int requiredGlyphs) {
		this.requiredGlyphs = requiredGlyphs;
	}

	public Circle(final int numRitualGlyphs, final int numOtherwhereGlyphs, final int numInfernalGlyphs) {
		requiredGlyphs = numRitualGlyphs + numOtherwhereGlyphs + numInfernalGlyphs;
		this.numRitualGlyphs = numRitualGlyphs;
		this.numOtherwhereGlyphs = numOtherwhereGlyphs;
		this.numInfernalGlyphs = numInfernalGlyphs;
	}

	public void addGlyph(final World world, final int posX, final int posY, final int posZ) {
		addGlyph(world, posX, posY, posZ, false);
	}

	public void addGlyph(final World world, final int posX, final int posY, final int posZ, final boolean remove) {
		if(requiredGlyphs <= 0) {
			return;
		}
		final Block blockID = world.getBlock(posX, posY, posZ);
		boolean found = false;
		if(Witchery.Blocks.GLYPH_RITUAL == blockID) {
			++numRitualGlyphs;
			found = true;
		}
		else if(Witchery.Blocks.GLYPH_OTHERWHERE == blockID) {
			++numOtherwhereGlyphs;
			found = true;
		}
		else if(Witchery.Blocks.GLYPH_INFERNAL == blockID) {
			++numInfernalGlyphs;
			found = true;
		}
		if(remove && found) {
			world.setBlockToAir(posX, posY, posZ);
		}
	}

	public void removeIfRequired(final ArrayList<Circle> circlesToFind) {
		if(!isComplete()) {
			return;
		}
		for(int i = 0; i < circlesToFind.size(); ++i) {
			if(isMatch(circlesToFind.get(i))) {
				circlesToFind.remove(i);
				return;
			}
		}
	}

	private boolean isMatch(final Circle other) {
		return numRitualGlyphs == other.numRitualGlyphs && numOtherwhereGlyphs == other.numOtherwhereGlyphs && numInfernalGlyphs == other.numInfernalGlyphs;
	}

	public boolean isComplete() {
		return requiredGlyphs == getGlyphCount();
	}

	private int getGlyphCount() {
		return numRitualGlyphs + numOtherwhereGlyphs + numInfernalGlyphs;
	}

	public int getRadius() {
		return (requiredGlyphs + 2) / 6 + 1;
	}

	public MultiBlock getMultiBlock() {
		MultiBlock mb = new MultiBlock();
		mb.addComponent(0, 0, 0, Witchery.Blocks.CIRCLE, 0);
		int circleSize = getCircleSize();
		Block glyphType = getGlyphType();
		for(int i = -7; i <= 7; i++) {
			for(int j = -7; j <= 7; j++) {
				if(PATTERN[i + 7][j + 7] == circleSize) {
					mb.addComponent(i, 0, j, glyphType, -1);
				}
			}
		}
		return mb;
	}

	public Block getGlyphType() {
		if(numRitualGlyphs == requiredGlyphs) {
			return Witchery.Blocks.GLYPH_RITUAL;
		}
		if(numOtherwhereGlyphs == requiredGlyphs) {
			return Witchery.Blocks.GLYPH_OTHERWHERE;
		}
		if(numInfernalGlyphs == requiredGlyphs) {
			return Witchery.Blocks.GLYPH_INFERNAL;
		}
		return Witchery.Blocks.GLYPH_RITUAL;
	}

	public int getCircleSize() {
		final int size = getGlyphCount();
		if(size == 40) {
			return 1;
		}
		if(size == 28) {
			return 2;
		}
		return 3;
	}
	
}
