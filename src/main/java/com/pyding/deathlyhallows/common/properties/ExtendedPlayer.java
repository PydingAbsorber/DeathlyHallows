package com.pyding.deathlyhallows.common.properties;

import com.emoniph.witchery.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedPlayer implements IExtendedEntityProperties
{
    public final static String EXT_PROP_NAME = "DeathlyHallows";
    private final EntityPlayer player;
    private int currentDuration;
    private EntityPlayer source;

    private double x;

    private double y;
    private double z;
    private int dimension;
    private int elfLvl;

    private int trigger;
    private int elfCount;
    private int mobsKilled;
    private int spellsUsed;
    private int foodEaten;
    private boolean choice;

    private int mobsFed;
    private boolean damageLog;
    public ExtendedPlayer(EntityPlayer player)
    {
        this.player = player;
    }

    @Override
    public void init(Entity entity, World world) {
    }

    public static final void register(EntityPlayer player)
    {
        player.registerExtendedProperties(ExtendedPlayer.EXT_PROP_NAME, new ExtendedPlayer(player));
    }

    public static final ExtendedPlayer get(EntityPlayer player)
    {
        return (ExtendedPlayer) player.getExtendedProperties(EXT_PROP_NAME);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound)
    {
        NBTTagCompound properties = new NBTTagCompound();
        properties.setInteger("CurrentDuration", this.currentDuration);
        properties.setDouble("X",this.x);
        properties.setDouble("Y",this.y);
        properties.setDouble("Z",this.z);
        properties.setInteger("Dimension",this.dimension);
        properties.setInteger("ElfLvl",this.elfLvl);
        properties.setInteger("Trigger",this.trigger);
        properties.setInteger("ElfCount",this.elfCount);
        properties.setInteger("MobsKilled",this.mobsKilled);
        properties.setInteger("SpellsUsed",this.spellsUsed);
        properties.setInteger("FoodEaten",this.foodEaten);
        properties.setBoolean("Choice",this.choice);
        properties.setInteger("MobsFed",this.mobsFed);
        properties.setBoolean("Logs",this.damageLog);
        compound.setTag(EXT_PROP_NAME, properties);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound)
    {
        if(compound.hasKey(EXT_PROP_NAME)){
            NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
            this.currentDuration = properties.getInteger("CurrentDuration");
            this.x = properties.getDouble("X");
            this.y = properties.getDouble("Y");
            this.z = properties.getDouble("Z");
            this.dimension = properties.getInteger("Dimension");
            this.elfLvl = properties.getInteger("ElfLvl");
            this.trigger = properties.getInteger("Trigger");
            this.elfCount = properties.getInteger("ElfCount");
            this.mobsKilled = properties.getInteger("MobsKilled");
            this.spellsUsed = properties.getInteger("SpellsUsed");
            this.foodEaten = properties.getInteger("FoodEaten");
            this.choice = properties.getBoolean("Choice");
            this.mobsFed = properties.getInteger("MobsFed");
            this.damageLog = properties.getBoolean("Logs");
        }
    }
    public void setAllNull(){
        this.trigger = 0;
        this.elfCount = 0;
        this.mobsKilled = 0;
        this.spellsUsed = 0;
        this.foodEaten = 0;
    }
    public void sync() {

    }

    public void deadInside(EntityLivingBase target) {
        if(target instanceof EntityPlayer) {
            EntityPlayer playerTarget = (EntityPlayer) target;
            if(playerTarget.capabilities.isCreativeMode)
                return;
        }
        if(source != null)
        EntityUtil.instantDeath(target,source);
        else EntityUtil.instantDeath(target,player);
    }
    public boolean getDamageLog(){
        return this.damageLog;
    }
    public void setDamageLog(boolean choice2){
        this.damageLog = choice2;
    }
    public int getMobsFed(){
        return mobsFed;
    }
    public void setMobsFed(int count){
        mobsFed = count;
    }
    public boolean getChoice(){
        return this.choice;
    }
    public void setChoice(boolean choice1){
        this.choice = choice1;
    }
    public int getFoodEaten(){
        return foodEaten;
    }
    public void setFoodEaten(int count){
        foodEaten = count;
    }

    public int getMobsKilled(){
        return mobsKilled;
    }
    public void setMobsKilled(int count){
        mobsKilled = count;
    }

    public int getSpellsUsed(){
        return spellsUsed;
    }
    public void setSpellsUsed(int count){
        mobsKilled = count;
    }
    public void setCurrentDuration(int duration) {
        this.currentDuration = duration;
    }

    public void setSource(EntityPlayer source) {
        this.source = source;
    }

    public void lowerDuration() {
        if(this.currentDuration > 0 )
            this.currentDuration = this.currentDuration-1;
    }

    public int getCurrentDuration() {
        return this.currentDuration;
    }

    public EntityPlayer getSource() {
        return this.source;
    }

    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public double getZ() {
        return this.z;
    }
    public int getDimension() {
        return this.dimension;
    }

    public void setCoordinates(double x,double y,double z,int dimention){
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimention;
    }

    public int getElfLvl(){
        return this.elfLvl;
    }

    public void setElfLvl(int lvl) {
        this.elfLvl = lvl;
        this.sync();
    }

    public int getTrigger() {return this.trigger;}
    public void setTrigger(int number) {
        this.trigger = number;
        this.sync();
    }

    public int getElfCount() {return this.elfCount;}
    public void setElfCount(int number) {
        this.elfCount = number;
        this.sync();
    }
}
