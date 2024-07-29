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
	public int page;
	private boolean choice;

	private int mobsFed;
	private boolean damageLog;
	private String monsters = "";
	private boolean avenger;
	private int cursed;

	private final List foodCollection = new ArrayList<>();

	public ExtendedPlayer(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public void init(Entity entity, World world) {
	}

	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties(ExtendedPlayer.EXT_PROP_NAME, new ExtendedPlayer(player));
	}

	public static final ExtendedPlayer get(EntityPlayer player) {
		return (ExtendedPlayer)player.getExtendedProperties(EXT_PROP_NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = new NBTTagCompound();
		properties.setInteger("CurrentDuration", this.currentDuration);
		properties.setDouble("X", this.x);
		properties.setDouble("Y", this.y);
		properties.setDouble("Z", this.z);
		properties.setInteger("Dimension", this.dimension);
		properties.setInteger("ElfLvl", this.elfLvl);
		properties.setInteger("Trigger", this.trigger);
		properties.setInteger("ElfCount", this.elfCount);
		properties.setInteger("MobsKilled", this.mobsKilled);
		properties.setInteger("SpellsUsed", this.spellsUsed);
		properties.setInteger("FoodEaten", this.foodEaten);
		properties.setBoolean("Choice", this.choice);
		properties.setInteger("MobsFed", this.mobsFed);
		properties.setString("DHMonsters", this.monsters);
		properties.setBoolean("Logs", this.damageLog);
		properties.setInteger("Page", this.page);
		properties.setBoolean("Avenger", this.avenger);
		properties.setInteger("Cursed", this.cursed);
		for(int i = 0; i < foodCollection.size(); i++) {
			properties.setString("DHFood" + i, foodCollection.get(i).toString());
			properties.setInteger("DHFoodSize", i);
		}
		compound.setTag(EXT_PROP_NAME, properties);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		if(compound.hasKey(EXT_PROP_NAME)) {
			NBTTagCompound properties = (NBTTagCompound)compound.getTag(EXT_PROP_NAME);
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
			this.monsters = properties.getString("DHMonsters");
			this.page = properties.getInteger("Page");
			this.avenger = properties.getBoolean("Avenger");
			this.cursed = properties.getInteger("Cursed");
			for(int i = 0; i < properties.getInteger("DHFoodSize"); i++) {
				foodCollection.add(i, properties.getString("DHFood" + i));
			}
		}
	}

	public void setAllNull() {
		this.trigger = 0;
		this.elfCount = 0;
		this.mobsKilled = 0;
		this.spellsUsed = 0;
		this.foodEaten = 0;
		this.foodCollection.clear();
	}

	public void sync() {

	}

	public List getFoodCollection() {
		return foodCollection;
	}

	public void addFoodToCollection(String name) {
		for(int i = 0; i < foodCollection.size(); i++) {
			if(foodCollection.get(i).toString().equals(name)) {
				return;
			}
		}
		foodCollection.add(name);
	}

	public void setCursed(int number) {
		this.cursed = number;
	}

	public int getCursed() {
		return this.cursed;
	}

	public void deadInside(EntityLivingBase target) {
		if(target instanceof EntityPlayer) {
			EntityPlayer playerTarget = (EntityPlayer)target;
			if(playerTarget.capabilities.isCreativeMode) {
				return;
			}
		}
		if(source != null) {
			EntityUtil.instantDeath(target, source);
		}
		else {
			EntityUtil.instantDeath(target, player);
		}
	}

	public boolean getDamageLog() {
		return this.damageLog;
	}

	public void setDamageLog(boolean choice2) {
		this.damageLog = choice2;
	}

	public int getMonstersCount() {
		int count = 0;
		if(this.monsters != null) {
			for(String monster: this.monsters.split(",")) {
				count++;
			}
		}
		return count;
	}

	public void addMonster(String name) {
		if(!this.monsters.contains(name)) {
			this.monsters += name + ",";
		}
	}

	public void setAvenger(boolean value) {
		this.avenger = value;
	}

	public boolean getAvenger() {
		return this.avenger;
	}

	public int getMobsFed() {
		return mobsFed;
	}

	public void setMobsFed(int count) {
		mobsFed = count;
	}

	public boolean getChoice() {
		return this.choice;
	}

	public void setChoice(boolean choice1) {
		this.choice = choice1;
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
		this.currentDuration = duration;
	}

	public void setSource(EntityPlayer source) {
		this.source = source;
	}

	public void lowerDuration() {
		if(this.currentDuration > 0) {
			this.currentDuration = this.currentDuration - 1;
		}
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

	public void setCoordinates(double x, double y, double z, int dimention) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dimention;
	}

	public int getElfLvl() {
		return this.elfLvl;
	}

	Multimap<String, AttributeModifier> attributes = HashMultimap.create();
	public float hpPerLvl = 4;

	public void increaseElfLvl() {
		this.elfLvl = elfLvl + 1;
		attributes.clear();
		attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("DH HP", hpPerLvl, 0));
		player.getAttributeMap().applyAttributeModifiers(attributes);
	}

	public void maxElfLvl() {
		attributes.clear();
		attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("DH HP", hpPerLvl * 10, 0));
		player.getAttributeMap().applyAttributeModifiers(attributes);
		this.elfLvl = 10;
	}

	public void decreaseElfLvl() {
		this.elfLvl = elfLvl - 1;
		attributes.clear();
		attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("DH HP", -hpPerLvl, 0));
		player.getAttributeMap().applyAttributeModifiers(attributes);
	}

	public void nullifyElfLvl() {
		attributes.clear();
		attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("DH HP", -this.elfLvl * hpPerLvl, 0));
		player.getAttributeMap().applyAttributeModifiers(attributes);
		this.elfLvl = 0;
		setAllNull();
	}

	public int getTrigger() {
		return this.trigger;
	}

	public void setTrigger(int number) {
		this.trigger = number;
	}

	public int getElfCount() {
		return this.elfCount;
	}

	public void setElfCount(int number) {
		this.elfCount = number;
	}
}
