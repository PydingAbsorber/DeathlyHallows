package com.pyding.deathlyhallows.utils.properties;

import com.emoniph.witchery.util.EntityUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.ArrayList;
import java.util.List;

public class ExtendedPlayer implements IExtendedEntityProperties {
	public final static String EXT_PROP_NAME = "DeathlyHallows";
	private final EntityPlayer player;
	private EntityPlayer source;
	private double x, y, z;
	private int dimension;
	private int
			elfLvl,
			elfCount,
			trigger,
			currentDuration,
			mobsKilled,
			spellsUsed,
			foodEaten,
			page,
			mobsFed,
			cursed;
	private boolean
			damageLog,
			choice,
			avenger;
	private String monsters = "";

	// unsaved
	public int
			elfTimeSurvived,
			elfInfusionTimer;


	private final List<String> foodCollection = new ArrayList<>();

	public ExtendedPlayer(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public void init(Entity entity, World world) {
	}

	public static void register(EntityPlayer player) {
		player.registerExtendedProperties(ExtendedPlayer.EXT_PROP_NAME, new ExtendedPlayer(player));
	}

	public static ExtendedPlayer get(EntityPlayer player) {
		return (ExtendedPlayer)player.getExtendedProperties(EXT_PROP_NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = new NBTTagCompound();
		properties.setInteger("CurrentDuration", currentDuration);
		properties.setDouble("X", x);
		properties.setDouble("Y", y);
		properties.setDouble("Z", z);
		properties.setInteger("Dimension", dimension);
		properties.setInteger("ElfLvl", elfLvl);
		properties.setInteger("Trigger", trigger);
		properties.setInteger("ElfCount", elfCount);
		properties.setInteger("MobsKilled", mobsKilled);
		properties.setInteger("SpellsUsed", spellsUsed);
		properties.setInteger("FoodEaten", foodEaten);
		properties.setBoolean("Choice", choice);
		properties.setInteger("MobsFed", mobsFed);
		properties.setString("DHMonsters", monsters);
		properties.setBoolean("Logs", damageLog);
		properties.setInteger("Page", page);
		properties.setBoolean("Avenger", avenger);
		properties.setInteger("Cursed", cursed);
		properties.setInteger("DHFoodSize", foodCollection.size());
		int i = 0;
		for(String food : foodCollection) {
			properties.setString("DHFood" + i++, food);
		}
		compound.setTag(EXT_PROP_NAME, properties);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		if(compound.hasKey(EXT_PROP_NAME)) {
			NBTTagCompound properties = (NBTTagCompound)compound.getTag(EXT_PROP_NAME);
			currentDuration = properties.getInteger("CurrentDuration");
			x = properties.getDouble("X");
			y = properties.getDouble("Y");
			z = properties.getDouble("Z");
			dimension = properties.getInteger("Dimension");
			elfLvl = properties.getInteger("ElfLvl");
			trigger = properties.getInteger("Trigger");
			elfCount = properties.getInteger("ElfCount");
			mobsKilled = properties.getInteger("MobsKilled");
			spellsUsed = properties.getInteger("SpellsUsed");
			foodEaten = properties.getInteger("FoodEaten");
			choice = properties.getBoolean("Choice");
			mobsFed = properties.getInteger("MobsFed");
			damageLog = properties.getBoolean("Logs");
			monsters = properties.getString("DHMonsters");
			page = properties.getInteger("Page");
			avenger = properties.getBoolean("Avenger");
			cursed = properties.getInteger("Cursed");
			for(int i = 0; i < properties.getInteger("DHFoodSize"); ++i) {
				foodCollection.add(i, properties.getString("DHFood" + i));
			}
		}
	}

	public void setAllNull() {
		trigger = 0;
		elfCount = 0;
		mobsKilled = 0;
		spellsUsed = 0;
		foodEaten = 0;
		foodCollection.clear();
	}
	
	public List<String> getFoodCollection() {
		return foodCollection;
	}

	public void addFoodToCollection(String name) {
		for(String s: foodCollection) {
			if(s.equals(name)) {
				return;
			}
		}
		foodCollection.add(name);
	}

	public void setCursed(int number) {
		cursed = number;
	}

	public int getCursed() {
		return cursed;
	}

	// TODO consider moving to DHUtils becouse there is nothing to do with props
	public void deadInside(EntityLivingBase target) { // FOX DIE!
		if(target instanceof EntityPlayer && ((EntityPlayer)target).capabilities.isCreativeMode) {
			return;
		}
		if(source == null) {
			EntityUtil.instantDeath(target, player);
			return;
		}
		EntityUtil.instantDeath(target, source);
	}

	public boolean getDamageLog() {
		return damageLog;
	}

	public void setDamageLog(boolean val) {
		damageLog = val;
	}

	public int getMonstersCount() {
		if(monsters == null) {
			return 0;
		}
		int count = 0;
		for(String ignored: monsters.split(",")) {
			count++;
		}
		return count;
	}

	public void addMonster(String name) {
		if(!monsters.contains(name)) {
			monsters += name + ",";
		}
	}

	public void setAvenger(boolean value) {
		avenger = value;
	}

	public boolean getAvenger() {
		return avenger;
	}

	public int getMobsFed() {
		return mobsFed;
	}

	public void setMobsFed(int count) {
		mobsFed = count;
	}

	public boolean getChoice() {
		return choice;
	}

	public void setChoice(boolean val) {
		choice = val;
	}

	public int getFoodEaten() {
		return foodEaten;
	}

	public void setFoodEaten(int count) {
		foodEaten = count;
	}

	public int getMobsKilled() {
		return mobsKilled;
	}

	public void setMobsKilled(int count) {
		mobsKilled = count;
	}

	public int getSpellsUsed() {
		return spellsUsed;
	}

	public void setSpellsUsed(int count) {
		spellsUsed = count;
	}

	public void setCurrentDuration(int duration) {
		currentDuration = duration;
	}

	public void setSource(EntityPlayer source) {
		this.source = source;
	}

	public void lowerDuration() {
		if(currentDuration > 0) {
			currentDuration = currentDuration - 1;
		}
	}

	public int getCurrentDuration() {
		return currentDuration;
	}

	public EntityPlayer getSource() {
		return source;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public int getDimension() {
		return dimension;
	}

	public void setCoordinates(double x, double y, double z, int dimension) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dimension;
	}

	public int getElfLvl() {
		return elfLvl;
	}

	Multimap<String, AttributeModifier> attributes = HashMultimap.create();
	public float hpPerLvl = 4;

	public void increaseElfLvl() {
		++elfLvl;
		attributes.clear();
		attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("DH HP", hpPerLvl, 0));
		player.getAttributeMap().applyAttributeModifiers(attributes);
	}

	public void maxElfLvl() {
		attributes.clear();
		attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("DH HP", hpPerLvl * 10, 0));
		player.getAttributeMap().applyAttributeModifiers(attributes);
		elfLvl = 10;
	}

	public void decreaseElfLvl() {
		--elfLvl;
		attributes.clear();
		attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("DH HP", -hpPerLvl, 0));
		player.getAttributeMap().applyAttributeModifiers(attributes);
	}

	public void nullifyElfLvl() {
		attributes.clear();
		attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("DH HP", -elfLvl * hpPerLvl, 0));
		player.getAttributeMap().applyAttributeModifiers(attributes);
		elfLvl = 0;
		setAllNull();
	}

	public int getTrigger() {
		return trigger;
	}

	public void setTrigger(int number) {
		trigger = number;
	}

	public int getElfCount() {
		return elfCount;
	}

	public void setElfCount(int number) {
		elfCount = number;
	}
}
