package com.pyding.deathlyhallows.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemDeadlyPrism extends ItemBase {
	public int damage;
	public float damageAmount;

	public ItemDeadlyPrism() {
		super("deadlyPrism", 1);
	}

	public String damageSource() {
		switch(damage) {
			case 0: {
				return "Void";
			}
			case 1: {
				return "Magic";
			}
			case 2: {
				return "Generic";
			}
			case 3: {
				return "Bypass Armor";
			}
			case 4: {
				return "Fire";
			}
			case 5: {
				return "Explosion";
			}
			case 6: {
				return "Allowed in Creative Mode";
			}
			case 7: {
				return "Projectile";
			}
			case 8: {
				return "Absolute";
			}
			case 9: {
				return "Anvil";
			}
			case 10: {
				return "Wither";
			}
			case 11: {
				return "Lava";
			}
			case 12: {
				return "Starve";
			}
			case 13: {
				return "Neco Arc";
			}
		}
		return null;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		if(p.worldObj.isRemote) {
			return super.onItemRightClick(stack, world, p);
		}
		try {
			switch(damage) {
				case 0: {
					p.attackEntityFrom(DamageSource.outOfWorld, damageAmount);
				}
				case 1: {
					p.attackEntityFrom(DamageSource.magic, damageAmount);
				}
				case 2: {
					p.attackEntityFrom(DamageSource.generic, damageAmount);
				}
				case 3: {
					p.attackEntityFrom(new DamageSource("bypass").setDamageBypassesArmor(), damageAmount);
				}
				case 4: {
					p.attackEntityFrom(new DamageSource("fire").setFireDamage(), damageAmount);
				}
				case 5: {
					p.attackEntityFrom(new DamageSource("explosion").setExplosion(), damageAmount);
				}
				case 6: {
					p.attackEntityFrom(new DamageSource("creative").setDamageAllowedInCreativeMode(), damageAmount);
				}
				case 7: {
					p.attackEntityFrom(new DamageSource("projectile").setProjectile(), damageAmount);
				}
				case 8: {
					p.attackEntityFrom(new DamageSource("absolute").setDamageIsAbsolute(), damageAmount);
				}
				case 9: {
					p.attackEntityFrom(DamageSource.anvil, damageAmount);
				}
				case 10: {
					p.attackEntityFrom(DamageSource.wither, damageAmount);
				}
				case 11: {
					p.attackEntityFrom(DamageSource.lava, damageAmount);
				}
				case 12: {
					p.attackEntityFrom(DamageSource.starve, damageAmount);
				}
			}
		}
		catch(Exception ignored) {

		}
		return super.onItemRightClick(stack, world, p);
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		if(!(entityLiving instanceof EntityPlayer) || entityLiving.worldObj.isRemote) {
			return super.onEntitySwing(entityLiving, stack);
		}
		EntityPlayer player = (EntityPlayer)entityLiving;
		if(!player.isSneaking()) {
			if(damage == 0 || damage < 12) {
				damage++;
			}
			else {
				damage = 0;
			}
			player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("dh.chat.deadlyPrism1", damageSource())));
			return super.onEntitySwing(entityLiving, stack);
		}
		player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("dh.chat.deadlyPrism2")));
		player.getEntityData().setBoolean("DeadlyPrism", true);
		return super.onEntitySwing(entityLiving, stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		l.add(StatCollector.translateToLocal("dh.desc.prism"));
	}
	
}
