package com.pyding.deathlyhallows.network;

import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.network.packets.PacketDisableFlight;
import com.pyding.deathlyhallows.network.packets.PacketElderBookPage;
import com.pyding.deathlyhallows.network.packets.PacketElderWandLastSpell;
import com.pyding.deathlyhallows.network.packets.PacketElderWandStrokes;
import com.pyding.deathlyhallows.network.packets.PacketKeyBindings;
import com.pyding.deathlyhallows.network.packets.PacketNBTSync;
import com.pyding.deathlyhallows.network.packets.PacketParticle;
import com.pyding.deathlyhallows.network.packets.PacketPropertiesSync;
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
		channel.registerMessage(PacketPropertiesSync.Server.class, PacketPropertiesSync.Server.class, id++, Side.SERVER);
		channel.registerMessage(PacketPropertiesSync.Client.class, PacketPropertiesSync.Client.class, id++, Side.CLIENT);
		channel.registerMessage(PacketKeyBindings.class, PacketKeyBindings.class, id++, Side.SERVER);
		channel.registerMessage(PacketDisableFlight.class, PacketDisableFlight.class, id++, Side.CLIENT);
		channel.registerMessage(PacketNBTSync.class, PacketNBTSync.class, id++, Side.CLIENT);
		channel.registerMessage(PacketRenderAbsoluteDeath.class, PacketRenderAbsoluteDeath.class, id++, Side.CLIENT);
		channel.registerMessage(PacketParticle.class, PacketParticle.class, id++, Side.CLIENT);
		channel.registerMessage(PacketElderBookPage.class, PacketElderBookPage.class, id++, Side.SERVER);
		channel.registerMessage(PacketElderWandStrokes.class, PacketElderWandStrokes.class, id++, Side.SERVER);
		channel.registerMessage(PacketElderWandLastSpell.class, PacketElderWandLastSpell.class, id++, Side.SERVER);
	}

	public static void sendToPlayer(IMessage message, EntityPlayer player) {
		if(!(player instanceof EntityPlayerMP) || ((EntityPlayerMP)player).playerNetServerHandler == null) {
			return;
		}
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
