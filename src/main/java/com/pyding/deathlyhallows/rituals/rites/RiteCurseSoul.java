package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityWitchHunter;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.blocks.BlockElderRitual;
import com.pyding.deathlyhallows.rituals.steps.StepBase;
import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;

public class RiteCurseSoul extends RiteBase {

	public RiteCurseSoul() {
	}

	public void addSteps(ArrayList<RitualStep> steps, int initialStage) {
		steps.add(new StepCurseCreature());
	}

	private static class StepCurseCreature extends StepBase {

		public StepCurseCreature() {
			super(false);
		}


		@Override
		public Result elderProcess(World world, int x, int y, int z, long ticks, BlockElderRitual.TileEntityCircle.ActivatedElderRitual ritual) {
			if(ticks % 20L != 0L) {
				return Result.STARTING;
			}

			if(world.isRemote) {
				return Result.COMPLETED;
			}
			boolean complete = false;
			boolean cursed = false;
			EntityPlayer curseMasterPlayer = ritual.getInitiatingPlayer(world);
			int levelBuff = curseMasterPlayer != null 
					&& Familiar.hasActiveCurseMasteryFamiliar(curseMasterPlayer) ? 1 : 0;
			if(ritual.covenSize == 6) {
				levelBuff += 2;
			}
			else if(ritual.covenSize >= 3) {
				++levelBuff;
			}

			for(SacrificedItem item: ritual.sacrificedItems) {
				if(item.itemstack.getItem() != Witchery.Items.TAGLOCK_KIT || item.itemstack.getItemDamage() != 1) {
					continue;
				}
				EntityLivingBase entity = Witchery.Items.TAGLOCK_KIT.getBoundEntity(world, null, item.itemstack, 1);
				if(entity == null) {
					break;
				}
				NBTTagCompound nbtTag = entity instanceof EntityPlayer ? Infusion.getNBT(entity) : entity.getEntityData();
				if(nbtTag == null) {
					break;
				}
				int curseLevel = 1;
				if(Familiar.hasActiveCurseMasteryFamiliar(curseMasterPlayer)) {
					curseLevel += 4;
				}
				if(DeathlyProperties.get(curseMasterPlayer).getElfLevel() > 1) {
					curseLevel += 4;
				}
				cursed = true;
				if(entity instanceof EntityPlayer) {
					Infusion.syncPlayer(entity.worldObj, (EntityPlayer)entity);
				}
				complete = true;
				EntityWitchHunter.blackMagicPerformed(curseMasterPlayer);
				boolean isImmune = false;
				if(!isImmune && !Witchery.Items.POPPET.voodooProtectionActivated(curseMasterPlayer, null, entity, levelBuff > 0 ? 3 : 1)) {
					if(entity instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer)entity;
						DeathlyProperties props = DeathlyProperties.get(player);
						props.setCursed(curseLevel);
					}
					if(entity instanceof EntityPlayer) {
						Infusion.syncPlayer(entity.worldObj, (EntityPlayer)entity);
					}
				}
				break;
			}

			if(!complete) {
				return Result.ABORTED_REFUND;
			}

			if(cursed) {
				ParticleEffect.FLAME.send(SoundEffect.MOB_ENDERDRAGON_GROWL, world, 0.5 + x, 0.1 + y, 0.5 + z, 1.0, 2.0, 16);
			}
			else {
				ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, world, 0.5 + x, 0.1 + y, 0.5 + z, 1.0, 2.0, 16);
			}

			return Result.COMPLETED;
		}
		
	}
	
}
