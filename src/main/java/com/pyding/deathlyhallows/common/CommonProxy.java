package com.pyding.deathlyhallows.common;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.ritual.RitualTraits;
import com.emoniph.witchery.ritual.SacrificeLiving;
import com.emoniph.witchery.ritual.rites.RiteSummonCreature;
import com.pyding.deathlyhallows.blocks.ElderRitualBlock;
import com.pyding.deathlyhallows.client.gui.GuiRegister;
import com.pyding.deathlyhallows.integration.Integration;
import com.pyding.deathlyhallows.items.ModItems;
import com.pyding.deathlyhallows.rituals.ElderRiteRegistry;
import com.pyding.deathlyhallows.rituals.Figure;
import com.pyding.deathlyhallows.rituals.rites.CurseSoulRitual;
import com.pyding.deathlyhallows.rituals.rites.ElderSacrificeItem;
import com.pyding.deathlyhallows.rituals.rites.ElderSacrificeLiving;
import com.pyding.deathlyhallows.rituals.rites.ElderSacrificeMultiple;
import com.pyding.deathlyhallows.rituals.rites.ElderSacrificePower;
import com.pyding.deathlyhallows.rituals.rites.FishCatchRite;
import com.pyding.deathlyhallows.rituals.rites.IceFortressRite;
import com.pyding.deathlyhallows.rituals.rites.PurifyRite;
import com.pyding.deathlyhallows.rituals.rites.RiteWithEffect;
import com.pyding.deathlyhallows.rituals.rites.SummonSpiritRite;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.item.ItemStack;
import thaumcraft.common.config.ConfigItems;

import java.util.EnumSet;

import static com.pyding.deathlyhallows.items.ModItems.gastronomicTemptation;
import static com.pyding.deathlyhallows.items.ModItems.viscousSecretions;
import static com.pyding.deathlyhallows.rituals.Figures.basik;
import static com.pyding.deathlyhallows.rituals.Figures.fishLake;
import static com.pyding.deathlyhallows.rituals.Figures.iceCastle;
import static com.pyding.deathlyhallows.rituals.Figures.test;

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
		ElderRiteRegistry.addRecipe(id++, 5, new RiteWithEffect(8, 80.0f, 0,"DHBanka",System.currentTimeMillis()+60*60*1000), new ElderSacrificeMultiple(new ElderSacrificeItem(new ItemStack(ConfigItems.itemSanitySoap)), new ElderSacrificePower(1000.0F, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(test))
						 .setUnlocalizedName("dh.rite.test");
		if(Integration.thaumcraft)
			ElderRiteRegistry.addRecipe(id++, 5, new RiteWithEffect(8, 80.0f, 0,"DHBanka",System.currentTimeMillis()+60*60*1000), new ElderSacrificeMultiple(new ElderSacrificeItem(new ItemStack(ConfigItems.itemBucketDeath), new ItemStack(ConfigItems.itemBottleTaint), new ItemStack(ConfigItems.itemSanitySoap), new ItemStack(Witchery.Blocks.VOID_BRAMBLE), new ItemStack(gastronomicTemptation), new ItemStack(viscousSecretions)), new ElderSacrificePower(30000.0F, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(basik))
						 .setUnlocalizedName("dh.rite.banka");
		ElderRiteRegistry.addRecipe(id++, 5, new SummonSpiritRite(8, 40.0f, 0), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemBrewOfWasting.createStack(), Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), Witchery.Items.GENERIC.itemWormyApple.createStack(), Witchery.Items.GENERIC.itemBrewSoulAnguish.createStack(), Witchery.Items.GENERIC.itemBrewGrave.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack()), new ElderSacrificePower(5000.0F, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(basik))
						 .setUnlocalizedName("dh.rite.sonata");
		ElderRiteRegistry.addRecipe(id++, 5, new FishCatchRite(8, 40.0f, 0), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemBrewOfWasting.createStack(), Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), Witchery.Items.GENERIC.itemWormyApple.createStack(), Witchery.Items.GENERIC.itemBrewSoulAnguish.createStack(), Witchery.Items.GENERIC.itemBrewGrave.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack()), new ElderSacrificePower(12000.0F, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(fishLake))
						 .setUnlocalizedName("dh.rite.catch");
		ElderRiteRegistry.addRecipe(id++, 5,new CurseSoulRitual(), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemBrewOfWasting.createStack(), Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), Witchery.Items.GENERIC.itemWormyApple.createStack(), Witchery.Items.GENERIC.itemBrewSoulAnguish.createStack(), Witchery.Items.GENERIC.itemBrewGrave.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack()), new ElderSacrificePower(50000.0F, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(basik))
						 .setUnlocalizedName("dh.rite.soul_curse");
		ElderRiteRegistry.addRecipe(id++, 5, new IceFortressRite(8, 40.0f, 0), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemBrewOfWasting.createStack(), Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), Witchery.Items.GENERIC.itemWormyApple.createStack(), Witchery.Items.GENERIC.itemBrewSoulAnguish.createStack(), Witchery.Items.GENERIC.itemBrewGrave.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack()), new ElderSacrificePower(7000.0F, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(iceCastle))
						 .setUnlocalizedName("dh.rite.castle");
		ElderRiteRegistry.addRecipe(id++, 5, new RiteWithEffect(8, 80.0f, 0,"DHMending",System.currentTimeMillis()+60*60*1000), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemBrewOfWasting.createStack(), Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), Witchery.Items.GENERIC.itemWormyApple.createStack(), Witchery.Items.GENERIC.itemBrewSoulAnguish.createStack(), Witchery.Items.GENERIC.itemBrewGrave.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack()), new ElderSacrificePower(18000.0F, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(basik))
						 .setUnlocalizedName("dh.rite.mending");
		ElderRiteRegistry.addRecipe(id++, 5, new RiteWithEffect(8, 80.0f, 0,"DHHunt",System.currentTimeMillis()+60*60*1000), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemBrewOfWasting.createStack(), Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), Witchery.Items.GENERIC.itemWormyApple.createStack(), Witchery.Items.GENERIC.itemBrewSoulAnguish.createStack(), Witchery.Items.GENERIC.itemBrewGrave.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack()), new ElderSacrificePower(35000.0F, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(basik))
						 .setUnlocalizedName("dh.rite.hunt");
		ElderRiteRegistry.addRecipe(id++, 5, new PurifyRite(8, 40.0f, 0), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemBrewOfWasting.createStack(), Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), Witchery.Items.GENERIC.itemWormyApple.createStack(), Witchery.Items.GENERIC.itemBrewSoulAnguish.createStack(), Witchery.Items.GENERIC.itemBrewGrave.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack()), new ElderSacrificePower(30000.0F, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(basik))
						 .setUnlocalizedName("dh.rite.purify");
		/*ElderRiteRegistry.addRecipe(id++, 5, new RiteSummonCreature(EntityCovenWitch.class,false), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemBrewOfWasting.createStack(), Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), Witchery.Items.GENERIC.itemWormyApple.createStack(), Witchery.Items.GENERIC.itemBrewSoulAnguish.createStack(), Witchery.Items.GENERIC.itemBrewGrave.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack()), new ElderSacrificeLiving(EntityWitch.class), new ElderSacrificePower(5000.0F, 20)), EnumSet.noneOf(RitualTraits.class), new Figure(basik))
						 .setUnlocalizedName("dh.rite.coven");*/
		
		/*ElderRiteRegistry.addRecipe(id++, 26, new RiteSummonCreature(AbsoluteDeath.class, false), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemRefinedEvil.createStack(), Witchery.Items.GENERIC.itemInfernalBlood.createStack(), new ItemStack(gastronomicTemptation), Witchery.Items.GENERIC.itemKobolditePentacle.createStack(), Witchery.Items.GENERIC.itemBinkyHead.createStack(), Witchery.Items.GENERIC.itemBrewOfInk.createStack()), new SacrificeLiving(EntityDeath.class), new SacrificeLiving(EntityZombie.class), new SacrificeLiving(EntitySkeleton.class), new SacrificeLiving(EntityEnderman.class), new SacrificeLiving(EntityNightmare.class), new SacrificeLiving(EntityGoblinGulg.class), new SacrificeLiving(EntityGoblinMog.class), new SacrificeLiving(EntityHornedHuntsman.class), new ElderSacrificePower(21000.0F, 20)), EnumSet.noneOf(RitualTraits.class), new Circle(0, 0, 40), new Circle(0, 28, 0), new Circle(16, 0, 0))
						 .setUnlocalizedName("dh.rite.death");
		ElderRiteRegistry.addRecipe(id++, 35, new RiteSummonItem(new ItemStack(nimbus), RiteSummonItem.Binding.NONE), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemBroomEnchanted.createStack(), Witchery.Items.GENERIC.itemFlyingOintment.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack(), Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), new ItemStack(Items.feather)), new ElderSacrificePower(15000.0F, 20)), EnumSet.of(RitualTraits.ONLY_AT_NIGHT), new Circle(16, 0, 0), new Circle(28, 0, 0), new Circle(40, 0, 0))
						 .setUnlocalizedName("dh.rite.nimbus");
		ElderRiteRegistry.addRecipe(id++, 5, new RiteOfElvenVanishing(8, 40.0f, 0), new ElderSacrificeMultiple(new ElderSacrificeItem(Witchery.Items.GENERIC.itemBrewOfWasting.createStack(), Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), Witchery.Items.GENERIC.itemWormyApple.createStack(), Witchery.Items.GENERIC.itemBrewSoulAnguish.createStack(), Witchery.Items.GENERIC.itemBrewGrave.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack()), new ElderSacrificePower(20000.0F, 20)), EnumSet.of(RitualTraits.ONLY_AT_NIGHT), new Circle(0, 16, 0), new Circle(0, 28, 0), new Circle(0, 40, 0))
						 .setUnlocalizedName("dh.rite.elf");*/
	}
}
