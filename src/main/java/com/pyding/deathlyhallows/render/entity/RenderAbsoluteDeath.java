package com.pyding.deathlyhallows.render.entity;

import com.emoniph.witchery.client.model.ModelDeath;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.entities.EntityAbsoluteDeath;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.render.ModelWrapperDisplayList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class RenderAbsoluteDeath extends RenderLiving {
	private static final ResourceLocation
			SHIELD_MODEL = new ResourceLocation(DeathlyHallows.MODID, "models/shield.obj"),
			DEATH_TEXTURE = new ResourceLocation(DHIntegration.WITCHERY, "textures/entities/death.png");

	private static final IModelCustom shield = new ModelWrapperDisplayList((WavefrontObject)AdvancedModelLoader.loadModel(SHIELD_MODEL));
	private static final float
			SHIELD_ORBIT_RADIUS = 1F,
			SHIELD_BOBBING_POWER = 0.1F;

	public RenderAbsoluteDeath() {
		super(new ModelDeath(), 0.5F);
	}
	
	public void doRenderDeath(EntityAbsoluteDeath e, double x, double y, double z, float yaw, float partial) {
		super.doRender(e, x, y, z, yaw, partial);
		BossStatus.setBossStatus(e, true);
		renderShields(e, e.ticksExisted + partial, x, y, z);
	}

	private void renderShields(EntityAbsoluteDeath e, float ticks, double x, double y, double z) {
		final int shieldAmount = 5;
		List<EntityAbsoluteDeath.EnumResists> shields = Arrays.stream(EntityAbsoluteDeath.EnumResists.values()).filter(
				resist -> e.getResist(resist) >= shieldAmount
		).collect(Collectors.toList());
		if(shields.isEmpty()) {
			return;
		}
		int shieldsSize = shields.size();
		glPushMatrix();
		glTranslated(x, y + 1.25F, z);
		glRotatef(2F * (ticks % 180F), 0.0F, 1.0F, 0.0F);
		for(int i = 0; i < shieldsSize; ++i) {
			glPushMatrix();
			glRotatef(360F * i / shieldsSize, 0.0F, 1.0F, 0.0F);
			float bobbing = SHIELD_BOBBING_POWER * MathHelper.sin((float)Math.PI * (((ticks) % 40F) / 20F + 2F * i / shieldsSize));
			glTranslatef(0F, bobbing, SHIELD_ORBIT_RADIUS);
			glScalef(1F, 1F, 1F);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glColor4f(1F, 1F, 1F, e.getResist(shields.get(i)) / 100F);
			bindTexture(shields.get(i).getShieldTexture());
			shield.renderAll();
			glDisable(GL_BLEND);
			glPopMatrix();
		}
		glPopMatrix();
	}

	public void doRender(EntityLiving entity, double x, double y, double z, float yaw, float partial) {
		this.doRenderDeath((EntityAbsoluteDeath)entity, x, y, z, yaw, partial);
	}

	protected ResourceLocation getEntityTexture(Entity e) {
		return DEATH_TEXTURE;
	}

}

