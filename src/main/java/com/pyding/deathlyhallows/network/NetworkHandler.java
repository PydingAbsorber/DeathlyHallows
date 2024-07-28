package com.pyding.deathlyhallows.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class NetworkHandler {
	private static SimpleNetworkWrapper wrapper;

	public static void preInit() {
		wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("dh");
	}
	
	
	public static void init() {
		int id = 1;
		wrapper.registerMessage(RenderPacket.class, RenderPacket.class, id++, Side.CLIENT); 
		wrapper.registerMessage(CPacketDisableFlight.class, CPacketDisableFlight.class, id++, Side.CLIENT);
		wrapper.registerMessage(KeyPacket.class, KeyPacket.class, id++, Side.SERVER);
		wrapper.registerMessage(PlayerRenderPacket.class, PlayerRenderPacket.class, id++, Side.CLIENT);
		wrapper.registerMessage(AnimaMobRenderPacket.class, AnimaMobRenderPacket.class, id++, Side.CLIENT);
		wrapper.registerMessage(NBTSync.class, NBTSync.class, id++, Side.CLIENT);
		wrapper.registerMessage(PlayerNBTSync.class, PlayerNBTSync.class, id++, Side.CLIENT);
		wrapper.registerMessage(ArrowSync.class, ArrowSync.class, id++, Side.CLIENT);
		wrapper.registerMessage(ParticlePacket.class, ParticlePacket.class, id++, Side.CLIENT);
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