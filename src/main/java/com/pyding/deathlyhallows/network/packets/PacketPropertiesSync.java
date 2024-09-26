package com.pyding.deathlyhallows.network.packets;

import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
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

public class PacketPropertiesSync  {
	
	public enum Type {
		FULL,
		COSMETIC
	}
	
	public static class Server implements IMessage, IMessageHandler<Server, IMessage> {

		private Type type;
		private String targetName;

		public Server() {

		}

		public Server(Type type, String targetName) {
			this.type = type;
			this.targetName = targetName;
		}
		
		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeByte(type.ordinal());
			ByteBufUtils.writeUTF8String(buf, targetName);
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			type = Type.values()[buf.readByte()];
			targetName = ByteBufUtils.readUTF8String(buf);
		}
		
		@Override
		public IMessage onMessage(Server msg, MessageContext ctx) {
			EntityPlayer sender = ctx.getServerHandler().playerEntity;
			EntityPlayer target = sender.getCommandSenderName().equals(msg.targetName) ? sender : sender.worldObj.getPlayerEntityByName(msg.targetName);
			if(target != null) { // there is no point in syncing data of absent player
				DHPacketProcessor.sendToPlayer(new Client(msg.type, target), sender);
			}
			return null;
		}
		
	}

	public static class Client implements IMessage, IMessageHandler<Client, IMessage> {

		private Type type;
		private String playerName;
		private NBTTagCompound propsTag;

		public Client() {

		}

		public Client(Type type, EntityPlayer p) {
			this.type = type;
			playerName = p.getCommandSenderName();
			propsTag = getTagData(type, DeathlyProperties.get(p));
			
		}
		
		private NBTTagCompound getTagData(Type type, DeathlyProperties props) {
			NBTTagCompound tag = new NBTTagCompound();
			switch(type) {
				case FULL: {
					props.saveNBTData(tag);
					break;
				}
				case COSMETIC: {
					props.saveCosmeticData(tag);
					break;
				}
			}
			return tag;
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeByte(type.ordinal());
			ByteBufUtils.writeUTF8String(buf, playerName);
			ByteBufUtils.writeTag(buf, propsTag);
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			type = Type.values()[buf.readByte()];
			playerName = ByteBufUtils.readUTF8String(buf);
			propsTag = ByteBufUtils.readTag(buf);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(Client msg, MessageContext ctx) {
			if(msg.playerName == null || msg.propsTag == null) {
				return null;
			}
			EntityPlayer p = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(msg.playerName);
			if(p == null) { // player isn't loaded by client
				return null;
			}
			DeathlyProperties props = DeathlyProperties.get(p);
			if(props == null) {
				return null;
			}
			switch(msg.type) {
				case FULL: {
					props.loadNBTData(msg.propsTag);
					break;
				}
				case COSMETIC: {
					props.loadCosmeticData(msg.propsTag);
					break;
				}
			}
			return null;
		}

	}
	
}
