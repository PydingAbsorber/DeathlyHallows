package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemMutator;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.utils.DHUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.foci.ItemFocusPech;

import java.util.List;

import static com.pyding.deathlyhallows.DeathlyHallows.tabDeathlyHallows;

public class ItemFocusInferioisMutandis extends ItemFocusPech {
	private IIcon depthIcon = null;
	private static final AspectList cost = (new AspectList()).add(Aspect.ORDER, 500).add(Aspect.ENTROPY, 500);
	private static final AspectList costAll = (new AspectList()).add(Aspect.AIR, 10)
																.add(Aspect.FIRE, 10)
																.add(Aspect.EARTH, 10)
																.add(Aspect.ORDER, 10)
																.add(Aspect.ENTROPY, 10)
																.add(Aspect.WATER, 10);
	private static final FocusUpgradeType nightshade = new FocusUpgradeType(15, new ResourceLocation("thaumcraft", "textures/foci/nightshade.png"), "focus.upgrade.nightshade.name", "focus.upgrade.nightshade.text", (new AspectList()).add(Aspect.LIFE, 1));


	@Override
	public void registerIcons(IIconRegister ir) {
		this.icon = ir.registerIcon(DeathlyHallows.MODID + ":" + iconString);
		this.depthIcon = ir.registerIcon("thaumcraft:focus_pech_depth");
	}

	public ItemFocusInferioisMutandis() {
		// Thaumcraft naming style
		setUnlocalizedName("focus_mutandis");
		setTextureName("focus_mutandis");
		setCreativeTab(tabDeathlyHallows);
	}

	@Override
	public int getFocusColor(ItemStack itemstack) {
		return 0x00FF40;
	}
	
	@Override
	public IIcon getFocusDepthLayerIcon(ItemStack itemstack) {
		return depthIcon;
	}

	public boolean mutator = false;

	@Override
	public ItemStack onFocusRightClick(ItemStack stack, World world, EntityPlayer player, MovingObjectPosition mob) {
		if(world.isRemote) {
			return null;
		}
		ItemWandCasting wand = (ItemWandCasting)stack.getItem();
		if(player.isSneaking()) {
			mutator = !mutator;
			world.playSoundAtEntity(player, SoundEffect.RANDOM_ORB.toString(), 1, 1);
			return null;
		}
		// TODO attach extremis as update and architect
		// TODO fix rituals because of shift+click fails
		MovingObjectPosition mop = DHUtils.rayTrace(player, 4.0);
		if(mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || !wand.consumeAllVis(stack, player, getVisCost(stack), false, false)) {
			return null;
		}
		if(mutator && (new ItemMutator()).onItemUseFirst(new ItemStack(DHItems.inferioisMutandis), player, world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, 0, 0, 0)
			|| !mutator && Witchery.Items.GENERIC.useMutandis(false, Witchery.Items.GENERIC.itemMutandis.createStack(), player, world, mop.blockX, mop.blockY, mop.blockZ)
		) {
			wand.consumeAllVis(stack, player, getVisCost(stack), true, false);
			ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, world, mop.blockX, mop.blockY, mop.blockZ, 1.0, 1.0, 8);
		}
		return stack;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void addInformation(ItemStack stack, EntityPlayer player, List l, boolean devMode) {
		super.addInformation(stack, player, l, devMode);
		l.add(StatCollector.translateToLocal("dh.desc.mutandis1"));
		l.add(StatCollector.translateToLocal("dh.desc.mutandis2"));
		l.add(StatCollector.translateToLocal("dh.desc.mutandis3"));
	}

	public boolean hasEffect(final ItemStack par1ItemStack, final int pass) {
		return mutator;
	}

	@Override
	public AspectList getVisCost(ItemStack itemstack) {
		return isUpgradedWith(itemstack, nightshade) ? costAll : cost;
	}
	
}
