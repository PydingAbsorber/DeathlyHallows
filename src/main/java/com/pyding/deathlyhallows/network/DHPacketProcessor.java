package com.pyding.deathlyhallows.network;

import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.network.packets.PacketAnimaMobRender;
import com.pyding.deathlyhallows.network.packets.PacketDisableFlight;
import com.pyding.deathlyhallows.network.packets.PacketEntityNBTSync;
import com.pyding.deathlyhallows.network.packets.PacketKeyBindings;
import com.pyding.deathlyhallows.network.packets.PacketParticle;
import com.pyding.deathlyhallows.network.packets.PacketPlayerNBTSync;
import com.pyding.deathlyhallows.network.packets.PacketPlayerRender;
import com.pyding.deathlyhallows.network.packets.PacketRenderAbsoluteDeath;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class DHPacketProcessor {
	private static SimpleNetworkWrapper channel;

	public static void preInit() {
		channel = NetworkRegistry.INSTANCE.newSimpleChannel(DeathlyHallows.MODID);
	}

	public static void init() {
		int id = 1;
		channel.registerMessage(PacketKeyBindings.class, PacketKeyBindings.class, id++, Side.SERVER);
		channel.registerMessage(PacketDisableFlight.class, PacketDisableFlight.class, id++, Side.CLIENT);
		channel.registerMessage(PacketPlayerRender.class, PacketPlayerRender.class, id++, Side.CLIENT);
		channel.registerMessage(PacketAnimaMobRender.class, PacketAnimaMobRender.class, id++, Side.CLIENT);
		channel.registerMessage(PacketEntityNBTSync.class, PacketEntityNBTSync.class, id++, Side.CLIENT);
		channel.registerMessage(PacketPlayerNBTSync.class, PacketPlayerNBTSync.class, id++, Side.CLIENT);
		channel.registerMessage(PacketRenderAbsoluteDeath.class, PacketRenderAbsoluteDeath.class, id++, Side.CLIENT);
		channel.registerMessage(PacketParticle.class, PacketParticle.class, id++, Side.CLIENT);
	}

	public static void sendToPlayer(IMessage message, EntityPlayer player) {
		channel.sendTo(message, (EntityPlayerMP)player);
	}

	public static void sendToAll(IMessage message) {
		channel.sendToAll(message);
	}

	public static void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
		channel.sendToAllAround(message, point);
	}

	public static void sendToServer(IMessage message) {
		channel.sendToServer(message);
	}
	
}
