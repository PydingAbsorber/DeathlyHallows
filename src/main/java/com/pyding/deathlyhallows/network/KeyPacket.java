package com.pyding.deathlyhallows.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;


public class KeyPacket implements IMessage, IMessageHandler<KeyPacket, IMessage> {
    public boolean state;
    public int id;

    public KeyPacket() {
    }

    public KeyPacket(boolean keyState, int keyNumber) {
        state = keyState;
        id = keyNumber;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        state = buf.readBoolean();
        id = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(state);
        buf.writeInt(id);
    }

    @Override
    public IMessage onMessage(KeyPacket message, MessageContext ctx) {
        if (ctx.side == Side.SERVER) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            if (message.id == 1) {
                player.getEntityData().setBoolean("dhkey1", message.state);
            } else {
                player.getEntityData().setBoolean("dhkey2", message.state);
            }
        }
        return null;
    }
}
