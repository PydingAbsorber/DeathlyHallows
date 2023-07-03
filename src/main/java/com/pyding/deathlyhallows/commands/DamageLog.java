package com.pyding.deathlyhallows.commands;

import com.pyding.deathlyhallows.common.properties.ExtendedPlayer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

public class DamageLog extends CommandBase {
    @Override
    public String getCommandName() {
        return "deathlyhallows";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/deathlyhallows <damagelog>";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("damagelog")) {
            if (sender instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) sender;
                ExtendedPlayer props = ExtendedPlayer.get(player);
                if(props.getDamageLog()){
                    props.setDamageLog(false);
                    player.addChatMessage(new ChatComponentText("Damage Log disabled for " + player.getDisplayName()));
                } else {
                    props.setDamageLog(true);
                    player.addChatMessage(new ChatComponentText("Damage Log enabled for " + player.getDisplayName()));
                }
            }
        } else {
            sender.addChatMessage(new ChatComponentText("§4Invalid command usage. Use: /deathlyhallows damagelog"));
        }
    }
}
