package com.pyding.deathlyhallows.network;

import com.pyding.deathlyhallows.common.properties.ExtendedPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class NBTSync implements IMessage, IMessageHandler<NBTSync, IMessage> {
	public NBTTagCompound nbtData;

	public int id;

	public NBTSync() {
	}

	public NBTSync(NBTTagCompound nbt, int identifier) {
		nbtData = nbt;
		id = identifier;
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
	public IMessage onMessage(NBTSync message, MessageContext ctx) {
		if(message.nbtData == null)
			return null;
		if(ctx.side == Side.CLIENT) {
			List entityList = Minecraft.getMinecraft().theWorld.getLoadedEntityList();
			for(Object o: entityList){
				Entity entity = (Entity)o;
				if(entity.getEntityId() == id) {
					if(entity instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().theWorld.getEntityByID(message.id);
						player.readFromNBT(nbtData);
						ExtendedPlayer props = ExtendedPlayer.get(player);
						if(props == null) {
							props = new ExtendedPlayer(player);
						}
						props.loadNBTData(nbtData);
					}
					else {
						entity.readFromNBT(nbtData);
					}
				}
			}
		}
		return null;
	}
}
