package com.pyding.deathlyhallows.commands;

import com.pyding.deathlyhallows.DHUtil;
import com.pyding.deathlyhallows.common.handler.ConfigHandler;
import com.pyding.deathlyhallows.common.handler.EventHandler;
import com.pyding.deathlyhallows.common.properties.ExtendedPlayer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
		if(args.length > 0 && args[0].equalsIgnoreCase("damagelog")) {
			if(sender instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)sender;
				ExtendedPlayer props = ExtendedPlayer.get(player);
				if(props.getDamageLog()) {
					props.setDamageLog(false);
					player.addChatMessage(new ChatComponentText("Damage Log disabled for " + player.getDisplayName()));
				}
				else {
					props.setDamageLog(true);
					player.addChatMessage(new ChatComponentText("Damage Log enabled for " + player.getDisplayName()));
				}
			}
		}
		else if(args.length > 0 && args[0].equalsIgnoreCase("getid")) {
			if(sender instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)sender;
				if(player.getHeldItem() != null) {
					Item item = player.getHeldItem().getItem();
					player.addChatMessage(new ChatComponentText("§bitem class name is: " + item.getClass().getName()));
					player.addChatMessage(new ChatComponentText("§9item unlock name is: " + item.getUnlocalizedName()));
					player.addChatMessage(new ChatComponentText("§5item damage is: " + item.getDamage(player.getHeldItem())));
					if(player.getHeldItem().getTagCompound() != null) {
						player.addChatMessage(new ChatComponentText("§citem nbt list is: " + player.getHeldItem()
																								   .getTagCompound()));
					}
				}
			}
		}
		else if(args.length > 0 && args[0].equalsIgnoreCase("getnbt")) {
			if(sender instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)sender;
				MovingObjectPosition rayTrace = rayTrace(player, 5.0); // Получаем точку, на которую игрок смотрит

				if(rayTrace != null) {
					if(rayTrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
						int blockX = rayTrace.blockX;
						int blockY = rayTrace.blockY;
						int blockZ = rayTrace.blockZ;

						TileEntity tileEntity = player.worldObj.getTileEntity(blockX, blockY, blockZ);
						if(tileEntity != null) {
							NBTTagCompound tileEntityNBT = new NBTTagCompound();
							tileEntity.writeToNBT(tileEntityNBT);

							String nbtString = tileEntityNBT.toString();
							player.addChatComponentMessage(new ChatComponentText("NBT Data (Block): §5" + tileEntity.getClass()
																													.getName() + " §9" + nbtString));
						}
						else {
							player.addChatComponentMessage(new ChatComponentText("this block is not tile"));
						}
					}
				}
				else {
					player.addChatMessage(new ChatComponentText("no tile blocks found"));
				}
				//getEntities(player);
			}
		}
		else if(args.length > 0 && args[0].equalsIgnoreCase("elf")) {
			if(sender instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)sender;
				ExtendedPlayer props = ExtendedPlayer.get(player);
				int elf = props.getElfLvl();
				if(elf > 0) {
					String text = "";
					EventHandler handler = new EventHandler();
					switch(elf) {
						case 1: {
							text = "you have " + player.experienceLevel + " lvl out of " + ConfigHandler.getElfRequirements(elf + 1);
							break;
						}
						case 2: {
							text = "your height is " + player.posY + " out of " + ConfigHandler.getElfRequirements(elf + 1);
							break;
						}
						case 3: {
							text = "you have " + handler.totalLvl(player) + " enchantment lvls in total out of " + ConfigHandler.getElfRequirements(elf + 1);
							break;
						}
						case 4: {
							text = "you killed " + props.getMobsKilled() + " creatures by bow out of " + ConfigHandler.getElfRequirements(elf + 1);
							break;
						}
						case 5: {
							text = "you eaten " + props.getFoodEaten() + " golden apples out of " + ConfigHandler.getElfRequirements(elf + 1);
							break;
						}
						case 6: {
							text = "you lived " + EventHandler.timeSurvived / 20 + " seconds under 10 debuffs out of " + +ConfigHandler.getElfRequirements(elf + 1);
							break;
						}
						case 7: {
							text = "you lived " + EventHandler.timeSurvived / 20 + " seconds in astral form out of " + +ConfigHandler.getElfRequirements(elf + 1);
							break;
						}
						case 8: {
							text = "you consumed " + props.getFoodCollection()
														  .size() + " items out of " + ConfigHandler.getElfRequirements(elf + 1);
							break;
						}
						case 9: {
							text = "you used " + props.getSpellsUsed() + " violet spells out of " + +ConfigHandler.getElfRequirements(elf + 1);
							break;
						}
						case 10: {
							text = "you are at the max lvl!";
							break;
						}
						default:
							text = "WHAT?!?!??!?!?!?!";
					}
					sender.addChatMessage(new ChatComponentText("You are now at lvl§5 " + elf));
					sender.addChatMessage(new ChatComponentText(text));
				}
				else {
					sender.addChatMessage(new ChatComponentText("§4you are not elf lol. Wanna know a secret? Just recharge dude!"));
				}
			}
		}
		else if(args.length == 7 && args[0].equalsIgnoreCase("figure")) {
			if(sender instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)sender;

				try {
					int x = Integer.parseInt(args[1]);
					int y = Integer.parseInt(args[2]);
					int z = Integer.parseInt(args[3]);
					int xSize = Integer.parseInt(args[4]);
					int ySize = Integer.parseInt(args[5]);
					int zSize = Integer.parseInt(args[6]);

					player.addChatMessage(new ChatComponentText("Figure sent in console: " +
							"x=" + x + ", y=" + y + ", z=" + z + ", xSize=" + xSize + ", ySize=" + ySize + ", zSize=" + zSize));
					DHUtil.getFigure(player.worldObj,(int)player.posX+x,(int)player.posY+y,(int)player.posZ+z,xSize,ySize,zSize);

				} catch (NumberFormatException e) {
					player.addChatMessage(new ChatComponentText("It must be all numbers you retard."));
				}
			} else {
				sender.addChatMessage(new ChatComponentText("Wtf who is that."));
			}
		}
		else {
			sender.addChatMessage(new ChatComponentText("§4Invalid command usage. Use:"));
			sender.addChatMessage(new ChatComponentText("§4/deathlyhallows damagelog"));
			sender.addChatMessage(new ChatComponentText("§4/deathlyhallows getid"));
			sender.addChatMessage(new ChatComponentText("§4/deathlyhallows getnbt"));
			sender.addChatMessage(new ChatComponentText("§4/deathlyhallows elf"));
		}
	}

	public void getEntities(EntityPlayer player) {
		List entities = player.worldObj.getEntitiesWithinAABB(Entity.class, player.getBoundingBox().expand(5, 5, 5));
		if(entities != null) {
			for(Object o: entities) {
				Entity entity = (Entity)o;
				String entityInfo = "Entity Info: " + entity.getCommandSenderName() + " " + entity.getEntityData();
				player.addChatComponentMessage(new ChatComponentText(entityInfo));
			}
		}
		else {
			player.addChatMessage(new ChatComponentText("no entities found"));
		}
	}

	private MovingObjectPosition rayTrace(EntityPlayer player, double distance) {
		Vec3 startVec = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		Vec3 lookVec = player.getLook(1.0F);
		Vec3 endVec = startVec.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);
		return player.worldObj.rayTraceBlocks(startVec, endVec);
	}
}
