package com.pyding.deathlyhallows.network.packets;

import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.ItemElderWand;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PacketElderWandStrokes implements IMessage, IMessageHandler<PacketElderWandStrokes, IMessage> {

	private byte[] strokes;

	public PacketElderWandStrokes() {

	}

	public PacketElderWandStrokes(byte[] strokes) {
		this.strokes = strokes;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(strokes.length);
		buf.writeBytes(strokes);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		strokes = new byte[buf.readByte()];
		buf.readBytes(strokes);
	}

	@Override
	public IMessage onMessage(PacketElderWandStrokes msg, MessageContext ctx) {
		if(msg.strokes == null || msg.strokes.length == 0) {
			return null;
		}
		EntityPlayer p = ctx.getServerHandler().playerEntity;
		ItemStack wand = p.getHeldItem();
		if(wand == null || wand.getItem() != DHItems.elderWand) {
			return null;
		}
		ItemElderWand.setLastStrokes(wand, msg.strokes);
		return null;
	}

}
