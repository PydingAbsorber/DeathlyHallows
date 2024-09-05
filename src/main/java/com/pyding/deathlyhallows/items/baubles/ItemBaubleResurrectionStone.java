package com.pyding.deathlyhallows.items.baubles;

import baubles.api.BaubleType;
import com.emoniph.witchery.util.ChatUtil;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.utils.DHUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.function.Function;

public class ItemBaubleResurrectionStone extends ItemBaubleBase {
	private static final int
			TICKS_TO_REGEN_POINT = 1200,
			MAX_POINTS = 3;

	public ItemBaubleResurrectionStone() {
		super("resurrectionStone", BaubleType.RING);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		if(world.isRemote) {
			return super.onItemRightClick(stack, world, p);
		}
		if(p.isSneaking()) {
			List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
			if(players.isEmpty()) {
				return super.onItemRightClick(stack, world, p);
			}
			String pName;
			int maxIndex = players.size();
			if(getIndex(stack) == 0) {
				pName = players.get(getIndex(stack)).getCommandSenderName();
				if(getIndex(stack) + 1 <= maxIndex) {
					setIndex(stack, getIndex(stack) + 1);
				}
			}
			else if(getIndex(stack) + 1 > maxIndex) {
				setIndex(stack, 0);
				pName = players.get(getIndex(stack)).getCommandSenderName();
				setIndex(stack, 1);
			}
			else {
				pName = players.get(getIndex(stack)).getCommandSenderName();
				setIndex(stack, getIndex(stack) + 1);
			}
			setPlayer(stack, pName);
			ChatUtil.sendTranslated(p, "dh.desc.resurrectionStone1", pName);
			DeathlyHallows.LOG.info("Chosen Player: " + pName);

			return super.onItemRightClick(stack, world, p);
		}

		if(!p.capabilities.isCreativeMode && getCharges(stack) <= 0) {
			ChatUtil.sendTranslated(p, "dh.desc.resurrectionStone3");
			return super.onItemRightClick(stack, world, p);
		}
		String pName = getPlayer(stack);
		if(pName == null || pName.equals("")) {
			ChatUtil.sendTranslated(p, "dh.desc.resurrectionStone4");
			return super.onItemRightClick(stack, world, p);
		}
		MinecraftPosition pos;

		EntityPlayer obj = world.getPlayerEntityByName(pName);

		if(obj == null) {
			NBTTagCompound tag = DHUtils.readOfflinePlayer(pName);
			if(tag == null) {
				ChatUtil.sendTranslated(p, "dh.desc.resurrectionStone4");
				return super.onItemRightClick(stack, world, p);
			}
			pos = new MinecraftPosition(tag);
		}
		else {
			pos = new MinecraftPosition(obj);
		}
		if(!p.capabilities.isCreativeMode) {
			setCharges(stack, getCharges(stack) - 1);
		}
		// minecraft replaces all %d %.1f and other codes to be simple %s, IDK why, so formatting manually
		Function<Double, String> format = (d) -> String.format("%.1f", d);
		String dimensionName = MinecraftServer.getServer().worldServerForDimension(pos.dimensionID).provider.getDimensionName();
		ChatUtil.sendTranslated(p, "dh.desc.resurrectionStone2", pName, format.apply(pos.x), format.apply(pos.y), format.apply(pos.z), dimensionName, pos.dimensionID);
		return super.onItemRightClick(stack, world, p);
	}

	// found this way better than creating Object[4] or creating 4 separate var's. I hope it will be inlined in runtime
	private static class MinecraftPosition {
		private final double x, y, z;
		private final int dimensionID;

		private MinecraftPosition(Entity e) {
			x = e.posX;
			y = e.posY;
			z = e.posZ;
			dimensionID = e.dimension;
		}

		private MinecraftPosition(NBTTagCompound tag) {
			NBTTagList pos = tag.getTagList("Pos", Constants.NBT.TAG_DOUBLE);
			x = pos.func_150309_d(0);
			y = pos.func_150309_d(1);
			z = pos.func_150309_d(2);
			dimensionID = tag.getInteger("Dimension");
		}

	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase e) {
		if(e.worldObj.isRemote) {
			return;
		}
		int charges = getCharges(stack);
		if(charges >= MAX_POINTS) {
			return;
		}
		int time = getTime(stack);
		if(e.ticksExisted < time) {
			setTime(stack, e.ticksExisted);
			return;
		}
		if(e.ticksExisted - time < TICKS_TO_REGEN_POINT) {
			return;
		}
		++charges;
		setTime(stack, e.ticksExisted);
		setCharges(stack, charges);
		if(charges >= MAX_POINTS) {
			setTime(stack, Integer.MAX_VALUE);
		}
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase e) {
		e.worldObj.playSoundAtEntity(e, "dh:ring.1", 1F, 1F);
		setTime(stack, e.ticksExisted);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase e) {
		e.worldObj.playSoundAtEntity(e, "dh:ring.1", 1F, 1F);
	}


	public static void setIndex(ItemStack stack, int value) {
		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		tag.setInteger("index", value);
	}

	public static int getIndex(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null) {
			return 0;
		}
		return tag.getInteger("index");
	}

	public static void setCharges(ItemStack stack, int value) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null) {
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		nbt.setInteger("charge", value);
	}

	public static int getCharges(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null) {
			return 0;
		}
		return nbt.getInteger("charge");
	}

	public static void setPlayer(ItemStack stack, String value) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null) {
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		nbt.setString("player", value);
	}

	public static String getPlayer(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt != null && nbt.hasKey("player")) {
			return nbt.getString("player");
		}
		return null;
	}

	public static void setTime(ItemStack stack, int time) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null) {
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		if(time == Integer.MAX_VALUE) {
			nbt.removeTag("timer");
		}
		nbt.setInteger("timer", time);
	}

	public static int getTime(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null) {
			return Integer.MAX_VALUE;
		}
		return nbt.getInteger("timer");
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
		l.add(StatCollector.translateToLocalFormatted("dh.desc.owner", owner));
		l.add(StatCollector.translateToLocal("dh.desc.hallow"));
	}

}
