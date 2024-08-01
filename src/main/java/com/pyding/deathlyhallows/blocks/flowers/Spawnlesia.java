package com.pyding.deathlyhallows.blocks.flowers;

import com.pyding.deathlyhallows.entities.EntityAbsoluteDeath;
import com.pyding.deathlyhallows.utils.DHConfig;
import com.pyding.deathlyhallows.utils.DHUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.subtile.SubTileFunctional;

import java.util.List;

public class Spawnlesia extends SubTileFunctional {

	public static final String NAME = "spawnlesia";
	
	private final String blackList = DHConfig.spawnlesia;
	public long summonMaxCd = 10000;
	public long summonCd = 0;
	public static int cost = DHConfig.spawnlesiaMana;

	@Override
	public void onUpdate() {
		super.onUpdate();
		if(mana > cost) {
			if(System.currentTimeMillis() - summonCd > summonMaxCd) {
				addMana(-cost);
				summonCd = summonMaxCd + System.currentTimeMillis();
				if(!supertile.getWorldObj().isRemote) {
					spawnRandomEntity();
				}
			}
		}
	}

	@Override
	public boolean onWanded(EntityPlayer player, ItemStack wand) {
		return super.onWanded(player, wand);
	}

	@Override
	public int getMaxMana() {
		return 20 * cost;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, entity, stack);
	}


	public void spawnRandomEntity() {
		World world = this.supertile.getWorldObj();
		double x = supertile.xCoord;
		double y = supertile.yCoord;
		double z = supertile.zCoord;
		List<String> entities = DHUtils.getEntitiesNames();
		if(!entities.isEmpty()) {
			String randomEntityName = entities.get(world.rand.nextInt(entities.size() - 1));
			if(!isBlackListed(randomEntityName)) {
				Entity entity = EntityList.createEntityByName(randomEntityName, world);
				if(entity != null && entity instanceof EntityLiving && !(entity instanceof EntityAbsoluteDeath)) {
					entity.setPosition(x + Math.random() * 10, y + 1, z + Math.random() * 10);
					world.spawnEntityInWorld(entity);
				}
				else {
					spawnRandomEntity();
				}
			}
			else {
				spawnRandomEntity();
			}
		}
	}

	public boolean isBlackListed(String name) {
		return blackList.contains(name);
	}
}
