package com.pyding.deathlyhallows.integration;

import com.pyding.deathlyhallows.DeathHallowsMod;
import cpw.mods.fml.common.Loader;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class Integration {
    public static boolean thaumcraft = false;
    public static boolean botania = false;
    public static boolean bloodMagic = false;
    public static void preInit(){
        thaumcraft = Loader.isModLoaded("Thaumcraft");
        botania = Loader.isModLoaded("Botania");
        bloodMagic = Loader.isModLoaded("AWWayofTime");
    }
    public static void postInit(){
        if (thaumcraft){
            ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.elderWand), (new AspectList()).add(Aspect.MAGIC,64).add(Aspect.DEATH,64).add(Aspect.DARKNESS,64).add(Aspect.WEAPON,64));
            ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.invisibilityMantle), (new AspectList()).add(Aspect.MAGIC,64).add(Aspect.DEATH,64).add(Aspect.DARKNESS,64).add(Aspect.ARMOR,64));
            ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.resurrectionStone), (new AspectList()).add(Aspect.MAGIC,64).add(Aspect.DEATH,64).add(Aspect.DARKNESS,64).add(Aspect.LIFE,64));
            ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.bertieBots), (new AspectList()).add(Aspect.HUNGER,16).add(Aspect.POISON,8).add(Aspect.HEAL,8));
            ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.gastronomicTemptation), (new AspectList()).add(Aspect.SOUL,8).add(Aspect.DEATH,8).add(Aspect.UNDEAD,8).add(Aspect.HUNGER,4));
            ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.resurrectionStone), (new AspectList()).add(Aspect.FLESH,1).add(Aspect.WATER,8).add(Aspect.TREE,16));
            ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.hobgoblinSoul), (new AspectList()).add(Aspect.SOUL,16).add(Aspect.BEAST,5));
            ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.hobgoblinChains), (new AspectList()).add(Aspect.SOUL,16).add(Aspect.TRAP,64).add(Aspect.MINE,64));
            ThaumcraftApi.registerObjectTag(new ItemStack(DeathHallowsMod.nimbus), (new AspectList()).add(Aspect.FLIGHT,32).add(Aspect.TREE,7).add(Aspect.WEATHER,10));
        }
    }
}
