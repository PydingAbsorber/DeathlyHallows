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
import net.minecraft.world.World;

public class ItemNiceCream extends ItemFoodBase{
	public ItemNiceCream() {
		super("niceCream",5, 25);
		setAlwaysEdible();
	}
	
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer p) {
		super.onEaten(stack, world, p);
		if(!world.isRemote) {
			ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, p, "dh.chat.food");
		}
		p.heal(20);
		Infusion.setCurrentEnergy(p,Math.min(Infusion.getMaxEnergy(p),Infusion.getCurrentEnergy(p)+5));
		p.getEntityData().setInteger("NiceCream",p.getEntityData().getInteger("NiceCream")+1);
		DeathlyProperties props = DeathlyProperties.get(p);
		props.setNiceCream(props.getNiceCream()+1);
		if(p.getEntityData().getLong("NiceTime") == 0)
			p.getEntityData().setLong("NiceTime",System.currentTimeMillis());
		else if(p.getEntityData().getLong("NiceTime")+20000 > System.currentTimeMillis()){
			p.getEntityData().setLong("NiceTime",System.currentTimeMillis());
			if(p.getEntityData().getInteger("NiceCream") > 5){
				p.getEntityData().setInteger("NiceCream",0);
				WitcheryPotions potions = new WitcheryPotions();
				p.addPotionEffect(new PotionEffect(potions.CHILLED.getId(),200,255));
			}
		}
		return stack;
	}
}
