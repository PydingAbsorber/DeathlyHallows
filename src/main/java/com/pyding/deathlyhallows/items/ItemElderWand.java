package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockPlacedItem;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketElderWandLastSpell;
import com.pyding.deathlyhallows.network.packets.PacketElderWandStrokes;
import com.pyding.deathlyhallows.utils.DHUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;

public class ItemElderWand extends ItemBase {
	public static final int MAX_SPELLS = 10;
	public static final String
			LAST_SPELL_TAG = "lastSpells",
			LAST_SPELL_BIND_TAG = "lastSpell",
			START_X_TAG = "startX",
			START_Y_TAG = "startY",
			STROKES_TAG = "Strokes",
			INDEX_TAG = "index",
			START_PTICH_TAG = "startPitch", 
			START_YAW_TAG = "startYaw";
	private static final float
			outerRadius = 22F,
			innerRadius = 14F;


	public ItemElderWand() {
		super("elderWand", 1);
		setFull3D();
	}

	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.rare;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		l.add(StatCollector.translateToLocal("dh.desc.wand1"));
		l.add(StatCollector.translateToLocal("dh.desc.wand2"));
		String owner = stack.hasTagCompound() ? stack.getTagCompound().getString("dhowner") : "";
		if(owner.equals("")) {
			owner = StatCollector.translateToLocal("dh.desc.defaultOwner");
		}
		l.add(StatCollector.translateToLocalFormatted("dh.desc.owner", owner));
		l.add(StatCollector.translateToLocal("dh.desc.hallow"));
	}

	public boolean hasEffect(ItemStack stack, int pass) {
		return true;
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer p, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(world.getBlock(x, y, z) == Witchery.Blocks.ALTAR && side == 1 && world.getBlock(x, y + 1, z) == Blocks.air) {
			BlockPlacedItem.placeItemInWorld(stack, p, world, x, y + 1, z);
			p.inventory.setInventorySlotContents(p.inventory.currentItem, null);
			return !world.isRemote;
		}
		else {
			return super.onItemUse(stack, p, world, x, y, z, side, hitX, hitY, hitZ);
		}
	}

	public EnumAction getItemUseAction(ItemStack stack) {
		return Witchery.Items.MYSTIC_BRANCH.getItemUseAction(stack);
	}

	public int getMaxItemUseDuration(ItemStack stack) {
		return Witchery.Items.MYSTIC_BRANCH.getMaxItemUseDuration(stack);
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		if(!p.isSneaking()) {
			if(world.isRemote) {
				setXY(p);
			}
			p.setItemInUse(stack, getMaxItemUseDuration(stack));
			return stack;
		}
		return Witchery.Items.MYSTIC_BRANCH.onItemRightClick(stack, world, p);
	}

	public void onUsingTick(ItemStack stack, EntityPlayer p, int countdown) {
		if(!p.worldObj.isRemote) {
			return;
		}
		NBTTagCompound tag = p.getEntityData();
		if(p.isSneaking() == (tag.hasKey(INDEX_TAG) || tag.hasKey(START_X_TAG))) {
			if(p.isSneaking()) {
				setIndex(-1, stack.getTagCompound());
				DHPacketProcessor.sendToServer(new PacketElderWandLastSpell(-1));
				resetXY(p);
			}
			else {
				setXY(p);
			}
			return;
		}

		if(!p.isSneaking()) {
			if(!tag.hasKey(START_X_TAG)) {
				return;
			}
			if(!stack.hasTagCompound()) {
				stack.setTagCompound(new NBTTagCompound());
			}
			if(stack.getTagCompound().hasKey(INDEX_TAG)) {
				return;
			}
			NBTTagList list = getLastSpells(stack);
			int size = list.tagCount();
			int index = getIdFromPosition(size, getXY(p));
			if(index == -1) {
				return;
			}
			DHPacketProcessor.sendToServer(new PacketElderWandLastSpell(index));
			setIndex(index, stack.getTagCompound());
			if(size < MAX_SPELLS && index == size) {
				setBinding(p, true);
			}
			return;
		}
		Witchery.Items.MYSTIC_BRANCH.onUsingTick(stack, p, countdown);
	}
	
	public static void processIndex(ItemStack stack, EntityPlayer p, int index) {
		
	}

	private static void setIndex(int index, NBTTagCompound tag) {
		if(tag == null) {
			return;
		}
		if(index == -1) {
			tag.removeTag(INDEX_TAG);
			return;
		}
		tag.setByte(INDEX_TAG, (byte)index);
	}

	public static byte getIndex(NBTTagCompound tag) {
		return tag == null || !tag.hasKey(INDEX_TAG) ? -1 : tag.getByte(INDEX_TAG);
	}

	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer p, int countdown) {
		NBTTagCompound tag = p.getEntityData();
		if(world.isRemote) {
			tag.removeTag(START_X_TAG);
			tag.removeTag(START_Y_TAG);

		}
		if(!isBinding(p)) {
			setIndex(-1, stack.getTagCompound());
			if(tag.hasKey("WITCSpellEffectID")) {
				Witchery.Items.MYSTIC_BRANCH.onPlayerStoppedUsing(stack, world, p, countdown);
			}
			return;
		}
		if(world.isRemote && getIndex(stack.getTagCompound()) != 0) {
			setBinding(p, false);
			DHPacketProcessor.sendToServer(new PacketElderWandLastSpell(-1));
		}
		setIndex(-1, stack.getTagCompound());
		if(!world.isRemote || !tag.hasKey(STROKES_TAG)) {
			return;
		}
		tag.removeTag(START_YAW_TAG);
		tag.removeTag(START_PTICH_TAG);
		byte[] strokes = tag.getByteArray(STROKES_TAG);
		if(strokes.length < 1 || EffectRegistry.instance().getEffect(strokes) == null) {
			return;
		}
		DHPacketProcessor.sendToServer(new PacketElderWandStrokes(strokes));
		addLastSpell(stack, strokes);
		setBinding(p, false);
		tag.removeTag(STROKES_TAG);
	}

	public static NBTTagList getLastSpells(ItemStack stack) {
		return stack.getTagCompound().getTagList(LAST_SPELL_TAG, Constants.NBT.TAG_BYTE_ARRAY);
	}

	public static void castLastSpell(EntityPlayer p, ItemStack stack, int index) {
		NBTTagCompound tag = p.getEntityData();
		byte[] strokes = DHUtils.getBytesFromTagList(getLastSpells(stack), index);
		EffectRegistry r = EffectRegistry.instance();
		int effect = r.getEffect(strokes).getEffectID(), level = r.getLevel(strokes);
		tag.setInteger("WITCSpellEffectID", effect);
		tag.setInteger("WITCSpellEffectEnhanced", level);
	}

	public static void setBinding(EntityPlayer p, boolean isBinding) {
		NBTTagCompound tag = p.getEntityData();
		if(isBinding) {
			tag.setBoolean(LAST_SPELL_BIND_TAG, true);
			return;
		}
		tag.removeTag(LAST_SPELL_BIND_TAG);
		tag.removeTag("WITCSpellEffectID");
		tag.removeTag("WITCSpellEffectEnhanced");
	}

	public static boolean isBinding(EntityPlayer p) {
		return p.getEntityData().hasKey(LAST_SPELL_BIND_TAG);
	}

	public static float[] getXY(EntityPlayer p) {
		NBTTagCompound tag = p.getEntityData();
		float
				fx = (p.rotationYaw - tag.getFloat(START_X_TAG)),
				fy = (p.rotationPitch - tag.getFloat(START_Y_TAG)),
				f = (fx * fx + fy * fy) / (outerRadius * outerRadius);
		if(f > 1) {
			f = MathHelper.sqrt_float(f);
			fx /= f;
			fy /= f;
		}
		return new float[]{fx, fy};
	}

	public static void setXY(EntityPlayer p) {
		NBTTagCompound tag = p.getEntityData();
		tag.setFloat("startX", p.rotationYaw);
		tag.setFloat("startY", p.rotationPitch);
		tag.removeTag(STROKES_TAG);
		tag.removeTag(START_YAW_TAG);
		tag.removeTag(START_PTICH_TAG);
	}

	public static void resetXY(EntityPlayer p){
		NBTTagCompound tag = p.getEntityData();
		tag.removeTag(START_X_TAG);
		tag.removeTag(START_Y_TAG);
		tag.setByteArray(STROKES_TAG, new byte[0]);
		tag.setFloat(START_PTICH_TAG, p.rotationPitch);
		tag.setFloat(START_YAW_TAG, p.rotationYawHead);
	}

	private static int getIdFromPosition(int length, float[] xy) {
		float x = xy[0], y = xy[1];
		if((x * x + y * y) < innerRadius * innerRadius) {
			return -1;
		}
		double wire = (Math.atan2(-x, y) / (2D * Math.PI) + 1.5D + (0.5D / (length + 1D))) % 1D;
		return MathHelper.floor_double(wire * (length + 1));
	}

	public static void addLastSpell(ItemStack wand, byte[] strokes) {
		NBTTagCompound tag = wand.getTagCompound();
		if(tag == null) {
			tag = new NBTTagCompound();
			wand.setTagCompound(tag);
		}
		if(!tag.hasKey(LAST_SPELL_TAG)) {
			tag.setTag(LAST_SPELL_TAG, new NBTTagList());
		}
		getLastSpells(wand).appendTag(new NBTTagByteArray(strokes));
	}

}
