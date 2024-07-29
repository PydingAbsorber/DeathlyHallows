package com.pyding.deathlyhallows.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;


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

	@SideOnly(Side.SERVER)
	@Override
	public IMessage onMessage(PacketKeyBindings message, MessageContext ctx) {
		EntityPlayer player = ctx.getServerHandler().playerEntity;
		if(message.id == 1) {
			player.getEntityData().setBoolean("dhkey1", message.state);
		}
		else if(message.id == 2) {
			player.getEntityData().setBoolean("dhkey2", message.state);
		}
		else if(message.id == 3) {
			player.getEntityData().setBoolean("DHSprint", message.state);
		}
		return null;
	}
}
