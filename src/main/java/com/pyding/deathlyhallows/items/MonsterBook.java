package com.pyding.deathlyhallows.items;

import com.pyding.deathlyhallows.common.properties.ExtendedPlayer;
import com.pyding.deathlyhallows.integration.Integration;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class MonsterBook extends Item {
	public float damage = 2;

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		ExtendedPlayer props = ExtendedPlayer.get(player);
		damage = 2 + props.getMonstersCount();
		if(props.getElfLvl() > 0) {
			damage = 2 + props.getMonstersCount() * (props.getElfLvl() * 10);
		}
		entity.attackEntityFrom(DamageSource.causePlayerDamage(player).setDamageIsAbsolute(), damage);
		return super.onLeftClickEntity(stack, player, entity);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
		if(Integration.thaumcraft) {
			if(I18n.format("dh.util.language").equals("Ru")) {
				list.add("§5Став обладателем этой книги ты и сам преврвщаешься в монстра... ");
				list.add("§5Наносит§b " + damage + " абсолютного §5урона на текущий момент. Каждое убийство нового существа увеличит урон на 1.");
				if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
					list.add("Поговаривают, что книга любит тех, кто много знает. А кто много знает? Хммм....");
					list.add("Думается мне, что знания не сильно исправят ситуацию");
				}
			}
			else {
				list.add("§5Becoming owner of this book makes you monster too... ");
				list.add("§5Deals§b " + damage + " absolute §5damage currently. Each new killed monster will increase the damage for 1.");
				if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
					list.add("Rumor says that the book likes those one, who knows a lot. But who knows a lot? Hmmm...");
					list.add("Don't think that could change the situation");
				}
			}
		}
	}
}
