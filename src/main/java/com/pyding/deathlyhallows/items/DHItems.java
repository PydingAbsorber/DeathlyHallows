package com.pyding.deathlyhallows.items;

import com.pyding.deathlyhallows.blocks.DHBlocks;
import com.pyding.deathlyhallows.render.item.RenderEldenWand;
import com.pyding.deathlyhallows.render.item.RenderItemVisConverter;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import static com.pyding.deathlyhallows.DeathHallowsMod.tabDeathlyHallows;

public final class DHItems {

	public static Item
			tabItem,
			invisibilityMantle,
			elderWand,
			resurrectionStone,
			creativeItem,
			bertieBots,
			gastronomicTemptation,
			soupWithSawdust,
			viscousSecretions,
			hobgoblinChains,
			deadlyPrism,
			hobgoblinSoul,
			nimbus,
			deathShard,
			cards,
			inferioisMutandis,
			monsterBook,
			trickOrTreat,
			elderBook,
			lightningInBag,
			elderChalk;

	private DHItems() {
	}

	public static void init() {
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
		tabItem = new Item().setUnlocalizedName("TabItem").setTextureName("dh:logo").setMaxStackSize(1);
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

		elderChalk = new ElderChalk(DHBlocks.elderRitualBlock).setUnlocalizedName("elder_chalk")
															  .setTextureName("dh:chalk")
															  .setMaxStackSize(1)
															  .setCreativeTab(tabDeathlyHallows);

		register(invisibilityMantle);
		register(elderWand);
		register(resurrectionStone);
		register(tabItem);
		register(creativeItem);
		register(bertieBots);
		register(gastronomicTemptation);
		register(soupWithSawdust);
		register(viscousSecretions);
		register(hobgoblinChains);
		register(deadlyPrism);
		register(hobgoblinSoul);
		register(nimbus);
		register(deathShard);
		register(cards);
		register(monsterBook);
		register(trickOrTreat);
		register(elderBook);
		register(lightningInBag);
		register(elderChalk);
	}

	@SideOnly(Side.CLIENT)
	public static void initClient() {
		render(elderWand, new RenderEldenWand());
		render(ItemBlock.getItemFromBlock(DHBlocks.visConverter), new RenderItemVisConverter());
	}

	private static void register(Item item) {
		GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
	}

	@SideOnly(Side.CLIENT)
	private static void render(Item item, IItemRenderer renderer) {
		MinecraftForgeClient.registerItemRenderer(item, renderer);
	}

}
