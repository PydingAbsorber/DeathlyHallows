package com.pyding.deathlyhallows.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.blocks.BlockGrassper;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.ritual.rites.RiteEclipse;
import com.emoniph.witchery.ritual.rites.RiteWeatherCallStorm;
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
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
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

import java.util.ArrayList;
import java.util.Random;

public class ElderRitualBlock extends BlockBaseContainer
{
	public ElderRitualBlock() {
		super(Material.vine, ElderRitualBlock.TileEntityCircle.class);
		this.registerWithCreateTab = false;
		this.setHardness(3.0f);
		this.setResistance(1000.0f);
		final float f1 = 0.015625f;
		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.015625f, 1.0f);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int par1, final int par2) {
		return this.blockIcon;
	}

	public void onBlockClicked(final World world, final int posX, final int posY, final int posZ, final EntityPlayer player) {
		if (!world.isRemote) {
			final ItemStack itemstack = player.getHeldItem();
			if (itemstack != null && (Witchery.Items.GENERIC.itemBroom.isMatch(itemstack) || Witchery.Items.GENERIC.itemBroomEnchanted.isMatch(itemstack))) {
				world.func_147480_a(posX, posY, posZ, false);
			}
		}
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(final World par1World, final int par2, final int par3, final int par4) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int quantityDropped(final Random rand) {
		return 0;
	}

	public ItemStack getPickBlock(final MovingObjectPosition target, final World world, final int x, final int y, final int z) {
		return new ItemStack(Witchery.Items.CHALK_GOLDEN);
	}

	public void onNeighborBlockChange(final World par1World, final int par2, final int par3, final int par4, final Block par5) {
		if (this.func_111046_k(par1World, par2, par3, par4)) {
			final boolean flag = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
			final BlockCircle.TileEntityCircle tileCircle = BlockUtil.getTileEntity((IBlockAccess)par1World, par2, par3, par4, BlockCircle.TileEntityCircle.class);
			if (tileCircle != null && tileCircle.previousRedstoneState != flag) {
				if (flag) {
					this.activateBlock(par1World, par2, par3, par4, null, false);
				}
				tileCircle.previousRedstoneState = flag;
			}
		}
	}

	private boolean func_111046_k(final World par1World, final int par2, final int par3, final int par4) {
		if (!this.canBlockStay(par1World, par2, par3, par4)) {
			par1World.setBlockToAir(par2, par3, par4);
			return false;
		}
		return true;
	}

	public boolean canBlockStay(final World world, final int x, final int y, final int z) {
		final Material material = world.getBlock(x, y - 1, z).getMaterial();
		return !world.isAirBlock(x, y - 1, z) && material != null && material.isOpaque() && material.isSolid();
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4, final int par5) {
		return par5 == 1;
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random rand) {
		final int metadata = world.getBlockMetadata(x, y, z);
		if (metadata == 1) {
			final double d0 = x + 0.4f + rand.nextFloat() * 0.2f;
			final double d2 = y + 0.1f + rand.nextFloat() * 0.3f;
			final double d3 = z + 0.4f + rand.nextFloat() * 0.2f;
			world.spawnParticle(ParticleEffect.REDDUST.toString(), d0, d2, d3, 0.0, 0.0, 0.0);
		}
	}

	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX, final float hitY, final float hitZ) {
		final ItemStack stack = player.getHeldItem();
		this.activateBlock(world, x, y, z, player, stack != null && Witchery.Items.GENERIC.itemSeerStone.isMatch(stack));
		return true;
	}

	private void activateBlock(final World world, final int posX, final int posY, final int posZ, final EntityPlayer player, boolean summonCoven) {
		final ElderRitualBlock.TileEntityCircle tileEntity = BlockUtil.getTileEntity((IBlockAccess)world, posX, posY, posZ, ElderRitualBlock.TileEntityCircle.class);
		if (tileEntity != null) {
			if (tileEntity.isRitualActive()) {
				tileEntity.deactivate();
			}
			else if (!world.isRemote) {
				if (!PowerSources.instance().isAreaNulled(world, posX, posY, posZ) && world.provider.dimensionId != Config.instance().dimensionDreamID) {
//                    final Circle a = new Circle(16);
//                    final Circle b = new Circle(28);
//                    final Circle c = new Circle(40);
//                    final Circle d = new Circle(0);
//                    final Circle[][] PATTERN = { { d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d }, { d, d, d, d, d, c, c, c, c, c, c, c, d, d, d, d, d }, { d, d, d, d, c, d, d, d, d, d, d, d, c, d, d, d, d }, { d, d, d, c, d, d, b, b, b, b, b, d, d, c, d, d, d }, { d, d, c, d, d, b, d, d, d, d, d, b, d, d, c, d, d }, { d, c, d, d, b, d, d, a, a, a, d, d, b, d, d, c, d }, { d, c, d, b, d, d, a, d, d, d, a, d, d, b, d, c, d }, { d, c, d, b, d, a, d, d, d, d, d, a, d, b, d, c, d }, { d, c, d, b, d, a, d, d, d, d, d, a, d, b, d, c, d }, { d, c, d, b, d, a, d, d, d, d, d, a, d, b, d, c, d }, { d, c, d, b, d, d, a, d, d, d, a, d, d, b, d, c, d }, { d, c, d, d, b, d, d, a, a, a, d, d, b, d, d, c, d }, { d, d, c, d, d, b, d, d, d, d, d, b, d, d, c, d, d }, { d, d, d, c, d, d, b, b, b, b, b, d, d, c, d, d, d }, { d, d, d, d, c, d, d, d, d, d, d, d, c, d, d, d, d }, { d, d, d, d, d, c, c, c, c, c, c, c, d, d, d, d, d }, { d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d } };
//                    final int offsetZ = (PATTERN.length - 1) / 2;
//                    for (int isDaytime = 0; isDaytime < PATTERN.length - 1; ++isDaytime) {
//                        final int isRainPossible = posZ - offsetZ + isDaytime;
//                        final int isRaining = (PATTERN[isDaytime].length - 1) / 2;
//                        for (int isThundering = 0; isThundering < PATTERN[isDaytime].length; ++isThundering) {
//                            final int maxRadius = posX - isRaining + isThundering;
//                            PATTERN[PATTERN.length - 1 - isDaytime][isThundering].addGlyph(world, maxRadius, posY, isRainPossible);
//                        }
//                    }
					final boolean isDaytime2 = world.isDaytime();
					final boolean var30 = world.getBiomeGenForCoords(posX, posZ).canSpawnLightningBolt();
					final boolean isRaining2 = world.isRaining() && var30;
					final boolean isThundering2 = world.isThundering();
					final int maxRadius = 7;//PATTERN.length / 2;
					final AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)(posX - maxRadius), (double)(posY - maxRadius), (double)(posZ - maxRadius), (double)(posX + maxRadius), (double)(posY + maxRadius), (double)(posZ + maxRadius));
					final ArrayList<Entity> entities = (ArrayList<Entity>)Lists.newArrayList((Iterable)world.getEntitiesWithinAABB((Class)Entity.class, bounds));
					final ArrayList<ItemStack> grassperStacks = new ArrayList<ItemStack>();
					final boolean var31 = true;
					for (int x = posX - 5; x <= posX + 5; ++x) {
						for (int z = posZ - 5; z <= posZ + 5; ++z) {
							final Block block = world.getBlock(x, posY, z);
							if (block == Witchery.Blocks.GRASSPER) {
								final TileEntity tile = world.getTileEntity(x, posY, z);
								if (tile instanceof BlockGrassper.TileEntityGrassper) {
									final BlockGrassper.TileEntityGrassper ritual = (BlockGrassper.TileEntityGrassper)tile;
									final ItemStack stack = ritual.getStackInSlot(0);
									if (stack != null) {
										grassperStacks.add(stack);
									}
								}
							}
						}
					}
					//final Circle[] nearbyCircles = { a, b, c };
					boolean success = false;
					final int covenSize = summonCoven ? EntityCovenWitch.getCovenSize(player) : 0;
					for (ElderRiteRegistry.Ritual o : ElderRiteRegistry.instance().getRituals()) {
						ElderRiteRegistry.Ritual ritual2 = o;
						if (ritual2.isMatch(world, posX, posY, posZ, entities, grassperStacks, isDaytime2, isRaining2, isThundering2)) {
							/*if (ritual2.rite instanceof RiteWeatherCallStorm || ritual2.rite instanceof RiteEclipse) {
								final ExtendedPlayer exPlayer = ExtendedPlayer.get(player);
							}*/
							tileEntity.queueRitual(ritual2, bounds, player, covenSize, summonCoven);
							summonCoven = false;
							success = true;
						}
					}
					if (!success && !world.isRemote) {
						RiteRegistry.RiteError("witchery.rite.unknownritual", player, world);
						SoundEffect.NOTE_SNARE.playAt(world, posX, posY, posZ);
					}
				}
				else {
					ChatUtil.sendTranslated(EnumChatFormatting.RED, (ICommandSender)player, "witchery.rite.nullfield", new Object[0]);
					SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
				}
			}
		}
	}

	static class SyntheticClass_1
	{
		static final int[] $SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result;

		static {
			$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result = new int[RitualStep.Result.values().length];
			try {
				SyntheticClass_1.$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[RitualStep.Result.COMPLETED.ordinal()] = 1;
			}
			catch (NoSuchFieldError noSuchFieldError) {}
			try {
				SyntheticClass_1.$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[RitualStep.Result.ABORTED.ordinal()] = 2;
			}
			catch (NoSuchFieldError noSuchFieldError2) {}
			try {
				SyntheticClass_1.$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[RitualStep.Result.ABORTED_REFUND.ordinal()] = 3;
			}
			catch (NoSuchFieldError noSuchFieldError3) {}
			try {
				SyntheticClass_1.$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[RitualStep.Result.UPKEEP.ordinal()] = 4;
			}
			catch (NoSuchFieldError noSuchFieldError4) {}
			try {
				SyntheticClass_1.$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[RitualStep.Result.STARTING.ordinal()] = 5;
			}
			catch (NoSuchFieldError noSuchFieldError5) {}
		}
	}

	public static class TileEntityCircle extends TileEntityBase
	{
		public boolean previousRedstoneState;
		private final ArrayList<ElderRitualBlock.TileEntityCircle.ActivatedElderRitual> activeRituals;
		private final ArrayList<ElderRitualBlock.TileEntityCircle.ActivatedElderRitual> upkeepRituals;
		private boolean abortNext;

		public TileEntityCircle() {
			this.activeRituals = new ArrayList<ElderRitualBlock.TileEntityCircle.ActivatedElderRitual>();
			this.upkeepRituals = new ArrayList<ElderRitualBlock.TileEntityCircle.ActivatedElderRitual>();
			this.abortNext = false;
		}

		public void writeToNBT(final NBTTagCompound nbtTag) {
			super.writeToNBT(nbtTag);
			final int[] ritualIDs = new int[this.upkeepRituals.size()];
			final byte[] stages = new byte[this.upkeepRituals.size()];
			final byte[] covenSizes = new byte[this.upkeepRituals.size()];
			final NBTTagList nbtList = new NBTTagList();
			final NBTTagList nbtLocationList = new NBTTagList();
			for (int i = 0; i < this.upkeepRituals.size(); ++i) {
				ritualIDs[i] = this.upkeepRituals.get(i).ritual.getRitualID();
				stages[i] = (byte)this.upkeepRituals.get(i).getCurrentStage();
				covenSizes[i] = (byte)this.upkeepRituals.get(i).covenSize;
				nbtList.appendTag((NBTBase)new NBTTagString(this.upkeepRituals.get(i).getInitiatingPlayerName()));
				nbtLocationList.appendTag((NBTBase)this.upkeepRituals.get(i).getLocationTag());
			}
			nbtTag.setIntArray("Rituals", ritualIDs);
			nbtTag.setByteArray("RitualStages", stages);
			nbtTag.setTag("Initiators", (NBTBase)nbtList);
			nbtTag.setTag("Locations", (NBTBase)nbtLocationList);
			nbtTag.setByteArray("RitualCovens", covenSizes);
		}

		public void readFromNBT(final NBTTagCompound nbtTag) {
			super.readFromNBT(nbtTag);
			if (nbtTag.hasKey("Rituals") && nbtTag.hasKey("RitualStages")) {
				final byte[] stages = nbtTag.getByteArray("RitualStages");
				final int[] ritualIDs = nbtTag.getIntArray("Rituals");
				final Coord[] locations = new Coord[stages.length];
				if (nbtTag.hasKey("Locations")) {
					final NBTTagList initators = nbtTag.getTagList("Locations", 10);
					for (int covenSizes = 0; covenSizes < Math.min(initators.tagCount(), locations.length); ++covenSizes) {
						final NBTTagCompound i = initators.getCompoundTagAt(covenSizes);
						locations[covenSizes] = Coord.fromTagNBT(i);
					}
				}
				final String[] var11 = new String[stages.length];
				if (nbtTag.hasKey("Initiators")) {
					final NBTTagList var12 = nbtTag.getTagList("Initiators", 8);
					for (int var13 = 0; var13 < Math.min(var12.tagCount(), var11.length); ++var13) {
						final String ritual = var12.getStringTagAt(var13);
						var11[var13] = ((ritual != null && !ritual.isEmpty()) ? ritual : null);
					}
				}
				final byte[] var14 = (byte[])(nbtTag.hasKey("RitualCovens") ? nbtTag.getByteArray("RitualCovens") : null);
				for (int var13 = 0; var13 < ritualIDs.length; ++var13) {
					final ElderRiteRegistry.Ritual var15 = ElderRiteRegistry.instance().getRitual(ritualIDs[var13]);
					if (var15 != null) {
						final ArrayList<RitualStep> ritualSteps = new ArrayList<RitualStep>();
						var15.addRiteSteps(ritualSteps, stages[var13]);
						if (!ritualSteps.isEmpty()) {
							final ElderRitualBlock.TileEntityCircle.ActivatedElderRitual ActivatedElderRitual = new ElderRitualBlock.TileEntityCircle.ActivatedElderRitual(var15, ritualSteps, var11[var13], (var14 != null) ? var14[var13] : 0, null);
							ActivatedElderRitual.setLocation(locations[var13]);
							this.upkeepRituals.add(ActivatedElderRitual);
						}
					}
				}
			}
		}

		@Override
		public void updateEntity() {
			super.updateEntity();
			if (!this.worldObj.isRemote) {
				if (!this.upkeepRituals.isEmpty()) {
					for (final ElderRitualBlock.TileEntityCircle.ActivatedElderRitual result : this.upkeepRituals) {
						final RitualStep.Result i$ = result.steps.get(0).elderRun(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.ticks, result);
						if (i$ != RitualStep.Result.UPKEEP && Config.instance().traceRites()) {
							Log.instance().traceRite(String.format(" - Upkeep ritual=%s, step=%s, result=%s", result.ritual.getUnlocalizedName(), result.steps.get(0).toString(), i$.toString()));
						}
						switch (SyntheticClass_1.$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[i$.ordinal()]) {
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
					for (int var5 = this.upkeepRituals.size() - 1; var5 >= 0; --var5) {
						if (this.upkeepRituals.get(var5).steps.isEmpty()) {
							this.upkeepRituals.remove(var5);
						}
					}
				}
				if (!this.activeRituals.isEmpty()) {
					final ElderRitualBlock.TileEntityCircle.ActivatedElderRitual var6 = this.activeRituals.get(0);
					ElderRitualStep.Result var7 = var6.steps.get(0).elderRun(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.ticks, var6);
					var6.postProcess(this.worldObj);
					if (this.abortNext) {
						this.abortNext = false;
						var7 = ElderRitualStep.Result.ABORTED_REFUND;
						this.activeRituals.clear();
					}
					if (var7 != ElderRitualStep.Result.STARTING && Config.instance().traceRites()) {
						Log.instance().traceRite(String.format("Active ritual=%s, step=%s, result=%s", var6.ritual.getUnlocalizedName(), var6.steps.get(0).toString(), var7.toString()));
					}
					switch (SyntheticClass_1.$SwitchMap$com$emoniph$witchery$ritual$RitualStep$Result[var7.ordinal()]) {
						case 1: {
							if (var6.steps.size() > 0) {
								var6.steps.remove(0);
							}
							if (var6.steps.isEmpty()) {
								this.activeRituals.remove(0);
								break;
							}
							break;
						}
						case 2:
						case 3: {
							if (this.activeRituals.size() > 0) {
								this.activeRituals.remove(0);
							}
							if (this.worldObj.isRemote) {
								break;
							}
							SoundEffect.NOTE_SNARE.playAt(this);
							if (var7 == RitualStep.Result.ABORTED_REFUND) {
								for (final RitualStep.SacrificedItem sacrificedItem : var6.sacrificedItems) {
									this.worldObj.spawnEntityInWorld((Entity)new EntityItem(this.worldObj, 0.5 + sacrificedItem.location.x, 0.5 + sacrificedItem.location.y, 0.5 + sacrificedItem.location.z, sacrificedItem.itemstack));
								}
								break;
							}
							break;
						}
						case 4: {
							if (this.activeRituals.size() > 0) {
								this.activeRituals.remove(0);
							}
							this.upkeepRituals.add(var6);
							break;
						}
					}
				}
				if (!this.isRitualActive() && this.getBlockMetadata() != 0) {
					this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 0, 3);
				}
			}
		}

		public void deactivate() {
			if (!this.worldObj.isRemote) {
				if (this.activeRituals.size() > 0) {
					this.abortNext = true;
				}
				this.upkeepRituals.clear();
				this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 0, 3);
				SoundEffect.NOTE_SNARE.playAt(this);
			}
		}

		public boolean isRitualActive() {
			return !this.activeRituals.isEmpty() || !this.upkeepRituals.isEmpty();
		}

		public void queueRitual(final ElderRiteRegistry.Ritual ritual, final AxisAlignedBB bounds, final EntityPlayer player, final int covenSize, final boolean summonCoven) {
			final ArrayList ritualSteps = new ArrayList();
			if (summonCoven) {
				EntityCovenWitch.summonCoven(ritualSteps, player.worldObj, player, new int[][] { { this.xCoord - 2, this.yCoord, this.zCoord - 2 }, { this.xCoord + 2, this.yCoord, this.zCoord - 2 }, { this.xCoord - 2, this.yCoord, this.zCoord + 2 }, { this.xCoord + 2, this.yCoord, this.zCoord + 2 }, { this.xCoord, this.yCoord, this.zCoord + 3 }, { this.xCoord, this.yCoord, this.zCoord - 3 } });
			}
			ritual.addSteps(ritualSteps, bounds);
			if (!ritualSteps.isEmpty() && !this.worldObj.isRemote) {
				this.activeRituals.add(new ElderRitualBlock.TileEntityCircle.ActivatedElderRitual(ritual, ritualSteps, (player != null) ? player.getCommandSenderName() : null, covenSize, null));
				this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 1, 3);
			}
		}

		public Packet getDescriptionPacket() {
			final NBTTagCompound nbtTag = new NBTTagCompound();
			this.writeToNBT(nbtTag);
			return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
		}

		public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity packet) {
			super.onDataPacket(net, packet);
			this.readFromNBT(packet.func_148857_g());
			this.worldObj.func_147479_m(this.xCoord, this.yCoord, this.zCoord);
		}

		public static class ActivatedElderRitual
		{
			public final ElderRiteRegistry.Ritual ritual;
			private final ArrayList<ElderRitualStep> steps;
			public final String playerName;
			public final ArrayList<RitualStep.SacrificedItem> sacrificedItems;
			public final int covenSize;
			private Coord coord;

			private ActivatedElderRitual(final ElderRiteRegistry.Ritual ritual, final ArrayList<ElderRitualStep> steps, final String playerName, final int covenSize) {
				this.sacrificedItems = new ArrayList<RitualStep.SacrificedItem>();
				this.ritual = ritual;
				this.steps = steps;
				this.playerName = playerName;
				this.covenSize = covenSize;
			}

			public Coord getLocation() {
				return this.coord;
			}

			public NBTTagCompound getLocationTag() {
				return (this.coord != null) ? this.coord.toTagNBT() : new NBTTagCompound();
			}

			public void setLocation(final Coord coord) {
				this.coord = coord;
			}

			public String getInitiatingPlayerName() {
				return (this.playerName != null) ? this.playerName : "";
			}

			public EntityPlayer getInitiatingPlayer(final World world) {
				return world.getPlayerEntityByName(this.getInitiatingPlayerName());
			}

			public void postProcess(final World world) {
				for (int i = 0; i < this.sacrificedItems.size(); ++i) {
					final RitualStep.SacrificedItem sacrificedItem = this.sacrificedItems.get(i);
					if (sacrificedItem != null && sacrificedItem.itemstack != null) {
						if (!this.ritual.isConsumeAttunedStoneCharged() && Witchery.Items.GENERIC.itemAttunedStoneCharged.isMatch(sacrificedItem.itemstack)) {
							world.spawnEntityInWorld((Entity)new EntityItem(world, 0.5 + sacrificedItem.location.x, 0.5 + sacrificedItem.location.y, 0.5 + sacrificedItem.location.z, Witchery.Items.GENERIC.itemAttunedStone.createStack()));
							this.sacrificedItems.remove(i);
							break;
						}
						if (sacrificedItem.itemstack.getItem() == Witchery.Items.ARTHANA) {
							world.spawnEntityInWorld((Entity)new EntityItem(world, 0.5 + sacrificedItem.location.x, 0.5 + sacrificedItem.location.y, 0.5 + sacrificedItem.location.z, sacrificedItem.itemstack));
							this.sacrificedItems.remove(i);
							break;
						}
						if (sacrificedItem.itemstack.getItem() == Witchery.Items.BOLINE) {
							world.spawnEntityInWorld((Entity)new EntityItem(world, 0.5 + sacrificedItem.location.x, 0.5 + sacrificedItem.location.y, 0.5 + sacrificedItem.location.z, sacrificedItem.itemstack));
							this.sacrificedItems.remove(i);
							break;
						}
						if (!this.ritual.isConsumeNecroStone() && Witchery.Items.GENERIC.itemNecroStone.isMatch(sacrificedItem.itemstack)) {
							world.spawnEntityInWorld((Entity)new EntityItem(world, 0.5 + sacrificedItem.location.x, 0.5 + sacrificedItem.location.y, 0.5 + sacrificedItem.location.z, sacrificedItem.itemstack));
							this.sacrificedItems.remove(i);
							break;
						}
					}
				}
			}

			public int getCurrentStage() {
				return this.steps.isEmpty() ? 0 : this.steps.get(0).getCurrentStage();
			}

			ActivatedElderRitual(final ElderRiteRegistry.Ritual x0, final ArrayList x1, final String x2, final int x3, final SyntheticClass_1 x4) {
				this(x0, x1, x2, x3);
			}
		}
	}
}