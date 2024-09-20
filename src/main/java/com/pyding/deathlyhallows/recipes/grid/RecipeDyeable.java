package com.pyding.deathlyhallows.recipes.grid;

import com.pyding.deathlyhallows.utils.IItemDyeable;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecipeDyeable implements IRecipe {

	private final List<Integer> dyeODs = Stream.of(
			"dye", 
			"dyeBlack",
			"dyeRed",
			"dyeGreen",
			"dyeBrown",
			"dyeBlue",
			"dyePurple",
			"dyeCyan",
			"dyeLightGray",
			"dyeGray",
			"dyePink",
			"dyeLime",
			"dyeYellow",
			"dyeLightBlue",
			"dyeMagenta",
			"dyeOrange",
			"dyeWhite"
	).map(OreDictionary::getOreID).collect(Collectors.toList());

	private byte getSheepColourFromDye(ItemStack stack) {
		for(int id: OreDictionary.getOreIDs(stack)) {
			for(int i = 1; i < dyeODs.size(); ++i) {
				if(id == dyeODs.get(i)) {
					return (byte)(dyeODs.size() - 1 - i);
				}
			}
		}
		return 0;
	}


	private boolean isDye(ItemStack stack) {
		for(int id: OreDictionary.getOreIDs(stack)) {
			if(dyeODs.contains(id)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World w) {
		boolean foundDyeable = false;
		boolean foundDye = false;

		for(int slot = 0; slot < inv.getSizeInventory(); ++slot) {
			ItemStack temp = inv.getStackInSlot(slot);
			if(temp != null) {
				if(temp.getItem() instanceof IItemDyeable && !foundDyeable) {
					foundDyeable = true;
				}
				else if(isDye(temp)) {
					foundDye = true;
				}
				else {
					return false;
				}
			}
		}
		return foundDyeable && foundDye;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack stack = null;
		float[] colour = new float[3];
		float i = 0, j = 0;
		boolean hasDye = false;
		int currentColour;
		for(int slot = 0; slot < inv.getSizeInventory(); ++slot) {
			ItemStack temp = inv.getStackInSlot(slot);
			if(temp != null) {
				if(temp.getItem() instanceof IItemDyeable) {
					stack = temp.copy();
					stack.stackSize = 1;
					if(!((IItemDyeable)temp.getItem()).isDyed(temp)) {
						continue;
					}
					currentColour = ((IItemDyeable)temp.getItem()).getDyedColor(temp);
					float r = (currentColour >> 16 & 255) / 255.0F;
					float g = (currentColour >> 8 & 255) / 255.0F;
					float b = (currentColour & 255) / 255.0F;
					i += Math.max(r, Math.max(g, b));
					colour[0] += r;
					colour[1] += g;
					colour[2] += b;
					j++;
				}
				else if(isDye(temp)) {
					hasDye = true;
					float[] dyeColour = EntitySheep.fleeceColorTable[getSheepColourFromDye(temp)];
					i += Math.round(Math.max(dyeColour[0], Math.max(dyeColour[1], dyeColour[2])));
					colour[0] += dyeColour[0];
					colour[1] += dyeColour[1];
					colour[2] += dyeColour[2];
					j++;
				}
				else {
					return null;
				}
			}
		}
		if(stack == null || !hasDye) {
			return null;
		}

		float f1 = i / j;
		float f2 = f1 / Math.max(colour[0], Math.max(colour[1], colour[2]));
		for(int k = 0; k < colour.length; ++k) {
			colour[k] = colour[k] * f2 * 255.0F;
		}
		currentColour = (((Math.round(colour[0]) << 8) + Math.round(colour[1])) << 8) + Math.round(colour[2]);
		((IItemDyeable)stack.getItem()).setDyedColor(stack, currentColour);

		return stack;
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}

}
