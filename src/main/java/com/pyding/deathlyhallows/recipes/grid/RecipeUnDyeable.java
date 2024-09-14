package com.pyding.deathlyhallows.recipes.grid;

import com.pyding.deathlyhallows.utils.IItemDyeable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeUnDyeable implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		boolean foundBleach = false;
		ItemStack bleach = null;
		int index = -1;
		for(int slot = 0; slot < inv.getSizeInventory(); ++slot) {
			ItemStack stack = inv.getStackInSlot(slot);
			if(stack == null) {
				continue;
			}
			if(stack.getItem() instanceof IItemDyeable) {
				IItemDyeable dyeable = (IItemDyeable)stack.getItem();
				if(!dyeable.isDyed(stack)) {
					continue;
				}
				bleach = dyeable.getBleach(stack);
				if(bleach == null) {
					return false;
				}
				if(bleach == stack) {
					foundBleach = true;
				}
				index = slot;
				break;
			}
		}
		if(index == -1) {
			return false;
		}		
		for(int slot = 0; slot < inv.getSizeInventory(); ++slot) {
			ItemStack stack = inv.getStackInSlot(slot);
			if(stack == null || slot == index) {
				continue;
			}
			if(foundBleach || !isBleach(stack, bleach)) {
				return false;	
			}
			foundBleach = true;
		}
		return foundBleach;
	}

	private boolean isBleach(ItemStack stack, ItemStack bleach) {
		if(OreDictionary.itemMatches(stack, bleach, false)) {
			return true;
		}
		if(!FluidContainerRegistry.isFilledContainer(bleach)) {
			return false;
		}
		FluidStack bleachFluid = FluidContainerRegistry.getFluidForFilledItem(bleach);
		if(bleachFluid == null) {
			return false;
		}
		FluidStack stackFluid = FluidContainerRegistry.getFluidForFilledItem(stack);
		if(stackFluid == null) {
			return false;
		}
		return bleachFluid.getFluid() == stackFluid.getFluid() && stackFluid.amount > 144;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack stack;

		for(int slot = 0; slot < inv.getSizeInventory(); ++slot) {
			ItemStack temp = inv.getStackInSlot(slot);
			if(temp != null) {
				if(temp.getItem() instanceof IItemDyeable) {
					stack = temp.copy();
					stack.stackSize = 1;
					((IItemDyeable)stack.getItem()).removeColor(stack);
					return stack;
				}
			}
		}
		return null;
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
