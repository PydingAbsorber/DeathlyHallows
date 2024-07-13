package com.pyding.deathlyhallows.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.model.obj.WavefrontObject;

public class EntityModelWrapper extends ModelBase {
	private final WavefrontObject model;

	public EntityModelWrapper(WavefrontObject model) {
		this.model = model;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		model.renderAll();
	}
}
