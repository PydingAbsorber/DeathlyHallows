package com.pyding.deathlyhallows.items.foci;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.MutableBlock;
import com.google.common.collect.Lists;
import com.pyding.deathlyhallows.DeathlyHallows;
import com.pyding.deathlyhallows.utils.DHID;
import com.pyding.deathlyhallows.utils.DHUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.BlockCoordinates;
import thaumcraft.api.IArchitect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.items.wands.foci.ItemFocusPech;

import java.util.ArrayList;
import java.util.List;

import static com.pyding.deathlyhallows.DeathlyHallows.tabDeathlyHallows;
import static thaumcraft.api.aspects.Aspect.*;
import static thaumcraft.api.wands.FocusUpgradeType.architect;
import static thaumcraft.api.wands.FocusUpgradeType.enlarge;
import static thaumcraft.api.wands.FocusUpgradeType.frugal;

public class ItemFocusInferioisMutandis extends ItemFocusPech implements IArchitect {
	private IIcon depthIcon = null;
	private static final AspectList costBasic = new AspectList()
			.add(ORDER, 125)
			.add(ENTROPY, 250)
			.add(WATER, 350)
			.add(EARTH, 350);
	private static final AspectList costExtremis = new AspectList()
			.add(ORDER, 250)
			.add(ENTROPY, 500)
			.add(WATER, 450)
			.add(EARTH, 450);
	private static final FocusUpgradeType extremis = new FocusUpgradeType(DHID.FOCUSUPGRADE_EXTREMIS,
			// TODO icon
			new ResourceLocation("thaumcraft", "textures/foci/nightshade.png"),
			"focus.upgrade.dh.extremis.name",
			"focus.upgrade.dh.extremis.text",
			new AspectList()
					.add(PLANT, 1)
					.add(EXCHANGE, 1)
					.add(ELDRITCH, 1)
	);

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
	public ItemStack onFocusRightClick(ItemStack stack, World world, EntityPlayer p, MovingObjectPosition mop) {
		if(world.isRemote) {
			return null;
		}
		ItemWandCasting wand = (ItemWandCasting)stack.getItem();
		boolean mutator = p.isSneaking();
		boolean extra = isUpgradedWith(wand.getFocusItem(stack), extremis);
		AspectList cost = mutator && extra ? costExtremis : costBasic;
		// reassign mop, reason:
		// thaumcraft uses mop that can't surpass water for action 
		// and vanilla minecraft (that can) for rendering
		// which makes some visual bug: with architect you see that you can surpass water, but actually you can't.
		mop = DHUtils.rayTrace(p);
		if(mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || !wand.consumeAllVis(stack, p, cost, false, false)) {
			return null;
		}
		if(mutator) {
			if(Witchery.Items.MUTATING_SPRIG.onItemUseFirst(stack, p, world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, 0, 0, 0)) {
				wand.consumeAllVis(stack, p, costBasic, true, false);
			}
			return stack;
		}
		ArrayList<BlockCoordinates> blocks = getArchitectBlocks(stack, world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, p);
		for(BlockCoordinates c: blocks) {
			if(!wand.consumeAllVis(stack, p, cost, false, false)) {
				break;
			}
			if(Witchery.Items.GENERIC.useMutandis(extra, Witchery.Items.GENERIC.itemMutandis.createStack(), p, world, c.x, c.y, c.z)) {
				wand.consumeAllVis(stack, p, cost, true, false);
			}
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
		return !type.equals(enlarge) || isUpgradedWith(stack, FocusUpgradeType.architect);
	}

	@Override
	public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack stack, int rank) {
		switch(rank) {
			case 1:
				return new FocusUpgradeType[]{frugal};
			case 2:
				return new FocusUpgradeType[]{frugal, architect};
			case 3:
				return new FocusUpgradeType[]{frugal, extremis, enlarge};
			case 4:
				return new FocusUpgradeType[]{frugal, enlarge};
			case 5:
				return new FocusUpgradeType[]{frugal, enlarge};
			default:
				return null;
		}
	}

	@Override
	public AspectList getVisCost(ItemStack stack) {
		return isUpgradedWith(stack, extremis) ? costExtremis : costBasic;
	}

	@Override
	public int getActivationCooldown(ItemStack stack) {
		return 150;
	}

	@Override
	public int getMaxAreaSize(ItemStack stack) {
		return 1 + getUpgradeLevel(stack, enlarge);
	}

	private final ArrayList<BlockCoordinates> checked = new ArrayList<>();

	@Override
	public ArrayList<BlockCoordinates> getArchitectBlocks(ItemStack stack, World world, int x, int y, int z, int side, EntityPlayer p) {
		ArrayList<BlockCoordinates> out = new ArrayList<>();
		if(p.isSneaking()) {
			return out;
		}
		ItemWandCasting wand = (ItemWandCasting)stack.getItem();
		ItemStack focus = wand.getFocusItem(stack);
		boolean extremis = isUpgradedWith(focus, ItemFocusInferioisMutandis.extremis);
		int type = getMutateType(extremis, p, world, x, y, z);
		if(type == 0) {
			return out;
		}
		int sizeX = 0, sizeY = 0, sizeZ = 0;
		if(isUpgradedWith(focus, architect)) {
			sizeX = WandManager.getAreaX(stack);
			sizeY = WandManager.getAreaY(stack);
			sizeZ = WandManager.getAreaZ(stack);
		}
		checked.clear();
		if(side != 2 && side != 3) {
			checkNeighbours(world, x, y, z, new BlockCoordinates(x, y, z), side, sizeX, sizeY, sizeZ, out, p, extremis, type);
		}
		else {
			checkNeighbours(world, x, y, z, new BlockCoordinates(x, y, z), side, sizeZ, sizeY, sizeX, out, p, extremis, type);
		}
		return out;
	}

	public void checkNeighbours(World world, int x, int y, int z, BlockCoordinates pos, int side, int sizeX, int sizeY, int sizeZ, ArrayList<BlockCoordinates> list, EntityPlayer p, boolean extremis, int type) {
		if(checked.contains(pos)) {
			return;
		}
		checked.add(pos);
		switch(side) {
			case 0:
			case 1:
				if(Math.abs(pos.x - x) > sizeX) {
					return;
				}

				if(Math.abs(pos.z - z) > sizeZ) {
					return;
				}

				if(Math.abs(pos.y - y) > sizeY) {
					return;
				}
				break;
			case 2:
			case 3:
				if(Math.abs(pos.x - x) > sizeX) {
					return;
				}

				if(Math.abs(pos.y - y) > sizeZ) {
					return;
				}

				if(Math.abs(pos.z - z) > sizeY) {
					return;
				}
				break;
			case 4:
			case 5:
				if(Math.abs(pos.y - y) > sizeX) {
					return;
				}

				if(Math.abs(pos.z - z) > sizeZ) {
					return;
				}

				if(Math.abs(pos.x - x) > sizeY) {
					return;
				}
		}
		if(!world.isAirBlock(pos.x, pos.y, pos.z) && (type == getMutateType(extremis, p, world, pos.x, pos.y, pos.z))) {
			list.add(pos);
			for(ForgeDirection dir: ForgeDirection.VALID_DIRECTIONS) {
				BlockCoordinates cc = new BlockCoordinates(pos.x + dir.offsetX, pos.y + dir.offsetY, pos.z + dir.offsetZ);
				checkNeighbours(world, x, y, z, cc, side, sizeX, sizeY, sizeZ, list, p, extremis, type);
			}
		}
	}

	byte getMutateType(boolean extremis, EntityPlayer p, World w, int x, int y, int z) {
		Block block = w.getBlock(x, y, z);
		boolean
				isPlant = isValidPlant(extremis, p.dimension, block, w.getBlockMetadata(x, y, z)),
				isBlock = isValidBlock(extremis, block, w.getBlock(x, y + 1, z));
		if(isPlant) {
			return 1;
		}
		if(isBlock) {
			return 2;
		}
		return 0;
	}

	private boolean isValidPlant(boolean extremis, int dimension, Block block, int metadata) {
		ArrayList<MutableBlock> list;
		if(block == Blocks.flower_pot && metadata > 0) {
			list = Lists.newArrayList(
					new MutableBlock(Blocks.flower_pot, 1),
					new MutableBlock(Blocks.flower_pot, 2),
					new MutableBlock(Blocks.flower_pot, 3),
					new MutableBlock(Blocks.flower_pot, 4),
					new MutableBlock(Blocks.flower_pot, 5),
					new MutableBlock(Blocks.flower_pot, 6),
					new MutableBlock(Blocks.flower_pot, 7),
					new MutableBlock(Blocks.flower_pot, 8),
					new MutableBlock(Blocks.flower_pot, 9),
					new MutableBlock(Blocks.flower_pot, 10),
					new MutableBlock(Blocks.flower_pot, 11)
			);
		}
		else {
			list = Lists.newArrayList(
					new MutableBlock(Blocks.sapling, 0),
					new MutableBlock(Blocks.sapling, 1),
					new MutableBlock(Blocks.sapling, 2),
					new MutableBlock(Blocks.sapling, 3),
					new MutableBlock(Blocks.sapling, 4),
					new MutableBlock(Blocks.sapling, 5),
					new MutableBlock(Witchery.Blocks.SAPLING, 0),
					new MutableBlock(Witchery.Blocks.SAPLING, 1),
					new MutableBlock(Witchery.Blocks.SAPLING, 2),
					new MutableBlock(Witchery.Blocks.EMBER_MOSS, 0),
					new MutableBlock(Blocks.tallgrass, 1),
					new MutableBlock(Blocks.waterlily),
					new MutableBlock(Blocks.brown_mushroom),
					new MutableBlock(Blocks.red_mushroom),
					new MutableBlock(Blocks.red_flower, 0),
					new MutableBlock(Blocks.yellow_flower),
					new MutableBlock(Witchery.Blocks.SPANISH_MOSS, 1)
			);
			for(String extra: Config.instance().mutandisExtras) {
				try {
					list.add(new MutableBlock(extra));
				}
				catch(Throwable ignored) {
				}
			}
			if(extremis) {
				list.addAll(Lists.newArrayList(
						new MutableBlock(Blocks.carrots, -1, Math.min(metadata, 7)),
						new MutableBlock(Blocks.potatoes, -1, Math.min(metadata, 7)),
						new MutableBlock(Blocks.wheat, -1, Math.min(metadata, 7)),
						new MutableBlock(Blocks.reeds, -1, Math.min(metadata, 7)),
						new MutableBlock(Witchery.Blocks.CROP_BELLADONNA, -1, Math.min(metadata, Witchery.Blocks.CROP_BELLADONNA.getNumGrowthStages())),
						new MutableBlock(Witchery.Blocks.CROP_MANDRAKE, -1, Math.min(metadata, Witchery.Blocks.CROP_MANDRAKE.getNumGrowthStages())),
						new MutableBlock(Witchery.Blocks.CROP_ARTICHOKE, -1, Math.min(metadata, Witchery.Blocks.CROP_ARTICHOKE.getNumGrowthStages())),
						new MutableBlock(Blocks.pumpkin_stem, -1, Math.min(metadata, 7)),
						new MutableBlock(Blocks.cactus),
						new MutableBlock(Blocks.melon_stem, -1, Math.min(metadata, 7)),
						new MutableBlock(Blocks.nether_wart, -1, Math.min(metadata, 3))
				));
			}
			else if(dimension == Config.instance().dimensionDreamID) {
				list.addAll(Lists.newArrayList(new MutableBlock(Blocks.nether_wart, -1, 3)));
			}
		}

		MutableBlock mutableBlock = new MutableBlock(block, metadata, 0);
		return list.contains(mutableBlock);
	}

	private boolean isValidBlock(boolean extremis, Block b, Block a) {
		if(!extremis) {
			return false;
		}
		return b == Blocks.grass || b == Blocks.mycelium || b == Blocks.dirt && (a == Blocks.water || a == Blocks.flowing_water);
	}

	@Override
	public boolean showAxis(ItemStack stack, World world, EntityPlayer p, int side, EnumAxis axis) {
		int dim = WandManager.getAreaDim(stack);
		if(dim == 0) {
			return true;
		}
		switch(side) {
			case 0:
			case 1:
				if(axis == EnumAxis.X && dim == 1 || axis == EnumAxis.Z && dim == 2 || axis == EnumAxis.Y && dim == 3) {
					return true;
				}
				break;
			case 2:
			case 3:
				if(axis == EnumAxis.Y && dim == 1 || axis == EnumAxis.X && dim == 2 || axis == EnumAxis.Z && dim == 3) {
					return true;
				}
				break;
			case 4:
			case 5:
				if(axis == EnumAxis.Y && dim == 1 || axis == EnumAxis.Z && dim == 2 || axis == EnumAxis.X && dim == 3) {
					return true;
				}
		}
		return false;
	}

}
