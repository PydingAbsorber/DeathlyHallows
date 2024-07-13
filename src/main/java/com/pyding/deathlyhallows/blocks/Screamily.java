package com.pyding.deathlyhallows.blocks;

import com.emoniph.witchery.entity.EntityBanshee;
import com.pyding.deathlyhallows.common.handler.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileGenerating;

import java.util.List;

public class Screamily extends SubTileGenerating {
	@Override
	public boolean isPassiveFlower() {
		return false;
	}

	public long manaMaxCd = 10000;
	public long manaCd = 0;

	@Override
	public void onUpdate() {
		super.onUpdate();
		int multiplier = 1;
		int radius = 4;
		int manaPerScream = ConfigHandler.screamilyMana;
		int maximumBanshee = 16;
		if(overgrowthBoost) {
			multiplier = 2;
		}
		double x = supertile.xCoord;
		double y = supertile.yCoord;
		double z = supertile.zCoord;
		AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
		List banshees = supertile.getWorldObj().getEntitiesWithinAABB(EntityBanshee.class, box);
		for(Object o: banshees) {
			if(o instanceof EntityBanshee) {
				EntityBanshee banshee = (EntityBanshee)o;
				if(banshee.isScreaming() && multiplier < maximumBanshee) {
					multiplier++;
				}
			}
		}
		if(System.currentTimeMillis() - manaCd > manaMaxCd && multiplier > 2) {
			this.addMana(manaPerScream * multiplier);
			manaCd = manaMaxCd + System.currentTimeMillis();
		}
	}

	@Override
	public boolean onWanded(EntityPlayer player, ItemStack wand) {
		return super.onWanded(player, wand);
	}

	@Override
	public int getMaxMana() {
		return 1000000;
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res) {
		String name = StatCollector.translateToLocal("tile.botania:flower." + this.getUnlocalizedName() + ".name");
		int color = this.getColor();
		BotaniaAPI.internalHandler.drawComplexManaHUD(color, this.knownMana, this.getMaxMana(), name, res, BotaniaAPI.internalHandler.getBindDisplayForFlowerType(this), this.isValidBinding());
	}
}
