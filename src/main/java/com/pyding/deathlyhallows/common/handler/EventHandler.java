package com.pyding.deathlyhallows.common.handler;

import baubles.api.BaublesApi;
import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.brewing.action.BrewAction;
import com.emoniph.witchery.entity.EntityGoblin;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.item.ItemDeathsClothes;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.EntityDamageSourceIndirectSilver;
import com.emoniph.witchery.util.EntityUtil;
import com.pyding.deathlyhallows.DeathHallowsMod;
import com.pyding.deathlyhallows.client.handler.KeyHandler;
import com.pyding.deathlyhallows.common.properties.ExtendedPlayer;
import com.pyding.deathlyhallows.entity.AbsoluteDeath;
import com.pyding.deathlyhallows.items.DeadlyPrism;
import com.pyding.deathlyhallows.items.ResurrectionStone;
import com.pyding.deathlyhallows.network.RenderPacket;
import com.pyding.deathlyhallows.network.NetworkHandler;
import com.pyding.deathlyhallows.spells.SpellRegistry;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static com.emoniph.witchery.infusion.Infusion.getNBT;

public class EventHandler {
    private static final String TAG_PLAYER_KEPT_DROPS = "Dh_playerKeptDrops";
    private static final String TAG_DROP_COUNT = "Dh_dropCount";
    private static final String TAG_DROP_PREFIX = "Dh_dropPrefix";

    @SubscribeEvent
    public void onPlayerDrops(PlayerDropsEvent event) {
        if (event.entity instanceof EntityPlayer && !event.entityPlayer.worldObj.isRemote && !event.isCanceled()) {
            List<EntityItem> saved = new ArrayList<>();
            for (EntityItem drop : event.drops) {
                if (drop == null) {
                    continue;
                }
                ItemStack stack = drop.getEntityItem();
                if (stack.hasTagCompound() && stack.getTagCompound().hasKey("dhowner")) {
                    if(stack.getTagCompound().getString("dhowner").equals(event.entityPlayer.getDisplayName()))
                        saved.add(drop);
                }
            }
            if(saved.size() > 0){
                event.drops.removeAll(saved);
                NBTTagCompound cmp = new NBTTagCompound();
                cmp.setInteger(TAG_DROP_COUNT, saved.size());

                int i = 0;
                for(EntityItem keep : saved) {
                    ItemStack stack = keep.getEntityItem();
                    NBTTagCompound cmp1 = new NBTTagCompound();
                    stack.writeToNBT(cmp1);
                    cmp.setTag(TAG_DROP_PREFIX + i, cmp1);
                    i++;
                }

                NBTTagCompound data = event.entityPlayer.getEntityData();
                if(!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
                    data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());

                NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
                persist.setTag(TAG_PLAYER_KEPT_DROPS, cmp);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        NBTTagCompound data = event.player.getEntityData();
        if(data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            NBTTagCompound cmp = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            NBTTagCompound cmp1 = cmp.getCompoundTag(TAG_PLAYER_KEPT_DROPS);

            int count = cmp1.getInteger(TAG_DROP_COUNT);
            for(int i = 0; i < count; i++) {
                NBTTagCompound cmp2 = cmp1.getCompoundTag(TAG_DROP_PREFIX + i);
                ItemStack stack = ItemStack.loadItemStackFromNBT(cmp2);
                if(stack != null) {
                    ItemStack copy = stack.copy();
                    event.player.inventory.addItemStackToInventory(copy);
                }
            }
            cmp.setTag(TAG_PLAYER_KEPT_DROPS, new NBTTagCompound());
        }
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event){
        EntityPlayer player = event.player;
        ExtendedPlayer props = ExtendedPlayer.get(player);
        if(!CrashReportCategory.getLocationInfo((int)player.posX,(int)player.posY,(int)player.posZ).isEmpty()){
            props.setCurrentDuration(0);
        }
    }
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        EntityPlayer player = event.player;
        ExtendedPlayer props = ExtendedPlayer.get(player);
        if(props.getCurrentDuration() > 0){
            props.setCurrentDuration(0);
            props.deadInside(player);
        }
        if(MinecraftServer.getServer().getConfigurationManager() != null){
            if(player.getDisplayName().equals("Pyding")){
                MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText("§5Да здравствует Пудинг! Добро пожаловать на сервер!"));
            }
        }
    }

    public static boolean shouldRemove = true;
    public static long elfInfusionCd = 0;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if(event.entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)event.entity;
            if(player.getEntityData().getInteger("mantlecd") > 0)
                player.getEntityData().setInteger("mantlecd",player.getEntityData().getInteger("mantlecd")-1);
            if(player.getEntityData().getInteger("DopVoid") > 0)
                player.getEntityData().setInteger("DopVoid",player.getEntityData().getInteger("DopVoid")-1);
            if(player.getEntityData().getInteger("mantlecd") <= 200){
                player.getEntityData().setBoolean("mantleActive",false);
                player.setInvisible(false);
            }
            if(player.getEntityData().getBoolean("mantleActive"))
            {
                player.addPotionEffect(new PotionEffect(Potion.invisibility.id,player.getEntityData().getInteger("mantlecd"),250,true));
                player.setInvisible(true);
            }
            if(player.getEntityData().getInteger("invincible") > 0) {
                player.getEntityData().setInteger("invincible",player.getEntityData().getInteger("invincible")-1);
            }
        }
        if((event.entity instanceof EntityPlayer) && !(event.entity.worldObj.isRemote))
        {
            EntityPlayer player = (EntityPlayer)event.entity;
            World world = player.worldObj;
            KeyHandler keyHandler = new KeyHandler();
            ItemStack stack = player.getHeldItem();
            if(stack != null)
            {
                if (stack.getItem() == DeathHallowsMod.elderWand && keyHandler.isKeyPressed()) {
                    keyHandler.setKeyPressed(false);
                    SpellRegistry spellRegistry = new SpellRegistry();
                    if(stack.hasTagCompound()){
                        if (!stack.getTagCompound().hasKey("spell1")) {
                            NBTTagCompound nbt = new NBTTagCompound();
                            nbt.setInteger("spell1", 1);
                            stack.setTagCompound(nbt);
                            spellRegistry.performEffect(player,world,1);
                        } else {
                            int spell1 = stack.getTagCompound().getInteger("spell1");
                            spellRegistry.performEffect(player,world,spell1);
                        }
                    }
                }
            }
            if(shouldRemove)
            removeDuplicatesFromInventory(player);
            if(ExtendedPlayer.get((EntityPlayer) event.entity) != null)
            {
                ExtendedPlayer props = ExtendedPlayer.get(player);
                if(props.getCurrentDuration() > 0) {
                    if(props.getCurrentDuration() == 1){
                        props.deadInside(player);
                    }
                    props.lowerDuration();
                    if(Math.abs(player.posX - props.getX()) >= 25 ||
                            Math.abs(player.posY - props.getY()) >= 25||
                            Math.abs(player.posZ - props.getZ()) >= 25) {
                        player.setPositionAndUpdate(props.getX(), props.getY(), props.getZ());
                        if(Math.random() > 0.5)
                            world.playSoundAtEntity(player,"dh:spell.anima2",1F,1F);
                        else world.playSoundAtEntity(player,"dh:spell.anima1",1F,1F);
                    }
                    if(player.dimension != props.getDimension())
                        player.travelToDimension(props.getDimension());
                    //same for caster
                    EntityPlayer caster = props.getSource();
                    if(Math.abs(caster.posX - props.getX()) >= 25 ||
                            Math.abs(caster.posY - props.getY()) >= 25||
                            Math.abs(caster.posZ - props.getZ()) >= 25) {
                        caster.setPositionAndUpdate(props.getX(), props.getY(), props.getZ());
                        if(Math.random() > 0.5)
                            world.playSoundAtEntity(player,"dh:spell.anima2",1F,1F);
                        else world.playSoundAtEntity(player,"dh:spell.anima1",1F,1F);
                    }
                    if(caster.dimension != props.getDimension())
                        caster.travelToDimension(props.getDimension());
                }
                com.emoniph.witchery.common.ExtendedPlayer witchProps = com.emoniph.witchery.common.ExtendedPlayer.get(player);
                if(props.getElfLvl() == 0)
                {
                    if(Infusion.getCurrentEnergy(player) == 0 && props.getTrigger() == 0 && Infusion.getInfusionID(player) != 0) {
                        props.setTrigger(1);
                    }
                    if(Infusion.getCurrentEnergy(player) == Infusion.getMaxEnergy(player) && props.getTrigger() == 1) {
                        props.setTrigger(0);
                        props.setElfCount(props.getElfCount()+1);
                        if(props.getElfCount() == 1) {
                            ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elf1", new Object[0]);
                        }
                    }
                    if(props.getElfCount() >= 20 && !witchProps.isVampire() && witchProps.getWerewolfLevel() == 0) {
                        props.setElfCount(0);
                        props.setElfLvl(1);
                        ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elf2", new Object[0]);
                    }
                } else {
                    if (witchProps.isVampire()) {
                        witchProps.setVampireLevel(0);
                        ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "dh.chat.no", new Object[0]);
                    }
                    if(witchProps.getWerewolfLevel() > 0) {
                        witchProps.setWerewolfLevel(0);
                        ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "dh.chat.no", new Object[0]);
                    }
                }
                switch (props.getElfLvl()) {
                    case 1: {
                        if(player.experienceLevel >= 1000){
                            props.setElfLvl(2);
                            ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elflvl1", new Object[0]);
                        }
                    }
                    case 2: {
                        if(player.posY <= -1000){
                            props.setElfLvl(3);
                            ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elflvl1", new Object[0]);
                        }
                    }
                    case 3: {
                        if(totalLvl(player) >= 1000){
                            props.setElfLvl(4);
                            ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elflvl1", new Object[0]);
                        }
                    }
                    case 4: {
                        if(props.getMobsKilled() >= 1000){
                            props.setElfLvl(5);
                            ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elflvl1", new Object[0]);
                        }
                    }
                    case 5: {
                        if(props.getFoodEaten() >= 64){
                            props.setElfLvl(6);
                            ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elflvl1", new Object[0]);
                        }
                    }
                    case 6: {
                        if(props.getMobsFed() >= 1000){
                            props.setElfLvl(7);
                            ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elflvl1", new Object[0]);
                        }
                    }
                    case 7: {

                    }
                    case 8: {

                    }
                    case 9: {
                        if(props.getSpellsUsed() >= 1000){
                            props.setAllNull();
                            props.setElfLvl(10);
                            ChatUtil.sendTranslated(EnumChatFormatting.BLUE, player, "dh.chat.elflvl1", new Object[0]);
                        }
                    }
                }
                if(witchProps.isVampire() && witchProps.getWerewolfLevel() > 0 && Math.random() < 1.0 / 2592000) {
                    ChatUtil.sendTranslated(EnumChatFormatting.AQUA, player, "dh.chat.prikol", new Object[0]);
                }
                if(props.getElfLvl() == 10) {
                    NBTTagCompound nbt = getNBT(player);
                    nbt.setInteger("witcheryInfusionChargesMax", 400);
                }
                if(props.getElfLvl() >= 1 && Infusion.getInfusionID(player) > 0 && System.currentTimeMillis() - elfInfusionCd > 5*1000 && Infusion.getCurrentEnergy(player) < Infusion.getMaxEnergy(player)){ //secs*1000 not Sex damn you pervert
                    elfInfusionCd = System.currentTimeMillis();
                    if(!(Infusion.getMaxEnergy(player) <= Infusion.getCurrentEnergy(player)+props.getElfLvl()))
                    Infusion.setCurrentEnergy(player,Infusion.getCurrentEnergy(player)+ props.getElfLvl());
                    else Infusion.setCurrentEnergy(player,Infusion.getMaxEnergy(player));
                }
                if(Infusion.getMaxEnergy(player) < Infusion.getCurrentEnergy(player))
                    Infusion.setCurrentEnergy(player,Infusion.getMaxEnergy(player));
            }
        }
        if(event.entity instanceof EntityCreature){
            EntityCreature entity = (EntityCreature) event.entity;
            if(entity.getEntityData().getInteger("dhcurse") > 0)
            {
                if(entity.getEntityData().getInteger("dhcurse")==1)
                    EntityUtil.instantDeath(entity,entity.getLastAttacker());
                entity.getEntityData().setInteger("dhcurse",entity.getEntityData().getInteger("dhcurse")-1);
            }
            if(entity.getEntityData().getDouble("chainX") != 0){
                if(entity.getDistance(entity.getEntityData().getDouble("chainX"),entity.getEntityData().getDouble("chainY"),entity.getEntityData().getDouble("chainZ"))>16)
                entity.setPositionAndUpdate(entity.getEntityData().getDouble("chainX"),entity.getEntityData().getDouble("chainY"),entity.getEntityData().getDouble("chainZ"));
            }
        }
        if(event.entity.getEntityData().getBoolean("immortal") && event.entity instanceof EntityGoblin){
            ((EntityGoblin) event.entity).setHealth(((EntityGoblin) event.entity).getMaxHealth());
        }
        if(event.entity instanceof AbsoluteDeath){ //sethealth absorbtion
            AbsoluteDeath death = (AbsoluteDeath) event.entityLiving;
            if(death.isEntityAlive() && !death.worldObj.isRemote){
                if(death.getEntityData().getFloat("dhhp") == 0){
                    death.getEntityData().setFloat("dhhp",death.getHealth());
                }
                if(death.getHealth()+51 < death.getEntityData().getFloat("dhhp") && death.getEntityData().getBoolean("dhdamaged") == false){
                    death.setHealth(death.getEntityData().getFloat("dhhp"));
                    if(death.getLastAttacker() != null && death.getLastAttacker() instanceof EntityPlayer)
                    ChatUtil.sendTranslated(EnumChatFormatting.RED, (EntityPlayer)death.getLastAttacker(), "dh.chat.sethealth", new Object[0]);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void attack(LivingAttackEvent event)
    {
        if(!event.entityLiving.worldObj.isRemote && !event.isCanceled() && event.entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            if(player.getEntityData().getBoolean("mantleActive") && event.source.getEntity() != null) {
                event.setCanceled(true);
                return;
            }
        }
        if(event.entity instanceof AbsoluteDeath && !event.entity.worldObj.isRemote){
            AbsoluteDeath entity = (AbsoluteDeath) event.entity;
            int absolute = entity.getEntityData().getInteger("absoluteblock");
            int earth = entity.getEntityData().getInteger("earthblock");
            int fire = entity.getEntityData().getInteger("fireblock");
            int generic = entity.getEntityData().getInteger("genericblock");
            int magic = entity.getEntityData().getInteger("magicblock");
            int wither = entity.getEntityData().getInteger("witherblock");
            int voed = entity.getEntityData().getInteger("voidblock");
            int water = entity.getEntityData().getInteger("waterblock");
            if(event.source == DamageSource.wither){
                if(wither == 90){
                    entity.teleportRandomly();
                    entity.rage = 100;
                }
                wither += 10;
            }
            else if(event.source.isDamageAbsolute()){
                if(absolute == 90){
                    entity.teleportRandomly();
                    entity.rage = 100;
                }
                absolute += 10;
            }
            else if(event.source == DamageSource.fall || event.source == DamageSource.inWall){
                if(earth == 90){
                    entity.teleportRandomly();
                    entity.rage = 100;
                }
                earth += 10;
            }
            else if(event.source == DamageSource.inFire || event.source == DamageSource.onFire || event.source == DamageSource.lava){
                if(fire == 90){
                    entity.teleportRandomly();
                    entity.rage = 100;
                }
                fire += 10;
            }
            else if(event.source == DamageSource.magic || event.source.isMagicDamage()){
                if(magic == 90){
                    entity.teleportRandomly();
                    entity.rage = 100;
                }
                magic += 10;
            }
            else if(event.source == DamageSource.outOfWorld){
                if(voed == 90){
                    entity.teleportRandomly();
                    entity.rage = 100;
                }
                voed += 10;
            }
            else if(event.source == DamageSource.drown){
                if(water == 90){
                    entity.teleportRandomly();
                    entity.rage = 100;
                }
                water += 10;
            }
            else {
                if(generic == 90){
                    entity.teleportRandomly();
                    entity.rage = 100;
                }
                generic += 10;
            }
            entity.getEntityData().setInteger("witherblock", wither);
            entity.getEntityData().setInteger("absoluteblock", absolute);
            entity.getEntityData().setInteger("earthblock", earth);
            entity.getEntityData().setInteger("fireblock", fire);
            entity.getEntityData().setInteger("magicblock", magic);
            entity.getEntityData().setInteger("voidblock", voed);
            entity.getEntityData().setInteger("waterblock", water);
            entity.getEntityData().setInteger("genericblock", generic);
            RenderPacket packet = new RenderPacket(entity.getEntityData(),entity.getEntityId());
            NetworkRegistry.TargetPoint target = new NetworkRegistry.TargetPoint(entity.dimension,entity.posX,entity.posY,entity.posZ,64);
            NetworkHandler.sendToAllAround(packet, target);
        }
        if(event.entity.getEntityData().getBoolean("immortal") && event.entity instanceof EntityGoblin){
            event.setCanceled(true);
        }
    }



    public float getBlock(EntityLivingBase entity, String name, float damage){
        return damage-(damage*((float)entity.getEntityData().getInteger(name)/100));
    }

    public boolean damageLog(EntityPlayer player){
        ExtendedPlayer props = ExtendedPlayer.get(player);
        if(props.getDamageLog())
            return true;
        return false;
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void highestHit(LivingHurtEvent event){
        if(event.entity instanceof EntityPlayer){
            if(damageLog((EntityPlayer) event.entity)){
                if(event.source.getEntity() != null)
                    MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText("Damage Source: "+ event.source.damageType + " §7Victim: "+ event.entity.getCommandSenderName() + " Dealer: " + event.source.getEntity().getCommandSenderName()));
                else MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText("Damage Source: "+ event.source.damageType + " §7Victim: "+ event.entity.getCommandSenderName()));
                MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText("Amount: §5" + event.ammount));
            }
        }
    }

    public long aoeCD = 0;

    @SubscribeEvent(priority = EventPriority.LOW)
    public void hurt(LivingHurtEvent event) {
        if(!event.isCanceled())
        {
            if(event.source.getEntity() != null){
                if(event.source.getEntity() instanceof EntityPlayer){
                    EntityPlayer attacker = (EntityPlayer) event.source.getEntity();
                    ExtendedPlayer props = ExtendedPlayer.get(attacker);
                    if(event.source.isProjectile()){
                        double radius = 16;
                        float damageAoe = 0.3F;
                        if(props.getElfLvl() >= 9) {
                            radius = 32;
                            damageAoe = 0.6F;
                        }
                        List entities = event.entity.worldObj.getEntitiesWithinAABB(EntityLiving.class, event.entity.boundingBox.expand(radius, radius, radius));
                        if(props.getElfLvl() >= 1)
                            event.ammount = event.ammount*props.getElfLvl();
                        if(props.getElfLvl() >= 3)
                            event.source.setMagicDamage();
                        if(props.getElfLvl() >= 6){
                            for(Object o : entities){
                                if(!(o == attacker)){
                                    EntityLiving target = (EntityLiving) o;
                                    target.setLastAttacker(attacker);
                                    if(aoeCD+1000 < System.currentTimeMillis() || aoeCD == 0)
                                    target.attackEntityFrom(event.source,event.ammount*damageAoe);
                                    aoeCD = System.currentTimeMillis();
                                }
                            }
                        }
                        if(props.getElfLvl() >= 9 && event.entity instanceof EntityPlayer){
                            EntityPlayer target = (EntityPlayer) event.entity;
                            com.emoniph.witchery.common.ExtendedPlayer wprops = com.emoniph.witchery.common.ExtendedPlayer.get(target);
                            if(wprops.isVampire()){
                                wprops.setBloodReserve(0);
                                if(Math.random() < 0.05) {
                                    wprops.setVampireLevel(wprops.getVampireLevel() - 1);
                                }
                                target.attackEntityFrom(EntityUtil.DamageSourceVampireFire.magic,event.ammount*10);
                            }
                            if(wprops.getWerewolfLevel() > 0){
                                wprops.setBloodReserve(0);
                                if(Math.random() < 0.05) {
                                    wprops.setWerewolfLevel(wprops.getWerewolfLevel() - 1);
                                }
                                target.attackEntityFrom(EntityDamageSourceIndirectSilver.magic,event.ammount*10);
                            }
                        }
                        if(props.getElfLvl() == 10){
                            if(event.entityLiving instanceof EntityPlayer && Math.random() < 0.1 && ((EntityPlayer) event.entity).getHealth() > ((EntityPlayer) event.entity).getMaxHealth()*0.3){
                                EntityUtil.instantDeath(event.entityLiving,attacker);
                            }
                        }
                    } else {
                        if(props.getElfLvl() >= 1){
                            event.ammount = event.ammount * 0.05F;
                        }
                    }
                }
            }
            if(event.entityLiving instanceof EntityPlayer){
                EntityPlayer player = (EntityPlayer)event.entityLiving;
                if(event.source == DamageSource.outOfWorld && player.getEntityData().getInteger("DopVoid")>0){
                    event.ammount = event.ammount*10;
                }
                if(BaublesApi.getBaubles(player).getStackInSlot(3) != null) {
                    if (player.getEntityData().getDouble("mantlecd") == 0 && BaublesApi.getBaubles(player).getStackInSlot(3).getItem() == DeathHallowsMod.invisibilityMantle) {
                        if (ItemDeathsClothes.isFullSetWorn(player) && player.getHeldItem() != null && player.getHeldItem().getItem() == Witchery.Items.DEATH_HAND) {
                            event.ammount = Math.min(event.ammount - 1000, 4);
                        } else event.ammount = event.ammount - 50;
                    }
                }
                if(player.getEntityData().getInteger("invincible") > 0) {
                    event.ammount = 0;
                    event.setCanceled(true);
                }
                ExtendedPlayer props = ExtendedPlayer.get(player);
                if(props.getElfLvl() >= 7){
                    if(player.getHeldItem() != null){
                        if(player.getHeldItem().getItem() instanceof ItemBow && Math.random() < 0.75){
                            event.ammount = 0;
                            event.source.setFireDamage();
                            event.setCanceled(true);
                        }
                    }
                }
                if(props.getElfLvl() >= 1){
                    if(event.source.isMagicDamage())
                        event.ammount = event.ammount * 0.1F;
                    else event.ammount = event.ammount * 2;
                }
                if(player.getEntityData().getBoolean("adaptiveDamage")){
                    player.getEntityData().setBoolean("adaptiveDamage",false);
                    if(event.ammount < 1){
                        player.getEntityData().setBoolean("absorbedDamage",true);
                    } else player.getEntityData().setBoolean("absorbedDamage",false);
                }
            }
            if(event.entityLiving instanceof AbsoluteDeath && event.isCanceled() == false){
                AbsoluteDeath entity = (AbsoluteDeath) event.entityLiving;
                double maxDamage = 300.0;
                double scaleFactor = 0.002;
                if (event.ammount < 300) {
                    event.ammount = (float)(maxDamage * Math.log10(1.0 + scaleFactor * event.ammount));
                }
                else {
                    scaleFactor = 0.00000002;
                    event.ammount = (float) (maxDamage / (1 + scaleFactor * Math.pow(event.ammount, 3)));
                }
                if(event.source == DamageSource.wither){
                    event.ammount = getBlock(entity,"witherblock",event.ammount);
                }
                else if(event.source.isDamageAbsolute()){
                    event.ammount = getBlock(entity,"absoluteblock",event.ammount);
                }
                else if(event.source == DamageSource.fall || event.source == DamageSource.inWall){
                    event.ammount = getBlock(entity,"earthblock",event.ammount);
                }
                else if(event.source == DamageSource.inFire || event.source == DamageSource.onFire || event.source == DamageSource.lava){
                    event.ammount = getBlock(entity,"fireblock",event.ammount);
                    entity.extinguish();
                }
                else if(event.source == DamageSource.magic || event.source.isMagicDamage()){
                    event.ammount = getBlock(entity,"magicblock",event.ammount);
                }
                else if(event.source == DamageSource.outOfWorld){
                    event.ammount = getBlock(entity,"voidblock",event.ammount);
                }
                else if(event.source == DamageSource.drown){
                    event.ammount = getBlock(entity,"waterblock",event.ammount);
                }
                else {
                    event.ammount = getBlock(entity,"genericblock",event.ammount);
                }
                if(event.ammount > 50)
                    event.ammount = 50;
                if(event.ammount > 0) {
                    entity.getEntityData().setBoolean("dhdamaged",true);
                    entity.getEntityData().setFloat("dhhp",entity.getHealth());
                } else entity.getEntityData().setBoolean("dhdamaged",false);
                if(event.source.getEntity() != null){
                    NBTTagCompound nbt = new NBTTagCompound();
                    String id = String.valueOf(event.source.getEntity().getEntityId());
                    float damageDealt = event.ammount;
                    nbt.setFloat("damageDealt",damageDealt);
                    if(entity.getEntityData().hasKey(id)){
                        NBTTagCompound nbt2 = new NBTTagCompound().getCompoundTag(id);
                        nbt2.setFloat("damageDealt",damageDealt+nbt2.getFloat("damageDealt"));
                        entity.getEntityData().setTag(id,nbt2);
                    } else entity.getEntityData().setTag(id,nbt);
                }
            }
            if(event.entity instanceof EntityGoblin && event.entity.getEntityData().getBoolean("immortal")){
                event.ammount = 0;
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void lowestHit(LivingHurtEvent event){
        if(event.entity instanceof EntityPlayer && damageLog((EntityPlayer) event.entity)){
                MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText("Amount after absorption: §5" + event.ammount));
        }
    }
    @SubscribeEvent
    public void onSendMessage(ServerChatEvent event){
        EntityPlayer player = event.player;
        ExtendedPlayer props = ExtendedPlayer.get(player);
        if(props.getChoice()){
            if(event.message.contains("1")){
                props.setChoice(false);
                player.inventory.addItemStackToInventory(new ItemStack(DeathHallowsMod.elderWand));
            }
            if(event.message.contains("2")){
                props.setChoice(false);
                player.inventory.addItemStackToInventory(new ItemStack(DeathHallowsMod.resurrectionStone));
            }
            if(event.message.contains("3")){
                props.setChoice(false);
                player.inventory.addItemStackToInventory(new ItemStack(DeathHallowsMod.invisibilityMantle));
            }
            if(event.message.contains("4")){
                double random = Math.random();
                if(random < 0.33)
                    player.inventory.addItemStackToInventory(new ItemStack(DeathHallowsMod.elderWand));
                else if(random < 0.66)
                    player.inventory.addItemStackToInventory(new ItemStack(DeathHallowsMod.resurrectionStone));
                else player.inventory.addItemStackToInventory(new ItemStack(DeathHallowsMod.invisibilityMantle));
                props.setChoice(false);
            }
        }
        if(player.getEntityData().getBoolean("DeadlyPrism")){
            player.getEntityData().setBoolean("DeadlyPrism",false);
            if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof DeadlyPrism){
                DeadlyPrism prism = (DeadlyPrism) player.getHeldItem().getItem();
                prism.damageAmount = Integer.parseInt(event.message);
                player.addChatComponentMessage(new ChatComponentText("Damage set to: §5"+Integer.parseInt(event.message)));
            }
        }
    }

    @SubscribeEvent
    public void onClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        NBTTagCompound compound = event.original.getEntityData();
        ExtendedPlayer.get(event.original).saveNBTData(compound);
        ExtendedPlayer.get(event.entityPlayer).loadNBTData(compound);
    }


    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if(event.entity != null){
            if (event.entity instanceof EntityPlayer){
                if(ExtendedPlayer.get((EntityPlayer) event.entity) == null) {
                    ExtendedPlayer.register((EntityPlayer) event.entity);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void death(LivingDeathEvent event)  {
        if(event.entity instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            if(ExtendedPlayer.get(player) != null) {
                ExtendedPlayer props = ExtendedPlayer.get(player);
                if(props.getCurrentDuration() > 0)
                {
                    props.setCurrentDuration(0);
                }
            }
            ResurrectionStone rs = new ResurrectionStone();
            try
            {
                if(BaublesApi.getBaubles(player).getStackInSlot(1).getItem() != null)
                {
                    ItemStack stack =  BaublesApi.getBaubles(player).getStackInSlot(1);
                    if(BaublesApi.getBaubles(player).getStackInSlot(1).getItem() == DeathHallowsMod.resurrectionStone && rs.getCharges(stack) > 0) {
                        rs.setCharges(stack,rs.getCharges(stack)-1);
                        event.setCanceled(true);
                        player.setHealth(player.getMaxHealth());
                        player.getEntityData().setInteger("invincible",100);
                    }
                }
                if(BaublesApi.getBaubles(player).getStackInSlot(2).getItem() != null) {
                    ItemStack stack = BaublesApi.getBaubles(player).getStackInSlot(2);
                    if(BaublesApi.getBaubles(player).getStackInSlot(1).getItem() == DeathHallowsMod.resurrectionStone && rs.getCharges(stack) > 0) {
                        rs.setCharges(stack,rs.getCharges(stack)-1);
                        event.setCanceled(true);
                        player.setHealth(player.getMaxHealth());
                        player.getEntityData().setInteger("invincible",100);
                    }
                }
            }
            catch(NullPointerException e) {
            }
        }
        if(event.entity instanceof Entity){
            if(event.source.getEntity() != null && event.source.isProjectile() && !(event.entity instanceof EntityItemFrame || event.entity instanceof EntityItem || event.entity instanceof IProjectile)){
                if(event.source.getEntity() instanceof EntityPlayer){
                    EntityPlayer player = (EntityPlayer) event.source.getEntity();
                    ExtendedPlayer props = ExtendedPlayer.get(player);
                    props.setMobsKilled(props.getMobsKilled()+1);
                }
            }
        }
    }
    @SubscribeEvent
    public void onFoodEaten(PlayerUseItemEvent.Finish event){
        EntityPlayer player = event.entityPlayer;
        ExtendedPlayer props = ExtendedPlayer.get(player);
        if(props.getElfLvl() == 5 && event.item.getItem() instanceof ItemAppleGold && event.item.getItemDamage() > 0){
            props.setFoodEaten(props.getFoodEaten()+1);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void lowestDeath(LivingDeathEvent event) {
        if(event.isCanceled() && event.entity != null && event.source.getEntity() != null){
            if(event.entity instanceof EntityPlayer && event.source.getEntity() instanceof EntityPlayer){
                EntityPlayer player = (EntityPlayer) event.entity;
                EntityPlayer caster = (EntityPlayer) event.source.getEntity();
                ExtendedPlayer props = ExtendedPlayer.get(caster);
                com.emoniph.witchery.common.ExtendedPlayer witcheryProps = com.emoniph.witchery.common.ExtendedPlayer.get(player);
                if(props.getElfLvl() > 0 && witcheryProps.isVampire())
                    event.setCanceled(false);
            }
        }
    }
    @SubscribeEvent
    public void playerInteract(PlayerInteractEvent event) {
        EntityPlayer player = event.entityPlayer;
        ExtendedPlayer props = ExtendedPlayer.get(player);
        for(Object o: getEntities(1, EntityAnimal.class,player)){
            if(o instanceof EntityAnimal){
                EntityAnimal entity = (EntityAnimal) o;
                try{
                    Field privateField = EntityAITempt.class.getDeclaredField("field_151484_k");
                    privateField.setAccessible(true);
                    List entries = entity.tasks.taskEntries;
                    for(int i = 0;i < entries.size();i++){
                        /*if(entries.get(i) == new EntityAITasks.EntityAITaskEntry(3,null)){

                        }*/
                    }
                    Item item = (Item) privateField.get(entity.tasks.taskEntries.get(3));
                    System.out.println(item.getUnlocalizedName());
                    if(player.getHeldItem().getItem() == item){
                        System.out.println("srabotalo");
                        props.setMobsFed(props.getMobsFed()+1);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    public boolean isHallow(ItemStack stack) {
        if(stack.getItem() == DeathHallowsMod.resurrectionStone || stack.getItem() == DeathHallowsMod.elderWand || stack.getItem() == DeathHallowsMod.invisibilityMantle)
            return true;
        else return false;
    }

    private void removeDuplicatesFromInventory(EntityPlayer player) {
        int count = 0;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            if(player.inventory.getStackInSlot(i) != null)
            {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if(isHallow(stack))
                {
                    if (!stack.hasTagCompound()) {
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setString("dhowner", player.getDisplayName());
                        stack.setTagCompound(nbt);
                    }
                    else if(stack.hasTagCompound() && !(stack.getTagCompound().hasKey("dhowner")))
                    {
                        NBTTagCompound nbt = stack.getTagCompound();
                        nbt.setString("dhowner", player.getDisplayName());
                        stack.setTagCompound(nbt);
                    }
                    else if (stack.hasTagCompound() && stack.getTagCompound().hasKey("dhowner")) {
                        String dhowner = stack.getTagCompound().getString("dhowner");
                        count++;
                        if(player.getDisplayName().equals(dhowner)){
                            if(count > 1)
                            {
                                player.inventory.setInventorySlotContents(i,null);
                                //player.entityDropItem(stack,1);
                                ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "dh.chat.dupe", new Object[0]);
                            }
                        } else {
                            player.inventory.setInventorySlotContents(i,null);
                            //player.entityDropItem(stack,1);
                        }
                    }
                }
            }
        }
        for (int i=1; i<4; i++) {
            if(BaublesApi.getBaubles(player).getStackInSlot(i) != null) {
                ItemStack stack = BaublesApi.getBaubles(player).getStackInSlot(i);
                if(isHallow(stack)){
                    if(stack.hasTagCompound() && stack.getTagCompound().hasKey("dhowner")){
                        count++;
                        if(!stack.getTagCompound().getString("dhowner").equals(player.getDisplayName())){
                            //player.entityDropItem(stack,1);
                            BaublesApi.getBaubles(player).setInventorySlotContents(i,null);
                        }
                        if(count > 1) {
                            //player.entityDropItem(stack,1);
                            BaublesApi.getBaubles(player).setInventorySlotContents(i,null);
                            ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "dh.chat.dupe", new Object[0]);
                        }
                    }
                }
            }
        }
    }

    public short totalLvl(EntityPlayer player){
        short totalLvl = 0;
        InventoryPlayer inventory = player.inventory;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);

            if (stack != null && stack.isItemEnchanted()) {
                NBTTagList enchantments = stack.getEnchantmentTagList();

                for (int j = 0; j < enchantments.tagCount(); j++) {
                    NBTTagCompound enchantment = (NBTTagCompound)enchantments.getCompoundTagAt(j);
                    short lvl = enchantment.getShort("lvl");
                    totalLvl += lvl;
                }
            }
        }
        return totalLvl;
    }


    private int findItemStackSlot(IInventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack existingStack = inventory.getStackInSlot(i);
            if (ItemStack.areItemStacksEqual(stack, existingStack)) {
                return i;
            }
        }
        return -1;
    }
    public List getEntities(double radius, Class target,EntityPlayer player){
        List entities = player.worldObj.getEntitiesWithinAABB(target, player.boundingBox.expand(radius, radius, radius));
        return entities;
    }
}
