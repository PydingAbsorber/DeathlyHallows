package com.pyding.deathlyhallows.network.packets;

import com.pyding.deathlyhallows.events.DHEvents;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;

public class PacketKeyBindings implements IMessage, IMessageHandler<PacketKeyBindings, IMessage> {
	public boolean state;
	public int id;

	public PacketKeyBindings() {
		
	}

	public PacketKeyBindings(boolean keyState, int keyNumber) {
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
	public IMessage onMessage(PacketKeyBindings msg, MessageContext ctx) {
		if(ctx.side != Side.SERVER) {
			return null;
		}
		DHEvents.processDHKeys(ctx.getServerHandler().playerEntity, msg.id, msg.state);
		return null;
	}
	
}
