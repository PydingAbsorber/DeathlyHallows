package com.pyding.deathlyhallows.rituals;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.entity.EntityDeath;
import com.emoniph.witchery.entity.EntityGoblinGulg;
import com.emoniph.witchery.entity.EntityGoblinMog;
import com.emoniph.witchery.entity.EntityHornedHuntsman;
import com.emoniph.witchery.entity.EntityNightmare;
import com.emoniph.witchery.ritual.RitualTraits;
import com.pyding.deathlyhallows.entities.EntityAbsoluteDeath;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.multiblocks.structures.DHStructures;
import com.pyding.deathlyhallows.rituals.rites.*;
import com.pyding.deathlyhallows.utils.DHConfig;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import thaumcraft.common.config.ConfigItems;

import java.util.EnumSet;

import static com.emoniph.witchery.Witchery.Items;
import static com.emoniph.witchery.ritual.RitualTraits.ONLY_AT_NIGHT;
import static com.pyding.deathlyhallows.items.DHItems.gastronomicTemptation;
import static com.pyding.deathlyhallows.items.DHItems.nimbus;
import static com.pyding.deathlyhallows.items.DHItems.viscousSecretions;
import static net.minecraft.init.Items.bread;
import static net.minecraft.init.Items.feather;
import static net.minecraft.init.Items.fishing_rod;
import static net.minecraft.init.Items.golden_apple;


public final class DHRituals {

	private DHRituals() {

	}

	public static void init() {
		DHStructures.init();
		ElderRiteRegistry.instance().rituals.clear();

		int id = 111; // TODO ???

		ElderRiteRegistry.addRecipe(id++, 26,
				"dh.rite.death",
				new ElderSummonCreature(EntityAbsoluteDeath.class, false),
				new ElderSacrificeMultiple(
						new ElderSacrificeItem(
								Items.GENERIC.itemRefinedEvil.createStack(),
								Items.GENERIC.itemInfernalBlood.createStack(),
								new ItemStack(gastronomicTemptation),
								Items.GENERIC.itemKobolditePentacle.createStack(),
								Items.GENERIC.itemBinkyHead.createStack(),
								Items.GENERIC.itemBrewOfInk.createStack()
						),
						new ElderSacrificeLiving(EntityDeath.class),
						new ElderSacrificeLiving(EntityZombie.class),
						new ElderSacrificeLiving(EntitySkeleton.class),
						new ElderSacrificeLiving(EntityEnderman.class),
						new ElderSacrificeLiving(EntityNightmare.class),
						new ElderSacrificeLiving(EntityGoblinGulg.class),
						new ElderSacrificeLiving(EntityGoblinMog.class),
						new ElderSacrificeLiving(EntityHornedHuntsman.class),
						new ElderSacrificePower(21000.0F, 20)
				),
				EnumSet.noneOf(RitualTraits.class),
				new Circle(0, 0, 40),
				new Circle(0, 28, 0),
				new Circle(16, 0, 0)
		);
		ElderRiteRegistry.addRecipe(id++, 35,
				"dh.rite.nimbus",
				new ElderSummonItem(new ItemStack(nimbus),
						ElderSummonItem.Binding.NONE),
				new ElderSacrificeMultiple(
						new ElderSacrificeItem(
								Items.GENERIC.itemBroomEnchanted.createStack(),
								Items.GENERIC.itemFlyingOintment.createStack(),
								Items.GENERIC.itemOwletsWing.createStack(),
								Items.GENERIC.itemBrewOfSoaring.createStack(),
								new ItemStack(feather)
						),
						new ElderSacrificePower(15000.0F, 20)
				),
				EnumSet.of(ONLY_AT_NIGHT), // night? maybe rain?
				new Circle(16, 0, 0),
				new Circle(28, 0, 0),
				new Circle(40, 0, 0)
		);
		ElderRiteRegistry.addRecipe(id++, 5,
				"dh.rite.elf",
				new RiteOfElvenVanishing(8, 40.0f, 0),
				new ElderSacrificeMultiple(
						new ElderSacrificeItem(
								Items.GENERIC.itemBrewOfWasting.createStack(),
								Items.GENERIC.itemBrewOfSoaring.createStack(),
								Items.GENERIC.itemWormyApple.createStack(),
								Items.GENERIC.itemBrewSoulAnguish.createStack(),
								Items.GENERIC.itemBrewGrave.createStack(),
								Items.GENERIC.itemOwletsWing.createStack()
						),
						new ElderSacrificePower(20000.0F, 20)
				),
				EnumSet.of(ONLY_AT_NIGHT),
				new Circle(0, 16, 0),
				new Circle(0, 28, 0),
				new Circle(0, 40, 0)
		);

		if(DHIntegration.thaumcraft && DHConfig.bathHouse) {
			ElderRiteRegistry.addRecipe(id++, 5,
					"dh.rite.banka",
					new RiteWithEffect(8, 80.0f, 0, "DHBanka", System.currentTimeMillis() + 60 * 60 * 1000),
					new ElderSacrificeMultiple(
							new ElderSacrificeItem(
									new ItemStack(DHItems.lightningInBag),
									new ItemStack(ConfigItems.itemBucketDeath),
									new ItemStack(ConfigItems.itemBottleTaint),
									new ItemStack(ConfigItems.itemSanitySoap),
									new ItemStack(Witchery.Blocks.VOID_BRAMBLE),
									new ItemStack(gastronomicTemptation),
									new ItemStack(viscousSecretions)
							),
							new ElderSacrificePower(DHConfig.cost1, 20)),
					EnumSet.noneOf(RitualTraits.class),
					DHStructures.basik
			);
		}
		if(DHConfig.despairSonata) {
			ElderRiteRegistry.addRecipe(id++, 5,
					"dh.rite.sonata",
					new SummonSpiritRite(8, 40.0f, 0),
					new ElderSacrificeMultiple(
							new ElderSacrificeItem(
									Items.GENERIC.itemCongealedSpirit.createStack(),
									Items.GENERIC.itemGraveyardDust.createStack(),
									Items.GENERIC.itemWormwood.createStack(),
									Items.GENERIC.itemDisturbedCotton.createStack(),
									new ItemStack(Witchery.Blocks.WISPY_COTTON),
									Items.GENERIC.itemAttunedStoneCharged.createStack(),
									Items.GENERIC.itemBatWool.createStack()
							),
							new ElderSacrificePower(DHConfig.cost2, 20)
					),
					EnumSet.noneOf(RitualTraits.class),
					DHStructures.sonata
			);
		}
		if(DHConfig.fishCatch) {
			ElderRiteRegistry.addRecipe(id++, 5, "dh.rite.catch", new FishCatchRite(8, 40.0f, 0),
					new ElderSacrificeMultiple(
							new ElderSacrificeItem(
									new ItemStack(fishing_rod),
									Items.GENERIC.itemWormyApple.createStack(),
									new ItemStack(Witchery.Blocks.LEAPING_LILY),
									new ItemStack(bread)
							),
							new ElderSacrificePower(DHConfig.cost3, 20)
					),
					EnumSet.noneOf(RitualTraits.class),
					DHStructures.fishLake)
			;
		}
		if(DHConfig.soulCurse) {
			ElderRiteRegistry.addRecipe(id++, 5,
					"dh.rite.soul_curse",
					new CurseSoulRitual(),
					new ElderSacrificeMultiple(
							new ElderSacrificeItem(
									new ItemStack(DHItems.lightningInBag),
									Items.GENERIC.itemBrewOfWasting.createStack(),
									Items.GENERIC.itemBrewOfSoaring.createStack(),
									Items.GENERIC.itemWormyApple.createStack(),
									Items.GENERIC.itemBrewSoulAnguish.createStack(),
									Items.GENERIC.itemBrewGrave.createStack(),
									Items.GENERIC.itemOwletsWing.createStack()
							),
							new ElderSacrificePower(DHConfig.cost4, 20)
					),
					EnumSet.noneOf(RitualTraits.class),
					DHStructures.curse
			);
		}
		if(DHConfig.iceCastle) {
			ElderRiteRegistry.addRecipe(id++, 5,
					"dh.rite.castle",
					new IceFortressRite(8, 40.0f, 0),
					new ElderSacrificeMultiple(
							new ElderSacrificeItem(
									new ItemStack(Witchery.Blocks.PERPETUAL_ICE),
									new ItemStack(Blocks.packed_ice),
									new ItemStack(Blocks.ice),
									new ItemStack(Witchery.Blocks.STOCKADE_ICE),
									Items.GENERIC.itemAnnointingPaste.createStack(),
									Items.GENERIC.itemBrewOfIce.createStack()
							),
							new ElderSacrificePower(DHConfig.cost5, 20)
					),
					EnumSet.noneOf(RitualTraits.class),
					DHStructures.iceCastle
			);
			ElderRiteRegistry.addRecipe(id++, 5,
					"dh.rite.niceCream",
					new ElderSummonItem(
							new ItemStack(DHItems.niceCream, 4)
					),
					new ElderSacrificeMultiple(
							new ElderSacrificeItem(
									new ItemStack(Witchery.Blocks.PERPETUAL_ICE),
									new ItemStack(Blocks.packed_ice),
									new ItemStack(Blocks.ice),
									new ItemStack(Witchery.Blocks.STOCKADE_ICE),
									Items.GENERIC.itemAnnointingPaste.createStack(),
									Items.GENERIC.itemBrewOfIce.createStack(),
									Items.GENERIC.itemPurifiedMilk.createStack(),
									Items.GENERIC.itemFrozenHeart.createStack(),
									Items.GENERIC.itemSubduedSpiritVillage.createStack()
							),
							new ElderSacrificePower(DHConfig.cost5 * 2, 20)
					),
					EnumSet.noneOf(RitualTraits.class),
					DHStructures.iceCastle
			);
		}
		if(DHConfig.healMending) {
			ElderRiteRegistry.addRecipe(id++, 5,
					"dh.rite.mending",
					new RiteWithEffect(8, 80.0f, 0, "DHMending", System.currentTimeMillis() + 60 * 60 * 1000),
					new ElderSacrificeMultiple(
							new ElderSacrificeItem(
									Items.GENERIC.itemSoulOfTheWorld.createStack(),
									Items.GENERIC.itemBrewOfHollowTears.createStack(),
									Items.GENERIC.itemBrewOfErosion.createStack(),
									Items.GENERIC.itemMutandisExtremis.createStack(),
									Items.GENERIC.itemHeartOfGold.createStack(),
									Items.GENERIC.itemBloodWarm.createStack(),
									Items.GENERIC.itemKobolditeIngot.createStack(),
									new ItemStack(Items.POPPET, 1, 11)
							),
							new ElderSacrificePower(DHConfig.cost6, 20)
					),
					EnumSet.noneOf(RitualTraits.class),
					DHStructures.mending
			);
		}
		if(DHConfig.huntMagicCreatures) {
			ElderRiteRegistry.addRecipe(id++, 5,
					"dh.rite.hunt",
					new RiteWithEffect(8, 80.0f, 0, "DHHunt", System.currentTimeMillis() + 60 * 60 * 1000),
					new ElderSacrificeMultiple(
							new ElderSacrificeItem(
									new ItemStack(DHItems.lightningInBag),
									new ItemStack(Witchery.Blocks.ALLURING_SKULL),
									Items.GENERIC.itemNullCatalyst.createStack(),
									Items.GENERIC.itemWolfsbane.createStack(),
									Items.GENERIC.itemKobolditePentacle.createStack(),
									new ItemStack(Witchery.Blocks.GARLIC_GARLAND),
									new ItemStack(Witchery.Blocks.STOCKADE),
									new ItemStack(Items.SILVER_SWORD)
							),
							new ElderSacrificePower(DHConfig.cost7, 20)
					),
					EnumSet.noneOf(RitualTraits.class),
					DHStructures.hunt
			);
		}
		if(DHConfig.purify) {
			ElderSacrificeMultiple sacrifice = new ElderSacrificeMultiple(
					new ElderSacrificeItem(
							new ItemStack(golden_apple, 1, 1),
							Items.GENERIC.itemPurifiedMilk.createStack(),
							Items.GENERIC.itemDisturbedCotton.createStack(),
							Items.GENERIC.itemBrewOfLove.createStack(),
							Items.GENERIC.itemInfusionBase.createStack()
					)
			);
			if(DHIntegration.thaumcraft) {
				sacrifice.add(new ElderSacrificeItem(new ItemStack(ConfigItems.itemSanitySoap)));
			}
			ElderRiteRegistry.addRecipe(id++, 5,
					"dh.rite.purify",
					new PurifyRite(8, 40.0f, 0),
					new ElderSacrificeMultiple(
							sacrifice,
							DHIntegration.thaumcraft ? new ElderSacrificeItem(new ItemStack(ConfigItems.itemSanitySoap)) : null,
							new ElderSacrificePower(DHConfig.cost8, 20)
					),
					EnumSet.noneOf(RitualTraits.class),
					DHStructures.purify
			);
		}
		if(DHConfig.covenWitch) {
			ElderRiteRegistry.addRecipe(id++, 5,
					"dh.rite.coven",
					new ElderSummonCreature(EntityCovenWitch.class, false),
					new ElderSacrificeMultiple(
							new ElderSacrificeItem(
									Items.GENERIC.itemFrozenHeart.createStack(),
									Items.GENERIC.itemSpiritOfOtherwhere.createStack(),
									Items.GENERIC.itemBrewGrotesque.createStack(),
									new ItemStack(Items.WITCH_HAT),
									new ItemStack(Items.WITCH_HAND),
									new ItemStack(viscousSecretions)
							),
							new ElderSacrificeLiving(EntityWitch.class),
							new ElderSacrificePower(DHConfig.cost9, 20)
					),
					EnumSet.noneOf(RitualTraits.class),
					DHStructures.coven
			);
		}
	}

}
