package com.pyding.deathlyhallows.items;

import com.pyding.deathlyhallows.blocks.DHBlocks;
import com.pyding.deathlyhallows.items.baubles.ItemBaubleInvisibilityMantle;
import com.pyding.deathlyhallows.items.baubles.ItemBaubleResurrectionStone;
import com.pyding.deathlyhallows.items.food.ItemFoodBertieBotts;
import com.pyding.deathlyhallows.items.food.ItemFoodSoupWithSawdust;
import com.pyding.deathlyhallows.items.food.ItemFoodViscousSecretions;
import com.pyding.deathlyhallows.items.food.ItemNiceCream;
import com.pyding.deathlyhallows.render.item.RenderEldenWand;
import com.pyding.deathlyhallows.render.item.RenderItemVisConverter;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public final class DHItems {

	public static Item
			itemLogo,
			invisibilityMantle,
			elderWand,
			resurrectionStone,
			elfToken,
			bertieBots,
			gastronomicTemptation,
			soupWithSawdust,
			viscousSecretions,
			hobgoblinChains,
			deadlyPrism,
			hobgoblinSoul,
			nimbus,
			deathShard,
			tarotCards,
			inferioisMutandis,
			monsterBook,
			trickOrTreat,
			elderBook,
			lightningInBag,
			bag,
			elderChalk,
			niceCream;

	private DHItems() {
		
	}

	public static void init() {
		register(itemLogo = new ItemBase("itemLogo", 1, null));
		register(invisibilityMantle = new ItemBaubleInvisibilityMantle());
		register(elderWand = new ItemElderWand());
		register(resurrectionStone = new ItemBaubleResurrectionStone());
		register(elfToken = new ItemElfToken());
		register(bertieBots = new ItemFoodBertieBotts());
		register(gastronomicTemptation = new ItemGastronomicTemptation());
		register(soupWithSawdust = new ItemFoodSoupWithSawdust());
		register(viscousSecretions = new ItemFoodViscousSecretions());
		register(hobgoblinSoul = new ItemHobgoblinSoul());
		register(hobgoblinChains = new ItemHobgoblinChains());
		register(deadlyPrism = new ItemDeadlyPrism());
		register(nimbus = new ItemNimbus());
		register(deathShard = new ItemDeathShard());
		register(tarotCards = new ItemTarotCards());
		register(monsterBook = new ItemMonsterBook());
		register(trickOrTreat = new ItemTrickOrTreat());
		register(elderBook = new ItemElderBook());
		register(elderChalk = new ItemElderChalk());
		register(bag = new ItemBag());
		register(lightningInBag = new ItemLightningInBag());
		register(niceCream = new ItemNiceCream());
	}

	@SideOnly(Side.CLIENT)
	public static void initClient() {
		render(elderWand, new RenderEldenWand());
		render(ItemBlock.getItemFromBlock(DHBlocks.visConverter), new RenderItemVisConverter());
	}

	public static void register(Item item) {
		GameRegistry.registerItem(item, item.getUnlocalizedName().replaceFirst("item.", ""));
	}

	@SideOnly(Side.CLIENT)
	private static void render(Item item, IItemRenderer renderer) {
		MinecraftForgeClient.registerItemRenderer(item, renderer);
	}

}
