package com.pyding.deathlyhallows.entities;

import com.emoniph.witchery.entity.EntitySpellEffect;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.IHandleDT;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.items.DHItems;
import com.pyding.deathlyhallows.utils.DHConfig;
import com.pyding.deathlyhallows.utils.DHUtils;
import com.pyding.deathlyhallows.utils.DamageSourceAdaptive;
import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class EntityAbsoluteDeath extends EntityMob implements IBossDisplayData, IHandleDT {
	
	private static final UUID attackingSpeedBoostModifierUUID = UUID.fromString("9c7e6fd9-2913-4c02-a0e9-330f5f3bf211");
	private static final AttributeModifier attackingSpeedBoostModifier = (new AttributeModifier(attackingSpeedBoostModifierUUID, "Govno", 12.99999809265137D, 0)).setSaved(false);
	private Entity lastEntityToAttack;
	private EntityPlayer mvp;
	private boolean isAggressive;
	public float 
			baseDamage,
			bestDamage;

	private int
			stareTimer,
			rage,
			daun,
			damageType = 0,
			absorbedCount = 0;

	public EntityAbsoluteDeath(World w) {
		super(w);
		setSize(0.6F, 1.8F);
		stepHeight = 2.0F;
		experienceValue = 80000;
		baseDamage = 33 * DHConfig.deathDifficulty;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(2000.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.8D);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4000.0D);
	}

	@Override
	public boolean getCanSpawnHere() {
		return false;
	}
	
	@Override
	protected Entity findPlayerToAttack() {
		EntityPlayer entityplayer = worldObj.getClosestVulnerablePlayerToEntity(this, 64D);
		if(entityplayer == null) {
			return null;
		}
		if(shouldAttackPlayer(entityplayer)) {
			isAggressive = true;
			if(stareTimer++ == 60) {
				stareTimer = 0;
				return entityplayer;
			}
			return null;
		}
		stareTimer = 0;
		return null;
	}

	private boolean shouldAttackPlayer(EntityPlayer par1EntityPlayer) {
		Vec3 vec3 = par1EntityPlayer.getLook(1.0F).normalize();
		Vec3 vec31 = Vec3.createVectorHelper(super.posX - par1EntityPlayer.posX, super.boundingBox.minY + (double)(super.height / 2.0F) - (par1EntityPlayer.posY + (double)par1EntityPlayer.getEyeHeight()), super.posZ - par1EntityPlayer.posZ);
		double d0 = vec31.lengthVector();
		vec31 = vec31.normalize();
		double d1 = vec3.dotProduct(vec31);
		return d1 > 1.0D - 0.025D / d0;
	}
	
	@Override
	public void onLivingUpdate() {
		if(this.lastEntityToAttack != super.entityToAttack) {
			IAttributeInstance i = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			i.removeModifier(attackingSpeedBoostModifier);
			if(super.entityToAttack != null) {
				i.applyModifier(attackingSpeedBoostModifier);
			}
		}
		@SuppressWarnings("unchecked")
		List<Entity> projectiles = DHUtils.getEntitiesAround(Entity.class, this, 16);
		for(Entity o: projectiles) {
			if(o instanceof EntitySpellEffect) {
				o.setDead();
				if(!worldObj.isRemote) {
					ChatUtil.sendTranslated(EnumChatFormatting.GREEN, worldObj.getClosestPlayerToEntity(this, 64), "dh.chat.nomagic");
				}
			}
		}
		lastEntityToAttack = entityToAttack;
		isJumping = false;
		if(entityToAttack != null) {
			faceEntity(entityToAttack, 100.0F, 100.0F);
		}
		if(worldObj.rand.nextDouble() < 0.05D && getAttackTarget() != null && (getAttackTarget().isAirBorne || getAttackTarget() instanceof EntityPlayer && ((EntityPlayer)getAttackTarget()).capabilities.isFlying) && !getAttackTarget().isPotionActive(Potion.moveSlowdown)) {
			getAttackTarget().addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 5));
		}
		if(this.getLastAttacker() != null) {
			daun++;
		}
		@SuppressWarnings("unchecked")
		List<EntityPlayer> entities = DHUtils.getEntitiesAround(EntityPlayer.class, this, 2);
		if(isAggressive) {
			for(EntityPlayer player: entities) {
				daun = 0;
				if(!(player.capabilities.isCreativeMode)) {
					attackPlayer(player, 1);
				}
				player.getEntityData().setBoolean("adaptiveDamage", true);
				setLastAttacker(player);
			}
		}
		if(daun > 120 && getLastAttacker() != null) {
			EntityPlayer player = (EntityPlayer)this.getLastAttacker();
			if(!player.capabilities.isCreativeMode) {
				teleportToEntity(this.getLastAttacker());
				if(daun > 130) {
					setPosition(player.posX, player.posY, player.posZ);
					worldObj.playSoundAtEntity(this, "dh:mobs.sonido", 1F, 1F);
					attackPlayer(player, 3);
					player.getEntityData().setBoolean("adaptiveDamage", true);
				}
			}
		}
		int block = 0;
		if(getEntityData().getInteger("fireblock") >= 100) {
			block++;
		}
		if(getEntityData().getInteger("waterblock") >= 100) {
			block++;
		}
		if(getEntityData().getInteger("earthblock") >= 100) {
			block++;
		}
		if(getEntityData().getInteger("genericblock") >= 100) {
			block++;
		}
		if(getEntityData().getInteger("magicblock") >= 100) {
			block++;
		}
		if(getEntityData().getInteger("voidblock") >= 100) {
			block++;
		}
		if(getEntityData().getInteger("absoluteblock") >= 100) {
			block++;
		}
		if(getEntityData().getInteger("witherblock") >= 100) {
			block++;
		}
		if(block >= 7) {
			NBTTagCompound tag = getEntityData();
			tag.setInteger("fireblock", 0);
			tag.setInteger("waterblock", 0);
			tag.setInteger("earthblock", 0);
			tag.setInteger("genericblock", 0);
			tag.setInteger("magicblock", 0);
			tag.setInteger("voidblock", 0);
			tag.setInteger("absoluteblock", 0);
			tag.setInteger("witherblock", 0);
			if(isAggressive) {
				for(EntityPlayer p: entities) {
					if(p.capabilities.isCreativeMode) {
						continue;
					}
					attackPlayer(p, 10 * block);
					worldObj.playSoundAtEntity(this, "dh:mobs.sonido", 1F, 1F);
					teleportRandomly();
					rage = 300;
				}
			}
		}
		if(rage > 0) {
			rage--;
			makePainfully();
		}
		else {
			noClip = false;
		}
		super.onLivingUpdate();
	}

	public void attackPlayer(EntityPlayer player, int multiplier) {
		player.getEntityData().setBoolean("adaptiveDamage", true);
		if(player.getEntityData().getBoolean("absorbedDamage")) {
			++damageType;
			damageType %= 10;
			++absorbedCount;
			if(absorbedCount > 8 && (baseDamage + (baseDamage * (0.3 * DHConfig.deathDifficulty)) < Float.MAX_VALUE)) {
				baseDamage = (float)(baseDamage + (baseDamage * (0.3 * DHConfig.deathDifficulty)));
			}
		}
		DamageSource source = getDamageSource(damageType);
		damage(player, baseDamage * multiplier, source);
		if(DHConfig.deathDifficulty > 1)
			DHUtils.fuckMagic(player,0.01f);
	}

	private DamageSource getDamageSource(int damageType) {
		switch(damageType) {
			case 8: 
				return DamageSource.outOfWorld;
			case 9:
				return DamageSource.starve; 
		}
		return new DamageSourceAdaptive(damageType);
	}


	public void damage(EntityPlayer player, float amount, DamageSource source) {
		player.attackEntityFrom(source, amount);
	}

	public void makePainfully() {
		if(getAITarget() == null || !(getAITarget() instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer)getAITarget();
		if(player.capabilities.isCreativeMode) {
			ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "dh.chat.creative");
			noClip = true;
			teleportRandomly();
			rage = 0;
			return;
		}

		double speed = 2;
		EntityLivingBase target = this.getAITarget();
		double distanceToTarget = this.getDistanceToEntity(target);
		if(distanceToTarget > 2) {
			Vec3 targetPosition = Vec3.createVectorHelper(target.posX, target.posY, target.posZ);
			double angle = Math.atan2(targetPosition.zCoord - posZ, targetPosition.xCoord - posX);
			double motionX = Math.cos(angle) * speed;
			double motionZ = Math.sin(angle) * speed;
			this.motionX = motionX;
			this.motionZ = motionZ;
		}
		else {
			teleportRandomly();
			motionX = 0;
			motionZ = 0;
			swingItem();
			if(DHConfig.deathDifficulty > 1) {
				heal(1);
				DHUtils.fuckMagic(player,0.1f);
			}
		}
		motionY = posY > target.posY ? -1 : 1;
		noClip = true;
	}

	public boolean teleportRandomly() {
		return this.teleportTo(
				posX + (rand.nextDouble() - 0.5D) * 32.0D,
				posY + rand.nextInt(64) - 32,
				posZ + (rand.nextDouble() - 0.5D) * 32.0D
		);
	}

	protected boolean teleportToEntity(Entity e) {
		Vec3 vec3 = Vec3.createVectorHelper(
								posX - e.posX,
								boundingBox.minY + height / 2.0D - e.posY + e.getEyeHeight(),
								posZ - e.posZ
						)
						.normalize();
		double range = 16.0D;
		double newX = posX + (rand.nextDouble() - 0.5D) * 8.0D - vec3.xCoord * range;
		double newY = posY + rand.nextInt(16) - 8 - vec3.yCoord * range;
		double newZ = posZ + (rand.nextDouble() - 0.5D) * 8.0D - vec3.zCoord * range;
		return teleportTo(newX, newY, newZ);
	}

	protected boolean teleportTo(double newX, double newY, double newZ) {
		double prevX = posX;
		double prevY = posY;
		double prevZ = posZ;
		posX = newX;
		posY = newY;
		posZ = newZ;
		boolean flag = false;
		int x = MathHelper.floor_double(posX);
		int y = MathHelper.floor_double(posY);
		int z = MathHelper.floor_double(posZ);
		if(worldObj.blockExists(x, y, z)) {
			boolean collided = false;
			while(!collided && y > 0) {
				Block l = worldObj.getBlock(x, y - 1, z);
				if(l.getMaterial().blocksMovement()) {
					collided = true;
				}
				else {
					--posY;
					--y;
				}
			}
			if(collided) {
				setPosition(posX, posY, posZ);
				if(super.worldObj.getCollidingBoundingBoxes(this, boundingBox)
								 .isEmpty() && !worldObj.isAnyLiquid(boundingBox)) {
					flag = true;
				}
			}
		}
		if(flag) {
			for(int i = 0; i < 128; ++i) {
				double d6 = i / 128D;
				Supplier<Float> guessWhat = () -> (rand.nextFloat() - 0.5F) * 0.2F;
				worldObj.spawnParticle(
						"portal",
						prevX + (posX - prevX) * d6 + (rand.nextDouble() - 0.5D) * width * 2.0D,
						prevY + (posY - prevY) * d6 + rand.nextDouble() * height,
						prevZ + (posZ - prevZ) * d6 + (rand.nextDouble() - 0.5D) * width * 2.0D,
						guessWhat.get(),
						guessWhat.get(),
						guessWhat.get()
				);
			}

			worldObj.playSoundEffect(prevX, prevY, prevZ, "mob.endermen.portal", 1.0F, 1.0F);
			worldObj.playSoundAtEntity(this, "dh:mobs.sonido", 1F, 1F);
			return true;
		}
		// no teleport
		setPosition(prevX, prevY, prevZ);
		return false;
	}

	@Override
	protected String getLivingSound() {
		return null;
	}

	@Override
	protected String getHurtSound() {
		return "mob.skeleton.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.skeleton.death";
	}

	@Override
	protected Item getDropItem() {
		return Items.bone;
	}

	@Override
	protected void dropFewItems(boolean drop, int few) {
	}

	@Override
	protected void attackEntity(Entity entity, float damage) {
		float damageBonus = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
		if(getEntityData().hasKey("block")) {
			damage = damageBonus + getEntityData().getInteger("block") * 500;
		}
		super.attackEntity(entity, damage);
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity) {
		return true;
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		// TODO rework on GUI and maybe something else
		findMVP();
		giftHallows();
		dropItem(DHItems.deathShard, 1);
		if(!worldObj.isRemote) {
			ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, this, 1.0D, 2.0D, 16);
		}
		super.setDead();
	}

	private void giftHallows() {
		if(mvp != null) {
			ChatUtil.sendTranslated(EnumChatFormatting.GREEN, mvp, "dh.chat.choice");
			DeathlyProperties props = DeathlyProperties.get(mvp);
			props.setChoice(true);
			return;
		}
		double random = Math.random();
		if(random < 0.33) {
			dropItem(DHItems.elderWand, 1);
		}
		else if(random < 0.66) {
			dropItem(DHItems.invisibilityMantle, 1);
		}
		else {
			dropItem(DHItems.resurrectionStone, 1);
		}
	}

	private void findMVP() {
		for(EntityPlayer p: DHUtils.getEntitiesAround(EntityPlayer.class, this, 80)) {
			if(p == null) {
				continue;
			}
			NBTTagCompound nbt = getEntityData().getCompoundTag(String.valueOf(p.getEntityId()));
			if(!(nbt.getFloat("damageDealt") > bestDamage)) {
				continue;
			}
			bestDamage = nbt.getFloat("damageDealt");
			mvp = p;
			// TODO log
			DeathlyHallows.LOG.info(bestDamage);
			DeathlyHallows.LOG.info(mvp);
		}
	}

	@Override
	public float getCapDT(DamageSource damageSource, float v) {
		return 0;
	}

	public void setRage() {
		rage = 100;
	}
}
