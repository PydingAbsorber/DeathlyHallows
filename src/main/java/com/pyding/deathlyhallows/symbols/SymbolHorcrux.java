package com.pyding.deathlyhallows.symbols;

import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.SoundEffect;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.deathlyhallows.utils.DHID;
import com.pyding.deathlyhallows.utils.DHUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class SymbolHorcrux extends SymbolEffectBase {
	
	public static final float HP_COST = 10F;
	
	public SymbolHorcrux() {
		super(DHID.SYMBOL_HORCRUX, "horcrux", 120, true, false, null, 10_000, true, ElderSymbolTraits.INFUSION(4), ElderSymbolTraits.ELF(1));
	}

	@Override
	public void perform(World world, EntityPlayer p, int level) {
		int lives = p.getEntityData().getInteger("Horcrux");
		if(p.getMaxHealth() - HP_COST > HP_COST) {
			Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("HorcruxHP", -HP_COST, 0));
			p.getAttributeMap().applyAttributeModifiers(attributes);
			p.getEntityData().setInteger("Horcrux", lives + 1);
			p.setHealth(Math.min(p.getHealth(), p.getMaxHealth()));
		}
		else {
			ChatUtil.sendTranslated(EnumChatFormatting.RED, p, "dh.chat.horcruxFail");
			SoundEffect.NOTE_SNARE.playAtPlayer(world, p);
		}
		world.playSoundAtEntity(p, "dh:spell.death" + DHUtils.getRandomInt(1, 2), 1F, 1F);
	}
	
}
