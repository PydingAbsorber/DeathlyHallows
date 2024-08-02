package com.pyding.deathlyhallows.utils.properties;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketPropertiesToClient;
import com.pyding.deathlyhallows.network.packets.PacketPropertiesToServer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
			cursed,
			niceCream;
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
		if(player.worldObj.isRemote) {
			syncToServer();
		}
	}

	public static void register(EntityPlayer player) {
		player.registerExtendedProperties(DeathlyProperties.NAME, new DeathlyProperties(player));
	}

	public static DeathlyProperties get(EntityPlayer player) {
		return (DeathlyProperties)player.getExtendedProperties(NAME);
	}
	
	public static void copy(EntityPlayer from, EntityPlayer to) {
		NBTTagCompound tag = new NBTTagCompound();
		get(from).saveNBTData(tag);
		get(to).loadNBTData(tag);
	}

	@Override
	public void saveNBTData(NBTTagCompound entityTag) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("CurrentDuration", currentDuration);
		tag.setDouble("X", x);
		tag.setDouble("Y", y);
		tag.setDouble("Z", z);
		tag.setInteger("Dimension", dimension);
		tag.setInteger("ElfLvl", elfLevel);
		tag.setInteger("Trigger", trigger);
		tag.setInteger("ElfCount", elfCount);
		tag.setInteger("MobsKilled", mobsKilled);
		tag.setInteger("SpellsUsed", spellsUsed);
		tag.setInteger("FoodEaten", foodEaten);
		tag.setBoolean("Choice", choice);
		tag.setInteger("MobsFed", mobsFed);
		tag.setString("DHMonsters", monsters);
		tag.setBoolean("Logs", damageLog);
		tag.setInteger("Page", page);
		tag.setBoolean("Avenger", avenger);
		tag.setInteger("Cursed", cursed);
		tag.setInteger("DHFoodSize", foodCollection.size());
		tag.setInteger("DHNiceCream", niceCream);
		int i = 0;
		for(String food: foodCollection) {
			tag.setString("DHFood" + i++, food);
		}
		entityTag.setTag(NAME, tag);
	}

	@Override
	public void loadNBTData(NBTTagCompound entityTag) {
		if(!entityTag.hasKey(NAME)) {
			return;
		}
		NBTTagCompound tag = (NBTTagCompound)entityTag.getTag(NAME);
		currentDuration = tag.getInteger("CurrentDuration");
		x = tag.getDouble("X");
		y = tag.getDouble("Y");
		z = tag.getDouble("Z");
		dimension = tag.getInteger("Dimension");
		elfLevel = tag.getInteger("ElfLvl");
		trigger = tag.getInteger("Trigger");
		elfCount = tag.getInteger("ElfCount");
		mobsKilled = tag.getInteger("MobsKilled");
		spellsUsed = tag.getInteger("SpellsUsed");
		foodEaten = tag.getInteger("FoodEaten");
		choice = tag.getBoolean("Choice");
		mobsFed = tag.getInteger("MobsFed");
		damageLog = tag.getBoolean("Logs");
		monsters = tag.getString("DHMonsters");
		page = tag.getInteger("Page");
		avenger = tag.getBoolean("Avenger");
		cursed = tag.getInteger("Cursed");
		niceCream = tag.getInteger("DHNiceCream");
		for(int i = 0; i < tag.getInteger("DHFoodSize"); ++i) {
			foodCollection.add(i, tag.getString("DHFood" + i));
		}
	}

	public void syncToClient() {
		if(player.worldObj.isRemote) {
			return;
		}
		DHPacketProcessor.sendToPlayer(new PacketPropertiesToClient(player, DeathlyProperties.NAME), player);
	}

	@SideOnly(Side.CLIENT)
	public void syncToServer() {
		DHPacketProcessor.sendToServer(new PacketPropertiesToServer(DeathlyProperties.NAME));
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
	
	public void setNiceCream(int count){
		niceCream = count;
	}
	
	public int getNiceCream(){
		return niceCream;
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
		syncToClient();
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
