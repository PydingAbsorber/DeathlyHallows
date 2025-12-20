package com.pyding.deathlyhallows.network.packets;

import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.items.ItemElderWand;
import com.pyding.deathlyhallows.utils.ElfUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

public class AbobaPacket implements IMessage, IMessageHandler<AbobaPacket, IMessage> {

	private int number;

	public AbobaPacket() {

	}

	public AbobaPacket(int number) {
		this.number = number;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(number);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		buf.readInt();
	}

	@Override
	public IMessage onMessage(AbobaPacket msg, MessageContext ctx) {
		EntityPlayer p = ctx.getServerHandler().playerEntity;
		ElfUtils.badPotions.clear();
		for(Potion potion: Potion.potionTypes){
			if(potion != null && potion.isBadEffect())
				ElfUtils.badPotions.add(potion);
		}
		return null;
	}

}
