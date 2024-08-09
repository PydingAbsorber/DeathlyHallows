package com.pyding.deathlyhallows.entities;

import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.render.entity.RenderAbsoluteDeath;
import com.pyding.deathlyhallows.render.entity.RenderEmpoweredArrow;
import com.pyding.deathlyhallows.render.entity.RenderNimbus;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

public class DHEntities {
	private static int ID = 92;
	private static int EGG_ID = 6400;

	private DHEntities() {

	}

	public static void init() {
		register(EntityAbsoluteDeath.class, "AbsoluteDeath");
		addEgg(EntityAbsoluteDeath.class, 0x910000, 0xB06C10);
		register(EntityNimbus.class, "Nimbus3000", 64, 2, true);
		register(EntityEmpoweredArrow.class, "EmpoweredArrow", 128, 2, true);

	}

	private static void register(Class<? extends Entity> clazz, String name) {
		register(clazz, name, 80, 3, true);
	}

	private static void register(Class<? extends Entity> clazz, String name, int updateDistance, int updateFrequency, boolean updateVelocity) {
		EntityRegistry.registerModEntity(clazz, name, ID++, DeathlyHallows.Instance, updateDistance, updateFrequency, updateVelocity);
	}

	@SuppressWarnings("unchecked")
	private static void addEgg(Class<? extends Entity> clazz, int color1, int color2) {
		while(EntityList.IDtoClassMapping.containsKey(EGG_ID)) { // weak overlap protection
			++EGG_ID;
		}
		EntityList.IDtoClassMapping.put(EGG_ID, clazz);
		EntityList.entityEggs.put(EGG_ID, new EntityList.EntityEggInfo(EGG_ID++, color1, color2));
	}

	@SideOnly(Side.CLIENT)
	public static void initClient() {
		// entities
		render(EntityAbsoluteDeath.class, new RenderAbsoluteDeath());
		render(EntityNimbus.class, new RenderNimbus());
		render(EntityEmpoweredArrow.class, new RenderEmpoweredArrow());
	}

	@SideOnly(Side.CLIENT)
	public static void render(Class<? extends Entity> clazz, Render render) {
		RenderingRegistry.registerEntityRenderingHandler(clazz, render);
	}
}
