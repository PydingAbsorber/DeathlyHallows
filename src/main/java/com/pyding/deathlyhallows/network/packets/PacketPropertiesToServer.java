package com.pyding.deathlyhallows.network.packets;

import com.pyding.deathlyhallows.network.DHPacketProcessor;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketPropertiesToServer implements IMessage, IMessageHandler<PacketPropertiesToServer, IMessage> {
	
	private String id;
	
	public PacketPropertiesToServer() {
		
	}

	public PacketPropertiesToServer(String identifier) {
		id = identifier;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, id);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		id = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public IMessage onMessage(PacketPropertiesToServer msg, MessageContext ctx) {
		if(msg.id == null) {
			return null;
		}
		EntityPlayer p = ctx.getServerHandler().playerEntity;
		DHPacketProcessor.sendToPlayer(new PacketPropertiesToClient(p, msg.id), p);
		return null;
	}
	
}
