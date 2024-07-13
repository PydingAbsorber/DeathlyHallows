package com.pyding.deathlyhallows;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.brewing.action.BrewAction;
import com.emoniph.witchery.brewing.action.BrewActionRitualRecipe;
import com.emoniph.witchery.crafting.KettleRecipes;
import com.emoniph.witchery.entity.*;
import com.emoniph.witchery.ritual.*;
import com.emoniph.witchery.ritual.rites.RiteSummonCreature;
import com.emoniph.witchery.ritual.rites.RiteSummonItem;
import com.pyding.deathlyhallows.commands.DamageLog;
import com.pyding.deathlyhallows.common.CommonProxy;
import com.pyding.deathlyhallows.common.handler.ConfigHandler;
import com.pyding.deathlyhallows.common.handler.EventHandler;
import com.pyding.deathlyhallows.entity.AbsoluteDeath;
import com.pyding.deathlyhallows.entity.ModEntity;
import com.pyding.deathlyhallows.integration.Integration;
import com.pyding.deathlyhallows.items.*;
import com.pyding.deathlyhallows.network.NetworkHandler;
import com.pyding.deathlyhallows.spells.RiteOfElvenVanishing;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import thaumcraft.common.config.ConfigItems;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;

@Mod(modid = DeathHallowsMod.ID, name = "Deathly Hallows", version = "1.0", dependencies = "required-after:witchery;required-after:Baubles;after:Thaumcraft;after:AWWayofTime;after:Botania;after:AdvancedBotany")
public class DeathHallowsMod {
    public static final String ID = "dh";
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
    public static ModEntity Entities;
    public static Item deadlyPrism;
    public static Item hobgoblinSoul;
    public static Item nimbus;
    public static Item deathShard;
    public static Item cards;
    public static Block visc;
    public static Item inferioisMutandis;

    public static Item monsterBook;
    public static Item trickOrTreat;
    @Mod.Instance("dh")
    public static DeathHallowsMod Instance;

    public static final NetworkHandler network = new NetworkHandler();
    @SidedProxy(
            clientSide = "com.pyding.deathlyhallows.client.ClientProxy",
            serverSide = "com.pyding.deathlyhallows.common.CommonProxy"
    )
    public static CommonProxy proxy;


    @Mod.EventHandler
    public void preInt(FMLPreInitializationEvent event) {
        invisibilityMantle = new InvisibilityMantle().setUnlocalizedName("InvisibilityMantle").setTextureName("dh:mantle").setMaxStackSize(1).setCreativeTab(tabDeathlyHallows);
        elderWand = new ElderWand().setUnlocalizedName("ElderWand").setTextureName("dh:wand").setMaxStackSize(1).setCreativeTab(tabDeathlyHallows);
        resurrectionStone = new ResurrectionStone().setUnlocalizedName("ResurrectionStone").setTextureName("dh:ring").setMaxStackSize(1).setCreativeTab(tabDeathlyHallows);
        tabItem = new TabItem().setUnlocalizedName("TabItem").setTextureName("dh:logo").setMaxStackSize(1);
        creativeItem = new CreativeItem().setUnlocalizedName("CreativeItem").setTextureName("dh:creative2").setMaxStackSize(1).setCreativeTab(tabDeathlyHallows);
        bertieBots = new BertieBotts(8, 20).setUnlocalizedName("BertieBotts").setTextureName("dh:candy").setMaxStackSize(64).setCreativeTab(tabDeathlyHallows);
        gastronomicTemptation = new GastronomicTemptation().setUnlocalizedName("GastronomicTemptation").setTextureName("dh:gastro").setMaxStackSize(64).setCreativeTab(tabDeathlyHallows);
        soupWithSawdust = new SoapWithSawdust().setUnlocalizedName("Soup").setTextureName("dh:soup").setMaxStackSize(64).setCreativeTab(null);
        viscousSecretions = new ViscousSecretions().setUnlocalizedName("ViscousSecretions").setTextureName("dh:secret").setMaxStackSize(64).setCreativeTab(tabDeathlyHallows);
        hobgoblinChains = new HobgoblinChains().setUnlocalizedName("HobgoblinChains").setTextureName("dh:chains").setMaxStackSize(64).setCreativeTab(tabDeathlyHallows);
        deadlyPrism = new DeadlyPrism().setUnlocalizedName("DeadlyPrism").setTextureName("dh:prism1").setMaxStackSize(1).setCreativeTab(tabDeathlyHallows);
        hobgoblinSoul = new HobgoblinSoul().setUnlocalizedName("HobgoblinSoul").setTextureName("dh:goblinsoul").setMaxStackSize(64).setCreativeTab(tabDeathlyHallows);
        nimbus = new Nimbus3000().setUnlocalizedName("Nimbus3000").setTextureName("dh:nimbus").setMaxStackSize(1).setCreativeTab(tabDeathlyHallows);
        deathShard = new DeathShard().setUnlocalizedName("deathShard").setTextureName("dh:shard").setMaxStackSize(16).setCreativeTab(tabDeathlyHallows);
        cards = new Cards().setUnlocalizedName("cards").setTextureName("dh:cards_daybreak").setMaxStackSize(1).setCreativeTab(tabDeathlyHallows);
        monsterBook = new MonsterBook().setUnlocalizedName("monsterBook").setTextureName("dh:monsterbook").setMaxStackSize(1).setCreativeTab(tabDeathlyHallows);
        trickOrTreat = new TrickOrTreat().setUnlocalizedName("trick").setTextureName("dh:trick").setMaxStackSize(64).setCreativeTab(tabDeathlyHallows);
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
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        FMLCommonHandler.instance().bus().register(new EventHandler());
        Entities = new ModEntity();
        NetworkHandler.preInit();
        Integration.preInit();
        proxy.preInit(event);
        ConfigHandler.init(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkHandler.init();
        Entities.init();
        Integration.init();
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        GameRegistry.addShapelessRecipe(new ItemStack(bertieBots), new ItemStack(Items.dye, 1, 3), new ItemStack(gastronomicTemptation));
        //Object[] recipe = {"A","B","A","C","D","C","E","F","G",'A',Witchery.Items.GENERIC.itemNullifiedLeather.createStack(),'B',Witchery.Items.GENERIC.itemOwletsWing.createStack(),'C',Witchery.Items.GENERIC.itemToeOfFrog.createStack(),'D',Witchery.Items.GENERIC.itemBookBurning.createStack(),'E',Witchery.Items.GENERIC.itemVampireBookPage.createStack(),'F',Witchery.Items.GENERIC.itemContractTorment.createStack(),'G',Witchery.Items.GENERIC.itemDarkCloth.createStack()};
        GameRegistry.addShapelessRecipe(new ItemStack(monsterBook), new ItemStack[]{Witchery.Items.GENERIC.itemNullifiedLeather.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack(), Witchery.Items.GENERIC.itemNullifiedLeather.createStack(), Witchery.Items.GENERIC.itemToeOfFrog.createStack(), Witchery.Items.GENERIC.itemBookBurning.createStack(), Witchery.Items.GENERIC.itemToeOfFrog.createStack(), Witchery.Items.GENERIC.itemVampireBookPage.createStack(), Witchery.Items.GENERIC.itemContractTorment.createStack(), Witchery.Items.GENERIC.itemDarkCloth.createStack()});
        //GameRegistry.addShapedRecipe(new ItemStack(monsterBook), recipe);
        KettleRecipes.instance().addRecipe(new ItemStack(gastronomicTemptation, 8), 0, 0, 2000F, -16003328, 0, Witchery.Items.GENERIC.itemDemonHeart.createStack(), Witchery.Items.GENERIC.itemToeOfFrog.createStack(), Witchery.Items.GENERIC.itemMellifluousHunger.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack(), Witchery.Items.GENERIC.itemWormyApple.createStack(), Witchery.Items.GENERIC.itemFrozenHeart.createStack());
        //16,28,40
        //RiteRegistry.addRecipe(110, 1, new RiteSummonItem(new ItemStack(soupWithSawdust), RiteSummonItem.Binding.NONE), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Items.water_bucket),new ItemStack(Items.water_bucket),new ItemStack(Items.water_bucket),new ItemStack(Items.rotten_flesh), new ItemStack(Blocks.planks, 1, 0)}), new SacrificePower(1000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 16)}).setUnlocalizedName("dh.rite.soup");
        RiteRegistry.addRecipe(111, 26, new RiteSummonCreature(AbsoluteDeath.class, false), new SacrificeMultiple(new SacrificeItem(Witchery.Items.GENERIC.itemRefinedEvil.createStack(), Witchery.Items.GENERIC.itemInfernalBlood.createStack(), new ItemStack(gastronomicTemptation), Witchery.Items.GENERIC.itemKobolditePentacle.createStack(), Witchery.Items.GENERIC.itemBinkyHead.createStack(), Witchery.Items.GENERIC.itemBrewOfInk.createStack()), new SacrificeLiving(EntityDeath.class), new SacrificeLiving(EntityZombie.class), new SacrificeLiving(EntitySkeleton.class), new SacrificeLiving(EntityEnderman.class), new SacrificeLiving(EntityNightmare.class), new SacrificeLiving(EntityGoblinGulg.class), new SacrificeLiving(EntityGoblinMog.class), new SacrificeLiving(EntityHornedHuntsman.class), new SacrificePower(21000.0F, 20)), EnumSet.noneOf(RitualTraits.class), new Circle(0, 0, 40), new Circle(0, 28, 0), new Circle(16, 0, 0)).setUnlocalizedName("dh.rite.death");
        Method meth = WitcheryBrewRegistry.class.getDeclaredMethod("register", BrewAction.class);
        meth.setAccessible(true);
        BrewActionRitualRecipe brewAction = new BrewActionRitualRecipe(new BrewItemKey(Items.water_bucket), new AltarPower(1000), new BrewActionRitualRecipe.Recipe(new ItemStack(soupWithSawdust), new ItemStack(Items.water_bucket), new ItemStack(Items.water_bucket), new ItemStack(Items.rotten_flesh), new ItemStack(Blocks.planks, 1, 0)));
        meth.invoke(WitcheryBrewRegistry.INSTANCE, brewAction);
        brewAction = new BrewActionRitualRecipe(new BrewItemKey(gastronomicTemptation), new AltarPower(5000), new BrewActionRitualRecipe.Recipe(new ItemStack(viscousSecretions), new ItemStack(Items.nether_star), Witchery.Items.GENERIC.itemTearOfTheGoddess.createStack(), new ItemStack(Items.redstone), Witchery.Items.GENERIC.itemBatWool.createStack(), Witchery.Items.GENERIC.itemDiamondVapour.createStack()));
        meth.invoke(WitcheryBrewRegistry.INSTANCE, brewAction);
        if (Integration.thaumcraft) {
            brewAction = new BrewActionRitualRecipe(new BrewItemKey(ConfigItems.itemBucketDeath), new AltarPower(7000), new BrewActionRitualRecipe.Recipe(new ItemStack(viscousSecretions), new ItemStack(Items.nether_star), Witchery.Items.GENERIC.itemTearOfTheGoddess.createStack(), new ItemStack(Items.redstone), Witchery.Items.GENERIC.itemBatWool.createStack(), Witchery.Items.GENERIC.itemDiamondVapour.createStack()));
            meth.invoke(WitcheryBrewRegistry.INSTANCE, brewAction);
        }
        brewAction = new BrewActionRitualRecipe(new BrewItemKey(Blocks.iron_bars), new AltarPower(17000), new BrewActionRitualRecipe.Recipe(new ItemStack(hobgoblinChains), new ItemStack(Blocks.iron_block), Witchery.Items.GENERIC.itemKobolditeNugget.createStack(), Witchery.Items.GENERIC.itemKobolditeDust.createStack(), new ItemStack(hobgoblinSoul)));
        meth.invoke(WitcheryBrewRegistry.INSTANCE, brewAction);
        RiteRegistry.addRecipe(112, 35, new RiteSummonItem(new ItemStack(nimbus), RiteSummonItem.Binding.NONE), new SacrificeMultiple(new SacrificeItem(Witchery.Items.GENERIC.itemBroomEnchanted.createStack(), Witchery.Items.GENERIC.itemFlyingOintment.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack(), Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), new ItemStack(Items.feather)), new SacrificePower(15000.0F, 20)), EnumSet.of(RitualTraits.ONLY_AT_NIGHT), new Circle(16, 0, 0), new Circle(28, 0, 0), new Circle(40, 0, 0)).setUnlocalizedName("dh.rite.nimbus");
        //RiteRegistry.addRecipe(113,36, new RiteSummonItem(new ItemStack(hobgoblinChains),RiteSummonItem.Binding.NONE), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemBrewOfWasting.createStack(), Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), Witchery.Items.GENERIC.itemWormyApple.createStack(), Witchery.Items.GENERIC.itemBrewSoulAnguish.createStack(), Witchery.Items.GENERIC.itemBrewGrave.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack()}), new SacrificePower(20000.0F, 20)}), EnumSet.of(RitualTraits.ONLY_AT_NIGHT), new Circle[]{new Circle(0, 16, 0), new Circle(0, 28, 0), new Circle(0, 40, 0)}).setUnlocalizedName("dh.rite.elf");
        RiteRegistry.addRecipe(113, 5, new RiteOfElvenVanishing(8, 40.0f, 0), new SacrificeMultiple(new SacrificeItem(Witchery.Items.GENERIC.itemBrewOfWasting.createStack(), Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), Witchery.Items.GENERIC.itemWormyApple.createStack(), Witchery.Items.GENERIC.itemBrewSoulAnguish.createStack(), Witchery.Items.GENERIC.itemBrewGrave.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack()), new SacrificePower(20000.0F, 20)), EnumSet.of(RitualTraits.ONLY_AT_NIGHT), new Circle(0, 16, 0), new Circle(0, 28, 0), new Circle(0, 40, 0)).setUnlocalizedName("dh.rite.elf");
        Integration.postInit();
        proxy.postInit(event);
        TrickOrTreat.initList();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new DamageLog());
    }

    public static CreativeTabs tabDeathlyHallows = new CreativeTabs("tabDeathlyHallows") {
        @Override
        public Item getTabIconItem() {
            return new ItemStack(tabItem).getItem();
        }
    };
}