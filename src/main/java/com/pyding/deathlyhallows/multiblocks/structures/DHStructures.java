package com.pyding.deathlyhallows.multiblocks.structures;

import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.rituals.BlockList;
import com.pyding.deathlyhallows.utils.IMultiBlockHandler;

public class DHStructures {
	
	public static final IMultiBlockHandler EMPTY = new StructureBase(){
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
		if(DHIntegration.thaumcraft) {
			basik = new StructureBasik();
		}
		// not yet implemented
		curse = new BlockList(new int[]{5, 0, 5}, StructuresString.curse);
		mending = new BlockList(new int[]{0, 0, 0}, new String[][][]{});
		hunt = new BlockList(new int[]{0, 0, 0}, new String[][][]{});
		coven = new BlockList(new int[]{8, 0, 8}, StructuresString.coven);
	}
	
}
