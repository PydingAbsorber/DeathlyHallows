package com.pyding.deathlyhallows.integrations;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.blocks.BlockVisConverter;
import com.pyding.deathlyhallows.blocks.tiles.TileEntityVisConverter;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.ItemFocusInferioisMutandis;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import static com.pyding.deathlyhallows.blocks.DHBlocks.visConverter;
import static com.pyding.deathlyhallows.items.DHItems.deathShard;
import static com.pyding.deathlyhallows.items.DHItems.hobgoblinSoul;
import static com.pyding.deathlyhallows.items.DHItems.inferioisMutandis;
import static thaumcraft.api.aspects.Aspect.*;

public final class DHThaumcraft {
	public static void init() {
		visConverter = new BlockVisConverter();
		inferioisMutandis = new ItemFocusInferioisMutandis();
		GameRegistry.registerBlock(visConverter, "visconverter");
		GameRegistry.registerTileEntity(TileEntityVisConverter.class, "visconverterTile");
		GameRegistry.registerItem(inferioisMutandis, inferioisMutandis.getUnlocalizedName().substring(5));

	}

	public static void recipes() {
		GameRegistry.addShapedRecipe(
				new ItemStack(visConverter),
				"BHB",
				"HSH", 
				"BHB",
				'B', new ItemStack(Witchery.Items.MYSTIC_BRANCH),
				'H', Witchery.Items.GENERIC.itemDemonHeart.createStack(),
				'S', new ItemStack(deathShard));
	}

	public static void aspects() {
		// TODO fix aspects
		addAspects(
				new ItemStack(DHItems.elderWand),
				MAGIC, 13,
				DEATH, 666,
				DARKNESS, 4,
				WEAPON, 17
		);
		addAspects(
				new ItemStack(DHItems.invisibilityMantle),
				MAGIC, 13,
				DEATH, 666,
				DARKNESS, 4,
				ARMOR, 17
		);
		addAspects(
				new ItemStack(DHItems.resurrectionStone),
				MAGIC, 13,
				DEATH, 666,
				DARKNESS, 4,
				LIFE, 17
		);
		addAspects(
				new ItemStack(DHItems.bertieBots),
				HUNGER, 16,
				POISON, 8,
				HEAL, 8
		);
		addAspects(
				new ItemStack(DHItems.gastronomicTemptation),
				SOUL, 8,
				DEATH, 8,
				UNDEAD, 8,
				HUNGER, 4
		);
		addAspects(new ItemStack(DHItems.resurrectionStone),
				FLESH, 1,
				WATER, 8,
				TREE, 16
		);
		addAspects(
				new ItemStack(hobgoblinSoul),
				SOUL, 16,
				BEAST, 5
		);
		addAspects(
				new ItemStack(DHItems.hobgoblinChains),
				SOUL, 16,
				TRAP, 64,
				MINE, 64
		);
		addAspects(
				new ItemStack(DHItems.nimbus),
				FLIGHT, 32,
				TREE, 7,
				WEATHER, 10
		);
		addAspects(
				new ItemStack(DHItems.lightningInBag),
				CLOTH, 4,
				WEATHER, 8,
				getAspect("electrum"), 2,
				getAspect("magneto"), 4
				
		);
		addAspects(
				new ItemStack(ItemBlockSpecialFlower.ofType("screamily").getItem()),
				TRAP, 210,
				MAGIC, 400,
				PLANT, 30,
				ENERGY, 1000
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
