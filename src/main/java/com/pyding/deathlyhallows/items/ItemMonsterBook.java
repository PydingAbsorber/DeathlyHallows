package com.pyding.deathlyhallows.items;

import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemMonsterBook extends ItemBase {

	public ItemMonsterBook() {
		super("monsterBook", 1);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer p, Entity target) {
		DeathlyProperties props = DeathlyProperties.get(p);
		if(target.canAttackWithItem()) {
			target.attackEntityFrom(DamageSource.causePlayerDamage(p).setDamageIsAbsolute(), getBookDamage(props) * getElfModifier(props));
		}
		return true;
	}

	public static float getBookDamage(DeathlyProperties props) {
		return 2 * props.getMonstersCount();
	}

	public static float getElfModifier(DeathlyProperties props) {
		return Math.max(1, 2 * props.getElfLevel());
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		DeathlyProperties props = DeathlyProperties.get(p);
		l.add(StatCollector.translateToLocal("dh.desc.book1"));
		l.add(StatCollector.translateToLocalFormatted("dh.desc.book2", getBookDamage(DeathlyProperties.get(p))));
		if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			return;
		}
		l.add(StatCollector.translateToLocal("dh.desc.book3"));
		if(props.getElfLevel() > 0) {
			l.add(StatCollector.translateToLocal("dh.desc.book4"));
		}
	}

}
