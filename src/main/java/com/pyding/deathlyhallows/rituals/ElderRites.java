package com.pyding.deathlyhallows.rituals;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.ritual.RitualTraits;
import com.emoniph.witchery.util.Const;
import com.pyding.deathlyhallows.multiblocks.MultiBlock;
import com.pyding.deathlyhallows.rituals.rites.ElderRite;
import com.pyding.deathlyhallows.rituals.rites.ElderSacrifice;
import com.pyding.deathlyhallows.utils.IMultiBlockHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

import static com.emoniph.witchery.ritual.RitualTraits.*;

public class ElderRites {

	private static final EnumMap<Category, List<ElderRitual>> rituals = new EnumMap<>(Category.class);

	public enum Category {
		
		CIRCLE,
		SONATA,
		LAKE,
		CURSE,
		ICECASTLE,
		MENDING,
		HUNT,
		COVEN,
		PURIFY,
		INTEGRATION

	}

	public static void clearRituals() {
		rituals.clear();
	}

	public static List<ElderRitual> getRituals(Category category) {
		if(!rituals.containsKey(category)) {
			rituals.put(category, new ArrayList<>());
		}
		return rituals.get(category);
	}

	public static void addRecipe(int ritualID, Category category, int bookIndex, String name, ElderRite rite, ElderSacrifice initialSacrifice, EnumSet<RitualTraits> traits, IMultiBlockHandler... circles) {
		ElderRitual ritual = new ElderRitual(ritualID, bookIndex, rite, initialSacrifice, traits, circles);
		ritual.setUnlocalizedName(name);
		getRituals(category).add(ritual);
	}

	public static ElderRitual getRitual(int ritualID) {
		for(Category category: Category.values()) {
			for(ElderRitual ritual: getRituals(category)) {
				if(ritual.ritualID == ritualID) {
					return ritual;
				}
			}
		}
		return null;
	}

	public static List<ElderRitual> getSortedRituals() {
		ArrayList<ElderRitual> sortedRituals = new ArrayList<>();
		for(Category category: Category.values()) {
			ArrayList<ElderRitual> l = new ArrayList<>(getRituals(category));
			l.sort(Comparator.comparingInt(r -> r.bookIndex));
			sortedRituals.addAll(l);
		}
		return sortedRituals;
	}

	public static class ElderRitual {
		public String unlocalizedName;
		public ElderRite rite;
		public ElderSacrifice initialSacrifice;
		public EnumSet<RitualTraits> traits;
		public IMultiBlockHandler[] circles;
		public int
				ritualID,
				bookIndex,
				covenRequired = 0;
		public String familiar = "";

		public String getUnlocalizedName() {
			return this.unlocalizedName;
		}

		public void setUnlocalizedName(String unlocalizedName) {
			this.unlocalizedName = unlocalizedName;
		}

		public String getLocalizedName() {
			return unlocalizedName != null ? Witchery.resource(getUnlocalizedName()) : toString();
		}

		public ElderRitual(int ritualID, int bookIndex, ElderRite rite, ElderSacrifice initialSacrifice, EnumSet<RitualTraits> traits, IMultiBlockHandler[] circles) {
			this.ritualID = ritualID;
			this.bookIndex = bookIndex;
			this.rite = rite;
			this.initialSacrifice = initialSacrifice;
			this.traits = traits;
			this.circles = circles;
		}

		public String getDescription() {
			StringBuffer sb = new StringBuffer();
			sb.append("§n");
			sb.append(this.getLocalizedName());
			sb.append("§r");
			sb.append(Const.BOOK_NEWLINE);
			sb.append(Const.BOOK_NEWLINE);
			initialSacrifice.addDescription(sb);

			if(!familiar.equals("")) {
				sb.append(Const.BOOK_NEWLINE);
				sb.append(Witchery.resource("witchery.rite.requirefamiliar"))
				  .append(' ')
				  .append(familiar)
				  .append(Const.BOOK_NEWLINE);
			}
			if(covenRequired != 0) {
				sb.append(Const.BOOK_NEWLINE);
				sb.append(I18n.format("witchery.rite.requirecoven", covenRequired));
			}
			return sb.toString();
		}

		public boolean isMatch(World world, int posX, int posY, int posZ, ArrayList<Entity> entities, ArrayList<ItemStack> grassperStacks, boolean isDaytime, boolean isRaining, boolean isThundering) {
			if((traits.contains(ONLY_AT_NIGHT) && isDaytime)
					|| (traits.contains(ONLY_AT_DAY) && !isDaytime)
					|| (traits.contains(ONLY_IN_RAIN) && !isRaining)
					|| (traits.contains(ONLY_IN_STROM) && !isThundering)
					|| (traits.contains(ONLY_OVERWORLD) && world.provider.dimensionId != 0)
			) {
				return false;
			}

			MultiBlock mb = new MultiBlock();
			if(this.circles.length > 0) {
				for(IMultiBlockHandler c: circles) {
					mb.mergeMultiBlocks(c.getMultiBlock());
				}
				if(!mb.matchAndRemove(world, posX, posY, posZ, false)) {
					return false;
				}
			}
			if(this.initialSacrifice.isMatch(world, posX, posY, posZ, this.getMaxDistance(), entities, grassperStacks)) {
				mb.matchAndRemove(world, posX, posY, posZ, this.traits.contains(DESTROYS_CIRCLE));
				return true;
			}
			return false;

		}

		public void addSteps(ArrayList<RitualStep> steps, AxisAlignedBB bounds) {
			int maxDistance = this.getMaxDistance();
			if(!familiar.equals("")) {
				steps.add(new StepCheckFamiliar(familiar));
			}
			if(covenRequired != 0) {
				steps.add(new StepCheckCoven(covenRequired));
			}
			this.initialSacrifice.addSteps(steps, bounds, maxDistance);
			this.addRiteSteps(steps, 0);
		}

		private int getMaxDistance() {
			return 7;
		}

		public int getRitualID() {
			return this.ritualID;
		}

		public void addRiteSteps(ArrayList<RitualStep> ritualSteps, int stage) {
			this.rite.addSteps(ritualSteps, stage);
		}

	}

}
