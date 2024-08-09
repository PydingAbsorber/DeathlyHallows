package com.pyding.deathlyhallows.symbols;

import com.pyding.deathlyhallows.utils.DHID;
import com.pyding.deathlyhallows.utils.DHUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.function.Supplier;

import static com.pyding.deathlyhallows.symbols.ElderSymbolTraits.ELDER;

public class SymbolBombardaMaxima extends SymbolEffectBase {

	public static float 
			TRACE_RANGE = 48F,
			DAMAGE = 10F;

	public SymbolBombardaMaxima() {
		super(DHID.SYMBOL_BOMBARDAMAXIMA, "bombardamaxima", 40, false, false, null, 5_000, false, ELDER);
	}

	@Override
	public void perform(World world, EntityPlayer p, int level) {
		Vec3 look = DHUtils.getLook(p, TRACE_RANGE);
		double x = look.xCoord, y = look.yCoord, z = look.zCoord;
		for(EntityLivingBase e: DHUtils.getEntitiesAt(EntityLivingBase.class, world, x, y, z, 4F * level)) {
			if(e.equals(p)
					|| !e.isEntityAlive()
					|| e instanceof EntityPlayer && ((EntityPlayer)e).capabilities.isCreativeMode
			) {
				continue;
			}
			float damage = (float)p.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
			e.attackEntityFrom(DamageSource.causePlayerDamage(p).setExplosion(), (DAMAGE * 3 + damage) * level);
		}
		world.createExplosion(p, x, y, z, 2 * level, true);
		world.playSoundEffect(x, y, z, "random.explode", 4.0F, 0.6F + 0.3F * world.rand.nextFloat());
		Supplier<Double> range = () -> 0.02D * world.rand.nextGaussian();
		world.spawnParticle("explode", x, y, z, range.get(), range.get(), range.get());
		world.playSoundAtEntity(p, "dh:spell.explode" + DHUtils.getRandomInt(1, 2), 1F, 1F);
	}

}
