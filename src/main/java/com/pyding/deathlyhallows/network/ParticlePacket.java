package com.pyding.deathlyhallows.network;

import com.pyding.deathlyhallows.client.particles.ParticleBlueMagic;
import com.pyding.deathlyhallows.common.properties.ExtendedPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.awt.*;
import java.util.List;

public class ParticlePacket implements IMessage, IMessageHandler<ParticlePacket, IMessage> {
	double x;
	double y;
	double z;
	int color;
	float resizeSpeed;
	float scale;
	int age;
	int type;
	float motionX;
	float motionY;
	float motionZ;
	
	public ParticlePacket() {
	}

	public ParticlePacket(double x, double y, double z, Color color, float resizeSpeed, float scale, int age,int type,float motionX,float motionY,float motionZ) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color.getRGB();
		this.resizeSpeed = resizeSpeed;
		this.scale = scale;
		this.age = age;
		this.type = type;
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		color = buf.readInt();
		resizeSpeed = buf.readFloat();
		scale = buf.readFloat();
		age = buf.readInt();
		type = buf.readInt();
		motionX = buf.readFloat();
		motionY = buf.readFloat();
		motionZ = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeInt(color);
		buf.writeFloat(resizeSpeed);
		buf.writeFloat(scale);
		buf.writeInt(age);
		buf.writeInt(type);
		buf.writeFloat(motionX);
		buf.writeFloat(motionY);
		buf.writeFloat(motionZ);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(ParticlePacket message, MessageContext ctx) {
		if(ctx.side == Side.CLIENT) {
			if(message.type == 1) {
				ParticleBlueMagic particle = new ParticleBlueMagic(Minecraft.getMinecraft().theWorld, message.x, message.y, message.z, new Color(message.color), message.resizeSpeed, message.scale, message.age);
				particle.motionX = message.motionX;
				particle.motionY = message.motionY;
				particle.motionZ = message.motionZ;
				particle.noClip = true;
				Minecraft.getMinecraft().effectRenderer.addEffect(particle);
			}
		}
		return null;
	}
}
