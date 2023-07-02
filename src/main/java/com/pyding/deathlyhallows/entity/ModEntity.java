package com.pyding.deathlyhallows.entity;

import com.pyding.deathlyhallows.DeathHallowsMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.ArrayList;
import java.util.List;

public class ModEntity {
    private final ArrayList entities = new ArrayList();
    public final ModEntity.EntityRef ABSOLUTE_DEATH;
    public List getEntites() {
        return this.entities;
    }

    public void init() {}

    public ModEntity(){
        this.ABSOLUTE_DEATH = (new ModEntity.LivingRef(92, AbsoluteDeath.class, "death", this.entities)).setEgg(9502720, 11430927);
    }

    public static class EntityRef {

        public final Class entity_class;
        public final String entity_name;
        public boolean can_capture;
        public boolean can_spawn;
        public boolean can_grind;
        private static int eggRoot = 6395;


        public EntityRef(int id, Class clazz, String name, ArrayList registry) {
            this(id, clazz, name, 80, 3, registry);
        }

        public EntityRef(int id, Class clazz, String name, int range, int updates, ArrayList registry) {
            this.entity_class = clazz;
            this.entity_name = name;
            EntityRegistry.registerModEntity(clazz, name, id, DeathHallowsMod.Instance, range, updates, true);
            registry.add(this);
        }

        public ModEntity.EntityRef setPropsMFR(boolean canCapture, boolean canSpawn, boolean canGrind) {
            this.can_capture = canCapture;
            this.can_spawn = canSpawn;
            this.can_grind = canGrind;
            return this;
        }

        public ModEntity.EntityRef setEgg(int color1, int color2) {
            int eggID = getUniqueEggId();
            EntityList.IDtoClassMapping.put(Integer.valueOf(eggID), this.entity_class);
            EntityList.entityEggs.put(Integer.valueOf(eggID), new EntityList.EntityEggInfo(eggID, color1, color2));
            return this;
        }

        private static int getUniqueEggId() {
            do {
                ++eggRoot;
            } while(EntityList.getStringFromID(eggRoot) != null);

            return eggRoot;
        }

    }

    public static class LivingRef extends ModEntity.EntityRef {

        public final Class living_class;


        public LivingRef(int id, Class clazz, String name, ArrayList registry) {
            super(id, clazz, name, 80, 3, registry);
            this.living_class = clazz;
        }

        public LivingRef(int id, Class clazz, String name, int range, int updates, ArrayList registry) {
            super(id, clazz, name, range, updates, registry);
            this.living_class = clazz;
        }

        public ModEntity.LivingRef addSpawn(int weight, int min, int max, EnumCreatureType type, BiomeGenBase... biomes) {
            EntityRegistry.addSpawn(this.living_class, weight, min, max, type, biomes);
            return this;
        }
    }
}
