package com.pyding.deathlyhallows.integrations.thaumcraft.research;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;

public class DHGResearchItem extends ResearchItem {

	private String integration;

	public DHGResearchItem(String key, String category, AspectList tags, int col, int row, int complex, ResourceLocation icon) {
		super(key, category, tags, col, row, complex, icon);
		if(complex > 3) { // if I set it, like, 4... I want it to be 4.
			setComplexity(complex);
		}
	}

	public DHGResearchItem(String key, String category, AspectList tags, int col, int row, int complex, ItemStack icon) {
		super(key, category, tags, col, row, complex, icon);
		if(complex > 3) { // if I set it, like, 4... I want it to be 4.
			setComplexity(complex);
		}
	}

	@Override
	public Aspect getResearchPrimaryTag() {
		Aspect aspect = super.getResearchPrimaryTag();
		return aspect != null ? aspect : tags.getAspectsSorted()[0]; // I love doing Aspect(Shit, 0)!!!
	}

	// use if other mods (than TC4, Witchery and DH) is required for research
	public DHGResearchItem setIntegrationTag(String tag) {
		integration = tag;
		return this;
	}
	
	@SideOnly(Side.CLIENT)
	public String getName() {
		return StatCollector.translateToLocal("dh.research_name." + key);
	}

	@SideOnly(Side.CLIENT)
	public String getText() {
		return "[DH] " + (integration == null ? "" : integration + " ") + StatCollector.translateToLocal("dh.research_text." + key);
	}

}
