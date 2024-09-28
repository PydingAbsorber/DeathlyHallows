package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.blocks.BlockElderRitual;
import com.pyding.deathlyhallows.rituals.steps.StepBase;
import com.pyding.deathlyhallows.utils.DHUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.function.Predicate;

public class RiteAction extends RiteBase {
	protected final Action action;

	public RiteAction(Action action) {
		this.action = action;
	}
	
	public void addSteps(ArrayList<RitualStep> steps, int initialStage) {
		steps.add(new StepSummon(this));
	}

	private static class StepSummon extends StepBase {
		private final RiteAction rite;

		public StepSummon(RiteAction rite) {
			super(false);
			this.rite = rite;
		}
		
		@Override
		public Result elderProcess(World world, int x, int y, int z, long ticks, BlockElderRitual.TileEntityCircle.ActivatedElderRitual ritual) {
			if(ticks % 20L != 0L) {
				return Result.STARTING;
			}

			if(world.isRemote) {
				return Result.COMPLETED;
			}
			return rite.action.preformAction(rite, world, x, y, z, ticks);
		}

	}
	
	@FunctionalInterface
	public interface Action {
		
		RitualStep.Result preformAction(RiteAction rite, World world, int x, int y, int z, long ticks);
		
		static RitualStep.Result summonItem(ItemStack stack, World world, int x, int y, int z) {
			EntityItem ei = new EntityItem(world, 0.5 + x, y + 1.5, 0.5 + z, stack);
			ei.motionY = 0.3D;
			world.spawnEntityInWorld(ei);
			ParticleEffect.SPELL.send(SoundEffect.RANDOM_FIZZ, ei, 0.5, 0.5, 16);
			return RitualStep.Result.COMPLETED;
		}

		static RitualStep.Result forEachPlayerInBounds(Predicate<EntityPlayer> subaction, World world, int x, int y, int z, int radius) {
			for(EntityPlayer p: DHUtils.getEntitiesIn(EntityPlayer.class, world, AxisAlignedBB.getBoundingBox(x - radius, y, z - radius, x + radius, y + 2, z + radius))) {
				if(Coord.distance(p.posX, p.posY, p.posZ, x, y, z) > radius || subaction.test(p)) {
					continue;
				}
				return RitualStep.Result.COMPLETED;				
			}
			return RitualStep.Result.UPKEEP;
		}
		
	}
	
}
