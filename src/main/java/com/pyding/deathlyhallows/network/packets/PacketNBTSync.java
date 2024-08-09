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

public class PacketNBTSync implements IMessage, IMessageHandler<PacketNBTSync, IMessage> {
	public NBTTagCompound tag;

	public int id;

	public PacketNBTSync() {
		
	}

	public PacketNBTSync(NBTTagCompound tag, int id) {
		this.tag = tag;
		this.id = id;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(id);
		ByteBufUtils.writeTag(buf, tag);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		id = buf.readInt();
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(PacketNBTSync msg, MessageContext ctx) {
		if(msg.tag == null) {
			return null;
		}
		Entity e = Minecraft.getMinecraft().theWorld.getEntityByID(msg.id);
		if(e == null) {
			return null;
		}
		NBTTagCompound entityData = new NBTTagCompound();
		e.writeToNBT(entityData);
		entityData.setTag("ForgeData", msg.tag);
		e.readFromNBT(entityData);
		return null;
	}
	
}
