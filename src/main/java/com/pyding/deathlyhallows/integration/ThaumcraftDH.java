package com.pyding.deathlyhallows.integration;

import com.emoniph.witchery.Witchery;
import com.pyding.deathlyhallows.DeathHallowsMod;
import com.pyding.deathlyhallows.blocks.VisConverter;
import com.pyding.deathlyhallows.blocks.VisConverterTile;
import com.pyding.deathlyhallows.items.InferioisMutandis;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import static com.pyding.deathlyhallows.DeathHallowsMod.*;

public class ThaumcraftDH {
    public static void register(){
        visc = new VisConverter().setBlockName("visconverter").setBlockTextureName("dh:visc").setCreativeTab(tabDeathlyHallows);
        inferioisMutandis = new InferioisMutandis().setUnlocalizedName("focusMutandis").setTextureName("dh:focus_mutant").setMaxStackSize(1).setCreativeTab(tabDeathlyHallows);
        GameRegistry.registerBlock(visc,"visconverter");
        GameRegistry.registerTileEntity(VisConverterTile.class,"visconverterTile");
        GameRegistry.registerItem(inferioisMutandis,inferioisMutandis.getUnlocalizedName().substring(5));

    }
    public static void recipes(){
        GameRegistry.addShapedRecipe(new ItemStack(visc),
                "BHB", "HSH", "BHB",
                'B', new ItemStack(Witchery.Items.MYSTIC_BRANCH),
                'H', Witchery.Items.GENERIC.itemDemonHeart.createStack(),
                'S', new ItemStack(deathShard));
    }
    public static void aspects(){
        ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.elderWand), (new AspectList()).add(Aspect.MAGIC,13).add(Aspect.DEATH,666).add(Aspect.DARKNESS,4).add(Aspect.WEAPON,17));
        ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.invisibilityMantle), (new AspectList()).add(Aspect.MAGIC,13).add(Aspect.DEATH,666).add(Aspect.DARKNESS,4).add(Aspect.ARMOR,17));
        ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.resurrectionStone), (new AspectList()).add(Aspect.MAGIC,13).add(Aspect.DEATH,666).add(Aspect.DARKNESS,4).add(Aspect.LIFE,17));
        ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.bertieBots), (new AspectList()).add(Aspect.HUNGER,16).add(Aspect.POISON,8).add(Aspect.HEAL,8));
        ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.gastronomicTemptation), (new AspectList()).add(Aspect.SOUL,8).add(Aspect.DEATH,8).add(Aspect.UNDEAD,8).add(Aspect.HUNGER,4));
        ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.resurrectionStone), (new AspectList()).add(Aspect.FLESH,1).add(Aspect.WATER,8).add(Aspect.TREE,16));
        ThaumcraftApi.registerObjectTag(new ItemStack(hobgoblinSoul), (new AspectList()).add(Aspect.SOUL,16).add(Aspect.BEAST,5));
        ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.hobgoblinChains), (new AspectList()).add(Aspect.SOUL,16).add(Aspect.TRAP,64).add(Aspect.MINE,64));
        ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.nimbus), (new AspectList()).add(Aspect.FLIGHT,32).add(Aspect.TREE,7).add(Aspect.WEATHER,10));
        ThaumcraftApi.registerObjectTag(new ItemStack(ItemBlockSpecialFlower.ofType("screamily").getItem()), (new AspectList().add(Aspect.TRAP,210).add(Aspect.MAGIC,400).add(Aspect.PLANT,30).add(Aspect.ENERGY,1000)));

    }
}
