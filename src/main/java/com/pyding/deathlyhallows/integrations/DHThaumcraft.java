package com.pyding.deathlyhallows.integrations;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.blocks.BlockVisConverter;
import com.pyding.deathlyhallows.blocks.DHBlocks;
import com.pyding.deathlyhallows.blocks.tiles.TileEntityVisConverter;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.foci.ItemFocusInferioisMutandis;
import com.pyding.deathlyhallows.recipes.DHWorkbenchRecipes;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import static com.pyding.deathlyhallows.blocks.DHBlocks.visConverter;
import static com.pyding.deathlyhallows.items.DHItems.deathShard;
import static com.pyding.deathlyhallows.items.DHItems.hobgoblinSoul;
import static com.pyding.deathlyhallows.items.DHItems.inferioisMutandis;
import static thaumcraft.api.aspects.Aspect.*;

public final class DHThaumcraft {
	
	public static void init() {
		DHItems.register(inferioisMutandis = new ItemFocusInferioisMutandis());
		DHBlocks.register(visConverter = new BlockVisConverter());
		DHBlocks.registerTile(TileEntityVisConverter.class, "visConverterTile");
	}

	public static void recipes() {
		DHWorkbenchRecipes.addShapedRecipe(
				new ItemStack(visConverter),
				"BHB",
				"HSH", 
				"BHB",
				'B', new ItemStack(Witchery.Items.MYSTIC_BRANCH),
				'H', Witchery.Items.GENERIC.itemDemonHeart.createStack(),
				'S', new ItemStack(deathShard));
	}

	public static void aspects() {
		addAspects(
				new ItemStack(inferioisMutandis),
				EXCHANGE, 12,
				PLANT, 2,
				MAGIC, 2
		);
		addAspects(
				new ItemStack(DHItems.viscousSecretions),
				VOID, 8,
				ELDRITCH, 4,
				SLIME, 4,
				HUNGER, 3,
				DEATH, 3,
				LIFE, 3,
				SENSES, 3,
				FLESH, 2,
				ORDER, 2
		);
		addAspects(
				new ItemStack(DHItems.tarotCards),
				SENSES, 6,
				GREED, 4,
				MAGIC, 2,
				MIND, 2
		);
		addAspects(
				new ItemStack(DHItems.trickOrTreat),
				GREED, 12,
				EXCHANGE, 8,
				SENSES, 4,
				CROP, 2

		);
		addAspects(
				new ItemStack(DHItems.deadlyPrism),
				DEATH, 32,
				WEAPON, 32,
				SENSES, 16
		);
		addAspects(
				new ItemStack(deathShard),
				DEATH, 64,
				UNDEAD, 32,
				ORDER, 32,
				DARKNESS, 16,
				getAspect("terminus"), 16
		);
		addAspects(
				new ItemStack(DHItems.elderWand),
				DEATH, 64,
				MAGIC, 13,
				DARKNESS, 8,
				
				MIND, 19,
				WEAPON, 17
		);
		addAspects(
				new ItemStack(DHItems.invisibilityMantle),
				DEATH, 64,
				MAGIC, 13,
				DARKNESS, 8,
				
				LIGHT, 19,
				ARMOR, 17
		);
		addAspects(
				new ItemStack(DHItems.resurrectionStone),
				DEATH, 64,
				MAGIC, 13,
				DARKNESS, 8,
				
				HEAL, 19,
				LIFE, 17
		);
		addAspects(
				new ItemStack(DHItems.bertieBots),
				HUNGER, 4,
				POISON, 4,
				HEAL, 4
		);
		addAspects(
				new ItemStack(DHItems.gastronomicTemptation),
				SOUL, 2,
				HUNGER, 2,
				DEATH, 2,
				WATER, 2,
				POISON, 2
		);
		addAspects(
				new ItemStack(DHItems.niceCream),
				HEAL, 8,
				COLD, 4,
				HUNGER, 4,
				SENSES, 2,
				LIFE, 2
		);
		addAspects(new ItemStack(DHItems.soupWithSawdust),
				TREE, 4,
				SLIME, 2,
				WATER, 2,
				HUNGER, 2,
				// bucket
				METAL, 8,
				VOID, 1
		);
		addAspects(
				new ItemStack(hobgoblinSoul),
				SOUL, 8,
				BEAST, 3
		);
		addAspects(
				new ItemStack(DHItems.hobgoblinChains),
				TRAP, 16,
				MINE, 16,
				GREED, 4,
				ORDER, 4
		);
		addAspects(
				new ItemStack(DHItems.nimbus),
				TREE, 2,
				TOOL, 1,
				
				MAGIC, 4,
				FLIGHT, 4,
				WEATHER, 4,
				FIRE, 2
		);
		addAspects(
				new ItemStack(DHItems.bag),
				CLOTH, 8,
				VOID, 1

		);
		addAspects(
				new ItemStack(DHItems.lightningInBag),
				CLOTH, 8,
				WEATHER, 8,
				getAspect("magneto"), 4,
				getAspect("electrum"), 2
				
		);
		addAspects(
				new ItemStack(DHItems.monsterBook),
				ELDRITCH, 4,
				MIND, 4,
				HUNGER, 4,
				TRAP, 4,
				WEAPON, 4,
				EXCHANGE, 2,
				CLOTH, 1
		);
		addAspects(
				new ItemStack(DHItems.elderChalk),
				TREE, 2,
				FIRE, 2,
				
				ELDRITCH, 8,
				DEATH, 4
		);
		addAspects(
				new ItemStack(DHBlocks.elderGlyph),
				TREE, 2,
				FIRE, 2,

				ELDRITCH, 8,
				DEATH, 4
		);
		addAspects(
				new ItemStack(DHItems.elderBook),
				MIND, 5,
				MAGIC, 1,
				
				ELDRITCH, 8,
				DEATH, 4
		);
		addAspects(
				new ItemStack(visConverter),
				AURA, 32,
				ENERGY, 32,
				DARKNESS, 24,
				SOUL, 12,
				ORDER, 8
		);
		// lol
		addAspects(
				new ItemStack(DHItems.itemLogo),
				ELDRITCH, 64,
				DEATH, 64
		);
	}

	private static void addAspects(ItemStack stack, Object... as) {
		ThaumcraftApi.registerObjectTag(stack, createAspectList(as));
	}

	private static AspectList createAspectList(Object... as) {
		final AspectList aspects = new AspectList();
		for(int i = 0; i < as.length / 2; ++i) {
			if(as[2 * i] == null || ((Integer)as[2 * i + 1]) < 1) {
				continue;
			}
			aspects.add((Aspect)as[2 * i], (Integer)as[2 * i + 1]);
		}
		return aspects;
	}

}
