package com.pyding.deathlyhallows.utils;

import com.pyding.deathlyhallows.items.food.ItemFoodBertieBotts;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class DHConfig {
	// TODO rework... This is no good.
	private static final String
			CATEGORY_STAFF = "staff",
			CATEGORY_RITUALS = "rituals",
			CATEGOFY_IDS = "IDs";

	public static Configuration config;
	public static String spawnlesia;
	public static int deathDifficulty;
	public static String spells;
	public static int[] elfQuestConditions = new int[] {20,1000,-128,1000,1000,64,20000,20000,100,1000};
	public static boolean hob;
	public static String despairSonataBlackList;
	public static int screamilyMana;
	public static int spawnlesiaMana;
	public static int bathHouseCost;
	public static int despairSonataCost;
	public static int fishCatchCost;
	public static int soulCurseCost;
	public static int iceCastleCost;
	public static int healMendingCost;
	public static int huntMagicCreaturesCost;
	public static int purifyCost;
	public static int covenWitchCost;
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
		elfQuestConditions = getIntArray("elfLvl", CATEGORY_STAFF, elfQuestConditions, "Change elf quest requirements, position of requirement corresponds to elf level");
		hob = config.getBoolean("hob", CATEGORY_STAFF, true, "Hobgoblin chains immortality");
		screamilyMana = config.getInt("screamilyMana", CATEGORY_STAFF, 10000, 1, Integer.MAX_VALUE, "Screamily mana per banshee scream");
		// why not?
		for(int i: getIntArray("bertieBottsEffectBlacklist", CATEGORY_STAFF, ItemFoodBertieBotts.getDefaultBlackList(), "Blacklisted Potion Effects")) {
			ItemFoodBertieBotts.addToBlackList(i);
		}
		// rituals
		// TODO why on the earth we need 2 configs, when we can just make cost like... -1 and that's it? 
		
		bathHouseCost = config.getInt("bathHouseCost", CATEGORY_RITUALS, 25000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		despairSonataCost = config.getInt("despairSonataCost", CATEGORY_RITUALS, 3000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		randomSpirits = config.getInt("despairSonataRandomSpirits", CATEGORY_RITUALS, 3, 1, Integer.MAX_VALUE, "Amount of random Spirits from Despair Sonata ritual");
		despairSonataBlackList = config.getString("despairSonataBlackList", CATEGORY_RITUALS, "", "Black list for ritual Sonat of Dispair, type names are separated with ',' for example: Zombie, EnderDragon ");
		fishCatchCost = config.getInt("fishCatchCost", CATEGORY_RITUALS, 1000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		soulCurseCost = config.getInt("soulCurseCost", CATEGORY_RITUALS, 50000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		iceCastleCost = config.getInt("iceCastleCost", CATEGORY_RITUALS, 7000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		healMendingCost = config.getInt("healMendingCost", CATEGORY_RITUALS, 18000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		huntMagicCreaturesCost = config.getInt("huntMagicCreaturesCost", CATEGORY_RITUALS, 30000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		purifyCost = config.getInt("purifyCost", CATEGORY_RITUALS, 24100, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		covenWitchCost = config.getInt("covenWitchCost", CATEGORY_RITUALS, 5000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		// ids
		DHID.FOCUSUPGRADE_EXTREMIS = config.getInt("FocusUpgradeType_Extremis_ID", CATEGOFY_IDS, DHID.FOCUSUPGRADE_EXTREMIS, 9, Integer.MAX_VALUE, "ID for Inferiois Mutandis Focus. Change if you have FocusUpgradeType ID related conflicts");
		config.save();
	}
	
	private static int[] getIntArray(String name, String category, int[] defaultValue, String comment) {
		Property p = config.get(category, name, defaultValue);
		p.comment = comment;
		return p.getIntList();
	}


	public static int getElfRequirements(int level) {
		return elfQuestConditions[MathHelper.clamp_int(level - 1,0, elfQuestConditions.length - 1)];
	}
}
