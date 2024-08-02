package com.pyding.deathlyhallows.items.foci;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.items.DHItems;
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
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.foci.ItemFocusPech;

import java.util.List;

import static com.pyding.deathlyhallows.DeathlyHallows.tabDeathlyHallows;
import static thaumcraft.api.wands.FocusUpgradeType.architect;
import static thaumcraft.api.wands.FocusUpgradeType.enlarge;
import static thaumcraft.api.wands.FocusUpgradeType.extend;
import static thaumcraft.api.wands.FocusUpgradeType.frugal;
import static thaumcraft.api.wands.FocusUpgradeType.potency;

public class ItemFocusInferioisMutandis extends ItemFocusPech {
	private IIcon depthIcon = null;
	private static final AspectList cost = (new AspectList()).add(Aspect.ORDER, 500).add(Aspect.ENTROPY, 500);
	private static final AspectList costAll = (new AspectList()).add(Aspect.AIR, 10)
																.add(Aspect.FIRE, 10)
																.add(Aspect.EARTH, 10)
																.add(Aspect.ORDER, 10)
																.add(Aspect.ENTROPY, 10)
																.add(Aspect.WATER, 10);
	private static final FocusUpgradeType extremis = new FocusUpgradeType(15, new ResourceLocation("thaumcraft", "textures/foci/nightshade.png"), "focus.upgrade.nightshade.name", "focus.upgrade.nightshade.text", (new AspectList()).add(Aspect.LIFE, 1));

	@Override
	public String getSortingHelper(ItemStack focus) {
		return "DH" + super.getSortingHelper(focus);
	}

	@Override
	public void registerIcons(IIconRegister ir) {
		icon = ir.registerIcon(DeathlyHallows.MODID + ":" + iconString);
		// TODO icon
		depthIcon = ((ItemFocusBasic)ConfigItems.itemFocusPech).getFocusDepthLayerIcon(null);
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

	@Override
	public ItemStack onFocusRightClick(ItemStack stack, World world, EntityPlayer player, MovingObjectPosition mob) {
		if(world.isRemote) {
			return null;
		}
		ItemWandCasting wand = (ItemWandCasting)stack.getItem();
		// TODO attach extremis as update and architect
		MovingObjectPosition mop = DHUtils.rayTrace(player, 4.0);
		if(mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || !wand.consumeAllVis(stack, player, getVisCost(stack), false, false)) {
			return null;
		}
		boolean mutator = player.isSneaking();
		if(mutator && Witchery.Items.MUTATING_SPRIG.onItemUseFirst(new ItemStack(DHItems.inferioisMutandis), player, world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, 0, 0, 0)
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

	public boolean canApplyUpgrade(ItemStack stack, EntityPlayer p, FocusUpgradeType type, int rank) {
		return !type.equals(extend) || isUpgradedWith(stack, FocusUpgradeType.architect);
	}

	@Override
	public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack stack, int rank) {
		switch(rank) {
			case 1:
				return new FocusUpgradeType[]{frugal, architect};
			case 2:
				return new FocusUpgradeType[]{frugal, enlarge};
			case 3:
				return new FocusUpgradeType[]{frugal, extremis};
			case 4:
				return new FocusUpgradeType[]{frugal, extend};
			case 5:
				return new FocusUpgradeType[]{frugal, potency};
			default:
				return null;
		}
	}

	@Override
	public AspectList getVisCost(ItemStack stack) {
		return isUpgradedWith(stack, extremis) ? costAll : cost;
	}

	@Override
	public int getActivationCooldown(ItemStack stack) {
		return 150;
	}

}
