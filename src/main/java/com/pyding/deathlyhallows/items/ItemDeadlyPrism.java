package com.pyding.deathlyhallows.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemDeadlyPrism extends ItemBase {
	private static final int SOURCES_COUNT = 14;
	private static final String 
			DAMAGE_TAG = "PrismDamage",
			SOURCE_TAG = "PrismDamageSource";
	public ItemDeadlyPrism() {
		super("deadlyPrism", 1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		if(p.worldObj.isRemote) {
			return stack;
		}
		int meta = stack.getItemDamage();
		DamageSource ds;
		if(meta == 13) {
			ds = new DamageSource(getDamageSourceFromTag(stack));
		}
		else {
			ds = getDamageSourceFromMeta(p, stack.getItemDamage());
		}
		try {
			p.attackEntityFrom(ds, getPrismDamage(stack));
		}
		catch(Exception ignored) {

		}
		return stack;
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase e, ItemStack stack) {
		if(!(e instanceof EntityPlayer) || e.worldObj.isRemote) {
			return false;
		}
		EntityPlayer p = (EntityPlayer)e;
		if(!p.isSneaking()) {
			int meta = stack.getItemDamage();
			++meta;
			meta %= SOURCES_COUNT;
			stack.setItemDamage(meta);
			p.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("dh.chat.deadlyPrism1", getDamageSourceString(meta))));
			return false;
		}
		p.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("dh.chat.deadlyPrism2")));
		p.getEntityData().setBoolean("DeadlyPrism", true);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		l.add(StatCollector.translateToLocal("dh.desc.prism"));
	}

	private static String getDamageSourceFromTag(ItemStack stack) {
		if(!stack.hasTagCompound()) {
			return "Custom";
		}
		return stack.getTagCompound().getString(SOURCE_TAG);
	}

	public static void getDamageSourceFromTag(ItemStack stack, String damageSource) {
		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		tag.setString(SOURCE_TAG, damageSource);
	}
	
	private static float getPrismDamage(ItemStack stack) {
		if(!stack.hasTagCompound()) {
			return 0F;
		}
		return stack.getTagCompound().getFloat(DAMAGE_TAG);
	}

	public static void setPrismDamage(ItemStack stack, float damage) {
		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		tag.setFloat(DAMAGE_TAG, damage);
	}

	private static String getDamageSourceString(int meta) {
		switch(meta) {
			default: {
				return "Generic";
			}
			case 1: {
				return "Magic";
			}
			case 2: {
				return "Void";
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
				return "Custom";
			}
		}
	}
	
	private static DamageSource getDamageSourceFromMeta(EntityPlayer p, int itemDamage) {
		switch(itemDamage) {
			default: {
				return DamageSource.generic;
			}
			case 1: {
				return DamageSource.magic;
			}
			case 2: {
				return DamageSource.outOfWorld;
			}
			case 3: {
				return DamageSource.causePlayerDamage(p).setDamageBypassesArmor();
			}
			case 4: {
				return DamageSource.inFire;
			}
			case 5: {
				return DamageSource.setExplosionSource(null);
			}
			case 6: {
				return DamageSource.causePlayerDamage(p).setDamageAllowedInCreativeMode();
			}
			case 7: {
				return DamageSource.causePlayerDamage(p).setProjectile();
			}
			case 8: {
				return DamageSource.causePlayerDamage(p).setDamageIsAbsolute();
			}
			case 9: {
				return DamageSource.anvil;
			}
			case 10: {
				return DamageSource.wither;
			}
			case 11: {
				return DamageSource.lava;
			}
			case 12: {
				return DamageSource.starve;
			}
		}
	}
	
}
