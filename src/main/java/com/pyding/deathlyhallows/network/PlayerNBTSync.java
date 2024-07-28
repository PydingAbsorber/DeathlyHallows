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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class PlayerNBTSync implements IMessage, IMessageHandler<PlayerNBTSync, IMessage> {
	public NBTTagCompound nbtData;

	public PlayerNBTSync() {
	}

	public PlayerNBTSync(NBTTagCompound nbt) {
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
	public IMessage onMessage(PlayerNBTSync message, MessageContext ctx) {
		if(message.nbtData == null)
			return null;
		if(ctx.side == Side.CLIENT) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			/*ExtendedPlayer props = ExtendedPlayer.get(player);
			if(props == null) {
				props = new ExtendedPlayer(player);
			}
			props.loadNBTData(message.nbtData);*/
			player.readFromNBT(message.nbtData);
		}
		return null;
	}
}
