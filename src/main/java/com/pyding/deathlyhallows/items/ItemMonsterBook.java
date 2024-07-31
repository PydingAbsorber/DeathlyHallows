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
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		DeathlyProperties props = DeathlyProperties.get(player);
		float damage = 2 + props.getMonstersCount();
		if(props.getElfLevel() > 0) {
			damage = 2 + props.getMonstersCount() * (props.getElfLevel() * 10);
		}
		entity.attackEntityFrom(DamageSource.causePlayerDamage(player).setDamageIsAbsolute(), damage);
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		DeathlyProperties props = DeathlyProperties.get(p);
		float damage = props.getMonstersCount();
		l.add(StatCollector.translateToLocal("dh.desc.book1"));
		l.add(StatCollector.translateToLocalFormatted("dh.desc.book2", damage));
		if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			return;
		}
		l.add(StatCollector.translateToLocal("dh.desc.book3"));
		if(props.getElfLevel() > 0) {
			l.add(StatCollector.translateToLocal("dh.desc.book4"));
		}
	}
	
}
