package com.pyding.deathlyhallows.render.block;

import com.emoniph.witchery.client.model.ModelDemonHeart;
import com.emoniph.witchery.client.model.ModelMysticBranch;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.blocks.tiles.TileEntityVisConverter;
import com.pyding.deathlyhallows.integrations.DHIntegration;
import com.pyding.deathlyhallows.render.ModelWrapperDisplayList;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;

import static org.lwjgl.opengl.GL11.*;

public class RenderTileVisConverter extends TileEntitySpecialRenderer {

	public static final ResourceLocation
			MODEL_DEATH_SHARD = new ResourceLocation(DeathlyHallows.MODID, "models/deathShard.obj"),
			TEXTURE_DEMONHEART = new ResourceLocation(DHIntegration.WITCHERY, "textures/blocks/demonHeart.png"),
			TEXTURE_BRANCH = new ResourceLocation(DHIntegration.WITCHERY, "textures/entities/mysticbranch.png"),
			TEXTURE_SHARD = new ResourceLocation(DeathlyHallows.MODID, "textures/items/deathShard.png");
	public final IModelCustom shard = new ModelWrapperDisplayList((WavefrontObject)AdvancedModelLoader.loadModel(MODEL_DEATH_SHARD));
	public final ModelDemonHeart heart = new ModelDemonHeart();
	public final ModelMysticBranch branch = new ModelMysticBranch();

	@Override
	public void renderTileEntityAt(TileEntity e, double x, double y, double z, float partial) {
		long ticks = ((TileEntityVisConverter)e).clientTicks;
		glPushMatrix();
		glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
		renderShard(ticks);
		renderHearts(ticks);
		renderBranches();
		glPopMatrix();
	}

	private void renderShard(long ticks) {
		bindTexture(TEXTURE_SHARD);
		glPushMatrix();
		glTranslatef(0F, 0.2F, 0F);
		glScalef(0.4F, 0.4F, 0.4F);
		glRotatef(ticks % 360F, 0.0F, 1.0F, 0.0F);
		shard.renderAll();
		glPopMatrix();
	}

	private void renderBranches() {
		bindTexture(TEXTURE_BRANCH);
		for(int i = 0; i < 4; ++i) {
			glPushMatrix();
			glRotatef(180, 1F, 0F, 0F);
			glRotatef(90F * i, 0F, 1F, 0F);
			glTranslated(0.5F, -0.5F, 0.5F);
			glRotatef(140F, 1F, 0F, -1F);
			glScalef(1.2F, 1.2F, 1.2F);
			branch.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			glPopMatrix();
		}
	}

	private void renderHearts(long ticks) {
		glRotatef(180F, 1F, 0F, 0F);
		bindTexture(TEXTURE_DEMONHEART);
		for(int i = 0; i < 4; ++i) {
			glPushMatrix();
			glRotatef(90F * i, 0F, 1F, 0F);
			glTranslatef(0.1F - 0.4F, -0.4F, -0.09F);
			glScalef(0.6F, 0.6F, 0.6F);
			// period is about 25L, 25/4 ~ 6
			heart.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, ticks + 6L * i);
			glPopMatrix();
		}
	}

}
