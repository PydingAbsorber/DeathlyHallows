package com.pyding.deathlyhallows.integrations.nei;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockGrassper;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.recipes.DHGrassperRecipes;
import com.pyding.deathlyhallows.utils.DHUtils;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class NEIDHGrassperRecipeHandler extends TemplateRecipeHandler {

	private static final ResourceLocation OVERLAY = new ResourceLocation(DeathlyHallows.MODID, "textures/gui/nei/grassperRecipeOverlay.png");
	private static final int topOffset = 15;

	public class CachedGrassperRecipe extends CachedRecipe {
		private final PositionedStack input, reagent, output;

		public CachedGrassperRecipe(String recipe) {
			ItemStack[] ingredients = DHGrassperRecipes.getInputsFromKey(recipe);
			input = new PositionedStack(ingredients[0], 31, 1 + topOffset, false);
			reagent = new PositionedStack(ingredients[1], 75, 8 + topOffset, false);
			output = new PositionedStack(DHGrassperRecipes.getResult(recipe), 122, 8 + topOffset, false);
		}

		@Override
		public PositionedStack getIngredient() {
			return input;
		}

		@Override
		public PositionedStack getOtherStack() {
			return reagent;
		}

		@Override
		public PositionedStack getResult() {
			return output;
		}
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if(!outputId.equals(getOverlayIdentifier()) || getClass() != this.getClass()) {
			super.loadCraftingRecipes(outputId, results);
			return;
		}
		for(String recipe: DHGrassperRecipes.recipeKeys()) {
			arecipes.add(new CachedGrassperRecipe(recipe));
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for(String recipe: DHGrassperRecipes.recipeKeys()) {
			if(NEIServerUtils.areStacksSameTypeCrafting(DHGrassperRecipes.getResult(recipe), result)) {
				arecipes.add(new CachedGrassperRecipe(recipe));
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		if(ingredient != null && ingredient.isItemEqual(new ItemStack(Witchery.Blocks.GRASSPER))) {
			loadCraftingRecipes(getOverlayIdentifier());
			return;
		}
		for(String recipe: DHGrassperRecipes.recipeKeys()) {
			for(ItemStack recipeIngr: DHGrassperRecipes.getInputsFromKey(recipe)) {
				if(NEIServerUtils.areStacksSameTypeCrafting(recipeIngr, ingredient)) {
					arecipes.add(new CachedGrassperRecipe(recipe));
				}
			}
		}
	}

	@Override
	public String getOverlayIdentifier() {
		return DeathlyHallows.MODID + ".grassperRecipes";
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(
				new Rectangle(94, 8 + topOffset, 24, 18),
				getOverlayIdentifier()
		));
	}

	@Override
	public int recipiesPerPage() {
		return 2;
	}

	@Override
	public String getRecipeName() {
		return "Grassper Mutation";
	}

	@Override
	public String getGuiTexture() {
		return "";
	}

	@Override
	public void drawBackground(int recipe) {
		glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		glDisable(0xB50);
		float scale = 45F;
		glPushMatrix();
		glTranslated(0F, topOffset, 0F);
		glScalef(scale, -scale, 1F);
		glTranslated(0.16F, -0.55F, 2F);
		glRotated(45F, 1F, 0F, 0F);
		glRotated(45F, 0F, 1F, 0F);
		TileEntityRendererDispatcher.instance.renderTileEntityAt(new BlockGrassper.TileEntityGrassper(), 0D, 0D, 0D, 0F);
		glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderManager.instance.renderEngine.bindTexture(OVERLAY);
		DHUtils.drawTexturedRect(30, topOffset, 10, 128, 32, 0, 0, 128, 32, 128, 32);
	}

	@Override
	public void drawForeground(int recipe) {
		
	}
	
}
