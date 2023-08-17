package com.pyding.deathlyhallows.items;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.item.ItemMutator;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.MutableBlock;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.pyding.deathlyhallows.DeathHallowsMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.IWandFocus;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.foci.ItemFocusPech;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InferioisMutandis extends ItemFocusPech {
    IIcon depthIcon = null;
    private static final AspectList cost;
    private static final AspectList costAll;
    public static FocusUpgradeType nightshade;

    @Override
    public void registerIcons(IIconRegister ir) {
        this.icon = ir.registerIcon("dh:focus_mutant");
        this.depthIcon = ir.registerIcon("thaumcraft:focus_pech_depth");
    }

    public InferioisMutandis() {

    }

    @Override
    public int getFocusColor(ItemStack itemstack) {
        return 65344;
    }

    public IIcon getDepthIcon() {
        return depthIcon;
    }

    @Override
    public IIcon getFocusDepthLayerIcon(ItemStack itemstack) {
        return depthIcon;
    }
    public static boolean mutator = false;

    @Override
    public ItemStack onFocusRightClick(ItemStack itemstack, World world, EntityPlayer player, MovingObjectPosition mob) {
        if(world.isRemote)
            return null;
        ItemWandCasting wand = (ItemWandCasting)itemstack.getItem();
        if(!player.isSneaking()) {
            MovingObjectPosition rayTrace = rayTrace(player, 4.0);
            if (rayTrace != null) {
                ItemStack falseStack;
                if (rayTrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && wand.consumeAllVis(itemstack, player, this.getVisCost(itemstack), false, false)) {
                    int blockX = rayTrace.blockX;
                    int blockY = rayTrace.blockY;
                    int blockZ = rayTrace.blockZ;
                    if (mutator) {
                        ItemMutator itemMutator = new ItemMutator();
                        falseStack = new ItemStack(DeathHallowsMod.inferioisMutandis);
                        if (itemMutator.onItemUseFirst(falseStack, player, world, blockX, blockY, blockZ, rayTrace.sideHit, 0, 0, 0)) {
                            wand.consumeAllVis(itemstack, player, this.getVisCost(itemstack), true, false);
                            ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, world, blockX, blockY, blockZ, 1.0, 1.0, 8);
                        }
                    } else {
                        falseStack = Witchery.Items.GENERIC.itemMutandis.createStack();
                        if(useMutandis(false,falseStack,player,world,blockX,blockY,blockZ)){
                            wand.consumeAllVis(itemstack, player, this.getVisCost(itemstack), true, false);
                            ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, world, blockX, blockY, blockZ, 1.0, 1.0, 8);
                        }
                    }
                }
            }
        } else {
            if(!mutator)
            mutator = true;
            else mutator = false;
            world.playSoundAtEntity(player,SoundEffect.RANDOM_ORB.toString(),1,1);
        }
        System.out.println(mutator);
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        super.addInformation(stack, player, list, par4);
        if (I18n.format("dh.util.language").equals("Ru")) {
            list.add("Мутандис и Мутирующий Сук за вис? Наверное полезно...");
            list.add("Шифт+ПКМ чтобы сменить режим");
            list.add("§aПропахло резким запахом Щипуна");
        } else {
            list.add("Mutandis and Mutating Sprig with a cost of vis? Useful maybe...");
            list.add("Shift+RMB to change mode");
            list.add("§aStrongly saturated with the smell of Grassper");
        }
    }

    public boolean hasEffect(final ItemStack par1ItemStack, final int pass) {
        return mutator;
    }

    @Override
    public AspectList getVisCost(ItemStack itemstack) {
        return this.isUpgradedWith(itemstack, nightshade) ? costAll : cost;
    }

    static {
        cost = (new AspectList()).add(Aspect.ORDER, 500).add(Aspect.ENTROPY, 500);
        costAll = (new AspectList()).add(Aspect.AIR, 10).add(Aspect.FIRE, 10).add(Aspect.EARTH, 10).add(Aspect.ORDER, 10).add(Aspect.ENTROPY, 10).add(Aspect.WATER, 10);
        nightshade = new FocusUpgradeType(15, new ResourceLocation("thaumcraft", "textures/foci/nightshade.png"), "focus.upgrade.nightshade.name", "focus.upgrade.nightshade.text", (new AspectList()).add(Aspect.LIFE, 1).add(Aspect.POISON, 1).add(Aspect.MAGIC, 1));
    }

    public MovingObjectPosition rayTrace(EntityPlayer player, double distance) {
        Vec3 startVec = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 lookVec = player.getLook(1.0F);
        Vec3 endVec = startVec.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);
        return player.worldObj.rayTraceBlocks(startVec, endVec);
    }

    private boolean useMutandis(final boolean extremis, final ItemStack itemstack, final EntityPlayer player, final World world, final int posX, final int posY, final int posZ) {
        if (!world.isRemote) {
            final Block block = world.getBlock(posX, posY, posZ);
            final Block blockAbove = world.getBlock(posX, posY + 1, posZ);
            if (!extremis || (block != Blocks.grass && block != Blocks.mycelium)) {
                if (!extremis || block != Blocks.dirt || (blockAbove != Blocks.water && blockAbove != Blocks.flowing_water)) {
                    final int metadata = world.getBlockMetadata(posX, posY, posZ);
                    ArrayList<MutableBlock> list;
                    if (block == Blocks.flower_pot && metadata > 0) {
                        final MutableBlock[] blocks = { new MutableBlock(Blocks.flower_pot, 1), new MutableBlock(Blocks.flower_pot, 2), new MutableBlock(Blocks.flower_pot, 3), new MutableBlock(Blocks.flower_pot, 4), new MutableBlock(Blocks.flower_pot, 5), new MutableBlock(Blocks.flower_pot, 6), new MutableBlock(Blocks.flower_pot, 7), new MutableBlock(Blocks.flower_pot, 8), new MutableBlock(Blocks.flower_pot, 9), new MutableBlock(Blocks.flower_pot, 10), new MutableBlock(Blocks.flower_pot, 11) };
                        list = new ArrayList<MutableBlock>(Arrays.asList(blocks));
                    }
                    else {
                        final MutableBlock[] blocks = { new MutableBlock(Blocks.sapling, 0), new MutableBlock(Blocks.sapling, 1), new MutableBlock(Blocks.sapling, 2), new MutableBlock(Blocks.sapling, 3), new MutableBlock(Blocks.sapling, 4), new MutableBlock(Blocks.sapling, 5), new MutableBlock(Witchery.Blocks.SAPLING, 0), new MutableBlock(Witchery.Blocks.SAPLING, 1), new MutableBlock(Witchery.Blocks.SAPLING, 2), new MutableBlock(Witchery.Blocks.EMBER_MOSS, 0), new MutableBlock((Block)Blocks.tallgrass, 1), new MutableBlock(Blocks.waterlily), new MutableBlock((Block)Blocks.brown_mushroom), new MutableBlock((Block)Blocks.red_mushroom), new MutableBlock((Block)Blocks.red_flower, 0), new MutableBlock((Block)Blocks.yellow_flower), new MutableBlock(Witchery.Blocks.SPANISH_MOSS, 1) };
                        list = new ArrayList<MutableBlock>(Arrays.asList(blocks));
                        for (final String extra : Config.instance().mutandisExtras) {
                            try {
                                list.add(new MutableBlock(extra));
                            }
                            catch (Throwable t) {}
                        }
                        if (extremis) {
                            final MutableBlock[] extremisBlocks = { new MutableBlock(Blocks.carrots, -1, Math.min(metadata, 7)), new MutableBlock(Blocks.potatoes, -1, Math.min(metadata, 7)), new MutableBlock(Blocks.wheat, -1, Math.min(metadata, 7)), new MutableBlock(Blocks.reeds, -1, Math.min(metadata, 7)), new MutableBlock((Block)Witchery.Blocks.CROP_BELLADONNA, -1, Math.min(metadata, Witchery.Blocks.CROP_BELLADONNA.getNumGrowthStages())), new MutableBlock((Block)Witchery.Blocks.CROP_MANDRAKE, -1, Math.min(metadata, Witchery.Blocks.CROP_MANDRAKE.getNumGrowthStages())), new MutableBlock((Block)Witchery.Blocks.CROP_ARTICHOKE, -1, Math.min(metadata, Witchery.Blocks.CROP_ARTICHOKE.getNumGrowthStages())), new MutableBlock(Blocks.pumpkin_stem, -1, Math.min(metadata, 7)), new MutableBlock(Blocks.cactus), new MutableBlock(Blocks.melon_stem, -1, Math.min(metadata, 7)), new MutableBlock(Blocks.nether_wart, -1, Math.min(metadata, 3)) };
                            list.addAll(Arrays.asList(extremisBlocks));
                        }
                        else if (player.dimension == Config.instance().dimensionDreamID) {
                            final MutableBlock[] spiritBlocks = { new MutableBlock(Blocks.nether_wart, -1, 3) };
                            list.addAll(Arrays.asList(spiritBlocks));
                        }
                    }
                    final MutableBlock mutableBlock = new MutableBlock(block, metadata, 0);
                    final int index = list.indexOf(mutableBlock);
                    if (index != -1) {
                        list.remove(index);
                        list.get(world.rand.nextInt(list.size())).mutate(world, posX, posY, posZ);
                        ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, world, posX, posY, posZ, 1.0, 1.0, 16);
                        --itemstack.stackSize;
                    }
                }
                else {
                    if (world.rand.nextInt(2) == 0) {
                        setBlockToClay(world, posX, posY, posZ);
                        setBlockToClay(world, posX + 1, posY, posZ);
                        setBlockToClay(world, posX - 1, posY, posZ);
                        setBlockToClay(world, posX, posY, posZ + 1);
                        setBlockToClay(world, posX, posY, posZ - 1);
                    }
                    else {
                        ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, world, posX, posY + 1, posZ, 1.0, 1.0, 16);
                    }
                    --itemstack.stackSize;
                }
            }
            else {
                if (world.rand.nextInt(2) == 0) {
                    world.setBlock(posX, posY, posZ, (Block)((block == Blocks.grass) ? Blocks.mycelium : Blocks.grass));
                }
                ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, world, posX, posY + 1, posZ, 1.0, 1.0, 16);
                --itemstack.stackSize;
            }
        }
        return true;
    }
    public static void setBlockToClay(final World world, final int x, final int y, final int z) {
        final Block block = world.getBlock(x, y, z);
        final Block blockAbove = world.getBlock(x, y + 1, z);
        if (block == Blocks.dirt && (blockAbove == Blocks.water || blockAbove == Blocks.flowing_water)) {
            world.setBlock(x, y, z, Blocks.clay);
            if (!world.isRemote) {
                ParticleEffect.INSTANT_SPELL.send(SoundEffect.MOB_SLIME_BIG, world, 0.5 + x, 1.5 + y, 0.5 + z, 1.0, 1.0, 16);
            }
        }
    }
}
