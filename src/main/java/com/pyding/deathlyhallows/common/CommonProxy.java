package com.pyding.deathlyhallows.common;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.entity.EntityDeath;
import com.emoniph.witchery.entity.EntityGoblinGulg;
import com.emoniph.witchery.entity.EntityGoblinMog;
import com.emoniph.witchery.entity.EntityHornedHuntsman;
import com.emoniph.witchery.entity.EntityNightmare;
import com.emoniph.witchery.ritual.RitualTraits;
import com.emoniph.witchery.ritual.rites.RiteSummonItem;
import com.pyding.deathlyhallows.blocks.ElderRitualBlock;
import com.pyding.deathlyhallows.client.gui.GuiRegister;
import com.pyding.deathlyhallows.common.handler.ConfigHandler;
import com.pyding.deathlyhallows.entity.AbsoluteDeath;
import com.pyding.deathlyhallows.integration.Integration;
import com.pyding.deathlyhallows.items.ModItems;
import com.pyding.deathlyhallows.rituals.Circle;
import com.pyding.deathlyhallows.rituals.ElderRiteRegistry;
import com.pyding.deathlyhallows.rituals.Figure;
import com.pyding.deathlyhallows.rituals.rites.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import thaumcraft.common.config.ConfigItems;

import java.util.EnumSet;

import static com.pyding.deathlyhallows.items.ModItems.*;
import static com.pyding.deathlyhallows.rituals.Figures.*;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
	}

	public void init(FMLInitializationEvent event) {
		ModItems.init();
		GameRegistry.registerTileEntity(ElderRitualBlock.TileEntityCircle.class, "elder_ritual_block_tile");
	}

	public void postInit(FMLPostInitializationEvent event) {
		GuiRegister.init();
		int id = 111;
		if(Integration.thaumcraft && ConfigHandler.bathHouse)
			ElderRiteRegistry.addRecipe(id++, 5, new RiteWithEffect(8, 80.0f, 0,"DHBanka",System.currentTimeMillis()+60*60*1000), new ElderSacrificeMultiple(new ElderSacrificeItem(new ItemStack(ConfigItems.itemBucketDeath), new ItemStack(ConfigItems.itemBottleTaint), new ItemStack(ConfigItems.itemSanitySoap), new ItemStack(Witchery.Blocks.VOID_BRAMBLE), new ItemStack(gastronomicTemptation), new ItemStack(viscousSecretions)), new ElderSacrificePower(ConfigHandler.cost1, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(basik))
						 .setUnlocalizedName("dh.rite.banka");
		if(ConfigHandler.despairSonata)
			ElderRiteRegistry.addRecipe(id++, 5, new SummonSpiritRite(8, 40.0f, 0), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemCongealedSpirit.createStack(), Witchery.Items.GENERIC.itemGraveyardDust.createStack(), Witchery.Items.GENERIC.itemWormwood.createStack(), Witchery.Items.GENERIC.itemDisturbedCotton.createStack(), new ItemStack(Witchery.Blocks.WISPY_COTTON), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack(), Witchery.Items.GENERIC.itemBatWool.createStack()), new ElderSacrificePower(ConfigHandler.cost2, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(sonata))
						 .setUnlocalizedName("dh.rite.sonata");
		if(ConfigHandler.fishCatch)
			ElderRiteRegistry.addRecipe(id++, 5, new FishCatchRite(8, 40.0f, 0), new ElderSacrificeMultiple(new ElderSacrificeItem(new ItemStack(Items.fishing_rod), Witchery.Items.GENERIC.itemWormyApple.createStack(), new ItemStack(Witchery.Blocks.LEAPING_LILY), new ItemStack(Items.bread)), new ElderSacrificePower(ConfigHandler.cost3, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(fishLake))
						 .setUnlocalizedName("dh.rite.catch");
		if(ConfigHandler.soulCurse)
			ElderRiteRegistry.addRecipe(id++, 5,new CurseSoulRitual(), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemBrewOfWasting.createStack(), Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), Witchery.Items.GENERIC.itemWormyApple.createStack(), Witchery.Items.GENERIC.itemBrewSoulAnguish.createStack(), Witchery.Items.GENERIC.itemBrewGrave.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack()), new ElderSacrificePower(ConfigHandler.cost4, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(curse))
						 .setUnlocalizedName("dh.rite.soul_curse"); 
		if(ConfigHandler.iceCastle)
			ElderRiteRegistry.addRecipe(id++, 5, new IceFortressRite(8, 40.0f, 0), new ElderSacrificeMultiple(new ElderSacrificeItem(new ItemStack(Witchery.Blocks.PERPETUAL_ICE), new ItemStack(Blocks.packed_ice), new ItemStack(Blocks.ice), new ItemStack(Witchery.Blocks.STOCKADE_ICE), Witchery.Items.GENERIC.itemAnnointingPaste.createStack(), Witchery.Items.GENERIC.itemBrewOfIce.createStack()), new ElderSacrificePower(ConfigHandler.cost5, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(iceCastle))
						 .setUnlocalizedName("dh.rite.castle");
		if(ConfigHandler.healMending)
			ElderRiteRegistry.addRecipe(id++, 5, new RiteWithEffect(8, 80.0f, 0,"DHMending",System.currentTimeMillis()+60*60*1000), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemSoulOfTheWorld.createStack(), Witchery.Items.GENERIC.itemBrewOfHollowTears.createStack(), Witchery.Items.GENERIC.itemBrewOfErosion.createStack(), Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), Witchery.Items.GENERIC.itemHeartOfGold.createStack(), Witchery.Items.GENERIC.itemBloodWarm.createStack(), Witchery.Items.GENERIC.itemKobolditeIngot.createStack(), new ItemStack(Witchery.Items.POPPET,1,11)), new ElderSacrificePower(ConfigHandler.cost6, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(mending))
						 .setUnlocalizedName("dh.rite.mending");
		if(ConfigHandler.huntMagicCreatures)
			ElderRiteRegistry.addRecipe(id++, 5, new RiteWithEffect(8, 80.0f, 0,"DHHunt",System.currentTimeMillis()+60*60*1000), new ElderSacrificeMultiple(new ElderSacrificeItem(new ItemStack(Witchery.Blocks.ALLURING_SKULL), Witchery.Items.GENERIC.itemNullCatalyst.createStack(), Witchery.Items.GENERIC.itemWolfsbane.createStack(), Witchery.Items.GENERIC.itemKobolditePentacle.createStack(), new ItemStack(Witchery.Blocks.GARLIC_GARLAND), new ItemStack(Witchery.Blocks.STOCKADE),new ItemStack(Witchery.Items.SILVER_SWORD)), new ElderSacrificePower(ConfigHandler.cost7, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(hunt))
						 .setUnlocalizedName("dh.rite.hunt");
		if(ConfigHandler.purify) {
			ElderSacrificeMultiple sacrifice;
			if(Integration.thaumcraft)
				sacrifice = new ElderSacrificeMultiple(new ElderSacrificeItem(new ItemStack(Items.golden_apple,1,1),Witchery.Items.GENERIC.itemPurifiedMilk.createStack(),Witchery.Items.GENERIC.itemDisturbedCotton.createStack(),Witchery.Items.GENERIC.itemBrewOfLove.createStack(),Witchery.Items.GENERIC.itemInfusionBase.createStack(),new ItemStack(ConfigItems.itemSanitySoap)));
			else sacrifice = new ElderSacrificeMultiple(new ElderSacrificeItem(new ItemStack(Items.golden_apple,1,1),Witchery.Items.GENERIC.itemPurifiedMilk.createStack(),Witchery.Items.GENERIC.itemDisturbedCotton.createStack(),Witchery.Items.GENERIC.itemBrewOfLove.createStack(),Witchery.Items.GENERIC.itemInfusionBase.createStack()));
			ElderRiteRegistry.addRecipe(id++, 5, new PurifyRite(8, 40.0f, 0), new ElderSacrificeMultiple(sacrifice, new ElderSacrificePower(ConfigHandler.cost8, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(purify))
							 .setUnlocalizedName("dh.rite.purify");
		}
		if(ConfigHandler.covenWitch)
			ElderRiteRegistry.addRecipe(id++, 5, new ElderSummonCreature(EntityCovenWitch.class,false), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemFrozenHeart.createStack(), Witchery.Items.GENERIC.itemSpiritOfOtherwhere.createStack(), Witchery.Items.GENERIC.itemBrewGrotesque.createStack(), new ItemStack(Witchery.Items.WITCH_HAT), new ItemStack(Witchery.Items.WITCH_HAND), new ItemStack(viscousSecretions)), new ElderSacrificeLiving(EntityWitch.class), new ElderSacrificePower(ConfigHandler.cost9, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(coven))
						 .setUnlocalizedName("dh.rite.coven");
		
		ElderRiteRegistry.addRecipe(id++, 26, new ElderSummonCreature(AbsoluteDeath.class, false), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemRefinedEvil.createStack(), Witchery.Items.GENERIC.itemInfernalBlood.createStack(), new ItemStack(gastronomicTemptation), Witchery.Items.GENERIC.itemKobolditePentacle.createStack(), Witchery.Items.GENERIC.itemBinkyHead.createStack(), Witchery.Items.GENERIC.itemBrewOfInk.createStack()), new ElderSacrificeLiving(EntityDeath.class), new ElderSacrificeLiving(EntityZombie.class), new ElderSacrificeLiving(EntitySkeleton.class), new ElderSacrificeLiving(EntityEnderman.class), new ElderSacrificeLiving(EntityNightmare.class), new ElderSacrificeLiving(EntityGoblinGulg.class), new ElderSacrificeLiving(EntityGoblinMog.class), new ElderSacrificeLiving(EntityHornedHuntsman.class), new ElderSacrificePower(21000.0F, 20)), EnumSet.noneOf(RitualTraits.class), new Circle(0, 0, 40), new Circle(0, 28, 0), new Circle(16, 0, 0))
						 .setUnlocalizedName("dh.rite.death");
		ElderRiteRegistry.addRecipe(id++, 35, new ElderSummonItem(new ItemStack(nimbus), ElderSummonItem.Binding.NONE), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemBroomEnchanted.createStack(), Witchery.Items.GENERIC.itemFlyingOintment.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack(), Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), new ItemStack(Items.feather)), new ElderSacrificePower(15000.0F, 20)), EnumSet.of(RitualTraits.ONLY_AT_NIGHT), new Circle(16, 0, 0), new Circle(28, 0, 0), new Circle(40, 0, 0))
						 .setUnlocalizedName("dh.rite.nimbus");
		ElderRiteRegistry.addRecipe(id++, 5, new RiteOfElvenVanishing(8, 40.0f, 0), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemBrewOfWasting.createStack(), Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), Witchery.Items.GENERIC.itemWormyApple.createStack(), Witchery.Items.GENERIC.itemBrewSoulAnguish.createStack(), Witchery.Items.GENERIC.itemBrewGrave.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack()), new ElderSacrificePower(20000.0F, 20)), EnumSet.of(RitualTraits.ONLY_AT_NIGHT), new Circle(0, 16, 0), new Circle(0, 28, 0), new Circle(0, 40, 0))
						 .setUnlocalizedName("dh.rite.elf");
	}
}
