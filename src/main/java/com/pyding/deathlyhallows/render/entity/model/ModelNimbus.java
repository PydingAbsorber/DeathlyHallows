package com.pyding.deathlyhallows.render.entity.model;

import com.pyding.deathlyhallows.entities.EntityNimbus;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelNimbus extends ModelBase {
	public ModelRenderer
			handle,
			bristle1,
			bristle2,
			bristle3,
			bristle4,
			bristle5,
			bristle6,
			bristle7,
			bristle8,
			bristle9,
			midHandle,
			backHandle1,
			backHandle2,
			frontHandle,
			stub,
			bristle1_,
			bristle2_,
			bristle3_,
			bristle4_,
			bristle5_,
			bristle6_,
			bristle7_,
			bristle8_,
			bristle9_;

	public ModelNimbus() {
		textureWidth = 16;
		textureHeight = 16;
		handle = new ModelRenderer(this, 0, 0);
		handle.setRotationPoint(0.0F, 11.0F, 9.0F);
		handle.addBox(-1.0F, -8.0F, -1.0F, 2, 8, 2, 0.0F);
		setRotateAngle(handle, 1.5707963267948966F, 0.0F, 0.0F);
		stub = new ModelRenderer(this, 0, 0);
		stub.setRotationPoint(0.0F, -11.0F, 0.0F);
		stub.addBox(-1.0F, -2.0F, -1.0F, 2, 3, 2, 0.0F);
		setRotateAngle(stub, 1.5707963267948966F, 0.0F, 0.0F);
		frontHandle = new ModelRenderer(this, 0, 0);
		frontHandle.setRotationPoint(0.0F, -5.8F, 0.1F);
		frontHandle.addBox(-1.0F, -10.0F, -1.0F, 2, 10, 2, 0.0F);
		setRotateAngle(frontHandle, 0.5235987755982988F, 0.0F, 0.0F);
		bristle4 = new ModelRenderer(this, 3, 9);
		bristle4.setRotationPoint(0.75F, 11.0F, 9.0F);
		bristle4.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		setRotateAngle(bristle4, 0.0F, 0.13962634015954636F, 0.0F);
		bristle9_ = new ModelRenderer(this, 4, 9);
		bristle9_.setRotationPoint(0.0F, 0.0F, 5.0F);
		bristle9_.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		setRotateAngle(bristle9_, -0.20943951023931953F, 0.20943951023931953F, -0.013962634015954637F);
		bristle8_ = new ModelRenderer(this, 3, 9);
		bristle8_.setRotationPoint(0.0F, 0.0F, 5.0F);
		bristle8_.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		setRotateAngle(bristle8_, -0.2792526803190927F, 0.0F, 0.0F);
		bristle5_ = new ModelRenderer(this, 3, 9);
		bristle5_.setRotationPoint(0.0F, 0.0F, 5.0F);
		bristle5_.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		bristle2_ = new ModelRenderer(this, 4, 9);
		bristle2_.setRotationPoint(0.0F, 0.0F, 5.0F);
		bristle2_.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		setRotateAngle(bristle2_, 0.2792526803190927F, 0.0F, 0.0F);
		bristle5 = new ModelRenderer(this, 3, 9);
		bristle5.setRotationPoint(0.0F, 11.0F, 9.0F);
		bristle5.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		midHandle = new ModelRenderer(this, 0, 0);
		midHandle.setRotationPoint(0.0F, -7.2F, -0.3F);
		midHandle.addBox(-1.0F, -6.3F, -1.0F, 2, 6, 2, 0.0F);
		setRotateAngle(midHandle, -0.5235987755982988F, 0.0F, 0.0F);
		bristle1_ = new ModelRenderer(this, 3, 9);
		bristle1_.setRotationPoint(0.0F, 0.0F, 5.0F);
		bristle1_.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		setRotateAngle(bristle1_, 0.20943951023931953F, -0.20943951023931953F, 0.0F);
		bristle3_ = new ModelRenderer(this, 3, 9);
		bristle3_.setRotationPoint(0.0F, 0.0F, 5.0F);
		bristle3_.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		setRotateAngle(bristle3_, 0.20943951023931953F, 0.20943951023931953F, 0.0F);
		bristle4_ = new ModelRenderer(this, 4, 9);
		bristle4_.setRotationPoint(0.0F, 0.0F, 5.0F);
		bristle4_.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		setRotateAngle(bristle4_, 0.0F, -0.2792526803190927F, 0.0F);
		bristle1 = new ModelRenderer(this, 4, 9);
		bristle1.setRotationPoint(0.75F, 11.75F, 9.0F);
		bristle1.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		setRotateAngle(bristle1, -0.10471975511965977F, 0.10471975511965977F, 0.0F);
		bristle2 = new ModelRenderer(this, 3, 9);
		bristle2.setRotationPoint(0.0F, 11.75F, 9.0F);
		bristle2.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		setRotateAngle(bristle2, -0.13962634015954636F, 0.0F, 0.0F);
		bristle6_ = new ModelRenderer(this, 3, 9);
		bristle6_.setRotationPoint(0.0F, 0.0F, 5.0F);
		bristle6_.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		setRotateAngle(bristle6_, 0.0F, 0.2792526803190927F, 0.0F);
		bristle7_ = new ModelRenderer(this, 3, 9);
		bristle7_.setRotationPoint(0.0F, 0.0F, 5.0F);
		bristle7_.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		setRotateAngle(bristle7_, -0.20943951023931953F, -0.20943951023931953F, 0.0F);
		bristle9 = new ModelRenderer(this, 3, 9);
		bristle9.setRotationPoint(-0.75F, 10.25F, 9.0F);
		bristle9.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		setRotateAngle(bristle9, 0.10471975511965977F, -0.10471975511965977F, 0.0F);
		backHandle1 = new ModelRenderer(this, 8, 0);
		backHandle1.setRotationPoint(0.0F, -0.25F, 0.0F);
		backHandle1.addBox(-1.5F, -1.5F, -0.5F, 3, 3, 1, 0.0F);
		setRotateAngle(backHandle1, 1.5707963267948966F, 0.0F, 0.0F);
		bristle6 = new ModelRenderer(this, 4, 9);
		bristle6.setRotationPoint(-0.75F, 11.0F, 9.0F);
		bristle6.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		setRotateAngle(bristle6, 0.0F, -0.13962634015954636F, 0.0F);
		bristle7 = new ModelRenderer(this, 4, 9);
		bristle7.setRotationPoint(0.75F, 10.25F, 9.0F);
		bristle7.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		setRotateAngle(bristle7, 0.10471975511965977F, 0.10471975511965977F, 0.0F);
		bristle8 = new ModelRenderer(this, 4, 9);
		bristle8.setRotationPoint(0.0F, 10.25F, 9.0F);
		bristle8.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		setRotateAngle(bristle8, 0.13962634015954636F, 0.0F, 0.0F);
		backHandle2 = new ModelRenderer(this, 8, 0);
		backHandle2.setRotationPoint(0.0F, 1.25F, 0.0F);
		backHandle2.addBox(-1.5F, -1.5F, -0.5F, 3, 3, 1, 0.0F);
		setRotateAngle(backHandle2, 1.5707963267948966F, 0.0F, 0.0F);
		bristle3 = new ModelRenderer(this, 3, 9);
		bristle3.setRotationPoint(-0.75F, 11.75F, 9.0F);
		bristle3.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 5, 0.0F);
		setRotateAngle(bristle3, -0.10471975511965977F, -0.10471975511965977F, 0.0F);
		frontHandle.addChild(stub);
		midHandle.addChild(frontHandle);
		bristle9.addChild(bristle9_);
		bristle8.addChild(bristle8_);
		bristle5.addChild(bristle5_);
		bristle2.addChild(bristle2_);
		handle.addChild(midHandle);
		bristle1.addChild(bristle1_);
		bristle3.addChild(bristle3_);
		bristle4.addChild(bristle4_);
		bristle6.addChild(bristle6_);
		bristle7.addChild(bristle7_);
		handle.addChild(backHandle1);
		handle.addChild(backHandle2);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		GL11.glColor4f(1F, 1F, 1F, f);
		handle.render(f5);
		if(entity instanceof EntityNimbus) {
			int color = ((EntityNimbus)entity).getBrushColor();
			GL11.glColor4ub((byte)((color >> 16) & 0xFF), (byte)((color >> 8) & 0xFF), (byte)(color & 0xFF), (byte)(f * 0xFF));
		}
		else {
			GL11.glColor4f(0.4F, 0.3F, 0.2F, f);
		}
		bristle1.render(f5);
		bristle2.render(f5);
		bristle3.render(f5);
		bristle4.render(f5);
		bristle5.render(f5);
		bristle6.render(f5);
		bristle7.render(f5);
		bristle8.render(f5);
		bristle9.render(f5);
	}
	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	
}
