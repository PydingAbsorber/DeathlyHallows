package com.pyding.deathlyhallows.network.packets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class PacketArrowSync implements IMessage, IMessageHandler<PacketArrowSync, IMessage> {
	public int owner;

	public int id;

	public PacketArrowSync() {
	}

	public PacketArrowSync(int owner, int identifier) {
		this.owner = owner;
		id = identifier;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		owner = buf.readInt();
		id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(owner);
		buf.writeInt(id);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(PacketArrowSync message, MessageContext ctx) {
		if(ctx.side == Side.CLIENT) {
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(message.id);
			entity.getEntityData().setInteger("DHOwner", message.owner);
		}
		return null;
	}
}
