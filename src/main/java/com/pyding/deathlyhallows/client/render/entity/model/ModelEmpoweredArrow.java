package com.pyding.deathlyhallows.client.render.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelEmpoweredArrow extends ModelBase {

	private final ModelRenderer head;
	private final ModelRenderer tail;
	private final ModelRenderer body;

	@SuppressWarnings("unchecked")
	public ModelEmpoweredArrow() {
		textureWidth = 32;
		textureHeight = 16;

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, -4.0F, 2.0F);
		head.cubeList.add(new ModelBox(head, -1, 2, -0.5F, 0.0F, -12.0F, 1, 0, 3, 0.0F));
		head.cubeList.add(new ModelBox(head, 0, 0, 0.0F, -0.5F, -12.0F, 0, 1, 3, 0.0F));
		head.cubeList.add(new ModelBox(head, -1, 1, -1.0F, 0.0F, -9.0F, 2, 0, 1, 0.0F));
		head.cubeList.add(new ModelBox(head, 2, 4, 0.0F, -2.0F, -8.0F, 0, 4, 1, 0.0F));
		head.cubeList.add(new ModelBox(head, -1, 5, -2.0F, 0.0F, -8.0F, 4, 0, 1, 0.0F));
		head.cubeList.add(new ModelBox(head, 2, 0, 0.0F, -1.0F, -9.0F, 0, 2, 1, 0.0F));

		tail = new ModelRenderer(this);
		tail.setRotationPoint(8.0F, 0.0F, -8.0F);
		tail.cubeList.add(new ModelBox(tail, 2, 7, -10.0F, -4.0F, 15.0F, 4, 0, 2, 0.0F));
		tail.cubeList.add(new ModelBox(tail, 2, 7, -8.0F, -6.0F, 15.0F, 0, 4, 2, 0.0F));
		tail.cubeList.add(new ModelBox(tail, 1, 6, -9.0F, -4.0F, 12.0F, 2, 0, 3, 0.0F));
		tail.cubeList.add(new ModelBox(tail, 1, 6, -8.0F, -5.0F, 12.0F, 0, 2, 3, 0.0F));

		body = new ModelRenderer(this);
		body.setRotationPoint(8.0F, 0.0F, -8.0F);
		body.cubeList.add(new ModelBox(body, 0, 0, -8.5F, -4.5F, 3.0F, 1, 1, 14, 0.0F));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		head.render(f5);
		tail.render(f5);
		body.render(f5);
	}

}
