package com.pyding.deathlyhallows.network.packets;

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

public class PacketPropertiesCosmetic implements IMessage, IMessageHandler<PacketPropertiesCosmetic, IMessage> {
	
	private String playerID;
	private NBTTagCompound props;

	public PacketPropertiesCosmetic() {
		
	}

	public PacketPropertiesCosmetic(EntityPlayer p) {
		playerID = p.getCommandSenderName();
		props = DeathlyProperties.get(p).getCosmeticData();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, playerID);
		ByteBufUtils.writeTag(buf, props);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		playerID = ByteBufUtils.readUTF8String(buf);
		props = ByteBufUtils.readTag(buf);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(PacketPropertiesCosmetic msg, MessageContext ctx) {
		if(msg.playerID == null || msg.props == null) {
			return null;
		}
		EntityPlayer p = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(msg.playerID);
		if(p == null) {
			return null;
		}
		DeathlyProperties props = DeathlyProperties.get(p);
		if(props == null) {
			return null;
		}
		props.setCosmeticData(msg.props);
		return null;
	}
	
}
