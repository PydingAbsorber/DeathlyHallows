package com.pyding.deathlyhallows.recipes.actions;

import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.EffectLevelCounter;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.brewing.ModifiersRitual;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.RitualStatus;
import com.emoniph.witchery.brewing.action.BrewAction;
import com.emoniph.witchery.brewing.action.BrewActionList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BrewActionIngredient extends BrewAction {
	
	public BrewActionIngredient(BrewItemKey itemKey) {
		super(itemKey, null, new AltarPower(0), Probability.CERTAIN, true);
	}

	@Override
	public boolean augmentEffectLevels(EffectLevelCounter effectLevelCounter) {
		return true;
	}

	@Override
	public void augmentEffectModifiers(ModifiersEffect modifiersEffect) {
	}

	@Override
	public void prepareSplashPotion(World world, BrewActionList brewActionList, ModifiersImpact modifiersImpact) {
	}
	
	@Override
	public final void applyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
	}

	@Override
	public final void applyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect effectModifiers, ItemStack stack) {
	}
	
	@Override
	public final void prepareRitual(World world, int x, int y, int z, ModifiersRitual modifiers, ItemStack stack) {
	}

	@Override
	public final RitualStatus updateRitual(MinecraftServer server, BrewActionList actionList, World world, int x, int y, int z, ModifiersRitual modifiers, ModifiersImpact impactModifiers) {
		return RitualStatus.COMPLETE;
	}
	
}

