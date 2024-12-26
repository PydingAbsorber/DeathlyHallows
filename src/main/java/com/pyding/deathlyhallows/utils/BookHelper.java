package com.pyding.deathlyhallows.utils;

import com.pyding.deathlyhallows.integrations.DHIntegration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class BookHelper {
	
	public static final Map<String, Function<EntityPlayer, String>> bookFormatter = new HashMap<>();
	
	public static void init() {
		for(int i = 2; i < 11; ++i) {
			int javaMoment = i;
			bookFormatter.put(
					"req" + i,
					p -> String.valueOf(DHConfig.getElfRequirements(javaMoment))
			);
		}
		bookFormatter.put(
				"modResource",
				p -> {
					StringBuilder b = new StringBuilder();
					if(DHIntegration.thaumcraft) {
						b.append(StatCollector.translateToLocal("dh:elfBook.modResource_" + DHIntegration.THAUMCRAFT));
					}
					if(DHIntegration.botania) {
						b.append(StatCollector.translateToLocal("dh:elfBook.modResource_" + DHIntegration.BOTANIA));
					}
					if(DHIntegration.arsMagica) {
						b.append(StatCollector.translateToLocal("dh:elfBook.modResource_" + DHIntegration.ARS_MAGICA));
					}
					if(DHIntegration.bloodMagic) {
						b.append(StatCollector.translateToLocal("dh:elfBook.modResource_" + DHIntegration.BLOOD_MAGIC));
					}
					return b.toString();
				}
		);
	}
	
}
