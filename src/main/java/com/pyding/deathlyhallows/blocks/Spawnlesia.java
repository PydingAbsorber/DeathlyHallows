package com.pyding.deathlyhallows.blocks;

import com.pyding.deathlyhallows.common.handler.ConfigHandler;
import com.pyding.deathlyhallows.entity.AbsoluteDeath;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.subtile.SubTileFunctional;

import java.util.ArrayList;
import java.util.List;

public class Spawnlesia extends SubTileFunctional {
    private List<String> entities = new ArrayList<>();
    private String blackList = ConfigHandler.spawnlesia;
    public long summonMaxCd = 10000;
    public long summonCd = 0;
    public static int cost = ConfigHandler.spawnlesiaMana;
    @Override
    public void onUpdate() {
        super.onUpdate();
        if(mana > cost){
            if(System.currentTimeMillis() - summonCd > summonMaxCd) {
                addMana(-cost);
                summonCd = summonMaxCd + System.currentTimeMillis();
                if(!supertile.getWorldObj().isRemote)
                spawnRandomEntity();
            }
        }
    }

    @Override
    public boolean onWanded(EntityPlayer player, ItemStack wand) {
        return super.onWanded(player, wand);
    }

    @Override
    public int getMaxMana() {
        return 20*cost;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, entity, stack);
        populateEntityList();
    }

    public void populateEntityList() {
        for (Object obj : EntityList.stringToClassMapping.entrySet()) {
            java.util.Map.Entry<String, Class<?>> entry = (java.util.Map.Entry<String, Class<?>>) obj;
            Class<?> entityClass = entry.getValue();

            if (EntityLiving.class.isAssignableFrom(entityClass)) {
                entities.add(entry.getKey());
            }
        }
    }
    public void spawnRandomEntity() {
        World world = this.supertile.getWorldObj();
        double x = supertile.xCoord;
        double y = supertile.yCoord;
        double z = supertile.zCoord;
        if (!entities.isEmpty()) {
            String randomEntityName = entities.get(world.rand.nextInt(entities.size()));
            Class<?> entityClass = (Class<?>) EntityList.stringToClassMapping.get(randomEntityName);
            if (entityClass != null && isBlackListed(randomEntityName) == false) {
                Entity entity = EntityList.createEntityByName(randomEntityName, world);
                if (entity != null && entity instanceof EntityLiving && !(entity instanceof AbsoluteDeath)) {
                    entity.setPosition(x + Math.random()*10, y + 1, z + Math.random()*10);
                    world.spawnEntityInWorld(entity);
                } else spawnRandomEntity();
            } else spawnRandomEntity();
        }
    }
    public boolean isBlackListed(String name){
        if(blackList.contains(name))
            return true;
        return false;
    }
    public void getListOfEntities() {
        for (Object obj : EntityList.stringToClassMapping.entrySet()) {
            java.util.Map.Entry<String, Class<?>> entry = (java.util.Map.Entry<String, Class<?>>) obj;
            entities.add(entry.getKey());
        }
    }
}
