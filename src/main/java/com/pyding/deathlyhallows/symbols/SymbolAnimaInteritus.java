package com.pyding.deathlyhallows.symbols;

import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketNBTSync;
import com.pyding.deathlyhallows.network.packets.PacketPropertiesToClient;
import com.pyding.deathlyhallows.utils.DHID;
import com.pyding.deathlyhallows.utils.DHUtils;
import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import static com.pyding.deathlyhallows.symbols.ElderSymbolTraits.ELDER;
import static com.pyding.deathlyhallows.symbols.ElderSymbolTraits.ELF;
import static com.pyding.deathlyhallows.symbols.ElderSymbolTraits.INFUSION;

public class SymbolAnimaInteritus extends SymbolEffectBase {

	public SymbolAnimaInteritus() {
		super(DHID.SYMBOL_ANIMAINTERITUS, "animainteritus", 120, true, false, null, 10_000, false, INFUSION(4), ELF(1), ELDER);
	}

	@Override
	public void perform(World world, EntityPlayer p, int level) {
		// TODO rework
		int cursedCount = 0;
		int maxCursed = MathHelper.floor_float(1.5F * level) - 1;
		float radius = 64F;
		DeathlyProperties casterProps = DeathlyProperties.get(p);
		for(EntityLivingBase e: DHUtils.getEntitiesAround(EntityLivingBase.class, p, radius)) {
			if(cursedCount > maxCursed) {
				break;
			}
			if(e.equals(p)
					|| !e.isEntityAlive()
					|| e instanceof EntityPlayer && ((EntityPlayer)e).capabilities.isCreativeMode
			) {
				continue;
			}
			NBTTagCompound tag = e.getEntityData();
			if(tag.hasKey("dhcurse")) {
				continue;
			}
			++cursedCount;
			tag.setInteger("dhcurse", 1200);
			if(e instanceof EntityPlayer) {
				e.setLastAttacker(p);
				DeathlyProperties targetProps = DeathlyProperties.get((EntityPlayer)e);
				targetProps.setSource(p);
				targetProps.setCurrentDuration(1200);
				targetProps.setCoordinates(e.posX, e.posY, e.posZ, e.dimension);
				casterProps.setCoordinates(e.posX, e.posY, e.posZ, e.dimension);
				tag.setInteger("casterCurse", 1200);
			}
			else {
				e.setLastAttacker(p);
				tag.setDouble("chainX", e.posX);
				tag.setDouble("chainY", e.posY);
				tag.setDouble("chainZ", e.posZ);
			}
			NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(e.dimension, e.posX, e.posY, e.posZ, radius * 1.5D);
			DHPacketProcessor.sendToAllAround(new PacketNBTSync(tag, e.getEntityId()), targetPoint);
			if(e.getExtendedProperties(DeathlyProperties.NAME) != null) {
				DHPacketProcessor.sendToAllAround(new PacketPropertiesToClient(e, DeathlyProperties.NAME), targetPoint);
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
