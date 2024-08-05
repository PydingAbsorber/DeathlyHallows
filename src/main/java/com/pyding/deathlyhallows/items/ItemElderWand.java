package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockPlacedItem;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.pyding.deathlyhallows.network.DHPacketProcessor;
import com.pyding.deathlyhallows.network.packets.PacketElderWandStrokes;
import com.pyding.deathlyhallows.utils.DHKeys;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemElderWand extends ItemBase {

	public static final String LAST_SPELL_TAG = "lastSpell";

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
		// TODO is there a point in wand1 and wand2?		
		l.add(StatCollector.translateToLocal("dh.desc.wand1"));
		l.add(StatCollector.translateToLocal("dh.desc.wand2"));
		SymbolEffect lastSpell = getLastSpell(stack);
		if(lastSpell != null) {
			l.add(StatCollector.translateToLocalFormatted("dh.desc.wand3", lastSpell.getLocalizedName()));
			l.add(StatCollector.translateToLocalFormatted("dh.desc.wand4", Keyboard.getKeyName(DHKeys.WAND.getKeyCode())));
		}
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
		return Witchery.Items.MYSTIC_BRANCH.onItemRightClick(stack, world, p);
	}

	public void onUsingTick(ItemStack stack, EntityPlayer p, int countdown) {
		Witchery.Items.MYSTIC_BRANCH.onUsingTick(stack, p, countdown);
	}

	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer p, int countdown) {
		if(world.isRemote) {
			byte[] strokes = p.getEntityData().getByteArray("Strokes");
			if(EffectRegistry.instance().getEffect(strokes) != null) {
				setLastStrokes(stack, strokes);
				DHPacketProcessor.sendToServer(new PacketElderWandStrokes(strokes));
			}
		}
		Witchery.Items.MYSTIC_BRANCH.onPlayerStoppedUsing(stack, world, p, countdown);
	}
	
	public static void setLastStrokes(ItemStack wand, byte[] strokes) {
		if(!wand.hasTagCompound()) {
			wand.setTagCompound(new NBTTagCompound());
		}
		wand.getTagCompound().setByteArray(LAST_SPELL_TAG, strokes);
	}
	
	public static SymbolEffect getLastSpell(ItemStack wand) {
		if(!wand.hasTagCompound()) {
			return null;
		}
		byte[] strokes = wand.getTagCompound().getByteArray(LAST_SPELL_TAG);
		return EffectRegistry.instance().getEffect(strokes);
	}
	
}
