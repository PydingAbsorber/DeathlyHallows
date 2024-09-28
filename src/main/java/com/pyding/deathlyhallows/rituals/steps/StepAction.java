package com.pyding.deathlyhallows.rituals.steps;

import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.blocks.BlockElderRitual;
import com.pyding.deathlyhallows.utils.DHUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class StepAction extends StepBase {
	protected final Action action;

	public StepAction(Action action) {
		super(false);
		this.action = action;
	}

	@Override
	public Result elderProcess(World world, int x, int y, int z, long ticks, BlockElderRitual.TileEntityCircle.ActivatedElderRitual ritual) {
		if(ticks % 20L != 0L) {
			return Result.STARTING;
		}

		if(world.isRemote) {
			return Result.COMPLETED;
		}
		return action.preformAction(world, x, y, z, ticks);
	}

	@FunctionalInterface
	public interface Action {
		
		Result preformAction(World world, int x, int y, int z, long ticks);
		
		static Result summonItem(ItemStack stack, World world, int x, int y, int z) {
			EntityItem ei = new EntityItem(world, x + 0.5D, y + 0.5D, z + 0.5D, stack);
			ei.motionY = 0.3D;
			world.spawnEntityInWorld(ei);
			ParticleEffect.SPELL.send(SoundEffect.RANDOM_FIZZ, ei, 0.5, 0.5, 16);
			return Result.COMPLETED;
		}

		static Result forEachPlayerInBounds(Predicate<EntityPlayer> subaction, World world, int x, int y, int z, int radius) {
			for(EntityPlayer p: DHUtils.getEntitiesIn(EntityPlayer.class, world, AxisAlignedBB.getBoundingBox(x - radius, y, z - radius, x + radius, y + 2, z + radius))) {
				if(Coord.distance(p.posX, p.posY, p.posZ, x, y, z) > radius || subaction.test(p)) {
					continue;
				}
				return Result.COMPLETED;				
			}
			return Result.UPKEEP;
		}
		
	}
}
