package com.pyding.deathlyhallows.network.packets;

import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.ItemElderWand;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PacketElderWandListSpell implements IMessage, IMessageHandler<PacketElderWandListSpell, IMessage> {

	private int index = -1;
	public PacketElderWandListSpell() {

	}

	public PacketElderWandListSpell(int index) {
		this.index = index;
	}	

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeShort(index);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		index = buf.readShort();
	}

	@Override
	public IMessage onMessage(PacketElderWandListSpell msg, MessageContext ctx) {
		EntityPlayer p = ctx.getServerHandler().playerEntity;
		ItemStack wand = p.getHeldItem();
		if(msg.index < 0 || wand == null || wand.getItem() != DHItems.elderWand || ItemElderWand.getMode(wand) != ItemElderWand.EnumCastingMode.LIST) {
			return null;
		}
		SoundEffect.NOTE_HARP.playAtPlayer(p.worldObj, p, 1.0F);
		NBTTagCompound tag = wand.getTagCompound();
		if(tag == null) {
			tag = new NBTTagCompound();
			wand.setTagCompound(tag);
		}
		ItemElderWand.setListCounter(tag, msg.index);
		return null;
	}

}
