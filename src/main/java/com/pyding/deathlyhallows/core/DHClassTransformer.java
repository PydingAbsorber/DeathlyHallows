package com.pyding.deathlyhallows.core;

import airburn.fasmtel.transformers.ClassTransformerBase;
import com.pyding.deathlyhallows.core.transformers.InfusionTransformer;
import com.pyding.deathlyhallows.core.transformers.ItemMysticBranchTransformer;
import com.pyding.deathlyhallows.core.transformers.ItemWandCastingTransformer;
import com.pyding.deathlyhallows.core.transformers.SymbolEffectTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

public class DHClassTransformer implements IClassTransformer {
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		ClassTransformerBase transformer = transform(name, basicClass);
		if(transformer == null) {
			return basicClass;
		}
		return transformer.transform();
	}

	private ClassTransformerBase transform(String name, byte[] basicClass) {
		switch(name) {
			case "com.emoniph.witchery.item.ItemMysticBranch":
				return new ItemMysticBranchTransformer(basicClass);
			case "com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect":
				return new SymbolEffectTransformer(basicClass);
			case "com.emoniph.witchery.infusion.Infusion":
				return new InfusionTransformer(basicClass);
			case "thaumcraft.common.items.wands.ItemWandCasting":
				return new ItemWandCastingTransformer(basicClass);
		}
		return null;
	}

}
