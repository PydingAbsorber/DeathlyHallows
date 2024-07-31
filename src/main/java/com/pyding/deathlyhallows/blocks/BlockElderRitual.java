package com.pyding.deathlyhallows.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.blocks.BlockGrassper;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.ritual.RitualStep.Result;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.google.common.collect.Lists;
import com.pyding.deathlyhallows.rituals.ElderRiteRegistry;
import com.pyding.deathlyhallows.rituals.rites.ElderRitualStep;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Random;

import static com.emoniph.witchery.ritual.RitualStep.Result.ABORTED_REFUND;
import static com.emoniph.witchery.ritual.RitualStep.Result.STARTING;
import static com.emoniph.witchery.ritual.RitualStep.Result.UPKEEP;

public class BlockElderRitual extends BlockBase implements ITileEntityProvider {

	public BlockElderRitual() {
		super("elderRitualRune", Material.vine, null);
		setHardness(3.0f);
		setResistance(1000.0f);
		setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.015625f, 1.0f);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return blockIcon;
	}

	public void onBlockClicked(World world, int posX, int posY, int posZ, EntityPlayer player) {
		if(world.isRemote) {
			return;
		}
		ItemStack itemstack = player.getHeldItem();
		if(itemstack != null && (Witchery.Items.GENERIC.itemBroom.isMatch(itemstack) || Witchery.Items.GENERIC.itemBroomEnchanted.isMatch(itemstack))) {
			world.func_147480_a(posX, posY, posZ, false);
		}
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int quantityDropped(Random rand) {
		return 0;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer p) {
		return new ItemStack(Witchery.Items.CHALK_GOLDEN);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if(!canStayOrRemove(world, x, y, z)) {
			return;
		}
		boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z);
		BlockCircle.TileEntityCircle tileCircle = BlockUtil.getTileEntity(world, x, y, z, BlockCircle.TileEntityCircle.class);
		if(tileCircle == null || tileCircle.previousRedstoneState == flag) {
			return;
		}
		if(flag) {
			activateBlock(world, x, y, z, null, false);
		}
		tileCircle.previousRedstoneState = flag;
	}

	private boolean canStayOrRemove(World world, int x, int y, int z) {
		if(canBlockStay(world, x, y, z)) {
			return true;
		}
		world.setBlockToAir(x, y, z);
		return false;
	}

	public boolean canBlockStay(World world, int x, int y, int z) {
		Material material = world.getBlock(x, y - 1, z).getMaterial();
		return !world.isAirBlock(x, y - 1, z) && material != null && material.isOpaque() && material.isSolid();
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
		return side == ForgeDirection.UP.ordinal();
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		int metadata = world.getBlockMetadata(x, y, z);
		if(metadata == 1) {
			world.spawnParticle(
					ParticleEffect.REDDUST.toString(),
					x + 0.4f + rand.nextFloat() * 0.2f,
					y + 0.1f + rand.nextFloat() * 0.3f,
					z + 0.4f + rand.nextFloat() * 0.2f,
					0.0, 0.0, 0.0
			);
		}
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem();
		activateBlock(world, x, y, z, player, stack != null && Witchery.Items.GENERIC.itemSeerStone.isMatch(stack));
		return true;
	}

	private void activateBlock(World world, int posX, int posY, int posZ, EntityPlayer player, boolean summonCoven) {
		BlockElderRitual.TileEntityCircle tileEntity = BlockUtil.getTileEntity(world, posX, posY, posZ, BlockElderRitual.TileEntityCircle.class);
		if(tileEntity == null) {
			return;
		}
		if(tileEntity.isRitualActive()) {
			tileEntity.deactivate();
			return;
		}

		if(world.isRemote) {
			return;
		}
		if(PowerSources.instance().isAreaNulled(world, posX, posY, posZ) || world.provider.dimensionId == Config.instance().dimensionDreamID) {
			ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.rite.nullfield");
			SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
			return;
		}

		boolean isDaytime2 = world.isDaytime();
		boolean canThunder = world.getBiomeGenForCoords(posX, posZ).canSpawnLightningBolt();
		boolean isRaining2 = world.isRaining() && canThunder;
		boolean isThundering2 = world.isThundering();
		int maxRadius = 7; // PATTERN.length / 2;
		AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(posX - maxRadius, posY - maxRadius, posZ - maxRadius, posX + maxRadius, posY + maxRadius, posZ + maxRadius);
		@SuppressWarnings("unchecked")
		ArrayList<Entity> entities = (ArrayList<Entity>)Lists.newArrayList(world.getEntitiesWithinAABB(Entity.class, bounds));
		ArrayList<ItemStack> grassperStacks = new ArrayList<>();
		for(int x = posX - 5; x <= posX + 5; ++x) {
			for(int z = posZ - 5; z <= posZ + 5; ++z) {
				Block block = world.getBlock(x, posY, z);
				if(block != Witchery.Blocks.GRASSPER) {
					continue;
				}
				TileEntity tile = world.getTileEntity(x, posY, z);
				if(!(tile instanceof BlockGrassper.TileEntityGrassper)) {
					continue;
				}
				BlockGrassper.TileEntityGrassper ritual = (BlockGrassper.TileEntityGrassper)tile;
				ItemStack stack = ritual.getStackInSlot(0);
				if(stack != null) {
					grassperStacks.add(stack);
				}
			}
		}
		boolean success = false;
		int covenSize = summonCoven ? EntityCovenWitch.getCovenSize(player) : 0;
		for(ElderRiteRegistry.Ritual o: ElderRiteRegistry.instance().getRituals()) {
			if(!o.isMatch(world, posX, posY, posZ, entities, grassperStacks, isDaytime2, isRaining2, isThundering2)) {
				continue;
			}
			tileEntity.queueRitual(o, bounds, player, covenSize, summonCoven);
			summonCoven = false;
			success = true;
				
		}
		if(!success && !world.isRemote) {
			RiteRegistry.RiteError("witchery.rite.unknownritual", player, world);
			SoundEffect.NOTE_SNARE.playAt(world, posX, posY, posZ);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new BlockElderRitual.TileEntityCircle();
	}

	public static class TileEntityCircle extends TileEntityBase {
		private final ArrayList<BlockElderRitual.TileEntityCircle.ActivatedElderRitual> activeRituals;
		private final ArrayList<BlockElderRitual.TileEntityCircle.ActivatedElderRitual> upkeepRituals;
		private boolean abortNext;

		public TileEntityCircle() {
			activeRituals = new ArrayList<>();
			upkeepRituals = new ArrayList<>();
			abortNext = false;
		}

		public void writeToNBT(NBTTagCompound tag) {
			super.writeToNBT(tag);
			int[] ritualIDs = new int[upkeepRituals.size()];
			byte[] stages = new byte[upkeepRituals.size()];
			byte[] covenSizes = new byte[upkeepRituals.size()];
			NBTTagList tagList = new NBTTagList();
			NBTTagList locationList = new NBTTagList();
			for(int i = 0; i < upkeepRituals.size(); ++i) {
				ActivatedElderRitual ritual = upkeepRituals.get(i);
				ritualIDs[i] = ritual.ritual.getRitualID();
				stages[i] = (byte)ritual.getCurrentStage();
				covenSizes[i] = (byte)ritual.covenSize;
				tagList.appendTag(new NBTTagString(ritual.getInitiatingPlayerName()));
				locationList.appendTag(ritual.getLocationTag());
			}
			tag.setIntArray("Rituals", ritualIDs);
			tag.setByteArray("RitualStages", stages);
			tag.setTag("Initiators", tagList);
			tag.setTag("Locations", locationList);
			tag.setByteArray("RitualCovens", covenSizes);
		}

		public void readFromNBT(NBTTagCompound tag) {
			super.readFromNBT(tag);
			if(!tag.hasKey("Rituals") || !tag.hasKey("RitualStages")) {
				return;
			}
			byte[] stages = tag.getByteArray("RitualStages");
			int[] ritualIDs = tag.getIntArray("Rituals");
			Coord[] locations = new Coord[stages.length];
			if(tag.hasKey("Locations")) {
				NBTTagList initators = tag.getTagList("Locations", 10);
				for(int covenSizes = 0; covenSizes < Math.min(initators.tagCount(), locations.length); ++covenSizes) {
					NBTTagCompound i = initators.getCompoundTagAt(covenSizes);
					locations[covenSizes] = Coord.fromTagNBT(i);
				}
			}
			String[] s = new String[stages.length];
			if(tag.hasKey("Initiators")) {
				NBTTagList initiators = tag.getTagList("Initiators", 8);
				for(int i = 0; i < Math.min(initiators.tagCount(), s.length); ++i) {
					String ritual = initiators.getStringTagAt(i);
					s[i] = ((ritual != null && !ritual.isEmpty()) ? ritual : null);
				}
			}
			byte[] covens = tag.hasKey("RitualCovens") ? tag.getByteArray("RitualCovens") : null;
			for(int i = 0; i < ritualIDs.length; ++i) {
				ElderRiteRegistry.Ritual ritual = ElderRiteRegistry.instance().getRitual(ritualIDs[i]);
				if(ritual == null) {
					continue;
				}
				ArrayList<RitualStep> ritualSteps = new ArrayList<>();
				ritual.addRiteSteps(ritualSteps, stages[i]);
				if(ritualSteps.isEmpty()) {
					continue;
				}
				BlockElderRitual.TileEntityCircle.ActivatedElderRitual ActivatedElderRitual = new BlockElderRitual.TileEntityCircle.ActivatedElderRitual(ritual, ritualSteps, s[i], (covens != null) ? covens[i] : 0, null);
				ActivatedElderRitual.setLocation(locations[i]);
				upkeepRituals.add(ActivatedElderRitual);
			}
		}

		@Override
		public void updateEntity() {
			super.updateEntity();
			if(worldObj.isRemote) {
				return;
			}
			if(!upkeepRituals.isEmpty()) {
				upkeepRituals();
			}
			if(!activeRituals.isEmpty()) {
				activeRituals();
			}
			if(!isRitualActive() && getBlockMetadata() != 0) {
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
			}
		}

		private void activeRituals() {
			ActivatedElderRitual ritual = activeRituals.get(0);
			Result stepResult = ((ElderRitualStep)ritual.steps.get(0)).elderRun(worldObj, xCoord, yCoord, zCoord, ticks, ritual);
			ritual.postProcess(worldObj);
			if(abortNext) {
				abortNext = false;
				stepResult = ABORTED_REFUND;
				activeRituals.clear();
			}
			if(stepResult != STARTING && Config.instance().traceRites()) {
				Log.instance()
				   .traceRite(String.format("Active ritual=%s, step=%s, result=%s", ritual.ritual.getUnlocalizedName(), ritual.steps.get(0)
																																	.toString(), stepResult.toString()));
			}
			switch(stepResult) {
				case COMPLETED: {
					if(ritual.steps.size() > 0) {
						ritual.steps.remove(0);
					}
					if(ritual.steps.isEmpty()) {
						activeRituals.remove(0);
						break;
					}
					break;
				}
				case ABORTED:
				case ABORTED_REFUND: {
					if(activeRituals.size() > 0) {
						activeRituals.remove(0);
					}
					if(worldObj.isRemote || stepResult != ABORTED_REFUND) {
						break;
					}
					SoundEffect.NOTE_SNARE.playAt(this);
					for(RitualStep.SacrificedItem sacrificedItem: ritual.sacrificedItems) {
						worldObj.spawnEntityInWorld(new EntityItem(worldObj, 0.5 + sacrificedItem.location.x, 0.5 + sacrificedItem.location.y, 0.5 + sacrificedItem.location.z, sacrificedItem.itemstack));
					}
					break;
				}
				case UPKEEP: {
					if(activeRituals.size() > 0) {
						activeRituals.remove(0);
					}
					upkeepRituals.add(ritual);
					break;
				}
			}
		}

		private void upkeepRituals() {
			for(ActivatedElderRitual result: upkeepRituals) {
				Result stepResult = ((ElderRitualStep)result.steps.get(0)).elderRun(worldObj, xCoord, yCoord, zCoord, ticks, result);
				if(stepResult != UPKEEP && Config.instance().traceRites()) {
					Log.instance()
					   .traceRite(String.format(" - Upkeep ritual=%s, step=%s, result=%s", result.ritual.getUnlocalizedName(), result.steps.get(0)
																																		   .toString(), stepResult.toString()));
				}
				switch(stepResult) {
					case COMPLETED: {
						result.steps.clear();
						continue;
					}
					case ABORTED:
					case ABORTED_REFUND: {
						result.steps.clear();
						SoundEffect.NOTE_SNARE.playAt(this);
						continue;
					}
				}
			}
			for(int i = upkeepRituals.size() - 1; i >= 0; --i) {
				if(upkeepRituals.get(i).steps.isEmpty()) {
					upkeepRituals.remove(i);
				}
			}
		}

		public void deactivate() {
			if(worldObj.isRemote) {
				return;
			}
			if(activeRituals.size() > 0) {
				abortNext = true;
			}
			upkeepRituals.clear();
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
			SoundEffect.NOTE_SNARE.playAt(this);
		}

		public boolean isRitualActive() {
			return !activeRituals.isEmpty() || !upkeepRituals.isEmpty();
		}

		public void queueRitual(ElderRiteRegistry.Ritual ritual, AxisAlignedBB bounds, EntityPlayer player, int covenSize, boolean summonCoven) {
			ArrayList<RitualStep> ritualSteps = new ArrayList<>();
			if(summonCoven) {
				EntityCovenWitch.summonCoven(ritualSteps, player.worldObj, player, new int[][]{{xCoord - 2, yCoord, zCoord - 2}, {xCoord + 2, yCoord, zCoord - 2}, {xCoord - 2, yCoord, zCoord + 2}, {xCoord + 2, yCoord, zCoord + 2}, {xCoord, yCoord, zCoord + 3}, {xCoord, yCoord, zCoord - 3}});
			}
			ritual.addSteps(ritualSteps, bounds);
			if(!ritualSteps.isEmpty() && !worldObj.isRemote) {
				activeRituals.add(new BlockElderRitual.TileEntityCircle.ActivatedElderRitual(ritual, ritualSteps, (player != null) ? player.getCommandSenderName() : null, covenSize, null));
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3);
			}
		}

		public Packet getDescriptionPacket() {
			NBTTagCompound nbtTag = new NBTTagCompound();
			writeToNBT(nbtTag);
			return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbtTag);
		}

		public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
			super.onDataPacket(net, packet);
			readFromNBT(packet.func_148857_g());
			worldObj.func_147479_m(xCoord, yCoord, zCoord);
		}

		public static class ActivatedElderRitual {
			public final ElderRiteRegistry.Ritual ritual;
			private final ArrayList<RitualStep> steps;
			public final String playerName;
			public final ArrayList<RitualStep.SacrificedItem> sacrificedItems;
			public final int covenSize;
			private Coord coord;

			private ActivatedElderRitual(ElderRiteRegistry.Ritual ritual, ArrayList<RitualStep> steps, String playerName, int covenSize) {
				sacrificedItems = new ArrayList<>();
				this.ritual = ritual;
				this.steps = steps;
				this.playerName = playerName;
				this.covenSize = covenSize;
			}

			public Coord getLocation() {
				return coord;
			}

			public NBTTagCompound getLocationTag() {
				return (coord != null) ? coord.toTagNBT() : new NBTTagCompound();
			}

			public void setLocation(Coord coord) {
				this.coord = coord;
			}

			public String getInitiatingPlayerName() {
				return (playerName != null) ? playerName : "";
			}

			public EntityPlayer getInitiatingPlayer(World world) {
				return world.getPlayerEntityByName(getInitiatingPlayerName());
			}

			public void postProcess(World world) {
				for(int i = 0; i < sacrificedItems.size(); ++i) {
					RitualStep.SacrificedItem sacrificedItem = sacrificedItems.get(i);
					if(sacrificedItem != null && sacrificedItem.itemstack != null) {
						if(!ritual.isConsumeAttunedStoneCharged() && Witchery.Items.GENERIC.itemAttunedStoneCharged.isMatch(sacrificedItem.itemstack)) {
							world.spawnEntityInWorld(new EntityItem(world, 0.5 + sacrificedItem.location.x, 0.5 + sacrificedItem.location.y, 0.5 + sacrificedItem.location.z, Witchery.Items.GENERIC.itemAttunedStone.createStack()));
							sacrificedItems.remove(i);
							break;
						}
						if(sacrificedItem.itemstack.getItem() == Witchery.Items.ARTHANA) {
							world.spawnEntityInWorld(new EntityItem(world, 0.5 + sacrificedItem.location.x, 0.5 + sacrificedItem.location.y, 0.5 + sacrificedItem.location.z, sacrificedItem.itemstack));
							sacrificedItems.remove(i);
							break;
						}
						if(sacrificedItem.itemstack.getItem() == Witchery.Items.BOLINE) {
							world.spawnEntityInWorld(new EntityItem(world, 0.5 + sacrificedItem.location.x, 0.5 + sacrificedItem.location.y, 0.5 + sacrificedItem.location.z, sacrificedItem.itemstack));
							sacrificedItems.remove(i);
							break;
						}
						if(!ritual.isConsumeNecroStone() && Witchery.Items.GENERIC.itemNecroStone.isMatch(sacrificedItem.itemstack)) {
							world.spawnEntityInWorld(new EntityItem(world, 0.5 + sacrificedItem.location.x, 0.5 + sacrificedItem.location.y, 0.5 + sacrificedItem.location.z, sacrificedItem.itemstack));
							sacrificedItems.remove(i);
							break;
						}
					}
				}
			}

			public int getCurrentStage() {
				return steps.isEmpty() ? 0 : steps.get(0).getCurrentStage();
			}

			ActivatedElderRitual(ElderRiteRegistry.Ritual x0, ArrayList<RitualStep> x1, String x2, int x3, Result x4) {
				this(x0, x1, x2, x3);
			}
		}
	}

}