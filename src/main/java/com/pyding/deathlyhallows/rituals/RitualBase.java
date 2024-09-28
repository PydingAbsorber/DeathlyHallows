package com.pyding.deathlyhallows.rituals;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.Const;
import com.pyding.deathlyhallows.multiblocks.MultiBlock;
import com.pyding.deathlyhallows.rituals.rites.RiteBase;
import com.pyding.deathlyhallows.rituals.sacrifices.SacrificeBase;
import com.pyding.deathlyhallows.rituals.steps.StepCheckCoven;
import com.pyding.deathlyhallows.rituals.steps.StepCheckFamiliar;
import com.pyding.deathlyhallows.utils.IMultiBlockHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;

public class RitualBase {
	public String unlocalizedName;
	public RiteBase rite;
	public SacrificeBase initialSacrifice;
	public DHRituals.RitualCondition condition;
	public IMultiBlockHandler circle;
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

	public RitualBase(int ritualID, int bookIndex, RiteBase rite, SacrificeBase initialSacrifice, DHRituals.RitualCondition condition, IMultiBlockHandler circle) {
		this.ritualID = ritualID;
		this.bookIndex = bookIndex;
		this.rite = rite;
		this.initialSacrifice = initialSacrifice;
		this.condition = condition;
		this.circle = circle;
	}

	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("§n");
		sb.append(getLocalizedName());
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

	public boolean isMatch(World world, int posX, int posY, int posZ, ArrayList<Entity> entities, ArrayList<ItemStack> grassperStacks) {
		if(!condition.test(world, posX, posY, posZ)) {
			return false;
		}

		MultiBlock mb = circle.getMultiBlock();
		if(!mb.matchAndRemove(world, posX, posY, posZ, false)) {
			return false;
		}
		if(initialSacrifice.isMatch(world, posX, posY, posZ, this.getMaxDistance(), entities, grassperStacks)) {
			mb.matchAndRemove(world, posX, posY, posZ, false);
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
		initialSacrifice.addSteps(steps, bounds, maxDistance);
		addRiteSteps(steps, 0);
	}

	private int getMaxDistance() {
		return 7;
	}

	public int getRitualID() {
		return ritualID;
	}

	public void addRiteSteps(ArrayList<RitualStep> ritualSteps, int stage) {
		rite.addSteps(ritualSteps, stage);
	}

}
