package com.pyding.deathlyhallows.network.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class PacketPropertiesSync implements IMessage, IMessageHandler<PacketPropertiesSync, IMessage> {
	
	private String id;
	private NBTTagCompound props;

	public PacketPropertiesSync() {
		
	}

	public PacketPropertiesSync(Entity e, String identifier) {
		id = identifier;
		props = new NBTTagCompound();
		e.getExtendedProperties(identifier).saveNBTData(props);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, id);
		ByteBufUtils.writeTag(buf, props);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		id = ByteBufUtils.readUTF8String(buf);
		props = ByteBufUtils.readTag(buf);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(PacketPropertiesSync message, MessageContext ctx) {
		if(message.id == null || message.props == null) {
			return null;
		}
		Minecraft.getMinecraft().thePlayer.getExtendedProperties(message.id).loadNBTData(message.props);
		return null;
	}
	
}
