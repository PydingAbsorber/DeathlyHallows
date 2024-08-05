package com.pyding.deathlyhallows.symbols;

import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketDisableFlight;
import com.pyding.deathlyhallows.utils.DHID;
import com.pyding.deathlyhallows.utils.DHUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class SymbolGraviole extends SymbolEffectBase {

	public static float
			TRACE_RANGE = 48F,
			RADIUS = 4F,
			DAMAGE = 10F,
			SPEED = 10F;

	public SymbolGraviole() {
		super(DHID.SYMBOL_GRAVIOLE, "graviole", 5, false, false, null, 2_500, true, -1, false);
	}

	@Override
	public void doPerform(World world, EntityPlayer p, int level) {
		Vec3 look = DHUtils.getLook(p, TRACE_RANGE);
		double x = look.xCoord, y = look.yCoord, z = look.zCoord;
		for(Entity entity: DHUtils.getEntitiesAt(EntityLivingBase.class, world, x, y, z, RADIUS)) {
			if(entity.equals(p)) {
				continue;
			}
			if(entity instanceof EntityPlayer) {
				EntityPlayer targetPlayer = (EntityPlayer)entity;
				targetPlayer.capabilities.isFlying = false;
				DHPacketProcessor.sendToPlayer(new PacketDisableFlight(), targetPlayer);
			}
			entity.motionY = -SPEED * level;
			entity.attackEntityFrom(DamageSource.fall.setDamageBypassesArmor(), DAMAGE * level);
		}
		world.playSoundAtEntity(p, "dh:spell.death" + DHUtils.getRandomInt(1, 2), 1F, 1F);
	}

}
