package com.pyding.deathlyhallows.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerRenderPacket implements IMessage, IMessageHandler<com.pyding.deathlyhallows.network.PlayerRenderPacket, IMessage> {
	public NBTTagCompound nbtData;


	public PlayerRenderPacket() {
	}

	public PlayerRenderPacket(NBTTagCompound nbt) {
		nbtData = nbt;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		nbtData = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, nbtData);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(PlayerRenderPacket message, MessageContext ctx) {
		if(ctx.side == Side.CLIENT) {
			EntityPlayer entity = ctx.getServerHandler().playerEntity;
			if(message.nbtData != null) {
				entity.getEntityData().setTag("dhRenderData", message.nbtData);
			}
		}
		return null;
	}
}
