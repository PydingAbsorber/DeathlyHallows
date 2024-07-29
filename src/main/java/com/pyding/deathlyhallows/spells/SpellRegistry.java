package com.pyding.deathlyhallows.spells;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketAnimaMobRender;
import com.pyding.deathlyhallows.network.packets.PacketDisableFlight;
import com.pyding.deathlyhallows.network.packets.PacketPlayerRender;
import com.pyding.deathlyhallows.utils.properties.ExtendedPlayer;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class SpellRegistry {
	public String getName(int id) {
		switch(id) {
			case 1:
				return "§eAccio";
			case 2:
				return "§eAguamenti";
			case 3:
				return "§eAlohomora";
			case 4:
				return "§cAvadaKedavra";
			case 5:
				return "§eCaveInimicum";
			case 6:
				return "§eColloportus";
			case 8:
				return "§eConfundus";
			case 9:
				return "§cCrucio";
			case 10:
				return "§eDefodio";
			case 12:
				return "§eEnnervate";
			case 13:
				return "§eEpiskey";
			case 15:
				return "§eExpelliarmus";
			case 16:
				return "§eFlagrate";
			case 17:
				return "§eFlipendo";
			case 19:
				return "§eImpedimenta";
			case 20:
				return "§cImperio";
			case 21:
				return "§eIncendio";
			case 22:
				return "§eLumos";
			case 23:
				return "§eMeteolojinxRecanto";
			case 26:
				return "§eNox";
			case 31:
				return "§eProtego";
			case 36:
				return "§eStupefy";
			case 39:
				return "§cIgnianima";
			case 40:
				return "§eCarnosaDiem";
			case 41:
				return "§cMORSMORDRE";
			case 42:
				return "§eTormentum";
			case 43:
				return "§9LEONARD_1";
			case 44:
				return "§9LEONARD_2";
			case 45:
				return "§9LEONARD_3";
			case 46:
				return "§9LEONARD_4";
			case 47:
				return "§eAttraho";
			case 48:
				return "§5Anima§cInteritus";
			case 49:
				return "§5Hor§ccrux";
			case 50:
				return "§5Sectumsempra";
			case 51:
				return "§5LumosTempestas";
			case 52:
				return "§5BombardaMaxima";
			case 53:
				return "§5Leonardo 1-4 urn";
			case 54:
				return "§5Decaimiento de la fuerza";
			case 55:
				return "§5Graviole";
		}
		return "IdError";
	}

	public int getId(String name) {
		switch(name) {
			case ("Accio"):
				return 1;
			case ("Aguamenti"):
				return 2;
			case ("Alohomora"):
				return 3;
			case ("AvadaKedavra"):
				return 4;
			case ("CaveInimicum"):
				return 5;
			case ("Colloportus"):
				return 6;
			case ("Confundus"):
				return 8;
			case ("Crucio"):
				return 9;
			case ("Defodio"):
				return 10;
			case ("Ennervate"):
				return 12;
			case ("Episkey"):
				return 13;
			case ("Expelliarmus"):
				return 15;
			case ("Flagrate"):
				return 16;
			case ("Flipendo"):
				return 17;
			case ("Impedimenta"):
				return 19;
			case ("Imperio"):
				return 20;
			case ("Incendio"):
				return 21;
			case ("Lumos"):
				return 22;
			case ("MeteolojinxRecanto"):
				return 23;
			case ("Nox"):
				return 26;
			case ("Protego"):
				return 31;
			case ("Stupefy"):
				return 36;
			case ("Ignianima"):
				return 39;
			case ("CarnosaDiem"):
				return 40;
			case ("MORSMORDRE"):
				return 41;
			case ("Tormentum"):
				return 42;
			case ("LEONARD_1"):
				return 43;
			case ("LEONARD_2"):
				return 44;
			case ("LEONARD_3"):
				return 45;
			case ("LEONARD_4"):
				return 46;
			case ("Attraho"):
				return 47;
			case ("AnimaInteritus"):
				return 48;
			case ("Horcrux"):
				return 49;
			case ("Sectumsempra"):
				return 50;
			case ("LumosTempestas"):
				return 51;
			case ("BombardaMaxima"):
				return 52;
			case ("Leonardo 1-4 urn"):
				return 53;
			case ("Decaimiento de la fuerza"):
				return 54;
			case ("Graviole"):
				return 55;
		}
		return 0;
	}

	public static int spellCount = 55;

	public static float cd = 2500;
	public static int cost = 120; //anima
	public static int sectumCost = 10;
	public static int magicCost = 5;

	public static float spellPower = 1;

	public static float spellRadius = 1;

	public static float baseDamage = 1000;
	public static float hpCost = 10;
	public static float discount = 1;
	public static EntityPlayer player;
	Multimap<String, AttributeModifier> attributes = HashMultimap.create();

	public void performEffect(EntityPlayer player2, World world, int spellId) {
		player = player2;
		ItemStack stack = player.getHeldItem();
		long lastUsedTime = stack.getTagCompound().getLong("lastUsedTime");
		if(System.currentTimeMillis() - lastUsedTime > cd) {
			cd = 2500;
			ExtendedPlayer props1 = ExtendedPlayer.get(player);
			if(props1.getElfLvl() > 0) {
				discount = 1 - props1.getElfLvl() / 20;
				cd = 2500 * discount;
				spellPower = props1.getElfLvl();
				cost = (int)(120 * discount);
				sectumCost = (int)(10 * discount);
				magicCost = (int)(5 * discount);
				spellRadius = 1 * (spellPower / 5);
			}
			double x = player.posX;
			double y = player.posY + player.getEyeHeight();
			double z = player.posZ;
			Vec3 lookVec = player.getLookVec();
			double endX = x + lookVec.xCoord * 60;
			double endY = y + lookVec.yCoord * 60;
			double endZ = z + lookVec.zCoord * 60;
			if(spellId < 48) {
				performWitcherySpell(player, world, spellId);
			}
			else {
				float damage = (float)player.getEntityAttribute(SharedMonsterAttributes.attackDamage)
											.getAttributeValue();
				switch(spellId) {
					case 48: {
						if(Infusion.getInfusionID(player) == 4 && props1.getElfLvl() > 0) {
							int cursedCount = 0;
							int elfBonus = 1 + props1.getElfLvl() / 5;
							props1.setSpellsUsed(props1.getSpellsUsed() + 1);
							double radius = 64;
							for(Object o: getEntities(radius, EntityLivingBase.class)) {
								if(cursedCount > (elfBonus - 1)) {
									break;
								}
								if(!o.equals(player)) {
									if(o instanceof EntityPlayer) {
										EntityPlayer targetPlayer = (EntityPlayer)o;
										if(targetPlayer.capabilities.isCreativeMode) {
											continue;
										}
									}
									EntityLivingBase target = (EntityLivingBase)o;
									if(target.isEntityAlive()) {
										if(!player.capabilities.isCreativeMode && Infusion.getNBT(player)
																						  .getInteger("witcheryInfusionCharges") < cost) {
											ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.nocharges");
											SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
											break;
										}
										else {
											cursedCount++;
											cd = 20000;
											target.getEntityData().setInteger("dhcurse", 1200);
											NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(target.dimension, target.posX, target.posY, target.posZ, radius * 1.2);
											if(target instanceof EntityPlayer) {
												EntityPlayer targetPlayer = (EntityPlayer)target;
												ExtendedPlayer props = ExtendedPlayer.get(targetPlayer);
												props.setCurrentDuration(1200);
												props.setSource(player);
												target.setLastAttacker(player);
												props.setCoordinates(targetPlayer.posX, targetPlayer.posY, targetPlayer.posZ, targetPlayer.dimension);
												props1.setCoordinates(targetPlayer.posX, targetPlayer.posY, targetPlayer.posZ, targetPlayer.dimension);
												player.getEntityData().setInteger("casterCurse", 1200);
												PacketPlayerRender packet = new PacketPlayerRender(targetPlayer.getEntityData());
												DHPacketProcessor.sendToAllAround(packet, targetPoint);
											}
											else {
												target.setLastAttacker(player);
												PacketAnimaMobRender packet = new PacketAnimaMobRender(target.getEntityData(), target.getEntityId());
												DHPacketProcessor.sendToAllAround(packet, targetPoint);
												target.getEntityData().setDouble("chainX", target.posX);
												target.getEntityData().setDouble("chainY", target.posY);
												target.getEntityData().setDouble("chainZ", target.posZ);
											}
											if(ExtendedPlayer.get(player) != null) {
												ExtendedPlayer props = ExtendedPlayer.get(player);
												if(props.getElfLvl() > 0) {
													cd = 20000 * (1 - props.getElfLvl() / 20);
												}
											}
											if(!player.capabilities.isCreativeMode) {
												Infusion.setCurrentEnergy(player, Infusion.getNBT(player)
																						  .getInteger("witcheryInfusionCharges") - cost);
											}
										}
									}
								}
							}
							if(cursedCount > 0) {
								if(Math.random() > 0.5) {
									world.playSoundAtEntity(player, "dh:spell.anima3", 1F, 1F);
								}
								else {
									if(Math.random() > 0.5) {
										world.playSoundAtEntity(player, "dh:spell.anima2", 1F, 1F);
									}
									else {
										world.playSoundAtEntity(player, "dh:spell.anima1", 1F, 1F);
									}
								}
							}
							else {
								SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
							}
						}
						else {
							ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.infernalrequired");
							ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "elf required");
							SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
						}
						break;
					}
					case 49: {
						if(Infusion.getInfusionID(player) == 4 && props1.getElfLvl() > 0) {
							if(!player.capabilities.isCreativeMode && Infusion.getNBT(player)
																			  .getInteger("witcheryInfusionCharges") < cost) {
								ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.nocharges");
								SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
							}
							else {
								if(!player.capabilities.isCreativeMode) {
									Infusion.setCurrentEnergy(player, Infusion.getNBT(player)
																			  .getInteger("witcheryInfusionCharges") - cost);
								}
								props1.setSpellsUsed(props1.getSpellsUsed() + 1);
								int lifes = player.getEntityData().getInteger("Horcrux");
								if(player.getMaxHealth() - hpCost > 10) {
									attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("HorcruxHP", -hpCost, 0));
									player.getAttributeMap().applyAttributeModifiers(attributes);
									attributes.clear();
									player.getEntityData().setInteger("Horcrux", lifes + 1);
									cd = 20000 * (1 - props1.getElfLvl() / 20);
								}
								else {
									ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "temptation is high but you seem to look back");
									SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
								}
								if(Math.random() > 0.5) {
									world.playSoundAtEntity(player, "dh:spell.death1", 1F, 1F);
								}
								else {
									world.playSoundAtEntity(player, "dh:spell.death2", 1F, 1F);
								}
							}
						}
						else {
							ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.infernalrequired");
							ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "elf required");
							SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
						}
						break;
					}
					case 50: {
						if(!player.capabilities.isCreativeMode && Infusion.getNBT(player)
																		  .getInteger("witcheryInfusionCharges") < sectumCost) {
							ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.nocharges");
							SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
						}
						else {
							if(!player.capabilities.isCreativeMode) {
								Infusion.setCurrentEnergy(player, Infusion.getNBT(player)
																		  .getInteger("witcheryInfusionCharges") - sectumCost);
							}
							props1.setSpellsUsed(props1.getSpellsUsed() + 1);
							AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(
									player.posX - 6 / 2, player.posY - 1, player.posZ - 6 / 2,
									player.posX + 6 / 2, player.posY + 1, player.posZ + 6 / 2
							);
							List entities = player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, boundingBox);
							for(Object o: entities) {
								if(o != player && o instanceof EntityLivingBase) {
									EntityLivingBase entity = (EntityLivingBase)o;
									entity.getEntityData().setInteger("SectumTime", 666);
									float hpLower = (float)(entity.getMaxHealth() * (0.15 * (1 + props1.getElfLvl() / 10)));
									if(entity.getMaxHealth() - hpLower > 5) {
										entity.getEntityData()
											  .setFloat("SectumHp", entity.getEntityData()
																		  .getInteger("SectumHp") + hpLower);
										attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("SectumHPAttribute", -hpLower, 0));
										entity.getAttributeMap().applyAttributeModifiers(attributes);
										attributes.clear();
										entity.setHealth(entity.getMaxHealth());
									}
								}
							}
							ParticleEffect.FLAME.send(SoundEffect.FIRE_FIRE, player, 6.0D, 2.0D, 16);
							if(Math.random() > 0.5) {
								world.playSoundAtEntity(player, "dh:spell.sectum1", 1F, 1F);
							}
							else {
								world.playSoundAtEntity(player, "dh:spell.sectum2", 1F, 1F);
							}
						}
						break;
					}
					case 51: {
						if(!player.capabilities.isCreativeMode && Infusion.getNBT(player)
																		  .getInteger("witcheryInfusionCharges") < magicCost) {
							ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.nocharges");
							SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
						}
						else {
							int count = 0;
							props1.setSpellsUsed(props1.getSpellsUsed() + 1);
							for(Object o: getEntities(32, EntityLivingBase.class)) {
								if(!o.equals(player) && count < 6 * spellRadius) {
									if(o instanceof EntityPlayer) {
										EntityPlayer targetPlayer = (EntityPlayer)o;
										if(targetPlayer.capabilities.isCreativeMode) {
											continue;
										}
									}
									EntityLivingBase target = (EntityLivingBase)o;
									if(target.isEntityAlive()) {
										world.addWeatherEffect(new EntityLightningBolt(world, target.posX, target.posY, target.posZ));
										target.attackEntityFrom(DamageSource.causePlayerDamage(player)
																			.setMagicDamage(), (baseDamage + damage) * spellPower);
										target.setLastAttacker(player);
										count++;
									}
								}
							}
							if(!player.capabilities.isCreativeMode) {
								Infusion.setCurrentEnergy(player, Infusion.getNBT(player)
																		  .getInteger("witcheryInfusionCharges") - magicCost);
							}
						}
						break;
					}
					case 52: {
						if(!player.capabilities.isCreativeMode && Infusion.getNBT(player)
																		  .getInteger("witcheryInfusionCharges") < magicCost) {
							ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.nocharges");
							SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
						}
						else {
							props1.setSpellsUsed(props1.getSpellsUsed() + 1);
							double radius = 40 * spellRadius;
							for(Object o: getEntities(radius, EntityLivingBase.class)) {
								if(!o.equals(player)) {
									if(o instanceof EntityPlayer) {
										EntityPlayer targetPlayer = (EntityPlayer)o;
										if(targetPlayer.capabilities.isCreativeMode) {
											continue;
										}
									}
									EntityLivingBase target = (EntityLivingBase)o;
									if(target.isEntityAlive()) {
										target.attackEntityFrom(DamageSource.causePlayerDamage(player)
																			.setExplosion(), (baseDamage * 3 + damage) * spellPower);
									}
								}
							}
							world.createExplosion(player, x, y, z, 15 * spellRadius, true);
							world.playSoundEffect(x, y, z, "random.explode", 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
							double d0 = world.rand.nextGaussian() * 0.02D;
							double d1 = world.rand.nextGaussian() * 0.02D;
							double d2 = world.rand.nextGaussian() * 0.02D;
							world.spawnParticle("explode", x, y, z, d0, d1, d2);
							if(Math.random() > 0.5) {
								world.playSoundAtEntity(player, "dh:spell.explode1", 1F, 1F);
							}
							else {
								world.playSoundAtEntity(player, "dh:spell.explode2", 1F, 1F);
							}
							if(!player.capabilities.isCreativeMode) {
								Infusion.setCurrentEnergy(player, Infusion.getNBT(player)
																		  .getInteger("witcheryInfusionCharges") - magicCost);
							}
						}
					}
					case 53: {
						if(!player.capabilities.isCreativeMode && Infusion.getNBT(player)
																		  .getInteger("witcheryInfusionCharges") < magicCost) {
							ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.nocharges");
							SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
						}
						else {
							props1.setSpellsUsed(props1.getSpellsUsed() + 1);
							performWitcherySpell(player, world, 43);
							performWitcherySpell(player, world, 44);
							performWitcherySpell(player, world, 45);
							performWitcherySpell(player, world, 46);
							if(!player.capabilities.isCreativeMode) {
								Infusion.setCurrentEnergy(player, Infusion.getNBT(player)
																		  .getInteger("witcheryInfusionCharges") - magicCost);
							}
						}
						break;
					}
					case 54: {
						if(!player.capabilities.isCreativeMode && Infusion.getNBT(player)
																		  .getInteger("witcheryInfusionCharges") < magicCost) {
							ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.nocharges");
							SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
						}
						else {
							props1.setSpellsUsed(props1.getSpellsUsed() + 1);
							double radius = 24;
							for(Object o: getEntities(radius, EntityLivingBase.class)) {
								if(!o.equals(player)) {
									EntityLivingBase entity = (EntityLivingBase)o;
									for(Object potionEffect: entity.getActivePotionEffects()) {
										PotionEffect effect = (PotionEffect)potionEffect;
										if(!Potion.potionTypes[effect.getPotionID()].isBadEffect()) {
											entity.removePotionEffect(effect.getPotionID());
										}
									}
								}
							}
							if(!player.capabilities.isCreativeMode) {
								Infusion.setCurrentEnergy(player, Infusion.getNBT(player)
																		  .getInteger("witcheryInfusionCharges") - magicCost);
							}
						}
						break;
					}
					case 55: {
						if(!player.capabilities.isCreativeMode && Infusion.getNBT(player)
																		  .getInteger("witcheryInfusionCharges") < magicCost) {
							ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.nocharges");
							SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
						}
						else {
							props1.setSpellsUsed(props1.getSpellsUsed() + 1);
							double radius = 16;
							List entities = getEntities(radius, EntityLivingBase.class);
							for(Object o: entities) {
								if(!o.equals(player)) {
									if(o instanceof EntityLivingBase && !(o instanceof EntityPlayer)) {
										Entity entity = (Entity)o;
										entity.motionY = -10F;
										entity.attackEntityFrom(DamageSource.fall.setDamageBypassesArmor(), (baseDamage * 10 * (props1.getElfLvl() + 1)) * damage);
									}
									if(o instanceof EntityPlayer) {
										EntityPlayer targetPlayer = (EntityPlayer)o;
										DHPacketProcessor.sendToPlayer(new PacketDisableFlight(), targetPlayer);
										targetPlayer.capabilities.isFlying = false;
										targetPlayer.motionY = -10F;
										targetPlayer.attackEntityFrom(DamageSource.fall.setDamageBypassesArmor(), (baseDamage * 10 * (props1.getElfLvl() + 1)) * damage);
									}
								}
							}
							if(!player.capabilities.isCreativeMode) {
								Infusion.setCurrentEnergy(player, Infusion.getNBT(player)
																		  .getInteger("witcheryInfusionCharges") - magicCost);
							}
							if(Math.random() > 0.5) {
								world.playSoundAtEntity(player, "dh:spell.death1", 1F, 1F);
							}
							else {
								world.playSoundAtEntity(player, "dh:spell.death2", 1F, 1F);
							}
						}
						break;
					}
				}
			}
			stack.getTagCompound().setLong("lastUsedTime", System.currentTimeMillis());
		}
		else {
			ChatUtil.sendTranslated(EnumChatFormatting.GREEN, player, "dh.chat.wait");
		}
	}

	public void performWitcherySpell(EntityPlayer player, World world, int spellId) {
		int level = 1;
		NBTTagCompound nbtTag = player.getEntityData();
		if(nbtTag != null) {
			SymbolEffect effect = EffectRegistry.instance().getEffect(spellId);
			if(nbtTag.hasKey("WITCSpellEffectEnhanced")) {
				level = nbtTag.getInteger("WITCSpellEffectEnhanced");
				nbtTag.removeTag("WITCSpellEffectEnhanced");
			}
			NBTTagCompound nbtPerm = Infusion.getNBT(player);
			if(effect != null) {
				if(!player.capabilities.isCreativeMode && (nbtPerm == null || !nbtPerm.hasKey("witcheryInfusionID") || !nbtPerm.hasKey("witcheryInfusionCharges"))) {
					ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.infusionrequired");
					SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
				}
				else if(effect.hasValidInfusion(player, nbtPerm.getInteger("witcheryInfusionID"))) {
					if(effect.hasValidKnowledge(player, nbtPerm)) {
						long ticksRemaining = effect.cooldownRemaining(player, nbtPerm);
						if(ticksRemaining > 0L && !player.capabilities.isCreativeMode) {
							ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.effectoncooldown", Long.valueOf(TimeUtil.ticksToSecs(ticksRemaining))
																																   .toString());
							SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
						}
						else {
							if(level > 1) {
								int newLevel = 1;
								if(player.isPotionActive(Witchery.Potions.WORSHIP)) {
									PotionEffect potion = player.getActivePotionEffect(Witchery.Potions.WORSHIP);
									if(level <= potion.getAmplifier() + 2) {
										newLevel = level;
									}
								}

								level = newLevel;
							}

							if(!player.capabilities.isCreativeMode && nbtPerm.getInteger("witcheryInfusionCharges") < effect.getChargeCost(world, player, level) * discount) {
								ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.nocharges");
								SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
							}
							else {
								effect.perform(world, player, level);
								if(!player.capabilities.isCreativeMode) {
									Infusion.setCurrentEnergy(player, (int)(nbtPerm.getInteger("witcheryInfusionCharges") - effect.getChargeCost(world, player, level) * discount));
								}
							}
						}
					}
					else {
						ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.unknowneffect");
						SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
					}
				}
				else {
					ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.infernalrequired");
					SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
				}
			}
			else {
				ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.unknownsymbol");
				SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
			}
		}
	}

	public List getEntities(double radius, Class target) {
		List entities = player.worldObj.getEntitiesWithinAABB(target, player.boundingBox.expand(radius, radius, radius));
		return entities;
	}
}
