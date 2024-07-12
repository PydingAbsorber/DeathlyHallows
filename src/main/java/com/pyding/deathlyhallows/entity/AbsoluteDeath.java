package com.pyding.deathlyhallows.entity;

import com.emoniph.witchery.entity.EntitySpellEffect;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.IHandleDT;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.DeathHallowsMod;
import com.pyding.deathlyhallows.common.handler.ConfigHandler;
import com.pyding.deathlyhallows.common.properties.ExtendedPlayer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class AbsoluteDeath extends EntityMob implements IBossDisplayData, IHandleDT {

    private static final UUID attackingSpeedBoostModifierUUID = UUID.fromString("9c7e6fd9-2913-4c02-a0e9-330f5f3bf211");
    private static final AttributeModifier attackingSpeedBoostModifier = (new AttributeModifier(attackingSpeedBoostModifierUUID, "Govno", 12.99999809265137D, 0)).setSaved(false);
    private int teleportDelay;
    private int stareTimer;
    private Entity lastEntityToAttack;
    private boolean isAggressive;

    private long cd;

    public int rage;

    public float baseDamage = 33 * ConfigHandler.deathDifficulty;

    public static EntityPlayer mvp;
    public float bestDamage;

    public AbsoluteDeath(World par1World) {
        super(par1World);
        this.setSize(0.6F, 1.8F);
        super.stepHeight = 2.0F;
        super.experienceValue = 80000;
    }

    protected int decreaseAirSupply(int par1) {
        return par1;
    }

    protected boolean canDespawn() {
        return false;
    }

    public String getCommandSenderName() {
        return this.hasCustomNameTag() ? this.getCustomNameTag() : StatCollector.translateToLocal("dh.entity.death");
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(2000.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.8D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4000.0D);
    }

    @Override
    public boolean getCanSpawnHere() {
        return false;
    }

    protected void entityInit() {
        super.entityInit();
        super.dataWatcher.addObject(19, new Byte((byte) 0));
        super.dataWatcher.addObject(20, new Byte((byte) 0));
        super.dataWatcher.addObject(21, new Byte((byte) 0));
    }

    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
        super.writeEntityToNBT(par1NBTTagCompound);
    }

    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
        super.readEntityFromNBT(par1NBTTagCompound);
    }

    protected Entity findPlayerToAttack() {
        EntityPlayer entityplayer = super.worldObj.getClosestVulnerablePlayerToEntity(this, 64.0D);
        if (entityplayer != null) {
            if (this.shouldAttackPlayer(entityplayer)) {
                this.isAggressive = true;
                if (this.stareTimer++ == 60) {
                    this.stareTimer = 0;
                    return entityplayer;
                }
            } else {
                this.stareTimer = 0;
            }
        }

        return null;
    }

    private boolean shouldAttackPlayer(EntityPlayer par1EntityPlayer) {
        ItemStack itemstack = par1EntityPlayer.inventory.armorInventory[3];
        Vec3 vec3 = par1EntityPlayer.getLook(1.0F).normalize();
        Vec3 vec31 = Vec3.createVectorHelper(super.posX - par1EntityPlayer.posX, super.boundingBox.minY + (double) (super.height / 2.0F) - (par1EntityPlayer.posY + (double) par1EntityPlayer.getEyeHeight()), super.posZ - par1EntityPlayer.posZ);
        double d0 = vec31.lengthVector();
        vec31 = vec31.normalize();
        double d1 = vec3.dotProduct(vec31);
        return d1 > 1.0D - 0.025D / d0;
    }

    public int daun;

    @Override
    public void onLivingUpdate() {
        if (this.lastEntityToAttack != super.entityToAttack) {
            IAttributeInstance i = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            i.removeModifier(attackingSpeedBoostModifier);
            if (super.entityToAttack != null) {
                i.applyModifier(attackingSpeedBoostModifier);
            }
        }
        double radius = 16;
        List<Entity> projectiles = this.worldObj.getEntitiesWithinAABBExcludingEntity(this,
                AxisAlignedBB.getBoundingBox(this.posX - radius, this.posY - radius, this.posZ - radius, this.posX + radius, this.posY + radius, this.posZ + radius));
        for (Object o : projectiles) {
/*            if(o instanceof EntityThrowable)
                ((EntityThrowable) o).setDead();
            else if(o instanceof EntityArrow)
                ((EntityArrow) o).setDead();
            else if(o instanceof EntityWitchProjectile)
                ((EntityWitchProjectile) o).setDead();*/
            if (o instanceof EntitySpellEffect) {
                ((EntitySpellEffect) o).setDead();
                if (!worldObj.isRemote)
                    ChatUtil.sendTranslated(EnumChatFormatting.GREEN, worldObj.getClosestPlayerToEntity(this, 64), "dh.chat.nomagic");
            }
        }
        this.lastEntityToAttack = super.entityToAttack;
        super.isJumping = false;
        if (super.entityToAttack != null) {
            this.faceEntity(super.entityToAttack, 100.0F, 100.0F);
        }
        if (super.worldObj.rand.nextDouble() < 0.05D && this.getAttackTarget() != null && (this.getAttackTarget().isAirBorne || this.getAttackTarget() instanceof EntityPlayer && ((EntityPlayer) this.getAttackTarget()).capabilities.isFlying) && !this.getAttackTarget().isPotionActive(Potion.moveSlowdown)) {
            this.getAttackTarget().addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 5));
        }
        radius = 2;
        if (this.getLastAttacker() != null)
            daun++;
        List<EntityLiving> entities = this.worldObj.getEntitiesWithinAABBExcludingEntity(this,
                AxisAlignedBB.getBoundingBox(this.posX - radius, this.posY - radius, this.posZ - radius, this.posX + radius, this.posY + radius, this.posZ + radius));
        for (Object o : entities) {
            if (o instanceof EntityPlayer && this.isAggressive) {
                EntityPlayer player = (EntityPlayer) o;
                daun = 0;
                if (!(((EntityPlayer) o).capabilities.isCreativeMode))
                    attackPlayer(player, 1);
                player.getEntityData().setBoolean("adaptiveDamage", true);
                this.setLastAttacker(player);
            }
        }
        if (daun > 120 && this.getLastAttacker() != null) {
            EntityPlayer player = (EntityPlayer) this.getLastAttacker();
            if (!player.capabilities.isCreativeMode) {
                this.teleportToEntity(this.getLastAttacker());
                if (daun > 130) {
                    this.setPosition(player.posX, player.posY, player.posZ);
                    this.worldObj.playSoundAtEntity(this, "dh:mobs.sonido", 1F, 1F);
                    attackPlayer(player, 3);
                    player.getEntityData().setBoolean("adaptiveDamage", true);
                }
            }
        }
        int block = 0;
        if (this.getEntityData().getInteger("fireblock") >= 100) {
            block++;
        }
        if (this.getEntityData().getInteger("waterblock") >= 100) {
            block++;
        }
        if (this.getEntityData().getInteger("earthblock") >= 100) {
            block++;
        }
        if (this.getEntityData().getInteger("genericblock") >= 100) {
            block++;
        }
        if (this.getEntityData().getInteger("magicblock") >= 100) {
            block++;
        }
        if (this.getEntityData().getInteger("voidblock") >= 100) {
            block++;
        }
        if (this.getEntityData().getInteger("absoluteblock") >= 100) {
            block++;
        }
        if (this.getEntityData().getInteger("witherblock") >= 100) {
            block++;
        }
        if (block >= 7) {
            this.getEntityData().setInteger("fireblock", 0);
            this.getEntityData().setInteger("waterblock", 0);
            this.getEntityData().setInteger("earthblock", 0);
            this.getEntityData().setInteger("genericblock", 0);
            this.getEntityData().setInteger("magicblock", 0);
            this.getEntityData().setInteger("voidblock", 0);
            this.getEntityData().setInteger("absoluteblock", 0);
            this.getEntityData().setInteger("witherblock", 0);
            for (Object o : entities) {
                if (o instanceof EntityPlayer && !(((EntityPlayer) o).capabilities.isCreativeMode) && this.isAggressive) {
                    EntityPlayer player = (EntityPlayer) o;
                    attackPlayer(player, 10 * block);
                    this.worldObj.playSoundAtEntity(this, "dh:mobs.sonido", 1F, 1F);
                    teleportRandomly();
                    rage = 300;
                }
            }
        }
        if (rage > 0) {
            rage--;
            attac();
        } else this.noClip = false;
        super.onLivingUpdate();
    }

    public int damageType = 0;
    public int absorbedCount = 0;

    public void attackPlayer(EntityPlayer player, int multiplier) {
        player.getEntityData().setBoolean("adaptiveDamage", true);
        DamageSource adaptive = new DamageSource("adaptive").setDamageBypassesArmor();
        if (player.getEntityData().getBoolean("absorbedDamage")) {
            damageType++;
            absorbedCount++;
            if (absorbedCount > 8 && (baseDamage + (baseDamage * (0.3 * ConfigHandler.deathDifficulty)) < Float.MAX_VALUE)) {
                baseDamage = (float) (baseDamage + (baseDamage * (0.3 * ConfigHandler.deathDifficulty)));
            }
            switch (damageType) {
                case 1: {
                    adaptive = new DamageSource("adaptive").setFireDamage();
                    break;
                }
                case 2: {
                    adaptive = new DamageSource("adaptive").setMagicDamage();
                    break;
                }
                case 3: {
                    adaptive = new DamageSource("adaptive").setExplosion();
                    break;
                }
                case 4: {
                    adaptive = new DamageSource("adaptive").setProjectile();
                    break;
                }
                case 5: {
                    adaptive = new DamageSource("adaptive").setDamageAllowedInCreativeMode();
                    break;
                }
                case 6: {
                    adaptive = new DamageSource("adaptive").setDamageIsAbsolute();
                    break;
                }
                case 7: {
                    adaptive = DamageSource.outOfWorld;
                    break;
                }
                case 8: {
                    adaptive = DamageSource.starve;
                    break;
                }
                case 9: {
                    adaptive = new DamageSource("adaptive").setDamageBypassesArmor();
                    damageType = 0;
                }
            }
        }
        damage(player, baseDamage * multiplier, adaptive);
    }

    public void damage(EntityPlayer player, float amount, DamageSource source) {
        player.attackEntityFrom(source, amount);
    }

    public void attac() {
        if (this.getAITarget() != null && this.getAITarget() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) this.getAITarget();
            if (!player.capabilities.isCreativeMode) {
                double speed = 2;
                EntityLivingBase target = this.getAITarget();
                double distanceToTarget = this.getDistanceToEntity(target);
                if (distanceToTarget > 2) {
                    Vec3 targetPosition = Vec3.createVectorHelper(target.posX, target.posY, target.posZ);
                    double angle = Math.atan2(targetPosition.zCoord - this.posZ, targetPosition.xCoord - this.posX);
                    double motionX = Math.cos(angle) * speed;
                    double motionZ = Math.sin(angle) * speed;
                    this.motionX = motionX;
                    this.motionZ = motionZ;
                } else {
                    this.teleportRandomly();
                    this.motionX = 0;
                    this.motionZ = 0;
                    this.swingItem();
                    if (ConfigHandler.deathDifficulty != 1)
                        this.heal(1);
                }
                if (this.posY > target.posY)
                    this.motionY = -1;
                else this.motionY = 1;
                this.noClip = true;
            } else {
                ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "dh.chat.creative");
                this.noClip = true;
                this.teleportRandomly();
                rage = 0;
            }
        }
    }

    public boolean teleportRandomly() {
        double d0 = super.posX + (super.rand.nextDouble() - 0.5D) * 32.0D;
        double d1 = super.posY + (double) (super.rand.nextInt(64) - 32);
        double d2 = super.posZ + (super.rand.nextDouble() - 0.5D) * 32.0D;
        return this.teleportTo(d0, d1, d2);
    }

    protected boolean teleportToEntity(Entity par1Entity) {
        Vec3 vec3 = Vec3.createVectorHelper(super.posX - par1Entity.posX, super.boundingBox.minY + (double) (super.height / 2.0F) - par1Entity.posY + (double) par1Entity.getEyeHeight(), super.posZ - par1Entity.posZ);
        vec3 = vec3.normalize();
        double d0 = 16.0D;
        double d1 = super.posX + (super.rand.nextDouble() - 0.5D) * 8.0D - vec3.xCoord * d0;
        double d2 = super.posY + (double) (super.rand.nextInt(16) - 8) - vec3.yCoord * d0;
        double d3 = super.posZ + (super.rand.nextDouble() - 0.5D) * 8.0D - vec3.zCoord * d0;
        return this.teleportTo(d1, d2, d3);
    }

    protected boolean teleportTo(double par1, double par3, double par5) {
        double d3 = super.posX;
        double d4 = super.posY;
        double d5 = super.posZ;
        super.posX = par1;
        super.posY = par3;
        super.posZ = par5;
        boolean flag = false;
        int i = MathHelper.floor_double(super.posX);
        int j = MathHelper.floor_double(super.posY);
        int k = MathHelper.floor_double(super.posZ);
        if (super.worldObj.blockExists(i, j, k)) {
            boolean short1 = false;

            while (!short1 && j > 0) {
                Block l = super.worldObj.getBlock(i, j - 1, k);
                if (l.getMaterial().blocksMovement()) {
                    short1 = true;
                } else {
                    --super.posY;
                    --j;
                }
            }

            if (short1) {
                this.setPosition(super.posX, super.posY, super.posZ);
                if (super.worldObj.getCollidingBoundingBoxes(this, super.boundingBox).isEmpty() && !super.worldObj.isAnyLiquid(super.boundingBox)) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            this.setPosition(d3, d4, d5);
            return false;
        } else {
            short var30 = 128;

            for (int var31 = 0; var31 < var30; ++var31) {
                double d6 = (double) var31 / ((double) var30 - 1.0D);
                float f = (super.rand.nextFloat() - 0.5F) * 0.2F;
                float f1 = (super.rand.nextFloat() - 0.5F) * 0.2F;
                float f2 = (super.rand.nextFloat() - 0.5F) * 0.2F;
                double d7 = d3 + (super.posX - d3) * d6 + (super.rand.nextDouble() - 0.5D) * (double) super.width * 2.0D;
                double d8 = d4 + (super.posY - d4) * d6 + super.rand.nextDouble() * (double) super.height;
                double d9 = d5 + (super.posZ - d5) * d6 + (super.rand.nextDouble() - 0.5D) * (double) super.width * 2.0D;
                super.worldObj.spawnParticle("portal", d7, d8, d9, f, f1, f2);
            }

            super.worldObj.playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
            this.worldObj.playSoundAtEntity(this, "dh:mobs.sonido", 1F, 1F);
            return true;
        }
    }

    protected String getLivingSound() {
        return null;
    }

    protected String getHurtSound() {
        return "mob.skeleton.hurt";
    }

    protected String getDeathSound() {
        return "mob.skeleton.death";
    }

    protected Item getDropItem() {
        return Items.bone;
    }

    protected void dropFewItems(boolean par1, int par2) {
    }

    @Override
    protected void attackEntity(Entity entity, float damage) {
        float damageBonus = (float) this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        if (this.getEntityData().hasKey("block")) {
            damage = damageBonus + this.getEntityData().getInteger("block") * 500;
        }
        super.attackEntity(entity, damage);
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
        return super.attackEntityFrom(par1DamageSource, par2);
    }

    public boolean attackEntityAsMob(Entity par1Entity) {
        return true;
    }

    public void onDeath(DamageSource par1DamageSource) {
        super.onDeath(par1DamageSource);
        dropDeathlyHallows();
        this.dropItem(DeathHallowsMod.deathShard, 1);
        if (!super.worldObj.isRemote) {
            ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, this, 1.0D, 2.0D, 16);
        }

        this.setDead();
    }

    public void dropDeathlyHallows() {
        List id = null;
        List entities = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.getBoundingBox().expand(80, 80, 80));
        for (Object o : entities) {
            EntityPlayer player = (EntityPlayer) o;
            id.add(player.getEntityId());
        }
        if (id != null) {
            for (Object o : id) {
                int objId = (int) o;
                EntityPlayer player = (EntityPlayer) Minecraft.getMinecraft().theWorld.getEntityByID(objId);
                if (player != null) {
                    NBTTagCompound nbt = this.getEntityData().getCompoundTag(String.valueOf(objId));
                    if (nbt.getFloat("damageDealt") > bestDamage) {
                        bestDamage = nbt.getFloat("damageDealt");
                        mvp = player;
                        System.out.println(bestDamage);
                        System.out.println(mvp);
                    }
                }
            }
        }
        if (mvp != null) {
            ChatUtil.sendTranslated(EnumChatFormatting.GREEN, mvp, "dh.chat.choice");
            ExtendedPlayer props = ExtendedPlayer.get(mvp);
            props.setChoice(true);
        } else {
            double random = Math.random();
            if (random < 0.33)
                this.dropItem(DeathHallowsMod.elderWand, 1);
            else if (random < 0.66)
                this.dropItem(DeathHallowsMod.invisibilityMantle, 1);
            else this.dropItem(DeathHallowsMod.resurrectionStone, 1);
        }
    }

    @Override
    public float getCapDT(DamageSource damageSource, float v) {
        return 0;
    }
}
