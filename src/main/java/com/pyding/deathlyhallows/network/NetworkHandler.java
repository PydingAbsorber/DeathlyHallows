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
        wrapper.registerMessage(RenderPacket.class, RenderPacket.class,1,Side.CLIENT); //client kyda prihodit
        wrapper.registerMessage(CPacketDisableFlight.class, CPacketDisableFlight.class, 2, Side.CLIENT);
        wrapper.registerMessage(KeyPacket.class, KeyPacket.class,3,Side.SERVER);
        wrapper.registerMessage(PlayerRenderPacket.class, PlayerRenderPacket.class,4,Side.CLIENT);
        wrapper.registerMessage(AnimaMobRenderPacket.class, AnimaMobRenderPacket.class,5,Side.CLIENT);
    }

    public static void sendToPlayer(IMessage message, EntityPlayer player) {
        wrapper.sendTo(message, (EntityPlayerMP) player);
    }

    public static void sendToAll(IMessage message) {
        wrapper.sendToAll(message);
    }

    public static void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point){
        wrapper.sendToAllAround(message,point);
    }

    public static void sendToServer(IMessage message) {
        wrapper.sendToServer(message);
    }
}