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

public class PacketElderWandLastSpell implements IMessage, IMessageHandler<PacketElderWandLastSpell, IMessage> {

	private int index = -1;

	public PacketElderWandLastSpell() {

	}

	public PacketElderWandLastSpell(int index) {
		this.index = index;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte((byte)index);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		index = buf.readByte();
	}

	@Override
	public IMessage onMessage(PacketElderWandLastSpell msg, MessageContext ctx) {
		EntityPlayer p = ctx.getServerHandler().playerEntity;
		ItemStack wand = p.getHeldItem();
		if(wand == null || wand.getItem() != DHItems.elderWand) {
			return null;
		}
		if(msg.index == -1) {
			ItemElderWand.setBinding(p, false);
			return null;
		}
		SoundEffect.NOTE_HARP.playAtPlayer(p.worldObj, p, 1.0F);
		int size = ItemElderWand.getLastSpells(wand).tagCount();
		if(size < ItemElderWand.MAX_SPELLS && msg.index == size) {
			ItemElderWand.setBinding(p, true);
			return null;
		}
		ItemElderWand.castLastSpell(p, wand, msg.index);
		return null;
	}

}
