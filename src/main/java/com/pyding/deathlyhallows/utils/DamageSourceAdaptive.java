package com.pyding.deathlyhallows.utils;

import net.minecraft.util.DamageSource;

public class DamageSourceAdaptive extends DamageSource {

	private static final String DAMAGE_SOURCE_ADAPTIVE = "adaptive";

	public DamageSourceAdaptive(int damageType) {
		super(DAMAGE_SOURCE_ADAPTIVE);
		adapt(damageType);
	}

	private void adapt(int damageType) {
		switch(damageType) {
			case 1:
				setFireDamage();
				return;
			case 2:
				setMagicDamage();
				return;
			case 3:
				setExplosion();
				return;
			case 4:
				setProjectile();
				return;
			case 5:
				setDamageAllowedInCreativeMode();
				return;
			case 6:
				setDamageIsAbsolute();
				return;
		}
		setDamageBypassesArmor();
	}
	
	public static boolean isAdaptive(DamageSource source) {
		return DAMAGE_SOURCE_ADAPTIVE.equals(source.damageType);
	}
	
}
