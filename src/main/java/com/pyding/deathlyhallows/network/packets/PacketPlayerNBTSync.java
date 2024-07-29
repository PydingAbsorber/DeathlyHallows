package com.pyding.deathlyhallows.network.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PacketPlayerNBTSync implements IMessage, IMessageHandler<PacketPlayerNBTSync, IMessage> {
	public NBTTagCompound nbtData;

	public PacketPlayerNBTSync() {
	}

	public PacketPlayerNBTSync(NBTTagCompound nbt) {
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
	public IMessage onMessage(PacketPlayerNBTSync message, MessageContext ctx) {
		if(message.nbtData == null) {
			return null;
		}
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		player.readFromNBT(message.nbtData);
		return null;
	}
}
