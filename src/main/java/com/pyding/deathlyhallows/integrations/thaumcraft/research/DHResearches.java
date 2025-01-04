package com.pyding.deathlyhallows.integrations.thaumcraft.research;

import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.blocks.DHBlocks;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.wands.ItemWandCap;
import com.pyding.deathlyhallows.items.wands.ItemWandRod;
import com.pyding.deathlyhallows.recipes.DHWorkbenchRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

import static thaumcraft.api.aspects.Aspect.*;

public class DHResearches {

	public static final String
			CATEGORY_DH = "DeathlyHallows";

	public static ResearchItem
			// entries
			VISC,
			INFERIOIS_MUTANDIS,
	// wands
	ROD_ROWAN,
			ROD_ALDER,
			ROD_HAWTHORN,
			CAP_KOBOLDITE,
			CAP_COTTON;

	public static void init() {
		ResearchCategories.registerCategory(CATEGORY_DH,
				new ResourceLocation(DeathlyHallows.MODID, "textures/misc/r_icon.png"),
				new ResourceLocation(DeathlyHallows.MODID, "textures/gui/thaumcraft/research_back.png")
		);

		VISC = new DHGResearchItem(
				"VISC", CATEGORY_DH,
				new AspectList()
						.add(DEATH, 4)
						.add(ENERGY, 2)
						.add(MAGIC, 1),
				2, 0,
				1,
				new ItemStack(DHBlocks.visConverter)
		)
				.setPages(new ResearchPage("dh.research_page.VISC.1"), new ResearchPage(DHWorkbenchRecipes.VISC))
				.setSpecial()
				.setRound()
				.setAutoUnlock()
				.registerResearchItem();
		INFERIOIS_MUTANDIS = new DHGResearchItem(
				"INFERIOIS_MUTANDIS", CATEGORY_DH,
				new AspectList()
						.add(PLANT, 4)
						.add(EXCHANGE, 2)
						.add(MAGIC, 1),
				-2, 0,
				1,
				new ItemStack(DHItems.inferioisMutandis)
		)
				.setPages(new ResearchPage("dh.research_page.INFERIOIS_MUTANDIS.1"))
				.setSpecial()
				.setRound()
				.setAutoUnlock()
				.registerResearchItem();
		CAP_KOBOLDITE = new DHGResearchItem(
				"CAP_koboldite", CATEGORY_DH,
				new AspectList()
						.add(EARTH, 8)
						.add(MINE, 4)
						.add(METAL, 2)
						.add(TOOL, 2)
						.add(MAGIC, 2),
				0, 0,
				1,
				new ItemStack(DHItems.wandCap, 1, ItemWandCap.Caps.koboldite.ordinal())
		)
				.setVirtual()
				.setAutoUnlock()
				.registerResearchItem();
		CAP_COTTON = new DHGResearchItem(
				"CAP_cotton", CATEGORY_DH,
				new AspectList()
						.add(CLOTH, 6)
						.add(MAGIC, 6)
						.add(TOOL, 2)
						.add(MIND, 1),
				0, 0,
				1,
				new ItemStack(DHItems.wandCap, 1, ItemWandCap.Caps.cotton.ordinal())
		)
				.setVirtual()
				.setAutoUnlock()
				.registerResearchItem();
		ROD_ROWAN = new DHGResearchItem(
				"ROD_rowan", CATEGORY_DH,
				new AspectList()
						.add(TREE, 6)
						.add(MAGIC, 4)
						.add(TOOL, 4),
				0, 0,
				1,
				new ItemStack(DHItems.wandRod, 1, ItemWandRod.Rods.rowan.ordinal())
		)
				.setVirtual()
				.setAutoUnlock()
				.registerResearchItem();
		ROD_ALDER = new DHGResearchItem(
				"ROD_alder", CATEGORY_DH,
				new AspectList()
						.add(TREE, 6)
						.add(MAGIC, 4)
						.add(TOOL, 4),
				0, 0,
				1,
				new ItemStack(DHItems.wandRod, 1, ItemWandRod.Rods.alder.ordinal())
		)
				.setVirtual()
				.setAutoUnlock()
				.registerResearchItem();
		ROD_HAWTHORN = new DHGResearchItem(
				"ROD_hawthorn", CATEGORY_DH,
				new AspectList()
						.add(TREE, 6)
						.add(MAGIC, 4)
						.add(TOOL, 4),
				0, 0,
				1,
				new ItemStack(DHItems.wandRod, 1, ItemWandRod.Rods.hawthorn.ordinal())
		)
				.setVirtual()
				.setAutoUnlock()
				.registerResearchItem();
	}

}
