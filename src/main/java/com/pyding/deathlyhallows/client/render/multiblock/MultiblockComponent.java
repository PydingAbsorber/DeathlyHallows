package com.pyding.deathlyhallows.client.render.multiblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MultiblockComponent {

    public ChunkCoordinates relPos;
    public final Block block;
    public final int meta;
    public final NBTTagCompound tag;
    public final TileEntity tileEntity;
    public boolean doFancyRender;

    public MultiblockComponent(ChunkCoordinates relPos, Block block, int meta) {
        this(relPos, block, meta, (NBTTagCompound) null);
    }

    public MultiblockComponent(ChunkCoordinates relPos, Block block, int meta, NBTTagCompound tag) {
        this(relPos, block, meta, false, null, tag);
    }

    public MultiblockComponent(ChunkCoordinates relPos, Block block, int meta, boolean doFancyRender) {
        this(relPos, block, meta, doFancyRender, null, null);
    }

    public MultiblockComponent(ChunkCoordinates relPos, Block block, int meta, TileEntity tileEntity) {
        this(relPos, block, meta, block.hasTileEntity() == (tileEntity != null), tileEntity, null);
    }

    public MultiblockComponent(ChunkCoordinates relPos, Block block, int meta, boolean doFancyRender, TileEntity tileEntity, NBTTagCompound tag) {
        this.relPos = relPos;
        this.block = block;
        this.meta = meta;
        this.tileEntity = tileEntity;
        this.doFancyRender = doFancyRender;
        this.tag = tag;
    }

    public ChunkCoordinates getRelativePosition() {
        return relPos;
    }

    public Block getBlock() {
        return block;
    }

    public int getMeta() {
        return meta;
    }

    public boolean matches(World world, int x, int y, int z) {
        if (world.getBlock(x, y, z) == getBlock() && (meta == -1 || world.getBlockMetadata(x, y, z) == meta)) {
            if (tag == null) return true;
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile == null) return false;
            NBTTagCompound tiletag = new NBTTagCompound();
            tile.writeToNBT(tiletag);
            if(areTagsEqual(tag,tiletag)) return true;
        }
        return false;
    }

    public ItemStack[] getMaterials() {
        return new ItemStack[]{new ItemStack(block, 1, meta)};
    }

    public void rotate(double angle) {
        double x = relPos.posX;
        double z = relPos.posZ;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        double xn = x * cos - z * sin;
        double zn = x * sin + z * cos;
        relPos = new ChunkCoordinates((int) Math.round(xn), relPos.posY, (int) Math.round(zn));
    }

    public MultiblockComponent copy() {
        return new MultiblockComponent(relPos, block, meta,doFancyRender, tileEntity,tag);
    }

    public TileEntity getTileEntity() {
        return tileEntity;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldDoFancyRender() {
        return doFancyRender;
    }

    public boolean equals(MultiblockComponent other) {
        if (other == null) return false;
        return relPos.equals(other.relPos) && block.equals(other.block) && meta == other.meta;
    }

    public boolean areTagsEqual(NBTTagCompound recipe, NBTTagCompound match){
        boolean flag=true;
        if(recipe==null || match==null)return false;
        for(Object keyObj:recipe.func_150296_c()){
            String key=(String)keyObj;
            if(recipe.getTag(key) instanceof NBTTagCompound && match.getTag(key) instanceof NBTTagCompound) {
                if (!areTagsEqual((NBTTagCompound) recipe.getTag(key), (NBTTagCompound) match.getTag(key))) {
                    flag = false;
                }
            }
            if(!recipe.getTag(key).equals(match.getTag(key))) {
                flag = false;
            }
        }
        return flag;
    }
}
