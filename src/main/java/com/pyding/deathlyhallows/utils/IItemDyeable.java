package com.pyding.deathlyhallows.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public interface IItemDyeable {

	default boolean isDyed(ItemStack stack) {
		if(!stack.hasTagCompound()) {
			return false;
		}
		NBTTagCompound tag = stack.getTagCompound();
		return tag.hasKey("display", Constants.NBT.TAG_COMPOUND) && tag.getCompoundTag("display").hasKey("color", Constants.NBT.TAG_INT);
	}


	default void setDyedColor(ItemStack stack, int color) {
		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}

		NBTTagCompound sub = tag.getCompoundTag("display");
		if(!tag.hasKey("display", Constants.NBT.TAG_COMPOUND)) {
			sub = new NBTTagCompound();
			tag.setTag("display", sub);
		}
		sub.setInteger("color", color);
	}

	default int getDefaultColor(ItemStack stack) {
		return 0xFFFFFF;
	}

	default int getDyedColor(ItemStack stack) {
		if(stack.hasTagCompound()) {
			NBTTagCompound sub = stack.getTagCompound().getCompoundTag("display");
			if(sub.hasKey("color", Constants.NBT.TAG_INT)) {
				return sub.getInteger("color");
			}
		}
		return getDefaultColor(stack);
	}

	default void removeDyedColor(ItemStack stack) {
		if(!isDyed(stack)) {
			return;
		}
		NBTTagCompound sub = stack.getTagCompound().getCompoundTag("display");
		sub.removeTag("color");
	}
	
	// return null if item can't be washed
	// return stack itself if no bleach is needed
	@Nullable
	default ItemStack getBleach(ItemStack stack) {
		return stack;
	}

	default boolean canWashInCauldron(ItemStack stack) {
		return true;
	}
	
}
