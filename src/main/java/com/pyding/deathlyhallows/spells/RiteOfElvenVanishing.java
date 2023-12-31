package com.pyding.deathlyhallows.spells;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockAltar;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.ritual.rites.RiteBindSpiritsToFetish;
import com.emoniph.witchery.ritual.rites.RiteExpandingEffect;
import com.emoniph.witchery.ritual.rites.RiteInfusionRecharge;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RiteOfElvenVanishing extends Rite{
    private final float upkeepPowerCost;
    private final int ticksToLive;
    private final int radius;


    public RiteOfElvenVanishing(int radius, float upkeepPowerCost, int ticksToLive) {
        this.radius = radius;
        this.upkeepPowerCost = upkeepPowerCost;
        this.ticksToLive = ticksToLive;
    }

    public void addSteps(ArrayList<RitualStep> steps, int intialStage) {
        steps.add(new StepVanish(this, intialStage));
    }

    private static class StepVanish extends RitualStep {
        private final RiteOfElvenVanishing rite;
        private boolean activated = false;
        protected int ticksSoFar;
        Coord powerSourceCoord;
        static final int POWER_SOURCE_RADIUS = 16;

        public StepVanish(RiteOfElvenVanishing rite, int ticksSoFar) {
            super(false);
            this.rite = rite;
            this.ticksSoFar = ticksSoFar;
        }

        public int getCurrentStage() {
            return this.ticksSoFar;
        }

        public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
            if (ticks % 20L != 0L) {
                return Result.STARTING;
            } else {
                if (!world.isRemote) {
                    if (this.rite.upkeepPowerCost > 0.0F) {
                        IPowerSource powerSource = this.getPowerSource(world, posX, posY, posZ);
                        if (powerSource == null) {
                            return Result.ABORTED;
                        }

                        this.powerSourceCoord = powerSource.getLocation();
                        if (!powerSource.consumePower(this.rite.upkeepPowerCost)) {
                            return Result.ABORTED;
                        }
                    }

                    if (this.rite.ticksToLive > 0 && ticks % 20L == 0L && ++this.ticksSoFar >= this.rite.ticksToLive) {
                        return Result.COMPLETED;
                    }

                    int r = this.rite.radius;
                    AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)(posX - r), (double)posY, (double)(posZ - r), (double)(posX + r), (double)(posY + 1), (double)(posZ + r));
                    Iterator i$ = world.getEntitiesWithinAABB(EntityPlayer.class, bounds).iterator();

                    while(i$.hasNext()) {
                        Object obj = i$.next();
                        EntityPlayer player = (EntityPlayer)obj;
                        if (Coord.distance(player.posX, player.posY, player.posZ, (double)posX, (double)posY, (double)posZ) <= (double)r) {
                            com.pyding.deathlyhallows.common.properties.ExtendedPlayer props = com.pyding.deathlyhallows.common.properties.ExtendedPlayer.get(player);
                            if (props.getElfLvl() > 0) {
                                props.nullifyElfLvl();
                                ParticleEffect.INSTANT_SPELL.send(SoundEffect.NOTE_PLING, player, 1.0, 2.0, 8);
                                ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, player, 1.0, 2.0, 8);
                                return Result.COMPLETED;
                            }
                        }
                    }
                }

                return Result.UPKEEP;
            }
        }

        IPowerSource getPowerSource(World world, int posX, int posY, int posZ) {
            if (this.powerSourceCoord != null && world.rand.nextInt(5) != 0) {
                TileEntity tileEntity = this.powerSourceCoord.getBlockTileEntity(world);
                if (!(tileEntity instanceof BlockAltar.TileEntityAltar)) {
                    return this.findNewPowerSource(world, posX, posY, posZ);
                } else {
                    BlockAltar.TileEntityAltar altarTileEntity = (BlockAltar.TileEntityAltar)tileEntity;
                    return (IPowerSource)(!altarTileEntity.isValid() ? this.findNewPowerSource(world, posX, posY, posZ) : altarTileEntity);
                }
            } else {
                return this.findNewPowerSource(world, posX, posY, posZ);
            }
        }

        private IPowerSource findNewPowerSource(World world, int posX, int posY, int posZ) {
            List<PowerSources.RelativePowerSource> sources = PowerSources.instance() != null ? PowerSources.instance().get(world, new Coord(posX, posY, posZ), 16) : null;
            return sources != null && sources.size() > 0 ? ((PowerSources.RelativePowerSource)sources.get(0)).source() : null;
        }
    }
}
