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
import net.minecraftforge.common.IExtendedEntityProperties;

public class PacketPropertiesToClient implements IMessage, IMessageHandler<PacketPropertiesToClient, IMessage> {
	
	private int entityID;
	private String propsID;
	private NBTTagCompound props;

	public PacketPropertiesToClient() {
		
	}

	public PacketPropertiesToClient(Entity e, String identifier) {
		entityID = e.getEntityId();
		propsID = identifier;
		props = new NBTTagCompound();
		e.getExtendedProperties(identifier).saveNBTData(props);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		ByteBufUtils.writeUTF8String(buf, propsID);
		ByteBufUtils.writeTag(buf, props);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		entityID = buf.readInt();
		propsID = ByteBufUtils.readUTF8String(buf);
		props = ByteBufUtils.readTag(buf);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(PacketPropertiesToClient msg, MessageContext ctx) {
		if(msg.propsID == null || msg.props == null) {
			return null;
		}
		Entity e = Minecraft.getMinecraft().theWorld.getEntityByID(msg.entityID);
		if(e == null) {
			return null;
		}
		IExtendedEntityProperties props = e.getExtendedProperties(msg.propsID);
		if(props == null) {
			return null;
		}
		props.loadNBTData(msg.props);
		return null;
	}
	
}
