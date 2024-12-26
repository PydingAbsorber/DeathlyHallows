package com.pyding.deathlyhallows.items;

import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.proxy.GuiHandler;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ItemElderBook extends ItemBase {

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public ItemElderBook() {
		super("elderBook", 64);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World w, EntityPlayer p) {
		FMLNetworkHandler.openGui(p, DeathlyHallows.Instance, Books.values()[stack.getItemDamage()].guiEnum.ordinal(), w, (int)p.posX, (int)p.posY, (int)p.posZ);
		return stack;
	}

	@Override
	public void registerIcons(IIconRegister i) {
		icons = new IIcon[Books.values().length];
		for(Books t: Books.values()) {
			icons[t.ordinal()] = i.registerIcon(DeathlyHallows.MODID + ":elderBook." + t.name());
		}
	}

	@Override
	public IIcon getIconFromDamage(int type) {
		if(type == OreDictionary.WILDCARD_VALUE) {
			type = 0;
		}
		if(type < 0 || type > Books.values().length) {
			return null;
		}
		return icons[type];
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		int type = stack.getItemDamage();
		if(type == OreDictionary.WILDCARD_VALUE) {
			type = 0;
		}
		return StatCollector.translateToLocal(getUnlocalizedName() + "." + Books.values()[type].name() + ".name");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List l) {
		for(Books t: Books.values()) {
			l.add(new ItemStack(this, 1, t.ordinal()));
		}
	}

	public enum Books {
		Rituals(GuiHandler.GUI.ELDER_RITUALS),
		Wands(GuiHandler.GUI.ELDER_WANDS);
		
		private final GuiHandler.GUI guiEnum;
		
		Books(GuiHandler.GUI guiEnum) {
			this.guiEnum = guiEnum;
		}
	}

}
