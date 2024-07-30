package com.pyding.deathlyhallows.rituals.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityWitchHunter;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.blocks.BlockElderRitual;
import com.pyding.deathlyhallows.utils.properties.ExtendedPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;

public class CurseSoulRitual extends ElderRite {

	public CurseSoulRitual() {
	}

	public void addSteps(ArrayList steps, int intialStage) {
		steps.add(new StepCurseCreature(this));
	}

	private static class StepCurseCreature extends ElderRitualStep {
		private final CurseSoulRitual rite;
		private static final int CURSE_MASTER_BONUS_LEVELS = 1;

		public StepCurseCreature(CurseSoulRitual rite) {
			super(false);
			this.rite = rite;
		}


		@Override
		public Result elderProcess(World world, int posX, int posY, int posZ, long ticks, BlockElderRitual.TileEntityCircle.ActivatedElderRitual ritual) {
			if(ticks % 20L != 0L) {
				return Result.STARTING;
			}
			else {
				if(!world.isRemote) {
					boolean complete = false;
					boolean cursed = false;
					EntityPlayer curseMasterPlayer = ritual.getInitiatingPlayer(world);
					int levelBuff = curseMasterPlayer != null && Familiar.hasActiveCurseMasteryFamiliar(curseMasterPlayer) ? 1 : 0;
					if(ritual.covenSize == 6) {
						levelBuff += 2;
					}
					else if(ritual.covenSize >= 3) {
						++levelBuff;
					}

					Iterator i$ = ritual.sacrificedItems.iterator();

					while(i$.hasNext()) {
						RitualStep.SacrificedItem item = (RitualStep.SacrificedItem)i$.next();
						if(item.itemstack.getItem() == Witchery.Items.TAGLOCK_KIT && item.itemstack.getItemDamage() == 1) {
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
							if(ExtendedPlayer.get(curseMasterPlayer).getElfLvl() > 1) {
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
									ExtendedPlayer props = ExtendedPlayer.get(player);
									props.setCursed(curseLevel);
								}
								cursed = true;
								if(entity instanceof EntityPlayer) {
									Infusion.syncPlayer(entity.worldObj, (EntityPlayer)entity);
								}
							}

							if(isImmune) {
								if(curseMasterPlayer != null) {
									ChatUtil.sendTranslated(EnumChatFormatting.RED, curseMasterPlayer, "witchery.rite.blackmagicdampening");
								}
							}
							else {
								complete = true;
							}
							break;
						}
					}

					if(!complete) {
						return Result.ABORTED_REFUND;
					}

					if(cursed) {
						ParticleEffect.FLAME.send(SoundEffect.MOB_ENDERDRAGON_GROWL, world, 0.5 + (double)posX, 0.1 + (double)posY, 0.5 + (double)posZ, 1.0, 2.0, 16);
					}
					else {
						ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, world, 0.5 + (double)posX, 0.1 + (double)posY, 0.5 + (double)posZ, 1.0, 2.0, 16);
					}
				}

				return Result.COMPLETED;
			}
		}
	}
}
