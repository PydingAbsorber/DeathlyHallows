package com.pyding.deathlyhallows.rituals;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.ritual.RitualTraits;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Const;
import com.pyding.deathlyhallows.multiblocks.MultiBlock;
import com.pyding.deathlyhallows.rituals.rites.ElderRite;
import com.pyding.deathlyhallows.rituals.rites.ElderSacrifice;
import com.pyding.deathlyhallows.utils.IMultiBlockHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

import static com.emoniph.witchery.ritual.RitualTraits.*;

public class ElderRiteRegistry {

	private static final ElderRiteRegistry INSTANCE = new ElderRiteRegistry();
	final ArrayList<Ritual> rituals = new ArrayList<>();


	public static ElderRiteRegistry instance() {
		return INSTANCE;
	}

	public final List<Ritual> getRituals() {
		return this.rituals;
	}

	public static Ritual addRecipe(int ritualID, int bookIndex, String name, ElderRite rite, ElderSacrifice initialSacrifice, EnumSet<RitualTraits> traits, IMultiBlockHandler... circles) {
		Ritual ritual = new Ritual(ritualID, bookIndex, rite, initialSacrifice, traits, circles);
		ritual.setUnlocalizedName(name);
		instance().rituals.add(ritual);
		return ritual;
	}

	public Ritual getRitual(int ritualID) {
		for(Ritual ritual: this.rituals) {
			if(ritual.ritualID == ritualID) {
				return ritual;
			}
		}
		return this.rituals.get(ritualID - 1);
	}

	public void removeRitual(int ritualID) {
		this.rituals.removeIf(r -> r.ritualID == ritualID);
	}

	public List<Ritual> getSortedRituals() {
		ArrayList<Ritual> sortedRituals = new ArrayList<>(this.rituals);
		sortedRituals.sort(Comparator.comparingInt(r -> r.bookIndex));
		return sortedRituals;
	}

	public static void RiteError(String translationID, String username, World world) {
		if(world == null || world.isRemote || username == null) {
			return;
		}
		for(Object obj: world.playerEntities) {
			if(!(obj instanceof EntityPlayer)) {
				continue;
			}
			EntityPlayer worldPlayer = (EntityPlayer)obj;
			if(worldPlayer.getCommandSenderName().equals(username)) {
				RiteError(translationID, worldPlayer, world);
				return;
			}
		}
	}

	public static void RiteError(String translationID, EntityPlayer player, World world) {
		if(world != null && !world.isRemote && player != null) {
			ChatUtil.sendTranslated(EnumChatFormatting.RED, player, translationID);
		}
	}

	public static class Ritual {
		public String unlocalizedName;
		public ElderRite rite;
		public ElderSacrifice initialSacrifice;
		public EnumSet<RitualTraits> traits;
		public IMultiBlockHandler[] circles;
		public int
				ritualID,
				bookIndex,
				covenRequired = 0;
		public boolean
				visibleInBook,
				consumeAttunedStoneCharged = false,
				consumeNecroStone = false;

		public String familiar = "";

		public String getUnlocalizedName() {
			return this.unlocalizedName;
		}

		public Ritual setUnlocalizedName(String unlocalizedName) {
			this.unlocalizedName = unlocalizedName;
			return this;
		}

		public String getLocalizedName() {
			return unlocalizedName != null ? Witchery.resource(getUnlocalizedName()) : toString();
		}

		public Ritual(int ritualID, int bookIndex, ElderRite rite, ElderSacrifice initialSacrifice, EnumSet<RitualTraits> traits, IMultiBlockHandler[] circles) {
			this.ritualID = ritualID;
			this.bookIndex = bookIndex;
			this.rite = rite;
			this.initialSacrifice = initialSacrifice;
			this.traits = traits;
			this.circles = circles;
			this.visibleInBook = true;
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

		public Ritual setShowInBook(boolean show) {
			this.visibleInBook = show;
			return this;
		}

		public boolean showInBook() {
			return this.visibleInBook;
		}

		public boolean isConsumeAttunedStoneCharged() {
			return this.consumeAttunedStoneCharged;
		}

		public void setConsumeAttunedStoneCharged() {
			this.consumeAttunedStoneCharged = true;
		}

		public boolean isConsumeNecroStone() {
			return this.consumeNecroStone;
		}

		public void setConsumeNecroStone() {
			this.consumeNecroStone = true;
		}
		
	}

}
