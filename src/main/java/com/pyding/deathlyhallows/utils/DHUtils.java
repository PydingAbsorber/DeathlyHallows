package com.pyding.deathlyhallows.utils;

import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import am2.api.ArsMagicaApi;
import am2.api.IExtendedProperties;
import baubles.api.BaublesApi;
import baubles.common.container.InventoryBaubles;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.google.common.base.Charsets;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.entities.EntityEmpoweredArrow;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketParticle;
import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.ItemWandCasting;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.common.item.ItemBaubleBox;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class DHUtils {
	public static NBTTagCompound readOfflinePlayer(String commandSenderName) {
		String uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + commandSenderName).getBytes(Charsets.UTF_8)).toString();
		try {
			File dir = new File(MinecraftServer.getServer().worldServers[0].getSaveHandler().getWorldDirectory(), "playerdata");
			File playerDir = new File(dir, uuid + ".dat");
			if(!playerDir.exists() || !playerDir.isFile()) {
				return null;
			}
			try(InputStream stream = Files.newInputStream(playerDir.toPath())) {
				return CompressedStreamTools.readCompressed(stream);
			}
		}
		catch(Exception e) {
			DeathlyHallows.LOG.warn("Failed to load player data for " + commandSenderName);
		}
		return null;
	}

	public static IInventory readOfflineBaubles(String commandSenderName) {
		InventoryBaubles baubles = new InventoryBaubles(null);
		NBTTagCompound tag = null;
		try {
			File dir = new File(MinecraftServer.getServer().worldServers[0].getSaveHandler().getWorldDirectory(), "playerdata");
			File baublesDir = new File(dir, commandSenderName + ".baub");
			if(!baublesDir.exists() || !baublesDir.isFile()) {
				return null;
			}
			try(InputStream stream = Files.newInputStream(baublesDir.toPath())) {
				tag = CompressedStreamTools.readCompressed(stream);
			}

		}
		catch(Exception e) {
			DeathlyHallows.LOG.warn("Failed to load baubles data for " + commandSenderName);
		}
		if(tag == null) {
			try {
				File dir = new File(MinecraftServer.getServer().worldServers[0].getSaveHandler().getWorldDirectory(), "playerdata");
				File baublesDir = new File(dir, commandSenderName + ".baubback");
				if(!baublesDir.exists() || !baublesDir.isFile()) {
					return null;
				}
				try(InputStream stream = Files.newInputStream(baublesDir.toPath())) {
					tag = CompressedStreamTools.readCompressed(stream);
				}

			}
			catch(Exception e) {
				DeathlyHallows.LOG.warn("Failed to load backup baubles data for " + commandSenderName);
			}
		}
		if(tag == null) {
			return null;
		}
		baubles.readNBT(tag);
		return baubles;
	}

	public static boolean hasMantle(EntityPlayer p) {
		return hasMantle(BaublesApi.getBaubles(p));
	}

	public static boolean hasMantle(IInventory baubles) {
		for(int i = 0; i < baubles.getSizeInventory(); ++i) {
			ItemStack mantle = baubles.getStackInSlot(i);
			if(mantle != null && mantle.getItem() == DHItems.invisibilityMantle) {
				return true;
			}
		}
		return false;
	}

	public static IPowerSource findClosestPowerSource(TileEntity e, int radius) {
		return findClosestPowerSource(e.getWorldObj(), new Coord(e.xCoord, e.yCoord, e.zCoord), radius);
	}

	public static IPowerSource findClosestPowerSource(Entity e, int radius) {
		return findClosestPowerSource(e.worldObj, new Coord((int)e.posX, (int)e.posY, (int)e.posZ), radius);
	}

	public static IPowerSource findClosestPowerSource(World world, Coord location, int radius) {
		List<IPowerSource> sources = findPowerSourcesInRange(world, location, radius);
		if(sources.size() == 0) {
			return null;
		}
		sources.sort(Comparator.comparingDouble(a -> location.distanceSqTo(a.getLocation())));
		return sources.get(0);
	}

	public static List<IPowerSource> findPowerSourcesInRange(World world, Coord location, int radius) {
		if(PowerSources.instance() == null) {
			return Collections.emptyList();
		}
		ArrayList<IPowerSource> nearbyPowerSources = new ArrayList<>();
		double radiusSq = radius * radius;
		for(IPowerSource source: PowerSources.instance().powerSources) {
			if(source.getWorld() == world && location.distanceSqTo(source.getLocation()) < radiusSq) {
				nearbyPowerSources.add(source);
			}
		}
		return nearbyPowerSources;
	}

	public static void drawTexturedRect(int x, int y, int zLevel, int w, int h, int minU, int minV, int stepU, int stepV, int textureX, int textureY) {
		float uStep = 1F / textureX;
		float vStep = 1F / textureY;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x, y + h, zLevel, minU * uStep, (minV + stepV) * vStep);
		tessellator.addVertexWithUV(x + w, y + h, zLevel, (minU + stepU) * uStep, (minV + stepV) * vStep);
		tessellator.addVertexWithUV(x + w, y, zLevel, (minU + stepU) * uStep, minV * vStep);
		tessellator.addVertexWithUV(x, y, zLevel, minU * uStep, minV * vStep);
		tessellator.draw();
	}

	private static class UtilsRandom {
		private static final Random random = new Random();
	}

	/**
	 Gets random int from min inclusive to max inclusive

	 @param min inclusive lower bound
	 @param max inclusive upper bound
	 @return random integer in range [min;max]
	 */
	public static int getRandomInt(int min, int max) {
		return min + getRandomInt(max - min + 1);
	}

	/**
	 Gets random int from 0 inclusive to argument exclusive

	 @param max exclusive upper bound
	 @return random integer in range [0;max)
	 */
	public static int getRandomInt(int max) {
		return UtilsRandom.random.nextInt(max);
	}
	
	public static <T extends Entity> List<T> getEntitiesAround(Class<T> clazz, Entity entity, float radius) {
		return getEntitiesIn(clazz, entity.worldObj, AxisAlignedBB.getBoundingBox(entity.posX - radius, entity.posY - radius, entity.posZ - radius, entity.posX + radius, entity.posY + radius, entity.posZ + radius));
	}
	
	public static <T extends Entity> List<T> getEntitiesAt(Class<T> clazz, World world, double x, double y, double z, float radius) {
		return getEntitiesIn(clazz, world, AxisAlignedBB.getBoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));
	}

	@SuppressWarnings("unchecked")
	public static <T extends Entity> List<T> getEntitiesIn(Class<T> clazz, World world, AxisAlignedBB boundingBox) {
		return world.getEntitiesWithinAABB(clazz, boundingBox);
	}
	
	public static byte[] getBytesFromTagList(NBTTagList list, int index) {
		return ((NBTTagByteArray)((NBTTagList)list.copy()).removeTag(index)).func_150292_c();
	}

	public static boolean contains(String list, String element) {
		for(String name: list.split(",")) {
			if(element.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static void spawnArrow(EntityPlayer player, int type) {
		if(player.worldObj.isRemote) {
			return;
		}

		EntityEmpoweredArrow arrow = new EntityEmpoweredArrow(player.getEntityWorld(), player, type);
		player.worldObj.spawnEntityInWorld(arrow);
		if(type == 1) {
			player.worldObj.playSoundAtEntity(player, "dh:spell.arrow", 1F, 1F);
		}
		if(type == 2) {
			player.worldObj.playSoundAtEntity(player, "dh:arrow.magic_arrow_1", 1F, 1F);
		}
		if(type == 3) {
			player.worldObj.playSoundAtEntity(player, "dh:arrow.magic_arrow_2", 1F, 1F);
		}
	}


	public static void getFigure(World world, int xBeg, int yBeg, int zBeg, int xSize, int ySize, int zSize) {
		ArrayList<String> struct = new ArrayList<>();
		for(int y = yBeg; y < yBeg + ySize; y++) {
			ArrayList<String> layer = new ArrayList<>();
			for(int x = xBeg; x < xBeg + xSize; x++) {
				ArrayList<String> row = new ArrayList<>();
				for(int z = zBeg; z < zBeg + zSize; z++) {
					Block block = world.getBlock(x, y, z);
					int meta = world.getBlockMetadata(x, y, z);
					if(block == Blocks.air || block == null || block == Blocks.command_block) {
						row.add(null);
					}
					else {
						String blockName = Block.blockRegistry.getNameForObject(block);
						if(blockName.contains("witchery:circleglyph")) {
							meta = -1;
						}
						row.add(blockName + " " + meta);
					}
				}
				layer.add(row.toString());
			}
			struct.add(layer.toString());
		}

		StringBuilder sb = getStringBuilder(struct);
		System.out.println(sb);
	}

	private static StringBuilder getStringBuilder(ArrayList<String> struct) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for(int i = 0; i < struct.size(); i++) {
			String formattedString = struct.get(i).replace("[", "{").replace("]", "}");
			StringBuilder formattedWithQuotes = new StringBuilder();
			boolean insideWord = false;
			boolean insideQuotes = false;
			for(int j = 0; j < formattedString.length(); j++) {
				char ch = formattedString.charAt(j);
				if(ch == '{' || ch == '}' || ch == ',') {
					if(insideQuotes) {
						formattedWithQuotes.append("\"");
						insideQuotes = false;
					}
					formattedWithQuotes.append(ch);
					if(ch == ',') {
						formattedWithQuotes.append(' ');
					}
				}
				else if(ch == ' ' && insideWord) {
					formattedWithQuotes.append(ch);
					insideWord = false;
				}
				else {
					if(!insideQuotes) {
						formattedWithQuotes.append("\"");
						insideQuotes = true;
					}
					formattedWithQuotes.append(ch);
					insideWord = true;
				}
			}
			if(insideQuotes) {
				formattedWithQuotes.append("\"");
			}

			sb.append(formattedWithQuotes);
			if(i < struct.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append("}");
		return sb;
	}

	public static float absorbExponentially(float damage, int warp) {
		float baseReductionFactor = 0.99f;
		float warpFactor = 1 + (warp * 10 / 1000.0f);
		float reducedDamage = (float)(baseReductionFactor * damage / (Math.log10(damage + 1) * warpFactor));
		if(damage > 10000) {
			reducedDamage = (damage + reducedDamage * 1000) / damage;
		}
		return reducedDamage;
	}

	public static List<Item> itemList = new ArrayList<>();

	public static List<Item> getItems() {
		if(itemList.isEmpty()) {
			for(Object o: GameData.getItemRegistry()) {
				if(o instanceof Item) {
					Item item = (Item)o;
					itemList.add(item);
				}
			}
		}
		return itemList;
	}

	public static void spawnEntity(World world, int x, int y, int z, Class<? extends EntityCreature> entityClass) {
		final EntityCreature creature = Infusion.spawnCreature(world, entityClass, x, y, z, null, 1, 2, ParticleEffect.INSTANT_SPELL, SoundEffect.NOTE_HARP);
		CreatureUtil.spawnWithEgg(creature, true);
	}

	public static List<String> entities = new ArrayList<>();

	public static List<String> getEntitiesNames() {
		if(entities.isEmpty()) {
			@SuppressWarnings("unchecked")
			Set<Map.Entry<String, Class<?>>> mapping = (Set<Map.Entry<String, Class<?>>>)EntityList.stringToClassMapping.entrySet();
			for(Map.Entry<String, Class<?>> entry: mapping) {
				if(EntityLivingBase.class.isAssignableFrom(entry.getValue())) {
					entities.add(entry.getKey());
				}
			}
		}
		return entities;
	}

	public static void deadInside(EntityLivingBase victim, EntityPlayer player) { // FOX! DIE!
		if(victim == null
				|| victim.worldObj == null
				|| victim.worldObj.isRemote
		) {
			return;
		}
		if(player != null) {
			DeathlyProperties props = DeathlyProperties.get(player);
			EntityLivingBase bound;
			if(props != null) {
				bound = props.getSource();
				deadInside(victim, bound != null ? bound : player);
				return;
			}
		}
		deadInside(victim, player);
	}

	public static void deadInside(EntityLivingBase entity, EntityLivingBase attacker) { // FOX! DIE!
		if(entity == null || entity.worldObj == null || entity.worldObj.isRemote) {
			return;
		}
		entity.setHealth(0F);
		if(entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)entity;
			if(player.isPlayerSleeping()) {
				player.wakeUpPlayer(true, true, false);
			}
			if(ExtendedPlayer.get(player).isVampire()) {
				entity.onDeath(attacker == null ? EntityUtil.DamageSourceSunlight.SUN : new EntityUtil.DamageSourceSunlight(attacker));
				return;
			}
		}
		entity.onDeath(attacker == null ? DamageSource.magic : new EntityDamageSource(DamageSource.magic.getDamageType(), attacker));
		if(entity instanceof EntityLiving) {
			entity.setDead();
		}
	}

	public static void spawnSphere(Entity entity, Vec3 pos, int count, float radius, Color color, float resizeSpeed, float scale, int age, int type) {
		Random random = new Random();
		for(int i = 0; i < count; i++) {
			double theta = 2 * Math.PI * random.nextDouble();
			double phi = Math.acos(2 * random.nextDouble() - 1);
			double xOffset = radius * Math.sin(phi) * Math.cos(theta);
			double yOffset = radius * Math.sin(phi) * Math.sin(theta);
			double zOffset = radius * Math.cos(phi);

			float speed = 1;
			float motionX = (float)(xOffset * speed);
			float motionY = (float)(yOffset * speed);
			float motionZ = (float)(zOffset * speed);

			spawnParticle(entity, pos.xCoord + xOffset, pos.yCoord + yOffset, pos.zCoord + zOffset, color, resizeSpeed, scale, age, type, motionX, motionY, motionZ);
		}
	}

	public static Vec3 getPosition(Entity e) {
		if(e instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer)e;
			return Vec3.createVectorHelper(p.posX, p.posY + p.getEyeHeight() - p.getDefaultEyeHeight(), p.posZ);
		}
		return Vec3.createVectorHelper(e.posX, e.posY, e.posZ);
	}

	public static void spawnParticle(Entity entity, double x, double y, double z, Color color, float resizeSpeed, float scale, int age, int type, float motionX, float motionY, float motionZ) {
		NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(entity.dimension, x, y, z, 64);
		DHPacketProcessor.sendToAllAround(new PacketParticle(x, y, z, color, resizeSpeed, scale, age, type, motionX, motionY, motionZ), targetPoint);
	}

	public static boolean isHallow(ItemStack stack) {
		return stack.getItem() == DHItems.resurrectionStone || stack.getItem() == DHItems.elderWand || stack.getItem() == DHItems.invisibilityMantle;
	}

	public static void removeDuplicatesFromInventory(EntityPlayer p) {
		int count = 0;
		for(int i = 0; i < p.inventory.getSizeInventory(); i++) {
			ItemStack stack = p.inventory.getStackInSlot(i);
			if(stack == null || !isHallow(stack)) {
				continue;
			}
			if(!stack.hasTagCompound()) {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("dhowner", p.getDisplayName());
				stack.setTagCompound(nbt);
			}
			else if(!stack.getTagCompound().hasKey("dhowner")) {
				NBTTagCompound nbt = stack.getTagCompound();
				nbt.setString("dhowner", p.getDisplayName());
				stack.setTagCompound(nbt);
			}
			else if(stack.hasTagCompound() && stack.getTagCompound().hasKey("dhowner")) {
				String dhowner = stack.getTagCompound().getString("dhowner");
				count++;
				if(p.getDisplayName().equals(dhowner)) {
					if(count > 1) {
						p.inventory.setInventorySlotContents(i, null);
						p.inventoryContainer.detectAndSendChanges();
						ChatUtil.sendTranslated(EnumChatFormatting.RED, p, "dh.chat.dupe");
					}
				}
				else {
					p.inventory.setInventorySlotContents(i, null);
					p.inventoryContainer.detectAndSendChanges();
				}
			}
		}
		IInventory baubles = BaublesApi.getBaubles(p);
		for(int i = 1; i < baubles.getSizeInventory(); i++) {
			ItemStack stack = baubles.getStackInSlot(i);
			if(stack == null
					|| !isHallow(stack)
					|| !stack.hasTagCompound()
					|| !stack.getTagCompound().hasKey("dhowner")
			) {
				continue;
			}
			if(!stack.getTagCompound().getString("dhowner").equals(p.getDisplayName())) {
				baubles.setInventorySlotContents(i, null);
				p.inventoryContainer.detectAndSendChanges();
			}
			if(++count > 1) {
				baubles.setInventorySlotContents(i, null);
				p.inventoryContainer.detectAndSendChanges();
				ChatUtil.sendTranslated(EnumChatFormatting.RED, p, "dh.chat.dupe");
			}
		}
	}

	public static boolean hasDeathlyHallow(EntityPlayer p) {
		int count = 0;
		for(ItemStack stack: getAllItems(p)) {
			if(!isHallow(stack)) {
				continue;
			}
			if(!stack.hasTagCompound()) {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("dhowner", p.getDisplayName());
				stack.setTagCompound(nbt);
			}
			else if(!stack.getTagCompound().hasKey("dhowner")) {
				NBTTagCompound nbt = stack.getTagCompound();
				nbt.setString("dhowner", p.getDisplayName());
				stack.setTagCompound(nbt);
			}
			else if(stack.hasTagCompound() && stack.getTagCompound().hasKey("dhowner")) {
				count++;
			}
		}
		return count > 0;
	}

	// Stolen code from Minecraft; Was @SideOnly(Side.CLIENT)
	public static MovingObjectPosition rayTrace(EntityPlayer p) {
		return rayTrace(p, p.capabilities.isCreativeMode ? 5.0F : 4.5F);
	}

	public static MovingObjectPosition rayTrace(EntityPlayer p, double distance) {
		return p.worldObj.rayTraceBlocks(Vec3.createVectorHelper(p.posX, p.posY + p.getEyeHeight(), p.posZ), playerLook(p, distance));
	}

	private static Vec3 playerLook(EntityPlayer p, double distance) {
		Vec3 startVec = Vec3.createVectorHelper(p.posX, p.posY + p.getEyeHeight(), p.posZ);
		Vec3 lookVec = p.getLookVec();
		return startVec.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);
	}

	public static Vec3 getLook(EntityPlayer p, double range) {
		MovingObjectPosition mop = rayTrace(p, range);
		if(mop == null) {
			return playerLook(p, range);
		}
		return mop.hitVec;
	}

	public static List<ItemStack> getAllItems(EntityPlayer player) {
		List<ItemStack> list = new ArrayList<>();
		for(int i = 0; i < player.inventory.getSizeInventory(); ++i) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if(stack != null) {
				list.add(stack);
			}
		}
		IInventory baubles = BaublesApi.getBaubles(player);
		for(int i = 0; i < baubles.getSizeInventory(); ++i) {
			ItemStack stack = baubles.getStackInSlot(i);
			if(stack != null) {
				list.add(stack);
			}
		}
		return list;
	}

	public static float fuckMagic(EntityPlayer player, float percent) {
		float magic = 0;
		List<ItemStack> list = getAllItems(player);
		magic += Infusion.getCurrentEnergy(player) * percent;
		Infusion.setCurrentEnergy(player, (int)(Infusion.getCurrentEnergy(player) - Infusion.getCurrentEnergy(player) * percent));
		if(DHIntegration.thaumcraft) {
			for(ItemStack stack: list) {
				if(stack.getItem() instanceof ItemWandCasting) {
					ItemWandCasting wand = (ItemWandCasting)stack.getItem();
					AspectList aspectList = new AspectList();
					for(Aspect aspect: Aspect.getPrimalAspects()) {
						aspectList.add(aspect, (int)(wand.getMaxVis(stack) * percent));
						magic += wand.getMaxVis(stack) * percent;
					}
					wand.consumeAllVis(stack, player, aspectList, true, false);
				}
			}
		}
		if(DHIntegration.bloodMagic) {
			magic += SoulNetworkHandler.getCurrentEssence(player.getCommandSenderName()) * percent;
			SoulNetworkHandler.setCurrentEssence(player.getCommandSenderName(),
					(int)Math.min(0, SoulNetworkHandler.getCurrentEssence(player.getCommandSenderName()) - SoulNetworkHandler.getCurrentEssence(player.getCommandSenderName()) * percent));
		}
		if(DHIntegration.botania) {
			for(ItemStack stack: list) {
				if(stack.getItem() instanceof IManaItem) {
					IManaItem item = (IManaItem)stack.getItem();
					magic += item.getMaxMana(stack) * percent;
					item.addMana(stack, (int)(-item.getMaxMana(stack) * percent));
				}
				if(stack.getItem() instanceof ItemBaubleBox) {
					ItemStack[] stacks = ItemBaubleBox.loadStacks(stack);
					for(ItemStack slotStack: stacks) {
						if(slotStack.getItem() instanceof IManaItem) {
							IManaItem item = (IManaItem)slotStack.getItem();
							magic += item.getMaxMana(stack) * percent;
							item.addMana(slotStack, (int)(-item.getMaxMana(slotStack) * percent));
						}
					}
				}
			}
		}
		if(DHIntegration.arsMagica) {
			IExtendedProperties props = ArsMagicaApi.instance.getExtendedProperties(player);
			magic += props.getMaxMana() * percent;
			props.setCurrentMana(Math.min(0, props.getCurrentMana() - props.getMaxMana() * percent));
		}
		return magic;
	}
}
