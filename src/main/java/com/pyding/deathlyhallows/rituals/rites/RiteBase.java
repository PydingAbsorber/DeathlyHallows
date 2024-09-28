package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.ritual.RitualStep;

import java.util.ArrayList;
import java.util.Arrays;

public class RiteBase {

	private final RitualStep[] steps;
	
	public RiteBase(RitualStep... steps) {
		this.steps = steps;
	}

	public void addSteps(ArrayList<RitualStep> steps, int initialStage) {
		steps.addAll(Arrays.asList(this.steps));
	}
	
}
