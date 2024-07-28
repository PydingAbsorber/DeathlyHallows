package com.pyding.deathlyhallows.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ResurrectionStone extends Item implements IBauble {
	private static int ticks = 0;

	@Override
	public BaubleType getBaubleType(ItemStack itemStack) {
		return BaubleType.RING;
	}

	public void setIndex(ItemStack stack, int value) {
		NBTTagCompound nbt = stack.getTagCompound(); // Получаем NBTTagCompound из ItemStack
		if(nbt == null) { // Если NBTTagCompound не существует, создаем новый
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		nbt.setInteger("index", value); // Записываем значение float в NBT с использованием указанного тега
	}

	public int getIndex(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound(); // Получаем NBTTagCompound из ItemStack
		if(nbt != null && nbt.hasKey("index")) { // Если NBTTagCompound существует и содержит указанный тег
			return nbt.getInteger("index"); // Возвращаем значение float из NBT
		}
		return 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(!world.isRemote) {
			if(player.isSneaking()) {
				List<EntityPlayerMP> playerList = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
				if(!playerList.isEmpty()) {
					String selectedPlayerName = "";
					int maxIndex = playerList.size();
					if(getIndex(stack) == 0) {
						selectedPlayerName = playerList.get(getIndex(stack)).getDisplayName();
						setPlayer(stack, selectedPlayerName);
						player.addChatComponentMessage(new ChatComponentText("§aChosen Player: " + selectedPlayerName));
						if(getIndex(stack) + 1 <= maxIndex) {
							setIndex(stack, getIndex(stack) + 1);
						}
					}
					else if(getIndex(stack) + 1 > maxIndex) {
						setIndex(stack, 0);
						selectedPlayerName = playerList.get(getIndex(stack)).getDisplayName();
						setPlayer(stack, selectedPlayerName);
						player.addChatComponentMessage(new ChatComponentText("§aChosen Player: " + selectedPlayerName));
						setIndex(stack, 1);
					}
					else {
						selectedPlayerName = playerList.get(getIndex(stack)).getDisplayName();
						setPlayer(stack, selectedPlayerName);
						player.addChatComponentMessage(new ChatComponentText("§aChosen Player: " + selectedPlayerName));
						setIndex(stack, getIndex(stack) + 1);
					}
					System.out.println("Chosen Player: " + selectedPlayerName);
				}
			}
			else {
				for(Object obj: world.playerEntities) {
					if(obj instanceof EntityPlayer && ((EntityPlayer)obj).getDisplayName() == (getPlayer(stack)) && getCharges(stack) > 0) {
						setCharges(stack, getCharges(stack) - 1);
						EntityPlayer otherPlayer = (EntityPlayer)obj;
						player.addChatComponentMessage(new ChatComponentText("§aPlayer Name: " + otherPlayer.getCommandSenderName()));
						player.addChatComponentMessage(new ChatComponentText("§aX=" + otherPlayer.posX));
						player.addChatComponentMessage(new ChatComponentText("§aY=" + otherPlayer.posY));
						player.addChatComponentMessage(new ChatComponentText("§aZ=" + otherPlayer.posZ));
						player.addChatComponentMessage(new ChatComponentText("§aDimension: " + otherPlayer.dimension));
					}
					else {
						player.addChatComponentMessage(new ChatComponentText("§4Player is offline/Not enough charges"));
					}
				}
			}
		}
		return super.onItemRightClick(stack, world, player);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}

	@Override
	public void onWornTick(ItemStack itemStack, EntityLivingBase entityLivingBase) {
		if(getCharges(itemStack) < 3) {
			if(ticks < 1200) {
				ticks++;
			}
			else {
				ticks = 0;
				setCharges(itemStack, getCharges(itemStack) + 1);
			}
		}
	}

	@Override
	public void onEquipped(ItemStack itemStack, EntityLivingBase entityLivingBase) {
		World world = entityLivingBase.worldObj;
		world.playSoundAtEntity(entityLivingBase, "dh:ring.1", 1F, 1F);
	}

	@Override
	public void onUnequipped(ItemStack itemStack, EntityLivingBase entityLivingBase) {
		World world = entityLivingBase.worldObj;
		world.playSoundAtEntity(entityLivingBase, "dh:ring.1", 1F, 1F);
	}

	@Override
	public boolean canEquip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
		return true;
	}

	public void setCharges(ItemStack stack, int value) {
		NBTTagCompound nbt = stack.getTagCompound(); // Получаем NBTTagCompound из ItemStack
		if(nbt == null) { // Если NBTTagCompound не существует, создаем новый
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		nbt.setInteger("charge", value); // Записываем значение float в NBT с использованием указанного тега
	}

	public int getCharges(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound(); // Получаем NBTTagCompound из ItemStack
		if(nbt != null && nbt.hasKey("charge")) { // Если NBTTagCompound существует и содержит указанный тег
			return nbt.getInteger("charge"); // Возвращаем значение float из NBT
		}
		return 0;
	}

	public void setPlayer(ItemStack stack, String value) {
		NBTTagCompound nbt = stack.getTagCompound(); // Получаем NBTTagCompound из ItemStack
		if(nbt == null) { // Если NBTTagCompound не существует, создаем новый
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		nbt.setString("player", value); // Записываем значение float в NBT с использованием указанного тега
	}

	public String getPlayer(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound(); // Получаем NBTTagCompound из ItemStack
		if(nbt != null && nbt.hasKey("player")) { // Если NBTTagCompound существует и содержит указанный тег
			return nbt.getString("player"); // Возвращаем значение float из NBT
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			list.add(I18n.format("dh.desc.stone1"));
			list.add(I18n.format("dh.desc.stone2"));
			list.add(I18n.format("dh.desc.stone3"));
		}
		else {
			list.add(I18n.format("dh.desc.stone4") + getCharges(stack));
			list.add(I18n.format("dh.desc.stone5") + getPlayer(stack));
			list.add(I18n.format("dh.desc.stone6"));
		}
		if(stack.hasTagCompound()) {
			if(stack.getTagCompound().hasKey("dhowner")) {
				list.add(I18n.format("dh.desc.stone7") + stack.getTagCompound().getString("dhowner"));
			}
		}
		else {
			list.add(I18n.format("dh.desc.stone8"));
		}
		list.add(I18n.format("dh.desc.stone9"));
	}
}
