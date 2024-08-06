package com.pyding.deathlyhallows.core;

import com.pyding.deathlyhallows.core.transformers.ItemMysticBranchTransformer;
import com.pyding.deathlyhallows.core.transformers.SymbolEffectTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

public class DHClassTransformer implements IClassTransformer {
	@Override
	public byte[] transform(String name, String transformedName, byte[] cls) {
		switch(name) {
			case "com.emoniph.witchery.item.ItemMysticBranch":
				return new ItemMysticBranchTransformer(cls).transform();
			case "com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect":
				return new SymbolEffectTransformer(cls).transform();
		}
		return cls;
	}

}
