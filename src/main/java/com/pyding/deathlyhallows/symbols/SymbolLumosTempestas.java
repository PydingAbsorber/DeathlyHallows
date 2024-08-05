package com.pyding.deathlyhallows.symbols;

import com.pyding.deathlyhallows.utils.DHID;
import com.pyding.deathlyhallows.utils.DHUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class SymbolLumosTempestas extends SymbolEffectBase {
	public static final int AMOUNT = 5;
	public static float
			TRACE_RANGE = 48F,
			RADIUS = 6F,
			DAMAGE = 10F;

	public SymbolLumosTempestas() {
		// §
		super(DHID.SYMBOL_LUMOSTEMPESTAS, "lumostempestas", 5, false, false, null, 30_000, true, -1, false);
	}

	@Override
	public void doPerform(World world, EntityPlayer p, int level) {
		int count = 0;
		Vec3 look = DHUtils.getLook(p, TRACE_RANGE);
		int amount = AMOUNT * level;
		for(EntityLivingBase e: DHUtils.getEntitiesAt(EntityLivingBase.class, world, look.xCoord, look.yCoord, look.zCoord, RADIUS * level)) {
			if(count > amount) {
				return;
			}
			if(e.equals(p)
					|| !e.isEntityAlive()
					|| e instanceof EntityPlayer && ((EntityPlayer)e).capabilities.isCreativeMode
			) {
				continue;
			}
			float damage = (float)p.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
			e.attackEntityFrom(DamageSource.causePlayerDamage(p).setMagicDamage().setFireDamage(), (DAMAGE + damage) * level);
			e.setLastAttacker(p);
			world.addWeatherEffect(new EntityLightningBolt(world, e.posX, e.posY, e.posZ));
			++count;
		}
		if(count < amount) {
			for(int i = 0; i < amount - count; ++i) {
				Supplier<Float> rand = () -> 2F * RADIUS * (world.rand.nextFloat() - 0.5F);
				world.addWeatherEffect(new EntityLightningBolt(world, look.xCoord + rand.get(), look.yCoord, look.zCoord + rand.get()));
			}
		}
	}

}
