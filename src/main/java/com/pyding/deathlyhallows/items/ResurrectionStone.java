package com.pyding.deathlyhallows.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
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
        if (nbt == null) { // Если NBTTagCompound не существует, создаем новый
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
        }
        nbt.setInteger("index", value); // Записываем значение float в NBT с использованием указанного тега
    }

    public int getIndex(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound(); // Получаем NBTTagCompound из ItemStack
        if (nbt != null && nbt.hasKey("index")) { // Если NBTTagCompound существует и содержит указанный тег
            return nbt.getInteger("index"); // Возвращаем значение float из NBT
        }
        return 0;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            if(player.isSneaking()) {
                List<EntityPlayerMP> playerList = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
                if (!playerList.isEmpty()) {
                    String selectedPlayerName = "";
                    int maxIndex = playerList.size();
                    if (getIndex(stack) == 0) {
                        selectedPlayerName = playerList.get(getIndex(stack)).getDisplayName();
                        setPlayer(stack,selectedPlayerName);
                        player.addChatComponentMessage(new ChatComponentText("§aChosen Player: " + selectedPlayerName));
                        if(getIndex(stack)+1 <= maxIndex)
                            setIndex(stack,getIndex(stack)+1);
                    }
                    else if(getIndex(stack)+1 > maxIndex) {
                        setIndex(stack,0);
                        selectedPlayerName = playerList.get(getIndex(stack)).getDisplayName();
                        setPlayer(stack,selectedPlayerName);
                        player.addChatComponentMessage(new ChatComponentText("§aChosen Player: " + selectedPlayerName));
                        setIndex(stack,1);
                    }
                    else {
                        selectedPlayerName = playerList.get(getIndex(stack)).getDisplayName();
                        setPlayer(stack,selectedPlayerName);
                        player.addChatComponentMessage(new ChatComponentText("§aChosen Player: " + selectedPlayerName));
                        setIndex(stack,getIndex(stack)+1);
                    }
                    System.out.println("Chosen Player: " + selectedPlayerName);
                }
            }
            else {
                for (Object obj : world.playerEntities) {
                    if (obj instanceof EntityPlayer && ((EntityPlayer) obj).getDisplayName() == (getPlayer(stack)) && getCharges(stack) > 0) {
                        setCharges(stack,getCharges(stack)-1);
                        EntityPlayer otherPlayer = (EntityPlayer) obj;
                        player.addChatComponentMessage(new ChatComponentText("§aPlayer Name: " + otherPlayer.getCommandSenderName()));
                        player.addChatComponentMessage(new ChatComponentText("§aX=" + otherPlayer.posX));
                        player.addChatComponentMessage(new ChatComponentText("§aY=" + otherPlayer.posY));
                        player.addChatComponentMessage(new ChatComponentText("§aZ=" + otherPlayer.posZ));
                        player.addChatComponentMessage(new ChatComponentText("§aDimension: " + otherPlayer.dimension));
                    } else {
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
            if (ticks < 1200) {
                ticks++;
            } else {
                ticks = 0;
                setCharges(itemStack,getCharges(itemStack)+1);
            }
        }
    }

    @Override
    public void onEquipped(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        World world = entityLivingBase.worldObj;
        world.playSoundAtEntity(entityLivingBase,"dh:ring.1",1F,1F);
    }

    @Override
    public void onUnequipped(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        World world = entityLivingBase.worldObj;
        world.playSoundAtEntity(entityLivingBase,"dh:ring.1",1F,1F);
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
        if (nbt == null) { // Если NBTTagCompound не существует, создаем новый
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
        }
        nbt.setInteger("charge", value); // Записываем значение float в NBT с использованием указанного тега
    }

    public int getCharges(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound(); // Получаем NBTTagCompound из ItemStack
        if (nbt != null && nbt.hasKey("charge")) { // Если NBTTagCompound существует и содержит указанный тег
            return nbt.getInteger("charge"); // Возвращаем значение float из NBT
        }
        return 0;
    }

    public void setPlayer(ItemStack stack, String value) {
        NBTTagCompound nbt = stack.getTagCompound(); // Получаем NBTTagCompound из ItemStack
        if (nbt == null) { // Если NBTTagCompound не существует, создаем новый
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
        }
        nbt.setString("player", value); // Записываем значение float в NBT с использованием указанного тега
    }

    public String getPlayer(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound(); // Получаем NBTTagCompound из ItemStack
        if (nbt != null && nbt.hasKey("player")) { // Если NBTTagCompound существует и содержит указанный тег
            return nbt.getString("player"); // Возвращаем значение float из NBT
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        String currentLanguage = StatCollector.translateToLocal("language.name");

        if (I18n.format("dh.util.language").equals("Ru")) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                list.add("Нажми §ashift+ПКМ §7с кольцом в руке, чтобы выбрать игрока");
                list.add("Нажми §aПКМ §7чтобы найти выбранного игрока за §a1 заряд воскрешения");
                list.add("§aВоскрешения работают только когда кольцо надето");
            } else {
                list.add("Воскрешений осталось:§a " + getCharges(stack));
                list.add("Выбранный игрок:§a " + getPlayer(stack));
                list.add("Нажми §ashift §7для дополнительной информации");
            }
            if(stack.hasTagCompound()){
                if(stack.getTagCompound().hasKey("dhowner")) {
                    list.add("Владелец §9" + stack.getTagCompound().getString("dhowner"));
                }
            } else list.add("Владелец §9Смерть");
            list.add("Возможно иметь лишь один дар у себя в инвентаре");
        } else {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                list.add("Press §ashift+RMB §7with ring in hand to select a player");
                list.add("Press §aRMB §7to search for the selected player for a cost of §a1 resurrection");
                list.add("§aResurrections work only if the ring is equipped");
            } else {
                list.add("Resurrections left:§a " + getCharges(stack));
                list.add("Chosen Player:§a " + getPlayer(stack));
                list.add("Press §ashift §7for additional information");
            }
            if(stack.hasTagCompound()){
                if(stack.getTagCompound().hasKey("dhowner")) {
                    list.add("Owner §9" + stack.getTagCompound().getString("dhowner"));
                }
            } else list.add("Owner §9Death");
            list.add("You can have only one Hallow at the time");
        }
    }
}
