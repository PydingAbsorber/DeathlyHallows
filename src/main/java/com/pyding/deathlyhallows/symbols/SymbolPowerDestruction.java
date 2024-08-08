package com.pyding.deathlyhallows.symbols;

import com.pyding.deathlyhallows.utils.DHID;
import com.pyding.deathlyhallows.utils.DHUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class SymbolPowerDestruction extends SymbolEffectBase {

	public SymbolPowerDestruction() {
		super(DHID.SYMBOL_POWERDESTRUCTION, "powerDestruction", 20, false, false, null, 500, false);
	}

	public static float
			TRACE_RANGE = 48F,
			RADIUS = 12F;

	@Override
	public void perform(World world, EntityPlayer p, int level) {
		Vec3 look = DHUtils.getLook(p, TRACE_RANGE);
		double x = look.xCoord, y = look.yCoord, z = look.zCoord;
		for(EntityLivingBase entity: DHUtils.getEntitiesAt(EntityLivingBase.class, world, x, y, z, RADIUS)) {
			if(entity.equals(p)) {
				continue;
			}
			for(Object potionEffect: entity.getActivePotionEffects()) {
				PotionEffect effect = (PotionEffect)potionEffect;
				if(!Potion.potionTypes[effect.getPotionID()].isBadEffect()) {
					entity.removePotionEffect(effect.getPotionID());
				}
			}
		}
	}

}
