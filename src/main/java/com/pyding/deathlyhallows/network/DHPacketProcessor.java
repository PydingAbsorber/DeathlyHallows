package com.pyding.deathlyhallows.network;

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
	private static SimpleNetworkWrapper wrapper;

	public static void preInit() {
		wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("dh");
	}


	public static void init() {
		int id = 1;
		wrapper.registerMessage(PacketKeyBindings.class, PacketKeyBindings.class, id++, Side.SERVER);
		wrapper.registerMessage(PacketDisableFlight.class, PacketDisableFlight.class, id++, Side.CLIENT);
		wrapper.registerMessage(PacketPlayerRender.class, PacketPlayerRender.class, id++, Side.CLIENT);
		wrapper.registerMessage(PacketAnimaMobRender.class, PacketAnimaMobRender.class, id++, Side.CLIENT);
		wrapper.registerMessage(PacketEntityNBTSync.class, PacketEntityNBTSync.class, id++, Side.CLIENT);
		wrapper.registerMessage(PacketPlayerNBTSync.class, PacketPlayerNBTSync.class, id++, Side.CLIENT);
		wrapper.registerMessage(PacketRenderAbsoluteDeath.class, PacketRenderAbsoluteDeath.class, id++, Side.CLIENT);
		wrapper.registerMessage(PacketParticle.class, PacketParticle.class, id++, Side.CLIENT);
	}

	public static void sendToPlayer(IMessage message, EntityPlayer player) {
		wrapper.sendTo(message, (EntityPlayerMP)player);
	}

	public static void sendToAll(IMessage message) {
		wrapper.sendToAll(message);
	}

	public static void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
		wrapper.sendToAllAround(message, point);
	}

	public static void sendToServer(IMessage message) {
		wrapper.sendToServer(message);
	}
}