package com.pyding.deathlyhallows.rituals;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class StepCheckCoven extends RitualStep {

    private int convenRequired;

    protected StepCheckCoven(int convenRequired) {
        super(false);
        this.convenRequired=convenRequired;
    }

    @Override
    public Result process(final World world, final int posX, final int posY, final int posZ, final long ticks, final BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
        if (ticks % 20L != 0L) {
            return Result.STARTING;
        }

        if (!world.isRemote) {
            if(ritual.covenSize>=convenRequired)
                return Result.COMPLETED;
        }

        RiteRegistry.RiteError(I18n.format("witchery.rite.missingcoven",convenRequired- ritual.covenSize), ritual.getInitiatingPlayerName(), world);
        return Result.ABORTED_REFUND;
    }
}
