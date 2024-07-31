package com.pyding.deathlyhallows.utils.properties;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketPropertiesSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.ArrayList;
import java.util.List;

public class DeathlyProperties implements IExtendedEntityProperties {
	public final static String NAME = "DeathlyHallows";
	private final EntityPlayer player;
	private EntityPlayer source;
	private double x, y, z;
	private int dimension;
	private int
			elfLevel,
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

	public DeathlyProperties(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public void init(Entity entity, World world) {
	}

	public static void register(EntityPlayer player) {
		player.registerExtendedProperties(DeathlyProperties.NAME, new DeathlyProperties(player));
	}

	public static DeathlyProperties get(EntityPlayer player) {
		return (DeathlyProperties)player.getExtendedProperties(NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound tag) {
		NBTTagCompound subtag = new NBTTagCompound();
		subtag.setInteger("CurrentDuration", currentDuration);
		subtag.setDouble("X", x);
		subtag.setDouble("Y", y);
		subtag.setDouble("Z", z);
		subtag.setInteger("Dimension", dimension);
		subtag.setInteger("ElfLvl", elfLevel);
		subtag.setInteger("Trigger", trigger);
		subtag.setInteger("ElfCount", elfCount);
		subtag.setInteger("MobsKilled", mobsKilled);
		subtag.setInteger("SpellsUsed", spellsUsed);
		subtag.setInteger("FoodEaten", foodEaten);
		subtag.setBoolean("Choice", choice);
		subtag.setInteger("MobsFed", mobsFed);
		subtag.setString("DHMonsters", monsters);
		subtag.setBoolean("Logs", damageLog);
		subtag.setInteger("Page", page);
		subtag.setBoolean("Avenger", avenger);
		subtag.setInteger("Cursed", cursed);
		subtag.setInteger("DHFoodSize", foodCollection.size());
		int i = 0;
		for(String food: foodCollection) {
			subtag.setString("DHFood" + i++, food);
		}
		tag.setTag(NAME, subtag);
	}

	@Override
	public void loadNBTData(NBTTagCompound tag) {
		if(!tag.hasKey(NAME)) {
			return;
		}
		NBTTagCompound subtag = (NBTTagCompound)tag.getTag(NAME);
		currentDuration = subtag.getInteger("CurrentDuration");
		x = subtag.getDouble("X");
		y = subtag.getDouble("Y");
		z = subtag.getDouble("Z");
		dimension = subtag.getInteger("Dimension");
		elfLevel = subtag.getInteger("ElfLvl");
		trigger = subtag.getInteger("Trigger");
		elfCount = subtag.getInteger("ElfCount");
		mobsKilled = subtag.getInteger("MobsKilled");
		spellsUsed = subtag.getInteger("SpellsUsed");
		foodEaten = subtag.getInteger("FoodEaten");
		choice = subtag.getBoolean("Choice");
		mobsFed = subtag.getInteger("MobsFed");
		damageLog = subtag.getBoolean("Logs");
		monsters = subtag.getString("DHMonsters");
		page = subtag.getInteger("Page");
		avenger = subtag.getBoolean("Avenger");
		cursed = subtag.getInteger("Cursed");
		for(int i = 0; i < subtag.getInteger("DHFoodSize"); ++i) {
			foodCollection.add(i, subtag.getString("DHFood" + i));
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

	Multimap<String, AttributeModifier> attributes = HashMultimap.create();
	private static final float HP_PER_ELF_LEVEL = 4;
	public static final int MAX_ELF_LEVEL = 10;
	
	public int getElfLevel() {
		return elfLevel;
	}

	public void setElfLevel(int level) {
		level = Math.max(0, Math.min(level, MAX_ELF_LEVEL));
		int diff = level - elfLevel;
		elfLevel = level;
		attributes.clear();
		attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("DH Elf HP Boost", diff * HP_PER_ELF_LEVEL, 0));
		player.getAttributeMap().applyAttributeModifiers(attributes);
		if(elfLevel == 0) {
			setAllNull();
		}
		DHPacketProcessor.sendToPlayer(new PacketPropertiesSync(player, NAME), player);
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
