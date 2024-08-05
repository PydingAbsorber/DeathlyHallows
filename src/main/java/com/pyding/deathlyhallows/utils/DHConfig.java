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
	public static int deathDifficulty;
	public static String spells;
	public static boolean shouldRemove;
	public static int[] elfQuestConditions = new int[] {20,20,-100,100,50,30,20000,35000,100,1000};
	public static boolean hob;
	public static int 
			screamilyMana,
			spawnlesiaMana;
	public static String spawnlesia;
	public static int 
			bathHouseCost,
			despairSonataCost,
			fishCatchCost,
			soulCurseCost,
			iceCastleCost,
			healMendingCost,
			huntMagicCreaturesCost,
			purifyCost,
			covenWitchCost,
			spawnlesiaCd;
	public static String despairSonataBlackList;
	public static int randomSpirits;

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
		shouldRemove = config.getBoolean("removeDuplicated", CATEGORY_STAFF, true, "Should have only one hallow or more?");

		spells = config.getString("spells", CATEGORY_STAFF, "", "Black list for Spells Id in Elder Wand, for example: 36, 37, 42.");
		spawnlesia = config.getString("spawnlesia", CATEGORY_STAFF, "", "Black list for Spawnlesia, type names are separated with ',' for example: Zombie, EnderDragon.");
		spawnlesiaMana = config.getInt("spawnlesiaMana", CATEGORY_STAFF, 1000000, 1, Integer.MAX_VALUE, "Mana requirements for Spawnlesia per spawn.");
		spawnlesiaCd = config.getInt("spawnlesiaCd", CATEGORY_STAFF, 10000, 1, Integer.MAX_VALUE, "Cooldown for Spawnlesia in long. 1000 is 1 sec.");

		deathDifficulty = config.getInt("deathDifficulty", CATEGORY_STAFF, 3, 1, 3, "Absolute Death difficulty (3 - Gigachad Man, 2 - weak casual, 1 - newborn toddler).");
		elfQuestConditions = getIntArray("elfLvl", CATEGORY_STAFF, elfQuestConditions, "Change elf quest requirements, position of requirement corresponds to elf level. Last one is not a requirement but a maximum.");
		hob = config.getBoolean("hob", CATEGORY_STAFF, true, "Hobgoblin chains immortality");
		screamilyMana = config.getInt("screamilyMana", CATEGORY_STAFF, 10000, 1, Integer.MAX_VALUE, "Screamily mana per banshee scream.");
		// why not?
		for(int i: getIntArray("bertieBottsEffectBlacklist", CATEGORY_STAFF, ItemFoodBertieBotts.getDefaultBlackList(), "Blacklisted Potion Effects.")) {
			ItemFoodBertieBotts.addToBlackList(i);
		}
		// rituals
		// TODO why on the earth we need 2 configs, when we can just make cost like... -1 and that's it? 
		
		bathHouseCost = config.getInt("bathHouseCost", CATEGORY_RITUALS, 25000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		despairSonataCost = config.getInt("despairSonataCost", CATEGORY_RITUALS, 3000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		randomSpirits = config.getInt("despairSonataRandomSpirits", CATEGORY_RITUALS, 3, 1, Integer.MAX_VALUE, "Amount of random Spirits from Despair Sonata ritual.");
		despairSonataBlackList = config.getString("despairSonataBlackList", CATEGORY_RITUALS, "", "Black list for ritual Sonat of Dispair, type names are separated with ',' for example: Zombie, EnderDragon.");
		fishCatchCost = config.getInt("fishCatchCost", CATEGORY_RITUALS, 1000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		soulCurseCost = config.getInt("soulCurseCost", CATEGORY_RITUALS, 50000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		iceCastleCost = config.getInt("iceCastleCost", CATEGORY_RITUALS, 7000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		healMendingCost = config.getInt("healMendingCost", CATEGORY_RITUALS, 18000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		huntMagicCreaturesCost = config.getInt("huntMagicCreaturesCost", CATEGORY_RITUALS, 30000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		purifyCost = config.getInt("purifyCost", CATEGORY_RITUALS, 24100, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
		covenWitchCost = config.getInt("covenWitchCost", CATEGORY_RITUALS, 5000, 1, Integer.MAX_VALUE, "Altar energy requirements for this ritual. Ritual will be disabled, if costs are negative.");
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
		return elfQuestConditions[MathHelper.clamp_int(level - 1,0, elfQuestConditions.length - 1)];
	}
}
