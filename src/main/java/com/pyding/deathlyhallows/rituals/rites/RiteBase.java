package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.ritual.RitualStep;

import java.util.ArrayList;

public abstract class RiteBase {
	
	public RiteBase() {
	}

	public abstract void addSteps(ArrayList<RitualStep> steps, int initialStage);
	
}
