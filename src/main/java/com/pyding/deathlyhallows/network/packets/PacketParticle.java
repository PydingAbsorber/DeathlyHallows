package com.pyding.deathlyhallows.network.packets;

import com.pyding.deathlyhallows.particles.ParticleBlueMagic;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class PacketParticle implements IMessage, IMessageHandler<PacketParticle, IMessage> {
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

	public PacketParticle() {
	}

	public PacketParticle(double x, double y, double z, Color color, float resizeSpeed, float scale, int age, int type, float motionX, float motionY, float motionZ) {
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
	public IMessage onMessage(PacketParticle message, MessageContext ctx) {
		if(message.type == 1) {
			ParticleBlueMagic particle = new ParticleBlueMagic(Minecraft.getMinecraft().theWorld, message.x, message.y, message.z, new Color(message.color), message.resizeSpeed, message.scale, message.age);
			particle.motionX = message.motionX;
			particle.motionY = message.motionY;
			particle.motionZ = message.motionZ;
			particle.noClip = true;
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		}
		return null;
	}
}
