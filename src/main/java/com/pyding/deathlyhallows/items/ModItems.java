package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.blocks.BlockCircle;
import com.pyding.deathlyhallows.blocks.ElderRitualBlock;
import com.pyding.deathlyhallows.blocks.VisConverterTile;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import static com.pyding.deathlyhallows.DeathHallowsMod.tabDeathlyHallows;

public class ModItems {
	public static Item invisibilityMantle;
	public static Item elderWand;
	public static Item resurrectionStone;
	public static Item tabItem;
	public static Item creativeItem;
	public static Item bertieBots;

	public static Item gastronomicTemptation;
	public static Item soupWithSawdust;
	public static Item viscousSecretions;
	public static Item hobgoblinChains;
	public static Item deadlyPrism;
	public static Item hobgoblinSoul;
	public static Item nimbus;
	public static Item deathShard;
	public static Item cards;
	public static Block visc;
	public static Item inferioisMutandis;

	public static Item monsterBook;
	public static Item trickOrTreat;
	public static Item elderBook;
	public static Item lightningInBag;
	public static Item elderChalk;
	public static Block elderRitualBlock;

	public ModItems(){
	}
	
	public static void init(){
		invisibilityMantle = new InvisibilityMantle().setUnlocalizedName("InvisibilityMantle")
													 .setTextureName("dh:mantle")
													 .setMaxStackSize(1)
													 .setCreativeTab(tabDeathlyHallows);
		elderWand = new ElderWand().setUnlocalizedName("ElderWand")
								   .setTextureName("dh:wand")
								   .setMaxStackSize(1)
								   .setCreativeTab(tabDeathlyHallows);
		resurrectionStone = new ResurrectionStone().setUnlocalizedName("ResurrectionStone")
												   .setTextureName("dh:ring")
												   .setMaxStackSize(1)
												   .setCreativeTab(tabDeathlyHallows);
		tabItem = new TabItem().setUnlocalizedName("TabItem").setTextureName("dh:logo").setMaxStackSize(1);
		creativeItem = new CreativeItem().setUnlocalizedName("CreativeItem")
										 .setTextureName("dh:creative2")
										 .setMaxStackSize(1)
										 .setCreativeTab(tabDeathlyHallows);
		bertieBots = new BertieBotts(8, 20).setUnlocalizedName("BertieBotts")
										   .setTextureName("dh:candy")
										   .setMaxStackSize(64)
										   .setCreativeTab(tabDeathlyHallows);
		gastronomicTemptation = new GastronomicTemptation().setUnlocalizedName("GastronomicTemptation")
														   .setTextureName("dh:gastro")
														   .setMaxStackSize(64)
														   .setCreativeTab(tabDeathlyHallows);
		soupWithSawdust = new SoapWithSawdust().setUnlocalizedName("Soup")
											   .setTextureName("dh:soup")
											   .setMaxStackSize(64)
											   .setCreativeTab(null);
		viscousSecretions = new ViscousSecretions().setUnlocalizedName("ViscousSecretions")
												   .setTextureName("dh:secret")
												   .setMaxStackSize(64)
												   .setCreativeTab(tabDeathlyHallows);
		hobgoblinChains = new HobgoblinChains().setUnlocalizedName("HobgoblinChains")
											   .setTextureName("dh:chains")
											   .setMaxStackSize(64)
											   .setCreativeTab(tabDeathlyHallows);
		deadlyPrism = new DeadlyPrism().setUnlocalizedName("DeadlyPrism")
									   .setTextureName("dh:prism1")
									   .setMaxStackSize(1)
									   .setCreativeTab(tabDeathlyHallows);
		hobgoblinSoul = new HobgoblinSoul().setUnlocalizedName("HobgoblinSoul")
										   .setTextureName("dh:goblinsoul")
										   .setMaxStackSize(64)
										   .setCreativeTab(tabDeathlyHallows);
		nimbus = new Nimbus3000().setUnlocalizedName("Nimbus3000")
								 .setTextureName("dh:nimbus")
								 .setMaxStackSize(1)
								 .setCreativeTab(tabDeathlyHallows);
		deathShard = new DeathShard().setUnlocalizedName("deathShard")
									 .setTextureName("dh:shard")
									 .setMaxStackSize(16)
									 .setCreativeTab(tabDeathlyHallows);
		cards = new Cards().setUnlocalizedName("cards")
						   .setTextureName("dh:cards_daybreak")
						   .setMaxStackSize(1)
						   .setCreativeTab(tabDeathlyHallows);
		monsterBook = new MonsterBook().setUnlocalizedName("monsterBook")
									   .setTextureName("dh:monsterbook")
									   .setMaxStackSize(1)
									   .setCreativeTab(tabDeathlyHallows);
		trickOrTreat = new TrickOrTreat().setUnlocalizedName("trick")
										 .setTextureName("dh:trick")
										 .setMaxStackSize(64)
										 .setCreativeTab(tabDeathlyHallows);
		elderBook = new ElderBook().setUnlocalizedName("elder_book")
								   .setTextureName("dh:book")
								   .setMaxStackSize(1)
								   .setCreativeTab(tabDeathlyHallows);
		lightningInBag = new LightningInBag().setUnlocalizedName("lightning_in_bag")
								   .setTextureName("dh:bag")
								   .setMaxStackSize(1)
								   .setCreativeTab(tabDeathlyHallows);

		elderRitualBlock = new ElderRitualBlock().setBlockName("elder_ritual_block")
												 .setBlockTextureName("dh:elderGlyph");


		elderChalk = new ElderChalk(elderRitualBlock).setUnlocalizedName("elder_chalk")
													 .setTextureName("dh:chalk")
													 .setMaxStackSize(1)
													 .setCreativeTab(tabDeathlyHallows);
		
		GameRegistry.registerItem(invisibilityMantle, invisibilityMantle.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(elderWand, elderWand.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(resurrectionStone, resurrectionStone.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(tabItem, tabItem.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(creativeItem, creativeItem.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(bertieBots, bertieBots.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(gastronomicTemptation, gastronomicTemptation.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(soupWithSawdust, soupWithSawdust.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(viscousSecretions, viscousSecretions.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(hobgoblinChains, hobgoblinChains.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(deadlyPrism, deadlyPrism.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(hobgoblinSoul, hobgoblinSoul.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(nimbus, nimbus.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(deathShard, deathShard.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(cards, cards.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(monsterBook, monsterBook.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(trickOrTreat, trickOrTreat.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(elderBook, elderBook.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(lightningInBag, lightningInBag.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(elderChalk, elderChalk.getUnlocalizedName().substring(5));
		//GameRegistry.registerBlock(elderRitualBlock, "elder_ritual_block");
		//GameRegistry.registerTileEntity(ElderRitualBlock.TileEntityCircle.class, "elder_ritual_block_tile");
	}
}
