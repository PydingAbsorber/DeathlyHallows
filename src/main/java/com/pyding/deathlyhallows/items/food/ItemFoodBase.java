package com.pyding.deathlyhallows.items.food;

import com.pyding.deathlyhallows.DeathlyHallows;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemFoodBase extends ItemFood {

	private boolean edibleInCreative = false;

	public ItemFoodBase(String unlocalizedName, int hunger, float saturation, int maxStackSize, CreativeTabs tab) {
		super(hunger, saturation, false);
		setUnlocalizedName(unlocalizedName);
		setTextureName(unlocalizedName);
		setMaxStackSize(maxStackSize);
		setCreativeTab(tab);
	}

	public ItemFoodBase(String unlocalizedName, int hunger, float saturation) {
		this(unlocalizedName, hunger, saturation, 64, DeathlyHallows.tabDeathlyHallows);
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer p) {
		if(edibleInCreative) {
			p.setItemInUse(stack, getMaxItemUseDuration(stack));
			return stack;
		}
		return super.onItemRightClick(stack, world, p);
	}

	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer p) {
		super.onEaten(stack, world, p);
		ItemStack container = getContainerItem(stack);
		if(container == null) {
			return stack;
		}
		if(stack.stackSize <= 0) {
			return container;
		}
		if(p.inventory.addItemStackToInventory(container)) {
			p.inventoryContainer.detectAndSendChanges();
		}
		else if(!world.isRemote) {
			p.entityDropItem(container, 1);
		}
		return stack;
	}

	@Override
	public ItemFood setAlwaysEdible() {
		edibleInCreative = true;
		return super.setAlwaysEdible();
	}

	@Override
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean devMode) {
		addTooltip(stack, player, list, devMode);
	}

	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister ir) {
		itemIcon = ir.registerIcon(DeathlyHallows.MODID + ":" + iconString);
	}

}
