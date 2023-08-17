package com.pyding.deathlyhallows.commands;

import com.pyding.deathlyhallows.blocks.VisConverter;
import com.pyding.deathlyhallows.common.properties.ExtendedPlayer;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.List;

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
        } else if(args.length > 0 && args[0].equalsIgnoreCase("getid")){
            if (sender instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) sender;
                if(player.getHeldItem() != null)
                player.addChatMessage(new ChatComponentText("§bitem class name is: " + player.getHeldItem().getItem().getClass().getName()));
                player.addChatMessage(new ChatComponentText("§9item unlock name is: " + player.getHeldItem().getItem().getUnlocalizedName()));
                player.addChatMessage(new ChatComponentText("§5item damage is: " + player.getHeldItem().getItem().getDamage(player.getHeldItem())));
                if(player.getHeldItem().getTagCompound() != null)
                player.addChatMessage(new ChatComponentText("§citem nbt list is: " + player.getHeldItem().getTagCompound()));
            }
        }
        else if(args.length > 0 && args[0].equalsIgnoreCase("getnbt")){
            if (sender instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) sender;
                MovingObjectPosition rayTrace = rayTrace(player, 5.0); // Получаем точку, на которую игрок смотрит

                if (rayTrace != null) {
                    if (rayTrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                        int blockX = rayTrace.blockX;
                        int blockY = rayTrace.blockY;
                        int blockZ = rayTrace.blockZ;

                        TileEntity tileEntity = player.worldObj.getTileEntity(blockX, blockY, blockZ);
                        if (tileEntity != null) {
                            NBTTagCompound tileEntityNBT = new NBTTagCompound();
                            tileEntity.writeToNBT(tileEntityNBT);

                            String nbtString = tileEntityNBT.toString();
                            player.addChatComponentMessage(new ChatComponentText("NBT Data (Block): §5" + tileEntity.getClass().getName() + " §9" + nbtString));
                        }
                        else player.addChatComponentMessage(new ChatComponentText("this block is not tile"));
                    }
                } else player.addChatMessage(new ChatComponentText("no tile blocks found"));
                //getEntities(player);
            }
        }
        else {
            sender.addChatMessage(new ChatComponentText("§4Invalid command usage. Use:"));
            sender.addChatMessage(new ChatComponentText("§4/deathlyhallows damagelog"));
            sender.addChatMessage(new ChatComponentText("§4/deathlyhallows getid"));
            sender.addChatMessage(new ChatComponentText("§4/deathlyhallows getnbt"));
        }
    }
    public void getEntities(EntityPlayer player){
        List entities = player.worldObj.getEntitiesWithinAABB(Entity.class, player.getBoundingBox().expand(5,5,5));
        if(entities != null) {
            for (Object o : entities) {
                Entity entity = (Entity) o;
                String entityInfo = "Entity Info: " + entity.getCommandSenderName() + " " + entity.getEntityData();
                player.addChatComponentMessage(new ChatComponentText(entityInfo));
            }
        } else player.addChatMessage(new ChatComponentText("no entities found"));
    }
    private MovingObjectPosition rayTrace(EntityPlayer player, double distance) {
        Vec3 startVec = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 lookVec = player.getLook(1.0F);
        Vec3 endVec = startVec.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);
        return player.worldObj.rayTraceBlocks(startVec, endVec);
    }
}
