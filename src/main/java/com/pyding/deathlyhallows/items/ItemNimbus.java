package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.InfusedBrewEffect;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.entities.EntityNimbus;
import com.pyding.deathlyhallows.utils.DHKeys;
import com.pyding.deathlyhallows.utils.IItemDyeable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemNimbus extends ItemBase implements IItemDyeable {

	private IIcon overlay;
	
	public ItemNimbus() {
		super("nimbus", 1);
	}

	@Override
	public int getRenderPasses(int meta) {
		return 2;
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public void registerIcons(IIconRegister ir) {
		super.registerIcons(ir);
		overlay = ir.registerIcon(DeathlyHallows.MODID + ":" + iconString + "_overlay");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamage(int meta) {
		return itemIcon;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamageForRenderPass(int meta, int pass) {
		return pass == 1 ? overlay : itemIcon;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		if(pass == 1) {
			return getColor(stack);
		}
		return 0xFFFFFF;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		if(p.ridingEntity != null) {
			return super.onItemRightClick(stack, world, p);
		}
		if(!canUse(p)) {
			return super.onItemRightClick(stack, world, p);
		}
		addNumbusCooldown(p, 5);
		setNumbusDuration(p, 160);
		if(world.isRemote) {
			return super.onItemRightClick(stack, world, p);
		}
		EntityNimbus nimbus = new EntityNimbus(world);
		nimbus.setPosition(p.posX, p.posY + 0.255D, p.posZ);
		nimbus.setBrushColor(getColor(stack));
		world.spawnEntityInWorld(nimbus);
		nimbus.interactFirst(p);
		return super.onItemRightClick(stack, world, p);
	}
	
	public static boolean canUse(Entity p) {
		NBTTagCompound tag = p.getEntityData();
		if(tag.getLong("NimbusDuration") > System.currentTimeMillis()) {
			return true;
		}
		return tag.getLong("NimbusCooldown") < System.currentTimeMillis();
	}

	public static void addNumbusCooldown(Entity p, int cooldownSeconds) {
		cooldownSeconds *= 1000;
		NBTTagCompound tag = p.getEntityData();
		tag.setLong("NimbusCooldown", Math.max(System.currentTimeMillis() + cooldownSeconds, tag.getLong("NimbusCooldown") + cooldownSeconds));
	}

	public static void setNumbusDuration(Entity p, int durationSeconds) {
		NBTTagCompound tag = p.getEntityData();
		if(tag.getLong("NimbusDuration") < System.currentTimeMillis()) {
			tag.setLong("NimbusDuration", MathHelper.ceiling_float_int( durationSeconds * 1000 * modifier(p)) + System.currentTimeMillis());
		}
	}
	
	public static float modifier(Entity e) {
		if(!(e instanceof EntityPlayer)) {
			return 1F;
		}
		EntityPlayer p = (EntityPlayer)e;
		float modifier = 1;
		if(Familiar.hasActiveBroomMasteryFamiliar(p)) {
			modifier += 0.7f;
		}
		if(InfusedBrewEffect.Soaring.isActive(p)) {
			modifier += 0.6f;
		}
		return modifier;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		long cd = 0;
		long duration = 0;
		NBTTagCompound tag = p.getEntityData();
		if(tag != null) {
			cd = tag.getLong("NimbusCooldown");
			duration = tag.getLong("NimbusDuration");
		}
		if((duration - System.currentTimeMillis()) > 0) {
			l.add(StatCollector.translateToLocalFormatted("dh.desc.broom1", (duration - System.currentTimeMillis()) / 1000));
		}
		else if(cd > System.currentTimeMillis()) {
			l.add(StatCollector.translateToLocalFormatted("dh.desc.broom0", (cd - System.currentTimeMillis()) / 1000));
		}
		l.add(StatCollector.translateToLocalFormatted("dh.desc.broom2", Keyboard.getKeyName(DHKeys.BROOM.getKeyCode())));
		l.add(StatCollector.translateToLocal("dh.desc.broom3"));
	}

	@Override
	public boolean hasEffect(final ItemStack stack, final int pass) {
		return true;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {
		return 1;
	}
	
	@Override
	public int getDefaultColor(ItemStack stack) {
		return 0x704020; // default color
	}
	
}
