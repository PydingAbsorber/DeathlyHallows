package com.pyding.deathlyhallows.multiblocks.structures;

import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.utils.IMultiBlockHandler;

public class DHStructures {

	public static final IMultiBlockHandler EMPTY = new StructureBase() {
		@Override
		protected void fillStructure() {
		}
	};

	public static IMultiBlockHandler
			basik,
			iceCastle,
			fishLake,
			sonata,
			curse,
			mending,
			hunt,
			purify,
			coven;

	public static void init() {
		sonata = new StructureSonata();
		iceCastle = new StructureIceCastle();
		fishLake = new StructureFishLake();
		purify = new StructurePurify();
		curse = new StructureCurse();
		coven = new StructureCoven();
		mending = new StructureMending();
		hunt = new StructureHunt();
		if(DHIntegration.thaumcraft) {
			basik = new StructureBasik();
		}
	}

}
