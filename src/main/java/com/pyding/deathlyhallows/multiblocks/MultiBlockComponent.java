package com.pyding.deathlyhallows.multiblocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import java.util.Set;

public class MultiBlockComponent {

	public ChunkCoordinates relPos;
	public final Block block;
	public int meta;
	public final NBTTagCompound tag;
	public final TileEntity tileEntity;
	private final BlockMetaRotator axisToRotation;
	public boolean doFancyRender;

	public MultiBlockComponent(ChunkCoordinates relPos, Block block, int meta) {
		this(relPos, block, meta, true, null, null, null);
	}

	public MultiBlockComponent(ChunkCoordinates relPos, Block block, int meta, NBTTagCompound tag) {
		this(relPos, block, meta, true, null, tag, null);
	}

	public MultiBlockComponent(ChunkCoordinates relPos, Block block, int meta, BlockMetaRotator axisToRotation) {
		this(relPos, block, meta, true, null, null, axisToRotation);
	}
	
	public MultiBlockComponent(ChunkCoordinates relPos, Block block, int meta, boolean doFancyRender, TileEntity tileEntity, NBTTagCompound tag, BlockMetaRotator axisToRotation) {
		this.relPos = relPos;
		this.block = block;
		this.meta = meta;
		this.tileEntity = tileEntity;
		this.tag = tag;
		this.axisToRotation = axisToRotation;
		this.doFancyRender = doFancyRender;
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
		if(world.getBlock(x, y, z) != getBlock() || (meta != -1 && world.getBlockMetadata(x, y, z) != meta)) {
			return false;
		}
		if(tag == null) {
			return true;
		}
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile == null) {
			return false;
		}
		NBTTagCompound tiletag = new NBTTagCompound();
		tile.writeToNBT(tiletag);
		return areTagsEqual(tag, tiletag);
	}

	public ItemStack[] getMaterials() {
		if(block instanceof BlockStairs || block instanceof BlockSlab) {
			return new ItemStack[]{new ItemStack(block, 1, -1)};
		}
		int meta = this.meta == -1 ? 0 : this.meta;
		ItemStack ret;
		if(Item.getItemFromBlock(block) == null) {
			ret = new ItemStack(block.getItem(null, 0, 0, 0), 1, meta);
		}
		else {
			ret = new ItemStack(block, 1, meta);
		}
		if(!ret.getHasSubtypes()) {
			ret.setItemDamage(0);
		}
		return new ItemStack[] {ret};
	}

	public void rotate(int intAngle) { // from 0 to 3
		int 
				x = relPos.posX,
				z = relPos.posZ,
				sin = intSin(intAngle),
				cos = intSin(intAngle + 1);
		relPos = new ChunkCoordinates(x * cos - z * sin, relPos.posY, x * sin + z * cos);
		if(axisToRotation != null) {
			meta = axisToRotation.rotate(meta, intAngle);
		}
	}
	
	private int intSin(int intAngle) {
		intAngle &= 3;
		return intAngle == 0 ? 0 : 2 - intAngle;
	}

	public MultiBlockComponent copy() {
		return new MultiBlockComponent(relPos, block, meta, doFancyRender, tileEntity, tag, axisToRotation);
	}

	public TileEntity getTileEntity() {
		return tileEntity;
	}

	public boolean equals(MultiBlockComponent other) {
		return other != null && relPos.equals(other.relPos) && block.equals(other.block) && meta == other.meta;
	}

	@SuppressWarnings("unchecked")
	public boolean areTagsEqual(NBTTagCompound recipe, NBTTagCompound match) {
		if(recipe == null || match == null) {
			return false;
		}
		for(String key: (Set<String>)recipe.func_150296_c()) {
			NBTBase rTag = recipe.getTag(key);
			NBTBase mTag = match.getTag(key);
			if(rTag instanceof NBTTagCompound && mTag instanceof NBTTagCompound
					&& !areTagsEqual((NBTTagCompound)rTag, (NBTTagCompound)mTag)
			) {
				return false;
			}
			if(!rTag.equals(mTag)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() { // json parsable
		return "{\"pos\": [" + relPos.posX + "," + relPos.posY +","+ relPos.posZ + "], \"block\": \"" + block.getUnlocalizedName() + "\"" + (meta != 0 ? ", \"meta\": " + meta : "") + (tag != null ? ", \"tag\": " + tag : "") + "}";
	}

	@FunctionalInterface
	public interface BlockMetaRotator {
		int rotate(int meta, int intAngle);
	}
	
}
