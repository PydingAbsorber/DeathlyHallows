package com.pyding.deathlyhallows.items;

import baubles.api.BaubleType;
import com.pyding.deathlyhallows.DeathlyHallows;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemBaubleResurrectionStone extends ItemBaubleBase {
	private static int ticks = 0;

	public ItemBaubleResurrectionStone() {
		super("resurrectionStone", BaubleType.RING);
	}

	public void setIndex(ItemStack stack, int value) {
		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		tag.setInteger("index", value);
	}

	public int getIndex(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null) {
			return 0;
		}
		return tag.getInteger("index");
	}

	@SuppressWarnings("unchecked")
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		if(world.isRemote) {
			return super.onItemRightClick(stack, world, p);
		}
		if(!p.isSneaking()) {
			for(EntityPlayer obj: (List<EntityPlayer>)world.playerEntities) {
				if(obj.getDisplayName().equals(getPlayer(stack)) && getCharges(stack) > 0) {
					setCharges(stack, getCharges(stack) - 1);
					p.addChatComponentMessage(new ChatComponentText("§aPlayer Name: " + obj.getCommandSenderName()));
					p.addChatComponentMessage(new ChatComponentText("§aX=" + obj.posX));
					p.addChatComponentMessage(new ChatComponentText("§aY=" + obj.posY));
					p.addChatComponentMessage(new ChatComponentText("§aZ=" + obj.posZ));
					p.addChatComponentMessage(new ChatComponentText("§aDimension: " + obj.dimension));
					return super.onItemRightClick(stack, world, p);
				}
				p.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("dh.desc.resurrectionStone2")));
			}
			return super.onItemRightClick(stack, world, p);
		}
		List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		if(players.isEmpty()) {
			return super.onItemRightClick(stack, world, p);
		}
		String pName = "";
		int maxIndex = players.size();
		if(getIndex(stack) == 0) {
			pName = players.get(getIndex(stack)).getDisplayName();
			setPlayer(stack, pName);
			p.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("dh.desc.resurrectionStone1", pName)));
			if(getIndex(stack) + 1 <= maxIndex) {
				setIndex(stack, getIndex(stack) + 1);
			}
		}
		else if(getIndex(stack) + 1 > maxIndex) {
			setIndex(stack, 0);
			pName = players.get(getIndex(stack)).getDisplayName();
			setPlayer(stack, pName);
			p.addChatComponentMessage(new ChatComponentText("§aChosen Player: " + pName));
			setIndex(stack, 1);
		}
		else {
			pName = players.get(getIndex(stack)).getDisplayName();
			setPlayer(stack, pName);
			p.addChatComponentMessage(new ChatComponentText("§aChosen Player: " + pName));
			setIndex(stack, getIndex(stack) + 1);
		}
		DeathlyHallows.LOG.info("Chosen Player: " + pName);

		return super.onItemRightClick(stack, world, p);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase e) {
		if(getCharges(stack) >= 3) {
			return;
		}
		if(ticks < 1200) {
			ticks++;
		}
		else {
			ticks = 0;
			setCharges(stack, getCharges(stack) + 1);
		}
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase e) {
		e.worldObj.playSoundAtEntity(e, "dh:ring.1", 1F, 1F);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase e) {
		e.worldObj.playSoundAtEntity(e, "dh:ring.1", 1F, 1F);
	}

	public void setCharges(ItemStack stack, int value) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null) {
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		nbt.setInteger("charge", value);
	}

	public int getCharges(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null) {
			return 0;
		}
		return nbt.getInteger("charge");
	}

	public void setPlayer(ItemStack stack, String value) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null) {
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		nbt.setString("player", value);
	}

	public String getPlayer(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt != null && nbt.hasKey("player")) {
			return nbt.getString("player");
		}
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			l.add(StatCollector.translateToLocal("dh.desc.stone1"));
			l.add(StatCollector.translateToLocal("dh.desc.stone2"));
			l.add(StatCollector.translateToLocal("dh.desc.stone3"));
		}
		else {
			l.add(StatCollector.translateToLocalFormatted("dh.desc.stone4", getCharges(stack)));
			l.add(StatCollector.translateToLocalFormatted("dh.desc.stone5", getPlayer(stack)));
			l.add(StatCollector.translateToLocal("dh.desc.stone6"));
		}
		String owner = stack.hasTagCompound() ? stack.getTagCompound().getString("dhowner") : "";
		if(owner.equals("")) {
			owner = StatCollector.translateToLocal("dh.desc.defaultOwner");
		}
		l.add(StatCollector.translateToLocalFormatted("dh.desc.stone7", owner));
		l.add(StatCollector.translateToLocal("dh.desc.stone8"));
	}
	
}
