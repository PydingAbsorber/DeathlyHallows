package com.pyding.deathlyhallows.symbols;

import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.pyding.deathlyhallows.utils.DHID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import static com.pyding.deathlyhallows.symbols.ElderSymbolTraits.ELDER;

public class SymbolOphiuchus extends SymbolEffectBase {
	
	public SymbolOphiuchus() {
		super(DHID.SYMBOL_OPHIUCHUS, "ophiuchus", 20, false, false, null, 500, false, ELDER);
	}
	
	@Override
	public void perform(World world, EntityPlayer p, int level) {
		SymbolEffect[] leonardos = new SymbolEffect[] {
				EffectRegistry.LEONARD_1,
				EffectRegistry.LEONARD_2,
				EffectRegistry.LEONARD_3,
				EffectRegistry.LEONARD_4
		};
		for(SymbolEffect leonardo : leonardos) {
			leonardo.perform(world, p, level);
		}
	}

}
