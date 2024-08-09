package com.pyding.deathlyhallows.utils;

import com.pyding.deathlyhallows.items.food.ItemFoodBertieBotts;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class DHConfig {

	private static final String
			CATEGORY_STAFF = "staff",
			CATEGORY_RITUALS = "rituals",
			CATEGORY_IDS = "IDs";

	public static Configuration config;
	public static int deathDifficulty = 3;
	public static int elderWandMaxSpells = 10;
	public static String spells = "";
	public static boolean shouldRemove = true;
	public static int[] elfQuestConditions = new int[]{20, 20, -100, 100, 50, 30, 20000, 35000, 100, 1000};
	public static boolean hob = true;
	public static int
			screamilyMana = 10000,
			spawnlesiaMana = 1000000,
			spawnlesiaCd = 10000;
	public static String spawnlesia;
	public static int
			bathHouseCost = 25000,
			despairSonataCost = 3000,
			fishCatchCost = 1000,
			soulCurseCost = 50000,
			iceCastleCost = 7000,
			healMendingCost = 18000,
			huntMagicCreaturesCost = 30000,
			purifyCost = 24100,
			covenWitchCost = 5000,
			arrowCost,
			betterArrowCost;
	public static String despairSonataBlackList = "";
	public static int randomSpirits = 3;

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
		config.addCustomCategoryComment(CATEGORY_STAFF, "Deathly Hallows config.");
		shouldRemove = config.getBoolean("removeDuplicated", CATEGORY_STAFF, shouldRemove, "Should have only one hallow or more?");
		elderWandMaxSpells = config.getInt("elderWandMaxSpells", CATEGORY_STAFF, elderWandMaxSpells, 1, 12, "How many spells can Elder Wand hold at once?");
		// TODO int array
		spells = config.getString("spells", CATEGORY_STAFF, spells, "Black list for Spells Id in Elder Wand, for example: 36, 37, 42.");
		// TODO int array
		spawnlesia = config.getString("spawnlesia", CATEGORY_STAFF, "", "Black list for Spawnlesia, type names are separated with ',' for example: Zombie, EnderDragon.");
		spawnlesiaMana = config.getInt("spawnlesiaMana", CATEGORY_STAFF, spawnlesiaMana, 1, Integer.MAX_VALUE, "Mana requirements for Spawnlesia per spawn.");
		spawnlesiaCd = config.getInt("spawnlesiaCd", CATEGORY_STAFF, spawnlesiaCd, 1, Integer.MAX_VALUE, "Cooldown for Spawnlesia in long. 1000 is 1 sec.");

		deathDifficulty = config.getInt("deathDifficulty", CATEGORY_STAFF, deathDifficulty, 1, 3, "Absolute Death difficulty (3 - Gigachad Man, 2 - weak casual, 1 - newborn toddler).");
		elfQuestConditions = getIntArray("elfLvl", CATEGORY_STAFF, elfQuestConditions, "Change elf quest requirements, position of requirement corresponds to elf level. Last one is not a requirement but a maximum.");
		hob = config.getBoolean("hob", CATEGORY_STAFF, true, "Hobgoblin chains immortality");
		screamilyMana = config.getInt("screamilyMana", CATEGORY_STAFF, 10000, 1, Integer.MAX_VALUE, "Screamily mana per banshee scream.");
		arrowCost = config.getInt("arrowCost", CATEGORY_STAFF, 10, 1, Integer.MAX_VALUE, "Infusion cost for Enhanced Arrow.");
		betterArrowCost = config.getInt("betterArrowCost", CATEGORY_STAFF, 40, 1, Integer.MAX_VALUE, "Infusion cost for Mighty Arrow.");

		hob = config.getBoolean("hob", CATEGORY_STAFF, hob, "Hobgoblin chains immortality");
		screamilyMana = config.getInt("screamilyMana", CATEGORY_STAFF, screamilyMana, 1, Integer.MAX_VALUE, "Screamily mana per banshee scream.");
		// why not?
		for(int i: getIntArray("bertieBottsEffectBlacklist", CATEGORY_STAFF, ItemFoodBertieBotts.getDefaultBlackList(), "Blacklisted Potion Effects.")) {
			ItemFoodBertieBotts.addToBlackList(i);
		}
		// rituals
		// TODO ritual ID's
		bathHouseCost = config.getInt("bathHouseCost", CATEGORY_RITUALS, bathHouseCost, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		despairSonataCost = config.getInt("despairSonataCost", CATEGORY_RITUALS, despairSonataCost, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		randomSpirits = config.getInt("despairSonataRandomSpirits", CATEGORY_RITUALS, randomSpirits, 1, Integer.MAX_VALUE, "Amount of random Spirits from Despair Sonata ritual.");
		// TODO string array
		despairSonataBlackList = config.getString("despairSonataBlackList", CATEGORY_RITUALS, despairSonataBlackList, "Black list for ritual Sonat of Dispair, type names are separated with ',' for example: Zombie, EnderDragon.");
		fishCatchCost = config.getInt("fishCatchCost", CATEGORY_RITUALS, fishCatchCost, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		soulCurseCost = config.getInt("soulCurseCost", CATEGORY_RITUALS, soulCurseCost, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		iceCastleCost = config.getInt("iceCastleCost", CATEGORY_RITUALS, iceCastleCost, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		healMendingCost = config.getInt("healMendingCost", CATEGORY_RITUALS, healMendingCost, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		huntMagicCreaturesCost = config.getInt("huntMagicCreaturesCost", CATEGORY_RITUALS, huntMagicCreaturesCost, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		purifyCost = config.getInt("purifyCost", CATEGORY_RITUALS, purifyCost, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		covenWitchCost = config.getInt("covenWitchCost", CATEGORY_RITUALS, covenWitchCost, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		// ids
		DHID.FOCUSUPGRADE_EXTREMIS = config.getInt("FocusUpgradeType_Extremis_ID", CATEGORY_IDS, DHID.FOCUSUPGRADE_EXTREMIS, 9, Integer.MAX_VALUE, "ID for Inferiois Mutandis Focus. Change if you have FocusUpgradeType ID related conflicts.");

		DHID.SYMBOL_ANIMAINTERITUS = config.getInt("Symbol_AnimaInteritus_ID", CATEGORY_IDS, DHID.SYMBOL_ANIMAINTERITUS, 48, Integer.MAX_VALUE, "ID for Anima Interitus Symbol. Change if you have Symbol ID related conflicts.");
		DHID.SYMBOL_HORCRUX = config.getInt("Symbol_Horcrux_ID", CATEGORY_IDS, DHID.SYMBOL_HORCRUX, 48, Integer.MAX_VALUE, "ID for Horcrux Symbol. Change if you have Symbol ID related conflicts.");
		DHID.SYMBOL_LUMOSTEMPESTAS = config.getInt("Symbol_LumosTempestas_ID", CATEGORY_IDS, DHID.SYMBOL_LUMOSTEMPESTAS, 48, Integer.MAX_VALUE, "ID for Lumos Tempestas Symbol. Change if you have Symbol ID related conflicts.");
		DHID.SYMBOL_BOMBARDAMAXIMA = config.getInt("Symbol_BombardaMaxima_ID", CATEGORY_IDS, DHID.SYMBOL_BOMBARDAMAXIMA, 48, Integer.MAX_VALUE, "ID for Bombarda Maxima Symbol. Change if you have Symbol ID related conflicts.");
		DHID.SYMBOL_GRAVIOLE = config.getInt("Symbol_Graviole_ID", CATEGORY_IDS, DHID.SYMBOL_GRAVIOLE, 48, Integer.MAX_VALUE, "ID for Graviole Symbol. Change if you have Symbol ID related conflicts.");
		DHID.SYMBOL_POWERDESTRUCTION = config.getInt("Symbol_PowerDestruction_ID", CATEGORY_IDS, DHID.SYMBOL_POWERDESTRUCTION, 48, Integer.MAX_VALUE, "ID for Power Destruction Symbol. Change if you have Symbol ID related conflicts.");
		DHID.SYMBOL_OPHIUCHUS = config.getInt("Symbol_Ophiuchus_ID", CATEGORY_IDS, DHID.SYMBOL_OPHIUCHUS, 48, Integer.MAX_VALUE, "ID for Ophiuchus Symbol. Change if you have Symbol ID related conflicts.");
		config.save();
	}

	private static int[] getIntArray(String name, String category, int[] defaultValue, String comment) {
		Property p = config.get(category, name, defaultValue);
		p.comment = comment;
		return p.getIntList();
	}


	public static int getElfRequirements(int level) {
		return elfQuestConditions[MathHelper.clamp_int(level - 1, 0, elfQuestConditions.length - 1)];
	}
}
