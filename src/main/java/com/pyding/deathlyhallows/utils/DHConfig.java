package com.pyding.deathlyhallows.utils;

import com.pyding.deathlyhallows.items.ItemFoodBertieBotts;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class DHConfig {

	private static final String
			CATEGORY_STAFF = "staff",
			CATEGORY_ELF = "elf";

	public static Configuration config;
	public static String spawnlesia;
	public static int deathDifficulty;
	public static String spells;
	public static String elfLvl;
	public static boolean hob;
	public static String sonatRitual;
	public static int screamilyMana;
	public static int spawnlesiaMana;
	public static boolean bathHouse;
	public static int cost1;
	public static boolean despairSonata;
	public static int cost2;
	public static boolean fishCatch;
	public static int cost3;
	public static boolean soulCurse;
	public static int cost4;
	public static boolean iceCastle;
	public static int cost5;
	public static boolean healMending;
	public static int cost6;
	public static boolean huntMagicCreatures;
	public static int cost7;
	public static boolean purify;
	public static int cost8;
	public static boolean covenWitch;
	public static int cost9;
	public static int randomSpirits;
	public static boolean shouldRemove;

	public DHConfig() {
	}

	public static void init(File file) {
		config = new Configuration(file);
		syncConfig();
	}

	public static void load() {
		config.load();
	}

	public static void syncConfig() {
		config.addCustomCategoryComment(CATEGORY_STAFF, "Deathly Hallows config");
		shouldRemove = config.getBoolean("removeDuplicated", CATEGORY_STAFF, true, "Should have only one hallow");

		spells = config.getString("spells", CATEGORY_STAFF, "", "Black list for Spells Id in Elder Wand, for example: 36, 37, 42 ");
		spawnlesia = config.getString("spawnlesia", CATEGORY_STAFF, "", "Black list for Spawnlesia, type names are separated with ',' for example: Zombie, EnderDragon ");
		spawnlesiaMana = config.getInt("spawnlesiaMana", CATEGORY_STAFF, 1000000, 1, Integer.MAX_VALUE, "Mana requirements for Spawnlesia per spawn");
		deathDifficulty = config.getInt("deathDifficulty", CATEGORY_STAFF, 3, 1, 3, "Absolute Death difficulty (3 - Gigachad Man, 2 - weak casual, 1 - newborn toddler)");
		elfLvl = config.getString("elfLvl", CATEGORY_STAFF, "1-20,2-1000,3--1000,4-1000,5-1000,6-64,7-20000,8-20000,9-100,10-1000", "Change this numbers for elf level requirements (First number is elf level you want)");
		hob = config.getBoolean("hob", CATEGORY_STAFF, true, "Hobgoblin chains immortality");
		screamilyMana = config.getInt("screamilyMana", CATEGORY_STAFF, 10000, 1, Integer.MAX_VALUE, "Screamily mana per banshee scream");
		sonatRitual = config.getString("sonatRitual", CATEGORY_STAFF, "", "Black list for ritual Sonat of Dispair, type names are separated with ',' for example: Zombie, EnderDragon ");

		bathHouse = config.getBoolean("bathHouse", CATEGORY_STAFF, true, "Should this ritual be enabled");
		cost1 = config.getInt("cost1", CATEGORY_STAFF, 25000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual");
		despairSonata = config.getBoolean("despairSonata", CATEGORY_STAFF, true, "Should this ritual be enabled");
		cost2 = config.getInt("cost2", CATEGORY_STAFF, 3000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual");
		randomSpirits = config.getInt("randomSpirits", CATEGORY_STAFF, 3, 1, Integer.MAX_VALUE, "Amount of random Spirits from Despair Sonata ritual");
		fishCatch = config.getBoolean("fishCatch", CATEGORY_STAFF, true, "Should this ritual be enabled");
		cost3 = config.getInt("cost3", CATEGORY_STAFF, 1000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual");
		soulCurse = config.getBoolean("soulCurse", CATEGORY_STAFF, true, "Should this ritual be enabled");
		cost4 = config.getInt("cost4", CATEGORY_STAFF, 50000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual");
		iceCastle = config.getBoolean("iceCastle", CATEGORY_STAFF, true, "Should this ritual be enabled");
		cost5 = config.getInt("cost5", CATEGORY_STAFF, 7000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual");
		healMending = config.getBoolean("healMending", CATEGORY_STAFF, true, "Should this ritual be enabled");
		cost6 = config.getInt("cost6", CATEGORY_STAFF, 18000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual");
		huntMagicCreatures = config.getBoolean("huntMagicCreatures", CATEGORY_STAFF, true, "Should this ritual be enabled");
		cost7 = config.getInt("cost7", CATEGORY_STAFF, 30000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual");
		purify = config.getBoolean("purify", CATEGORY_STAFF, true, "Should this ritual be enabled");
		cost8 = config.getInt("cost8", CATEGORY_STAFF, 24100, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual");
		covenWitch = config.getBoolean("covenWitch", CATEGORY_STAFF, true, "Should this ritual be enabled");
		cost9 = config.getInt("cost9", CATEGORY_STAFF, 5000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual");
		Property p = config.get(CATEGORY_STAFF, "bertieBottsEffectBlacklist", ItemFoodBertieBotts.getDefaultBlackList());
		p.comment = "Blacklisted Potion Effects";
		for(int i: p.getIntList()) {
			ItemFoodBertieBotts.addToBlackList(i);
		}
		config.save();
	}


	public static int getElfRequirements(int level) {
		for(String jopa: elfLvl.split(",")) {
			if(!jopa.contains(level + "-")) {
				continue;
			}
			StringBuilder text = new StringBuilder(jopa);
			text.deleteCharAt(0);
			text.deleteCharAt(0);
			return Integer.parseInt(String.valueOf(text));
		}
		return 0;
	}
}
