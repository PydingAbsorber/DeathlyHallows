package com.pyding.deathlyhallows.multiblocks.structures;

import com.pyding.deathlyhallows.multiblocks.OctagonPart;

public class StructureOctagonDrawing extends StructureBase {

	private final OctagonPart core;
	private final OctagonPart[] circles;
	
	public StructureOctagonDrawing(OctagonPart core, OctagonPart... circles) {
		this.core = core;
		this.circles = circles;
	}
	
	@Override
	protected void fillStructure() {
		add(core.getRune(), core.getMeta(), pos(0, 0, 0));
		for(OctagonPart circle : circles) {
			add(circle.getRune(), circle.getMeta(), circle.getPositions());
		}
	}

}
