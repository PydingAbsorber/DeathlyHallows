package com.pyding.deathlyhallows.utils;

import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.deathlyhallows.entities.EntityEmpoweredArrow;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketParticle;
import com.pyding.deathlyhallows.particles.ParticleBlueMagic;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DHUtils {
	public static List<EntityLivingBase> getEntitiesAround(Entity entity, float radius, boolean self) {
		List<EntityLivingBase> list = new ArrayList<>();
		for(Object o: entity.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(entity.posX - radius, entity.posY - radius, entity.posZ - radius, entity.posX + radius, entity.posY + radius, entity.posZ + radius))) {
			if(o instanceof EntityLivingBase) {
				if(o.equals(entity) && !self) {
					continue;
				}
				list.add((EntityLivingBase)o);
			}
		}
		return list;
	}

	public static Vec3 rayCords(EntityLivingBase entity, double distance, boolean stopOnBlocks) {
		Vec3 startVec = Vec3.createVectorHelper(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
		Vec3 lookVec = entity.getLookVec();
		Vec3 endVec = startVec.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);
		MovingObjectPosition hitResult;
		if(stopOnBlocks) {
			hitResult = entity.worldObj.rayTraceBlocks(startVec, endVec, true);
		}
		else {
			hitResult = entity.worldObj.func_147447_a(startVec, endVec, false, false, false);
		}
		if(hitResult != null) {
			return hitResult.hitVec;
		}
		else {
			return endVec;
		}
	}

	public static String resource(String id) {
		String s = StatCollector.translateToLocal(id);
		return s.replace("|", "\n").replace("{", "§");
	}

	public static void sync(Entity entity) {
		if(entity.getEntityData() == null || entity.worldObj.isRemote) {
			return;
		}
		if(entity instanceof EntityPlayer) {
			//EntityPlayer player = (EntityPlayer)entity;
			//NetworkHandler.sendToPlayer(new PlayerNBTSync(player.getEntityData()),player);
		}
		else {
			//NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 64);
			//NetworkHandler.sendToAllAround(new NBTSync(entity.getEntityData(),entity.getEntityId()),targetPoint);
		}
	}

	public static void spawnArrow(EntityPlayer player, int type) {
		float damage = (float)(player.getEntityAttribute(SharedMonsterAttributes.attackDamage)
									 .getAttributeValue() * 20
		);
		float radius = 4;
		DamageSource source = DamageSource.causePlayerDamage(player).setMagicDamage().setProjectile();
		if(type == 2) {
			damage = damage * 20 + 1000;
			radius *= 2;
			source = DamageSource.causePlayerDamage(player).setMagicDamage().setDamageIsAbsolute();
		}
		else if(type == 3) {
			damage = damage * 20 + 5000;
			radius *= 3;
			source = DamageSource.causePlayerDamage(player)
								 .setMagicDamage()
								 .setDamageBypassesArmor()
								 .setDamageIsAbsolute();
		}
		EntityEmpoweredArrow arrow = new EntityEmpoweredArrow(player.getEntityWorld(), player, damage, radius, source, type);
		arrow.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
		DHUtils.sync(arrow);
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
			String formattedString = struct.get(i)
										   .replace("[", "{")
										   .replace("]", "}")
										   .replace("null", "null");

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

	public static void spawnEntity(World world, int x, int y, int z, Class entityClass) {
		final EntityCreature creature = Infusion.spawnCreature(world, entityClass, x, y, z, null, 1, 2, ParticleEffect.INSTANT_SPELL, SoundEffect.NOTE_HARP);
		CreatureUtil.spawnWithEgg(creature, true);
	}

	public static List<String> entities = new ArrayList<>();

	public static List<String> getEntitiesNames() {
		if(entities.isEmpty()) {
			for(Object obj: EntityList.stringToClassMapping.entrySet()) {
				java.util.Map.Entry<String, Class<?>> entry = (java.util.Map.Entry<String, Class<?>>)obj;
				Class<?> entityClass = entry.getValue();

				if(EntityLiving.class.isAssignableFrom(entityClass)) {
					entities.add(entry.getKey());
				}
			}
		}
		return entities;
	}

	public static boolean contains(String list, String element) {
		boolean contains = false;
		for(String name: list.split(",")) {
			if(name.equals(element) || name.contains(element)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	public static void deadInside(EntityLivingBase victim, EntityPlayer player) {
		if(victim instanceof EntityPlayer) {
			EntityPlayer playerTarget = (EntityPlayer)victim;
			if(playerTarget.capabilities.isCreativeMode) {
				return;
			}
		}
		EntityUtil.instantDeath(victim, player);
	}


	public static void modifyAttribute(EntityLivingBase entity, IAttribute attribute, String name, float amount, int method, boolean add) {
		Multimap<String, AttributeModifier> attributes = HashMultimap.create();
		attributes.put(attribute.getAttributeUnlocalizedName(), new AttributeModifier(name, amount, method));
		if(add) {
			entity.getAttributeMap().applyAttributeModifiers(attributes);
		}
		else {
			entity.getAttributeMap().removeAttributeModifiers(attributes);
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

	public static void spawnParticle(Entity entity, double x, double y, double z, Color color, float resizeSpeed, float scale, int age, int type, float motionX, float motionY, float motionZ) {
		NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(entity.dimension, x, y, z, 64);
		DHPacketProcessor.sendToAllAround(new PacketParticle(x, y, z, color, resizeSpeed, scale, age, type, motionX, motionY, motionZ), targetPoint);
	}

	@SideOnly(Side.CLIENT)
	public static void spawnParticleClient(Entity entity, double x, double y, double z, Color color, float resizeSpeed, float scale, int age, int type, float motionX, float motionY, float motionZ) {
		if(type == 1) {
			ParticleBlueMagic particle = new ParticleBlueMagic(entity.worldObj, x, y, z, color, resizeSpeed, scale, age);
			particle.motionX = motionX;
			particle.motionY = motionY;
			particle.motionZ = motionZ;
			particle.noClip = true;
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		}
	}
}
