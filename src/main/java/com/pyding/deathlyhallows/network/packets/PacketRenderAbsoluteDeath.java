package com.pyding.deathlyhallows.network.packets;

import com.pyding.deathlyhallows.entities.EntityAbsoluteDeath;
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

public class PacketRenderAbsoluteDeath implements IMessage, IMessageHandler<PacketRenderAbsoluteDeath, IMessage> {
	public NBTTagCompound nbtData;

	public int id;

	public PacketRenderAbsoluteDeath() {
		
	}

	public PacketRenderAbsoluteDeath(EntityAbsoluteDeath death) {
		nbtData = death.getResistsData();
		id = death.getEntityId();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		nbtData = ByteBufUtils.readTag(buf);
		id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, nbtData);
		buf.writeInt(id);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(PacketRenderAbsoluteDeath message, MessageContext ctx) {
		if(message.nbtData == null) {
			return null;
		}
		Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(message.id);
		if(entity instanceof EntityAbsoluteDeath) {
			((EntityAbsoluteDeath)entity).setResists(message.nbtData);
		}
		return null;
	}
	
}
