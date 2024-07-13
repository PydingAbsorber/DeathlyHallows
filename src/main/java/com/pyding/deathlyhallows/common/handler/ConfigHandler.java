package com.pyding.deathlyhallows.common.handler;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {
	public static Configuration config;
	public static String spawnlesia;
	public static int deathDifficulty;
	public static String spells;
	public static String elfLvl;
	public static boolean hob;

	public static int screamilyMana;

	public static int spawnlesiaMana;

	public ConfigHandler() {
	}

	public static void init(File file) {
		config = new Configuration(file);
		syncConfig();
	}

	public static void load() {
		config.load();
	}

	public static void syncConfig() {
		config.addCustomCategoryComment("staff", "Deathly Hallows config");
		spells = config.getString("spells", "staff", "", "Black list for Spells Id in Elder Wand, for example: 36, 37, 42 ");
		spawnlesia = config.getString("spawnlesia", "staff", "", "Black list for Spawnlesia, type names are separated with ',' for example: Zombie, EnderDragon ");
		spawnlesiaMana = config.getInt("spawnlesiaMana", "staff", 1000000, 1, Integer.MAX_VALUE, "Mana requirements for Spawnlesia per spawn");
		deathDifficulty = config.getInt("deathDifficulty", "staff", 3, 1, 3, "Absolute Death difficulty (3 - Gigachad Man, 2 - weak casual, 1 - newborn toddler)");
		elfLvl = config.getString("elfLvl", "staff", "1-20,2-1000,3--1000,4-1000,5-1000,6-64,7-20000,8-20000,9-100,10-1000", "Change this numbers for elf lvl requirements (First number is elf lvl you want)");
		hob = config.getBoolean("hob", "staff", true, "Hobgoblin chains immortality");
		screamilyMana = config.getInt("screamilyMana", "staff", 10000, 1, Integer.MAX_VALUE, "Screamily mana per banshee scream");
		config.save();
	}


	public static int getElfRequirements(int lvl) {
		for(String jopa: elfLvl.split(",")) {
			if(jopa.contains(lvl + "-")) {
				StringBuilder text = new StringBuilder(jopa);
				text.deleteCharAt(0);
				text.deleteCharAt(0);
				return Integer.valueOf(String.valueOf(text));
			}
		}
		return 0;
	}
}
