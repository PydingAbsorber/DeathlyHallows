package com.pyding.deathlyhallows.symbols;

import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.deathlyhallows.utils.DHID;
import com.pyding.deathlyhallows.utils.DHUtils;
import com.pyding.deathlyhallows.utils.ElfUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class SymbolSectumsempra extends SymbolEffectBase {

	private static final float
			TRACE_RANGE = 48F,
			RANGE = 1F,
			HP_LOWER = 5F,
			HP_PERCENT = 0.15F;

	public SymbolSectumsempra() {
		super(DHID.SYMBOL_SECTUMSEMPRA, "sectumsempra", 15, true, false, null, 5_000, true);
	}

	@Override
	public void perform(World world, EntityPlayer p, int level) {
		Vec3 look = DHUtils.getLook(p, TRACE_RANGE);
		double x = look.xCoord, y = look.yCoord, z = look.zCoord;
		float range = RANGE + level / 2F;
		List<EntityLivingBase> entities = DHUtils.getEntitiesAt(EntityLivingBase.class, world, x, y, z, range);
		for(EntityLivingBase e: entities) {
			if(e == p) {
				continue;
			}
			NBTTagCompound tag = e.getEntityData();
			tag.setInteger("SectumTime", 666);
			float hpLower = e.getMaxHealth() * HP_PERCENT * (1F + ElfUtils.getElfLevel(p) / 10F);
			if(e.getMaxHealth() - hpLower < HP_LOWER) {
				continue;
			}
			tag.setFloat("SectumHp", tag.getInteger("SectumHp") + hpLower);
			Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			// TODO operation type 1 exists, and no need to -hp*percent when you can -percent
			attributes.put(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), new AttributeModifier("SectumHPAttribute", -hpLower, 0));
			e.getAttributeMap().applyAttributeModifiers(attributes);
			attributes.clear();
			e.setHealth(Math.min(e.getHealth(), e.getMaxHealth()));
		}
		ParticleEffect.FLAME.send(SoundEffect.FIRE_FIRE, world, x, y, z, range * 2D, range * 2D, 64);
		world.playSoundAtEntity(p, "dh:spell.sectum" + DHUtils.getRandomInt(1, 2), 1F, 1F);
	}

}
