package com.pyding.deathlyhallows.symbols;

import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketAnimaMobRender;
import com.pyding.deathlyhallows.network.packets.PacketPlayerRender;
import com.pyding.deathlyhallows.utils.DHID;
import com.pyding.deathlyhallows.utils.DHUtils;
import com.pyding.deathlyhallows.utils.ElfUtils;
import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class SymbolAnimaInteritus extends SymbolEffectBase {
	
	public SymbolAnimaInteritus() {
		super(DHID.SYMBOL_ANIMAINTERITUS, "animainteritus", 120, true, false, null, 30_000, true, 4, true);
	}
	
	@Override
	protected void doPerform(World world, EntityPlayer p, int level) {
		// TODO rework
		int elfLevel = ElfUtils.getElfLevel(p);
		int cursedCount = 0;
		int elfBonus = 1 + elfLevel / 5;
		DeathlyProperties props = DeathlyProperties.get(p);
		float radius = 64F;
		for(EntityLivingBase target: DHUtils.getEntitiesAround(EntityLivingBase.class, p, radius)) {
			if(cursedCount > (elfBonus - 1)) {
				break;
			}
			if(target.equals(p)) {
				continue;
			}

			if(target instanceof EntityPlayer) {
				EntityPlayer targetPlayer = (EntityPlayer)target;
				if(targetPlayer.capabilities.isCreativeMode) {
					continue;
				}
			}
			if(!target.isEntityAlive()) {
				continue;
			}
			cursedCount++;
			NBTTagCompound targetTag = target.getEntityData();
			targetTag.setInteger("dhcurse", 1200);
			NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(target.dimension, target.posX, target.posY, target.posZ, radius * 1.2);
			if(target instanceof EntityPlayer) {
				DeathlyProperties props1 = DeathlyProperties.get((EntityPlayer)target);
				props1.setCurrentDuration(1200);
				props1.setSource(p);
				target.setLastAttacker(p);
				props1.setCoordinates(target.posX, target.posY, target.posZ, target.dimension);
				props.setCoordinates(target.posX, target.posY, target.posZ, target.dimension);
				p.getEntityData().setInteger("casterCurse", 1200);
				PacketPlayerRender packet = new PacketPlayerRender(targetTag);
				DHPacketProcessor.sendToAllAround(packet, targetPoint);
			}
			else {
				target.setLastAttacker(p);
				PacketAnimaMobRender packet = new PacketAnimaMobRender(targetTag, target.getEntityId());
				DHPacketProcessor.sendToAllAround(packet, targetPoint);
				targetTag.setDouble("chainX", target.posX);
				targetTag.setDouble("chainY", target.posY);
				targetTag.setDouble("chainZ", target.posZ);
			}
		}
		if(cursedCount > 0) {
			world.playSoundAtEntity(p, "dh:spell.anima" + DHUtils.getRandomInt(1, 3), 1F, 1F);
		}
		else {
			SoundEffect.NOTE_SNARE.playAtPlayer(world, p);
		}
	}
	
}
