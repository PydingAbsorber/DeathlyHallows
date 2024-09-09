package com.pyding.deathlyhallows.integrations;

import am2.items.ItemsCommonProxy;
import net.minecraft.item.ItemStack;

public final class DHArsMagica2 {
	
	// TODO do.
	private DHArsMagica2() {
		
	}
	
	public static ItemStack essence(int count, int meta) {
		return new ItemStack(ItemsCommonProxy.essence, count, meta);
	}
	
}
