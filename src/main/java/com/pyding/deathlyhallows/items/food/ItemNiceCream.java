package com.pyding.deathlyhallows.items.food;

import com.emoniph.witchery.brewing.potions.WitcheryPotions;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.ChatUtil;
import com.pyding.deathlyhallows.utils.properties.DeathlyProperties;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ItemNiceCream extends ItemFoodBase{
	public ItemNiceCream() {
		super("niceCream",5, 25);
		setAlwaysEdible();
	}

	@Override
	protected void addTooltip(ItemStack stack, EntityPlayer p, List<String> l, boolean devMode) {
		l.add(StatCollector.translateToLocal("dh.desc.niceCream"));
		super.addTooltip(stack, p, l, devMode);
	}

	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer p) {
		super.onEaten(stack, world, p);
		if(!world.isRemote) {
			Random random = new Random();
			int numba = random.nextInt(6)+1;
			ChatUtil.sendTranslated(EnumChatFormatting.GREEN, p, "dh.chat.niceCream"+numba);
		}
		p.heal(20);
		Infusion.setCurrentEnergy(p,Math.min(Infusion.getMaxEnergy(p),Infusion.getCurrentEnergy(p)+5));
		p.getEntityData().setInteger("NiceCream",p.getEntityData().getInteger("NiceCream")+10);
		DeathlyProperties props = DeathlyProperties.get(p);
		props.setNiceCream(props.getNiceCream()+1);
		if(p.getEntityData().getLong("NiceTime") == 0)
			p.getEntityData().setLong("NiceTime",System.currentTimeMillis()+20000);
		else if(p.getEntityData().getLong("NiceTime") <= System.currentTimeMillis()){
			p.getEntityData().setLong("NiceTime",System.currentTimeMillis()+20000);
			if(p.getEntityData().getInteger("NiceCream") > 5){
				WitcheryPotions potions = new WitcheryPotions();
				p.addPotionEffect(new PotionEffect(potions.CHILLED.getId(),200,255));
			}
			p.getEntityData().setInteger("NiceCream",0);
		}
		return stack;
	}
}
