package com.pyding.deathlyhallows;

import com.emoniph.witchery.entity.EntityBanshee;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pyding.deathlyhallows.entity.EntityEmpoweredArrow;
import com.pyding.deathlyhallows.network.NBTSync;
import com.pyding.deathlyhallows.network.NetworkHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import scala.util.parsing.json.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DHUtil {
	public static List<EntityLivingBase> getEntitiesAround(Entity entity, float radius, boolean self){
		List<EntityLivingBase> list = new ArrayList<>();
		for(Object o : entity.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(entity.posX - radius, entity.posY - radius, entity.posZ - radius, entity.posX + radius, entity.posY + radius, entity.posZ + radius))){
			if(o instanceof EntityLivingBase){
				if(o.equals(entity) && !self)
					continue;
				list.add((EntityLivingBase)o);
			}
		}
		return list;
	}

	public static Vec3 rayCords(EntityLivingBase entity,double distance, boolean stopOnBlocks) {
		Vec3 startVec = Vec3.createVectorHelper(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
		Vec3 lookVec = entity.getLookVec();
		Vec3 endVec = startVec.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);
		MovingObjectPosition hitResult;
		if (stopOnBlocks) {
			hitResult = entity.worldObj.rayTraceBlocks(startVec, endVec, true);
		} else {
			hitResult = entity.worldObj.func_147447_a(startVec, endVec, false, false, false);
		}
		if (hitResult != null) {
			return hitResult.hitVec;
		} else {
			return endVec;
		}
	}

	public static String resource(String id) {
		String s = StatCollector.translateToLocal(id);
		return s.replace("|", "\n").replace("{", "§");
	}
	
	public static void sync(Entity entity){
		if(entity.getEntityData() == null || entity.worldObj.isRemote)
			return;
		if(entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)entity;
			NetworkHandler.sendToPlayer(new NBTSync(player.getEntityData(),player.getEntityId()),player);
		} else {
			NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 64);
			NetworkHandler.sendToAllAround(new NBTSync(entity.getEntityData(),entity.getEntityId()),targetPoint);
		}
	}
	
	public static void spawnArrow(EntityPlayer player){
		EntityEmpoweredArrow arrow = new EntityEmpoweredArrow(player.getEntityWorld(), player, (float)(player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue()*20), 4, DamageSource.causePlayerDamage(player).setMagicDamage().setProjectile());
		arrow.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
		DHUtil.sync(arrow);
		player.worldObj.spawnEntityInWorld(arrow);
		player.worldObj.playSoundAtEntity(player, "dh:spell.arrow", 1F, 1F);
	}


	public static void getFigure(World world, int xBeg, int yBeg, int zBeg, int xSize, int ySize, int zSize) {
		ArrayList<String> struct = new ArrayList<>();
		for (int y = yBeg; y < yBeg + ySize; y++) {
			ArrayList<String> layer = new ArrayList<>();
			for (int x = xBeg; x < xBeg + xSize; x++) {
				ArrayList<String> row = new ArrayList<>();
				for (int z = zBeg; z < zBeg + zSize; z++) {
					Block block = world.getBlock(x, y, z);
					int meta = world.getBlockMetadata(x, y, z);
					if (block == Blocks.air || block == null || block == Blocks.command_block) {
						row.add(null);
					} else {
						String blockName = Block.blockRegistry.getNameForObject(block).toString();
						if (blockName.contains("witchery:circleglyph")) {
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
		for (int i = 0; i < struct.size(); i++) {
			String formattedString = struct.get(i)
										   .replace("[", "{")
										   .replace("]", "}")
										   .replace("null", "null"); 

			StringBuilder formattedWithQuotes = new StringBuilder();
			boolean insideWord = false;
			boolean insideQuotes = false;
			for (int j = 0; j < formattedString.length(); j++) {
				char ch = formattedString.charAt(j);
				if (ch == '{' || ch == '}' || ch == ',') {
					if (insideQuotes) {
						formattedWithQuotes.append("\"");
						insideQuotes = false;
					}
					formattedWithQuotes.append(ch);
					if (ch == ',') {
						formattedWithQuotes.append(' ');
					}
				} else if (ch == ' ' && insideWord) {
					formattedWithQuotes.append(ch);
					insideWord = false;
				} else {
					if (!insideQuotes) {
						formattedWithQuotes.append("\"");
						insideQuotes = true;
					}
					formattedWithQuotes.append(ch);
					insideWord = true;
				}
			}
			if (insideQuotes) {
				formattedWithQuotes.append("\"");
			}

			sb.append(formattedWithQuotes.toString());
			if (i < struct.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append("}");
		return sb;
	}

	public static float absorbExponentially(float damage, int warp) {
		float baseReductionFactor = 0.99f;
		float warpFactor = 1 + (warp*10 / 1000.0f);
		float reducedDamage = (float) (baseReductionFactor * damage / (Math.log10(damage + 1) * warpFactor));
		if (damage > 10000)
			reducedDamage = (damage+reducedDamage*1000)/damage;
		return reducedDamage;
	}
	
	public static List<Item> itemList = new ArrayList<>();
	
	public static List<Item> getItems(){
		if(itemList.isEmpty()){
			for(Object o: GameData.getItemRegistry()){
				if(o instanceof Item){
					Item item = (Item)o;
					itemList.add(item);
				}
			}
		}
		return itemList;
	}
	
	public static void spawnEntity(World world,int x, int y, int z,Class entityClass){
		final EntityCreature creature = Infusion.spawnCreature(world, entityClass, x, y, z, null, 1, 2, ParticleEffect.INSTANT_SPELL, SoundEffect.NOTE_HARP);
		CreatureUtil.spawnWithEgg((EntityLiving)creature, true);
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
	
	public static boolean contains(String list, String element){
		boolean contains = false;
		for(String name: list.split(",")){
			if(name.equals(element) || name.contains(element)) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	public static void deadInside(EntityLivingBase victim,EntityPlayer player){
		if(victim instanceof EntityPlayer) {
			EntityPlayer playerTarget = (EntityPlayer)victim;
			if(playerTarget.capabilities.isCreativeMode) {
				return;
			}
		}
		EntityUtil.instantDeath(victim, player);
	}
	
	
	/*public static void getFigure(World world, int xBeg, int yBeg, int zBeg, int xSize, int ySize, int zSize) {
		String[] struct = new String[(yBeg + ySize)];
		for (int y = yBeg; y < yBeg + ySize; y++) {
			String[] layer = new String[(yBeg + ySize)*(xBeg + xSize)];
			for (int x = xBeg; x < xBeg + xSize; x++) {
				String[] row = new String[(xBeg + xSize)*(zBeg + zSize)];
				for (int z = zBeg; z < zBeg + zSize; z++) {
					Block block = world.getBlock(x, y, z);
					int meta = world.getBlockMetadata(x, y, z);
					if (block == Blocks.air || block == null) {
						row[z] = null;
					} else {
						String blockName = Block.blockRegistry.getNameForObject(block).toString();
						if (blockName.contains("witchery:circleglyph")) {
							meta = -1;
						}
						row[z] = (blockName + " " + meta);
					}
				}
				layer[x] = row;
			}
			struct[y] = layer;
		}
		System.out.println(Arrays.toString(struct));
	}*/
}
