package com.pyding.deathlyhallows.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

public class CPacketDisableFlight implements IMessage, IMessageHandler<CPacketDisableFlight,IMessage> {

    public CPacketDisableFlight() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(CPacketDisableFlight message, MessageContext ctx) {
        if(ctx.side == Side.CLIENT){
            Minecraft.getMinecraft().thePlayer.capabilities.isFlying = false;
            Minecraft.getMinecraft().thePlayer.motionY = -10F;
        }
        return null;
    }
}
