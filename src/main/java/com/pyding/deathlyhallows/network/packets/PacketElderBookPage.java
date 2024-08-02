package com.pyding.deathlyhallows.network.packets;

import com.pyding.deathlyhallows.items.DHItems;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PacketElderBookPage implements IMessage, IMessageHandler<PacketElderBookPage, IMessage> {
	
	int slot = -1, currPage, visualizedPage = -1;

	public PacketElderBookPage() {

	}

	public PacketElderBookPage(int slot, int currPage, int visualizedPage) {
		this.slot = slot;
		this.currPage = currPage;
		this.visualizedPage = visualizedPage;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(slot);
		buf.writeInt(currPage);
		buf.writeInt(visualizedPage);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		slot = buf.readInt();
		currPage = buf.readInt();
		visualizedPage = buf.readInt();
	}

	@Override
	public IMessage onMessage(PacketElderBookPage msg, MessageContext ctx) {
		EntityPlayerMP p = ctx.getServerHandler().playerEntity;
		if(msg.slot == -1) {
			return null;
		}
		ItemStack book = p.inventory.getStackInSlot(msg.slot);
		if(book == null || book.getItem() != DHItems.elderBook) {
			return null;
		}
		NBTTagCompound tag = book.getTagCompound();
		if(tag == null) {
			tag = new NBTTagCompound();
			book.setTagCompound(tag);
		}

		tag.setInteger("CurrentPage", msg.currPage);
		if(msg.visualizedPage != -1) {
			tag.setInteger("VisualizedPage", msg.visualizedPage);
		}
		else {
			tag.removeTag("VisualizedPage");
		}
		return null;
	}

}