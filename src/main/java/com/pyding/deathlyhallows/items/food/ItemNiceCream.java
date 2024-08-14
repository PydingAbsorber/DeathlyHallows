package com.pyding.deathlyhallows.items.food;

import com.emoniph.witchery.brewing.potions.WitcheryPotions;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.ChatUtil;
import com.pyding.deathlyhallows.utils.DHUtils;
import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemNiceCream extends ItemFoodBase {
	
	public ItemNiceCream() {
		super("niceCream", 4, 25);
		setAlwaysEdible();
	}

	@Override
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		l.add(StatCollector.translateToLocal("dh.desc.niceCream"));
	}

	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer p) {
		if(!world.isRemote) {
			ChatUtil.sendTranslated(EnumChatFormatting.GREEN, p, "dh.chat.niceCream" + DHUtils.getRandomInt(1, 7));
		}
		p.heal(20);
		Infusion.setCurrentEnergy(p, Math.min(Infusion.getMaxEnergy(p), Infusion.getCurrentEnergy(p) + 5));
		NBTTagCompound tag = p.getEntityData();
		tag.setInteger("NiceCream", tag.getInteger("NiceCream") + 10);
		DeathlyProperties props = DeathlyProperties.get(p);
		props.setNiceCream(props.getNiceCream() + 1);
		if(tag.getLong("NiceTime") == 0) {
			tag.setLong("NiceTime", System.currentTimeMillis() + 20000);
			return;
		}
		if(tag.getLong("NiceTime") <= System.currentTimeMillis()) {
			tag.setLong("NiceTime", System.currentTimeMillis() + 20000);
			if(tag.getInteger("NiceCream") > 5) {
				WitcheryPotions potions = new WitcheryPotions();
				p.addPotionEffect(new PotionEffect(potions.CHILLED.getId(), 200, 255));
			}
			tag.setInteger("NiceCream", 0);
		}
	}
	
}
