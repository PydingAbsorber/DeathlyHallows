package com.pyding.deathlyhallows.rituals;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.entity.EntityBanshee;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.entity.EntityDeath;
import com.emoniph.witchery.entity.EntityGoblinGulg;
import com.emoniph.witchery.entity.EntityGoblinMog;
import com.emoniph.witchery.entity.EntityHornedHuntsman;
import com.emoniph.witchery.entity.EntityNightmare;
import com.emoniph.witchery.entity.EntityPoltergeist;
import com.emoniph.witchery.entity.EntitySpirit;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.blocks.DHBlocks;
import com.pyding.deathlyhallows.entities.EntityAbsoluteDeath;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.multiblocks.OctagonPart;
import com.pyding.deathlyhallows.multiblocks.structures.DHStructures;
import com.pyding.deathlyhallows.multiblocks.structures.StructureOctagonDrawing;
import com.pyding.deathlyhallows.rituals.rites.RiteAction;
import com.pyding.deathlyhallows.rituals.rites.RiteActionContinuously;
import com.pyding.deathlyhallows.rituals.rites.RiteBase;
import com.pyding.deathlyhallows.rituals.rites.RiteCurseSoul;
import com.pyding.deathlyhallows.rituals.rites.RiteSummonEntity;
import com.pyding.deathlyhallows.rituals.sacrifices.SacrificeBase;
import com.pyding.deathlyhallows.rituals.sacrifices.SacrificeItem;
import com.pyding.deathlyhallows.rituals.sacrifices.SacrificeLiving;
import com.pyding.deathlyhallows.rituals.sacrifices.SacrificeMultiple;
import com.pyding.deathlyhallows.rituals.sacrifices.SacrificePower;
import com.pyding.deathlyhallows.utils.DHConfig;
import com.pyding.deathlyhallows.utils.DHUtils;
import com.pyding.deathlyhallows.utils.ElfUtils;
import com.pyding.deathlyhallows.utils.IMultiBlockHandler;
import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.FishingHooks;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

import static com.emoniph.witchery.Witchery.Items;
import static com.pyding.deathlyhallows.rituals.DHRituals.Category.*;
import static net.minecraft.init.Items.bread;
import static net.minecraft.init.Items.feather;
import static net.minecraft.init.Items.fishing_rod;
import static net.minecraft.init.Items.golden_apple;

public class DHRituals {

	private static final EnumMap<Category, List<RitualBase>> rituals = new EnumMap<>(Category.class);

	public static void init() {
		DHStructures.init();
		clearRituals();
		int id = 1;
		addRecipe(id++, CIRCLE, 0,
				"dh.rite.elf",
				new RiteActionContinuously((rite, world, x, y, z, ticks) -> RiteAction.Action.forEachPlayerInBounds(p -> {
					ElfUtils.setElfLevel(p, 0);
					ParticleEffect.INSTANT_SPELL.send(SoundEffect.NOTE_PLING, p, 1.0, 2.0, 8);
					ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, p, 1.0, 2.0, 8);
					return false;
				}, world, x, y, z, 8), 40.0f, 0),
				new SacrificeMultiple(
						new SacrificeItem(
								Items.GENERIC.itemBrewOfWasting.createStack(),
								Items.GENERIC.itemBrewOfSoaring.createStack(),
								Items.GENERIC.itemWormyApple.createStack(),
								Items.GENERIC.itemBrewSoulAnguish.createStack(),
								Items.GENERIC.itemBrewGrave.createStack(),
								Items.GENERIC.itemOwletsWing.createStack()
						),
						new SacrificePower(12000.0F, 20)
				),
				RitualCondition::isNight,
				new StructureOctagonDrawing(
						new OctagonPart(DHBlocks.elderGlyph),
						new OctagonPart(Witchery.Blocks.GLYPH_OTHERWHERE, 3),
						new OctagonPart(Witchery.Blocks.GLYPH_OTHERWHERE, 5),
						new OctagonPart(Witchery.Blocks.GLYPH_OTHERWHERE, 7)
				)
		);
		addRecipe(id++, CIRCLE, 10,
				"dh.rite.death",
				new RiteSummonEntity(EntityAbsoluteDeath.class, false),
				new SacrificeMultiple(
						new SacrificeItem(
								Items.GENERIC.itemRefinedEvil.createStack(),
								Items.GENERIC.itemInfernalBlood.createStack(),
								new ItemStack(DHItems.gastronomicTemptation),
								Items.GENERIC.itemKobolditePentacle.createStack(),
								Items.GENERIC.itemBinkyHead.createStack(),
								Items.GENERIC.itemBrewOfInk.createStack()
						),
						new SacrificeLiving(EntityDeath.class),
						new SacrificeLiving(EntityZombie.class),
						new SacrificeLiving(EntitySkeleton.class),
						new SacrificeLiving(EntityEnderman.class),
						new SacrificeLiving(EntityNightmare.class),
						new SacrificeLiving(EntityGoblinGulg.class),
						new SacrificeLiving(EntityGoblinMog.class),
						new SacrificeLiving(EntityHornedHuntsman.class),
						new SacrificePower(21000.0F, 20)
				),
				RitualCondition::always,
				new StructureOctagonDrawing(
						new OctagonPart(DHBlocks.elderGlyph),
						new OctagonPart(Witchery.Blocks.GLYPH_RITUAL, 3),
						new OctagonPart(Witchery.Blocks.GLYPH_OTHERWHERE, 5),
						new OctagonPart(Witchery.Blocks.GLYPH_INFERNAL, 7)
				)
		);
		addRecipe(id++, CIRCLE, 20,
				"dh.rite.nimbus",
				new RiteAction((rite, world, x, y, z, ticks) -> {
					world.spawnEntityInWorld(new EntityLightningBolt(world, x + 0.5D, y, z + 0.5D));
					return RiteAction.Action.summonItem(new ItemStack(DHItems.nimbus), world, x, y, z);
				}),
				new SacrificeMultiple(
						new SacrificeItem(
								Items.GENERIC.itemBroomEnchanted.createStack(),
								Items.GENERIC.itemFlyingOintment.createStack(),
								Items.GENERIC.itemOwletsWing.createStack(),
								Items.GENERIC.itemBrewOfSoaring.createStack(),
								new ItemStack(feather)
						),
						new SacrificePower(15000.0F, 20)
				),
				RitualCondition::isRaining, // night? maybe rain?
				new StructureOctagonDrawing(
						new OctagonPart(DHBlocks.elderGlyph),
						new OctagonPart(Witchery.Blocks.GLYPH_RITUAL, 3),
						new OctagonPart(Witchery.Blocks.GLYPH_RITUAL, 5),
						new OctagonPart(Witchery.Blocks.GLYPH_RITUAL, 7)
				)
		);
		if(DHConfig.despairSonataCost > -1) {
			addRecipe(id++, SONATA, 0,
					"dh.rite.sonata",
					new RiteActionContinuously((rite, world, x, y, z, ticks) -> {
						if(world.rand.nextDouble() < 0.05) {
							String randomEntityName = DHUtils.getEntitiesNames().get(world.rand.nextInt(DHUtils.getEntitiesNames().size() - 1));
							int tries = 0;
							while(DHUtils.contains(DHConfig.despairSonataBlackList, randomEntityName) && tries <= 1000) {
								randomEntityName = DHUtils.getEntitiesNames().get(world.rand.nextInt(DHUtils.getEntitiesNames().size() - 1));
								tries++;
							}
							if(tries <= 1000) {
								return RitualStep.Result.ABORTED_REFUND;
							}
							Entity entity = EntityList.createEntityByName(randomEntityName, world);
							world.spawnEntityInWorld(entity);
						}
						else {
							Class<? extends EntityCreature> entityClass;
							for(int i = 0; i < DHConfig.randomSpirits; i++) {
								switch(world.rand.nextInt(4)) {
									case 0:
										entityClass = EntityBanshee.class;
										break;
									case 1:
										entityClass = EntitySpirit.class;
										break;
									case 2:
										entityClass = EntityNightmare.class;
										break;
									case 3:
										entityClass = EntityPoltergeist.class;
										break;
									default:
										entityClass = null;
								}
								DHUtils.spawnEntity(world, x, y + 1, z, entityClass);
							}
						}
						ParticleEffect.INSTANT_SPELL.send(SoundEffect.WITCHERY_MOB_BABA_LIVING, world, x + 0.5D, y, z + 0.5D, 1.0, 2.0, 8);
						ParticleEffect.INSTANT_SPELL.send(SoundEffect.WITCHERY_MOB_IMP_LAUGH, world, x + 0.5D, y, z + 0.5D, 1.0, 2.0, 8);
						return RitualStep.Result.COMPLETED;
					}, 40.0f, 0),
					new SacrificeMultiple(
							new SacrificeItem(
									Items.GENERIC.itemCongealedSpirit.createStack(),
									Items.GENERIC.itemGraveyardDust.createStack(),
									Items.GENERIC.itemWormwood.createStack(),
									Items.GENERIC.itemDisturbedCotton.createStack(),
									new ItemStack(Witchery.Blocks.WISPY_COTTON),
									Items.GENERIC.itemAttunedStoneCharged.createStack(),
									Items.GENERIC.itemBatWool.createStack()
							),
							new SacrificePower(DHConfig.despairSonataCost, 20)
					),
					RitualCondition::always,
					DHStructures.sonata
			);
		}
		if(DHConfig.fishCatchCost > -1) {
			addRecipe(id++, LAKE, 0,
					"dh.rite.catch",
					new RiteActionContinuously((rite, world, x, y, z, ticks) -> {
						for(int i = 0; i < 20; ++i) {
							RiteAction.Action.summonItem(FishingHooks.getRandomFishable(world.rand, 10, 99, 10), world, x, y, z);
						}
						return RitualStep.Result.UPKEEP;
					},
							40.0f, 0
					),
					new SacrificeMultiple(
							new SacrificeItem(
									new ItemStack(fishing_rod),
									Items.GENERIC.itemWormyApple.createStack(),
									new ItemStack(Witchery.Blocks.LEAPING_LILY),
									new ItemStack(bread)
							),
							new SacrificePower(DHConfig.fishCatchCost, 20)
					),
					RitualCondition::always,
					DHStructures.fishLake)
			;
		}
		if(DHConfig.soulCurseCost > -1) {
			addRecipe(id++, CURSE, 0,
					"dh.rite.soul_curse",
					new RiteCurseSoul(),
					new SacrificeMultiple(
							new SacrificeItem(
									new ItemStack(DHItems.lightningInBag),
									Items.GENERIC.itemBrewOfWasting.createStack(),
									Items.GENERIC.itemBrewOfSoaring.createStack(),
									Items.GENERIC.itemWormyApple.createStack(),
									Items.GENERIC.itemBrewSoulAnguish.createStack(),
									Items.GENERIC.itemBrewGrave.createStack(),
									Items.GENERIC.itemOwletsWing.createStack()
							),
							new SacrificePower(DHConfig.soulCurseCost, 20)
					),
					RitualCondition::always,
					DHStructures.curse
			);
		}
		if(DHConfig.iceCastleCost > -1) {
			addRecipe(id++, ICECASTLE, 0,
					"dh.rite.castle",
					new RiteAction(
							(rite, world, x, y, z, ticks) -> {
								Random random = new Random();
								List<Item> hearts = new ArrayList<>();
								for(Item item: DHUtils.getItems()) {
									if(item.getUnlocalizedName().contains("heart")) {
										hearts.add(item);
									}
								}
								if(hearts.size() <= 1) {
									return RitualStep.Result.ABORTED;
								}
								return RiteAction.Action.summonItem(new ItemStack(hearts.get(random.nextInt(hearts.size()))), world, x, y, z);
							}),
					new SacrificeMultiple(
							new SacrificeItem(
									new ItemStack(Witchery.Blocks.PERPETUAL_ICE),
									new ItemStack(Blocks.packed_ice),
									new ItemStack(Blocks.ice),
									new ItemStack(Witchery.Blocks.STOCKADE_ICE),
									Items.GENERIC.itemAnnointingPaste.createStack(),
									Items.GENERIC.itemBrewOfIce.createStack()
							),
							new SacrificePower(DHConfig.iceCastleCost, 20)
					),
					RitualCondition::always,
					DHStructures.iceCastle
			);
			addRecipe(id++, ICECASTLE, 10,
					"dh.rite.niceCream",
					new RiteAction((rite, world, x, y, z, ticks) -> RiteAction.Action.summonItem(new ItemStack(DHItems.niceCream, 4), world, x, y, z)),
					new SacrificeMultiple(
							new SacrificeItem(
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
							new SacrificePower(DHConfig.iceCastleCost * 2, 20)
					),
					RitualCondition::always,
					DHStructures.iceCastle
			);
		}

		if(DHIntegration.thaumcraft && DHConfig.bathHouseCost > -1) {
			addRecipe(id++, INTEGRATION, 0,
					"dh.rite.banka",
					new RiteActionContinuously((rite, world, x, y, z, ticks) -> RiteAction.Action.forEachPlayerInBounds(p -> {
						DeathlyProperties.get(p).setBanka(System.currentTimeMillis() + 60 * 60 * 1000);
						ParticleEffect.INSTANT_SPELL.send(SoundEffect.WITCHERY_MOB_BABA_LIVING, p, 1.0, 2.0, 8);
						ParticleEffect.INSTANT_SPELL.send(SoundEffect.WITCHERY_MOB_IMP_LAUGH, p, 1.0, 2.0, 8);
						return false;
					}, world, x, y, z, 8), 80F, 0),
					new SacrificeMultiple(
							new SacrificeItem(
									new ItemStack(DHItems.lightningInBag),
									new ItemStack(ConfigItems.itemBucketDeath),
									new ItemStack(ConfigItems.itemBottleTaint),
									new ItemStack(ConfigItems.itemSanitySoap),
									new ItemStack(Witchery.Blocks.VOID_BRAMBLE),
									new ItemStack(DHItems.gastronomicTemptation),
									new ItemStack(DHItems.viscousSecretions)
							),
							new SacrificePower(DHConfig.bathHouseCost, 20)),
					RitualCondition::always,
					DHStructures.basik
			);
		}
		if(DHConfig.huntMagicCreaturesCost > -1) {
			addRecipe(id++, HUNT, 0,
					"dh.rite.hunt",
					new RiteActionContinuously((rite, world, x, y, z, ticks) -> RiteAction.Action.forEachPlayerInBounds(p -> {
						DeathlyProperties.get(p).setHunt(System.currentTimeMillis() + 60 * 60 * 1000);
						ParticleEffect.INSTANT_SPELL.send(SoundEffect.WITCHERY_MOB_BABA_LIVING, p, 1.0, 2.0, 8);
						ParticleEffect.INSTANT_SPELL.send(SoundEffect.WITCHERY_MOB_IMP_LAUGH, p, 1.0, 2.0, 8);
						return false;
					}, world, x, y, z, 8), 80F, 0),
					new SacrificeMultiple(
							new SacrificeItem(
									new ItemStack(DHItems.lightningInBag),
									new ItemStack(Witchery.Blocks.ALLURING_SKULL),
									Items.GENERIC.itemNullCatalyst.createStack(),
									Items.GENERIC.itemWolfsbane.createStack(),
									Items.GENERIC.itemKobolditePentacle.createStack(),
									new ItemStack(Witchery.Blocks.GARLIC_GARLAND),
									new ItemStack(Witchery.Blocks.STOCKADE),
									new ItemStack(Items.SILVER_SWORD)
							),
							new SacrificePower(DHConfig.huntMagicCreaturesCost, 20)
					),
					RitualCondition::always,
					DHStructures.hunt
			);
		}
		if(DHConfig.healMendingCost > -1) {
			addRecipe(id++, MENDING, 0,
					"dh.rite.mending",
					new RiteActionContinuously((rite, world, x, y, z, ticks) -> RiteAction.Action.forEachPlayerInBounds(p -> {
						DeathlyProperties.get(p).setHeal(System.currentTimeMillis() + 60 * 60 * 1000);
						ParticleEffect.INSTANT_SPELL.send(SoundEffect.WITCHERY_MOB_BABA_LIVING, p, 1.0, 2.0, 8);
						ParticleEffect.INSTANT_SPELL.send(SoundEffect.WITCHERY_MOB_IMP_LAUGH, p, 1.0, 2.0, 8);
						return false;
					}, world, x, y, z, 8), 80F, 0),
					new SacrificeMultiple(
							new SacrificeItem(
									Items.GENERIC.itemSoulOfTheWorld.createStack(),
									Items.GENERIC.itemBrewOfHollowTears.createStack(),
									Items.GENERIC.itemBrewOfErosion.createStack(),
									Items.GENERIC.itemMutandisExtremis.createStack(),
									Items.GENERIC.itemHeartOfGold.createStack(),
									Items.GENERIC.itemBloodWarm.createStack(),
									Items.GENERIC.itemKobolditeIngot.createStack(),
									new ItemStack(Items.POPPET, 1, 11)
							),
							new SacrificePower(DHConfig.healMendingCost, 20)
					),
					RitualCondition::always,
					DHStructures.mending
			);
		}
		if(DHConfig.purifyCost > -1) {
			SacrificeMultiple sacrifice = new SacrificeMultiple(
					new SacrificeItem(
							new ItemStack(golden_apple, 1, 1),
							Items.GENERIC.itemPurifiedMilk.createStack(),
							Items.GENERIC.itemDisturbedCotton.createStack(),
							Items.GENERIC.itemBrewOfLove.createStack(),
							Items.GENERIC.itemInfusionBase.createStack()
					)
			);
			if(DHIntegration.thaumcraft) {
				sacrifice.add(new SacrificeItem(new ItemStack(ConfigItems.itemSanitySoap)));
			}
			addRecipe(id++, PURIFY, 0,
					"dh.rite.purify",
					new RiteActionContinuously((rite, world, x, y, z, ticks) -> RiteAction.Action.forEachPlayerInBounds(p -> {
						NBTTagCompound tag = p.getEntityData();
						tag.setInteger("DHMagicAvenger", 0);
						DeathlyProperties props = DeathlyProperties.get(p);
						props.setAvenger(false);
						tag.setInteger("witcheryCursed", 0);
						tag.setInteger("witcheryInsanity", 0);
						tag.setInteger("witcherySinking", 0);
						tag.setInteger("witcheryOverheating", 0);
						tag.setInteger("witcheryWakingNightmare", 0);
						if(DHIntegration.thaumcraft) {
							Thaumcraft.proxy.playerKnowledge.setWarpPerm(p.getCommandSenderName(), 0);
							Thaumcraft.proxy.playerKnowledge.setWarpTemp(p.getCommandSenderName(), 0);
							Thaumcraft.proxy.playerKnowledge.setWarpSticky(p.getCommandSenderName(), 0);
						}
						ParticleEffect.INSTANT_SPELL.send(SoundEffect.WITCHERY_MOB_BABA_LIVING, p, 1.0, 2.0, 8);
						ParticleEffect.INSTANT_SPELL.send(SoundEffect.WITCHERY_MOB_IMP_LAUGH, p, 1.0, 2.0, 8);
						return false;
					}, world, x, y, z, 8),
							40.0f, 0),
					new SacrificeMultiple(
							sacrifice,
							new SacrificePower(DHConfig.purifyCost, 20)
					),
					RitualCondition::always,
					DHStructures.purify
			);
		}
		if(DHConfig.covenWitchCost > -1) {
			addRecipe(id++, COVEN, 0,
					"dh.rite.coven",
					new RiteSummonEntity(EntityCovenWitch.class, false),
					new SacrificeMultiple(
							new SacrificeItem(
									Items.GENERIC.itemFrozenHeart.createStack(),
									Items.GENERIC.itemSpiritOfOtherwhere.createStack(),
									Items.GENERIC.itemBrewGrotesque.createStack(),
									new ItemStack(Items.WITCH_HAT),
									new ItemStack(Items.WITCH_HAND),
									new ItemStack(DHItems.viscousSecretions)
							),
							new SacrificeLiving(EntityWitch.class),
							new SacrificePower(DHConfig.covenWitchCost, 20)
					),
					RitualCondition::always,
					DHStructures.coven
			);
		}
	}

	public enum Category {

		CIRCLE, // it's called circle but it's actually all octagons
		SONATA,
		LAKE,
		CURSE,
		ICECASTLE,
		MENDING,
		HUNT,
		COVEN,
		PURIFY,
		INTEGRATION

	}

	public static void clearRituals() {
		rituals.clear();
	}

	public static List<RitualBase> getRituals(Category category) {
		if(!rituals.containsKey(category)) {
			rituals.put(category, new ArrayList<>());
		}
		return rituals.get(category);
	}

	public static void addRecipe(int ritualID, Category category, int bookIndex, String name, RiteBase rite, SacrificeBase initialSacrifice, RitualCondition condition, IMultiBlockHandler mb) {
		RitualBase ritual = new RitualBase(ritualID, bookIndex, rite, initialSacrifice, condition, mb);
		ritual.setUnlocalizedName(name);
		getRituals(category).add(ritual);
	}

	public static RitualBase getRitual(int ritualID) {
		for(Category category: Category.values()) {
			for(RitualBase ritual: getRituals(category)) {
				if(ritual.ritualID == ritualID) {
					return ritual;
				}
			}
		}
		return null;
	}

	public static List<RitualBase> getSortedRituals() {
		ArrayList<RitualBase> sortedRituals = new ArrayList<>();
		for(Category category: Category.values()) {
			ArrayList<RitualBase> l = new ArrayList<>(getRituals(category));
			l.sort(Comparator.comparingInt(r -> r.bookIndex));
			sortedRituals.addAll(l);
		}
		return sortedRituals;
	}

	@FunctionalInterface
	public interface RitualCondition {

		boolean test(World world, int x, int y, int z);

		static boolean always(World world, int x, int y, int z) {
			return true;
		}

		static boolean isRaining(World world, int x, int y, int z) {
			return world.isRaining() && world.getBiomeGenForCoords(x, y).canSpawnLightningBolt();
		}

		static boolean isThundering(World world, int x, int y, int z) {
			return world.isThundering();
		}

		static boolean isDay(World world, int x, int y, int z) {
			return world.isDaytime();
		}

		static boolean isNight(World world, int x, int y, int z) {
			return !world.isDaytime();
		}

		static boolean isOverworld(World world, int x, int y, int z) {
			return world.provider.dimensionId == 0;
		}

		static boolean isDream(World world, int x, int y, int z) {
			return world.provider instanceof WorldProviderDreamWorld;
		}

	}

}
