package com.pyding.deathlyhallows.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.blocks.BlockGrassper;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
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

public class BlockElderRitual extends BlockBaseContainer {

	public BlockElderRitual() {
		super(Material.vine, BlockElderRitual.TileEntityCircle.class);
		registerWithCreateTab = false;
		setHardness(3.0f);
		setResistance(1000.0f);
		setBlockName("elderRitualRune");
		setBlockTextureName("dh:ritual");
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
			double d0 = x + 0.4f + rand.nextFloat() * 0.2f;
			double d2 = y + 0.1f + rand.nextFloat() * 0.3f;
			double d3 = z + 0.4f + rand.nextFloat() * 0.2f;
			world.spawnParticle(ParticleEffect.REDDUST.toString(), d0, d2, d3, 0.0, 0.0, 0.0);
		}
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem();
		activateBlock(world, x, y, z, player, stack != null && Witchery.Items.GENERIC.itemSeerStone.isMatch(stack));
		return true;
	}

	private void activateBlock(World world, int posX, int posY, int posZ, EntityPlayer player, boolean summonCoven) {
		BlockElderRitual.TileEntityCircle tileEntity = BlockUtil.getTileEntity(world, posX, posY, posZ, BlockElderRitual.TileEntityCircle.class);
		if(tileEntity != null) {
			if(tileEntity.isRitualActive()) {
				tileEntity.deactivate();
			}
			else if(!world.isRemote) {
				if(!PowerSources.instance()
								.isAreaNulled(world, posX, posY, posZ) && world.provider.dimensionId != Config.instance().dimensionDreamID) {
//                     Circle a = new Circle(16);
//                     Circle b = new Circle(28);
//                     Circle c = new Circle(40);
//                     Circle d = new Circle(0);
//                     Circle[][] PATTERN = { { d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d }, { d, d, d, d, d, c, c, c, c, c, c, c, d, d, d, d, d }, { d, d, d, d, c, d, d, d, d, d, d, d, c, d, d, d, d }, { d, d, d, c, d, d, b, b, b, b, b, d, d, c, d, d, d }, { d, d, c, d, d, b, d, d, d, d, d, b, d, d, c, d, d }, { d, c, d, d, b, d, d, a, a, a, d, d, b, d, d, c, d }, { d, c, d, b, d, d, a, d, d, d, a, d, d, b, d, c, d }, { d, c, d, b, d, a, d, d, d, d, d, a, d, b, d, c, d }, { d, c, d, b, d, a, d, d, d, d, d, a, d, b, d, c, d }, { d, c, d, b, d, a, d, d, d, d, d, a, d, b, d, c, d }, { d, c, d, b, d, d, a, d, d, d, a, d, d, b, d, c, d }, { d, c, d, d, b, d, d, a, a, a, d, d, b, d, d, c, d }, { d, d, c, d, d, b, d, d, d, d, d, b, d, d, c, d, d }, { d, d, d, c, d, d, b, b, b, b, b, d, d, c, d, d, d }, { d, d, d, d, c, d, d, d, d, d, d, d, c, d, d, d, d }, { d, d, d, d, d, c, c, c, c, c, c, c, d, d, d, d, d }, { d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d } };
//                     int offsetZ = (PATTERN.length - 1) / 2;
//                    for (int isDaytime = 0; isDaytime < PATTERN.length - 1; ++isDaytime) {
//                         int isRainPossible = posZ - offsetZ + isDaytime;
//                         int isRaining = (PATTERN[isDaytime].length - 1) / 2;
//                        for (int isThundering = 0; isThundering < PATTERN[isDaytime].length; ++isThundering) {
//                             int maxRadius = posX - isRaining + isThundering;
//                            PATTERN[PATTERN.length - 1 - isDaytime][isThundering].addGlyph(world, maxRadius, posY, isRainPossible);
//                        }
//                    }
					boolean isDaytime2 = world.isDaytime();
					boolean var30 = world.getBiomeGenForCoords(posX, posZ).canSpawnLightningBolt();
					boolean isRaining2 = world.isRaining() && var30;
					boolean isThundering2 = world.isThundering();
					int maxRadius = 7;//PATTERN.length / 2;
					AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(posX - maxRadius, posY - maxRadius, posZ - maxRadius, posX + maxRadius, posY + maxRadius, posZ + maxRadius);
					@SuppressWarnings("unchecked")
					ArrayList<Entity> entities = (ArrayList<Entity>)Lists.newArrayList(world.getEntitiesWithinAABB(Entity.class, bounds));
					ArrayList<ItemStack> grassperStacks = new ArrayList<ItemStack>();
					boolean var31 = true;
					for(int x = posX - 5; x <= posX + 5; ++x) {
						for(int z = posZ - 5; z <= posZ + 5; ++z) {
							Block block = world.getBlock(x, posY, z);
							if(block == Witchery.Blocks.GRASSPER) {
								TileEntity tile = world.getTileEntity(x, posY, z);
								if(tile instanceof BlockGrassper.TileEntityGrassper) {
									BlockGrassper.TileEntityGrassper ritual = (BlockGrassper.TileEntityGrassper)tile;
									ItemStack stack = ritual.getStackInSlot(0);
									if(stack != null) {
										grassperStacks.add(stack);
									}
								}
							}
						}
					}
					// Circle[] nearbyCircles = { a, b, c };
					boolean success = false;
					int covenSize = summonCoven ? EntityCovenWitch.getCovenSize(player) : 0;
					for(ElderRiteRegistry.Ritual o: ElderRiteRegistry.instance().getRituals()) {
						if(o.isMatch(world, posX, posY, posZ, entities, grassperStacks, isDaytime2, isRaining2, isThundering2)) {
							/*if (ritual2.rite instanceof RiteWeatherCallStorm || ritual2.rite instanceof RiteEclipse) {
								 ExtendedPlayer exPlayer = ExtendedPlayer.get(player);
							}*/
							tileEntity.queueRitual(o, bounds, player, covenSize, summonCoven);
							summonCoven = false;
							success = true;
						}
					}
					if(!success && !world.isRemote) {
						RiteRegistry.RiteError("witchery.rite.unknownritual", player, world);
						SoundEffect.NOTE_SNARE.playAt(world, posX, posY, posZ);
					}
				}
				else {
					ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.rite.nullfield");
					SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
				}
			}
		}
	}

	static class SyntheticClass_1 {
		// TODO map enum back
		static final int[] $SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result;

		static {
			$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result = new int[RitualStep.Result.values().length];
			try {
				SyntheticClass_1.$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[RitualStep.Result.COMPLETED.ordinal()] = 1;
			}
			catch(NoSuchFieldError noSuchFieldError) {
			}
			try {
				SyntheticClass_1.$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[RitualStep.Result.ABORTED.ordinal()] = 2;
			}
			catch(NoSuchFieldError noSuchFieldError2) {
			}
			try {
				SyntheticClass_1.$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[RitualStep.Result.ABORTED_REFUND.ordinal()] = 3;
			}
			catch(NoSuchFieldError noSuchFieldError3) {
			}
			try {
				SyntheticClass_1.$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[RitualStep.Result.UPKEEP.ordinal()] = 4;
			}
			catch(NoSuchFieldError noSuchFieldError4) {
			}
			try {
				SyntheticClass_1.$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[RitualStep.Result.STARTING.ordinal()] = 5;
			}
			catch(NoSuchFieldError noSuchFieldError5) {
			}
		}
	}

	public static class TileEntityCircle extends TileEntityBase {
		public boolean previousRedstoneState;
		private final ArrayList<BlockElderRitual.TileEntityCircle.ActivatedElderRitual> activeRituals;
		private final ArrayList<BlockElderRitual.TileEntityCircle.ActivatedElderRitual> upkeepRituals;
		private boolean abortNext;

		public TileEntityCircle() {
			activeRituals = new ArrayList<BlockElderRitual.TileEntityCircle.ActivatedElderRitual>();
			upkeepRituals = new ArrayList<BlockElderRitual.TileEntityCircle.ActivatedElderRitual>();
			abortNext = false;
		}

		public void writeToNBT(NBTTagCompound nbtTag) {
			super.writeToNBT(nbtTag);
			int[] ritualIDs = new int[upkeepRituals.size()];
			byte[] stages = new byte[upkeepRituals.size()];
			byte[] covenSizes = new byte[upkeepRituals.size()];
			NBTTagList nbtList = new NBTTagList();
			NBTTagList nbtLocationList = new NBTTagList();
			for(int i = 0; i < upkeepRituals.size(); ++i) {
				ritualIDs[i] = upkeepRituals.get(i).ritual.getRitualID();
				stages[i] = (byte)upkeepRituals.get(i).getCurrentStage();
				covenSizes[i] = (byte)upkeepRituals.get(i).covenSize;
				nbtList.appendTag(new NBTTagString(upkeepRituals.get(i).getInitiatingPlayerName()));
				nbtLocationList.appendTag(upkeepRituals.get(i).getLocationTag());
			}
			nbtTag.setIntArray("Rituals", ritualIDs);
			nbtTag.setByteArray("RitualStages", stages);
			nbtTag.setTag("Initiators", nbtList);
			nbtTag.setTag("Locations", nbtLocationList);
			nbtTag.setByteArray("RitualCovens", covenSizes);
		}

		public void readFromNBT(NBTTagCompound nbtTag) {
			super.readFromNBT(nbtTag);
			if(nbtTag.hasKey("Rituals") && nbtTag.hasKey("RitualStages")) {
				byte[] stages = nbtTag.getByteArray("RitualStages");
				int[] ritualIDs = nbtTag.getIntArray("Rituals");
				Coord[] locations = new Coord[stages.length];
				if(nbtTag.hasKey("Locations")) {
					NBTTagList initators = nbtTag.getTagList("Locations", 10);
					for(int covenSizes = 0; covenSizes < Math.min(initators.tagCount(), locations.length); ++covenSizes) {
						NBTTagCompound i = initators.getCompoundTagAt(covenSizes);
						locations[covenSizes] = Coord.fromTagNBT(i);
					}
				}
				String[] var11 = new String[stages.length];
				if(nbtTag.hasKey("Initiators")) {
					NBTTagList var12 = nbtTag.getTagList("Initiators", 8);
					for(int var13 = 0; var13 < Math.min(var12.tagCount(), var11.length); ++var13) {
						String ritual = var12.getStringTagAt(var13);
						var11[var13] = ((ritual != null && !ritual.isEmpty()) ? ritual : null);
					}
				}
				byte[] var14 = nbtTag.hasKey("RitualCovens") ? nbtTag.getByteArray("RitualCovens") : null;
				for(int var13 = 0; var13 < ritualIDs.length; ++var13) {
					ElderRiteRegistry.Ritual var15 = ElderRiteRegistry.instance().getRitual(ritualIDs[var13]);
					if(var15 != null) {
						ArrayList<RitualStep> ritualSteps = new ArrayList<RitualStep>();
						var15.addRiteSteps(ritualSteps, stages[var13]);
						if(!ritualSteps.isEmpty()) {
							BlockElderRitual.TileEntityCircle.ActivatedElderRitual ActivatedElderRitual = new BlockElderRitual.TileEntityCircle.ActivatedElderRitual(var15, ritualSteps, var11[var13], (var14 != null) ? var14[var13] : 0, null);
							ActivatedElderRitual.setLocation(locations[var13]);
							upkeepRituals.add(ActivatedElderRitual);
						}
					}
				}
			}
		}

		@Override
		public void updateEntity() {
			super.updateEntity();
			if(!worldObj.isRemote) {
				if(!upkeepRituals.isEmpty()) {
					for(BlockElderRitual.TileEntityCircle.ActivatedElderRitual result: upkeepRituals) {
						RitualStep.Result stepResult = result.steps.get(0)
																   .elderRun(worldObj, xCoord, yCoord, zCoord, ticks, result);
						if(stepResult != RitualStep.Result.UPKEEP && Config.instance().traceRites()) {
							Log.instance()
							   .traceRite(String.format(" - Upkeep ritual=%s, step=%s, result=%s", result.ritual.getUnlocalizedName(), result.steps.get(0)
																																				   .toString(), stepResult.toString()));
						}
						switch(SyntheticClass_1.$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[stepResult.ordinal()]) {
							case 1: {
								result.steps.clear();
								continue;
							}
							case 2:
							case 3: {
								result.steps.clear();
								SoundEffect.NOTE_SNARE.playAt(this);
								continue;
							}
						}
					}
					for(int var5 = upkeepRituals.size() - 1; var5 >= 0; --var5) {
						if(upkeepRituals.get(var5).steps.isEmpty()) {
							upkeepRituals.remove(var5);
						}
					}
				}
				if(!activeRituals.isEmpty()) {
					BlockElderRitual.TileEntityCircle.ActivatedElderRitual var6 = activeRituals.get(0);
					ElderRitualStep.Result var7 = var6.steps.get(0)
															.elderRun(worldObj, xCoord, yCoord, zCoord, ticks, var6);
					var6.postProcess(worldObj);
					if(abortNext) {
						abortNext = false;
						var7 = ElderRitualStep.Result.ABORTED_REFUND;
						activeRituals.clear();
					}
					if(var7 != ElderRitualStep.Result.STARTING && Config.instance().traceRites()) {
						Log.instance()
						   .traceRite(String.format("Active ritual=%s, step=%s, result=%s", var6.ritual.getUnlocalizedName(), var6.steps.get(0)
																																		.toString(), var7.toString()));
					}
					switch(SyntheticClass_1.$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[var7.ordinal()]) {
						case 1: {
							if(var6.steps.size() > 0) {
								var6.steps.remove(0);
							}
							if(var6.steps.isEmpty()) {
								activeRituals.remove(0);
								break;
							}
							break;
						}
						case 2:
						case 3: {
							if(activeRituals.size() > 0) {
								activeRituals.remove(0);
							}
							if(worldObj.isRemote) {
								break;
							}
							SoundEffect.NOTE_SNARE.playAt(this);
							if(var7 == RitualStep.Result.ABORTED_REFUND) {
								for(RitualStep.SacrificedItem sacrificedItem: var6.sacrificedItems) {
									worldObj.spawnEntityInWorld(new EntityItem(worldObj, 0.5 + sacrificedItem.location.x, 0.5 + sacrificedItem.location.y, 0.5 + sacrificedItem.location.z, sacrificedItem.itemstack));
								}
								break;
							}
							break;
						}
						case 4: {
							if(activeRituals.size() > 0) {
								activeRituals.remove(0);
							}
							upkeepRituals.add(var6);
							break;
						}
					}
				}
				if(!isRitualActive() && getBlockMetadata() != 0) {
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
				}
			}
		}

		public void deactivate() {
			if(!worldObj.isRemote) {
				if(activeRituals.size() > 0) {
					abortNext = true;
				}
				upkeepRituals.clear();
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
				SoundEffect.NOTE_SNARE.playAt(this);
			}
		}

		public boolean isRitualActive() {
			return !activeRituals.isEmpty() || !upkeepRituals.isEmpty();
		}

		public void queueRitual(ElderRiteRegistry.Ritual ritual, AxisAlignedBB bounds, EntityPlayer player, int covenSize, boolean summonCoven) {
			ArrayList ritualSteps = new ArrayList();
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
			private final ArrayList<ElderRitualStep> steps;
			public final String playerName;
			public final ArrayList<RitualStep.SacrificedItem> sacrificedItems;
			public final int covenSize;
			private Coord coord;

			private ActivatedElderRitual(ElderRiteRegistry.Ritual ritual, ArrayList<ElderRitualStep> steps, String playerName, int covenSize) {
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

			ActivatedElderRitual(ElderRiteRegistry.Ritual x0, ArrayList x1, String x2, int x3, SyntheticClass_1 x4) {
				this(x0, x1, x2, x3);
			}
		}
	}
}