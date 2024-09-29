package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockPlacedItem;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.pyding.deathlyhallows.events.DHPlayerRenderEvents;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketElderWandLastSpell;
import com.pyding.deathlyhallows.network.packets.PacketElderWandStrokes;
import com.pyding.deathlyhallows.utils.DHConfig;
import com.pyding.deathlyhallows.utils.DHUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
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
	public static final String
			LAST_SPELL_TAG = "lastSpells",
			LAST_SPELL_BIND_TAG = "lastSpell",
			START_X_TAG = "startX",
			START_Y_TAG = "startY",
			STROKES_TAG = "Strokes",
			INDEX_TAG = "index",
			LIST_COUNTER_TAG = "listCounter",
			MODE_TAG = "castingMode",
			START_PITCH_TAG = "startPitch",
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
		if(p.isSneaking()) {
			EnumCastingMode mode = getMode(stack).next();
			setMode(stack, mode);
			if(world.isRemote) {
				setTooltip(mode);
			}
			setBinding(p, false);
			setIndex(stack.getTagCompound(), -1);
			return stack;
		}
		switch(getMode(stack)) {
			case LIST: {
				p.getEntityData().setInteger("WITCSpellEffectID", getSpells().get(getListCounter(stack.getTagCompound())).getEffectID());
				Witchery.Items.MYSTIC_BRANCH.onPlayerStoppedUsing(stack, world, p, 0);
				return stack;
			}
			case BIND: {
				if(isBinding(p)) {
					setMode(stack, EnumCastingMode.STROKE);
					if(world.isRemote) {
						setTooltip("dh.desc.wandMode.bindStart");
					}
					p.setItemInUse(stack, getMaxItemUseDuration(stack));
					return stack;
				}
				if(world.isRemote) {
					setXY(p);
				}
				p.setItemInUse(stack, getMaxItemUseDuration(stack));
				return stack;
			}
			case STROKE: {
				return Witchery.Items.MYSTIC_BRANCH.onItemRightClick(stack, world, p);
			}
		}
		return stack;
	}

	public void onUsingTick(ItemStack stack, EntityPlayer p, int countdown) {
		if(!p.worldObj.isRemote) {
			return;
		}
		NBTTagCompound tag = p.getEntityData();
		switch(getMode(stack)) {
			case LIST: {
				return;
			}
			case BIND: {
				if(isBinding(p)) {
					return;
				}
				if(!tag.hasKey(START_X_TAG)) {
					setXY(p);
				}
				if(!stack.hasTagCompound()) {
					stack.setTagCompound(new NBTTagCompound());
				}
				if(stack.getTagCompound().hasKey(INDEX_TAG)) {
					if(Minecraft.getMinecraft().gameSettings.keyBindAttack.getIsKeyPressed()) {
						int index = getIndex(stack.getTagCompound());
						removeLastSpell(stack, index);
						DHPacketProcessor.sendToServer(new PacketElderWandLastSpell(index, true));
						setIndex(stack.getTagCompound(), -1);
						resetXY(p);
					}
					return;
				}
				NBTTagList list = getLastSpells(stack);
				int size = list.tagCount();
				int index = getIdFromPosition(size, getXY(p));
				if(index == -1) {
					return;
				}
				DHPacketProcessor.sendToServer(new PacketElderWandLastSpell(index));
				setIndex(stack.getTagCompound(), index);
				if(size < DHConfig.elderWandMaxSpells && index == size) {
					setBinding(p, true);
				}
				return;
			}
			default:
				Witchery.Items.MYSTIC_BRANCH.onUsingTick(stack, p, countdown);
		}
	}

	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer p, int countdown) {
		NBTTagCompound tag = p.getEntityData();
		if(world.isRemote) {
			tag.removeTag(START_X_TAG);
			tag.removeTag(START_Y_TAG);
		}
		switch(getMode(stack)) {
			case LIST: {
				return;
			}
			case BIND: {
				int size = getLastSpells(stack).tagCount();
				if(world.isRemote && isBinding(p) && getIndex(stack.getTagCompound()) != size && size < DHConfig.elderWandMaxSpells) {
					setBinding(p, false);
					DHPacketProcessor.sendToServer(new PacketElderWandLastSpell(-1));
					setIndex(stack.getTagCompound(), -1);
					return;
				}
				setIndex(stack.getTagCompound(), -1);
				if(!world.isRemote && tag.hasKey("WITCSpellEffectID")) {
					Witchery.Items.MYSTIC_BRANCH.onPlayerStoppedUsing(stack, world, p, countdown);
				}
				return;
			}
			case STROKE: {
				if(isBinding(p)) {
					setBinding(p, false);
					setMode(stack, EnumCastingMode.BIND);
					if(world.isRemote) {

						byte[] strokes = tag.getByteArray(STROKES_TAG);
						if(strokes.length < 1 || EffectRegistry.instance().getEffect(strokes) == null) {
							setTooltip("dh.desc.wandMode.bindFail");
							return;
						}
						setTooltip("dh.desc.wandMode.bindFinish");
						DHPacketProcessor.sendToServer(new PacketElderWandStrokes(strokes));
						addLastSpell(stack, strokes);
						tag.removeTag(STROKES_TAG);
						tag.removeTag(START_YAW_TAG);
						tag.removeTag(START_PITCH_TAG);
					}
					return;
				}
				Witchery.Items.MYSTIC_BRANCH.onPlayerStoppedUsing(stack, world, p, countdown);
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {
		return super.getItemStackDisplayName(p_77653_1_);
	}

	public enum EnumCastingMode {

		STROKE,
		BIND,
		LIST;

		private EnumCastingMode next() {
			EnumCastingMode[] values = values();
			return values[(ordinal() + 1) % values.length];
		}

	}

	private static void setTooltip(String toolitp) {
		DHPlayerRenderEvents.setTooltip(StatCollector.translateToLocal(toolitp));
	}

	private static void setTooltip(EnumCastingMode mode) {
		setTooltip("dh.desc.wandMode." + mode.name().toLowerCase());
	}

	public static void setMode(ItemStack stack, EnumCastingMode mode) {
		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		tag.setByte(MODE_TAG, (byte)mode.ordinal());
	}

	public static EnumCastingMode getMode(ItemStack stack) {
		NBTTagCompound tag = stack.getTagCompound();
		return tag == null || !tag.hasKey(MODE_TAG) ? EnumCastingMode.STROKE : EnumCastingMode.values()[tag.getByte(MODE_TAG)];
	}

	public static List<SymbolEffect> getSpells() {
		return EffectRegistry.instance().getEffects();
	}

	public static void setListCounter(NBTTagCompound tag, int index) {
		if(tag == null) {
			return;
		}
		tag.setInteger(LIST_COUNTER_TAG, (byte)index);
	}

	public static int getListCounter(NBTTagCompound tag) {
		return tag == null ? 0 : tag.getInteger(LIST_COUNTER_TAG);
	}

	private static void setIndex(NBTTagCompound tag, int index) {
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
		tag.removeTag(START_PITCH_TAG);
	}

	public static void resetXY(EntityPlayer p) {
		NBTTagCompound tag = p.getEntityData();
		tag.removeTag(START_X_TAG);
		tag.removeTag(START_Y_TAG);
		tag.setByteArray(STROKES_TAG, new byte[0]);
		tag.setFloat(START_PITCH_TAG, p.rotationPitch);
		tag.setFloat(START_YAW_TAG, p.rotationYawHead);
	}

	private static int getIdFromPosition(int length, float[] xy) {
		float x = xy[0], y = xy[1];
		if((x * x + y * y) < innerRadius * innerRadius) {
			return -1;
		}
		double wire = (Math.atan2(-x, y) / (2D * Math.PI) + 1.5D + (0.5D / (length + 1D))) % 1D;
		return MathHelper.floor_double(wire * (length + (length < DHConfig.elderWandMaxSpells ? 1 : 0)));
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

	public static void removeLastSpell(ItemStack wand, int index) {
		NBTTagList list = getLastSpells(wand);
		if(list.tagCount() > index) {
			list.removeTag(index);
		}
	}

}
